package steve_gall.minecolonies_compatibility.module.common.ae2;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.common.ForgeConfigSpec.LongValue;
import steve_gall.minecolonies_compatibility.module.common.AbstractModuleConfig;

public class AE2Config extends AbstractModuleConfig
{
	public final LongValue citizenTerminal_calculationTimeoutTicks;
	public final IntValue citizenTerminal_linkNoProgressChecks;

	public AE2Config(ForgeConfigSpec.Builder builder)
	{
		super(builder);

		builder.comment("Citizen Terminal");
		builder.push("citizenTerminal");
		this.citizenTerminal_calculationTimeoutTicks = builder.comment("Ticks allowed for AE2 crafting calculation to complete before being cancelled (20 ticks = 1 second, default = 400 = 20s)").defineInRange("calculationTimeoutTicks", 400L, 20L, Long.MAX_VALUE);
		this.citizenTerminal_linkNoProgressChecks = builder.comment("Number of consecutive 5-tick checks with no crafting progress before the job is cancelled (default = 1200 ≈ 5 minutes)").defineInRange("linkNoProgressChecks", 1200, 1, Integer.MAX_VALUE);
		builder.pop();
	}

}
