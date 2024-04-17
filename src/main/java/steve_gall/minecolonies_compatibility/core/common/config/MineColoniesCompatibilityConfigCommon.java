package steve_gall.minecolonies_compatibility.core.common.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class MineColoniesCompatibilityConfigCommon
{
	public static final MineColoniesCompatibilityConfigCommon INSTANCE;
	public static final ForgeConfigSpec SPEC;

	static
	{
		var common = new ForgeConfigSpec.Builder().configure(MineColoniesCompatibilityConfigCommon::new);
		INSTANCE = common.getLeft();
		SPEC = common.getRight();
	}

	public MineColoniesCompatibilityConfigCommon(ForgeConfigSpec.Builder builder)
	{

	}

}
