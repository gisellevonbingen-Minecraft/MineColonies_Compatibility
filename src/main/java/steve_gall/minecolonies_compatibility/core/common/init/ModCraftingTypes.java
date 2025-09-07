package steve_gall.minecolonies_compatibility.core.common.init;

import com.minecolonies.api.crafting.RecipeCraftingType;
import com.minecolonies.api.crafting.registry.CraftingType;

import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.core.common.crafting.BucketFillingCraftingType;
import steve_gall.minecolonies_compatibility.core.common.crafting.SmithingCraftingType;
import steve_gall.minecolonies_tweaks.api.registries.DeferredRegisterHelper;

public class ModCraftingTypes
{
	public static final DeferredRegister<CraftingType> REGISTER = DeferredRegisterHelper.craftingTypes(MineColoniesCompatibility.MOD_ID);
	public static final DeferredHolder<CraftingType, BucketFillingCraftingType> BUCKET_FILLING = DeferredRegisterHelper.registerCraftingType(REGISTER, "bucket_filling", BucketFillingCraftingType::new);
	public static final DeferredHolder<CraftingType, SmithingCraftingType> SMITHING = DeferredRegisterHelper.registerCraftingType(REGISTER, "smithing", SmithingCraftingType::new);
	public static final DeferredHolder<CraftingType, CraftingType> STONECUTTING = DeferredRegisterHelper.registerCraftingType(REGISTER, "stonecutting", id -> new RecipeCraftingType<>(id, RecipeType.STONECUTTING, null));
}
