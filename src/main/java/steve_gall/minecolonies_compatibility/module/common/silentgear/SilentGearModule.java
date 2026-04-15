package steve_gall.minecolonies_compatibility.module.common.silentgear;

import com.minecolonies.api.colony.buildings.ModBuildings;

import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import steve_gall.minecolonies_compatibility.api.common.repair.CustomizedRepair;
import steve_gall.minecolonies_compatibility.api.common.tool.CustomizedToolSystem;
import steve_gall.minecolonies_compatibility.module.client.silentgear.RepairKitInventoryScreen;
import steve_gall.minecolonies_compatibility.module.client.silentgear.RepairMaterialTeachScreen;
import steve_gall.minecolonies_compatibility.module.common.AbstractModule;
import steve_gall.minecolonies_compatibility.module.common.silentgear.init.ModuleBuildingModules;
import steve_gall.minecolonies_compatibility.module.common.silentgear.init.ModuleMenuTypes;
import steve_gall.minecolonies_compatibility.module.common.silentgear.network.RepairKitOpenInventoryMessage;
import steve_gall.minecolonies_compatibility.module.common.silentgear.network.RepairMaterialMaterialMessage;
import steve_gall.minecolonies_compatibility.module.common.silentgear.network.RepairMaterialOpenTeachMessage;
import steve_gall.minecolonies_tweaks.api.common.network.MessageRegistrar;
import steve_gall.minecolonies_tweaks.api.common.requestsystem.DeliverableObjectRegistry;

public class SilentGearModule extends AbstractModule
{
	@Override
	protected void onLoad()
	{
		super.onLoad();

		var fml_bus = ModLoadingContext.get().getActiveContainer().getEventBus();
		ModuleMenuTypes.REGISTER.register(fml_bus);

		DeliverableObjectRegistry.INSTANCE.register(BrokenItem.ID, BrokenItem::serialize, BrokenItem::deserialize);
	}

	@Override
	protected void onRegisterNetwork(MessageRegistrar channel)
	{
		super.onRegisterNetwork(channel);

		channel.playToServer(RepairMaterialOpenTeachMessage.TYPE, RepairMaterialOpenTeachMessage::new);
		channel.playToServer(RepairMaterialMaterialMessage.TYPE, RepairMaterialMaterialMessage::new);
		channel.playToServer(RepairKitOpenInventoryMessage.TYPE, RepairKitOpenInventoryMessage::new);
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
	protected void onRegisterMenuScreens(RegisterMenuScreensEvent e)
	{
		super.onRegisterMenuScreens(e);

		e.register(ModuleMenuTypes.REPAIR_MATERIAL_TEACH.get(), RepairMaterialTeachScreen::new);
		e.register(ModuleMenuTypes.REPAIR_KIT_INVENTORY.get(), RepairKitInventoryScreen::new);
	}

}
