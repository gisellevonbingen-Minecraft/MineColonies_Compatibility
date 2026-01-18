package steve_gall.minecolonies_compatibility.module.common.refinedstorage;

import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.ModConfigSpec.IntValue;
import steve_gall.minecolonies_compatibility.module.common.AbstractModuleConfig;

public class RSConfig extends AbstractModuleConfig
{
	public final IntValue citizen_grid_energyUsage;

	public RSConfig(ModConfigSpec.Builder builder)
	{
		super(builder);

		builder.comment("Citizen Grid");
		builder.push("citizenGrid");
		this.citizen_grid_energyUsage = builder.defineInRange("energyUsage", 1, 0, Integer.MAX_VALUE);
		builder.pop();
	}

}
