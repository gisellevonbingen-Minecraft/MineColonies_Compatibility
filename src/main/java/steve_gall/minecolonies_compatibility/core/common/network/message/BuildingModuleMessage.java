package steve_gall.minecolonies_compatibility.core.common.network.message;

import com.minecolonies.api.colony.buildings.modules.IBuildingModule;
import com.minecolonies.api.colony.buildings.modules.IBuildingModuleView;

import net.minecraft.network.FriendlyByteBuf;
import steve_gall.minecolonies_tweaks.api.common.building.module.ModulePos;
import steve_gall.minecolonies_tweaks.api.common.network.AbstractMessage;

public abstract class BuildingModuleMessage extends AbstractMessage
{
	private final ModulePos modulePos;

	public BuildingModuleMessage(IBuildingModule module)
	{
		this.modulePos = new ModulePos(module);
	}

	public BuildingModuleMessage(IBuildingModuleView module)
	{
		this.modulePos = new ModulePos(module);
	}

	public BuildingModuleMessage(FriendlyByteBuf buffer)
	{
		super(buffer);

		this.modulePos = new ModulePos(buffer);
	}

	@Override
	public void encode(FriendlyByteBuf buffer)
	{
		super.encode(buffer);

		this.modulePos.serializeBuffer(buffer);
	}

	public IBuildingModule getModule()
	{
		return this.modulePos.getModule();
	}

	public IBuildingModuleView getModuleView()
	{
		return this.modulePos.getModuleView();
	}

	public ModulePos getModulePos()
	{
		return this.modulePos;
	}

}
