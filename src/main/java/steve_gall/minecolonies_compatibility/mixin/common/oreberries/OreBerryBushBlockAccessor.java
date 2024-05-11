package steve_gall.minecolonies_compatibility.mixin.common.oreberries;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import com.mrbysco.oreberriesreplanted.block.OreBerryBushBlock;

import net.minecraft.world.level.ItemLike;

@Mixin(value = OreBerryBushBlock.class, remap = false)
public interface OreBerryBushBlockAccessor
{
	@Invoker(value = "getBerryItem", remap = false)
	ItemLike invokeGetBerryItem();
}
