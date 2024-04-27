package steve_gall.minecolonies_compatibility.core.common.config;

import net.minecraftforge.common.ForgeConfigSpec;
import steve_gall.minecolonies_compatibility.core.common.entity.ai.orchardist.OrchardistConfig;

public class JobConfig
{
	public final OrchardistConfig orchardist;

	public JobConfig(ForgeConfigSpec.Builder builder)
	{
		builder.push("orchardist");
		this.orchardist = new OrchardistConfig(builder);
		builder.pop();
	}

}
