package steve_gall.minecolonies_compatibility.core.common.config;

import net.minecraftforge.common.ForgeConfigSpec;
import steve_gall.minecolonies_compatibility.core.common.entity.ai.orchardist.OrchardistConfig;
import steve_gall.minecolonies_compatibility.module.common.farmersdelight.entity.ai.FamersCookConfig;

public class JobConfig
{
	public final OrchardistConfig orchardist;
	public final FamersCookConfig farmersCook;

	public JobConfig(ForgeConfigSpec.Builder builder)
	{
		builder.push("orchardist");
		this.orchardist = new OrchardistConfig(builder);
		builder.pop();

		builder.push("farmersCook");
		this.farmersCook = new FamersCookConfig(builder);
		builder.pop();
	}

}
