package steve_gall.minecolonies_compatibility.module.common.storagenetwork;

import java.util.HashSet;
import java.util.Objects;
import java.util.stream.Stream;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.lothrazar.storagenetwork.StorageNetworkMod;
import com.lothrazar.storagenetwork.block.TileConnectable;
import com.lothrazar.storagenetwork.block.main.TileMain;
import com.lothrazar.storagenetwork.capability.handler.ItemStackMatcher;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntMaps;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import steve_gall.minecolonies_compatibility.core.common.block.entity.BlockEntityExtension;
import steve_gall.minecolonies_compatibility.core.common.building.module.NetworkStorageModule;
import steve_gall.minecolonies_compatibility.core.common.building.module.QueueNetworkStorageView;
import steve_gall.minecolonies_compatibility.mixin.common.storagenetwork.NetworkModuleAccessor;
import steve_gall.minecolonies_compatibility.module.common.storagenetwork.init.ModuleBlockEntities;
import steve_gall.minecolonies_compatibility.module.common.storagenetwork.init.ModuleItems;

public class CitizenInventoryBlockEntity extends TileConnectable
{
	private static final String TAG_LINK = "link";

	private final StorageView view;
	private final Object2IntMap<ItemStackKey> counter;

	public CitizenInventoryBlockEntity(BlockPos pos, BlockState state)
	{
		super(ModuleBlockEntities.CITIZEN_INVENTORY.get(), pos, state);

		this.view = new StorageView();
		this.counter = new Object2IntOpenHashMap<>();
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

	private void markUpdated()
	{
		this.setChanged();
		this.getLevel().sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), Block.UPDATE_ALL);
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
		var prevKeys = new HashSet<>(this.counter.keySet());
		var stacks = this.getStacks();

		for (var entry : stacks.object2IntEntrySet())
		{
			var key = entry.getKey();
			var nextCount = entry.getIntValue();
			var prevCount = this.counter.getInt(key);

			if (prevCount < nextCount)
			{
				this.view.enqueue(key.stack);
			}

			this.counter.put(key, nextCount);
			prevKeys.remove(key);
		}

		prevKeys.forEach(this.counter::removeInt);
	}

	private Object2IntMap<ItemStackKey> getStacks()
	{
		var main = getMainBlockEntity();

		if (main == null)
		{
			return Object2IntMaps.emptyMap();
		}

		var isFiltered = true;
		var counter = new Object2IntOpenHashMap<ItemStackKey>();
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
					var count = counter.getInt(key);
					counter.put(key, count + stack.getCount());
				}

			}

		}
		catch (Exception e)
		{
			StorageNetworkMod.LOGGER.info("3rd party storage mod has an error", e);
		}

		return counter;
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

	public class ItemStackKey
	{
		private final ItemStack stack;
		private final int hashCode;

		public ItemStackKey(ItemStack stack)
		{
			this.stack = stack.copy();
			this.hashCode = Objects.hash(stack.getItem(), stack.getTag());
		}

		@Override
		public String toString()
		{
			return this.stack.toString();
		}

		@Override
		public int hashCode()
		{
			return this.hashCode;
		}

		@Override
		public boolean equals(Object obj)
		{
			if (obj == this)
			{
				return true;
			}
			else if (obj == null)
			{
				return false;
			}
			else
			{
				return obj instanceof ItemStackKey other && ItemStack.isSameItemSameTags(this.stack, other.stack);
			}

		}

		public ItemStack getStack(int count)
		{
			var stack = this.stack.copy();
			stack.setCount(count);
			return stack;
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
			return counter.object2IntEntrySet().stream().map(e -> e.getKey().getStack(e.getIntValue()));
		}

		@Override
		public void link(NetworkStorageModule module)
		{
			super.link(module);

			markUpdated();
		}

		@Override
		public void unlink()
		{
			super.unlink();

			markUpdated();
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
