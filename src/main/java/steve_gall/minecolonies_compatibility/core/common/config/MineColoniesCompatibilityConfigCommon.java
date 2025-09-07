package steve_gall.minecolonies_compatibility.core.common.config;

import net.neoforged.neoforge.common.ModConfigSpec;
import steve_gall.minecolonies_compatibility.core.common.entity.ai.CommonJobConfig;

public class MineColoniesCompatibilityConfigCommon
{
	public static final MineColoniesCompatibilityConfigCommon INSTANCE;
	public static final ModConfigSpec SPEC;

	static
	{
		var common = new ModConfigSpec.Builder().configure(MineColoniesCompatibilityConfigCommon::new);
		INSTANCE = common.getLeft();
		SPEC = common.getRight();
	}

	public final CommonJobConfig jobs;

	public MineColoniesCompatibilityConfigCommon(ModConfigSpec.Builder builder)
	{
		builder.push("jobs");
		this.jobs = new CommonJobConfig(builder);
		builder.pop();
	}

}
