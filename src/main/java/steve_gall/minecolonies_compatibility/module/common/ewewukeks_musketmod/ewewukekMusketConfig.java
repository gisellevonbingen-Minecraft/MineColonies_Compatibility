package steve_gall.minecolonies_compatibility.module.common.ewewukeks_musketmod;

import net.neoforged.neoforge.common.ModConfigSpec;
import steve_gall.minecolonies_compatibility.core.common.entity.ai.AttackDamageConfig;
import steve_gall.minecolonies_compatibility.core.common.entity.ai.AttackDelayConfig;
import steve_gall.minecolonies_compatibility.core.common.entity.ai.guard.GunnerConfig;
import steve_gall.minecolonies_compatibility.module.common.AbstractModuleConfig;

public class ewewukekMusketConfig extends AbstractModuleConfig
{
	public final JobConfig job;

	public ewewukekMusketConfig(ModConfigSpec.Builder builder)
	{
		super(builder);

		builder.push("job");
		this.job = new JobConfig(builder);
		builder.pop();
	}

	public class JobConfig
	{
		public final GunnerGunConfig gunnerPistol;
		public final GunnerGunConfig gunnerMusket;

		public JobConfig(ModConfigSpec.Builder builder)
		{
			builder.push("gunner_pistol");
			this.gunnerPistol = new GunnerGunConfig(builder);
			builder.pop();

			builder.push("gunner_musket");
			this.gunnerMusket = new GunnerGunConfig(builder);
			builder.pop();
		}

		public class GunnerGunConfig
		{
			public static final int RELOAD_DURATION = 30 + 10;
			public static final int STAGE_DURATION_1 = 5;
			public static final int STAGE_DURATION_2 = 10;
			public static final int STAGE_DURATION_3 = 20;
			public static final int STAGE_DURATION_4 = STAGE_DURATION_3 + 10;

			public final AttackDelayConfig attackDelay;
			public final AttackDamageConfig defaultBulletDamage;

			public GunnerGunConfig(ModConfigSpec.Builder builder)
			{
				builder.push("attackDelay");
				builder.comment("will reload before every shot and reload duration is '" + GunnerGunConfig.RELOAD_DURATION + "' ticks");
				this.attackDelay = new AttackDelayConfig(builder, GunnerConfig.getDefaultDelay().base(28));
				builder.pop();

				builder.push("defaultBulletDamage");
				this.defaultBulletDamage = new AttackDamageConfig(builder, GunnerConfig.getDefaultDamage());
				builder.pop();
			}

		}

	}

}
