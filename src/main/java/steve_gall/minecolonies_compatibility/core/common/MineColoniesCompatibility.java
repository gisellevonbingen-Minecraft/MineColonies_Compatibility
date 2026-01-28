package steve_gall.minecolonies_compatibility.core.common;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.minecolonies.api.colony.buildings.ModBuildings;
import com.minecolonies.api.colony.requestsystem.StandardFactoryController;
import com.minecolonies.api.creativetab.ModCreativeTabs;
import com.minecolonies.api.equipment.ModEquipmentTypes;
import com.minecolonies.core.colony.buildings.modules.BuildingModules;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeManager;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.javafmlmod.FMLModContainer;
import net.neoforged.neoforge.client.event.RecipesUpdatedEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import steve_gall.minecolonies_compatibility.api.common.building.module.NetworkStorageViewRegistry;
import steve_gall.minecolonies_compatibility.api.common.butcher.CustomizedButcherable;
import steve_gall.minecolonies_compatibility.api.common.requestsystem.IngredientDeliverable;
import steve_gall.minecolonies_compatibility.api.common.tool.CustomizedToolSystem;
import steve_gall.minecolonies_compatibility.core.client.MineColoniesCompatibilityClient;
import steve_gall.minecolonies_compatibility.core.common.block.entity.INetworkStorageViewHolder;
import steve_gall.minecolonies_compatibility.core.common.building.module.InjectBuildingSettingsModuleEvent;
import steve_gall.minecolonies_compatibility.core.common.config.MineColoniesCompatibilityConfigCommon;
import steve_gall.minecolonies_compatibility.core.common.config.MineColoniesCompatibilityConfigServer;
import steve_gall.minecolonies_compatibility.core.common.crafting.BucketFillingRecipeStorage;
import steve_gall.minecolonies_compatibility.core.common.crafting.SmithingRecipeStorage;
import steve_gall.minecolonies_compatibility.core.common.crafting.SmithingTemplateRecipeStorage;
import steve_gall.minecolonies_compatibility.core.common.crafting.StonecutterRecipeStorage;
import steve_gall.minecolonies_compatibility.core.common.entity.ai.butcher.Butcherable;
import steve_gall.minecolonies_compatibility.core.common.init.ModBlockEntities;
import steve_gall.minecolonies_compatibility.core.common.init.ModBlocks;
import steve_gall.minecolonies_compatibility.core.common.init.ModBuildingModules;
import steve_gall.minecolonies_compatibility.core.common.init.ModCraftingTypes;
import steve_gall.minecolonies_compatibility.core.common.init.ModDataComponents;
import steve_gall.minecolonies_compatibility.core.common.init.ModGuardTypes;
import steve_gall.minecolonies_compatibility.core.common.init.ModInteractions;
import steve_gall.minecolonies_compatibility.core.common.init.ModItems;
import steve_gall.minecolonies_compatibility.core.common.init.ModJobs;
import steve_gall.minecolonies_compatibility.core.common.init.ModMenuTypes;
import steve_gall.minecolonies_compatibility.core.common.init.ModToolTypes;
import steve_gall.minecolonies_compatibility.core.common.network.ModMessagesRegistrar;
import steve_gall.minecolonies_compatibility.core.common.requestsystem.NetworkCrafting;
import steve_gall.minecolonies_compatibility.core.common.requestsystem.NetworkCraftingProductionResolverFactory;
import steve_gall.minecolonies_compatibility.core.common.requestsystem.NetworkCraftingRequestResolverFactory;
import steve_gall.minecolonies_compatibility.module.common.ModuleManager;
import steve_gall.minecolonies_tweaks.api.common.building.module.ModuleRegisterEvent;
import steve_gall.minecolonies_tweaks.api.common.crafting.CustomizedRecipeStorageRegistry;
import steve_gall.minecolonies_tweaks.api.common.network.MessageRegistrar;
import steve_gall.minecolonies_tweaks.api.common.requestsystem.DeliverableObjectRegistry;
import steve_gall.minecolonies_tweaks.api.common.requestsystem.RequestableObjectRegistry;
import steve_gall.minecolonies_tweaks.api.common.tool.CustomToolTypeRegisterEvent;

@Mod(MineColoniesCompatibility.MOD_ID)
public class MineColoniesCompatibility
{
	public static final String MOD_ID = "minecolonies_compatibility";
	public static final Logger LOGGER = LogManager.getLogger();
	public static final CompatibilityManager COMPAT = new CompatibilityManager();

