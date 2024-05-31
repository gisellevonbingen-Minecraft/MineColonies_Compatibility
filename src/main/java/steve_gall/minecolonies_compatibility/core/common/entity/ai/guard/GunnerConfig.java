package steve_gall.minecolonies_compatibility.core.common.entity.ai.guard;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.EnumValue;
import steve_gall.minecolonies_compatibility.core.common.entity.ai.AttackRangeConfig;
import steve_gall.minecolonies_compatibility.core.common.entity.ai.MoveSpeedConfig;
import steve_gall.minecolonies_compatibility.core.common.entity.ai.SearchRangeConfig;

public class GunnerConfig
{
	public final EnumValue<BulletMode> bulletMode;

	public final SearchRangeConfig searchRange;
	public final AttackRangeConfig attackRange;
	public final MoveSpeedConfig combatMoveSpeed;

	public GunnerConfig(ForgeConfigSpec.Builder builder)
	{
		builder.comment(BulletMode.DONT_USE.name() + ": don't use bullet, only shot default bullet", //
				BulletMode.CAN_USE.name() + ": use bullet if citizen have bullet, else shot default bullet", //
				BulletMode.ONLY_USE.name() + ": citizen must have bullet"//
		);
		this.bulletMode = builder.defineEnum("bulletMode", BulletMode.CAN_USE);

		builder.push("searchRange");
		this.searchRange = new SearchRangeConfig(builder, 25);
		builder.pop();

		builder.push("attackRange");
		this.attackRange = new AttackRangeConfig(builder, new AttackRangeConfig.DefaultValues()//
				.base(10.0D).increasePerSkillLevel(0.3D).increasePerBuildingLevel(1.0D).maximum(24.0D)//
				.bonusOnGuard(10.0D).yDifferenceCorrection(true));
		builder.pop();

		builder.push("combatMoveSpeed");
		this.combatMoveSpeed = new MoveSpeedConfig(builder, new MoveSpeedConfig.DefaultValues()//
				.base(1.0D).increasePerSkillLevel(0.01D).increasePerBuildingLevel(0.02D));
		builder.pop();
	}

}
