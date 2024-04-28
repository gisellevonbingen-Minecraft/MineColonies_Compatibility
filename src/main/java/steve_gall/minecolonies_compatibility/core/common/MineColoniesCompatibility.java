package steve_gall.minecolonies_compatibility.core.common;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.minecolonies.api.colony.buildings.ModBuildings;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import steve_gall.minecolonies_compatibility.core.common.config.MineColoniesCompatibilityConfigCommon;
import steve_gall.minecolonies_compatibility.core.common.config.MineColoniesCompatibilityConfigServer;
import steve_gall.minecolonies_compatibility.core.common.init.ModBuildingModules;
import steve_gall.minecolonies_compatibility.core.common.init.ModGuardTypes;
import steve_gall.minecolonies_compatibility.core.common.init.ModJobs;
import steve_gall.minecolonies_compatibility.core.common.init.ModToolTypes;
import steve_gall.minecolonies_compatibility.core.common.module.ModuleManager;
import steve_gall.minecolonies_tweaks.api.common.tool.CustomToolTypeRegisterEvent;

@Mod(MineColoniesCompatibility.MOD_ID)
public class MineColoniesCompatibility
{
	public static final String MOD_ID = "minecolonies_compatibility";
	public static final Logger LOGGER = LogManager.getLogger();

	public MineColoniesCompatibility()
	{
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, MineColoniesCompatibilityConfigCommon.SPEC);
		ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, MineColoniesCompatibilityConfigServer.SPEC);

		var fml_bus = FMLJavaModLoadingContext.get().getModEventBus();
		ModGuardTypes.REGISTER.register(fml_bus);
		ModJobs.REGISTER.register(fml_bus);
		fml_bus.addListener(this::onFMLCommonSetup);

		var forge_bus = MinecraftForge.EVENT_BUS;
		forge_bus.addListener(this::onCustomToolTypeRegister);

		ModuleManager.initialize();
	}

	private void onFMLCommonSetup(FMLCommonSetupEvent e)
	{
		ModBuildings.guardTower.get().getModuleProducers().add(ModBuildingModules.GUNNER_TOWER_WORK);
		ModBuildings.lumberjack.get().getModuleProducers().add(ModBuildingModules.ORCHARDIST_WORK);
	}

	private void onCustomToolTypeRegister(CustomToolTypeRegisterEvent e)
	{
		e.register(ModToolTypes.GUN);
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
