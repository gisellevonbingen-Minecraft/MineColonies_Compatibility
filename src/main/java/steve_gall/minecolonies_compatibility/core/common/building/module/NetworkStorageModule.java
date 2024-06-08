package steve_gall.minecolonies_compatibility.core.common.building.module;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.minecolonies.api.colony.ICitizenData;
import com.minecolonies.api.colony.buildings.modules.IBuildingEventsModule;
import com.minecolonies.api.colony.requestsystem.request.IRequest;
import com.minecolonies.api.colony.requestsystem.requestable.IDeliverable;
import com.minecolonies.api.entity.citizen.AbstractEntityCitizen;
import com.minecolonies.api.util.ItemStackUtils;
import com.minecolonies.api.util.Tuple;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.IItemHandlerModifiable;
import steve_gall.minecolonies_compatibility.api.common.building.module.AbstractModuleWithExternalWorkingBlocks;
import steve_gall.minecolonies_compatibility.api.common.entity.pathfinding.PathJobFindWorkingBlocks;
import steve_gall.minecolonies_compatibility.api.common.entity.pathfinding.WorkingBlocksPathResult;
import steve_gall.minecolonies_compatibility.module.common.ModuleManager;
import steve_gall.minecolonies_compatibility.module.common.refinedstorage.CitizenGridBlockEntity;

public class NetworkStorageModule extends AbstractModuleWithExternalWorkingBlocks implements IBuildingEventsModule
{
	private boolean isDestroyed;

	public void onItemIncremented(ItemStack stack)
	{
		this.getBuilding().getColony().getRequestManager().onColonyUpdate(request -> this.testDeliverable(request, stack));
	}

	private boolean testDeliverable(IRequest<?> request, ItemStack stack)
	{
		return request.getRequest() instanceof IDeliverable deliverable && deliverable.matches(stack);
	}

	@Override
	public boolean isWorkingBlock(@NotNull LevelReader level, @NotNull BlockPos pos, @NotNull BlockState state)
	{
		var blockEntity = level.getBlockEntity(pos);
		var view = resolveView(blockEntity);

		if (view != null)
		{
			return true;
		}

		return false;
	}

	public Stream<BlockPos> getExtractableBlocks()
	{
		return this.getWorkingBlocks().filter(this::canExtract);
	}

	public boolean canExtract(@NotNull BlockPos pos)
	{
		var view = this.getView(pos);

		if (view != null)
		{
			return view.isActive() && view.canExtract();
		}

		return false;
	}

	public Stream<BlockPos> getInsertableBlocks()
	{
		return this.getWorkingBlocks().filter(this::canInsert);
	}

	public boolean canInsert(@NotNull BlockPos pos)
	{
		var view = this.getView(pos);

		if (view != null)
		{
			return view.isActive() && view.canInsert();
		}

		return false;
	}

	public boolean hasMatchingItemStack(ItemStack itemStack, int count, boolean ignoreNBT, boolean ignoreDamage, int leftOver)
	{
		var totalCountFound = 0 - leftOver;

		for (var pos : this.getExtractableBlocks().toList())
		{
			var view = this.getView(pos);

			if (view != null)
			{
				for (var stack : view.getMatchingStacks(stack -> ItemStackUtils.compareItemStacksIgnoreStackSize(itemStack, stack, !ignoreDamage, !ignoreNBT)))
				{
					totalCountFound += stack.getCount();

					if (totalCountFound >= count)
					{
						return true;
					}

				}

			}

		}

		return false;
	}

	public List<Tuple<ItemStack, BlockPos>> getMatchingItemStacks(Predicate<ItemStack> predicate)
	{
		var list = new ArrayList<Tuple<ItemStack, BlockPos>>();

		for (var pos : this.getExtractableBlocks().toList())
		{
			var view = this.getView(pos);

			if (view != null)
			{
				for (var stack : view.getMatchingStacks(predicate))
				{
					list.add(new Tuple<>(stack, pos));
				}

			}

		}

		return list;
	}

	public void dump(IItemHandlerModifiable itemHandler)
	{
		var blocks = this.getInsertableBlocks().toList();

		for (var block : blocks)
		{
			var view = this.getView(block);

			if (view == null)
			{
				continue;
			}

			for (var i = 0; i < itemHandler.getSlots(); i++)
			{
				var stack = itemHandler.getStackInSlot(i);

				if (stack.isEmpty())
				{
					continue;
				}

				var remained = view.insert(itemHandler, stack);
				itemHandler.setStackInSlot(i, remained);
			}

		}

	}

	@Override
	public void serializeToView(FriendlyByteBuf buf)
	{
		super.serializeToView(buf);

		buf.writeCollection(this.getWorkingBlocks().toList(), FriendlyByteBuf::writeBlockPos);
	}

	public void onLink(INetworkStorageView view)
	{
		this.addWorkingBlock(view.getPos());
	}

	public void onUnlink(INetworkStorageView view)
	{
		this.removeWorkingBlock(view.getPos());
	}

	@Override
	protected @Nullable AbstractEntityCitizen getPathFindingCitizen()
	{
		var citizens = this.building.getAllAssignedCitizen();
		return citizens.stream().findAny().flatMap(ICitizenData::getEntity).orElse(null);
	}

	@Override
	protected void onWorkingBlockAdded(@NotNull BlockPos pos)
	{
		super.onWorkingBlockAdded(pos);

		this.link(pos);
	}

	@Override
	protected void onWorkingBlockRemoved(@Nullable BlockPos pos)
	{
		super.onWorkingBlockRemoved(pos);

		this.unlink(pos);
	}

	@Nullable
	private INetworkStorageView getView(@NotNull BlockPos pos)
	{
		var level = this.building.getColony().getWorld();
		return this.getView(level, pos);
	}

	@Nullable
	private INetworkStorageView getView(LevelReader level, @NotNull BlockPos pos)
	{
		var blockEntity = level.getBlockEntity(pos);
		var view = resolveView(blockEntity);

		if (view != null)
		{
			if (view.getPairedModule() == null)
			{
				view.link(this);
				return view;
			}
			else if (view.getPairedModule() == this)
			{
				return view;
			}

		}

		this.removeWorkingBlock(pos);
		return null;
	}

	@Nullable
	public static INetworkStorageView resolveView(BlockEntity blockEntity)
	{
		if (ModuleManager.RS.isLoaded())
		{
			if (blockEntity instanceof CitizenGridBlockEntity grid)
			{
				return grid.getNode();
			}

		}

		return null;
	}

	private void link(BlockPos pos)
	{
		var level = this.building.getColony().getWorld();
		var blockEntity = level.getBlockEntity(pos);
		var view = resolveView(blockEntity);

		if (view != null && view.getPairedModule() == null)
		{
			view.link(this);
		}

	}

	private void unlink(BlockPos pos)
	{
		var view = this.getView(pos);

		if (view != null && view.getPairedModule() == this)
		{
			view.unlink();
		}

	}

	@Override
	public void onDestroyed()
	{
		this.isDestroyed = true;
		this.getRegisteredBlocks().forEach(this::unlink);
	}

	public boolean isDestroyed()
	{
		return this.isDestroyed;
	}

	@Override
	public WorkingBlocksPathResult createPathResult(@Nullable AbstractEntityCitizen citizen)
	{
		return new WorkingBlocksPathResult(this)
		{
			@Override
			public boolean test(@NotNull PathJobFindWorkingBlocks<?> job, @NotNull BlockPos.MutableBlockPos pos)
			{
				return super.test(job, pos) || super.test(job, pos.setY(pos.getY() + 1));
			}
		};
	}

}
