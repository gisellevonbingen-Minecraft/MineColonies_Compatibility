package steve_gall.minecolonies_compatibility.module.common.lets_do_vinery;

import com.minecolonies.api.colony.buildings.ModBuildings;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.item.BlockItem;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import satisfyu.vinery.registry.GrapeTypes;
import steve_gall.minecolonies_compatibility.api.common.plant.CustomizedFruit;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.module.client.lets_do_vinery.ApplePressTeachScreen;
import steve_gall.minecolonies_compatibility.module.common.AbstractModule;
import steve_gall.minecolonies_compatibility.module.common.lets_do_vinery.crafting.ApplePressRecipeStorage;
import steve_gall.minecolonies_compatibility.module.common.lets_do_vinery.init.ModuleBuildingModules;
import steve_gall.minecolonies_compatibility.module.common.lets_do_vinery.init.ModuleCraftingTypes;
import steve_gall.minecolonies_compatibility.module.common.lets_do_vinery.init.ModuleMenuTypes;
import steve_gall.minecolonies_compatibility.module.common.lets_do_vinery.network.ApplePressOpenTeachMessage;
import steve_gall.minecolonies_tweaks.api.common.crafting.CustomizedRecipeStorageRegistry;

public class LetsDoVineryModule extends AbstractModule
{
	@Override
	protected void onLoad()
	{
		super.onLoad();

		var fml_bus = FMLJavaModLoadingContext.get().getModEventBus();
		ModuleCraftingTypes.REGISTER.register(fml_bus);
		ModuleMenuTypes.REGISTER.register(fml_bus);

		var network = MineColoniesCompatibility.network();
		network.registerMessage(ApplePressOpenTeachMessage.class, ApplePressOpenTeachMessage::new);

		CustomizedRecipeStorageRegistry.INSTANCE.register(ApplePressRecipeStorage.ID, ApplePressRecipeStorage::serialize, ApplePressRecipeStorage::new);
	}

	@Override
	protected void onInitBuildingModule()
	{
		super.onInitBuildingModule();
		ModBuildings.farmer.get().getModuleProducers().add(ModuleBuildingModules.FARMER_APPLE_PRESS);
	}

	@Override
	protected void onFMLCommonSetup(FMLCommonSetupEvent e)
	{
		super.onFMLCommonSetup(e);
		e.enqueueWork(() ->
		{
			CustomizedFruit.register(new AppleLeavesFruit());
			CustomizedFruit.register(new CherryLeavesFruit());

			for (var grapeType : GrapeTypes.GRAPE_TYPE_TYPES)
			{
				if (grapeType.getSeeds() instanceof BlockItem item)
				{
					CustomizedFruit.register(new GrapeFruit(grapeType, item.getBlock()));
				}

			}
		});
	}

	@Override
	protected void onFMLClientSetup(FMLClientSetupEvent e)
	{
		super.onFMLClientSetup(e);

		MenuScreens.register(ModuleMenuTypes.APPLE_PRESS_TEACH.get(), ApplePressTeachScreen::new);
	}

}
