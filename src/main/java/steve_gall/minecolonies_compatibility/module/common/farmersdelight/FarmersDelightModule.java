package steve_gall.minecolonies_compatibility.module.common.farmersdelight;

import java.util.Collections;
import java.util.List;

import com.minecolonies.api.colony.buildings.ModBuildings;
import com.minecolonies.core.colony.crafting.LootTableAnalyzer;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import steve_gall.minecolonies_compatibility.api.common.event.AnimalHerdingLootEvent;
import steve_gall.minecolonies_compatibility.api.common.event.AnimalHerdingToolEvent;
import steve_gall.minecolonies_compatibility.api.common.plant.CustomizedCrop;
import steve_gall.minecolonies_compatibility.api.common.plant.CustomizedFruit;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.core.common.init.ModToolTypes;
import steve_gall.minecolonies_compatibility.module.client.farmersdelight.CookingTeachScreen;
import steve_gall.minecolonies_compatibility.module.client.farmersdelight.CuttingTeachScreen;
import steve_gall.minecolonies_compatibility.module.client.farmersdelight.PlatingTeachScreen;
import steve_gall.minecolonies_compatibility.module.common.AbstractModule;
import steve_gall.minecolonies_compatibility.module.common.farmersdelight.crafting.CookingRecipeStorage;
import steve_gall.minecolonies_compatibility.module.common.farmersdelight.crafting.CuttingRecipeStorage;
import steve_gall.minecolonies_compatibility.module.common.farmersdelight.crafting.PlatingRecipeStorage;
import steve_gall.minecolonies_compatibility.module.common.farmersdelight.init.ModuleBuildingModules;
import steve_gall.minecolonies_compatibility.module.common.farmersdelight.init.ModuleCraftingTypes;
import steve_gall.minecolonies_compatibility.module.common.farmersdelight.init.ModuleJobs;
import steve_gall.minecolonies_compatibility.module.common.farmersdelight.init.ModuleMenuTypes;
import steve_gall.minecolonies_compatibility.module.common.farmersdelight.network.CookingOpenTeachMessage;
import steve_gall.minecolonies_compatibility.module.common.farmersdelight.network.CuttingOpenTeachMessage;
import steve_gall.minecolonies_compatibility.module.common.farmersdelight.network.PlatingOpenTeachMessage;
import steve_gall.minecolonies_tweaks.api.common.crafting.CustomizedRecipeStorageRegistry;
import vectorwing.farmersdelight.common.block.MushroomColonyBlock;
import vectorwing.farmersdelight.common.registry.ModItems;

public class FarmersDelightModule extends AbstractModule
{
	@Override
	protected void onLoad()
	{
		super.onLoad();

		var fml_bus = FMLJavaModLoadingContext.get().getModEventBus();
		ModuleCraftingTypes.REGISTER.register(fml_bus);
		ModuleMenuTypes.REGISTER.register(fml_bus);
		ModuleJobs.REGISTER.register(fml_bus);

		var forge_bus = MinecraftForge.EVENT_BUS;
		forge_bus.addListener(this::onAnimalHerdingTool);
		forge_bus.addListener(this::onAnimalHerdingLoot);

		var network = MineColoniesCompatibility.network();
		network.registerMessage(CuttingOpenTeachMessage.class, CuttingOpenTeachMessage::new);
		network.registerMessage(CookingOpenTeachMessage.class, CookingOpenTeachMessage::new);
		network.registerMessage(PlatingOpenTeachMessage.class, PlatingOpenTeachMessage::new);

		CustomizedRecipeStorageRegistry.INSTANCE.register(CuttingRecipeStorage.ID, CuttingRecipeStorage::serialize, CuttingRecipeStorage::deserialize);
		CustomizedRecipeStorageRegistry.INSTANCE.register(CookingRecipeStorage.ID, CookingRecipeStorage::serialize, CookingRecipeStorage::deserialize);
		CustomizedRecipeStorageRegistry.INSTANCE.register(PlatingRecipeStorage.ID, PlatingRecipeStorage::serialize, PlatingRecipeStorage::deserialize);
	}

	@Override
	protected void onFMLCommonSetup(FMLCommonSetupEvent e)
	{
		super.onFMLCommonSetup(e);
		e.enqueueWork(() ->
		{
			CustomizedCrop.register(new TomatoCrop());

			for (var block : ForgeRegistries.BLOCKS.getValues())
			{
				if (block instanceof MushroomColonyBlock mushroomColony)
				{
					CustomizedFruit.register(new MushroomColonyFruit(mushroomColony));
				}

			}

			CustomizedFruit.register(new RiceFruit());

			ModBuildings.cook.get().getModuleProducers().add(ModuleBuildingModules.COOKASSISTENT_CUTTING);
			ModBuildings.cook.get().getModuleProducers().add(ModuleBuildingModules.COOKASSISTENT_COOKING);
			ModBuildings.cook.get().getModuleProducers().add(ModuleBuildingModules.COOKASSISTENT_PLATING);
			ModBuildings.lumberjack.get().getModuleProducers().add(ModuleBuildingModules.LUMBERJACK_CUTTING);
			ModBuildings.stoneMason.get().getModuleProducers().add(ModuleBuildingModules.STONEMASON_CUTTING);
		});
	}

	@Override
	protected void onFMLClientSetup(FMLClientSetupEvent e)
	{
		super.onFMLClientSetup(e);

		MenuScreens.register(ModuleMenuTypes.CUTTING_TEACH.get(), CuttingTeachScreen::new);
		MenuScreens.register(ModuleMenuTypes.COOKING_TEACH.get(), CookingTeachScreen::new);
		MenuScreens.register(ModuleMenuTypes.PLATING_TEACH.get(), PlatingTeachScreen::new);
	}

	private void onAnimalHerdingTool(AnimalHerdingToolEvent e)
	{
		var type = e.getAnimal().getType();

		if (type == EntityType.PIG || type == EntityType.HOGLIN)
		{
			e.register(ModToolTypes.KNIFE.getToolType());
		}

	}

	private void onAnimalHerdingLoot(AnimalHerdingLootEvent e)
	{
		var type = e.getRecipe().getRequiredEntity().getType();

		if (type == EntityType.PIG || type == EntityType.HOGLIN)
		{
			if (e.getRecipe().getRequiredTool() == ModToolTypes.KNIFE.getToolType())
			{
				e.register(new LootTableAnalyzer.LootDrop(Collections.singletonList(new ItemStack(ModItems.HAM.get())), 0.5F, 1, false));
			}

		}

	}

	public static List<Component> getChanceTooltip(float chance)
	{
		if (chance != 1.0F)
		{
			return Collections.singletonList(createChanceTooltip(chance));
		}
		else
		{
			return Collections.emptyList();
		}

	}

	public static Component createChanceTooltip(float chance)
	{
		return Component.translatable("farmersdelight.jei.chance", chance < 0.01 ? "<1" : (int) (chance * 100)).withStyle(ChatFormatting.GOLD);
	}

}
