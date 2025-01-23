package steve_gall.minecolonies_compatibility.module.common.lets_do_meadow;

import com.minecolonies.api.colony.buildings.ModBuildings;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.module.client.lets_do_meadow.CheeseTeachScreen;
import steve_gall.minecolonies_compatibility.module.client.lets_do_meadow.CookingTeachScreen;
import steve_gall.minecolonies_compatibility.module.client.lets_do_meadow.WoodcuttingTeachScreen;
import steve_gall.minecolonies_compatibility.module.common.AbstractModule;
import steve_gall.minecolonies_compatibility.module.common.lets_do_meadow.crafting.CheeseRecipeStorage;
import steve_gall.minecolonies_compatibility.module.common.lets_do_meadow.crafting.CookingRecipeStorage;
import steve_gall.minecolonies_compatibility.module.common.lets_do_meadow.crafting.WoodcuttingRecipeStorage;
import steve_gall.minecolonies_compatibility.module.common.lets_do_meadow.init.ModuleBuildingModules;
import steve_gall.minecolonies_compatibility.module.common.lets_do_meadow.init.ModuleCraftingTypes;
import steve_gall.minecolonies_compatibility.module.common.lets_do_meadow.init.ModuleMenuTypes;
import steve_gall.minecolonies_compatibility.module.common.lets_do_meadow.network.CheeseOpenTeachMessage;
import steve_gall.minecolonies_compatibility.module.common.lets_do_meadow.network.CookingOpenTeachMessage;
import steve_gall.minecolonies_compatibility.module.common.lets_do_meadow.network.WoodcuttingOpenTeachMessage;
import steve_gall.minecolonies_tweaks.api.common.crafting.CustomizedRecipeStorageRegistry;

public class LetsDoMeadowModule extends AbstractModule
{
	@Override
	protected void onLoad()
	{
		super.onLoad();

		var fml_bus = FMLJavaModLoadingContext.get().getModEventBus();
		ModuleCraftingTypes.REGISTER.register(fml_bus);
		ModuleMenuTypes.REGISTER.register(fml_bus);

		var network = MineColoniesCompatibility.network();
		network.registerMessage(CheeseOpenTeachMessage.class, CheeseOpenTeachMessage::new);
		network.registerMessage(CookingOpenTeachMessage.class, CookingOpenTeachMessage::new);
		network.registerMessage(WoodcuttingOpenTeachMessage.class, WoodcuttingOpenTeachMessage::new);

		CustomizedRecipeStorageRegistry.INSTANCE.register(CheeseRecipeStorage.ID, CheeseRecipeStorage::serialize, CheeseRecipeStorage::new);
		CustomizedRecipeStorageRegistry.INSTANCE.register(CookingRecipeStorage.ID, CookingRecipeStorage::serialize, CookingRecipeStorage::new);
		CustomizedRecipeStorageRegistry.INSTANCE.register(WoodcuttingRecipeStorage.ID, WoodcuttingRecipeStorage::serialize, WoodcuttingRecipeStorage::new);
	}

	@Override
	protected void onFMLCommonSetup(FMLCommonSetupEvent e)
	{
		super.onFMLCommonSetup(e);
		e.enqueueWork(() ->
		{
			ModBuildings.kitchen.get().getModuleProducers().add(ModuleBuildingModules.CHEF_CHEESE);
			ModBuildings.kitchen.get().getModuleProducers().add(ModuleBuildingModules.CHEF_COOKING);
			ModBuildings.sawmill.get().getModuleProducers().add(ModuleBuildingModules.SAWMILL_WOODCUTTING);
		});
	}

	@Override
	protected void onFMLClientSetup(FMLClientSetupEvent e)
	{
		super.onFMLClientSetup(e);

		MenuScreens.register(ModuleMenuTypes.CHEESE_TEACH.get(), CheeseTeachScreen::new);
		MenuScreens.register(ModuleMenuTypes.COOKING_TEACH.get(), CookingTeachScreen::new);
		MenuScreens.register(ModuleMenuTypes.WOODCUTTING_TEACH.get(), WoodcuttingTeachScreen::new);
	}

}