	public MineColoniesCompatibility(FMLModContainer modContainer, Dist dist)
	{
		modContainer.registerConfig(ModConfig.Type.COMMON, MineColoniesCompatibilityConfigCommon.SPEC);
		modContainer.registerConfig(ModConfig.Type.SERVER, MineColoniesCompatibilityConfigServer.SPEC);

		var fml_bus = modContainer.getEventBus();
		ModDataComponents.REGISTER.register(fml_bus);
		ModBlocks.REGISTER.register(fml_bus);
		ModItems.REGISTER.register(fml_bus);
		ModBlockEntities.REGISTER.register(fml_bus);
		ModGuardTypes.REGISTER.register(fml_bus);
		ModJobs.REGISTER.register(fml_bus);
		ModCraftingTypes.REGISTER.register(fml_bus);
		ModMenuTypes.REGISTER.register(fml_bus);
		ModInteractions.REGISTER.register(fml_bus);
		fml_bus.addListener(this::onFMLCommonSetup);
		fml_bus.addListener(this::onModuleRegister);
		fml_bus.addListener(this::onCustomToolTypeRegister);
		fml_bus.addListener(this::onBuildCreativeModeTabContents);
		fml_bus.addListener(this::onRegisterPayloadHandlers);

		var forge_bus = NeoForge.EVENT_BUS;
		forge_bus.addListener(this::onInjectBuildingSettingsModule);
		forge_bus.addListener(this::onRecipesUpdated);
		forge_bus.addListener(this::onOnDatapackSync);

		ModuleManager.initialize();

		CustomizedRecipeStorageRegistry.INSTANCE.register(BucketFillingRecipeStorage.ID, BucketFillingRecipeStorage::serialize, BucketFillingRecipeStorage::deserialize);
		CustomizedRecipeStorageRegistry.INSTANCE.register(SmithingRecipeStorage.ID, SmithingRecipeStorage::serialize, SmithingRecipeStorage::deserialize);
		CustomizedRecipeStorageRegistry.INSTANCE.register(SmithingTemplateRecipeStorage.ID, SmithingTemplateRecipeStorage::serialize, SmithingTemplateRecipeStorage::deserialize);
		CustomizedRecipeStorageRegistry.INSTANCE.register(StonecutterRecipeStorage.ID, StonecutterRecipeStorage::serialize, StonecutterRecipeStorage::deserialize);

		StandardFactoryController.getInstance().registerNewFactory(new NetworkCraftingRequestResolverFactory());
		StandardFactoryController.getInstance().registerNewFactory(new NetworkCraftingProductionResolverFactory());
		RequestableObjectRegistry.INSTANCE.register(NetworkCrafting.ID, NetworkCrafting::serialize, NetworkCrafting::deserialize);

		DeliverableObjectRegistry.INSTANCE.register(IngredientDeliverable.ID, IngredientDeliverable::serialize, IngredientDeliverable::deserialize);
		DeliverableObjectRegistry.INSTANCE.register(Butcherable.ID, Butcherable::serialize, Butcherable::deserialize);

		if (dist.isClient())
		{
			new MineColoniesCompatibilityClient(modContainer);
		}

	}

	private void onFMLCommonSetup(FMLCommonSetupEvent e)
	{
		e.enqueueWork(() ->
		{
			COMPAT.initialize();

			CustomizedToolSystem.registerDurabilityBasedLevel(ModEquipmentTypes.bow.get());
			CustomizedToolSystem.registerDurabilityBasedLevel(ModEquipmentTypes.fishing_rod.get());
			CustomizedToolSystem.registerDurabilityBasedLevel(ModEquipmentTypes.shears.get());
			CustomizedToolSystem.registerDurabilityBasedLevel(ModEquipmentTypes.shield.get());
			CustomizedToolSystem.registerDurabilityBasedLevel(ModEquipmentTypes.flint_and_steel.get());

			NetworkStorageViewRegistry.register((be, direction) -> be instanceof INetworkStorageViewHolder blockEntity ? blockEntity.getNetworkStorageView() : null);
		});
	}

