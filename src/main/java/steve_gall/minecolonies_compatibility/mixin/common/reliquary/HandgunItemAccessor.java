package steve_gall.minecolonies_compatibility.mixin.common.reliquary;

import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import reliquary.items.HandgunItem;
import reliquary.items.HandgunItem.IShotEntityFactory;

@Mixin(value = HandgunItem.class, remap = false)
public interface HandgunItemAccessor
{
	@Accessor(value = "magazineShotFactories", remap = false)
	Map<String, IShotEntityFactory> getMagazineShotFactories();
}
