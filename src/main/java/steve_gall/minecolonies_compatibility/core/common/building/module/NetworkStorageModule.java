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
import com.minecolonies.api.colony.buildings.modules.ICreatesResolversModule;
import com.minecolonies.api.colony.requestsystem.request.IRequest;
import com.minecolonies.api.colony.requestsystem.requestable.IDeliverable;
import com.minecolonies.api.colony.requestsystem.resolver.IRequestResolver;
import com.minecolonies.api.entity.citizen.AbstractEntityCitizen;
import com.minecolonies.api.util.ItemStackUtils;
import com.minecolonies.api.util.Tuple;
import com.minecolonies.api.util.constant.TypeConstants;

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
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.core.common.requestsystem.NetworkCraftingProductionResolver;
import steve_gall.minecolonies_compatibility.core.common.requestsystem.NetworkCraftingRequestResolver;
import steve_gall.minecolonies_compatibility.core.common.util.StreamUtils;

public class NetworkStorageModule extends AbstractModuleWithExternalWorkingBlocks implements IBuildingEventsModule, ICreatesResolversModule
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

	@Override
	public List<IRequestResolver<?>> createResolvers()
	{
		var location = this.getBuilding().getLocation();
		var factoryController = this.getBuilding().getColony().getRequestManager().getFactoryController();
		return Arrays.asList(//
				new NetworkCraftingRequestResolver(location, factoryController.getNewInstance(TypeConstants.ITOKEN)), //
				new NetworkCraftingProductionResolver(location, factoryController.getNewInstance(TypeConstants.ITOKEN))//
		);
	}

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
			return this.getOwnLinkedViews(pos).findAny().isPresent();
		}
		else
		{
			return getUnlinkedView(level, pos) != null;
		}

	}

	public Stream<INetworkStorageView> getBlocks()
	{
		return this.getRegisteredBlocks().stream().flatMap(this::getOwnLinkedViews);
	}

	public Stream<INetworkStorageView> getRequestableBlocks()
	{
		return this.getBlocks().filter(m -> canRequestable(m));
	}

	public static boolean canRequestable(@Nullable INetworkStorageView view)
	{
		return view != null && view.isActive() && view.canRequest();
	}

	public Stream<INetworkStorageView> getExtractableBlocks()
	{
		return this.getBlocks().filter(m -> canExtract(m));
	}

	public static boolean canExtract(@Nullable INetworkStorageView view)
	{
		return view != null && view.isActive() && view.canExtract();
	}

	public Stream<INetworkStorageView> getInsertableBlocks()
	{
		return this.getBlocks().filter(m -> canInsert(m));
	}

	public static boolean canInsert(@Nullable INetworkStorageView view)
	{
		return view != null && view.isActive() && view.canInsert();
	}

	public int getMatchingItemStackCount(ItemStack itemStack, int count, boolean ignoreNBT, boolean ignoreDamage, int leftOver)
	{
		return this.getMatchingItemStackCount(stack -> ItemStackUtils.compareItemStacksIgnoreStackSize(itemStack, stack, !ignoreDamage, !ignoreNBT), count, leftOver);
	}

	public int getMatchingItemStackCount(Predicate<ItemStack> predicate, int count, int leftOver)
	{
		var totalCountFound = 0 - leftOver;

		for (var stack : StreamUtils.toIterable(this.getExtractableBlocks().flatMap(view -> view.getAllStacks().filter(predicate))))
		{
			totalCountFound += stack.getCount();

			if (totalCountFound >= count)
			{
				return totalCountFound;
			}

		}

		return totalCountFound;
	}

	public boolean hasMatchingItemStack(ItemStack itemStack, int count, boolean ignoreNBT, boolean ignoreDamage, int leftOver)
	{
		return this.getMatchingItemStackCount(itemStack, count, ignoreNBT, ignoreDamage, leftOver) >= count;
	}

	public List<Tuple<ItemStack, BlockPos>> getMatchingItemStacks(Predicate<ItemStack> predicate, int limit)
	{
		var list = new ArrayList<Tuple<ItemStack, BlockPos>>();
		var remained = limit;

		for (var view : StreamUtils.toIterable(this.getExtractableBlocks()))
		{
			for (var stack : StreamUtils.toIterable(view.getAllStacks().filter(predicate)))
			{
				if (remained <= 0)
				{
					break;
				}

				for (var splited : this.split(stack, remained))
				{
					list.add(new Tuple<>(splited, view.getPos()));
					remained -= splited.getCount();
				}

			}

		}

		return list;
	}

	private List<ItemStack> split(ItemStack stack, int limit)
	{
		var list = new ArrayList<ItemStack>();
		var count = Math.min(stack.getCount(), limit);
		var maxStackSize = stack.getMaxStackSize();

		while (true)
		{
			if (count <= maxStackSize)
			{
				break;
			}

			list.add(stack.copyWithCount(maxStackSize));
			count -= maxStackSize;
		}

		list.add(stack.copyWithCount(count));
		return list;
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

				var remained = view.insertItem(stack.copy(), false);
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
	public Stream<INetworkStorageView> getOwnLinkedViews(BlockPos pos)
	{
		var level = this.building.getColony().getWorld();
		return getAllViews(level, pos, view -> view.getLinkedModule() == this);
	}

	private void link(BlockPos pos)
	{
		var level = this.building.getColony().getWorld();
		var view = getUnlinkedView(level, pos);

		if (view != null)
		{
			view.link(this);
		}

	}

	private void unlink(BlockPos pos)
	{
		this.getOwnLinkedViews(pos).forEach(view ->
		{
			view.unlink();
		});
	}

	public static Stream<INetworkStorageView> getAllViews(LevelReader level, BlockPos pos, Predicate<INetworkStorageView> predicate)
	{
		var blockEntity = level.getBlockEntity(pos);

		if (blockEntity == null)
		{
			return Stream.empty();
		}

		return VIEW_DIRECTIONS.stream().map(direction ->
		{
			return NetworkStorageViewRegistry.select(blockEntity, direction);
		}).filter(view ->
		{
			return view != null && (predicate == null || predicate.test(view));
		});
	}

	@Nullable
	public static INetworkStorageView getUnlinkedView(LevelReader level, BlockPos pos)
	{
		return getAllViews(level, pos, view -> view.getLinkedModule() == null).findAny().orElse(null);
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

}
