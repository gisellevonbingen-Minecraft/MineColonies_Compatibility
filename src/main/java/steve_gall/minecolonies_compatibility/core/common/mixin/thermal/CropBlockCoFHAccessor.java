package steve_gall.minecolonies_compatibility.core.common.mixin.thermal;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import cofh.lib.block.CropBlockCoFH;
import net.minecraft.world.level.ItemLike;

@Mixin(value = CropBlockCoFH.class, remap = false)
public interface CropBlockCoFHAccessor
{
	@Invoker(value = "getCropItem", remap = false)
	ItemLike invokeGetCropItem();

	@Invoker(value = "getPostHarvestAge", remap = false)
	int invokeGetPostHarvestAge();
}
