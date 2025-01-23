package steve_gall.minecolonies_compatibility.module.common.lets_do_meadow.crafting;

import java.util.List;

import com.minecolonies.api.crafting.ItemStorage;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import steve_gall.minecolonies_compatibility.api.common.crafting.SimpleRecipeStorage;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;

public class WoodcuttingRecipeStorage extends SimpleRecipeStorage<WoodcuttingGenericRecipe>
{
	public static final ResourceLocation ID = MineColoniesCompatibility.rl("lets_do_meadow_woodcutting");

	public WoodcuttingRecipeStorage(CompoundTag tag)
	{
		super(tag);
	}

	public WoodcuttingRecipeStorage(ResourceLocation recipeId, List<ItemStorage> ingredients, ItemStack output)
	{
		super(recipeId, ingredients, output);
	}

	@Override
	public ResourceLocation getId()
	{
		return ID;
	}

	@Override
	protected GenericRecipeFactory<WoodcuttingGenericRecipe> getGenericRecipeFactory()
	{
		return WoodcuttingGenericRecipe::new;
	}

}
