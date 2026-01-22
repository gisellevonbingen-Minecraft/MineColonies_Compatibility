package steve_gall.minecolonies_compatibility.module.common.lets_do_brewery;

import com.minecolonies.api.colony.buildings.ModBuildings;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import steve_gall.minecolonies_compatibility.api.common.plant.CustomizedCrop;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.module.client.lets_do_brewery.SiloTeachScreen;
import steve_gall.minecolonies_compatibility.module.common.AbstractModule;
import steve_gall.minecolonies_compatibility.module.common.lets_do_brewery.crafting.SiloRecipeStorage;
import steve_gall.minecolonies_compatibility.module.common.lets_do_brewery.init.ModuleBuildingModules;
import steve_gall.minecolonies_compatibility.module.common.lets_do_brewery.init.ModuleCraftingTypes;
import steve_gall.minecolonies_compatibility.module.common.lets_do_brewery.init.ModuleMenuTypes;
import steve_gall.minecolonies_compatibility.module.common.lets_do_brewery.network.SiloOpenTeachMessage;
import steve_gall.minecolonies_tweaks.api.common.crafting.CustomizedRecipeStorageRegistry;

public class LetsDoBreweryModule extends AbstractModule
{
	@Override
	protected void onLoad()
	{
		super.onLoad();

		var fml_bus = FMLJavaModLoadingContext.get().getModEventBus();
		ModuleCraftingTypes.REGISTER.register(fml_bus);
		ModuleMenuTypes.REGISTER.register(fml_bus);

		var network = MineColoniesCompatibility.network();
		network.registerMessage(SiloOpenTeachMessage.class, SiloOpenTeachMessage::new);

		CustomizedRecipeStorageRegistry.INSTANCE.register(SiloRecipeStorage.ID, SiloRecipeStorage::serialize, SiloRecipeStorage::new);
	}

	@Override
	protected void onInitBuildingModule()
	{
		super.onInitBuildingModule();
		ModBuildings.farmer.get().getModuleProducers().add(ModuleBuildingModules.FARMER_SILO);
	}

	@Override
	protected void onFMLCommonSetup(FMLCommonSetupEvent e)
	{
		super.onFMLCommonSetup(e);
		e.enqueueWork(() ->
		{
			CustomizedCrop.register(new HopsCrop());
		});
	}

	@Override
	protected void onFMLClientSetup(FMLClientSetupEvent e)
	{
		super.onFMLClientSetup(e);

		MenuScreens.register(ModuleMenuTypes.SILO_TEACH.get(), SiloTeachScreen::new);
	}

}
