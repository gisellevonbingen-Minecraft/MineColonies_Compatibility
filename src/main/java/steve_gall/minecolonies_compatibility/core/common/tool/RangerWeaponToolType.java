package steve_gall.minecolonies_compatibility.core.common.tool;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

import com.minecolonies.api.equipment.registry.EquipmentTypeEntry;

import net.minecraft.resources.ResourceLocation;
import steve_gall.minecolonies_compatibility.core.common.config.MineColoniesCompatibilityConfigServer;
import steve_gall.minecolonies_compatibility.core.common.init.ModToolTypes;
import steve_gall.minecolonies_tweaks.api.common.tool.OrToolType;

public class RangerWeaponToolType extends OrToolType
{
	public RangerWeaponToolType(ResourceLocation name, Collection<Supplier<EquipmentTypeEntry>> toolTypes)
	{
		super(name, toolTypes);
	}

	@Override
	public List<Supplier<EquipmentTypeEntry>> getToolTypes()
	{
		var list = new ArrayList<>(super.getToolTypes());

		if (MineColoniesCompatibilityConfigServer.INSTANCE.jobs.ranger.canUseCrossbow.get().booleanValue())
		{
			list.add(ModToolTypes.CROSSBOW::getToolType);
		}

		return list;
	}

}
