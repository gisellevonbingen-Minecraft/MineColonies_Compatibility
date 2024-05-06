package steve_gall.minecolonies_compatibility.core.common.mixin.thermal;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import cofh.lib.common.block.CropBlockCoFH;
import net.minecraft.world.level.ItemLike;

@Mixin(value = CropBlockCoFH.class, remap = false)
public interface CropBlockCoFHAccessor
{
	@Invoker("getCropItem")
	ItemLike invokeGetCropItem();

	@Invoker("getPostHarvestAge")
	int invokeGetPostHarvestAge();
}
