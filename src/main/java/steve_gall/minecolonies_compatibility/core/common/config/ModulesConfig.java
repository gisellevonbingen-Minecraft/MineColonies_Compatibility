package steve_gall.minecolonies_compatibility.core.common.config;

import net.minecraftforge.common.ForgeConfigSpec;
import steve_gall.minecolonies_compatibility.module.common.ie.IEConfig;

public class ModulesConfig
{
	public final IEConfig IE;

	public ModulesConfig(ForgeConfigSpec.Builder builder)
	{
		builder.comment("Immersive Engineering");
		builder.push("immersiveengineering");
		this.IE = new IEConfig(builder);
		builder.pop();
	}

}
