package steve_gall.minecolonies_compatibility.core.common.entity.ai.guard;

import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.ModConfigSpec.BooleanValue;

public class RangerConfig
{
	public final BooleanValue canShootFireworkRocket;

	public RangerConfig(ModConfigSpec.Builder builder)
	{
		this.canShootFireworkRocket = builder.define("canShootFireworkRocket", true);
	}

}
