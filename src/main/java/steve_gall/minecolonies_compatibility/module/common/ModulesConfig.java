package steve_gall.minecolonies_compatibility.module.common;

import net.neoforged.neoforge.common.ModConfigSpec;
import steve_gall.minecolonies_compatibility.module.common.ewewukeks_musketmod.ewewukekMusketConfig;
import steve_gall.minecolonies_compatibility.module.common.ie.IEConfig;
import steve_gall.minecolonies_compatibility.module.common.reliquary.ReliquaryConfig;

public class ModulesConfig
{
	public final IEConfig IE;
	public final ReliquaryConfig reliquary;
	public final ewewukekMusketConfig ewewukekMusket;

	public ModulesConfig(ModConfigSpec.Builder builder)
	{
		builder.comment("Immersive Engineering");
		builder.push("immersiveengineering");
		this.IE = new IEConfig(builder);
		builder.pop();

		builder.comment("Reliquary Reincarnations");
		builder.push("reliquary");
		this.reliquary = new ReliquaryConfig(builder);
		builder.pop();

		builder.comment("ewewukek's Musket Mod");
		builder.push("ewewukeks_musketmod");
		this.ewewukekMusket = new ewewukekMusketConfig(builder);
		builder.pop();
	}

}
