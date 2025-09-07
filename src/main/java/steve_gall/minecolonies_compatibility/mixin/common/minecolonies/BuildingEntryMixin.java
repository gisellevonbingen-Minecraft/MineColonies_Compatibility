package steve_gall.minecolonies_compatibility.mixin.common.minecolonies;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.minecolonies.api.colony.IColony;
import com.minecolonies.api.colony.buildings.IBuilding;
import com.minecolonies.api.colony.buildings.registry.BuildingEntry;

import net.minecraft.core.BlockPos;
import net.neoforged.neoforge.common.NeoForge;
import steve_gall.minecolonies_compatibility.core.common.building.module.InjectBuildingSettingsModuleEvent;

@Mixin(value = BuildingEntry.class, remap = false)
public abstract class BuildingEntryMixin
{
	@Inject(method = "produceBuilding", remap = false, at = @At(value = "TAIL"), cancellable = true)
	private void produceBuilding(BlockPos position, IColony colony, CallbackInfoReturnable<IBuilding> cir)
	{
		NeoForge.EVENT_BUS.post(new InjectBuildingSettingsModuleEvent(cir.getReturnValue()));
	}

}
