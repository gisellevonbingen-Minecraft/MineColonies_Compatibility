package steve_gall.minecolonies_compatibility.module.common.refinedstorage;

import java.util.List;
import java.util.stream.Stream;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.refinedmods.refinedstorage.api.network.INetwork;
import com.refinedmods.refinedstorage.api.network.security.Permission;
import com.refinedmods.refinedstorage.api.storage.cache.IStorageCacheListener;
import com.refinedmods.refinedstorage.api.util.Action;
import com.refinedmods.refinedstorage.api.util.StackListEntry;
import com.refinedmods.refinedstorage.api.util.StackListResult;
import com.refinedmods.refinedstorage.apiimpl.network.node.NetworkNode;
import com.refinedmods.refinedstorage.util.LevelUtils;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.core.common.building.module.NetworkStorageModule;
import steve_gall.minecolonies_compatibility.core.common.building.module.QueueNetworkStorageView;
import steve_gall.minecolonies_compatibility.core.common.config.MineColoniesCompatibilityConfigServer;

public class CitizenGridNetworkNode extends NetworkNode
{
	private static final String TAG_LINK = "link";

	public static final ResourceLocation ID = MineColoniesCompatibility.rl("citizen_grid");

	private final StorageView view;
	private final StorageListener listener;

	public CitizenGridNetworkNode(Level level, BlockPos pos)
	{
		super(level, pos);

		this.view = new StorageView();
		this.listener = new StorageListener();
	}

	public CitizenGridNetworkNode(CompoundTag tag, Level level, BlockPos pos)
	{
		this(level, pos);

		this.read(tag);
	}

	@Override
	public int getEnergyUsage()
	{
		return MineColoniesCompatibilityConfigServer.INSTANCE.modules.RS.citizen_grid_energyUsage.get().intValue();
	}

	@Override
	public void onConnected(INetwork network)
	{
		super.onConnected(network);

		network.getItemStorageCache().addListener(this.listener);
	}

	@Override
	public void onDisconnected(INetwork network)
	{
		super.onDisconnected(network);

		network.getItemStorageCache().removeListener(this.listener);
	}

	@Override
	public void update()
	{
		super.update();

		this.view.onTick();
	}

	public StorageView getView()
	{
		return this.view;
	}

	@Override
	public ResourceLocation getId()
	{
		return ID;
	}

	@Override
	public CompoundTag write(CompoundTag tag)
	{
		super.write(tag);

		tag.put(TAG_LINK, this.view.write());

		return tag;
	}

	@Override
	public void read(CompoundTag tag)
	{
		super.read(tag);

		this.view.read(tag.getCompound(TAG_LINK));
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
			return pos;
		}

		@Override
		public @Nullable Direction getDirection()
		{
			return null;
		}

		@Override
		public @NotNull ItemStack getIcon()
		{
			return getItemStack();
		}

		@Override
		public boolean isActive()
		{
			return CitizenGridNetworkNode.this.canUpdate();
		}

		@Override
		public void link(NetworkStorageModule module)
		{
			super.link(module);

			markDirty();
			LevelUtils.updateBlock(level, pos);
		}

		@Override
		public void unlink()
		{
			super.unlink();

			markDirty();
			LevelUtils.updateBlock(level, pos);
		}

		@Override
		protected void enqueueAll()
		{
			var results = network.getItemStorageCache().getList().getStacks();
			this.enqueue(results.stream().map(StackListEntry<ItemStack>::getStack).toList());
		}

		@Override
		public boolean canExtract()
		{
			var network = getNetwork();

			if (network == null)
			{
				return false;
			}

			return RefinedStorageModule.hasPermission(this.getLinkedModule().getBuilding().getColony(), network, Permission.EXTRACT);
		}

		@Override
		public boolean canInsert()
		{
			var network = getNetwork();

			if (network == null)
			{
				return false;
			}

			return RefinedStorageModule.hasPermission(this.getLinkedModule().getBuilding().getColony(), network, Permission.INSERT);
		}

		@Override
		public Stream<ItemStack> getAllStacks()
		{
			var network = getNetwork();

			if (network == null)
			{
				return Stream.empty();
			}

			var entryList = network.getItemStorageCache().getList().getStacks();
			return entryList.stream().map(StackListEntry<ItemStack>::getStack);
		}

		@Override
		public ItemStack extractItem(ItemStack stack, boolean simulate)
		{
			var network = getNetwork();

			if (network == null)
			{
				return ItemStack.EMPTY;
			}

			return network.extractItem(stack, stack.getCount(), simulate ? Action.SIMULATE : Action.PERFORM);
		}

		@Override
		public ItemStack insertItem(ItemStack stack, boolean simulate)
		{
			var network = getNetwork();

			if (network == null)
			{
				return stack;
			}

			return network.insertItem(stack, stack.getCount(), simulate ? Action.SIMULATE : Action.PERFORM);
		}

	}

	public class StorageListener implements IStorageCacheListener<ItemStack>
	{
		@Override
		public void onAttached()
		{

		}

		@Override
		public void onChanged(StackListResult<ItemStack> result)
		{
			var view = getView();

			if (view.canEnqueue() && result.getChange() > 0)
			{
				getView().enqueue(result.getStack());
			}

		}

		@Override
		public void onChangedBulk(List<StackListResult<ItemStack>> results)
		{
			var view = getView();

			if (view.canEnqueue())
			{
				view.enqueue(results.stream().filter(result -> result.getChange() > 0).map(e -> e.getStack()).toList());
			}

		}

		@Override
		public void onInvalidated()
		{
			getView().requestAll();
		}

	}

}
