package steve_gall.minecolonies_compatibility.module.common.ae2;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import appeng.api.config.Actionable;
import appeng.api.config.SecurityPermissions;
import appeng.api.networking.IGridNode;
import appeng.api.networking.IStackWatcher;
import appeng.api.networking.security.IActionSource;
import appeng.api.networking.storage.IStorageWatcherNode;
import appeng.api.networking.ticking.IGridTickable;
import appeng.api.networking.ticking.TickRateModulation;
import appeng.api.networking.ticking.TickingRequest;
import appeng.api.parts.IPartItem;
import appeng.api.parts.IPartModel;
import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.AEKey;
import appeng.api.stacks.KeyCounter;
import appeng.items.parts.PartModels;
import appeng.menu.MenuOpener;
import appeng.menu.locator.MenuLocators;
import appeng.parts.PartModel;
import appeng.parts.reporting.AbstractDisplayPart;
import it.unimi.dsi.fastutil.objects.Object2LongMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import steve_gall.minecolonies_compatibility.api.common.building.module.INetworkStorageView;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.core.common.building.module.NetworkStorageModule;
import steve_gall.minecolonies_compatibility.core.common.building.module.QueueNetworkStorageView;
import steve_gall.minecolonies_compatibility.core.common.colony.ColonyHelper;
import steve_gall.minecolonies_compatibility.module.common.ae2.init.ModuleMenuTypes;

public class CitizenTerminalPart extends AbstractDisplayPart implements IStorageWatcherNode, IGridTickable
{
	@PartModels
	public static final ResourceLocation MODEL_OFF = MineColoniesCompatibility.rl("part/citizen_terminal_off");
	@PartModels
	public static final ResourceLocation MODEL_ON = MineColoniesCompatibility.rl("part/citizen_terminal_on");

	public static final IPartModel MODELS_OFF = new PartModel(MODEL_BASE, MODEL_OFF, MODEL_STATUS_OFF);
	public static final IPartModel MODELS_ON = new PartModel(MODEL_BASE, MODEL_ON, MODEL_STATUS_ON);
	public static final IPartModel MODELS_HAS_CHANNEL = new PartModel(MODEL_BASE, MODEL_ON, MODEL_STATUS_HAS_CHANNEL);

	private static final String TAG_LINK = "link";

	private final StorageView view;
	private final KeyCounter counter;
	private final IActionSource action;

	public CitizenTerminalPart(IPartItem<?> partItem)
	{
		super(partItem, false);

		this.view = new StorageView();
		this.counter = new KeyCounter();
		this.action = IActionSource.ofMachine(this);

		var mainNode = this.getMainNode();
		mainNode.addService(IStorageWatcherNode.class, this);
		mainNode.addService(IGridTickable.class, this);
	}

	public boolean hasPermission(SecurityPermissions permissions)
	{
		var module = this.view.getLinkedModule();
		var grid = this.getMainNode().getGrid();

		if (module == null || grid == null)
		{
			return false;
		}

		var colony = module.getBuilding().getColony();
		var owner = ColonyHelper.getFakeOwner(colony);
		return grid.getSecurityService().hasPermission(owner, permissions);
	}
	
	@Override
	public void removeFromWorld()
	{
		super.removeFromWorld();
		
		this.view.unlink();
	}

	@Override
	public boolean onPartActivate(Player player, InteractionHand hand, Vec3 pos)
	{
		if (!super.onPartActivate(player, hand, pos) && !isClientSide())
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
		return new TickingRequest(1, 1, false, true);
	}

	@Override
	public TickRateModulation tickingRequest(IGridNode node, int ticksSinceLastCall)
	{
		this.view.onTick();
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
				}

			});
		}

	}

	@Override
	public void readFromNBT(CompoundTag data)
	{
		super.readFromNBT(data);

		this.view.read(data.getCompound(TAG_LINK));
	}

	@Override
	public void writeToNBT(CompoundTag data)
	{
		super.writeToNBT(data);

		data.put(TAG_LINK, this.view.write());
	}

	@Override
	public boolean readFromStream(FriendlyByteBuf data)
	{
		var needRedraw = super.readFromStream(data);

		var changed = this.view.read(data);
		return needRedraw || changed;
	}

	@Override
	public void writeToStream(FriendlyByteBuf data)
	{
		super.writeToStream(data);

		this.view.write(data);
	}

	public @NotNull INetworkStorageView getView()
	{
		return this.view;
	}

	public class StorageView extends QueueNetworkStorageView
	{
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
			return hasPermission(SecurityPermissions.EXTRACT);
		}

		@Override
		public boolean canInsert()
		{
			return hasPermission(SecurityPermissions.INJECT);
		}

		@Override
		public Stream<ItemStack> getAllStacks()
		{
			return StreamSupport.stream(counter.spliterator(), false).map(CitizenTerminalPart::toStack);
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
		protected void enqueueAll()
		{
			for (var entry : counter)
			{
				view.enqueue(toStack(entry));
			}

		}

	}

}
