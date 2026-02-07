package steve_gall.minecolonies_compatibility.module.common.tconstruct;

import com.minecolonies.api.colony.buildings.ModBuildings;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import steve_gall.minecolonies_compatibility.api.common.repair.CustomizedRepair;
import steve_gall.minecolonies_compatibility.api.common.tool.CustomizedToolSystem;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.module.client.tconstruct.RepairMaterialTeachScreen;
import steve_gall.minecolonies_compatibility.module.common.AbstractModule;
import steve_gall.minecolonies_compatibility.module.common.tconstruct.init.ModuleBuildingModules;
import steve_gall.minecolonies_compatibility.module.common.tconstruct.init.ModuleMenuTypes;
import steve_gall.minecolonies_compatibility.module.common.tconstruct.network.RepairMaterialOpenTeachMessage;
import steve_gall.minecolonies_compatibility.module.common.tconstruct.network.RepairMaterialUpdateMessage;
import steve_gall.minecolonies_tweaks.api.common.requestsystem.DeliverableObjectRegistry;

public class TConstructModule extends AbstractModule
{
	@Override
	protected void onLoad()
	{
		super.onLoad();

		var fml_bus = FMLJavaModLoadingContext.get().getModEventBus();
		ModuleMenuTypes.REGISTER.register(fml_bus);

		DeliverableObjectRegistry.INSTANCE.register(BrokenItem.ID, BrokenItem::serialize, BrokenItem::deserialize);
		DeliverableObjectRegistry.INSTANCE.register(RepairKit.ID, RepairKit::serialize, RepairKit::deserialize);

		var network = MineColoniesCompatibility.network();
		network.registerMessage(RepairMaterialOpenTeachMessage.class, RepairMaterialOpenTeachMessage::new);
		network.registerMessage(RepairMaterialUpdateMessage.class, RepairMaterialUpdateMessage::new);
	}

	@Override
	protected void onInitBuildingModule()
	{
		super.onInitBuildingModule();
		ModBuildings.blacksmith.get().getModuleProducers().add(ModuleBuildingModules.REPAIR_MATERIALS);
	}

	@Override
	protected void onFMLCommonSetup(FMLCommonSetupEvent e)
	{
		super.onFMLCommonSetup(e);
		e.enqueueWork(() ->
		{
			CustomizedToolSystem.register(TConstructToolSystem.INSTANCE);
			CustomizedRepair.register(new TConstructRepair());
		});
	}

	@Override
	protected void onFMLClientSetup(FMLClientSetupEvent e)
	{
		super.onFMLClientSetup(e);
		MenuScreens.register(ModuleMenuTypes.REPAIR_MATERIAL_TEACH.get(), RepairMaterialTeachScreen::new);
	}

}
