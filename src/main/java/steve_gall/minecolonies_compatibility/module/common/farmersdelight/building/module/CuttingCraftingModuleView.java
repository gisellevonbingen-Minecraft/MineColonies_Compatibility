package steve_gall.minecolonies_compatibility.module.common.farmersdelight.building.module;

import org.jetbrains.annotations.NotNull;

import com.minecolonies.api.IMinecoloniesAPI;
import com.minecolonies.api.equipment.ModEquipmentTypes;
import com.minecolonies.api.equipment.registry.EquipmentTypeEntry;
import com.minecolonies.core.colony.buildings.moduleviews.CraftingModuleView;

import net.minecraft.network.FriendlyByteBuf;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.module.common.farmersdelight.network.CuttingOpenTeachMessage;

public class CuttingCraftingModuleView extends CraftingModuleView
{
	private EquipmentTypeEntry toolType;

	public CuttingCraftingModuleView()
	{
		this.toolType = ModEquipmentTypes.none.get();
	}

	@Override
	public void deserialize(@NotNull FriendlyByteBuf buf)
	{
		super.deserialize(buf);

		this.toolType = buf.readRegistryIdUnsafe(IMinecoloniesAPI.getInstance().getEquipmentTypeRegistry());
	}

	@Override
	public void openCraftingGUI()
	{
		MineColoniesCompatibility.network().sendToServer(new CuttingOpenTeachMessage(this, this.getToolType()));
	}

	public EquipmentTypeEntry getToolType()
	{
		return this.toolType;
	}

}
