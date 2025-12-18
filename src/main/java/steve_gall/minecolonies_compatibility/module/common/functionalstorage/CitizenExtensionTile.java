package steve_gall.minecolonies_compatibility.module.common.functionalstorage;

import java.util.Collections;
import java.util.stream.Stream;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.buuz135.functionalstorage.block.config.FunctionalStorageConfig;
import com.buuz135.functionalstorage.block.tile.ControllableDrawerTile;
import com.buuz135.functionalstorage.block.tile.StorageControllerTile;
import com.hrznstudio.titanium.block.BasicTileBlock;
import com.hrznstudio.titanium.component.inventory.InventoryComponent;
import com.hrznstudio.titanium.util.TileUtil;
import com.minecolonies.api.util.WorldUtil;

import it.unimi.dsi.fastutil.objects.Object2LongMap.Entry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import steve_gall.minecolonies_compatibility.api.common.building.module.INetworkStorageView;
import steve_gall.minecolonies_compatibility.core.common.block.entity.IAccessDirectionHolder;
import steve_gall.minecolonies_compatibility.core.common.block.entity.INetworkStorageViewHolder;
import steve_gall.minecolonies_compatibility.core.common.building.module.AccessDirection;
import steve_gall.minecolonies_compatibility.core.common.building.module.NetworkStorageModule;
import steve_gall.minecolonies_compatibility.core.common.building.module.QueueNetworkStorageView;
import steve_gall.minecolonies_compatibility.core.common.item.ItemHandlerHelper2;
import steve_gall.minecolonies_compatibility.core.common.item.ItemStackCounter;
import steve_gall.minecolonies_compatibility.core.common.item.ItemStackKey;
import steve_gall.minecolonies_compatibility.module.common.functionalstorage.init.ModuleItems;

public class CitizenExtensionTile extends ControllableDrawerTile<CitizenExtensionTile> implements INetworkStorageViewHolder, IAccessDirectionHolder
{
	private final StorageView view;
	private final ItemStackCounter counter;

	private AccessDirection accessDirection = AccessDirection.INSERT_EXTRACT;

	public CitizenExtensionTile(BasicTileBlock<CitizenExtensionTile> base, BlockEntityType<CitizenExtensionTile> entityType, BlockPos pos, BlockState state)
	{
		super(base, entityType, pos, state);

		this.view = new StorageView();
		this.counter = new ItemStackCounter();
		this.counter.addListener(this::onCounterChanged);
	}

	@Override
	public int getBaseSize(int arg0)
	{
		return 0;
	}

	@Override
	public int getStorageSlotAmount()
	{
		return 0;
	}

	@Override
	public InventoryComponent<ControllableDrawerTile<CitizenExtensionTile>> getStorageUpgradesConstructor()
	{
		return new InventoryComponent<ControllableDrawerTile<CitizenExtensionTile>>("storage_upgrades", 10, 70, getStorageSlotAmount())
		{
			@NotNull
			@Override
			public ItemStack extractItem(int slot, int amount, boolean simulate)
			{
				return ItemStack.EMPTY;
			}
		}.setInputFilter((stack, integer) ->
		{
			return false;
		}).setOnSlotChanged((stack, integer) ->
		{
			setNeedsUpgradeCache(true);
		}).setSlotLimit(1);
	}

	@Override
	public CitizenExtensionTile getSelf()
	{
		return this;
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
			this.view.requestAll();
			this.setChanged();
		}

	}

	@Override
	public void setChanged()
	{
		var level = this.getLevel();

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
		var controller = this.getController();

		if (controller == null)
		{
			return Collections.emptyList();
		}

		var counter = new ItemStackCounter();
		var inventoryHandler = controller.inventoryHandler;

		for (var i = 0; i < inventoryHandler.getSlots(); i++)
		{
			var stack = inventoryHandler.getStackInSlot(i);

			if (stack == null || stack.isEmpty())
			{
				continue;
			}

			var key = new ItemStackKey(stack);
			counter.insert(key, stack.getCount());
		}

		return counter.entrySet();
	}

	public StorageControllerTile<?> getController()
	{
		var pos = this.getControllerPos();

		if (pos == null)
		{
			return null;
		}

		return TileUtil.getTileEntity(this.level, pos, StorageControllerTile.class).orElse(null);
	}

	@Override
	public void serverTick(Level level, BlockPos pos, BlockState state, CitizenExtensionTile self)
	{
		super.serverTick(level, pos, state, self);

		if (level.getGameTime() % FunctionalStorageConfig.UPGRADE_TICK == 0)
		{
			this.update();
		}

		this.view.onTick();
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
			return new ItemStack(ModuleItems.CITIZEN_EXTENSION.get());
		}

		@Override
		public boolean isActive()
		{
			return getControllerPos() != null;
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
			var controller = getController();

			if (controller == null)
			{
				return ItemStack.EMPTY;
			}

			return ItemHandlerHelper2.extractItem(controller.inventoryHandler, stack, simulate);
		}

		@Override
		public @NotNull ItemStack insertItem(@NotNull ItemStack stack, boolean simulate)
		{
			var controller = getController();

			if (controller == null)
			{
				return stack;
			}

			return ItemHandlerHelper2.insertItem(controller.inventoryHandler, stack, simulate);
		}

	}

}
