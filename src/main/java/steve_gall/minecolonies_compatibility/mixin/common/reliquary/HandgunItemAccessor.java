package steve_gall.minecolonies_compatibility.mixin.common.reliquary;

import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.resources.ResourceLocation;
import reliquary.item.HandgunItem;
import reliquary.item.HandgunItem.IShotFactory;

@Pseudo
@Mixin(value = HandgunItem.class, remap = false)
public interface HandgunItemAccessor
{
	@Accessor(value = "magazineShotFactories", remap = false)
	Map<ResourceLocation, IShotFactory> getMagazineShotFactories();
}
