package steve_gall.minecolonies_compatibility.core.common.entity.ai.orchardist;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.DoubleValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;

public class OrchardistConfig
{
	public final IntValue searchRangeStart;
	public final IntValue searchRangeLimit;
	public final IntValue searchRangeIncrement;
	public final IntValue searchVerticalRange;

	public final IntValue searchdelayBeforeIncrement;
	public final IntValue searchDelayAfterNotFound;
	public final IntValue harvestDelay;
	public final DoubleValue harvestDelayReducePerSkillLevel;
	public final DoubleValue moveSpeedBonusPerSkillLevel;

	public final IntValue actionsDoneUntilDumping;

	public OrchardistConfig(ForgeConfigSpec.Builder builder)
	{
		this.searchRangeStart = builder.defineInRange("searchRangeStart", 10, 0, 120);
		this.searchRangeLimit = builder.defineInRange("searchRangeLimit", 120, 0, 120);
		this.searchRangeIncrement = builder.defineInRange("searchRangeIncrement", 55, 0, 120);
		this.searchVerticalRange = builder.defineInRange("searchVerticalRange", 10, 0, 20);

		this.searchdelayBeforeIncrement = builder.defineInRange("searchdelayBeforeIncrement", 20, 0, Integer.MAX_VALUE);
		this.searchDelayAfterNotFound = builder.defineInRange("searchDelayAfterNotFound", 400, 0, Integer.MAX_VALUE);
		this.harvestDelay = builder.defineInRange("harvestDelay", 40, 0, Integer.MAX_VALUE);
		this.harvestDelayReducePerSkillLevel = builder.defineInRange("harvestDelayReducePerSkillLevel", 0.5D, 0.0D, Integer.MAX_VALUE);
		this.moveSpeedBonusPerSkillLevel = builder.defineInRange("moveSpeedBonusPerSkillLevel", 0.0015D, 0.0D, Integer.MAX_VALUE);

		this.actionsDoneUntilDumping = builder.defineInRange("actionsDoneUntilDumping", 128, 64, Integer.MAX_VALUE);
	}

}
