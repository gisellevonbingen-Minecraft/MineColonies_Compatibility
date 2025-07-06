package steve_gall.minecolonies_compatibility.core.common.init;

import com.minecolonies.api.crafting.RecipeCraftingType;
import com.minecolonies.api.crafting.registry.CraftingType;

import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.StonecutterRecipe;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.core.common.crafting.BucketFillingCraftingType;
import steve_gall.minecolonies_compatibility.core.common.crafting.SmithingCraftingType;
import steve_gall.minecolonies_tweaks.api.registries.DeferredRegisterHelper;

public class ModCraftingTypes
{
	public static final DeferredRegister<CraftingType> REGISTER = DeferredRegisterHelper.craftingTypes(MineColoniesCompatibility.MOD_ID);
	public static final RegistryObject<BucketFillingCraftingType> BUCKET_FILLING = DeferredRegisterHelper.registerCraftingType(REGISTER, "bucket_filling", BucketFillingCraftingType::new);
	public static final RegistryObject<SmithingCraftingType> SMITHING = DeferredRegisterHelper.registerCraftingType(REGISTER, "smithing", SmithingCraftingType::new);
	public static final RegistryObject<RecipeCraftingType<Container, StonecutterRecipe>> STONECUTTING = DeferredRegisterHelper.registerCraftingType(REGISTER, "stonecutting", id -> new RecipeCraftingType<>(id, RecipeType.STONECUTTING, null));
}
