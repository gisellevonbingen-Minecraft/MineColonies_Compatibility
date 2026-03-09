package steve_gall.minecolonies_compatibility.module.common.tacz;

import com.minecolonies.api.colony.buildings.ModBuildings;
import com.tacz.guns.api.item.IGun;

import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import steve_gall.minecolonies_compatibility.api.common.entity.ai.CustomizedAI;
import steve_gall.minecolonies_compatibility.core.common.init.ModToolTypes;
import steve_gall.minecolonies_compatibility.module.client.tacz.GunSmithTableTeachScreen;
import steve_gall.minecolonies_compatibility.module.common.AbstractModule;
import steve_gall.minecolonies_compatibility.module.common.tacz.crafting.GunSmithTableRecipeStorage;
import steve_gall.minecolonies_compatibility.module.common.tacz.init.ModuleBuildingModules;
import steve_gall.minecolonies_compatibility.module.common.tacz.init.ModuleCraftingTypes;
import steve_gall.minecolonies_compatibility.module.common.tacz.init.ModuleMenuTypes;
import steve_gall.minecolonies_compatibility.module.common.tacz.network.GunSmithTableOpenTeachMessage;
import steve_gall.minecolonies_tweaks.api.common.crafting.CustomizedRecipeStorageRegistry;
import steve_gall.minecolonies_tweaks.api.common.network.MessageRegistrar;
import steve_gall.minecolonies_tweaks.api.common.requestsystem.DeliverableObjectRegistry;

public class TACZModule extends AbstractModule
{
	@Override
	protected void onLoad()
	{
		super.onLoad();

		var fml_bus = ModLoadingContext.get().getActiveContainer().getEventBus();
		ModuleCraftingTypes.REGISTER.register(fml_bus);
		ModuleMenuTypes.REGISTER.register(fml_bus);

		CustomizedRecipeStorageRegistry.INSTANCE.register(GunSmithTableRecipeStorage.ID, GunSmithTableRecipeStorage::serialize, GunSmithTableRecipeStorage::new);

		DeliverableObjectRegistry.INSTANCE.register(Ammo.ID, Ammo::serialize, Ammo::deserialize);
	}

	@Override
	protected void onRegisterNetwork(MessageRegistrar channel)
	{
		super.onRegisterNetwork(channel);

		channel.playToServer(GunSmithTableOpenTeachMessage.TYPE, GunSmithTableOpenTeachMessage::new);
	}

	@Override
	protected void onInitBuildingModule()
	{
		super.onInitBuildingModule();
		ModBuildings.blacksmith.get().getModuleProducers().add(ModuleBuildingModules.BLACKSMITH_GUN_SMITH_TABLE);
	}

	@Override
	protected void onFMLCommonSetup(FMLCommonSetupEvent e)
	{
		super.onFMLCommonSetup(e);

		e.enqueueWork(() ->
		{
			CustomizedAI.register(new GunnerGunAI());
			ModToolTypes.GUN.register(item -> item instanceof IGun);
		});

	}

	@Override
	protected void onRegisterMenuScreens(RegisterMenuScreensEvent e)
	{
		super.onRegisterMenuScreens(e);

		e.register(ModuleMenuTypes.GUN_SMITH_TABLE_TEACH.get(), GunSmithTableTeachScreen::new);
	}

}
