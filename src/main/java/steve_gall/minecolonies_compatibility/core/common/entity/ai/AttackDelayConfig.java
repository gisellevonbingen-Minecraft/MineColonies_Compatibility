package steve_gall.minecolonies_compatibility.core.common.entity.ai;

import com.minecolonies.api.entity.citizen.AbstractEntityCitizen;
import com.minecolonies.api.util.constant.GuardConstants;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.DoubleValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;

public class AttackDelayConfig
{
	public final IntValue base;
	public final DoubleValue decreasePerSkillLevel;
	public final DoubleValue decreasePerBuildingLevel;

	public AttackDelayConfig(ForgeConfigSpec.Builder builder, DefaultValues defaultValues)
	{
		this.base = builder.defineInRange("base", defaultValues.base(), GuardConstants.PHYSICAL_ATTACK_DELAY_MIN, 200);
		this.decreasePerSkillLevel = builder.defineInRange("decreasePerSkillLevel", defaultValues.decreasePerSkillLevel(), 0.0D, 200.0D);
		this.decreasePerBuildingLevel = builder.defineInRange("decreasePerBuildingLevel", defaultValues.decreasePerBuildingLevel(), 0.0D, 200.0D);
	}

	public int apply(AbstractEntityCitizen user, int skillLevel)
	{
		var building = user.getCitizenData().getWorkBuilding();

		var delay = this.base.get().doubleValue();
		delay -= this.decreasePerSkillLevel.get().doubleValue() * skillLevel;
		delay -= this.decreasePerBuildingLevel.get().doubleValue() * (building != null ? (building.getBuildingLevel() - 1) : 0);

		return (int) delay;
	}

	public static class DefaultValues
	{
		private int base;
		private double decreasePerSkillLevel;
		private double decreasePerBuildingLevel;

		public DefaultValues()
		{

		}

		public int base()
		{
			return this.base;
		}

		public DefaultValues base(int base)
		{
			this.base = base;
			return this;
		}

		public double decreasePerSkillLevel()
		{
			return this.decreasePerSkillLevel;
		}

		public DefaultValues decreasePerSkillLevel(double decreasePerSkillLevel)
		{
			this.decreasePerSkillLevel = decreasePerSkillLevel;
			return this;
		}

		public double decreasePerBuildingLevel()
		{
			return this.decreasePerBuildingLevel;
		}

		public DefaultValues decreasePerBuildingLevel(double decreasePerBuildingLevel)
		{
			this.decreasePerBuildingLevel = decreasePerBuildingLevel;
			return this;
		}

	}

}
