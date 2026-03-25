package steve_gall.minecolonies_compatibility.module.common.ae2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import com.minecolonies.api.colony.requestsystem.StandardFactoryController;
import com.minecolonies.api.util.NBTUtils;
import net.minecraft.nbt.Tag;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import appeng.api.config.AccessRestriction;
import appeng.api.config.Actionable;
import appeng.api.config.Setting;
import appeng.api.config.Settings;
import appeng.api.networking.IGridNode;
import appeng.api.networking.IStackWatcher;
import appeng.api.networking.security.IActionSource;
import appeng.api.networking.storage.IStorageWatcherNode;
import appeng.api.networking.ticking.IGridTickable;
import appeng.api.networking.ticking.TickRateModulation;
import appeng.api.networking.crafting.CalculationStrategy;
import appeng.api.networking.crafting.ICraftingCPU;
import appeng.api.networking.crafting.ICraftingLink;
import appeng.api.networking.crafting.ICraftingPlan;
import appeng.api.networking.crafting.ICraftingSimulationRequester;
import appeng.api.networking.crafting.ICraftingSubmitResult;
import appeng.api.networking.ticking.TickingRequest;
import appeng.api.parts.IPartItem;
import appeng.api.parts.IPartModel;
import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.AEKey;
import appeng.api.stacks.KeyCounter;
import appeng.api.storage.AEKeyFilter;
import appeng.api.util.IConfigManager;
import appeng.api.util.IConfigurableObject;
import appeng.items.parts.PartModels;
import appeng.menu.MenuOpener;
import appeng.menu.locator.MenuLocators;
import appeng.parts.PartModel;
import appeng.parts.reporting.AbstractDisplayPart;
import com.minecolonies.api.colony.requestsystem.request.RequestState;
import com.minecolonies.api.colony.requestsystem.requestable.IDeliverable;
import com.minecolonies.api.colony.requestsystem.token.IToken;
import it.unimi.dsi.fastutil.objects.Object2LongMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import steve_gall.minecolonies_compatibility.api.common.building.module.INetworkStorageView;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.core.common.building.module.NetworkStorageModule;
import steve_gall.minecolonies_compatibility.core.common.building.module.QueueNetworkStorageView;
import steve_gall.minecolonies_compatibility.core.common.requestsystem.NetworkCrafting;
import steve_gall.minecolonies_compatibility.core.common.config.MineColoniesCompatibilityConfigServer;
import steve_gall.minecolonies_compatibility.module.common.ae2.init.ModuleMenuTypes;

public class CitizenTerminalPart extends AbstractDisplayPart implements IStorageWatcherNode, IGridTickable, IConfigurableObject
{
	@PartModels
	public static final ResourceLocation MODEL_OFF = MineColoniesCompatibility.rl("part/citizen_terminal_off");
	@PartModels
	public static final ResourceLocation MODEL_ON = MineColoniesCompatibility.rl("part/citizen_terminal_on");

	public static final IPartModel MODELS_OFF = new PartModel(MODEL_BASE, MODEL_OFF, MODEL_STATUS_OFF);
	public static final IPartModel MODELS_ON = new PartModel(MODEL_BASE, MODEL_ON, MODEL_STATUS_ON);
	public static final IPartModel MODELS_HAS_CHANNEL = new PartModel(MODEL_BASE, MODEL_ON, MODEL_STATUS_HAS_CHANNEL);

	private static final String TAG_LINK = "link";
	private static final String TAG_TASKS = "tasks";

	private static final int CRAFTABLE_NOTIFY_INTERVAL = 100;

	private final StorageView view;
	private final KeyCounter counter;
	private boolean craftableUpdatePending = false;
	private int craftableNotifyTick = 0;
	private final IActionSource action;
	private final IConfigManager config;

	public CitizenTerminalPart(IPartItem<?> partItem)
	{
		super(partItem, false);

		this.view = new StorageView();
		this.counter = new KeyCounter();
		this.action = IActionSource.ofMachine(this);

		var builder = IConfigManager.builder(this::onSettingChanged);
		builder.registerSetting(Settings.ACCESS, AccessRestriction.READ_WRITE);
		this.config = builder.build();

		var mainNode = this.getMainNode();
		mainNode.addService(IStorageWatcherNode.class, this);
		mainNode.addService(IGridTickable.class, this);
	}

