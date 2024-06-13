package steve_gall.minecolonies_compatibility.module.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

import steve_gall.minecolonies_compatibility.module.common.ae2.AppliedEnergistics2Module;
import steve_gall.minecolonies_compatibility.module.common.aether.AetherModule;
import steve_gall.minecolonies_compatibility.module.common.ars_nouveau.ArsNouveauModule;
import steve_gall.minecolonies_compatibility.module.common.blue_skies.BlueSkiesModule;
import steve_gall.minecolonies_compatibility.module.common.cobblemon.CobblemonModule;
import steve_gall.minecolonies_compatibility.module.common.croptopia.CroptopiaModule;
import steve_gall.minecolonies_compatibility.module.common.cyclic.CyclicModule;
import steve_gall.minecolonies_compatibility.module.common.delightful.DelightfulModule;
import steve_gall.minecolonies_compatibility.module.common.farmersdelight.FarmersDelightModule;
import steve_gall.minecolonies_compatibility.module.common.ie.IEModule;
import steve_gall.minecolonies_compatibility.module.common.minecraft.MinecraftModule;
import steve_gall.minecolonies_compatibility.module.common.oreberries.OreberriesModule;
import steve_gall.minecolonies_compatibility.module.common.pamhc2trees.PamsHarvestCraft2TreesModule;
import steve_gall.minecolonies_compatibility.module.common.polymorph.PolymorphModule;
import steve_gall.minecolonies_compatibility.module.common.refinedstorage.RefinedStorageModule;
import steve_gall.minecolonies_compatibility.module.common.regions_unexplored.RegionsUnexploredModule;
import steve_gall.minecolonies_compatibility.module.common.reliquary.ReliquaryModule;
import steve_gall.minecolonies_compatibility.module.common.thermal.ThermalModule;
import steve_gall.minecolonies_compatibility.module.common.undergarden.UndergardenModule;
import steve_gall.minecolonies_compatibility.module.common.vinery.VineryModule;

public class ModuleManager
{
	private static final List<OptionalModule<?>> _MODULES;
	public static final List<OptionalModule<?>> MODULES;
	private static boolean INITIALIZED;
	private static final List<OptionalModule<?>> _LOADED_MODULES;
	public static final List<OptionalModule<?>> LOADED_MODULES;

	static
	{
		_MODULES = new ArrayList<>();
		MODULES = Collections.unmodifiableList(_MODULES);

		_LOADED_MODULES = new ArrayList<>();
		LOADED_MODULES = Collections.unmodifiableList(_LOADED_MODULES);
	}

	public static final OptionalModule<MinecraftModule> MINECRAFT = register("minecraft", () -> MinecraftModule::new);
	public static final OptionalModule<AppliedEnergistics2Module> AE2 = register("ae2", () -> AppliedEnergistics2Module::new);
	public static final OptionalModule<AetherModule> AETHER = register("aether", () -> AetherModule::new);
	public static final OptionalModule<ArsNouveauModule> ARS_NOUVEAU = register("ars_nouveau", () -> ArsNouveauModule::new);
	public static final OptionalModule<BlueSkiesModule> BLUE_SKIES = register("blue_skies", () -> BlueSkiesModule::new);
	public static final OptionalModule<CroptopiaModule> CROPTOPIA = register("croptopia", () -> CroptopiaModule::new);
	public static final OptionalModule<CyclicModule> CYCLIC = register("cyclic", () -> CyclicModule::new);
	public static final OptionalModule<DelightfulModule> DELIGHTFUL = register("delightful", () -> DelightfulModule::new);
	public static final OptionalModule<FarmersDelightModule> FARMERSDELIGHT = register("farmersdelight", () -> FarmersDelightModule::new);
	public static final OptionalModule<IEModule> IE = register("immersiveengineering", () -> IEModule::new);
	public static final OptionalModule<OreberriesModule> OREBERRIES = register("oreberriesreplanted", () -> OreberriesModule::new);
	public static final OptionalModule<PamsHarvestCraft2TreesModule> PHC2TREES = register("pamhc2trees", () -> PamsHarvestCraft2TreesModule::new);
	public static final OptionalModule<PolymorphModule> POLYMORPH = register("polymorph", () -> PolymorphModule::new);
	public static final OptionalModule<RegionsUnexploredModule> REGIONS_UNEXPLORED = register("regions_unexplored", () -> RegionsUnexploredModule::new);
	public static final OptionalModule<ReliquaryModule> RELIQUARY = register("reliquary", () -> ReliquaryModule::new);
	public static final OptionalModule<RefinedStorageModule> RS = register("refinedstorage", () -> RefinedStorageModule::new);
	public static final OptionalModule<ThermalModule> THERMAL = register("thermal", () -> ThermalModule::new);
	public static final OptionalModule<UndergardenModule> UNDERGARDEN = register("undergarden", () -> UndergardenModule::new);
	public static final OptionalModule<VineryModule> VINERY = register("vinery", () -> VineryModule::new);

	public static final OptionalModule<CobblemonModule> COBBLEMON = register("cobblemon", () -> CobblemonModule::new);

	private static <MODULE extends AbstractModule> OptionalModule<MODULE> register(String modid, Supplier<Supplier<MODULE>> initializer)
	{
		var module = new OptionalModule<>(modid, initializer);
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
