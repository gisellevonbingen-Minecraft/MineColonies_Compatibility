package steve_gall.minecolonies_compatibility.module.common.farmersdelight.crafting;

import java.util.List;

import com.minecolonies.api.colony.requestsystem.factory.IFactoryController;
import com.minecolonies.api.crafting.ItemStorage;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import steve_gall.minecolonies_compatibility.api.common.crafting.SimpleContainerRecipeStorage;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;

public class CookingRecipeStorage extends SimpleContainerRecipeStorage<CookingGenericRecipe>
{
	public static final ResourceLocation ID = MineColoniesCompatibility.rl("farmerdelight_cooking");

	public CookingRecipeStorage(HolderLookup.Provider provider, IFactoryController controller, CompoundTag tag)
	{
		super(provider, controller, tag);
	}

	public CookingRecipeStorage(ResourceLocation recipeId, List<ItemStorage> ingredients, ItemStorage container, ItemStack output)
	{
		super(recipeId, ingredients, container, output);
	}

	@Override
	public ResourceLocation getId()
	{
		return ID;
	}

	@Override
	protected ContainerGenericRecipeFactory<CookingGenericRecipe> getContainerGenericRecipeFactory()
	{
		return CookingGenericRecipe::new;
	}

}
