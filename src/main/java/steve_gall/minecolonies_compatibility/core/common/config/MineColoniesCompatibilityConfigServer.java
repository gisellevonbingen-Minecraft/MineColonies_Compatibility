package steve_gall.minecolonies_compatibility.core.common.config;

import net.neoforged.neoforge.common.ModConfigSpec;
import steve_gall.minecolonies_compatibility.core.common.entity.ai.ServerJobConfig;
import steve_gall.minecolonies_compatibility.module.common.ModulesConfig;

public class MineColoniesCompatibilityConfigServer
{
	public static final MineColoniesCompatibilityConfigServer INSTANCE;
	public static final ModConfigSpec SPEC;

	static
	{
		var common = new ModConfigSpec.Builder().configure(MineColoniesCompatibilityConfigServer::new);
		INSTANCE = common.getLeft();
		SPEC = common.getRight();
	}

	public final ServerJobConfig jobs;
	public final ModulesConfig modules;

	public MineColoniesCompatibilityConfigServer(ModConfigSpec.Builder builder)
	{
		builder.push("jobs");
		this.jobs = new ServerJobConfig(builder);
		builder.pop();

		builder.push("modules");
		this.modules = new ModulesConfig(builder);
		builder.pop();
	}

}
