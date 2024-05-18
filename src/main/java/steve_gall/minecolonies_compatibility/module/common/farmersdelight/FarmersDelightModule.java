package steve_gall.minecolonies_compatibility.module.common.farmersdelight;

import java.util.Collections;
import java.util.List;

import com.minecolonies.api.colony.buildings.ModBuildings;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import steve_gall.minecolonies_compatibility.api.common.plant.CustomizedCrop;
import steve_gall.minecolonies_compatibility.core.common.init.ModToolTypes;
import steve_gall.minecolonies_compatibility.module.client.farmersdelight.FarmersDelightModuleClient;
import steve_gall.minecolonies_compatibility.module.common.farmersdelight.crafting.CookingRecipeStorage;
import steve_gall.minecolonies_compatibility.module.common.farmersdelight.crafting.CuttingRecipeStorage;
import steve_gall.minecolonies_compatibility.module.common.farmersdelight.init.ModuleBuildingModules;
import steve_gall.minecolonies_compatibility.module.common.farmersdelight.init.ModuleCraftingTypes;
import steve_gall.minecolonies_compatibility.module.common.farmersdelight.init.ModuleJobs;
import steve_gall.minecolonies_compatibility.module.common.farmersdelight.init.ModuleMenuTypes;
import steve_gall.minecolonies_tweaks.api.common.crafting.CustomizedRecipeStorageRegistry;
import steve_gall.minecolonies_tweaks.api.common.tool.CustomToolTypeRegisterEvent;

public class FarmersDelightModule
{
	public static void onLoad()
	{
		var fml_bus = FMLJavaModLoadingContext.get().getModEventBus();
		ModuleCraftingTypes.REGISTER.register(fml_bus);
		ModuleMenuTypes.REGISTER.register(fml_bus);
		ModuleJobs.REGISTER.register(fml_bus);
		fml_bus.addListener(FarmersDelightModule::onFMLCommonSetup);

		var forge_bus = MinecraftForge.EVENT_BUS;
		forge_bus.addListener(FarmersDelightModule::onCustomToolTypeRegister);

		CustomizedCrop.register(new TomatoCrop());
		CustomizedRecipeStorageRegistry.INSTANCE.register(CuttingRecipeStorage.ID, CuttingRecipeStorage::serialize, CuttingRecipeStorage::deserialize);
		CustomizedRecipeStorageRegistry.INSTANCE.register(CookingRecipeStorage.ID, CookingRecipeStorage::serialize, CookingRecipeStorage::deserialize);

		DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> FarmersDelightModuleClient::onLoad);
	}

	private static void onFMLCommonSetup(FMLCommonSetupEvent e)
	{
		ModBuildings.cook.get().getModuleProducers().add(ModuleBuildingModules.COOKASSISTENT_CUTTING);
		ModBuildings.cook.get().getModuleProducers().add(ModuleBuildingModules.COOK_WORK);
		ModBuildings.cook.get().getModuleProducers().add(ModuleBuildingModules.COOK_COOKING);
	}

	private static void onCustomToolTypeRegister(CustomToolTypeRegisterEvent e)
	{
		e.register(ModToolTypes.KNIFE);
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
