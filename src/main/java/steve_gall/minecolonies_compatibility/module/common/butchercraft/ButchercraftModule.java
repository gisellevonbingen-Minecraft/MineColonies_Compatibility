package steve_gall.minecolonies_compatibility.module.common.butchercraft;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import com.lance5057.butchercraft.ButchercraftItems;
import com.lance5057.butchercraft.ButchercraftRecipes;
import com.lance5057.butchercraft.blocks.AnimalHeadBlock;
import com.lance5057.butchercraft.blocks.HideBlock;
import com.lance5057.butchercraft.items.CarcassItem;
import com.lance5057.butchercraft.workstations.butcherblock.ButcherBlockRecipe;
import com.lance5057.butchercraft.workstations.hook.HookRecipe;
import com.minecolonies.api.colony.buildings.ModBuildings;
import com.minecolonies.core.colony.buildings.modules.BuildingModules;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.util.FakePlayer;
import steve_gall.minecolonies_compatibility.api.common.butcher.CustomizedButcherable;
import steve_gall.minecolonies_compatibility.core.common.building.module.InjectBuildingSettingsModuleEvent;
import steve_gall.minecolonies_compatibility.module.client.butchercraft.GrinderTeachScreen;
import steve_gall.minecolonies_compatibility.module.common.AbstractModule;
import steve_gall.minecolonies_compatibility.module.common.butchercraft.butcherable.ButcherBlockButcherable;
import steve_gall.minecolonies_compatibility.module.common.butchercraft.butcherable.HookButcherable;
import steve_gall.minecolonies_compatibility.module.common.butchercraft.crafting.GrinderRecipeStorage;
import steve_gall.minecolonies_compatibility.module.common.butchercraft.init.ModuleBuildingModules;
import steve_gall.minecolonies_compatibility.module.common.butchercraft.init.ModuleCraftingTypes;
import steve_gall.minecolonies_compatibility.module.common.butchercraft.init.ModuleMenuTypes;
import steve_gall.minecolonies_compatibility.module.common.butchercraft.network.GrinderOpenTeachMessage;
import steve_gall.minecolonies_tweaks.api.common.crafting.CustomizedRecipeStorageRegistry;
import steve_gall.minecolonies_tweaks.api.common.network.MessageRegistrar;

public class ButchercraftModule extends AbstractModule
{
	@Override
	protected void onLoad()
	{
		super.onLoad();

		var fml_bus = ModLoadingContext.get().getActiveContainer().getEventBus();
		ModuleCraftingTypes.REGISTER.register(fml_bus);
		ModuleMenuTypes.REGISTER.register(fml_bus);

		var forge_bus = NeoForge.EVENT_BUS;
		forge_bus.addListener(this::onInjectBuildingSettingsModule);

		CustomizedRecipeStorageRegistry.INSTANCE.register(GrinderRecipeStorage.ID, GrinderRecipeStorage::serialize, GrinderRecipeStorage::new);
	}

	@Override
	protected void onInitBuildingModule()
	{
		super.onInitBuildingModule();
		ModBuildings.kitchen.get().getModuleProducers().add(ModuleBuildingModules.CHEF_GRINDER);
	}

	@Override
	protected void onRegisterMenuScreens(RegisterMenuScreensEvent e)
	{
		super.onRegisterMenuScreens(e);

		e.register(ModuleMenuTypes.GRINDER_TEACH.get(), GrinderTeachScreen::new);
	}

	@Override
	protected void onRegisterNetwork(MessageRegistrar channel)
	{
		super.onRegisterNetwork(channel);

		channel.playToServer(GrinderOpenTeachMessage.TYPE, GrinderOpenTeachMessage::new);
	}

	private <RECIPE extends Recipe<RECIPE_INPUT>, RECIPE_INPUT extends RecipeInput, BUTCHERABLE extends CustomizedButcherable> void registerAll(RecipeManager recipeManager, RecipeType<RECIPE> recipeType, Function<RECIPE, Ingredient> ingredientFunc, Function<RecipeHolder<RECIPE>, BUTCHERABLE> butcherableFactory)
	{
		for (var recipeHolder : recipeManager.getAllRecipesFor(recipeType))
		{
			var ingredient = ingredientFunc.apply(recipeHolder.value());

			if (Arrays.stream(ingredient.getItems()).allMatch(ButchercraftModule::isButcherable))
			{
				var butcherable = butcherableFactory.apply(recipeHolder);
				CustomizedButcherable.registerVolatile(butcherable);
			}

		}

	}

	public static boolean isButcherable(ItemStack stack)
	{
		var item = stack.getItem();

		if (item instanceof CarcassItem)
		{
			return true;
		}
		else if (item instanceof BlockItem blockItem)
		{
			var block = blockItem.getBlock();
			return block instanceof HideBlock || block instanceof AnimalHeadBlock;
		}

		return false;
	}

	@Override
	protected void onRecipeReloaded(RecipeManager recipeManager)
	{
		super.onRecipeReloaded(recipeManager);

		this.registerAll(recipeManager, ButchercraftRecipes.BUTCHER_BLOCK.get(), ButcherBlockRecipe::carcass, ButcherBlockButcherable::new);
		this.registerAll(recipeManager, ButchercraftRecipes.HOOK.get(), HookRecipe::carcass, HookButcherable::new);
	}

	private void onInjectBuildingSettingsModule(InjectBuildingSettingsModuleEvent e)
	{
		var entry = e.getBuilding().getBuildingType();

		if (entry == ModBuildings.swineHerder.get())
		{
			e.register(BuildingModules.SWINEHERDER_SETTINGS, ModuleBuildingModules.HERDER_SETTINGS);
		}
		else if (entry == ModBuildings.chickenHerder.get())
		{
			e.register(BuildingModules.CHICKENHERDER_SETTINGS_BREEDING, ModuleBuildingModules.HERDER_SETTINGS);
		}
		else if (entry == ModBuildings.cowboy.get())
		{
			e.register(BuildingModules.COWHERDER_SETTINGS, ModuleBuildingModules.HERDER_SETTINGS);
		}
		else if (entry == ModBuildings.rabbitHutch.get())
		{
			e.register(BuildingModules.RABBITHERDER_SETTINGS, ModuleBuildingModules.HERDER_SETTINGS);
		}
		else if (entry == ModBuildings.shepherd.get())
		{
			e.register(BuildingModules.SHEPERD_SETTINGS, ModuleBuildingModules.HERDER_SETTINGS);
		}

	}

	public static List<ItemStack> getItemsToBeKept()
	{
		return Arrays.asList(//
				new ItemStack(ButchercraftItems.BUTCHER_KNIFE.get()), //
				new ItemStack(ButchercraftItems.SKINNING_KNIFE.get()), //
				new ItemStack(ButchercraftItems.BONE_SAW.get()), //
				new ItemStack(ButchercraftItems.GUT_KNIFE.get())//
		);
	}

	public static boolean slaughter(FakePlayer player, Animal animal, InteractionHand hand)
	{
		var tool = player.getItemInHand(hand);
		var item = ButchercraftItems.BUTCHER_KNIFE.get();

		if (tool.is(item))
		{
			tool.interactLivingEntity(player, animal, hand);
			return true;
		}

		return false;
	}

}
