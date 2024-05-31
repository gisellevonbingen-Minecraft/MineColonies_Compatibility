package steve_gall.minecolonies_compatibility.core.common.config;

import net.minecraftforge.common.ForgeConfigSpec;
import steve_gall.minecolonies_compatibility.core.common.entity.ai.guard.RangerConfig;
import steve_gall.minecolonies_compatibility.core.common.entity.ai.orchardist.OrchardistConfig;
import steve_gall.minecolonies_compatibility.module.common.farmersdelight.entity.ai.FarmersCookConfig;

public class JobConfig
{
	public final RangerConfig ranger;
	public final OrchardistConfig orchardist;
	public final FarmersCookConfig farmersCook;

	public JobConfig(ForgeConfigSpec.Builder builder)
	{
		builder.push("ranger");
		this.ranger = new RangerConfig(builder);
		builder.pop();

		builder.push("orchardist");
		this.orchardist = new OrchardistConfig(builder);
		builder.pop();

		builder.push("farmersCook");
		this.farmersCook = new FarmersCookConfig(builder);
		builder.pop();
	}

}
