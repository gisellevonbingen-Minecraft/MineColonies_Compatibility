package steve_gall.minecolonies_compatibility.core.common.network.message;

import com.minecolonies.api.colony.IColonyManager;
import com.minecolonies.api.colony.buildings.modules.IBuildingModule;
import com.minecolonies.api.colony.buildings.modules.IBuildingModuleView;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import steve_gall.minecolonies_compatibility.core.common.network.AbstractMessage;

public abstract class BuildingModuleMessage extends AbstractMessage
{
	private final ResourceKey<Level> dimensionId;
	private final int coloyId;
	private final BlockPos buildingId;
	private final int moduleId;

	public BuildingModuleMessage(IBuildingModule module)
	{
		var building = module.getBuilding();
		this.dimensionId = building.getColony().getDimension();
		this.coloyId = building.getColony().getID();
		this.buildingId = building.getID();
		this.moduleId = module.getProducer().getRuntimeID();
	}

	public BuildingModuleMessage(IBuildingModuleView module)
	{
		var building = module.getBuildingView();
		this.dimensionId = building.getColony().getDimension();
		this.coloyId = building.getColony().getID();
		this.buildingId = building.getID();
		this.moduleId = module.getProducer().getRuntimeID();
	}

	public BuildingModuleMessage(FriendlyByteBuf buffer)
	{
		super(buffer);

		this.dimensionId = buffer.readResourceKey(Registry.DIMENSION_REGISTRY);
		this.coloyId = buffer.readInt();
		this.buildingId = buffer.readBlockPos();
		this.moduleId = buffer.readInt();
	}

	@Override
	public void encode(FriendlyByteBuf buffer)
	{
		super.encode(buffer);

		buffer.writeResourceKey(this.dimensionId);
		buffer.writeInt(this.coloyId);
		buffer.writeBlockPos(this.buildingId);
		buffer.writeInt(this.moduleId);
	}

	public IBuildingModule getModule()
	{
		var colony = IColonyManager.getInstance().getColonyByDimension(this.coloyId, this.dimensionId);

		if (colony == null)
		{
			return null;
		}

		var building = colony.getBuildingManager().getBuilding(this.buildingId);

		if (building == null)
		{
			return null;
		}

		return building.getModule(this.moduleId);
	}

	public IBuildingModuleView getModuleView()
	{
		var building = IColonyManager.getInstance().getBuildingView(this.dimensionId, this.buildingId);

		if (building == null)
		{
			return null;
		}

		return building.getModuleView(this.moduleId);
	}

	public ResourceKey<Level> getDimensionId()
	{
		return this.dimensionId;
	}

	public int getColoyId()
	{
		return this.coloyId;
	}

	public BlockPos getBuildingId()
	{
		return this.buildingId;
	}

	public int getModuleId()
	{
		return this.moduleId;
	}

}