	protected void onSettingChanged(IConfigManager manager, Setting<?> setting)
	{
		this.getHost().markForSave();
	}

	public boolean hasPermission()
	{
		var module = this.view.getLinkedModule();
		var grid = this.getMainNode().getGrid();

		if (module == null || grid == null)
		{
			return false;
		}

		return true;
	}

	@Override
	public void removeFromWorld()
	{
		super.removeFromWorld();

		if (this.getBlockEntity() instanceof CableBusBlockEntityExtension cableBus)
		{
			if (!cableBus.minecolonies_compatibility$isChunkUnloaded())
			{
				this.view.unlink();
			}

		}

	}

	@Override
	public boolean onUseWithoutItem(Player player, Vec3 pos)
	{
		if (!super.onUseWithoutItem(player, pos) && !isClientSide())
		{
			MenuOpener.open(ModuleMenuTypes.CITIZEN_TERMINAL.get(), player, MenuLocators.forPart(this));
		}

		return true;
	}

	@Override
	public IPartModel getStaticModels()
	{
		return this.selectModel(MODELS_OFF, MODELS_ON, MODELS_HAS_CHANNEL);
	}

	@Override
	public TickingRequest getTickingRequest(IGridNode node)
	{
		return new TickingRequest(1, 1, false, 1);
	}

	@Override
	public TickRateModulation tickingRequest(IGridNode node, int ticksSinceLastCall)
	{
		if (this.craftableUpdatePending)
		{
			this.craftableUpdatePending = false;
			this.view.requestAll();
		}
		else if (++this.craftableNotifyTick >= CRAFTABLE_NOTIFY_INTERVAL)
		{
			this.craftableNotifyTick = 0;
			this.view.requestAll();
		}

		this.view.tick();
		return TickRateModulation.SAME;
	}

	@Override
	public void updateWatcher(IStackWatcher newWatcher)
	{
		if (newWatcher != null)
		{
			newWatcher.reset();
			newWatcher.setWatchAll(true);

			this.counter.clear();
			this.getMainNode().ifPresent(grid ->
			{
				var original = grid.getStorageService().getCachedInventory();

				for (var entry : original)
				{
					if (entry.getKey() instanceof AEItemKey itemKey)
					{
						this.counter.add(itemKey, entry.getLongValue());
					}

				}

			});
		}

	}

	public static ItemStack toStack(Object2LongMap.Entry<AEKey> entry)
	{
		return toStack((AEItemKey) entry.getKey(), entry.getLongValue());
	}

	public static ItemStack toStack(AEItemKey key, long amount)
	{
		return key.toStack((int) Math.min(Integer.MAX_VALUE, amount));
	}

	@Override
	public void onStackChange(AEKey what, long amount)
	{
		if (what instanceof AEItemKey itemKey)
		{
			this.getMainNode().ifPresent(grid ->
			{
				var prev = this.counter.get(itemKey);
				this.counter.set(itemKey, amount);

				if (prev < amount)
				{
					this.view.enqueue(toStack(itemKey, amount));
					this.craftableUpdatePending = true;
				}

			});
		}

	}

	@Override
	public void readFromNBT(CompoundTag data, Provider provider)
	{
		super.readFromNBT(data, provider);

		this.view.readLink(provider, data.getCompound(TAG_LINK));
		this.view.readData(provider, data.getCompound(TAG_TASKS));
		this.config.readFromNBT(data.getCompound("config"), provider);
	}

	@Override
	public void writeToNBT(CompoundTag data, Provider provider)
	{
		super.writeToNBT(data, provider);

		data.put(TAG_LINK, this.view.writeLink(provider));
		data.put(TAG_TASKS, this.view.writeData(provider));

		var configTag = new CompoundTag();
		this.config.writeToNBT(configTag, provider);
		data.put("config", configTag);
	}

	@Override
	public boolean readFromStream(RegistryFriendlyByteBuf data)
	{
		var needRedraw = super.readFromStream(data);

		var changed = this.view.readLink(data);
		return needRedraw || changed;
	}

