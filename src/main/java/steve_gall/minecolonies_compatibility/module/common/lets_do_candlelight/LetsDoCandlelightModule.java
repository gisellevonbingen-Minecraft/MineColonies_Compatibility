package steve_gall.minecolonies_compatibility.module.common.lets_do_candlelight;

import com.minecolonies.api.colony.buildings.ModBuildings;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.module.client.lets_do_candlelight.PanTeachScreen;
import steve_gall.minecolonies_compatibility.module.client.lets_do_candlelight.PotTeachScreen;
import steve_gall.minecolonies_compatibility.module.common.AbstractModule;
import steve_gall.minecolonies_compatibility.module.common.lets_do_candlelight.crafting.PanRecipeStorage;
import steve_gall.minecolonies_compatibility.module.common.lets_do_candlelight.crafting.PotRecipeStorage;
import steve_gall.minecolonies_compatibility.module.common.lets_do_candlelight.init.ModuleBuildingModules;
import steve_gall.minecolonies_compatibility.module.common.lets_do_candlelight.init.ModuleCraftingTypes;
import steve_gall.minecolonies_compatibility.module.common.lets_do_candlelight.init.ModuleMenuTypes;
import steve_gall.minecolonies_compatibility.module.common.lets_do_candlelight.network.PanOpenTeachMessage;
import steve_gall.minecolonies_compatibility.module.common.lets_do_candlelight.network.PotOpenTeachMessage;
import steve_gall.minecolonies_tweaks.api.common.crafting.CustomizedRecipeStorageRegistry;

public class LetsDoCandlelightModule extends AbstractModule
{
	@Override
	protected void onLoad()
	{
		super.onLoad();

		var fml_bus = FMLJavaModLoadingContext.get().getModEventBus();
		ModuleCraftingTypes.REGISTER.register(fml_bus);
		ModuleMenuTypes.REGISTER.register(fml_bus);

		var network = MineColoniesCompatibility.network();
		network.registerMessage(PanOpenTeachMessage.class, PanOpenTeachMessage::new);
		network.registerMessage(PotOpenTeachMessage.class, PotOpenTeachMessage::new);

		CustomizedRecipeStorageRegistry.INSTANCE.register(PanRecipeStorage.ID, PanRecipeStorage::serialize, PanRecipeStorage::new);
		CustomizedRecipeStorageRegistry.INSTANCE.register(PotRecipeStorage.ID, PotRecipeStorage::serialize, PotRecipeStorage::new);
	}

	@Override
	protected void onInitBuildingModule()
	{
		super.onInitBuildingModule();
		ModBuildings.cook.get().getModuleProducers().add(ModuleBuildingModules.COOKASSISTANT_PAN);
		ModBuildings.cook.get().getModuleProducers().add(ModuleBuildingModules.COOKASSISTANT_POT);
	}

	@Override
	protected void onFMLClientSetup(FMLClientSetupEvent e)
	{
		super.onFMLClientSetup(e);

		MenuScreens.register(ModuleMenuTypes.PAN_TEACH.get(), PanTeachScreen::new);
		MenuScreens.register(ModuleMenuTypes.POT_TEACH.get(), PotTeachScreen::new);
	}

}
