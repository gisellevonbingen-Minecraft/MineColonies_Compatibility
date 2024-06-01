package steve_gall.minecolonies_compatibility.module.common.reliquary;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import steve_gall.minecolonies_compatibility.core.common.entity.ai.AttackDamageConfig;
import steve_gall.minecolonies_compatibility.core.common.entity.ai.AttackDelayConfig;
import steve_gall.minecolonies_compatibility.module.common.AbstractModuleConfig;

public class ReliquaryConfig extends AbstractModuleConfig
{
	public final JobConfig job;

	public ReliquaryConfig(ForgeConfigSpec.Builder builder)
	{
		super(builder);

		builder.push("job");
		this.job = new JobConfig(builder);
		builder.pop();
	}

	public class JobConfig
	{
		public final GunnerHandgunConfig gunnerHandgun;

		public JobConfig(ForgeConfigSpec.Builder builder)
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

			public GunnerHandgunConfig(ForgeConfigSpec.Builder builder)
			{
				builder.comment("Citizen will have reload time after every 8 shots");
				this.reloadDuration = builder.defineInRange("reloadDuration", 60, 0, 600);

				builder.push("attackDelay");
				this.attackDelay = new AttackDelayConfig(builder, new AttackDelayConfig.DefaultValues()//
						.base(60).decreasePerSkillLevel(1.0D).decreasePerBuildingLevel(0.0D));
				builder.pop();

				builder.push("defaultBulletDamage");
				this.defaultBulletDamage = new AttackDamageConfig(builder, new AttackDamageConfig.DefaultValues()//
						.base(2.0D).increasePerSkillLevel(0.2D).increasePerBuildingLevel(0.0D));
				builder.pop();
			}

		}

	}

}
