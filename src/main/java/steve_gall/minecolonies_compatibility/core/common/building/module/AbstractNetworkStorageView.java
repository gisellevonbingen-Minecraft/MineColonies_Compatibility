package steve_gall.minecolonies_compatibility.core.common.building.module;

import java.util.Optional;

import com.minecolonies.api.MinecoloniesAPIProxy;
import com.minecolonies.api.colony.requestsystem.manager.IRequestManager;
import com.minecolonies.api.colony.requestsystem.requestable.IDeliverable;
import com.minecolonies.api.colony.requestsystem.token.IToken;
import com.minecolonies.api.util.BlockPosUtil;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import steve_gall.minecolonies_compatibility.api.common.building.module.INetworkStorageView;
import steve_gall.minecolonies_compatibility.core.common.init.ModBuildingModules;
import steve_gall.minecolonies_compatibility.core.common.requestsystem.NetworkCrafting;
import steve_gall.minecolonies_tweaks.api.common.requestsystem.CustomizableRequestable;

public abstract class AbstractNetworkStorageView implements INetworkStorageView
{
	private static final String TAG_COLONY_ID = "colonyId";
	private static final String TAG_WAREHOUSE_POS = "warehousePos";

	private int colonyId = 0;
	private Optional<BlockPos> warehousePos = Optional.empty();

	private NetworkStorageModule module = null;
	private boolean wasActive = false;
	private int autocraftingTickCounter = 0;

	public AbstractNetworkStorageView()
	{

	}

	public void tick()
	{
		this.updateActive();

		if (this.wasActive)
		{
			if (--this.autocraftingTickCounter <= 0)
			{
				this.autocraftingTickCounter = 5;
				this.updateAutocraftings();
			}

		}

	}

	private void updateActive()
	{
		var isActive = this.isActive() && this.getLinkedModule() != null;

		if (this.wasActive != isActive)
		{
			this.onActiveChanged(isActive);
		}

		this.wasActive = isActive;
	}

	protected void onActiveChanged(boolean isActive)
	{

	}

	@Override
	public void link(NetworkStorageModule module)
	{
		if (module == null || module.isDestroyed())
		{
			return;
		}

		var building = module.getBuilding();
		this.colonyId = building.getColony().getID();
		this.warehousePos = Optional.of(building.getID());
		this.module = module;

		this.module.onLink(this);
	}

	@Override
	public void unlink()
	{
		var module = this.getLinkedModule0();

		this.colonyId = 0;
		this.warehousePos = Optional.empty();
		this.module = null;

		if (module != null)
		{
			module.onUnlink(this);
			this.onUnlink(module);
		}

	}

	protected void onUnlink(NetworkStorageModule module)
	{

	}

	public boolean readLink(CompoundTag tag)
	{
		var colonyId = this.colonyId;
		var warehousePos = this.warehousePos;

		this.colonyId = tag.getInt(TAG_COLONY_ID);
		this.warehousePos = tag.contains(TAG_WAREHOUSE_POS) ? Optional.of(BlockPosUtil.read(tag, TAG_WAREHOUSE_POS)) : Optional.empty();

		return this.colonyId != colonyId || !this.warehousePos.equals(warehousePos);
	}

	public CompoundTag writeLink()
	{
		var tag = new CompoundTag();
		tag.putInt(TAG_COLONY_ID, this.colonyId);
		this.warehousePos.ifPresent(p -> BlockPosUtil.write(tag, TAG_WAREHOUSE_POS, p));
		return tag;
	}

	public boolean readLink(FriendlyByteBuf buffer)
	{
		var colonyId = this.colonyId;
		var warehousePos = this.warehousePos;
		this.colonyId = buffer.readInt();
		this.warehousePos = buffer.readOptional(FriendlyByteBuf::readBlockPos);

		return this.colonyId != colonyId || !this.warehousePos.equals(warehousePos);
	}

	public void writeLink(FriendlyByteBuf buffer)
	{
		buffer.writeInt(this.colonyId);
		buffer.writeOptional(this.warehousePos, FriendlyByteBuf::writeBlockPos);
	}

	public void readData(CompoundTag tag)
	{

	}

	public CompoundTag writeData()
	{
		var tag = new CompoundTag();
		this.writeData(tag);
		return tag;
	}

	public void writeData(CompoundTag tag)
	{

	}

	@Override
	public Optional<BlockPos> getLinkedPos()
	{
		return this.warehousePos;
	}

	@Override
	public NetworkStorageModule getLinkedModule()
	{
		var module = this.getLinkedModule0();

		if (module == null || module.isDestroyed())
		{
			if (!this.warehousePos.isEmpty())
			{
				this.unlink();
			}

			return null;
		}

		return module;
	}

	@Override
	public NetworkStorageModuleView getLinkedModuleView()
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

	private NetworkStorageModule getLinkedModule0()
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

	protected NetworkCrafting getNetworkCrafting(IRequestManager requestManager, IToken<?> requestId)
	{
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

	protected IDeliverable getDeliverable(IRequestManager requestManager, IToken<?> requestId)
	{
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

	public boolean wasActive()
	{
		return this.wasActive;
	}

}
