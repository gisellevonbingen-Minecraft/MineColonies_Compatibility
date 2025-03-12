package steve_gall.minecolonies_compatibility.module.common.storagedrawers;

import java.util.Collections;
import java.util.stream.Stream;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.jaquadro.minecraft.storagedrawers.api.capabilities.IItemRepository.ItemRecord;
import com.jaquadro.minecraft.storagedrawers.block.tile.BlockEntitySlave;
import com.minecolonies.api.util.WorldUtil;

import it.unimi.dsi.fastutil.objects.Object2LongMap.Entry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import steve_gall.minecolonies_compatibility.api.common.building.module.INetworkStorageView;
import steve_gall.minecolonies_compatibility.core.common.block.entity.BlockEntityExtension;
import steve_gall.minecolonies_compatibility.core.common.block.entity.IAccessDirectionHolder;
import steve_gall.minecolonies_compatibility.core.common.block.entity.INetworkStorageViewHolder;
import steve_gall.minecolonies_compatibility.core.common.building.module.AccessDirection;
import steve_gall.minecolonies_compatibility.core.common.building.module.NetworkStorageModule;
import steve_gall.minecolonies_compatibility.core.common.building.module.QueueNetworkStorageView;
import steve_gall.minecolonies_compatibility.core.common.item.ItemStackCounter;
import steve_gall.minecolonies_compatibility.core.common.item.ItemStackKey;
import steve_gall.minecolonies_compatibility.module.common.storagedrawers.init.ModuleBlockEntities;
import steve_gall.minecolonies_compatibility.module.common.storagedrawers.init.ModuleItems;

public class CitizenSlaveTile extends BlockEntitySlave implements INetworkStorageViewHolder, IAccessDirectionHolder
{
	private final StorageView view;
	private final ItemStackCounter counter;

	private AccessDirection accessDirection = AccessDirection.INSERT_EXTRACT;

	public CitizenSlaveTile(BlockPos pos, BlockState state)
	{
		super(ModuleBlockEntities.CITIZEN_SLAVE.get(), pos, state);

		this.view = new StorageView();
		this.counter = new ItemStackCounter();
		this.counter.addListener(this::onCounterChanged);
	}

	@Override
	public void readPortable(CompoundTag compound)
	{
		super.readPortable(compound);

		this.view.read(compound.getCompound("view"));
		this.accessDirection = AccessDirection.deserialize(compound.get("accessDirection"));
	}

	@Override
	public CompoundTag writePortable(CompoundTag compound)
	{
		compound = super.writePortable(compound);

		compound.put("view", this.view.write());
		compound.put("accessDirection", this.accessDirection.serialize());

		return compound;
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
	public void setRemoved()
	{
		super.setRemoved();

		if (this instanceof BlockEntityExtension bee && !bee.minecolonies_compatibility$isUnloaded())
		{
			this.view.unlink();
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
		var allItems = controller.getItemRepository().getAllItems();

		for (ItemRecord stack : allItems)
		{
			if (stack == null || stack.itemPrototype.isEmpty() || stack.count <= 0)
			{
				continue;
			}

			var key = new ItemStackKey(stack.itemPrototype);
			counter.insert(key, stack.count);
		}

		return counter.entrySet();
	}

	public static <BLOCK_ENTITY extends CitizenSlaveTile> void tick(Level level, BlockPos pos, BlockState state, BLOCK_ENTITY blockEntity)
	{
		blockEntity.onTick();
	}

	protected void onTick()
	{
		var level = this.getLevel();

		if (level.isClientSide())
		{
			return;
		}

		if (level.getGameTime() % 100 == 0)
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
			return new ItemStack(ModuleItems.CITIZEN_SLAVE.get());
		}

		@Override
		public boolean isActive()
		{
			return getController() != null;
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

			return controller.getItemRepository().extractItem(stack, stack.getCount(), simulate);
		}

		@Override
		public @NotNull ItemStack insertItem(@NotNull ItemStack stack, boolean simulate)
		{
			var controller = getController();

			if (controller == null)
			{
				return stack;
			}

			return controller.getItemRepository().insertItem(stack, simulate);
		}

	}

}
