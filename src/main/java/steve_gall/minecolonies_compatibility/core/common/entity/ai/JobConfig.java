package steve_gall.minecolonies_compatibility.core.common.entity.ai;

import net.minecraftforge.common.ForgeConfigSpec;
import steve_gall.minecolonies_compatibility.core.common.entity.ai.butcher.ButcherConfig;
import steve_gall.minecolonies_compatibility.core.common.entity.ai.fluid_manager.FluidManagerConfig;
import steve_gall.minecolonies_compatibility.core.common.entity.ai.guard.GunnerConfig;
import steve_gall.minecolonies_compatibility.core.common.entity.ai.guard.KnightConfig;
import steve_gall.minecolonies_compatibility.core.common.entity.ai.guard.RangerConfig;
import steve_gall.minecolonies_compatibility.core.common.entity.ai.orchardist.OrchardistConfig;

public class JobConfig
{
	public final RangerConfig ranger;
	public final KnightConfig knight;
	public final GunnerConfig gunner;
	public final OrchardistConfig orchardist;
	public final FluidManagerConfig fluidManager;
	public final ButcherConfig butcher;

	public JobConfig(ForgeConfigSpec.Builder builder)
	{
		builder.push("ranger");
		this.ranger = new RangerConfig(builder);
		builder.pop();

		builder.push("knight");
		this.knight = new KnightConfig(builder);
		builder.pop();

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
