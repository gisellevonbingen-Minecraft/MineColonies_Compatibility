package steve_gall.minecolonies_compatibility.module.common.storagenetwork;

import java.util.Collections;
import java.util.stream.Stream;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.lothrazar.storagenetwork.StorageNetworkMod;
import com.lothrazar.storagenetwork.block.TileConnectable;
import com.lothrazar.storagenetwork.block.main.TileMain;
import com.lothrazar.storagenetwork.capability.handler.ItemStackMatcher;
import com.minecolonies.api.util.WorldUtil;

import it.unimi.dsi.fastutil.objects.Object2LongMap.Entry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import steve_gall.minecolonies_compatibility.core.common.block.entity.BlockEntityExtension;
import steve_gall.minecolonies_compatibility.core.common.building.module.NetworkStorageModule;
import steve_gall.minecolonies_compatibility.core.common.building.module.QueueNetworkStorageView;
import steve_gall.minecolonies_compatibility.core.common.item.ItemStackCounter;
import steve_gall.minecolonies_compatibility.core.common.item.ItemStackKey;
import steve_gall.minecolonies_compatibility.mixin.common.storagenetwork.NetworkModuleAccessor;
import steve_gall.minecolonies_compatibility.module.common.storagenetwork.init.ModuleBlockEntities;
import steve_gall.minecolonies_compatibility.module.common.storagenetwork.init.ModuleItems;

public class CitizenInventoryBlockEntity extends TileConnectable
{
	private static final String TAG_LINK = "link";

	private final StorageView view;
	private final ItemStackCounter counter;

	public CitizenInventoryBlockEntity(BlockPos pos, BlockState state)
	{
		super(ModuleBlockEntities.CITIZEN_INVENTORY.get(), pos, state);

		this.view = new StorageView();
		this.counter = new ItemStackCounter();
		this.counter.addListener(this::onCounterChanged);
	}

	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> arg0, Direction arg1)
	{
		var cap = super.getCapability(arg0, arg1);

		return cap;
	}

	@Override
	public void load(CompoundTag compound)
	{
		super.load(compound);

		this.view.read(compound.getCompound(TAG_LINK));
	}

	@Override
	public void saveAdditional(CompoundTag compound)
	{
		super.saveAdditional(compound);

		compound.put(TAG_LINK, this.view.write());
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

	protected void onTick()
	{
		var level = this.getLevel();

		if (level.isClientSide())
		{
			return;
		}

		if (level.getGameTime() % StorageNetworkMod.CONFIG.refreshTicks() == 0)
		{
			this.update();
		}

		this.view.onTick();
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
		var main = getMainBlockEntity();

		if (main == null)
		{
			return Collections.emptyList();
		}

		var isFiltered = true;
		var counter = new ItemStackCounter();
		main.nw.setShouldRefresh();

		try
		{
			var links = ((NetworkModuleAccessor) main.nw).invokeGetConnectableStorage();

			for (var link : links)
			{
				for (var stack : link.getStoredStacks(isFiltered))
				{
					if (stack == null || stack.isEmpty())
					{
						continue;
					}

					var key = new ItemStackKey(stack);
					counter.insert(key, stack.getCount());
				}

			}

		}
		catch (Exception e)
		{
			StorageNetworkMod.LOGGER.info("3rd party storage mod has an error", e);
		}

		return counter.entrySet();
	}

	public TileMain getMainBlockEntity()
	{
		var mainPos = this.getMain();

		if (mainPos != null)
		{
			return mainPos.getTileEntity(TileMain.class);
		}
		else
		{
			return null;
		}

	}

	public StorageView getView()
	{
		return this.view;
	}

	public static <BLOCK_ENTITY extends CitizenInventoryBlockEntity> void tick(Level level, BlockPos pos, BlockState state, BLOCK_ENTITY blockEntity)
	{
		blockEntity.onTick();
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
			return new ItemStack(ModuleItems.CITIZEN_INVENTORY.get());
		}

		@Override
		public boolean isActive()
		{
			return getMainBlockEntity() != null;
		}

		@Override
		public boolean canExtract()
		{
			return true;
		}

		@Override
		public boolean canInsert()
		{
			return true;
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
			var main = getMainBlockEntity();

			if (main == null)
			{
				return ItemStack.EMPTY;
			}

			return main.request(new ItemStackMatcher(stack), stack.getCount(), simulate);
		}

		@Override
		public @NotNull ItemStack insertItem(@NotNull ItemStack stack, boolean simulate)
		{
			var main = getMainBlockEntity();

			if (main == null)
			{
				return stack;
			}

			var remainedCount = main.insertStack(stack, simulate);
			var copy = stack.copy();
			copy.setCount(remainedCount);
			return copy;
		}

	}

}
