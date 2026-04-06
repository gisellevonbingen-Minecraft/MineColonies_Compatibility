package steve_gall.minecolonies_compatibility.module.common.create;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.jetbrains.annotations.NotNull;

import com.minecolonies.api.colony.requestsystem.StandardFactoryController;
import com.minecolonies.api.colony.requestsystem.request.RequestState;
import com.minecolonies.api.colony.requestsystem.requestable.IDeliverable;
import com.minecolonies.api.colony.requestsystem.token.IToken;
import com.minecolonies.api.util.NBTUtils;
import com.minecolonies.api.util.WorldUtil;
import com.simibubi.create.content.logistics.BigItemStack;
import com.simibubi.create.content.logistics.packager.InventorySummary;
import com.simibubi.create.content.logistics.packagerLink.LogisticallyLinkedBehaviour.RequestType;
import com.simibubi.create.content.logistics.stockTicker.PackageOrderWithCrafts;
import com.simibubi.create.content.logistics.stockTicker.StockTickerBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import steve_gall.minecolonies_compatibility.api.common.building.module.INetworkStorageView;
import steve_gall.minecolonies_compatibility.api.common.building.module.NetworkCraftingDestination;
import steve_gall.minecolonies_compatibility.core.common.block.entity.INetworkStorageViewHolder;
import steve_gall.minecolonies_compatibility.core.common.building.module.NetworkStorageModule;
import steve_gall.minecolonies_compatibility.core.common.building.module.QueueNetworkStorageView;
import steve_gall.minecolonies_compatibility.core.common.item.ItemStackCounter;
import steve_gall.minecolonies_compatibility.core.common.item.ItemStackKey;
import steve_gall.minecolonies_compatibility.core.common.item.WrappedItemHandler;
import steve_gall.minecolonies_compatibility.module.common.create.init.ModuleBlockEntities;
import steve_gall.minecolonies_compatibility.module.common.create.init.ModuleBlocks;

public class CitizenStockKeeperBlockEntity extends BlockEntity implements INetworkStorageViewHolder
{
	public static final String TAG_LINK = "link";
	public static final String TAG_DATA = "data";
	public static final String TAG_ADDRESS = "address";

	private final StorageView view;
	private String address = "";

	public CitizenStockKeeperBlockEntity(BlockPos pos, BlockState state)
	{
		super(ModuleBlockEntities.CITIZEN_STOCK_KEEPER.get(), pos, state);

		this.view = new StorageView();
	}

	protected void onInventoryInserted(int slot, ItemStack item)
	{
		for (var entry : this.view.tasks.entrySet())
		{
			var holder = entry.getValue();

			if (holder.requested.isEmpty() || holder.insertedCount >= holder.requestCount)
			{
				continue;
			}
			else
			{
				var requestedCount = holder.requested.getCountOf(item);

				if (requestedCount > 0)
				{
					holder.insertedCount += item.getCount();
					this.setChanged();
					return;
				}

			}

		}

	}

