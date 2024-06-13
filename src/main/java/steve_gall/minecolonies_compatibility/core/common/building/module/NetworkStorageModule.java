package steve_gall.minecolonies_compatibility.core.common.building.module;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.IItemHandlerModifiable;
import steve_gall.minecolonies_compatibility.api.common.building.module.AbstractModuleWithExternalWorkingBlocks;
import steve_gall.minecolonies_compatibility.api.common.building.module.INetworkStorageView;
import steve_gall.minecolonies_compatibility.api.common.building.module.NetworkStorageViewRegistry;
import steve_gall.minecolonies_compatibility.api.common.entity.pathfinding.PathJobFindWorkingBlocks;
import steve_gall.minecolonies_compatibility.api.common.entity.pathfinding.WorkingBlocksPathResult;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.core.common.util.StreamUtils;

public class NetworkStorageModule extends AbstractModuleWithExternalWorkingBlocks implements IBuildingEventsModule
{
	public static final String TAG_POSTION_DIRECTIONS = MineColoniesCompatibility.rl("position_directions").toString();

	private static final List<Direction> VIEW_DIRECTIONS = new ArrayList<>();

	static
	{
		VIEW_DIRECTIONS.add(null);
		VIEW_DIRECTIONS.addAll(Arrays.asList(Direction.values()));
	}

	private boolean isDestroyed = false;
	private final Map<BlockPos, Direction> directions = new HashMap<>();

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
		if (this.containsWorkingBlock(pos))
		{
			return this.getView(level, pos) != null;
		}
		else
		{
			return resolveView(level, pos) != null;
		}

	}

	public Stream<INetworkStorageView> getExtractableBlocks()
	{
		return this.getRegisteredBlocks().stream().map(this::getView).filter(this::canExtract);
	}

	public boolean canExtract(@Nullable INetworkStorageView view)
	{
		return view != null && view.isActive() && view.canExtract();
	}

	public Stream<INetworkStorageView> getInsertableBlocks()
	{
		return this.getRegisteredBlocks().stream().map(this::getView).filter(this::canInsert);
	}

	public boolean canInsert(@Nullable INetworkStorageView view)
	{
		return view != null && view.isActive() && view.canInsert();
	}

	public boolean hasMatchingItemStack(ItemStack itemStack, int count, boolean ignoreNBT, boolean ignoreDamage, int leftOver)
	{
		var totalCountFound = 0 - leftOver;
		Predicate<ItemStack> predicate = stack -> ItemStackUtils.compareItemStacksIgnoreStackSize(itemStack, stack, !ignoreDamage, !ignoreNBT);

		for (var stack : StreamUtils.toIterable(this.getExtractableBlocks().flatMap(view -> view.getAllStacks().filter(predicate))))
		{
			totalCountFound += stack.getCount();

			if (totalCountFound >= count)
			{
				return true;
			}

		}

		return false;
	}

	public Stream<Tuple<ItemStack, BlockPos>> getMatchingItemStacks(Predicate<ItemStack> predicate)
	{
		return this.getExtractableBlocks().flatMap(view ->
		{
			return view.getAllStacks().filter(predicate).map(stack -> new Tuple<>(stack, view.getPos()));
		});
	}

	public void dump(IItemHandlerModifiable itemHandler)
	{
		for (var i = 0; i < itemHandler.getSlots(); i++)
		{
			for (var view : StreamUtils.toIterable(this.getInsertableBlocks()))
			{
				var stack = itemHandler.getStackInSlot(i);

				if (stack.isEmpty())
				{
					continue;
				}

				var remained = view.insertItem(stack, false);
				itemHandler.setStackInSlot(i, remained);
			}

		}

	}

	@Override
	public void deserializeNBT(CompoundTag compound)
	{
		super.deserializeNBT(compound);

		var directionsTag = compound.getList(TAG_POSTION_DIRECTIONS, Tag.TAG_COMPOUND);
		this.directions.clear();

		for (var i = 0; i < directionsTag.size(); i++)
		{
			var entryTag = directionsTag.getCompound(i);
			var pos = NbtUtils.readBlockPos(entryTag.getCompound("pos"));
			var direction = entryTag.getString("direction");
			this.directions.put(pos, Direction.byName(direction));
		}

	}

	@Override
	public void serializeNBT(@NotNull CompoundTag compound)
	{
		super.serializeNBT(compound);

		var directionsTag = new ListTag();
		compound.put(TAG_POSTION_DIRECTIONS, directionsTag);

		for (var entry : this.directions.entrySet())
		{
			var entryTag = new CompoundTag();
			entryTag.put("pos", NbtUtils.writeBlockPos(entry.getKey()));
			entryTag.putString("direction", entry.getValue().getSerializedName());
			directionsTag.add(entryTag);
		}

	}

	@Override
	public void serializeToView(FriendlyByteBuf buf)
	{
		super.serializeToView(buf);

		buf.writeCollection(this.getWorkingBlocks().toList(), FriendlyByteBuf::writeBlockPos);
		buf.writeCollection(this.directions.entrySet(), (buf2, data) ->
		{
			buf2.writeBlockPos(data.getKey());
			buf2.writeEnum(data.getValue());
		});
	}

	public void onLink(INetworkStorageView view)
	{
		this.addWorkingBlock(view.getPos());

		var direction = view.getDirection();

		if (direction != null)
		{
			this.directions.put(view.getPos(), direction);
		}

	}

	public void onUnlink(INetworkStorageView view)
	{
		this.removeWorkingBlock(view.getPos());
		this.directions.remove(view.getPos());
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
	public INetworkStorageView getView(@NotNull BlockPos pos)
	{
		var level = this.building.getColony().getWorld();
		var view = this.getView(level, pos);

		if (view == null)
		{
			this.removeWorkingBlock(pos);
		}

		return view;
	}

	@Nullable
	private INetworkStorageView getView(LevelReader level, BlockPos pos)
	{
		var blockEntity = level.getBlockEntity(pos);
		var direction = this.directions.get(pos);
		var view = NetworkStorageViewRegistry.select(blockEntity, direction);

		if (view != null)
		{
			if (view.getLinkedModule() == null)
			{
				view.link(this);
				return view;
			}
			else if (view.getLinkedModule() == this)
			{
				return view;
			}

		}

		return null;
	}

	private void link(BlockPos pos)
	{
		var level = this.building.getColony().getWorld();
		var view = resolveView(level, pos);

		if (view != null && view.getLinkedModule() == null)
		{
			view.link(this);
		}

	}

	public static INetworkStorageView resolveView(LevelReader level, BlockPos pos)
	{
		var blockEntity = level.getBlockEntity(pos);

		if (blockEntity == null)
		{
			return null;
		}

		for (var direction : VIEW_DIRECTIONS)
		{
			var view = NetworkStorageViewRegistry.select(blockEntity, direction);

			if (view != null)
			{
				return view;
			}

		}

		return null;
	}

	private void unlink(BlockPos pos)
	{
		var view = this.getView(pos);

		if (view != null && view.getLinkedModule() == this)
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
