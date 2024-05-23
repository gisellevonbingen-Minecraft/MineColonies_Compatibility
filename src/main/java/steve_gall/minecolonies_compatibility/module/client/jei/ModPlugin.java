package steve_gall.minecolonies_compatibility.module.client.jei;

import com.minecolonies.api.blocks.ModBlocks;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import steve_gall.minecolonies_compatibility.api.common.plant.CustomizedFruit;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.core.common.init.ModJobs;

@JeiPlugin
public class ModPlugin implements IModPlugin
{
	@Override
	public void registerCategories(IRecipeCategoryRegistration registration)
	{
		var job = ModJobs.ORCHARDIST.get().produceJob(null);
		var guiHelper = registration.getJeiHelpers().getGuiHelper();

		registration.addRecipeCategories(new OrchardistCategory(job, ModJeiRecipeTypes.ORCHARDIST_FRUIT, new ItemStack(ModBlocks.blockHutLumberjack), guiHelper));
	}

	@Override
	public void registerRecipes(IRecipeRegistration registration)
	{
		registration.addRecipes(ModJeiRecipeTypes.ORCHARDIST_FRUIT, CustomizedFruit.getRegistry().values().stream().toList());
	}

	@Override
	public ResourceLocation getPluginUid()
	{
		return MineColoniesCompatibility.rl("plugin");
	}

}
