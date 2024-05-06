package steve_gall.minecolonies_compatibility.core.common.mixin.minecolonies;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.minecolonies.api.colony.IColony;
import com.minecolonies.api.colony.buildings.IBuilding;
import com.minecolonies.api.colony.buildings.ModBuildings;
import com.minecolonies.api.colony.buildings.registry.BuildingEntry;
import com.minecolonies.core.colony.buildings.modules.BuildingModules;

import net.minecraft.core.BlockPos;
import steve_gall.minecolonies_compatibility.core.common.init.ModBuildingModules;

@Mixin(value = BuildingEntry.class, remap = false)
public abstract class BuildingEntryMixin
{
	@Inject(method = "produceBuilding", remap = false, at = @At(value = "TAIL"), cancellable = true)
	private void produceBuilding(BlockPos position, IColony colony, CallbackInfoReturnable<IBuilding> cir)
	{
		var building = cir.getReturnValue();

		if (building.getBuildingType() == ModBuildings.lumberjack.get())
		{
			var module = building.getModule(BuildingModules.FORESTER_SETTINGS);

			if (module != null)
			{
				ModBuildingModules.ORCHARDIST_SETTINGS.stream().forEach(pair -> module.with(pair.getFirst(), pair.getSecond()));
			}

		}

	}

}
