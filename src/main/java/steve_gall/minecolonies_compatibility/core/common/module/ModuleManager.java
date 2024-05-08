package steve_gall.minecolonies_compatibility.core.common.module;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import steve_gall.minecolonies_compatibility.core.common.module.aether.AetherModule;
import steve_gall.minecolonies_compatibility.core.common.module.ars_nouveau.ArsNouveauModule;
import steve_gall.minecolonies_compatibility.core.common.module.blue_skies.BlueSkiesModule;
import steve_gall.minecolonies_compatibility.core.common.module.croptopia.CroptopiaModule;
import steve_gall.minecolonies_compatibility.core.common.module.delightful.DelightfulModule;
import steve_gall.minecolonies_compatibility.core.common.module.farmersdelight.FarmersDelightModule;
import steve_gall.minecolonies_compatibility.core.common.module.ie.IEModule;
import steve_gall.minecolonies_compatibility.core.common.module.minecraft.MinecraftModule;
import steve_gall.minecolonies_compatibility.core.common.module.pamhc2trees.PamsHarvestCraft2TreesModule;
import steve_gall.minecolonies_compatibility.core.common.module.polymorph.PolymorphModule;
import steve_gall.minecolonies_compatibility.core.common.module.regions_unexplored.RegionsUnexploredModule;
import steve_gall.minecolonies_compatibility.core.common.module.thermal.ThermalModule;

public class ModuleManager
{
	public static final List<OptionalModule> MODULES;
	private static boolean INITIALIZED;
	private static final List<OptionalModule> _LOADED_MODULES;
	public static final List<OptionalModule> LOADED_MODULES;

	public static final OptionalModule MINECRAFT;
	public static final OptionalModule IE;
	public static final OptionalModule CROPTOPIA;
	public static final OptionalModule PHC2TREES;
	public static final OptionalModule FARMERSDELIGHT;
	public static final OptionalModule DELIGHTFUL;
	public static final OptionalModule THERMAL;
	public static final OptionalModule ARS_NOUVEAU;
	public static final OptionalModule POLYMORPH;
	public static final OptionalModule BLUE_SKIES;
	public static final OptionalModule REGIONS_UNEXPLORED;
	public static final OptionalModule AETHER;

	static
	{
		var modules = new ArrayList<OptionalModule>();
		modules.add(MINECRAFT = new OptionalModule("minecraft", () -> MinecraftModule::onLoad));
		modules.add(IE = new OptionalModule("immersiveengineering", () -> IEModule::onLoad));
		modules.add(CROPTOPIA = new OptionalModule("croptopia", () -> CroptopiaModule::onLoad));
		modules.add(PHC2TREES = new OptionalModule("pamhc2trees", () -> PamsHarvestCraft2TreesModule::onLoad));
		modules.add(FARMERSDELIGHT = new OptionalModule("farmersdelight", () -> FarmersDelightModule::onLoad));
		modules.add(DELIGHTFUL = new OptionalModule("delightful", () -> DelightfulModule::onLoad));
		modules.add(THERMAL = new OptionalModule("thermal", () -> ThermalModule::onLoad));
		modules.add(ARS_NOUVEAU = new OptionalModule("ars_nouveau", () -> ArsNouveauModule::onLoad));
		modules.add(POLYMORPH = new OptionalModule("polymorph", () -> PolymorphModule::onLoad));
		modules.add(BLUE_SKIES = new OptionalModule("blue_skies", () -> BlueSkiesModule::onLoad));
		modules.add(REGIONS_UNEXPLORED = new OptionalModule("regions_unexplored", () -> RegionsUnexploredModule::onLoad));
		modules.add(AETHER = new OptionalModule("aether", () -> AetherModule::onLoad));

		MODULES = Collections.unmodifiableList(modules);
		_LOADED_MODULES = new ArrayList<>();
		LOADED_MODULES = Collections.unmodifiableList(_LOADED_MODULES);
	}

	public static boolean isInitialized()
	{
		return INITIALIZED;
	}

	public static void initialize()
	{
		if (INITIALIZED)
		{
			throw new IllegalCallerException();
		}

		INITIALIZED = true;
		_LOADED_MODULES.clear();

		for (var module : MODULES)
		{
			module.tryLoad();

			if (module.isLoaded())
			{
				_LOADED_MODULES.add(module);
			}

		}

	}

}
