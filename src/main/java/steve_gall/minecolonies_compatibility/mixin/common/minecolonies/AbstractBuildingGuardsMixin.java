package steve_gall.minecolonies_compatibility.mixin.common.minecolonies;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.minecolonies.api.colony.IColony;
import com.minecolonies.api.util.ItemStackUtils;
import com.minecolonies.api.util.constant.EquipmentLevelConstants;
import com.minecolonies.core.colony.buildings.AbstractBuilding;
import com.minecolonies.core.colony.buildings.AbstractBuildingGuards;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Tuple;
import steve_gall.minecolonies_compatibility.core.common.init.ModToolTypes;

@Mixin(value = AbstractBuildingGuards.class, remap = false)
public abstract class AbstractBuildingGuardsMixin extends AbstractBuilding
{
	protected AbstractBuildingGuardsMixin(@NotNull IColony colony, BlockPos pos)
	{
		super(colony, pos);
	}

	@Inject(method = "<init>", remap = false, at = @At(value = "TAIL"), cancellable = false)
	private void init(IColony c, BlockPos l, CallbackInfo ci)
	{
		this.keepX.put(itemStack -> ItemStackUtils.hasEquipmentLevel(itemStack, ModToolTypes.RANGER_WEAPON.getToolType(), EquipmentLevelConstants.TOOL_LEVEL_WOOD_OR_GOLD, this.getMaxEquipmentLevel()), new Tuple<>(1, true));
		this.keepX.put(itemStack -> ItemStackUtils.hasEquipmentLevel(itemStack, ModToolTypes.KNIGHT_WEAPON.getToolType(), EquipmentLevelConstants.TOOL_LEVEL_WOOD_OR_GOLD, this.getMaxEquipmentLevel()), new Tuple<>(1, true));
		this.keepX.put(itemStack -> ItemStackUtils.hasEquipmentLevel(itemStack, ModToolTypes.GUN.getToolType(), EquipmentLevelConstants.TOOL_LEVEL_WOOD_OR_GOLD, this.getMaxEquipmentLevel()), new Tuple<>(1, true));
	}

}
