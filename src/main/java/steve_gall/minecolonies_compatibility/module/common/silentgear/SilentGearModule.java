package steve_gall.minecolonies_compatibility.module.common.silentgear;

import com.minecolonies.api.colony.buildings.ModBuildings;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import steve_gall.minecolonies_compatibility.api.common.repair.CustomizedRepair;
import steve_gall.minecolonies_compatibility.api.common.tool.CustomizedToolSystem;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.module.client.silentgear.RepairKitInventoryScreen;
import steve_gall.minecolonies_compatibility.module.client.silentgear.RepairMaterialTeachScreen;
import steve_gall.minecolonies_compatibility.module.common.AbstractModule;
import steve_gall.minecolonies_compatibility.module.common.silentgear.init.ModuleBuildingModules;
import steve_gall.minecolonies_compatibility.module.common.silentgear.init.ModuleMenuTypes;
import steve_gall.minecolonies_compatibility.module.common.silentgear.network.RepairKitOpenInventoryMessage;
import steve_gall.minecolonies_compatibility.module.common.silentgear.network.RepairMaterialCanUseHigherMessage;
import steve_gall.minecolonies_compatibility.module.common.silentgear.network.RepairMaterialMaterialMessage;
import steve_gall.minecolonies_compatibility.module.common.silentgear.network.RepairMaterialOpenTeachMessage;
import steve_gall.minecolonies_tweaks.api.common.requestsystem.DeliverableObjectRegistry;

public class SilentGearModule extends AbstractModule
{
	@Override
	protected void onLoad()
	{
		super.onLoad();

		var fml_bus = FMLJavaModLoadingContext.get().getModEventBus();
		ModuleMenuTypes.REGISTER.register(fml_bus);

		DeliverableObjectRegistry.INSTANCE.register(BrokenItem.ID, BrokenItem::serialize, BrokenItem::deserialize);

		var network = MineColoniesCompatibility.network();
		network.registerMessage(RepairMaterialOpenTeachMessage.class, RepairMaterialOpenTeachMessage::new);
		network.registerMessage(RepairMaterialMaterialMessage.class, RepairMaterialMaterialMessage::new);
		network.registerMessage(RepairMaterialCanUseHigherMessage.class, RepairMaterialCanUseHigherMessage::new);
		network.registerMessage(RepairKitOpenInventoryMessage.class, RepairKitOpenInventoryMessage::new);
	}

	@Override
	protected void onFMLCommonSetup(FMLCommonSetupEvent e)
	{
		super.onFMLCommonSetup(e);
		e.enqueueWork(() ->
		{
			CustomizedToolSystem.register(SilentGearToolSystem.INSTANCE);

			ModBuildings.blacksmith.get().getModuleProducers().add(ModuleBuildingModules.REPAIR_MATERIALS);
			CustomizedRepair.register(new SilentGearRepair());
		});
	}

	@Override
	protected void onFMLClientSetup(FMLClientSetupEvent e)
	{
		super.onFMLClientSetup(e);

		MenuScreens.register(ModuleMenuTypes.REPAIR_MATERIAL_TEACH.get(), RepairMaterialTeachScreen::new);
		MenuScreens.register(ModuleMenuTypes.REPAIR_KIT_INVENTORY.get(), RepairKitInventoryScreen::new);
	}

}
