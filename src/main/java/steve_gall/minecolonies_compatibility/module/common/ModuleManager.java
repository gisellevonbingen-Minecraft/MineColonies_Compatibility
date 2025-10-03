package steve_gall.minecolonies_compatibility.module.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Supplier;

import steve_gall.minecolonies_compatibility.module.common.ae2.AppliedEnergistics2Module;
import steve_gall.minecolonies_compatibility.module.common.aether.AetherModule;
import steve_gall.minecolonies_compatibility.module.common.ars_nouveau.ArsNouveauModule;
import steve_gall.minecolonies_compatibility.module.common.atmospheric.AtmosphericModule;
import steve_gall.minecolonies_compatibility.module.common.butchercraft.ButchercraftModule;
import steve_gall.minecolonies_compatibility.module.common.cobblemon.CobblemonModule;
import steve_gall.minecolonies_compatibility.module.common.croptopia.CroptopiaModule;
import steve_gall.minecolonies_compatibility.module.common.ewewukeks_musketmod.ewewukekMusketModule;
import steve_gall.minecolonies_compatibility.module.common.farmersdelight.FarmersDelightModule;
import steve_gall.minecolonies_compatibility.module.common.ie.IEModule;
import steve_gall.minecolonies_compatibility.module.common.jade.JadeModule;
import steve_gall.minecolonies_compatibility.module.common.lets_do_meadow.LetsDoMeadowModule;
import steve_gall.minecolonies_compatibility.module.common.minecraft.MinecraftModule;
import steve_gall.minecolonies_compatibility.module.common.neapolitan.NeapolitanModule;
import steve_gall.minecolonies_compatibility.module.common.oreberries.OreberriesModule;
import steve_gall.minecolonies_compatibility.module.common.pamhc2trees.PamsHarvestCraft2TreesModule;
import steve_gall.minecolonies_compatibility.module.common.polymorph.PolymorphModule;
import steve_gall.minecolonies_compatibility.module.common.regions_unexplored.RegionsUnexploredModule;
import steve_gall.minecolonies_compatibility.module.common.reliquary.ReliquaryModule;
import steve_gall.minecolonies_compatibility.module.common.undergarden.UndergardenModule;

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
	public static final OptionalModule<AtmosphericModule> ATMOSPHERIC = register("atmospheric", () -> AtmosphericModule::new);
	public static final OptionalModule<CobblemonModule> COBBLEMON = register("cobblemon", () -> CobblemonModule::new);
	public static final OptionalModule<CroptopiaModule> CROPTOPIA = register("croptopia", () -> CroptopiaModule::new);
	public static final OptionalModule<ewewukekMusketModule> EWEWUKEK_MUSKET = register("musketmod", () -> ewewukekMusketModule::new);
	public static final OptionalModule<FarmersDelightModule> FARMERSDELIGHT = register("farmersdelight", () -> FarmersDelightModule::new);
	public static final OptionalModule<ButchercraftModule> BUTCHERCRAFT = register("butchercraft", () -> ButchercraftModule::new);
	public static final OptionalModule<IEModule> IE = register("immersiveengineering", () -> IEModule::new);
	public static final OptionalModule<JadeModule> JADE = register("jade", () -> JadeModule::new);
	public static final OptionalModule<LetsDoMeadowModule> LETS_DO_MEADOW = register("meadow", () -> LetsDoMeadowModule::new);
	public static final OptionalModule<NeapolitanModule> NEAPOLITAN = register("neapolitan", () -> NeapolitanModule::new);
	public static final OptionalModule<OreberriesModule> OREBERRIES = register("oreberriesreplanted", () -> OreberriesModule::new);
	public static final OptionalModule<PamsHarvestCraft2TreesModule> PHC2TREES = register("pamhc2trees", () -> PamsHarvestCraft2TreesModule::new);
	public static final OptionalModule<PolymorphModule> POLYMORPH = register("polymorph", () -> PolymorphModule::new);
	public static final OptionalModule<RegionsUnexploredModule> REGIONS_UNEXPLORED = register("regions_unexplored", () -> RegionsUnexploredModule::new);
	public static final OptionalModule<ReliquaryModule> RELIQUARY = register("reliquary", () -> ReliquaryModule::new);
	public static final OptionalModule<UndergardenModule> UNDERGARDEN = register("undergarden", () -> UndergardenModule::new);

	private static <MODULE extends AbstractModule> OptionalModule<MODULE> register(String modid, Supplier<Supplier<MODULE>> initializer)
	{
		return register(modid, OptionalModule::new, initializer);
	}

	private static <WRAPPER extends OptionalModule<MODULE>, MODULE extends AbstractModule> WRAPPER register(String modid, BiFunction<String, Supplier<Supplier<MODULE>>, WRAPPER> wrapper, Supplier<Supplier<MODULE>> initializer)
	{
		var module = wrapper.apply(modid, initializer);
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
