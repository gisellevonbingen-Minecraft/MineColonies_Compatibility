package steve_gall.minecolonies_compatibility.module.common.refinedstorage;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.UUID;
import java.util.stream.Stream;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.minecolonies.api.colony.requestsystem.StandardFactoryController;
import com.minecolonies.api.colony.requestsystem.request.RequestState;
import com.minecolonies.api.colony.requestsystem.requestable.IDeliverable;
import com.minecolonies.api.colony.requestsystem.token.IToken;
import com.minecolonies.api.util.NBTUtils;
import com.refinedmods.refinedstorage.api.autocrafting.ICraftingPattern;
import com.refinedmods.refinedstorage.api.autocrafting.task.CalculationResultType;
import com.refinedmods.refinedstorage.api.network.INetwork;
import com.refinedmods.refinedstorage.api.network.security.Permission;
import com.refinedmods.refinedstorage.api.storage.AccessType;
import com.refinedmods.refinedstorage.api.storage.cache.IStorageCacheListener;
import com.refinedmods.refinedstorage.api.storage.cache.InvalidateCause;
import com.refinedmods.refinedstorage.api.util.Action;
import com.refinedmods.refinedstorage.api.util.StackListEntry;
import com.refinedmods.refinedstorage.api.util.StackListResult;
import com.refinedmods.refinedstorage.apiimpl.network.node.NetworkNode;
import com.refinedmods.refinedstorage.blockentity.config.IAccessType;
import com.refinedmods.refinedstorage.util.AccessTypeUtils;
import com.refinedmods.refinedstorage.util.LevelUtils;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.core.common.building.module.NetworkStorageModule;
import steve_gall.minecolonies_compatibility.core.common.building.module.QueueNetworkStorageView;
import steve_gall.minecolonies_compatibility.core.common.colony.ColonyHelper;
import steve_gall.minecolonies_compatibility.core.common.config.MineColoniesCompatibilityConfigServer;
import steve_gall.minecolonies_compatibility.core.common.requestsystem.NetworkCrafting;
import steve_gall.minecolonies_tweaks.api.common.requestsystem.CustomizableRequestable;

public class CitizenGridNetworkNode extends NetworkNode implements IAccessType
{
	private static final String TAG_LINK = "link";
	private static final String TAG_DATA = "data";

	public static final ResourceLocation ID = MineColoniesCompatibility.rl("citizen_grid");

	private final StorageView view;
	private final StorageListener listener;

