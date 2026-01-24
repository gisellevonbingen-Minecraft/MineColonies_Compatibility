package steve_gall.minecolonies_compatibility.module.common.refinedstorage;

import java.util.stream.Stream;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.refinedmods.refinedstorage.api.core.Action;
import com.refinedmods.refinedstorage.api.network.node.NetworkNodeActor;
import com.refinedmods.refinedstorage.api.network.security.Permission;
import com.refinedmods.refinedstorage.api.network.security.SecurityNetworkComponent;
import com.refinedmods.refinedstorage.api.network.storage.StorageNetworkComponent;
import com.refinedmods.refinedstorage.api.storage.AccessMode;
import com.refinedmods.refinedstorage.api.storage.Actor;
import com.refinedmods.refinedstorage.common.security.BuiltinPermission;
import com.refinedmods.refinedstorage.common.security.PlayerSecurityActor;
import com.refinedmods.refinedstorage.common.storage.AccessModeSettings;
import com.refinedmods.refinedstorage.common.support.containermenu.NetworkNodeMenuProvider;
import com.refinedmods.refinedstorage.common.support.network.AbstractBaseNetworkNodeContainerBlockEntity;
import com.refinedmods.refinedstorage.common.support.resource.ItemResource;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import steve_gall.minecolonies_compatibility.api.common.building.module.INetworkStorageView;
import steve_gall.minecolonies_compatibility.core.common.block.entity.INetworkStorageViewHolder;
import steve_gall.minecolonies_compatibility.core.common.building.module.NetworkStorageModule;
import steve_gall.minecolonies_compatibility.core.common.building.module.QueueNetworkStorageView;
import steve_gall.minecolonies_compatibility.module.common.refinedstorage.CitizenGridNetworkNode.StorageListener;
import steve_gall.minecolonies_compatibility.module.common.refinedstorage.init.ModuleBlockEntities;

public class CitizenGridBlockEntity extends AbstractBaseNetworkNodeContainerBlockEntity<CitizenGridNetworkNode> implements NetworkNodeMenuProvider, INetworkStorageViewHolder
{
	private static final String TAG_LINK = "link";
	private static final String TAG_ACCESS_MODE = "am";

	private final StorageView view;
	private final Actor actor;

	private AccessMode accessMode = AccessMode.INSERT_EXTRACT;

	public CitizenGridBlockEntity(BlockPos pos, BlockState state)
	{
		super(ModuleBlockEntities.CITIZEN_GRID.get(), pos, state, new CitizenGridNetworkNode());

		this.view = new StorageView();
		this.actor = new NetworkNodeActor(this.mainNetworkNode);
		this.mainNetworkNode.addStorageListener(new StorageListener()
		{
			@Override
			public void onChanged(ItemStack t)
			{
				if (view.canEnqueue())
				{
					view.enqueue(t);
				}

			}
		});
	}

	@Override
	public Component getName()
	{
		return this.overrideName(this.getBlockState().getBlock().getName());
	}

	@Override
	public AbstractContainerMenu createMenu(int windowId, Inventory inventory, Player player)
	{
		return new CitizenGridContainerMenu(this, player, windowId);
	}

	@Override
	public void doWork()
	{
		super.doWork();

		this.view.tick();
	}

	@Override
	public void writeConfiguration(CompoundTag tag, Provider provider)
	{
		super.writeConfiguration(tag, provider);

		tag.put(TAG_LINK, this.view.writeLink());
		tag.putInt(TAG_ACCESS_MODE, AccessModeSettings.getAccessMode(this.accessMode));
	}

	@Override
	public void readConfiguration(CompoundTag tag, Provider provider)
	{
		super.readConfiguration(tag, provider);

		if (tag.contains(TAG_LINK))
		{
			this.view.readLink(tag.getCompound(TAG_LINK));
		}

		if (tag.contains(TAG_ACCESS_MODE))
		{
			this.accessMode = AccessModeSettings.getAccessMode(tag.getInt(TAG_ACCESS_MODE));
		}

	}

	public AccessMode getAccessMode()
	{
		return this.accessMode;
	}

	public void setAccessMode(AccessMode accessMode)
	{
		if (this.accessMode != accessMode)
		{
			this.accessMode = accessMode;
			this.view.requestAll();
			this.setChanged();
		}

	}

	@Override
	public @NotNull INetworkStorageView getNetworkStorageView()
	{
		return this.view;
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
			return new ItemStack(getBlockState().getBlock());
		}

		@Override
		public boolean isActive()
		{
			return mainNetworkNode.isActive();
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

		public boolean hasPermission(Permission permission)
		{
			var module = this.getLinkedModule();
			var network = mainNetworkNode.getNetwork();

			if (module == null || network == null)
			{
				return false;
			}

			var owner = module.getBuilding().getColony().getPermissions().getOwner();
			var actor = new PlayerSecurityActor(owner);
			return owner != null && network.getComponent(SecurityNetworkComponent.class).isAllowed(permission, actor);
		}

		@Override
		public boolean canExtract()
		{
			return hasPermission(BuiltinPermission.EXTRACT) && accessMode != AccessMode.INSERT;
		}

		@Override
		public boolean canInsert()
		{
			return hasPermission(BuiltinPermission.INSERT) && accessMode != AccessMode.EXTRACT;
		}

		@Override
		public Stream<ItemStack> getAllStacks()
		{
			var network = mainNetworkNode.getNetwork();

			if (network == null)
			{
				return Stream.empty();
			}

			var entryList = network.getComponent(StorageNetworkComponent.class).getAll();
			return entryList.stream().filter(r -> r.resource() instanceof ItemResource).map(r -> ((ItemResource) r.resource()).toItemStack(r.amount()));
		}

		@Override
		public ItemStack extractItem(ItemStack stack, boolean simulate)
		{
			var network = mainNetworkNode.getNetwork();

			if (network == null)
			{
				return ItemStack.EMPTY;
			}

			var extracted = network.getComponent(StorageNetworkComponent.class).extract(ItemResource.ofItemStack(stack), stack.getCount(), simulate ? Action.SIMULATE : Action.EXECUTE, actor);
			return stack.copyWithCount((int) extracted);
		}

		@Override
		public ItemStack insertItem(ItemStack stack, boolean simulate)
		{
			var network = mainNetworkNode.getNetwork();

			if (network == null)
			{
				return stack;
			}

			var inserted = network.getComponent(StorageNetworkComponent.class).insert(ItemResource.ofItemStack(stack), stack.getCount(), simulate ? Action.SIMULATE : Action.EXECUTE, actor);
			return stack.copyWithCount(stack.getCount() - (int) inserted);
		}

	}

}
