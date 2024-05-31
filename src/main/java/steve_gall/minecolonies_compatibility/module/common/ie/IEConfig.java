package steve_gall.minecolonies_compatibility.module.common.ie;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.DoubleValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import steve_gall.minecolonies_compatibility.core.common.entity.ai.AttackDamageConfig;
import steve_gall.minecolonies_compatibility.core.common.entity.ai.AttackDelayConfig;
import steve_gall.minecolonies_compatibility.module.common.AbstractModuleConfig;

public class IEConfig extends AbstractModuleConfig
{
	public final JobConfig job;

	public IEConfig(ForgeConfigSpec.Builder builder)
	{
		super(builder);

		builder.push("job");
		this.job = new JobConfig(builder);
		builder.pop();
	}

	public class JobConfig
	{
		public final GunnerRevolverConfig gunnerRevolver;

		public JobConfig(ForgeConfigSpec.Builder builder)
		{
			builder.push("gunner_revolver");
			this.gunnerRevolver = new GunnerRevolverConfig(builder);
			builder.pop();
		}

		public class GunnerRevolverConfig
		{
			public final BooleanValue needReload;
			public final IntValue reloadDuration;

			public final BooleanValue occurNoise;

			public final AttackDelayConfig attackDelay;
			public final DoubleValue scopeRangeMultiplier;
			public final AttackDamageConfig defaultBulletDamage;
			public final DoubleValue defaultBulletHeadshotMultiplier;

			public GunnerRevolverConfig(ForgeConfigSpec.Builder builder)
			{
				builder.comment("Citizen will have reload time after every 8 shots");
				this.needReload = builder.define("needReload", true);
				this.reloadDuration = builder.defineInRange("reloadDuration", 60, 0, 600);

				builder.comment("Nearby monsters will come to the Citizen", "Also Warden and Sculk blocks can may listen noise");
				this.occurNoise = builder.define("occurNoise", true);

				builder.push("attackDelay");
				this.attackDelay = new AttackDelayConfig(builder, new AttackDelayConfig.DefaultValues()//
						.base(60).decreasePerSkillLevel(1.0D).decreasePerBuildingLevel(0.0D));
				builder.pop();

				builder.comment("If revolver has 'Precision Scope', search/attack range will be multiply this");
				this.scopeRangeMultiplier = builder.defineInRange("scopeRangeMultiplier", 1.25D, 1.00D, 2.50D);

				builder.push("defaultBulletDamage");
				this.defaultBulletDamage = new AttackDamageConfig(builder, new AttackDamageConfig.DefaultValues()//
						.base(0.0D).increasePerSkillLevel(0.5D).increasePerBuildingLevel(0.0D));
				this.defaultBulletHeadshotMultiplier = builder.defineInRange("headshotMultiplier", 1.5D, 1.0D, 3.0D);
				builder.pop();
			}

		}

	}

}
