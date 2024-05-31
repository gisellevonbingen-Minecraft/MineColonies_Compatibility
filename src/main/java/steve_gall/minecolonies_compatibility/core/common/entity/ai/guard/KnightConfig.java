package steve_gall.minecolonies_compatibility.core.common.entity.ai.guard;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;

public class KnightConfig
{
	public final BooleanValue canUseAxe;

	public KnightConfig(ForgeConfigSpec.Builder builder)
	{
		this.canUseAxe = builder.define("canUseAxe", true);
	}

}
