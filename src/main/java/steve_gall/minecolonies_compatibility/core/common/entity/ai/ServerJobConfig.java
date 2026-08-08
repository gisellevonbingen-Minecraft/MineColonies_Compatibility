package steve_gall.minecolonies_compatibility.core.common.entity.ai;

import net.neoforged.neoforge.common.ModConfigSpec;
import steve_gall.minecolonies_compatibility.core.common.entity.ai.butcher.ButcherConfig;
import steve_gall.minecolonies_compatibility.core.common.entity.ai.fluid_manager.FluidManagerConfig;
import steve_gall.minecolonies_compatibility.core.common.entity.ai.guard.GunnerConfig;
import steve_gall.minecolonies_compatibility.core.common.entity.ai.orchardist.OrchardistConfig;

public class ServerJobConfig
{
	public final GunnerConfig gunner;
	public final OrchardistConfig orchardist;
	public final FluidManagerConfig fluidManager;
	public final ButcherConfig butcher;

	public ServerJobConfig(ModConfigSpec.Builder builder)
	{
		builder.push("gunner");
		this.gunner = new GunnerConfig(builder);
		builder.pop();

		builder.push("orchardist");
		this.orchardist = new OrchardistConfig(builder);
		builder.pop();

		builder.push("fluidManager");
		this.fluidManager = new FluidManagerConfig(builder);
		builder.pop();

		builder.push("butcher");
		this.butcher = new ButcherConfig(builder);
		builder.pop();
	}

}
