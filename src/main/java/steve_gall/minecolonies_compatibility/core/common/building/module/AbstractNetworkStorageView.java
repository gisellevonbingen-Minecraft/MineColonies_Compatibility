package steve_gall.minecolonies_compatibility.core.common.building.module;

import java.util.Optional;

import com.minecolonies.api.MinecoloniesAPIProxy;
import com.minecolonies.api.util.BlockPosUtil;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import steve_gall.minecolonies_compatibility.api.common.building.module.INetworkStorageView;
import steve_gall.minecolonies_compatibility.core.common.init.ModBuildingModules;

public abstract class AbstractNetworkStorageView implements INetworkStorageView
{
	private static final String TAG_COLONY_ID = "colonyId";
	private static final String TAG_WAREHOUSE_POS = "warehousePos";

	private int colonyId = 0;
	private Optional<BlockPos> warehousePos = Optional.empty();

	private NetworkStorageModule module = null;

	public AbstractNetworkStorageView()
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
		}

	}

	public boolean read(CompoundTag tag)
	{
		var colonyId = this.colonyId;
		var warehousePos = this.warehousePos;

		this.colonyId = tag.getInt(TAG_COLONY_ID);
		this.warehousePos = tag.contains(TAG_WAREHOUSE_POS) ? Optional.of(BlockPosUtil.read(tag, TAG_WAREHOUSE_POS)) : Optional.empty();

		return this.colonyId != colonyId || !this.warehousePos.equals(warehousePos);
	}

	public CompoundTag write()
	{
		var tag = new CompoundTag();
		tag.putInt(TAG_COLONY_ID, this.colonyId);
		this.warehousePos.ifPresent(p -> BlockPosUtil.write(tag, TAG_WAREHOUSE_POS, p));
		return tag;
	}

	public boolean read(FriendlyByteBuf buffer)
	{
		var colonyId = this.colonyId;
		var warehousePos = this.warehousePos;
		this.colonyId = buffer.readInt();
		this.warehousePos = buffer.readOptional(FriendlyByteBuf::readBlockPos);

		return this.colonyId != colonyId || !this.warehousePos.equals(warehousePos);
	}

	public void write(FriendlyByteBuf buffer)
	{
		buffer.writeInt(this.colonyId);
		buffer.writeOptional(this.warehousePos, FriendlyByteBuf::writeBlockPos);
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

}
