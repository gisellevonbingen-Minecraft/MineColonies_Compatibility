package steve_gall.minecolonies_compatibility.module.client.jei;

import java.util.stream.Stream;

import com.minecolonies.api.MinecoloniesAPIProxy;
import com.minecolonies.api.blocks.ModBlocks;
import com.minecolonies.api.research.IGlobalResearch;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import steve_gall.minecolonies_compatibility.api.common.plant.CustomizedFruit;
import steve_gall.minecolonies_compatibility.api.common.plant.FruitIconCache;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.core.common.init.ModJobs;
import steve_gall.minecolonies_compatibility.module.client.jei.ResearchCategory.ResearchCache;

@JeiPlugin
public class ModPlugin implements IModPlugin
{
	@Override
	public void registerCategories(IRecipeCategoryRegistration registration)
	{
		var job = ModJobs.ORCHARDIST.get().produceJob(null);
		var guiHelper = registration.getJeiHelpers().getGuiHelper();

		registration.addRecipeCategories(new ResearchCategory(guiHelper));
		registration.addRecipeCategories(new OrchardistCategory(job, ModJeiRecipeTypes.ORCHARDIST_FRUIT, new ItemStack(ModBlocks.blockHutLumberjack), guiHelper));
	}

	@Override
	public void registerRecipes(IRecipeRegistration registration)
	{
		registration.addRecipes(ModJeiRecipeTypes.RESEARCH, this.getGlobalResearches().map(ResearchCache::new).toList());
		registration.addRecipes(ModJeiRecipeTypes.ORCHARDIST_FRUIT, CustomizedFruit.getRegistry().values().stream().map(FruitIconCache::new).toList());
	}

	@Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration registration)
	{
		registration.addRecipeCatalyst(new ItemStack(ModBlocks.blockHutUniversity), ModJeiRecipeTypes.RESEARCH);
	}

	public Stream<IGlobalResearch> getGlobalResearches()
	{
		var api = MinecoloniesAPIProxy.getInstance().getGlobalResearchTree();
		return api.getBranches().stream().flatMap(branchId -> api.getPrimaryResearch(branchId).stream()).flatMap(this::getGlobalResearches);
	}

	public Stream<IGlobalResearch> getGlobalResearches(ResourceLocation researchId)
	{
		var api = MinecoloniesAPIProxy.getInstance().getGlobalResearchTree();
		var research = api.getResearch(researchId);
		return Stream.concat(Stream.of(research), research.getChildren().stream().flatMap(this::getGlobalResearches));
	}

	@Override
	public ResourceLocation getPluginUid()
	{
		return MineColoniesCompatibility.rl("plugin");
	}

}