	@Override
	public void writeToStream(RegistryFriendlyByteBuf data)
	{
		super.writeToStream(data);

		this.view.writeLink(data);
	}

	public @NotNull INetworkStorageView getView()
	{
		return this.view;
	}

	@Override
	public IConfigManager getConfigManager()
	{
		return this.config;
	}

	public class TaskHolder
	{
		private Future<ICraftingPlan> calculationFuture;
		private ICraftingLink craftingLink;
		private AEItemKey outputKey;

		private long calculationStartTick = -1;
		private long lastProgressValue = -1;
		private int noProgressChecks = 0;

		public TaskHolder()
		{
			this.calculationFuture = null;
			this.craftingLink = null;
		}

		public TaskHolder(CompoundTag tag, Provider provider)
		{
			if (tag.contains("outputKey"))
			{
				this.outputKey = AEItemKey.fromTag(provider, tag.getCompound("outputKey"));
			}
		}

		public CompoundTag write(Provider provider)
		{
			var tag = new CompoundTag();

			if (this.outputKey != null)
			{
				tag.put("outputKey", this.outputKey.toTag(provider));
			}

			return tag;
		}

		public Future<ICraftingPlan> getCalculationFuture()
		{
			return calculationFuture;
		}

		public void setCalculationFuture(Future<ICraftingPlan> calculationFuture)
		{
			this.calculationFuture = calculationFuture;
		}

		public ICraftingLink getCraftingLink()
		{
			return craftingLink;
		}

		public void setCraftingLink(ICraftingLink craftingLink)
		{
			this.craftingLink = craftingLink;
			this.lastProgressValue = -1;
			this.noProgressChecks = 0;
		}

		public AEItemKey getOutputKey()
		{
			return outputKey;
		}

		public void setOutputKey(AEItemKey outputKey)
		{
			this.outputKey = outputKey;
		}

		public void setCalculationStartTick(long tick)
		{
			this.calculationStartTick = tick;
		}

		public boolean isCalculationTimedOut(long currentTick)
		{
			return calculationStartTick >= 0 && (currentTick - calculationStartTick) > MineColoniesCompatibilityConfigServer.INSTANCE.modules.AE2.citizenTerminal_calculationTimeoutTicks.get();
		}

		public boolean checkLinkStalled(Iterable<ICraftingCPU> cpus)
		{
			if (outputKey == null)
			{
				return false;
			}

			long currentProgress = -1;

			for (var cpu : cpus)
			{
				if (!cpu.isBusy())
				{
					continue;
				}

				var status = cpu.getJobStatus();

				if (status != null && outputKey.equals(status.crafting().what()))
				{
					currentProgress = status.progress();
					break;
				}

			}

			if (currentProgress < 0)
			{
				return false;
			}

			if (currentProgress != lastProgressValue)
			{
				lastProgressValue = currentProgress;
				noProgressChecks = 0;
			}
			else
			{
				noProgressChecks++;
			}

			return noProgressChecks >= MineColoniesCompatibilityConfigServer.INSTANCE.modules.AE2.citizenTerminal_linkNoProgressChecks.get();
		}

	}

	public class StorageView extends QueueNetworkStorageView
	{
		private final Map<IToken<?>, TaskHolder> tasks = new HashMap<>();

		@Override
		public Level getLevel()
		{
			return CitizenTerminalPart.this.getLevel();
		}

		@Override
		public BlockPos getPos()
		{
			return CitizenTerminalPart.this.getBlockEntity().getBlockPos();
		}

		@Override
		public @Nullable Direction getDirection()
		{
			return CitizenTerminalPart.this.getSide();
		}

		@Override
		public @NotNull ItemStack getIcon()
		{
			return new ItemStack(getPartItem());
		}

		@Override
		public boolean isActive()
		{
			return CitizenTerminalPart.this.isActive();
		}

		@Override
		public void link(NetworkStorageModule module)
		{
			super.link(module);

			var host = getHost();

			if (host != null)
			{
				host.markForSave();
				host.markForUpdate();
			}

		}

		@Override
		public void unlink()
		{
			super.unlink();

			this.tasks.clear();

			var host = getHost();

			if (host != null)
			{
				host.markForSave();
				host.markForUpdate();
			}

		}

