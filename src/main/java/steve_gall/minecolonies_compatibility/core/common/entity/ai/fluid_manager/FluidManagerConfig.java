package steve_gall.minecolonies_compatibility.core.common.entity.ai.fluid_manager;

import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.ModConfigSpec.DoubleValue;
import net.neoforged.neoforge.common.ModConfigSpec.IntValue;

public class FluidManagerConfig
{
	public final IntValue searchRange;
	public final IntValue searchVerticalRange;
	public final IntValue searchDelayAfterNotFound;

	public final IntValue pickupDelay;
	public final DoubleValue pickupDelayReducePerSkillLevel;

	public FluidManagerConfig(ModConfigSpec.Builder builder)
	{
		this.searchRange = builder.defineInRange("searchRange", 120, 0, 240);
		this.searchVerticalRange = builder.defineInRange("searchVerticalRange", 10, 0, 20);
		this.searchDelayAfterNotFound = builder.defineInRange("searchDelayAfterNotFound", 400, 0, Integer.MAX_VALUE);

		this.pickupDelay = builder.defineInRange("pickupDelay", 80, 0, Integer.MAX_VALUE);
		this.pickupDelayReducePerSkillLevel = builder.defineInRange("pickupDelayReducePerSkillLevel", 0.5D, 0.0D, Integer.MAX_VALUE);
	}

}
