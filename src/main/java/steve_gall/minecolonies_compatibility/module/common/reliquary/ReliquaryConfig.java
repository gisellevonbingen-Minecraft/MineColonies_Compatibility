package steve_gall.minecolonies_compatibility.module.common.reliquary;

import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.ModConfigSpec.IntValue;
import steve_gall.minecolonies_compatibility.core.common.entity.ai.AttackDamageConfig;
import steve_gall.minecolonies_compatibility.core.common.entity.ai.AttackDelayConfig;
import steve_gall.minecolonies_compatibility.core.common.entity.ai.guard.GunnerConfig;
import steve_gall.minecolonies_compatibility.module.common.AbstractModuleConfig;

public class ReliquaryConfig extends AbstractModuleConfig
{
	public final JobConfig job;

	public ReliquaryConfig(ModConfigSpec.Builder builder)
	{
		super(builder);

		builder.push("job");
		this.job = new JobConfig(builder);
		builder.pop();
	}

	public class JobConfig
	{
		public final GunnerHandgunConfig gunnerHandgun;

		public JobConfig(ModConfigSpec.Builder builder)
		{
			builder.push("gunner_handgun");
			this.gunnerHandgun = new GunnerHandgunConfig(builder);
			builder.pop();
		}

		public class GunnerHandgunConfig
		{
			public final IntValue reloadDuration;

			public final AttackDelayConfig attackDelay;
			public final AttackDamageConfig defaultBulletDamage;

			public GunnerHandgunConfig(ModConfigSpec.Builder builder)
			{
				builder.comment("Citizen will have reload time after every 8 shots");
				this.reloadDuration = builder.defineInRange("reloadDuration", 60, 0, 600);

				builder.push("attackDelay");
				this.attackDelay = new AttackDelayConfig(builder, GunnerConfig.getDefaultDelay());
				builder.pop();

				builder.push("defaultBulletDamage");
				this.defaultBulletDamage = new AttackDamageConfig(builder, GunnerConfig.getDefaultDamage());
				builder.pop();
			}

		}

	}

}
