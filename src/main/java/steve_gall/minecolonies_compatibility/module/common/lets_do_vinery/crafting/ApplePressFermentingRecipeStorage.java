package steve_gall.minecolonies_compatibility.module.common.lets_do_vinery.crafting;

import java.util.Collections;
import java.util.List;

import com.minecolonies.api.colony.requestsystem.factory.IFactoryController;
import com.minecolonies.api.crafting.ItemStorage;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import steve_gall.minecolonies_compatibility.api.common.crafting.SimpleContainerRecipeStorage;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;

public class ApplePressFermentingRecipeStorage extends SimpleContainerRecipeStorage<ApplePressFermentingGenericRecipe>
{
	public static final ResourceLocation ID = MineColoniesCompatibility.rl("lets_do_vinery_apple_press_fermenting");

	public ApplePressFermentingRecipeStorage(HolderLookup.Provider provider, IFactoryController controller, CompoundTag tag)
	{
		super(provider, controller, tag);
	}

	public ApplePressFermentingRecipeStorage(ResourceLocation recipeId, List<ItemStorage> ingredients, ItemStorage container, ItemStack output)
	{
		super(recipeId, ingredients, container, output);
	}

	@Override
	public ResourceLocation getId()
	{
		return ID;
	}

	@Override
	public List<ItemStack> getSecondaryOutputs()
	{
		return Collections.emptyList();
	}

	@Override
	protected ContainerGenericRecipeFactory<ApplePressFermentingGenericRecipe> getContainerGenericRecipeFactory()
	{
		return ApplePressFermentingGenericRecipe::new;
	}

}
