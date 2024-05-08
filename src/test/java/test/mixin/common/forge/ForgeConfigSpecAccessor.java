package test.mixin.common.forge;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import com.electronwill.nightconfig.core.Config;

import net.minecraftforge.common.ForgeConfigSpec;

@Mixin(value = ForgeConfigSpec.class)
public interface ForgeConfigSpecAccessor
{
	@Accessor
	Config getChildConfig();
}
