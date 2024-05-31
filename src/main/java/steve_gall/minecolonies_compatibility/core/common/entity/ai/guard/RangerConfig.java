package steve_gall.minecolonies_compatibility.core.common.entity.ai.guard;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;

public class RangerConfig
{
	public final BooleanValue canUseCrossbow;
	public final BooleanValue canShootFireworkRocket;

	public RangerConfig(ForgeConfigSpec.Builder builder)
	{
		this.canUseCrossbow = builder.define("canUseCrossbow", true);
		this.canShootFireworkRocket = builder.define("canShootFireworkRocket", true);
	}

}