		@Override
		public boolean canExtract()
		{
			return hasPermission() && config.getSetting(Settings.ACCESS).isAllowExtraction();
		}

		@Override
		public boolean canInsert()
		{
			return hasPermission() && config.getSetting(Settings.ACCESS).isAllowInsertion();
		}

		@Override
		public Stream<ItemStack> getAllStacks()
		{
			var storageStream = StreamSupport.stream(counter.spliterator(), false).map(CitizenTerminalPart::toStack);

			var grid = getMainNode().getGrid();

			if (grid == null)
			{
				return storageStream;
			}

			var craftableStream = grid.getCraftingService()
				.getCraftables((AEKeyFilter) k -> k instanceof AEItemKey)
				.stream()
				.filter(k -> counter.get((AEItemKey) k) <= 0)
				.map(key -> ((AEItemKey) key).toStack(1));

			return Stream.concat(storageStream, craftableStream);
		}

		@Override
		public ItemStack extractItem(ItemStack stack, boolean simulate)
		{
			var grid = getMainNode().getGrid();

			if (grid == null)
			{
				return ItemStack.EMPTY;
			}

			var key = AEItemKey.of(stack);
			var network = grid.getStorageService().getInventory();
			var extractingCount = (int) network.extract(key, stack.getCount(), Actionable.ofSimulate(simulate), action);

			if (extractingCount == 0)
			{
				return ItemStack.EMPTY;
			}

			return key.toStack(extractingCount);
		}

		@Override
		public ItemStack insertItem(ItemStack stack, boolean simulate)
		{
			var grid = getMainNode().getGrid();

			if (grid == null)
			{
				return ItemStack.EMPTY;
			}

			var key = AEItemKey.of(stack);
			var network = grid.getStorageService().getInventory();
			var insertedCount = (int) network.insert(key, stack.getCount(), Actionable.ofSimulate(simulate), action);

			if (insertedCount == 0)
			{
				return stack;
			}
			else
			{
				stack = stack.copy();
				stack.shrink(insertedCount);
				return stack;
			}

		}

		@Override
		public @NotNull ItemStack calculateAutocrafting(@NotNull IDeliverable deliverable)
		{
			var grid = getMainNode().getGrid();

			if (grid == null)
			{
				return ItemStack.EMPTY;
			}

			var craftingService = grid.getCraftingService();

			for (var key : craftingService.getCraftables((AEKeyFilter) k -> k instanceof AEItemKey))
			{
				var stack = ((AEItemKey) key).toStack();

				if (deliverable.matches(stack))
				{
					return stack;
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
				var future = taskHolder.getCalculationFuture();

				if (future != null)
				{
					future.cancel(true);
				}

				var link = taskHolder.getCraftingLink();

				if (link != null)
				{
					link.cancel();
				}

				var module = this.getLinkedModule();

				if (module != null)
				{
					module.getBuilding().getColony().getRequestManager().markDirty();
				}

				var host = getHost();

				if (host != null)
				{
					host.markForSave();
				}

			}

		}

		@Override
		public void createAutocrafting(@NotNull IToken<?> requestId)
		{
			super.createAutocrafting(requestId);

			this.tasks.put(requestId, new TaskHolder());

			var host = getHost();

			if (host != null)
			{
				host.markForSave();
			}

		}

		public void readData(Provider provider, CompoundTag tag)
		{
			var factoryController = StandardFactoryController.getInstance();
			this.tasks.clear();

			for (var taskTag : NBTUtils.streamCompound(tag.getList("tasks", Tag.TAG_COMPOUND)).toList())
			{
				IToken<?> requestId = factoryController.deserializeTag(provider, taskTag.getCompound("requestId"));
				var taskHolder = new TaskHolder(taskTag.getCompound("task"), provider);
				this.tasks.put(requestId, taskHolder);
			}

		}

		public CompoundTag writeData(Provider provider)
		{
			var tag = new CompoundTag();
			var factoryController = StandardFactoryController.getInstance();
			tag.put("tasks", this.tasks.entrySet().stream().map(entry ->
			{
				var taskTag = new CompoundTag();
				taskTag.put("requestId", factoryController.serializeTag(provider, entry.getKey()));
				taskTag.put("task", entry.getValue().write(provider));
				return taskTag;
			}).collect(NBTUtils.toListNBT()));
			return tag;
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

			var grid = getMainNode().getGrid();

			if (grid == null)
			{
				return;
			}

			var requestManager = module.getBuilding().getColony().getRequestManager();
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

				if (this.updateTaskHolder(requestId, taskHolder, networkCrafting, deliverable, grid, toRemove))
				{
					continue;
				}

			}

			for (var requestId : toRemove)
			{
				var request = requestManager.getRequestForToken(requestId);
				this.tasks.remove(requestId);

				if (request == null)
				{
					continue;
				}

				requestManager.updateRequestState(requestId, RequestState.CANCELLED);
				requestManager.markDirty();
			}

		}