	public static void tick(Level level, BlockPos pos, BlockState state, CitizenStockKeeperBlockEntity self)
	{
		if (!level.isClientSide())
		{
			self.view.tick();
		}

	}

	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side)
	{
		if (cap == ForgeCapabilities.ITEM_HANDLER)
		{
			if (!this.getLevel().isClientSide())
			{
				var module = this.view.getLinkedModule();

				if (module != null)
				{
					var internal = module.getBuilding().getCapability(cap, side);

					if (internal.isPresent())
					{
						var handler = (IItemHandler) internal.orElse(null);

						return LazyOptional.of(() ->
						{
							return new WrappedItemHandler(handler)
							{
								@Override
								public ItemStack insertItem(int slot, ItemStack stack, boolean simulate)
								{
									var remaining = super.insertItem(slot, stack, simulate);

									if (!simulate && stack.getCount() > remaining.getCount())
									{
										onInventoryInserted(slot, stack.copyWithCount(stack.getCount() - remaining.getCount()));
									}

									return remaining;
								}

								@Override
								public ItemStack extractItem(int slot, int amount, boolean simulate)
								{
									return ItemStack.EMPTY;
								}
							};
						}).cast();
					}
					else
					{
						return internal;
					}

				}

			}

		}

		return super.getCapability(cap, side);
	}

	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket()
	{
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@NotNull
	@Override
	public CompoundTag getUpdateTag()
	{
		var tag = new CompoundTag();
		tag.put(TAG_LINK, this.view.writeLink());
		tag.putString(TAG_ADDRESS, this.address);
		return tag;
	}

	@Override
	public void load(CompoundTag tag)
	{
		super.load(tag);

		if (tag.contains(TAG_LINK))
		{
			this.view.readLink(tag.getCompound(TAG_LINK));
		}

		if (tag.contains(TAG_DATA))
		{
			this.view.readData(tag.getCompound(TAG_DATA));
		}

		if (tag.contains(TAG_ADDRESS))
		{
			this.address = tag.getString(TAG_ADDRESS);
		}

	}

	@Override
	protected void saveAdditional(CompoundTag tag)
	{
		super.saveAdditional(tag);

		tag.put(TAG_LINK, this.view.writeLink());
		tag.put(TAG_DATA, this.view.writeData());
		tag.putString(TAG_ADDRESS, this.address);
	}

	@Override
	public void setChanged()
	{
		var level = this.level;

		if (level != null)
		{
			WorldUtil.markChunkDirty(level, this.getBlockPos());
		}

		super.setChanged();
	}

	@Override
	public INetworkStorageView getNetworkStorageView()
	{
		return this.view;
	}

	public String getAddress()
	{
		return this.address;
	}

	public void setAddress(String address)
	{
		if (!this.address.equals(address))
		{
			this.address = address;
			this.setChanged();
		}

	}

	private class StorageView extends QueueNetworkStorageView
	{
		private final Map<IToken<?>, TaskHolder> tasks = new HashMap<>();
		private final ItemStackCounter counter = new ItemStackCounter();
		private StockTickerBlockEntity lastStockTicker;

		public StorageView()
		{
			this.counter.addListener(this::onCounterChanged);
		}

		private void onCounterChanged(ItemStackKey key, long oldCount, long newCount)
		{
			if (oldCount < newCount)
			{
				this.enqueue(key.getStack(newCount));
			}

		}

		@Override
		public Level getLevel()
		{
			return level;
		}

		@Override
		public BlockPos getPos()
		{
			return worldPosition;
		}

		@Override
		public Direction getDirection()
		{
			return null;
		}

		@Override
		public @NotNull ItemStack getIcon()
		{
			return new ItemStack(ModuleBlocks.CITIZEN_STOCK_KEEPER.get());
		}

		@Override
		public void tick()
		{
			this.lastStockTicker = this.getStockTicker();

			if (this.getLevel().getGameTime() % 100 == 0)
			{
				if (this.lastStockTicker != null)
				{
					var map = new ItemStackCounter();

					for (var entry : this.lastStockTicker.getAccurateSummary().getStacks())
					{
						map.insert(new ItemStackKey(entry.stack), entry.count);
					}

					this.counter.replace(map.entrySet());
				}
				else
				{
					this.counter.replace(Collections.emptyList());
				}

			}

			super.tick();
		}

		@Override
		public boolean isActive()
		{
			return this.lastStockTicker != null;
		}

		@Override
		public boolean canExtract()
		{
			return false;
		}

		@Override
		public NetworkCraftingDestination getNetworkCraftingDestination()
		{
			return NetworkCraftingDestination.BUILDING;
		}

		@Override
		public boolean canInsert()
		{
			return false;
		}

		@Override
		public Stream<ItemStack> getAllStacks()
		{
			if (this.lastStockTicker == null)
			{
				return Stream.empty();
			}

			return this.lastStockTicker.getAccurateSummary().getItemMap().values().stream().flatMap(List::stream).map(i -> i.stack);
		}

		@Override
		public ItemStack extractItem(ItemStack stack, boolean simulate)
		{
			return ItemStack.EMPTY;
		}

		@Override
		public ItemStack insertItem(ItemStack stack, boolean simulate)
		{
			return stack;
		}

		@Override
		public void link(NetworkStorageModule module)
		{
			super.link(module);

			setChanged();
		}

		@Override
		public void unlink()
		{
			super.unlink();

			setChanged();
		}

		@Override
		protected void onUnlink(NetworkStorageModule module)
		{
			super.onUnlink(module);

			var requestManager = module.getBuilding().getColony().getRequestManager();

			for (var requestId : new ArrayList<>(this.tasks.keySet()))
			{
				this.cancelAutocrafting(requestId);

				var request = requestManager.getRequestForToken(requestId);

				if (request == null)
				{
					continue;
				}

				requestManager.updateRequestState(requestId, RequestState.CANCELLED);
			}

		}

		private StockTickerBlockEntity getStockTicker()
		{
			var basePos = this.getPos();
			var level = this.getLevel();

			for (var direction : Direction.values())
			{
				var pos = basePos.relative(direction);

				if (level.getBlockEntity(pos) instanceof StockTickerBlockEntity blockEntity)
				{
					return blockEntity;
				}

			}

			return null;
		}

		private BigItemStack find(StockTickerBlockEntity stockTicker, IDeliverable deliverable)
		{
			var summary = stockTicker.getAccurateSummary();

			for (var entry : summary.getItemMap().entrySet())
			{
				for (var value : entry.getValue())
				{
					if (deliverable.matches(value.stack))
					{
						return value;
					}

				}

			}

			return null;
		}

		@Override
		public ItemStack calculateAutocrafting(IDeliverable deliverable)
		{
			var stockTicker = this.lastStockTicker;

			if (stockTicker != null)
			{
				var found = this.find(stockTicker, deliverable);

				if (found != null)
				{
					return found.stack.copyWithCount(1);
				}

			}

			return super.calculateAutocrafting(deliverable);
		}

		@Override
		public void createAutocrafting(IToken<?> requestId)
		{
			super.createAutocrafting(requestId);

			this.tasks.put(requestId, new TaskHolder());
			setChanged();
		}

		@Override
		public void cancelAutocrafting(@NotNull IToken<?> requestId)
		{
			super.cancelAutocrafting(requestId);

			var taskHolder = this.tasks.remove(requestId);

			if (taskHolder != null)
			{
				setChanged();
			}

		}

		@Override
		public void updateAutocraftings()
		{
			super.updateAutocraftings();

			var module = this.getLinkedModule();

			if (module == null)
			{
				return;
			}

			var stockTicker = this.lastStockTicker;

			if (stockTicker == null)
			{
				return;
			}

			var requestManager = module.getBuilding().getColony().getRequestManager();
			var toRemoveds = new ArrayList<IToken<?>>();
			var anyChanged = false;

			for (var entry : this.tasks.entrySet())
			{
				var requestId = entry.getKey();
				var holder = entry.getValue();
				var networkCrafting = this.getNetworkCrafting(requestManager, requestId);
				var deliverable = this.getDeliverable(requestManager, requestId);

				if (networkCrafting == null || deliverable == null)
				{
					toRemoveds.add(requestId);
					continue;
				}

				holder.requestCount = deliverable.getCount();

				if (!holder.requested.isEmpty() && holder.insertedCount >= holder.requestCount)
				{
					toRemoveds.add(requestId);
					continue;
				}

				var found = this.find(stockTicker, deliverable);

				if (found == null)
				{
					continue;
				}

				var stack = found.stack.copy();
				var requestableCount = Math.min(found.count, holder.requestCount - holder.requested.getTotalCount());
				var order = PackageOrderWithCrafts.simple(Collections.singletonList(new BigItemStack(stack, requestableCount)));
				var result = stockTicker.broadcastPackageRequest(RequestType.REDSTONE, order, null, address);

				if (result)
				{
					this.counter.extract(stack, requestableCount);
					holder.requested.add(stack, requestableCount);
					networkCrafting.setText(Component.literal("REQUESTED: " + holder.requested.getTotalCount() + "/" + holder.requestCount));
					anyChanged = true;
				}

			}

			if (toRemoveds.size() > 0)
			{
				for (var requestId : toRemoveds)
				{
					this.tasks.remove(requestId);
					requestManager.updateRequestState(requestId, RequestState.CANCELLED);
				}

				anyChanged = true;
			}

			if (anyChanged)
			{
				requestManager.markDirty();
				setChanged();
			}

		}

		@Override
		public void readData(CompoundTag tag)
		{
			super.readData(tag);

			var factoryController = StandardFactoryController.getInstance();
			this.tasks.clear();

			for (var taskTag : NBTUtils.streamCompound(tag.getList("tasks", Tag.TAG_COMPOUND)).toList())
			{
				IToken<?> requestId = factoryController.deserialize(taskTag.getCompound("requestId"));
				var holder = new TaskHolder(taskTag.getCompound("value"));
				this.tasks.put(requestId, holder);
			}

		}

		@Override
		public void writeData(CompoundTag tag)
		{
			super.writeData(tag);

			var factoryController = StandardFactoryController.getInstance();
			tag.put("tasks", this.tasks.entrySet().stream().map(entry ->
			{
				var taskTag = new CompoundTag();
				taskTag.put("requestId", factoryController.serialize(entry.getKey()));
				taskTag.put("value", entry.getValue().serializeTag());
				return taskTag;
			}).collect(NBTUtils.toListNBT()));

		}

	}

	private class TaskHolder
	{
		public final InventorySummary requested;
		public int requestCount;
		public int insertedCount;

		public TaskHolder()
		{
			this.requested = new InventorySummary();
			this.requestCount = 0;
			this.insertedCount = 0;
		}

		public TaskHolder(CompoundTag tag)
		{
			this.requested = InventorySummary.read(tag.getCompound("requested"));
			this.requestCount = tag.getInt("requestCount");
			this.insertedCount = tag.getInt("insertedCount");
		}

		public CompoundTag serializeTag()
		{
			var tag = new CompoundTag();
			tag.put("requested", this.requested.write());
			tag.putInt("requestCount", this.requestCount);
			tag.putInt("insertedCount", this.insertedCount);

			return tag;
		}

	}

}
