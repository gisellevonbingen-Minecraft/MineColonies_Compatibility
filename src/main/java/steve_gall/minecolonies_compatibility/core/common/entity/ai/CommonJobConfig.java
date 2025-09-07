package steve_gall.minecolonies_compatibility.core.common.entity.ai;

import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.ModConfigSpec.BooleanValue;

public class CommonJobConfig
{
	public final BooleanValue canUseCrossbow;
	public final BooleanValue canUseAxe;

	public CommonJobConfig(ModConfigSpec.Builder builder)
	{
		builder.push("ranger");
		this.canUseCrossbow = builder.define("canUseCrossbow", true);
		builder.pop();

		builder.push("knight");
		this.canUseAxe = builder.define("canUseAxe", true);
		builder.pop();
	}

}
