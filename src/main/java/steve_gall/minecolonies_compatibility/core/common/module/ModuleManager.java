package steve_gall.minecolonies_compatibility.core.common.module;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import steve_gall.minecolonies_compatibility.core.common.module.croptopia.CroptopiaModule;
import steve_gall.minecolonies_compatibility.core.common.module.farmersdelight.FarmersDelightModule;
import steve_gall.minecolonies_compatibility.core.common.module.ie.IEModule;
import steve_gall.minecolonies_compatibility.core.common.module.minecraft.MinecraftModule;
import steve_gall.minecolonies_compatibility.core.common.module.pamhc2trees.PamsHarvestCraft2TreesModule;
import steve_gall.minecolonies_compatibility.core.common.module.thermal.ThermalModule;

public class ModuleManager
{
	public static final List<AbstractModule> MODULES;
	private static boolean INITIALIZED;
	private static final List<AbstractModule> _LOADED_MODULES;
	public static final List<AbstractModule> LOADED_MODULES;

	public static final MinecraftModule MINECRAFT;
	public static final IEModule IE;
	public static final CroptopiaModule CROPTOPIA;
	public static final PamsHarvestCraft2TreesModule PHC2TREES;
	public static final FarmersDelightModule FARMERSDELIGHT;
	public static final ThermalModule THERMAL;

	static
	{
		var modules = new ArrayList<AbstractModule>();
		modules.add(MINECRAFT = new MinecraftModule());
		modules.add(IE = new IEModule());
		modules.add(CROPTOPIA = new CroptopiaModule());
		modules.add(PHC2TREES = new PamsHarvestCraft2TreesModule());
		modules.add(FARMERSDELIGHT = new FarmersDelightModule());
		modules.add(THERMAL = new ThermalModule());

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
