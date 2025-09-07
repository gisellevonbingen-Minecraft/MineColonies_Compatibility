package steve_gall.minecolonies_compatibility.core.common.entity.ai;

import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.ModConfigSpec.IntValue;

public class SearchRangeConfig
{
	public final IntValue horizontal;
	public final IntValue vertical;

	public final IntValue verticalBonusOnGuard;

	public SearchRangeConfig(ModConfigSpec.Builder builder, int verticalBonusOnGuard)
	{
		this.horizontal = builder.defineInRange("horizontal", 16, 0, 32);
		this.vertical = builder.defineInRange("vertical", 3, 0, 32);

		this.verticalBonusOnGuard = builder.defineInRange("verticalBonusOnGuard", verticalBonusOnGuard, 0, 32);
	}

}
