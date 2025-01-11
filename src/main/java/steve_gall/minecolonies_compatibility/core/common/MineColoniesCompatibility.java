package steve_gall.minecolonies_compatibility.core.common;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.minecolonies.api.colony.buildings.ModBuildings;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import steve_gall.minecolonies_compatibility.api.common.requestsystem.IngredientDeliverable;
import steve_gall.minecolonies_compatibility.core.client.gui.BucketFillingTeachScreen;
import steve_gall.minecolonies_compatibility.core.client.gui.SmithingTeachScreen;
import steve_gall.minecolonies_compatibility.core.common.config.MineColoniesCompatibilityConfigCommon;
import steve_gall.minecolonies_compatibility.core.common.config.MineColoniesCompatibilityConfigServer;
import steve_gall.minecolonies_compatibility.core.common.crafting.BucketFillingRecipeStorage;
import steve_gall.minecolonies_compatibility.core.common.crafting.SmithingRecipeStorage;
import steve_gall.minecolonies_compatibility.core.common.entity.ai.butcher.Butcherable;
import steve_gall.minecolonies_compatibility.core.common.init.ModBuildingModules;
import steve_gall.minecolonies_compatibility.core.common.init.ModCraftingTypes;
import steve_gall.minecolonies_compatibility.core.common.init.ModGuardTypes;
import steve_gall.minecolonies_compatibility.core.common.init.ModItems;
import steve_gall.minecolonies_compatibility.core.common.init.ModJobs;
import steve_gall.minecolonies_compatibility.core.common.init.ModMenuTypes;
import steve_gall.minecolonies_compatibility.core.common.init.ModToolTypes;
import steve_gall.minecolonies_compatibility.core.common.network.NetworkChannel;
import steve_gall.minecolonies_compatibility.module.common.ModuleManager;
import steve_gall.minecolonies_tweaks.api.common.crafting.CustomizedRecipeStorageRegistry;
import steve_gall.minecolonies_tweaks.api.common.requestsystem.DeliverableObjectRegistry;
import steve_gall.minecolonies_tweaks.api.common.tool.CustomToolTypeRegisterEvent;

@Mod(MineColoniesCompatibility.MOD_ID)
public class MineColoniesCompatibility
{
	public static final String MOD_ID = "minecolonies_compatibility";
	public static final Logger LOGGER = LogManager.getLogger();

	private static NetworkChannel NETWORK;

	public MineColoniesCompatibility()
	{
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, MineColoniesCompatibilityConfigCommon.SPEC);
		ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, MineColoniesCompatibilityConfigServer.SPEC);

		var fml_bus = FMLJavaModLoadingContext.get().getModEventBus();
		ModItems.REGISTER.register(fml_bus);
		ModGuardTypes.REGISTER.register(fml_bus);
		ModJobs.REGISTER.register(fml_bus);
		ModCraftingTypes.REGISTER.register(fml_bus);
		ModMenuTypes.REGISTER.register(fml_bus);
		fml_bus.addListener(this::onFMLCommonSetup);
		fml_bus.addListener(this::onFMLClientSetup);

		var forge_bus = MinecraftForge.EVENT_BUS;
		forge_bus.addListener(this::onCustomToolTypeRegister);

		NETWORK = new NetworkChannel("main");
		ModuleManager.initialize();

		CustomizedRecipeStorageRegistry.INSTANCE.register(BucketFillingRecipeStorage.ID, BucketFillingRecipeStorage::serialize, BucketFillingRecipeStorage::deserialize);
		CustomizedRecipeStorageRegistry.INSTANCE.register(SmithingRecipeStorage.ID, SmithingRecipeStorage::serialize, SmithingRecipeStorage::deserialize);

		DeliverableObjectRegistry.INSTANCE.register(IngredientDeliverable.ID, IngredientDeliverable::serialize, IngredientDeliverable::deserialize);
		DeliverableObjectRegistry.INSTANCE.register(Butcherable.ID, Butcherable::serialize, Butcherable::deserialize);
	}

	private void onFMLCommonSetup(FMLCommonSetupEvent e)
	{
		e.enqueueWork(() ->
		{
			ModBuildings.guardTower.get().getModuleProducers().add(ModBuildingModules.GUNNER_TOWER_WORK);
			ModBuildings.barracksTower.get().getModuleProducers().add(ModBuildingModules.GUNNER_BARRACKS_WORK);

			ModBuildings.lumberjack.get().getModuleProducers().add(ModBuildingModules.ORCHARDIST_WORK);
			ModBuildings.lumberjack.get().getModuleProducers().add(ModBuildingModules.FRUITLIST_BLACKLIST);
			ModBuildings.wareHouse.get().getModuleProducers().add(ModBuildingModules.NETWORK_STORAGE);
			ModBuildings.blacksmith.get().getModuleProducers().add(ModBuildingModules.BLACKSMITH_SMITHING);

			ModBuildings.deliveryman.get().getModuleProducers().add(ModBuildingModules.FLUID_MANAGER_WORK);
			ModBuildings.deliveryman.get().getModuleProducers().add(ModBuildingModules.FLUID_MANAGER_BUCKET_FILLING);
			ModBuildings.deliveryman.get().getModuleProducers().add(ModBuildingModules.FLUID_MANAGER_LAVA_CAULDRON);

			ModBuildings.swineHerder.get().getModuleProducers().add(ModBuildingModules.BUTCHER_WORK);
			ModBuildings.chickenHerder.get().getModuleProducers().add(ModBuildingModules.BUTCHER_WORK);
			ModBuildings.cowboy.get().getModuleProducers().add(ModBuildingModules.BUTCHER_WORK);
			ModBuildings.rabbitHutch.get().getModuleProducers().add(ModBuildingModules.BUTCHER_WORK);
			ModBuildings.shepherd.get().getModuleProducers().add(ModBuildingModules.BUTCHER_WORK);
		});
	}

	private void onFMLClientSetup(FMLClientSetupEvent e)
	{
		MenuScreens.register(ModMenuTypes.BUCKET_FILLING_TEACH.get(), BucketFillingTeachScreen::new);
		MenuScreens.register(ModMenuTypes.SMITHING_TEACH.get(), SmithingTeachScreen::new);
	}

	private void onCustomToolTypeRegister(CustomToolTypeRegisterEvent e)
	{
		e.register(ModToolTypes.CROSSBOW);
		e.register(ModToolTypes.GUN);
		e.register(ModToolTypes.KNIFE);

		e.register(ModToolTypes.RANGER_WEAPON);
		e.register(ModToolTypes.KNIGHT_WEAPON);
		e.register(ModToolTypes.BUTCHER_TOOL);
	}

	public static NetworkChannel network()
	{
		return NETWORK;
	}

	public static ResourceLocation rl(String path)
	{
		return new ResourceLocation(MOD_ID, path);
	}

	public static String tl(String path)
	{
		return MOD_ID + "." + path;
	}

}
