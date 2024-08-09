package steve_gall.minecolonies_compatibility.mixin.common;

import com.google.common.collect.ImmutableMap;
import net.minecraftforge.fml.loading.FMLLoader;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import steve_gall.minecolonies_compatibility.module.common.ModuleManager;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

public class MineColoniesCompatibilityMixinPlugin implements IMixinConfigPlugin
{
    private static boolean isModPresent(String id)
    {
        return FMLLoader.getLoadingModList().getModFileById(id) != null;
    }

    private static final Supplier<Boolean> TRUE = () -> true;

    private static final Map<String, Supplier<Boolean>> CONDITIONS = ImmutableMap.of(
            "steve_gall.minecolonies_compatibility.mixin.common.blue_skies.BrewberryBushBlockAccessor", () -> isModPresent(ModuleManager.BLUE_SKIES.getModId()),
            "steve_gall.minecolonies_compatibility.mixin.common.cobblemon.BerryBlockAccessor", () -> isModPresent(ModuleManager.COBBLEMON.getModId()),
            "steve_gall.minecolonies_compatibility.mixin.common.cyclic.AppleCropBlockAccessor", () -> isModPresent(ModuleManager.CYCLIC.getModId()),
            "steve_gall.minecolonies_compatibility.mixin.common.oreberries.OreBerryBushBlockAccessor", () -> isModPresent(ModuleManager.OREBERRIES.getModId()),
            "steve_gall.minecolonies_compatibility.mixin.common.reliquary.HandgunItemAccessor", () -> isModPresent(ModuleManager.RELIQUARY.getModId()),
            "steve_gall.minecolonies_compatibility.mixin.common.reliquary.NeutralShotEntityMixin", () -> isModPresent(ModuleManager.RELIQUARY.getModId()),
            "steve_gall.minecolonies_compatibility.mixin.common.storagenetwork.NetworkModuleAccessor", () -> isModPresent(ModuleManager.STORAGE_NETWORK.getModId()),
            "steve_gall.minecolonies_compatibility.mixin.common.storagenetwork.UtilConnectionsMixin", () -> isModPresent(ModuleManager.STORAGE_NETWORK.getModId()),
            "steve_gall.minecolonies_compatibility.mixin.common.thermal.CropBlockCoFHAccessor", () -> isModPresent(ModuleManager.THERMAL.getModId())
    );

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName)
    {
        return CONDITIONS.getOrDefault(mixinClassName, TRUE).get();
    }

    // Unused

    @Override
    public void onLoad(String mixinPackage)
    {

    }

    @Override
    public String getRefMapperConfig()
    {
        return "";
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets)
    {

    }

    @Override
    public List<String> getMixins()
    {
        return List.of();
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo)
    {

    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo)
    {

    }
}
