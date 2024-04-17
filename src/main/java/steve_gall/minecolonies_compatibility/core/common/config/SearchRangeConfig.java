package steve_gall.minecolonies_compatibility.core.common.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;

public class SearchRangeConfig
{
	public final IntValue horizontal;
	public final IntValue vertical;

	public final IntValue verticalBonusOnGuard;

	public SearchRangeConfig(ForgeConfigSpec.Builder builder, int verticalBonusOnGuard)
	{
		this.horizontal = builder.defineInRange("horizontal", 16, 0, 32);
		this.vertical = builder.defineInRange("vertical", 3, 0, 32);

		this.verticalBonusOnGuard = builder.defineInRange("verticalBonusOnGuard", verticalBonusOnGuard, 0, 32);
	}

}
