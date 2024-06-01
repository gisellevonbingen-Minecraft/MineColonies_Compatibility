package steve_gall.minecolonies_compatibility.core.common.entity.ai;

import com.minecolonies.api.entity.citizen.AbstractEntityCitizen;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.DoubleValue;

public class AttackDamageConfig
{
	public final DoubleValue base;
	public final DoubleValue increasePerSkillLevel;
	public final DoubleValue increasePerBuildingLevel;

	public AttackDamageConfig(ForgeConfigSpec.Builder builder, DefaultValues defaultValues)
	{
		this.base = builder.defineInRange("base", defaultValues.base(), 0.0D, Integer.MAX_VALUE);
		this.increasePerSkillLevel = builder.defineInRange("increasePerSkillLevel", defaultValues.increasePerSkillLevel(), 0.0D, Integer.MAX_VALUE);
		this.increasePerBuildingLevel = builder.defineInRange("increasePerBuildingLevel", defaultValues.increasePerBuildingLevel(), 0.0D, Integer.MAX_VALUE);
	}

	public double apply(AbstractEntityCitizen user, int skillLevel)
	{
		var building = user.getCitizenData().getWorkBuilding();

		var damage = this.base.get().doubleValue();
		damage += this.increasePerSkillLevel.get().doubleValue() * skillLevel;
		damage += this.increasePerBuildingLevel.get().doubleValue() * (building != null ? (building.getBuildingLevel() - 1) : 0);

		return damage;
	}

	public static class DefaultValues
	{
		private double base;
		private double increasePerSkillLevel;
		private double increasePerBuildingLevel;

		public DefaultValues()
		{

		}

		public double base()
		{
			return this.base;
		}

		public DefaultValues base(double base)
		{
			this.base = base;
			return this;
		}

		public double increasePerSkillLevel()
		{
			return this.increasePerSkillLevel;
		}

		public DefaultValues increasePerSkillLevel(double increasePerSkillLevel)
		{
			this.increasePerSkillLevel = increasePerSkillLevel;
			return this;
		}

		public double increasePerBuildingLevel()
		{
			return this.increasePerBuildingLevel;
		}

		public DefaultValues increasePerBuildingLevel(double increasePerBuildingLevel)
		{
			this.increasePerBuildingLevel = increasePerBuildingLevel;
			return this;
		}

	}

}
