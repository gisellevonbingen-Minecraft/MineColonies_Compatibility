package steve_gall.minecolonies_compatibility.module.common.tacz;

import com.minecolonies.api.colony.buildings.ModBuildings;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import steve_gall.minecolonies_compatibility.api.common.entity.ai.CustomizedAI;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.module.client.tacz.GunSmithTableTeachScreen;
import steve_gall.minecolonies_compatibility.module.common.AbstractModule;
import steve_gall.minecolonies_compatibility.module.common.tacz.crafting.GunSmithTableRecipeStorage;
import steve_gall.minecolonies_compatibility.module.common.tacz.init.ModuleBuildingModules;
import steve_gall.minecolonies_compatibility.module.common.tacz.init.ModuleCraftingTypes;
import steve_gall.minecolonies_compatibility.module.common.tacz.init.ModuleMenuTypes;
import steve_gall.minecolonies_compatibility.module.common.tacz.network.GunSmithTableOpenTeachMessage;
import steve_gall.minecolonies_tweaks.api.common.crafting.CustomizedRecipeStorageRegistry;
import steve_gall.minecolonies_tweaks.api.common.requestsystem.DeliverableObjectRegistry;

public class TACZModule extends AbstractModule
{
	@Override
	protected void onLoad()
	{
		super.onLoad();

		var fml_bus = FMLJavaModLoadingContext.get().getModEventBus();
		ModuleCraftingTypes.REGISTER.register(fml_bus);
		ModuleMenuTypes.REGISTER.register(fml_bus);

		var network = MineColoniesCompatibility.network();
		network.registerMessage(GunSmithTableOpenTeachMessage.class, GunSmithTableOpenTeachMessage::new);

		CustomizedRecipeStorageRegistry.INSTANCE.register(GunSmithTableRecipeStorage.ID, GunSmithTableRecipeStorage::serialize, GunSmithTableRecipeStorage::new);

		DeliverableObjectRegistry.INSTANCE.register(Ammo.ID, Ammo::serialize, Ammo::deserialize);
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
		});

	}

	@Override
	protected void onFMLClientSetup(FMLClientSetupEvent e)
	{
		super.onFMLClientSetup(e);

		MenuScreens.register(ModuleMenuTypes.TACZ_GUN_SMITH_TABLE_TEACH.get(), GunSmithTableTeachScreen::new);
	}

}
