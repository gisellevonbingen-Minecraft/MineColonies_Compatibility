package steve_gall.minecolonies_compatibility.module.common;

import net.minecraftforge.common.ForgeConfigSpec;
import steve_gall.minecolonies_compatibility.module.common.ie.IEConfig;
import steve_gall.minecolonies_compatibility.module.common.reliquary.ReliquaryConfig;

public class ModulesConfig
{
	public final IEConfig IE;
	public final ReliquaryConfig reliquary;

	public ModulesConfig(ForgeConfigSpec.Builder builder)
	{
		builder.comment("Immersive Engineering");
		builder.push("immersiveengineering");
		this.IE = new IEConfig(builder);
		builder.pop();

		builder.comment("Reliquary Reincarnations");
		builder.push("reliquary");
		this.reliquary = new ReliquaryConfig(builder);
		builder.pop();
	}

}
