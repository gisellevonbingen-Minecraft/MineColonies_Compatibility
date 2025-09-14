package steve_gall.minecolonies_compatibility.module.common.tacz.crafting;

import java.util.List;

import com.minecolonies.api.colony.requestsystem.factory.IFactoryController;
import com.minecolonies.api.crafting.ItemStorage;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import steve_gall.minecolonies_compatibility.api.common.crafting.SimpleRecipeStorage;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;

public class GunSmithTableRecipeStorage extends SimpleRecipeStorage<GunSmithTableGenericRecipe>
{
	public static final ResourceLocation ID = MineColoniesCompatibility.rl("tacz_gun_smith_table");

	public GunSmithTableRecipeStorage(IFactoryController controller, CompoundTag tag)
	{
		super(controller, tag);
	}

	public GunSmithTableRecipeStorage(ResourceLocation recipeId, List<ItemStorage> ingredients, ItemStack output)
	{
		super(recipeId, ingredients, output);
	}

	@Override
	public ResourceLocation getId()
	{
		return ID;
	}

	@Override
	protected GenericRecipeFactory<GunSmithTableGenericRecipe> getGenericRecipeFactory()
	{
		return GunSmithTableGenericRecipe::new;
	}

}
