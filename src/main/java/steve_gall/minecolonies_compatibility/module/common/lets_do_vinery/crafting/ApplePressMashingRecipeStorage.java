package steve_gall.minecolonies_compatibility.module.common.lets_do_vinery.crafting;

import java.util.Collections;
import java.util.List;

import com.minecolonies.api.colony.requestsystem.factory.IFactoryController;
import com.minecolonies.api.crafting.ItemStorage;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import steve_gall.minecolonies_compatibility.api.common.crafting.SimpleRecipeStorage;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;

public class ApplePressMashingRecipeStorage extends SimpleRecipeStorage<ApplePressMashingGenericRecipe>
{
	public static final ResourceLocation ID = MineColoniesCompatibility.rl("lets_do_vinery_apple_press");

	public ApplePressMashingRecipeStorage(IFactoryController controller, CompoundTag tag)
	{
		super(controller, tag);
	}

	public ApplePressMashingRecipeStorage(ResourceLocation recipeId, List<ItemStorage> ingredients, ItemStack output)
	{
		super(recipeId, ingredients, output);
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
	protected GenericRecipeFactory<ApplePressMashingGenericRecipe> getGenericRecipeFactory()
	{
		return ApplePressMashingGenericRecipe::new;
	}

}
