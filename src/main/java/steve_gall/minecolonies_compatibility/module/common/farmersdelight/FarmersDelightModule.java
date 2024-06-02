package steve_gall.minecolonies_compatibility.module.common.farmersdelight;

import java.util.Collections;
import java.util.List;

import com.minecolonies.api.colony.buildings.ModBuildings;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.network.chat.Component;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import steve_gall.minecolonies_compatibility.api.common.plant.CustomizedCrop;
import steve_gall.minecolonies_compatibility.module.client.farmersdelight.TeachCookingScreen;
import steve_gall.minecolonies_compatibility.module.client.farmersdelight.TeachCuttingScreen;
import steve_gall.minecolonies_compatibility.module.common.AbstractModule;
import steve_gall.minecolonies_compatibility.module.common.farmersdelight.crafting.CookingRecipeStorage;
import steve_gall.minecolonies_compatibility.module.common.farmersdelight.crafting.CuttingRecipeStorage;
import steve_gall.minecolonies_compatibility.module.common.farmersdelight.init.ModuleBuildingModules;
import steve_gall.minecolonies_compatibility.module.common.farmersdelight.init.ModuleCraftingTypes;
import steve_gall.minecolonies_compatibility.module.common.farmersdelight.init.ModuleJobs;
import steve_gall.minecolonies_compatibility.module.common.farmersdelight.init.ModuleMenuTypes;
import steve_gall.minecolonies_tweaks.api.common.crafting.CustomizedRecipeStorageRegistry;

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

		CustomizedRecipeStorageRegistry.INSTANCE.register(CuttingRecipeStorage.ID, CuttingRecipeStorage::serialize, CuttingRecipeStorage::deserialize);
		CustomizedRecipeStorageRegistry.INSTANCE.register(CookingRecipeStorage.ID, CookingRecipeStorage::serialize, CookingRecipeStorage::deserialize);
	}

	@Override
	protected void onFMLCommonSetup(FMLCommonSetupEvent e)
	{
		super.onFMLCommonSetup(e);
		e.enqueueWork(() ->
		{
			CustomizedCrop.register(new TomatoCrop());

			ModBuildings.cook.get().getModuleProducers().add(ModuleBuildingModules.COOKASSISTENT_CUTTING);
			ModBuildings.cook.get().getModuleProducers().add(ModuleBuildingModules.COOK_WORK);
			ModBuildings.cook.get().getModuleProducers().add(ModuleBuildingModules.COOK_COOKING);
		});
	}

	@Override
	protected void onFMLClientSetup(FMLClientSetupEvent e)
	{
		super.onFMLClientSetup(e);

		MenuScreens.register(ModuleMenuTypes.TEACH_CUTTING.get(), TeachCuttingScreen::new);
		MenuScreens.register(ModuleMenuTypes.TEACH_COOKING.get(), TeachCookingScreen::new);
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