	private AccessType accessType = AccessType.INSERT_EXTRACT;

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
		((ICraftingManagerExtension) network.getCraftingManager()).minecolonies_compatibility$addInvalidateListener(this.listener::onCraftingManagerInvaliated);
	}

	@Override
	public void onDisconnected(INetwork network)
	{
		super.onDisconnected(network);

		network.getItemStorageCache().removeListener(this.listener);
		((ICraftingManagerExtension) network.getCraftingManager()).minecolonies_compatibility$removeInvalidateListener(this.listener::onCraftingManagerInvaliated);
	}

	public boolean hasPermission(Permission permission)
	{
		var module = this.view.getLinkedModule();
		var network = this.network;

		if (module == null || network == null)
		{
			return false;
		}

		var colony = module.getBuilding().getColony();
		var owner = ColonyHelper.getFakeOwner(colony);
		return owner != null && network.getSecurityManager().hasPermission(permission, owner);
	}

	@Override
	public void update()
	{
		super.update();

		this.view.tick();
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

		tag.put(TAG_LINK, this.view.writeLink());
		tag.put(TAG_DATA, this.view.writeData());

		return tag;
	}

	@Override
	public CompoundTag writeConfiguration(CompoundTag tag)
	{
		super.writeConfiguration(tag);

		AccessTypeUtils.writeAccessType(tag, this.accessType);

		return tag;
	}

	@Override
	public void read(CompoundTag tag)
	{
		super.read(tag);

		this.view.readLink(tag.getCompound(TAG_LINK));
		this.view.readData(tag.getCompound(TAG_DATA));
	}

	@Override
	public void readConfiguration(CompoundTag tag)
	{
		super.readConfiguration(tag);

		this.accessType = AccessTypeUtils.readAccessType(tag);
	}

	@Override
	public AccessType getAccessType()
	{
		return this.accessType;
	}

	@Override
	public void setAccessType(AccessType value)
	{
		this.accessType = value;

		if (this.network != null)
		{
			this.network.getItemStorageCache().invalidate(InvalidateCause.DEVICE_CONFIGURATION_CHANGED);
		}

		this.markDirty();
	}

	public class TaskHolder
	{
		private UUID taskId;

		public TaskHolder()
		{
			this.taskId = null;
		}

		public TaskHolder(CompoundTag tag)
		{
			this.taskId = tag.hasUUID("taskId") ? tag.getUUID("taskId") : null;
		}

		public CompoundTag write()
		{
			var tag = new CompoundTag();

			if (this.taskId != null)
			{
				tag.putUUID("taskId", this.taskId);
			}

			return tag;
		}

		public UUID getTaskId()
		{
			return taskId;
		}

		public void setTaskId(UUID taskId)
		{
			this.taskId = taskId;
		}

	}

	public class StorageView extends QueueNetworkStorageView
	{
		private Map<IToken<?>, TaskHolder> tasks = new HashMap<>();
		private Queue<ICraftingPattern> patternQueue = new ArrayDeque<>();

		public INetwork getNetwork()
		{
			return CitizenGridNetworkNode.this.getNetwork();
		}

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
		public @NotNull ItemStack calculateAutocrafting(@NotNull IDeliverable deliverable)
		{
			return this.findMatchedOutput(deliverable);
		}

		private ItemStack findMatchedOutput(ICraftingPattern pattern, IDeliverable deliverable)
		{
			if (pattern.isValid())
			{
				for (var output : pattern.getOutputs())
				{
					if (deliverable.matches(output))
					{
						return output;
					}

				}

			}

			return ItemStack.EMPTY;
		}

		private ItemStack findMatchedOutput(IDeliverable deliverable)
		{
			var network = getNetwork();

			if (network == null)
			{
				return ItemStack.EMPTY;
			}

			var craftingManager = network.getCraftingManager();

			for (var pattern : craftingManager.getPatterns())
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
				markDirty();

				var network = getNetwork();

				if (network != null)
				{
					var taskId = taskHolder.getTaskId();

					if (taskId != null)
					{
						network.getCraftingManager().cancel(taskId);
					}

				}

			}

		}

		@Override
		public void createAutocrafting(@NotNull IToken<?> requestId)
		{
			super.createAutocrafting(requestId);

			this.tasks.put(requestId, new TaskHolder());
			markDirty();
		}

		private NetworkCrafting getNetworkCrafting(NetworkStorageModule module, IToken<?> requestId)
		{
			var requestManager = module.getBuilding().getColony().getRequestManager();
			var request = requestManager.getRequestForToken(requestId);

			if (request == null)
			{
				return null;
			}
			else if (request.getRequest() instanceof CustomizableRequestable customizable && customizable.getObject() instanceof NetworkCrafting networkCrafting)
			{
				return networkCrafting;
			}
			else
			{
				return null;
			}

		}

		private IDeliverable getDeliverable(NetworkStorageModule module, IToken<?> requestId)
		{
			var requestManager = module.getBuilding().getColony().getRequestManager();
			var request = requestManager.getRequestForToken(requestId);

			if (request != null)
			{
				var parentId = request.getParent();

				if (parentId != null)
				{
					var parent = requestManager.getRequestForToken(parentId);

					if (parent != null && parent.getRequest() instanceof IDeliverable deliverable)
					{
						return deliverable;
					}

				}
				else if (request.getRequest() instanceof IDeliverable deliverable)
				{
					return deliverable;
				}

			}

			return null;
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

			var network = getNetwork();

			if (network == null)
			{
				return;
			}

			var requestManager = module.getBuilding().getColony().getRequestManager();
			var craftingManager = network.getCraftingManager();
			var toRemove = new ArrayList<IToken<?>>();

			for (var entry : this.tasks.entrySet())
			{
				var requestId = entry.getKey();
				var taskHolder = entry.getValue();
				var networkCrafting = this.getNetworkCrafting(module, requestId);
				var deliverable = this.getDeliverable(module, requestId);

				if (networkCrafting == null || deliverable == null)
				{
					toRemove.add(requestId);
					continue;
				}

				var taskId = taskHolder.getTaskId();

				if (taskId == null)
				{
					var output = this.findMatchedOutput(deliverable);
					var extracting = network.extractItem(output, deliverable.getCount(), Action.SIMULATE);
					var craftingCount = deliverable.getCount() - extracting.getCount();

					if (craftingCount <= 0)
					{
						toRemove.add(requestId);
						continue;
					}

					var calculationResult = craftingManager.create(output, craftingCount);

					if (calculationResult.getType() == CalculationResultType.OK)
					{
						var craftingTask = calculationResult.getTask();
						craftingManager.start(craftingTask);

						taskHolder.setTaskId(craftingTask.getId());
						markDirty();

						requestManager.markDirty();
						networkCrafting.setText(Component.literal("CRAFTING"));
					}
					else
					{
						networkCrafting.setText(Component.literal("ERROR: " + calculationResult.getType()));
					}

				}
				else
				{
					var task = craftingManager.getTask(taskId);

					if (task == null)
					{
						toRemove.add(requestId);
					}
					else
					{
						networkCrafting.setText(Component.literal("CRAFTING: " + task.getCompletionPercentage() + "%"));
					}

				}

			}

			for (var requestId : toRemove)
			{
				var request = requestManager.getRequestForToken(requestId);
				this.tasks.remove(requestId);
				markDirty();

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
				var requestManager = module.getBuilding().getColony().getRequestManager();

				for (var i = 0; i < DEQUEUE_COUNT; i++)
				{
					var pattern = this.patternQueue.poll();

					if (pattern == null)
					{
						break;
					}
					else if (pattern.isValid())
					{
						requestManager.onColonyUpdate(request -> request.getRequest() instanceof IDeliverable deliverable && !this.findMatchedOutput(pattern, deliverable).isEmpty());
					}

				}

			}
			else
			{
				this.patternQueue.clear();
			}

		}

		private void onCraftingManagerInvaliated()
		{
			this.requestAllPattern();
		}

		private void requestAllPattern()
		{
			this.patternQueue.clear();

			var network = getNetwork();

			if (network == null)
			{
				return;
			}

			this.patternQueue.addAll(network.getCraftingManager().getPatterns());
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

		@Override
		public boolean canExtract()
		{
			return hasPermission(Permission.EXTRACT) && accessType != AccessType.INSERT;
		}

		@Override
		public boolean canInsert()
		{
			return hasPermission(Permission.INSERT) && accessType != AccessType.EXTRACT;
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

		@Override
		public void readData(CompoundTag tag)
		{
			super.readData(tag);

			var factoryController = StandardFactoryController.getInstance();
			this.tasks.clear();

			for (var taskTag : NBTUtils.streamCompound(tag.getList("tasks", Tag.TAG_COMPOUND)).toList())
			{
				IToken<?> requestId = factoryController.deserialize(taskTag.getCompound("requestId"));
				var task = new TaskHolder(taskTag.getCompound("task"));
				this.tasks.put(requestId, task);
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
				taskTag.put("task", entry.getValue().write());
				return taskTag;
			}).collect(NBTUtils.toListNBT()));

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

			if (result.getChange() > 0)
			{
				view.enqueue(result.getStack());
			}

		}

		@Override
		public void onChangedBulk(List<StackListResult<ItemStack>> results)
		{
			var view = getView();
			view.enqueue(results.stream().filter(result -> result.getChange() > 0).map(e -> e.getStack()).toList());
		}

		@Override
		public void onInvalidated()
		{
			getView().requestAll();
		}

		public void onCraftingManagerInvaliated()
		{
			getView().onCraftingManagerInvaliated();
		}

	}

}
