package steve_gall.minecolonies_compatibility.module.common;

import net.minecraftforge.common.ForgeConfigSpec;
import steve_gall.minecolonies_compatibility.module.common.ae2.AE2Config;
import steve_gall.minecolonies_compatibility.module.common.ewewukeks_musketmod.ewewukekMusketConfig;
import steve_gall.minecolonies_compatibility.module.common.ie.IEConfig;
import steve_gall.minecolonies_compatibility.module.common.refinedstorage.RSConfig;
import steve_gall.minecolonies_compatibility.module.common.reliquary.ReliquaryConfig;

public class ModulesConfig
{
	public final AE2Config AE2;
	public final IEConfig IE;
	public final ReliquaryConfig reliquary;
	public final RSConfig RS;
	public final ewewukekMusketConfig ewewukekMusket;

	public ModulesConfig(ForgeConfigSpec.Builder builder)
	{
		builder.comment("Applied Energistics 2");
		builder.push("appliedenergistics2");
		this.AE2 = new AE2Config(builder);
		builder.pop();

		builder.comment("Immersive Engineering");
		builder.push("immersiveengineering");
		this.IE = new IEConfig(builder);
		builder.pop();

		builder.comment("Reliquary Reincarnations");
		builder.push("reliquary");
		this.reliquary = new ReliquaryConfig(builder);
		builder.pop();

		builder.comment("Refined Storage");
		builder.push("refinedstorage");
		this.RS = new RSConfig(builder);
		builder.pop();

		builder.comment("ewewukek's Musket Mod");
		builder.push("ewewukeks_musketmod");
		this.ewewukekMusket = new ewewukekMusketConfig(builder);
		builder.pop();
	}

}
