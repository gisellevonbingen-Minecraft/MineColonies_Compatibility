package steve_gall.minecolonies_compatibility.module.common.lets_do_vinery;

import com.minecolonies.api.colony.buildings.ModBuildings;

import net.minecraft.world.item.BlockItem;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.satisfy.vinery.core.registry.GrapeTypeRegistry;
import steve_gall.minecolonies_compatibility.api.common.plant.CustomizedFruit;
import steve_gall.minecolonies_compatibility.module.client.lets_do_vinery.ApplePressFermentingTeachScreen;
import steve_gall.minecolonies_compatibility.module.client.lets_do_vinery.ApplePressMashingTeachScreen;
import steve_gall.minecolonies_compatibility.module.common.AbstractModule;
import steve_gall.minecolonies_compatibility.module.common.lets_do_vinery.crafting.ApplePressFermentingRecipeStorage;
import steve_gall.minecolonies_compatibility.module.common.lets_do_vinery.crafting.ApplePressMashingRecipeStorage;
import steve_gall.minecolonies_compatibility.module.common.lets_do_vinery.init.ModuleBuildingModules;
import steve_gall.minecolonies_compatibility.module.common.lets_do_vinery.init.ModuleCraftingTypes;
import steve_gall.minecolonies_compatibility.module.common.lets_do_vinery.init.ModuleMenuTypes;
import steve_gall.minecolonies_compatibility.module.common.lets_do_vinery.network.ApplePressFermentingOpenTeachMessage;
import steve_gall.minecolonies_compatibility.module.common.lets_do_vinery.network.ApplePressMashingOpenTeachMessage;
import steve_gall.minecolonies_tweaks.api.common.crafting.CustomizedRecipeStorageRegistry;
import steve_gall.minecolonies_tweaks.api.common.network.MessageRegistrar;

public class LetsDoVineryModule extends AbstractModule
{
	@Override
	protected void onLoad()
	{
		super.onLoad();

		var fml_bus = ModLoadingContext.get().getActiveContainer().getEventBus();
		ModuleCraftingTypes.REGISTER.register(fml_bus);
		ModuleMenuTypes.REGISTER.register(fml_bus);

		CustomizedRecipeStorageRegistry.INSTANCE.register(ApplePressMashingRecipeStorage.ID, ApplePressMashingRecipeStorage::serialize, ApplePressMashingRecipeStorage::new);
		CustomizedRecipeStorageRegistry.INSTANCE.register(ApplePressFermentingRecipeStorage.ID, ApplePressFermentingRecipeStorage::serialize, ApplePressFermentingRecipeStorage::new);
	}

	@Override
	protected void onRegisterNetwork(MessageRegistrar channel)
	{
		super.onRegisterNetwork(channel);
		channel.playToServer(ApplePressMashingOpenTeachMessage.TYPE, ApplePressMashingOpenTeachMessage::new);
		channel.playToServer(ApplePressFermentingOpenTeachMessage.TYPE, ApplePressFermentingOpenTeachMessage::new);
	}

	@Override
	protected void onInitBuildingModule()
	{
		super.onInitBuildingModule();
		ModBuildings.farmer.get().getModuleProducers().add(ModuleBuildingModules.FARMER_APPLE_PRESS_MASHING);
		ModBuildings.farmer.get().getModuleProducers().add(ModuleBuildingModules.FARMER_APPLE_PRESS_FERMENTING);
	}

	@Override
	protected void onFMLCommonSetup(FMLCommonSetupEvent e)
	{
		super.onFMLCommonSetup(e);
		e.enqueueWork(() ->
		{
			CustomizedFruit.register(new AppleLeavesFruit());
			CustomizedFruit.register(new CherryLeavesFruit());

			for (var grapeType : GrapeTypeRegistry.GRAPE_TYPE_TYPES)
			{
				if (grapeType.getSeeds() instanceof BlockItem item)
				{
					CustomizedFruit.register(new GrapeFruit(grapeType, item.getBlock()));
				}

			}
		});
	}

	@Override
	protected void onRegisterMenuScreens(RegisterMenuScreensEvent e)
	{
		super.onRegisterMenuScreens(e);
		e.register(ModuleMenuTypes.APPLE_PRESS_MASHING.get(), ApplePressMashingTeachScreen::new);
		e.register(ModuleMenuTypes.APPLE_PRESS_FERMENTING.get(), ApplePressFermentingTeachScreen::new);
	}

}
