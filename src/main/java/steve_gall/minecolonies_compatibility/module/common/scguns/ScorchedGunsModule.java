package steve_gall.minecolonies_compatibility.module.common.scguns;

import com.minecolonies.api.colony.buildings.ModBuildings;

import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import steve_gall.minecolonies_compatibility.api.common.entity.ai.CustomizedAI;
import steve_gall.minecolonies_compatibility.core.common.init.ModToolTypes;
import steve_gall.minecolonies_compatibility.module.client.scguns.GunBenchTeachScreen;
import steve_gall.minecolonies_compatibility.module.common.AbstractModule;
import steve_gall.minecolonies_compatibility.module.common.scguns.crafting.GunBenchRecipeStorage;
import steve_gall.minecolonies_compatibility.module.common.scguns.init.ModuleBuildingModules;
import steve_gall.minecolonies_compatibility.module.common.scguns.init.ModuleCraftingTypes;
import steve_gall.minecolonies_compatibility.module.common.scguns.init.ModuleMenuTypes;
import steve_gall.minecolonies_compatibility.module.common.scguns.network.GunBenchOpenTeachMessage;
import steve_gall.minecolonies_tweaks.api.common.crafting.CustomizedRecipeStorageRegistry;
import steve_gall.minecolonies_tweaks.api.common.network.MessageRegistrar;
import steve_gall.minecolonies_tweaks.api.common.requestsystem.DeliverableObjectRegistry;
import top.ribs.scguns.item.GunItem;

public class ScorchedGunsModule extends AbstractModule
{
	@Override
	protected void onLoad()
	{
		super.onLoad();

		var fml_bus = ModLoadingContext.get().getActiveContainer().getEventBus();
		ModuleCraftingTypes.REGISTER.register(fml_bus);
		ModuleMenuTypes.REGISTER.register(fml_bus);

		CustomizedRecipeStorageRegistry.INSTANCE.register(GunBenchRecipeStorage.ID, GunBenchRecipeStorage::serialize, GunBenchRecipeStorage::new);

		DeliverableObjectRegistry.INSTANCE.register(Ammo.ID, Ammo::serialize, Ammo::deserialize);
	}
	
	@Override
	protected void onRegisterNetwork(MessageRegistrar channel)
	{
		super.onRegisterNetwork(channel);

		channel.playToServer(GunBenchOpenTeachMessage.TYPE, GunBenchOpenTeachMessage::new);
	}

	@Override
	protected void onInitBuildingModule()
	{
		super.onInitBuildingModule();
		ModBuildings.blacksmith.get().getModuleProducers().add(ModuleBuildingModules.BLACKSMITH_GUN_BENCH);
	}

	@Override
	protected void onFMLCommonSetup(FMLCommonSetupEvent e)
	{
		super.onFMLCommonSetup(e);

		e.enqueueWork(() ->
		{
			CustomizedAI.register(new GunnerGunAI());
			ModToolTypes.GUN.register(item -> item instanceof GunItem);
		});

	}
	
	@Override
	protected void onRegisterMenuScreens(RegisterMenuScreensEvent e)
	{
		super.onRegisterMenuScreens(e);

		e.register(ModuleMenuTypes.GUN_BENCH.get(), GunBenchTeachScreen::new);
	}

}
