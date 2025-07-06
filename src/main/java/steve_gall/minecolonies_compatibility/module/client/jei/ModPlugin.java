package steve_gall.minecolonies_compatibility.module.client.jei;

import java.util.stream.Stream;

import com.minecolonies.api.MinecoloniesAPIProxy;
import com.minecolonies.api.blocks.ModBlocks;
import com.minecolonies.api.colony.jobs.registry.JobEntry;
import com.minecolonies.api.crafting.IGenericRecipe;
import com.minecolonies.api.research.IGlobalResearch;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import steve_gall.minecolonies_compatibility.api.client.jei.GhostIngredientHandler;
import steve_gall.minecolonies_compatibility.api.common.butcher.ButcherableIconCache;
import steve_gall.minecolonies_compatibility.api.common.butcher.CustomizedButcherable;
import steve_gall.minecolonies_compatibility.api.common.plant.CustomizedFruit;
import steve_gall.minecolonies_compatibility.api.common.plant.FruitIconCache;
import steve_gall.minecolonies_compatibility.core.client.gui.SmithingTeachScreen;
import steve_gall.minecolonies_compatibility.core.client.gui.StonecutterTeachScreen;
import steve_gall.minecolonies_compatibility.core.client.gui.TeachRecipeScreen;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.core.common.init.ModJobs;
import steve_gall.minecolonies_compatibility.module.client.jei.ResearchCategory.ResearchCache;

@JeiPlugin
public class ModPlugin implements IModPlugin
{
	@Override
	public void registerCategories(IRecipeCategoryRegistration registration)
	{
		var guiHelper = registration.getJeiHelpers().getGuiHelper();

		registration.addRecipeCategories(new ResearchCategory(guiHelper));
		registration.addRecipeCategories(new OrchardistCategory(ModJobs.ORCHARDIST.get().produceJob(null), ModJeiRecipeTypes.ORCHARDIST_FRUIT, guiHelper));
		registration.addRecipeCategories(new ButcherCategory(ModJobs.BUTCHER.get().produceJob(null), ModJeiRecipeTypes.BUTCHER_BUTCHERABLE, guiHelper));
	}

	@Override
	public void registerRecipes(IRecipeRegistration registration)
	{
		registration.addRecipes(ModJeiRecipeTypes.RESEARCH, this.getGlobalResearches().map(ResearchCache::new).toList());
		registration.addRecipes(ModJeiRecipeTypes.ORCHARDIST_FRUIT, CustomizedFruit.getRegistry().values().stream().map(FruitIconCache::new).toList());
		registration.addRecipes(ModJeiRecipeTypes.BUTCHER_BUTCHERABLE, CustomizedButcherable.getRegistry().values().stream().map(ButcherableIconCache::new).toList());
	}

	@Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration registration)
	{
		registration.addRecipeCatalyst(new ItemStack(ModBlocks.blockHutUniversity), ModJeiRecipeTypes.RESEARCH);
	}

	@Override
	public void registerGuiHandlers(IGuiHandlerRegistration registration)
	{
		registration.addGhostIngredientHandler(TeachRecipeScreen.class, new GhostIngredientHandler<>());

		registration.addRecipeClickArea(SmithingTeachScreen.class, 102, 49, 22, 15, RecipeTypes.SMITHING);
		registration.addRecipeClickArea(StonecutterTeachScreen.class, 77, 36, 22, 15, RecipeTypes.STONECUTTING);
	}

	@Override
	public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration)
	{
		var transferHelper = registration.getTransferHelper();
		var recipeType = createRecipeType(ModJobs.FLUID_MANAGER.get());
		registration.addRecipeTransferHandler(new BucketFillingTeachRecipeTransferHandler(transferHelper, recipeType), recipeType);
		registration.addRecipeTransferHandler(new SmithingTeachRecipeTransferHandler(transferHelper), RecipeTypes.SMITHING);
		registration.addRecipeTransferHandler(new StonecutterTeachRecipeTransferHandler(transferHelper), RecipeTypes.STONECUTTING);
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

	public static RecipeType<IGenericRecipe> createRecipeType(JobEntry jobEntry)
	{
		var uid = jobEntry.getKey();
		return RecipeType.create(uid.getNamespace(), uid.getPath(), IGenericRecipe.class);
	}

	@Override
	public ResourceLocation getPluginUid()
	{
		return MineColoniesCompatibility.rl("plugin");
	}

}
