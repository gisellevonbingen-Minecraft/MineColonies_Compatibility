package steve_gall.minecolonies_compatibility.core.common.block.entity;

import java.util.ArrayList;
import java.util.stream.Stream;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.minecolonies.api.util.WorldUtil;

import it.unimi.dsi.fastutil.objects.Object2LongMap.Entry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;
import steve_gall.minecolonies_compatibility.api.common.building.module.INetworkStorageView;
import steve_gall.minecolonies_compatibility.core.common.building.module.AccessDirection;
import steve_gall.minecolonies_compatibility.core.common.building.module.NetworkStorageModule;
import steve_gall.minecolonies_compatibility.core.common.building.module.QueueNetworkStorageView;
import steve_gall.minecolonies_compatibility.core.common.init.ModBlockEntities;
import steve_gall.minecolonies_compatibility.core.common.init.ModItems;
import steve_gall.minecolonies_compatibility.core.common.item.CombinedItemHandler;
import steve_gall.minecolonies_compatibility.core.common.item.ItemHandlerHelper2;
import steve_gall.minecolonies_compatibility.core.common.item.ItemStackCounter;
import steve_gall.minecolonies_compatibility.core.common.item.ItemStackKey;

public class CommonNetworkStorageBlockEntity extends BlockEntity implements INetworkStorageViewHolder, IAccessDirectionHolder
{
	private final StorageView view;
	private final ItemStackCounter counter;

	private AccessDirection accessDirection = AccessDirection.INSERT_EXTRACT;

	public CommonNetworkStorageBlockEntity(BlockPos pos, BlockState state)
	{
		super(ModBlockEntities.COMMON_NETWORK_STORAGE.get(), pos, state);

		this.view = new StorageView();
		this.counter = new ItemStackCounter();
		this.counter.addListener(this::onCounterChanged);
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
		return this.saveWithId();
	}

	@Override
	public void load(CompoundTag compound)
	{
		super.load(compound);

		this.view.readLink(compound.getCompound("view"));
		this.accessDirection = AccessDirection.deserialize(compound.get("accessDirection"));
	}

	@Override
	protected void saveAdditional(CompoundTag compound)
	{
		super.saveAdditional(compound);

		compound.put("view", this.view.writeLink());
		compound.put("accessDirection", this.accessDirection.serialize());
	}

	@Override
	public @NotNull INetworkStorageView getNetworkStorageView()
	{
		return this.view;
	}

	@Override
	public @NotNull AccessDirection getAccessDirection()
	{
		return this.accessDirection;
	}

	@Override
	public void setAccessDirection(@NotNull AccessDirection value)
	{
		if (this.accessDirection != value)
		{
			this.accessDirection = value;
			this.setChanged();
		}

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

	public void update()
	{
		var stacks = this.getStacks();
		this.counter.replace(stacks);
	}

	private void onCounterChanged(ItemStackKey key, long oldCount, long newCount)
	{
		if (oldCount < newCount)
		{
			this.view.enqueue(key.getStack(newCount));
		}

	}

	private Iterable<Entry<ItemStackKey>> getStacks()
	{
		var handler = this.getCombinedHandler();
		var counter = new ItemStackCounter();

		for (var i = 0; i < handler.getSlots(); i++)
		{
			var stack = handler.getStackInSlot(i);

			if (stack == null || stack.isEmpty())
			{
				continue;
			}

			counter.insert(stack);
		}

		return counter.entrySet();
	}

	public IItemHandler getCombinedHandler()
	{
		var pos = this.getBlockPos();
		var handlers = new ArrayList<IItemHandler>();

		for (var direction : Direction.values())
		{
			var be = this.level.getBlockEntity(pos.relative(direction));

			if (be != null)
			{
				var cap = be.getCapability(ForgeCapabilities.ITEM_HANDLER, direction.getOpposite());

				if (cap != null && cap.isPresent())
				{
					var handler = cap.orElse(null);

					if (handler != null)
					{
						handlers.add(handler);
					}

				}

			}

		}

		return new CombinedItemHandler(handlers.toArray(IItemHandler[]::new));
	}

	public static void tick(Level level, BlockPos pos, BlockState state, CommonNetworkStorageBlockEntity self)
	{
		if (!level.isClientSide())
		{
			if (level.getGameTime() % 100 == 0)
			{
				self.update();
			}

			self.view.tick();
		}

	}

	public class StorageView extends QueueNetworkStorageView
	{
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
		public @Nullable Direction getDirection()
		{
			return null;
		}

		@Override
		public @NotNull ItemStack getIcon()
		{
			return new ItemStack(ModItems.COMMON_NETWORK_STORAGE.get());
		}

		@Override
		public boolean isActive()
		{
			return true;
		}

		@Override
		public boolean canExtract()
		{
			return getAccessDirection().canExtract();
		}

		@Override
		public boolean canInsert()
		{
			return getAccessDirection().canInsert();
		}

		@Override
		public @NotNull Stream<ItemStack> getAllStacks()
		{
			return counter.entrySet().stream().map(e -> e.getKey().getStack(e.getLongValue()));
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
		public @NotNull ItemStack extractItem(@NotNull ItemStack stack, boolean simulate)
		{
			return ItemHandlerHelper2.extractItem(getCombinedHandler(), stack, simulate);
		}

		@Override
		public @NotNull ItemStack insertItem(@NotNull ItemStack stack, boolean simulate)
		{
			return ItemHandlerHelper2.insertItem(getCombinedHandler(), stack, simulate);
		}

	}

}