		private boolean updateTaskHolder(IToken<?> requestId, TaskHolder taskHolder, NetworkCrafting networkCrafting, IDeliverable deliverable, appeng.api.networking.IGrid grid, java.util.List<IToken<?>> toRemove)
		{
			var link = taskHolder.getCraftingLink();

			if (link != null)
			{
				var cpus = grid.getCraftingService().getCpus();

				if (link.isDone() || link.isCanceled() || taskHolder.checkLinkStalled(cpus))
				{
					if (!link.isDone() && !link.isCanceled())
					{
						link.cancel();
					}

					toRemove.add(requestId);
				}
				else
				{
					networkCrafting.setText(Component.literal("CRAFTING"));
				}

				return true;
			}

			var future = taskHolder.getCalculationFuture();

			if (future != null)
			{
				var currentTick = CitizenTerminalPart.this.getLevel().getGameTime();

				if (taskHolder.isCalculationTimedOut(currentTick))
				{
					future.cancel(true);
					networkCrafting.setText(Component.literal("ERROR: CALCULATION_TIMEOUT"));
					toRemove.add(requestId);
					return true;
				}

				if (future.isDone())
				{
					taskHolder.setCalculationFuture(null);

					try
					{
						var plan = future.get();

						if (plan == null)
						{
							networkCrafting.setText(Component.literal("ERROR: NO_PLAN"));
							toRemove.add(requestId);
							return true;
						}

						ICraftingSubmitResult result = grid.getCraftingService().submitJob(plan, null, null, false, action);

						if (result != null && result.successful())
						{
							var craftingLink = result.link();

							if (craftingLink != null)
							{
								taskHolder.setCraftingLink(craftingLink);
								networkCrafting.setText(Component.literal("CRAFTING"));
							}
							else
							{
								networkCrafting.setText(Component.literal("ERROR: SUBMISSION_FAILED"));
							}
						}
						else
						{
							networkCrafting.setText(Component.literal("ERROR: MISSING_ITEMS"));
						}
					}
					catch (Exception e)
					{
						networkCrafting.setText(Component.literal("ERROR: " + e));
						toRemove.add(requestId);
						return true;
					}
				}
				else
				{
					networkCrafting.setText(Component.literal("CALCULATING"));
				}

				return true;
			}

			var output = this.calculateAutocrafting(deliverable);

			if (output.isEmpty())
			{
				toRemove.add(requestId);
				return true;
			}

			var outputKey = AEItemKey.of(output);
			taskHolder.setOutputKey(outputKey);
			taskHolder.setCalculationStartTick(CitizenTerminalPart.this.getLevel().getGameTime());
			var inventory = grid.getStorageService().getInventory();
			var alreadyAvailable = inventory.extract(outputKey, deliverable.getCount(), Actionable.SIMULATE, action);
			var craftingCount = deliverable.getCount() - alreadyAvailable;

			if (craftingCount <= 0)
			{
				toRemove.add(requestId);
				return true;
			}

			var calculationFuture = grid.getCraftingService().beginCraftingCalculation(
				CitizenTerminalPart.this.getLevel(),
				(ICraftingSimulationRequester) () -> action,
				outputKey,
				craftingCount,
				CalculationStrategy.REPORT_MISSING_ITEMS
			);
			taskHolder.setCalculationFuture(calculationFuture);
			networkCrafting.setText(Component.literal("CALCULATING"));
			return false;
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

	}

}
