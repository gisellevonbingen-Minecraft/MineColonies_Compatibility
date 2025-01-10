package steve_gall.minecolonies_compatibility.core.common.entity.ai;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;

public class CommonJobConfig
{
	public final BooleanValue canUseCrossbow;
	public final BooleanValue canUseAxe;

	public CommonJobConfig(ForgeConfigSpec.Builder builder)
	{
		builder.push("ranger");
		this.canUseCrossbow = builder.define("canUseCrossbow", true);
		builder.pop();

		builder.push("knight");
		this.canUseAxe = builder.define("canUseAxe", true);
		builder.pop();
	}

}
