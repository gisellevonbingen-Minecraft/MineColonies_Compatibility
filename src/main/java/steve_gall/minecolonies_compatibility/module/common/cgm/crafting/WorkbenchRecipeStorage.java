package steve_gall.minecolonies_compatibility.module.common.cgm.crafting;

import java.util.List;

import com.minecolonies.api.colony.requestsystem.factory.IFactoryController;
import com.minecolonies.api.crafting.ItemStorage;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import steve_gall.minecolonies_compatibility.api.common.crafting.SimpleRecipeStorage;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;

public class WorkbenchRecipeStorage extends SimpleRecipeStorage<WorkbenchGenericRecipe>
{
	public static final ResourceLocation ID = MineColoniesCompatibility.rl("cgm_workbench");

	public WorkbenchRecipeStorage(IFactoryController controller, CompoundTag tag)
	{
		super(controller, tag);
	}

	public WorkbenchRecipeStorage(ResourceLocation recipeId, List<ItemStorage> ingredients, ItemStack output)
	{
		super(recipeId, ingredients, output);
	}

	@Override
	public ResourceLocation getId()
	{
		return ID;
	}

	@Override
	protected GenericRecipeFactory<WorkbenchGenericRecipe> getGenericRecipeFactory()
	{
		return WorkbenchGenericRecipe::new;
	}

}
