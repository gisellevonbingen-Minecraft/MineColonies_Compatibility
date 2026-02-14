package steve_gall.minecolonies_compatibility.module.common.refinedstorage;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.stream.Stream;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.minecolonies.api.colony.requestsystem.request.RequestState;
import com.minecolonies.api.colony.requestsystem.requestable.IDeliverable;
import com.minecolonies.api.colony.requestsystem.token.IToken;
import com.refinedmods.refinedstorage.api.autocrafting.Pattern;
import com.refinedmods.refinedstorage.api.autocrafting.calculation.CancellationToken;
import com.refinedmods.refinedstorage.api.autocrafting.task.TaskId;
import com.refinedmods.refinedstorage.api.core.Action;
import com.refinedmods.refinedstorage.api.network.autocrafting.AutocraftingNetworkComponent;
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
import steve_gall.minecolonies_compatibility.mixin.common.refinedstorage.AutocraftingNetworkComponentImplAccessor;
import steve_gall.minecolonies_compatibility.module.common.refinedstorage.CitizenGridNetworkNode.ExternalListener;
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
		this.mainNetworkNode.addExternalListener(this.view);
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
			this.setChanged();
		}

	}

	@Override
	public @NotNull INetworkStorageView getNetworkStorageView()
	{
		return this.view;
	}

	public class TaskHolder
	{
		private TaskId taskId;

		public TaskHolder()
		{
			this.taskId = null;
		}

		public TaskHolder(CompoundTag tag)
		{
			if (tag.hasUUID("taskId"))
			{
				this.taskId = new TaskId(tag.getUUID("taskId"));
			}

		}

		public CompoundTag write()
		{
			var tag = new CompoundTag();

			if (this.taskId != null)
			{
				tag.putUUID("taskId", this.taskId.id());
			}

			return tag;
		}

		public TaskId getTaskId()
		{
			return taskId;
		}

		public void setTaskId(TaskId taskId)
		{
			this.taskId = taskId;
		}

	}

	public class StorageView extends QueueNetworkStorageView implements ExternalListener
	{
		private Map<IToken<?>, TaskHolder> tasks = new HashMap<>();
		private Queue<Pattern> patternQueue = new ArrayDeque<>();

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
			this.tasks.clear();
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

		@Override
		public @NotNull ItemStack calculateAutocrafting(@NotNull IDeliverable deliverable)
		{
			return this.findMatchedOutput(deliverable);
		}

		private ItemStack findMatchedOutput(Pattern pattern, IDeliverable deliverable)
		{
			for (var output : pattern.layout().outputs())
			{
				if (output.resource() instanceof ItemResource itemOutput)
				{
					var stack = itemOutput.toItemStack();

					if (deliverable.matches(stack))
					{
						return stack;
					}

				}

			}

			return ItemStack.EMPTY;
		}

		private ItemStack findMatchedOutput(IDeliverable deliverable)
		{
			var network = mainNetworkNode.getNetwork();

			if (network == null)
			{
				return ItemStack.EMPTY;
			}

			var autocrafting = network.getComponent(AutocraftingNetworkComponent.class);

			for (var pattern : autocrafting.getPatterns())
			{
				var output = this.findMatchedOutput(pattern, deliverable);

				if (!output.isEmpty())
				{
					return output;
				}

			}

			return ItemStack.EMPTY;
		}

		@Override
		public void cancelAutocrafting(@NotNull IToken<?> requestId)
		{
			super.cancelAutocrafting(requestId);

			var taskHolder = this.tasks.remove(requestId);

			if (taskHolder != null)
			{
				setChanged();

				var network = mainNetworkNode.getNetwork();

				if (network != null)
				{
					var taskId = taskHolder.getTaskId();

					if (taskId != null)
					{
						network.getComponent(AutocraftingNetworkComponent.class).cancel(taskId);
					}

				}

			}

		}

		@Override
		public void createAutocrafting(@NotNull IToken<?> requestId)
		{
			super.createAutocrafting(requestId);

			this.tasks.put(requestId, new TaskHolder());
			setChanged();
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

			var network = mainNetworkNode.getNetwork();

			if (network == null)
			{
				return;
			}

			var requestManager = module.getBuilding().getColony().getRequestManager();
			var autocrafting = network.getComponent(AutocraftingNetworkComponent.class);
			var storage = network.getComponent(StorageNetworkComponent.class);
			var toRemove = new ArrayList<IToken<?>>();

			for (var entry : this.tasks.entrySet())
			{
				var requestId = entry.getKey();
				var taskHolder = entry.getValue();
				var networkCrafting = this.getNetworkCrafting(requestManager, requestId);
				var deliverable = this.getDeliverable(requestManager, requestId);

				if (networkCrafting == null || deliverable == null)
				{
					toRemove.add(requestId);
					continue;
				}

				var taskId = taskHolder.getTaskId();

				if (taskId == null)
				{
					var output = ItemResource.ofItemStack(this.findMatchedOutput(deliverable));
					var extracting = storage.extract(output, deliverable.getCount(), Action.SIMULATE, actor);
					var craftingCount = deliverable.getCount() - extracting;

					if (craftingCount <= 0)
					{
						toRemove.add(requestId);
						continue;
					}

					taskId = autocrafting.startTask(output, craftingCount, actor, false, CancellationToken.NONE).orElse(null);

					if (taskId != null)
					{
						taskHolder.setTaskId(taskId);
						setChanged();

						requestManager.markDirty();
						networkCrafting.setText(Component.literal("CRAFTING"));
					}
					else
					{
						networkCrafting.setText(Component.literal("RESOURCE MISSING"));
					}

				}
				else
				{
					var provider = ((AutocraftingNetworkComponentImplAccessor) autocrafting).getProviderByTaskId().get(taskId);

					if (provider == null)
					{
						toRemove.add(requestId);
					}

				}

			}

			for (var requestId : toRemove)
			{
				var request = requestManager.getRequestForToken(requestId);
				this.tasks.remove(requestId);
				setChanged();

				if (request == null)
				{
					continue;
				}

				requestManager.updateRequestState(requestId, RequestState.CANCELLED);
				requestManager.markDirty();
			}

		}

		@Override
		protected void onActiveChanged(boolean isActive)
		{
			super.onActiveChanged(isActive);

			if (isActive)
			{
				this.requestAllPattern();
			}

		}

		@Override
		public void tick()
		{
			super.tick();

			var module = this.getLinkedModule();

			if (module != null)
			{
				for (var i = 0; i < DEQUEUE_COUNT; i++)
				{
					var pattern = this.patternQueue.poll();

					if (pattern == null)
					{
						break;
					}

					var requestManager = module.getBuilding().getColony().getRequestManager();
					requestManager.onColonyUpdate(request -> request.getRequest() instanceof IDeliverable deliverable && !this.findMatchedOutput(pattern, deliverable).isEmpty());
				}

			}
			else
			{
				this.patternQueue.clear();
			}

		}

		private void requestAllPattern()
		{
			this.patternQueue.clear();

			var network = mainNetworkNode.getNetwork();

			if (network == null)
			{
				return;
			}

			var autocrafting = network.getComponent(AutocraftingNetworkComponent.class);
			this.patternQueue.addAll(autocrafting.getPatterns());
		}

		@Override
		public void onChanged(ItemStack item)
		{
			this.enqueue(item);
		}

		@Override
		public void onAdded(Pattern pattern)
		{
			this.patternQueue.add(pattern);
		}

		@Override
		public void onRemoved(Pattern pattern)
		{

		}

	}

}
