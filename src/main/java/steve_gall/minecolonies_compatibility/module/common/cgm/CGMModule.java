package steve_gall.minecolonies_compatibility.module.common.cgm;

import com.minecolonies.api.colony.buildings.ModBuildings;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import steve_gall.minecolonies_compatibility.api.common.entity.ai.CustomizedAI;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.module.client.cgm.WorkbenchTeachScreen;
import steve_gall.minecolonies_compatibility.module.common.AbstractModule;
import steve_gall.minecolonies_compatibility.module.common.cgm.crafting.WorkbenchRecipeStorage;
import steve_gall.minecolonies_compatibility.module.common.cgm.init.ModuleBuildingModules;
import steve_gall.minecolonies_compatibility.module.common.cgm.init.ModuleCraftingTypes;
import steve_gall.minecolonies_compatibility.module.common.cgm.init.ModuleMenuTypes;
import steve_gall.minecolonies_compatibility.module.common.cgm.network.WorkbenchOpenTeachMessage;
import steve_gall.minecolonies_tweaks.api.common.crafting.CustomizedRecipeStorageRegistry;
import steve_gall.minecolonies_tweaks.api.common.requestsystem.DeliverableObjectRegistry;

public class CGMModule extends AbstractModule
{
	@Override
	protected void onLoad()
	{
		super.onLoad();

		var fml_bus = FMLJavaModLoadingContext.get().getModEventBus();
		ModuleCraftingTypes.REGISTER.register(fml_bus);
		ModuleMenuTypes.REGISTER.register(fml_bus);

		var network = MineColoniesCompatibility.network();
		network.registerMessage(WorkbenchOpenTeachMessage.class, WorkbenchOpenTeachMessage::new);

		CustomizedRecipeStorageRegistry.INSTANCE.register(WorkbenchRecipeStorage.ID, WorkbenchRecipeStorage::serialize, WorkbenchRecipeStorage::new);

		DeliverableObjectRegistry.INSTANCE.register(Ammo.ID, Ammo::serialize, Ammo::deserialize);
	}

	@Override
	protected void onInitBuildingModule()
	{
		super.onInitBuildingModule();
		ModBuildings.blacksmith.get().getModuleProducers().add(ModuleBuildingModules.BLACKSMITH_WORKBENCH);
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

		MenuScreens.register(ModuleMenuTypes.WORKBENCH.get(), WorkbenchTeachScreen::new);
	}

}
