package steve_gall.minecolonies_compatibility.core.common.entity.ai.butcher;

import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.ModConfigSpec.DoubleValue;
import net.neoforged.neoforge.common.ModConfigSpec.IntValue;

public class ButcherConfig
{
	public final IntValue searchDelayAfterNotFound;

	public final IntValue workDelay;
	public final DoubleValue workDelayReducePerSkillLevel;

	public ButcherConfig(ModConfigSpec.Builder builder)
	{
		this.searchDelayAfterNotFound = builder.defineInRange("searchDelayAfterNotFound", 400, 0, Integer.MAX_VALUE);

		this.workDelay = builder.defineInRange("harvestDelay", 80, 0, Integer.MAX_VALUE);
		this.workDelayReducePerSkillLevel = builder.defineInRange("workDelayReducePerSkillLevel", 0.5D, 0.0D, Integer.MAX_VALUE);
	}

}
