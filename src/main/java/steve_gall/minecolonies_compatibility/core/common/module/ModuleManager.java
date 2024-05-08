package steve_gall.minecolonies_compatibility.core.common.module;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

import steve_gall.minecolonies_compatibility.core.common.module.aether.AetherModule;
import steve_gall.minecolonies_compatibility.core.common.module.ars_nouveau.ArsNouveauModule;
import steve_gall.minecolonies_compatibility.core.common.module.blue_skies.BlueSkiesModule;
import steve_gall.minecolonies_compatibility.core.common.module.cobblemon.CobblemonModule;
import steve_gall.minecolonies_compatibility.core.common.module.croptopia.CroptopiaModule;
import steve_gall.minecolonies_compatibility.core.common.module.cyclic.CyclicModule;
import steve_gall.minecolonies_compatibility.core.common.module.delightful.DelightfulModule;
import steve_gall.minecolonies_compatibility.core.common.module.farmersdelight.FarmersDelightModule;
import steve_gall.minecolonies_compatibility.core.common.module.ie.IEModule;
import steve_gall.minecolonies_compatibility.core.common.module.minecraft.MinecraftModule;
import steve_gall.minecolonies_compatibility.core.common.module.oreberries.OreberriesModule;
import steve_gall.minecolonies_compatibility.core.common.module.pamhc2trees.PamsHarvestCraft2TreesModule;
import steve_gall.minecolonies_compatibility.core.common.module.polymorph.PolymorphModule;
import steve_gall.minecolonies_compatibility.core.common.module.regions_unexplored.RegionsUnexploredModule;
import steve_gall.minecolonies_compatibility.core.common.module.thermal.ThermalModule;
import steve_gall.minecolonies_compatibility.core.common.module.undergarden.UndergardenModule;
import steve_gall.minecolonies_compatibility.core.common.module.vinery.VineryModule;

public class ModuleManager
{
	private static final List<OptionalModule> _MODULES;
	public static final List<OptionalModule> MODULES;
	private static boolean INITIALIZED;
	private static final List<OptionalModule> _LOADED_MODULES;
	public static final List<OptionalModule> LOADED_MODULES;

	static
	{
		_MODULES = new ArrayList<>();
		MODULES = Collections.unmodifiableList(_MODULES);

		_LOADED_MODULES = new ArrayList<>();
		LOADED_MODULES = Collections.unmodifiableList(_LOADED_MODULES);
	}

	public static final OptionalModule MINECRAFT = register("minecraft", () -> MinecraftModule::onLoad);
	public static final OptionalModule AETHER = register("aether", () -> AetherModule::onLoad);
	public static final OptionalModule ARS_NOUVEAU = register("ars_nouveau", () -> ArsNouveauModule::onLoad);
	public static final OptionalModule BLUE_SKIES = register("blue_skies", () -> BlueSkiesModule::onLoad);
	public static final OptionalModule CROPTOPIA = register("croptopia", () -> CroptopiaModule::onLoad);
	public static final OptionalModule CYCLIC = register("cyclic", () -> CyclicModule::onLoad);
	public static final OptionalModule DELIGHTFUL = register("delightful", () -> DelightfulModule::onLoad);
	public static final OptionalModule FARMERSDELIGHT = register("farmersdelight", () -> FarmersDelightModule::onLoad);
	public static final OptionalModule IE = register("immersiveengineering", () -> IEModule::onLoad);
	public static final OptionalModule OREBERRIES = register("oreberriesreplanted", () -> OreberriesModule::onLoad);
	public static final OptionalModule PHC2TREES = register("pamhc2trees", () -> PamsHarvestCraft2TreesModule::onLoad);
	public static final OptionalModule POLYMORPH = register("polymorph", () -> PolymorphModule::onLoad);
	public static final OptionalModule REGIONS_UNEXPLORED = register("regions_unexplored", () -> RegionsUnexploredModule::onLoad);
	public static final OptionalModule THERMAL = register("thermal", () -> ThermalModule::onLoad);
	public static final OptionalModule UNDERGARDEN = register("undergarden", () -> UndergardenModule::onLoad);
	public static final OptionalModule VINERY = register("vinery", () -> VineryModule::onLoad);

	public static final OptionalModule COBBLEMON = register("cobblemon", () -> CobblemonModule::onLoad);

	private static OptionalModule register(String modid, Supplier<Runnable> initializer)
	{
		var module = new OptionalModule(modid, initializer);
		_MODULES.add(module);
		return module;
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
