package steve_gall.minecolonies_compatibility.module.common.farmersdelight;

import java.util.Collections;
import java.util.List;

import com.minecolonies.api.colony.buildings.ModBuildings;
import com.minecolonies.core.colony.crafting.LootTableAnalyzer;

import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.common.NeoForge;
import steve_gall.minecolonies_compatibility.api.common.event.AnimalHerdingLootEvent;
import steve_gall.minecolonies_compatibility.api.common.event.AnimalHerdingToolEvent;
import steve_gall.minecolonies_compatibility.api.common.plant.CustomizedCrop;
import steve_gall.minecolonies_compatibility.api.common.plant.CustomizedFruit;
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
import steve_gall.minecolonies_compatibility.module.common.farmersdelight.init.ModuleMenuTypes;
import steve_gall.minecolonies_compatibility.module.common.farmersdelight.network.CookingOpenTeachMessage;
import steve_gall.minecolonies_compatibility.module.common.farmersdelight.network.CuttingOpenTeachMessage;
import steve_gall.minecolonies_compatibility.module.common.farmersdelight.network.PlatingOpenTeachMessage;
import steve_gall.minecolonies_tweaks.api.common.crafting.CustomizedRecipeStorageRegistry;
import steve_gall.minecolonies_tweaks.api.common.network.MessageRegistrar;
import vectorwing.farmersdelight.common.block.MushroomColonyBlock;
import vectorwing.farmersdelight.common.registry.ModItems;

public class FarmersDelightModule extends AbstractModule
{
	@Override
	protected void onLoad()
	{
		super.onLoad();

		var fml_bus = ModLoadingContext.get().getActiveContainer().getEventBus();
		ModuleCraftingTypes.REGISTER.register(fml_bus);
		ModuleMenuTypes.REGISTER.register(fml_bus);

		var forge_bus = NeoForge.EVENT_BUS;
		forge_bus.addListener(this::onAnimalHerdingTool);
		forge_bus.addListener(this::onAnimalHerdingLoot);

		CustomizedRecipeStorageRegistry.INSTANCE.register(CuttingRecipeStorage.ID, CuttingRecipeStorage::serialize, CuttingRecipeStorage::deserialize);
		CustomizedRecipeStorageRegistry.INSTANCE.register(CookingRecipeStorage.ID, CookingRecipeStorage::serialize, CookingRecipeStorage::new);
		CustomizedRecipeStorageRegistry.INSTANCE.register(PlatingRecipeStorage.ID, PlatingRecipeStorage::serialize, PlatingRecipeStorage::deserialize);
	}

	@Override
	protected void onInitBuildingModule()
	{
		super.onInitBuildingModule();
		ModBuildings.kitchen.get().getModuleProducers().add(ModuleBuildingModules.CHEF_CUTTING);
		ModBuildings.kitchen.get().getModuleProducers().add(ModuleBuildingModules.CHEF_COOKING);
		ModBuildings.kitchen.get().getModuleProducers().add(ModuleBuildingModules.CHEF_PLATING);
		ModBuildings.lumberjack.get().getModuleProducers().add(ModuleBuildingModules.LUMBERJACK_CUTTING);
		ModBuildings.stoneMason.get().getModuleProducers().add(ModuleBuildingModules.STONEMASON_CUTTING);
	}

	@Override
	protected void onFMLCommonSetup(FMLCommonSetupEvent e)
	{
		super.onFMLCommonSetup(e);
		e.enqueueWork(() ->
		{
			CustomizedCrop.register(new TomatoCrop());

			for (var block : BuiltInRegistries.BLOCK)
			{
				if (block instanceof MushroomColonyBlock mushroomColony)
				{
					CustomizedFruit.register(new MushroomColonyFruit(mushroomColony));
				}

			}

			CustomizedFruit.register(new RiceFruit());
		});
	}

	@Override
	protected void onRegisterMenuScreens(RegisterMenuScreensEvent e)
	{
		super.onRegisterMenuScreens(e);

		e.register(ModuleMenuTypes.CUTTING_TEACH.get(), CuttingTeachScreen::new);
		e.register(ModuleMenuTypes.COOKING_TEACH.get(), CookingTeachScreen::new);
		e.register(ModuleMenuTypes.PLATING_TEACH.get(), PlatingTeachScreen::new);
	}

	@Override
	protected void onRegisterNetwork(MessageRegistrar channel)
	{
		super.onRegisterNetwork(channel);

		channel.playToServer(CuttingOpenTeachMessage.TYPE, CuttingOpenTeachMessage::new);
		channel.playToServer(CookingOpenTeachMessage.TYPE, CookingOpenTeachMessage::new);
		channel.playToServer(PlatingOpenTeachMessage.TYPE, PlatingOpenTeachMessage::new);
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
		var type = e.getRecipe().getRequiredEntity();

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
