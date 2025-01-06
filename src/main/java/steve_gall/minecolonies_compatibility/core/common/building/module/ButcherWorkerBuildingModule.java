package steve_gall.minecolonies_compatibility.core.common.building.module;

import java.util.function.Function;
import java.util.function.Predicate;

import org.apache.logging.log4j.util.TriConsumer;

import com.minecolonies.api.colony.buildings.IBuilding;
import com.minecolonies.api.colony.buildings.modules.IAltersRequiredItems;
import com.minecolonies.api.colony.jobs.registry.JobEntry;
import com.minecolonies.api.entity.citizen.Skill;
import com.minecolonies.api.equipment.ModEquipmentTypes;
import com.minecolonies.core.colony.buildings.modules.WorkerBuildingModule;

import net.minecraft.world.item.ItemStack;
import steve_gall.minecolonies_compatibility.core.common.init.ModToolTypes;

public class ButcherWorkerBuildingModule extends WorkerBuildingModule implements IAltersRequiredItems
{
	public ButcherWorkerBuildingModule(JobEntry entry, Skill primary, Skill secondary, boolean canWorkingDuringRain, Function<IBuilding, Integer> sizeLimit)
	{
		super(entry, primary, secondary, canWorkingDuringRain, sizeLimit);
	}

	@Override
	public void alterItemsToBeKept(TriConsumer<Predicate<ItemStack>, Integer, Boolean> consumer)
	{
		consumer.accept(ModToolTypes.BUTCHER_TOOL.getToolType()::checkIsEquipment, 1, true);
		consumer.accept(ModEquipmentTypes.shears.get()::checkIsEquipment, 1, true);
	}

}
