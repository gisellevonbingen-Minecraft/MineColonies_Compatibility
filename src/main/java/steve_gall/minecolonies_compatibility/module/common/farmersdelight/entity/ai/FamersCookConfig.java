package steve_gall.minecolonies_compatibility.module.common.farmersdelight.entity.ai;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.DoubleValue;

public class FamersCookConfig
{
	public final DoubleValue acceleratePerSkillLevel;

	public FamersCookConfig(ForgeConfigSpec.Builder builder)
	{
		builder.comment("Reduce cooking ticsk by (int)(skillevel *  acceleratePerSkillLevel)");
		this.acceleratePerSkillLevel = builder.defineInRange("acceleratePerSkillLevel", 0.2D, 0.1D, Integer.MAX_VALUE);
	}

}
