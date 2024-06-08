package steve_gall.minecolonies_compatibility.module.common.refinedstorage;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import com.minecolonies.api.MinecoloniesAPIProxy;
import com.minecolonies.api.util.BlockPosUtil;
import com.refinedmods.refinedstorage.api.network.INetwork;
import com.refinedmods.refinedstorage.api.network.security.Permission;
import com.refinedmods.refinedstorage.api.storage.cache.IStorageCacheListener;
import com.refinedmods.refinedstorage.api.util.Action;
import com.refinedmods.refinedstorage.api.util.StackListEntry;
import com.refinedmods.refinedstorage.api.util.StackListResult;
import com.refinedmods.refinedstorage.apiimpl.network.node.NetworkNode;
import com.refinedmods.refinedstorage.util.LevelUtils;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.core.common.building.module.INetworkStorageView;
import steve_gall.minecolonies_compatibility.core.common.building.module.NetworkStorageModule;
import steve_gall.minecolonies_compatibility.core.common.building.module.NetworkStorageModuleView;
import steve_gall.minecolonies_compatibility.core.common.config.MineColoniesCompatibilityConfigServer;
import steve_gall.minecolonies_compatibility.core.common.init.ModBuildingModules;

public class CitizenGridNetworkNode extends NetworkNode implements INetworkStorageView
{
	private static final String TAG_LINK = "link";
	private static final String TAG_COLONY_ID = "colonyId";
	private static final String TAG_WAREHOUSE_POS = "warehousePos";

	public static final ResourceLocation ID = MineColoniesCompatibility.rl("citizen_grid");

	private final StorageListener listener;

	private int colonyId;
	private Optional<BlockPos> warehousePos;

	private NetworkStorageModule module;

	public CitizenGridNetworkNode(Level level, BlockPos pos)
	{
		super(level, pos);

		this.listener = new StorageListener();
		this.colonyId = 0;
		this.warehousePos = Optional.empty();
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
	public boolean canExtract()
	{
		var network = this.getNetwork();

		if (network == null)
		{
			return false;
		}

		return RefinedStorageModule.hasPermission(this.getPairedModule().getBuilding().getColony(), network, Permission.EXTRACT);
	}

	@Override
	public boolean canInsert()
	{
		var network = this.getNetwork();

		if (network == null)
		{
			return false;
		}

		return RefinedStorageModule.hasPermission(this.getPairedModule().getBuilding().getColony(), network, Permission.INSERT);
	}

	@Override
	public List<ItemStack> getMatchingStacks(Predicate<ItemStack> predicate)
	{
		var network = this.getNetwork();

		if (network == null)
		{
			return Collections.emptyList();
		}

		var stackList = network.getItemStorageCache().getList();
		return stackList.getStacks().stream().map(StackListEntry<ItemStack>::getStack).filter(predicate).toList();
	}

	@Override
	public boolean extract(IItemHandlerModifiable to, ItemStack stack)
	{
		var network = this.getNetwork();

		if (network == null)
		{
			return false;
		}

		var extracting = network.extractItem(stack, stack.getCount(), Action.SIMULATE);

		if (extracting.isEmpty())
		{
			return false;
		}

		var remain = ItemHandlerHelper.insertItem(to, extracting, true);

		if (remain.isEmpty())
		{
			var extracted = network.extractItem(stack, stack.getCount(), Action.PERFORM);
			ItemHandlerHelper.insertItem(to, extracted, false);
			return true;
		}

		return false;
	}

	@Override
	public ItemStack insert(IItemHandlerModifiable from, ItemStack stack)
	{
		var network = this.getNetwork();

		if (network == null)
		{
			return stack;
		}

		return network.insertItem(stack, stack.getCount(), Action.PERFORM);
	}

	@Override
	public void link(NetworkStorageModule module)
	{
		var building = module.getBuilding();
		this.colonyId = building.getColony().getID();
		this.warehousePos = Optional.of(building.getID());
		this.module = module;

		this.module.onLink(this);

		this.markDirty();
		LevelUtils.updateBlock(this.level, this.pos);
	}

	@Override
	public void unlink()
	{
		var module = this.getPairedModule0();

		if (module != null)
		{
			module.onUnlink(this);
		}

		this.colonyId = 0;
		this.warehousePos = Optional.empty();
		this.module = null;

		this.markDirty();
		LevelUtils.updateBlock(this.level, this.pos);
	}

	@Override
	public NetworkStorageModule getPairedModule()
	{
		var module = this.getPairedModule0();

		if (module == null || module.isDestroyed())
		{
			this.unlink();
			return null;
		}

		return module;
	}

	public NetworkStorageModuleView getPairedModuleView()
	{
		if (this.warehousePos.isEmpty())
		{
			return null;
		}

		var colony = MinecoloniesAPIProxy.getInstance().getColonyManager().getColonyView(this.colonyId, this.getLevel().dimension());

		if (colony == null)
		{
			return null;
		}

		var building = colony.getBuilding(this.warehousePos.get());

		if (building == null)
		{
			return null;
		}

		return building.getModuleView(ModBuildingModules.NETWORK_STORAGE);
	}

	private NetworkStorageModule getPairedModule0()
	{
		if (this.module == null)
		{
			if (this.warehousePos.isEmpty())
			{
				return null;
			}

			var colony = MinecoloniesAPIProxy.getInstance().getColonyManager().getColonyByWorld(this.colonyId, this.getLevel());

			if (colony == null)
			{
				return null;
			}

			var building = colony.getBuildingManager().getBuilding(this.warehousePos.get());

			if (building == null)
			{
				return null;
			}

			this.module = building.getModule(ModBuildingModules.NETWORK_STORAGE);
		}

		return this.module;
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

		tag.put(TAG_LINK, this.writeLink());

		return tag;
	}

	public CompoundTag writeLink()
	{
		var tag = new CompoundTag();
		tag.putInt(TAG_COLONY_ID, this.colonyId);
		this.warehousePos.ifPresent(p -> BlockPosUtil.write(tag, TAG_WAREHOUSE_POS, p));
		return tag;
	}

	@Override
	public void read(CompoundTag tag)
	{
		super.read(tag);

		this.readLink(tag.getCompound(TAG_LINK));
	}

	public void readLink(CompoundTag tag)
	{
		this.colonyId = tag.getInt(TAG_COLONY_ID);
		this.warehousePos = tag.contains(TAG_WAREHOUSE_POS) ? Optional.of(BlockPosUtil.read(tag, TAG_WAREHOUSE_POS)) : Optional.empty();
	}

	public int getColonyId()
	{
		return this.colonyId;
	}

	public Optional<BlockPos> getWarehousePos()
	{
		return this.warehousePos;
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
			if (!isActive())
			{
				return;
			}

			var module = getPairedModule();

			if (module != null && result.getChange() > 0)
			{
				module.onItemIncremented(result.getStack());
			}

		}

		@Override
		public void onChangedBulk(List<StackListResult<ItemStack>> results)
		{
			if (!isActive())
			{
				return;
			}

			var module = getPairedModule();

			if (module == null)
			{
				return;
			}

			for (var result : results)
			{
				if (result.getChange() > 0)
				{
					module.onItemIncremented(result.getStack());
				}

			}

		}

		@Override
		public void onInvalidated()
		{

		}

	}

}