	private void onModuleRegister(ModuleRegisterEvent e)
	{
		ModBuildings.guardTower.get().getModuleProducers().add(ModBuildingModules.GUNNER_TOWER_WORK);
		ModBuildings.barracksTower.get().getModuleProducers().add(ModBuildingModules.GUNNER_BARRACKS_WORK);

		ModBuildings.lumberjack.get().getModuleProducers().add(ModBuildingModules.ORCHARDIST_WORK);
		ModBuildings.lumberjack.get().getModuleProducers().add(ModBuildingModules.FRUITLIST_BLACKLIST);
		ModBuildings.wareHouse.get().getModuleProducers().add(ModBuildingModules.NETWORK_STORAGE);
		ModBuildings.blacksmith.get().getModuleProducers().add(ModBuildingModules.BLACKSMITH_SMITHING);
		ModBuildings.blacksmith.get().getModuleProducers().add(ModBuildingModules.BLACKSMITH_SMITHING_TEMPLATE_CRAFTING);

		ModBuildings.deliveryman.get().getModuleProducers().add(ModBuildingModules.FLUID_MANAGER_WORK);
		ModBuildings.deliveryman.get().getModuleProducers().add(ModBuildingModules.FLUID_MANAGER_BUCKET_FILLING);
		ModBuildings.deliveryman.get().getModuleProducers().add(ModBuildingModules.FLUID_MANAGER_LAVA_CAULDRON);

		ModBuildings.swineHerder.get().getModuleProducers().add(ModBuildingModules.BUTCHER_WORK);
		ModBuildings.swineHerder.get().getModuleProducers().add(ModBuildingModules.BUTCHERABLELIST_BLACKLIST);

		ModBuildings.chickenHerder.get().getModuleProducers().add(ModBuildingModules.BUTCHER_WORK);
		ModBuildings.chickenHerder.get().getModuleProducers().add(ModBuildingModules.BUTCHERABLELIST_BLACKLIST);

		ModBuildings.cowboy.get().getModuleProducers().add(ModBuildingModules.BUTCHER_WORK);
		ModBuildings.cowboy.get().getModuleProducers().add(ModBuildingModules.BUTCHERABLELIST_BLACKLIST);

		ModBuildings.rabbitHutch.get().getModuleProducers().add(ModBuildingModules.BUTCHER_WORK);
		ModBuildings.rabbitHutch.get().getModuleProducers().add(ModBuildingModules.BUTCHERABLELIST_BLACKLIST);

		ModBuildings.shepherd.get().getModuleProducers().add(ModBuildingModules.BUTCHER_WORK);
		ModBuildings.shepherd.get().getModuleProducers().add(ModBuildingModules.BUTCHERABLELIST_BLACKLIST);

		ModBuildings.stoneMason.get().getModuleProducers().add(ModBuildingModules.STONEMASON_STONECUTTING);
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

	private void onBuildCreativeModeTabContents(BuildCreativeModeTabContentsEvent e)
	{
		if (e.getTab() == ModCreativeTabs.GENERAL.get())
		{
			e.accept(ModItems.COMMON_NETWORK_STORAGE.get());
		}

	}

	private void onInjectBuildingSettingsModule(InjectBuildingSettingsModuleEvent e)
	{
		var buildingType = e.getBuilding().getBuildingType();

		if (buildingType == ModBuildings.guardTower.get())
		{
			e.register(BuildingModules.GUARD_SETTINGS, ModBuildingModules.GUARD_SETTINGS);
		}
		else if (buildingType == ModBuildings.barracksTower.get())
		{
			e.register(BuildingModules.GUARD_SETTINGS, ModBuildingModules.GUARD_SETTINGS);
		}
		else if (buildingType == ModBuildings.lumberjack.get())
		{
			e.register(BuildingModules.FORESTER_SETTINGS, ModBuildingModules.ORCHARDIST_SETTINGS);
		}
		else if (buildingType == ModBuildings.blacksmith.get())
		{
			e.register(BuildingModules.SETTINGS_CRAFTER_RECIPE, ModBuildingModules.BLACKSMITH_SETTINGS);
		}

	}

	private void onRecipesUpdated(RecipesUpdatedEvent e)
	{
		this.reloadRecipeBaseds(e.getRecipeManager());
	}

	private void onOnDatapackSync(OnDatapackSyncEvent e)
	{
		if (e.getPlayer() == null)
		{
			this.reloadRecipeBaseds(e.getPlayerList().getServer().getRecipeManager());
		}

	}

	private void reloadRecipeBaseds(RecipeManager recipeManager)
	{
		CustomizedButcherable.reload(recipeManager);
		Butcherable.reload();
	}

	private void onRegisterPayloadHandlers(RegisterPayloadHandlersEvent event)
	{
		var modVersion = ModList.get().getModContainerById(MOD_ID).get().getModInfo().getVersion().toString();
		var registry = new MessageRegistrar(event.registrar(MOD_ID).versioned(modVersion));
		ModMessagesRegistrar.register(registry);
	}

	public static ResourceLocation rl(String path)
	{
		return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
	}

	public static String tl(String path)
	{
		return MOD_ID + "." + path;
	}

}
