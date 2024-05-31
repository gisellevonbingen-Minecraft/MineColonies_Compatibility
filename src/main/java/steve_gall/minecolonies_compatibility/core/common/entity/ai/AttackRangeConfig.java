package steve_gall.minecolonies_compatibility.core.common.entity.ai;

import com.minecolonies.api.entity.citizen.AbstractEntityCitizen;
import com.minecolonies.core.colony.buildings.modules.settings.GuardTaskSetting;

import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.DoubleValue;
import steve_gall.minecolonies_compatibility.core.common.building.BuildingHelper;

public class AttackRangeConfig
{
	public final DoubleValue base;
	public final DoubleValue increasePerSkillLevel;
	public final DoubleValue increasePerBuildingLevel;
	public final DoubleValue maximum;

	public final DoubleValue bonusOnGuard;
	public final BooleanValue yDifferenceCorrection;

	public AttackRangeConfig(ForgeConfigSpec.Builder builder, DefaultValues defaultValues)
	{
		this.base = builder.defineInRange("base", defaultValues.base(), 2.0D, 24.0D);
		this.increasePerSkillLevel = builder.defineInRange("increasePerSkillLevel", defaultValues.increasePerSkillLevel(), 0, 24.0D);
		this.increasePerBuildingLevel = builder.defineInRange("increasePerBuildingLevel", defaultValues.increasePerBuildingLevel(), 0, 24.0D);
		this.maximum = builder.defineInRange("maximum", defaultValues.maximum(), 2.0D, 48.0D);

		this.bonusOnGuard = builder.defineInRange("bonusOnGuard", defaultValues.bonusOnGuard(), 0.0D, 24.0D);
		this.yDifferenceCorrection = builder.define("yDifferenceCorrection", defaultValues.yDifferenceCorrection());
	}

	public double apply(AbstractEntityCitizen user, int skillLevel, LivingEntity target)
	{
		var building = user.getCitizenData().getWorkBuilding();

		var distance = this.base.get().doubleValue();
		distance += this.increasePerSkillLevel.get().doubleValue() * skillLevel;
		distance += this.increasePerBuildingLevel.get().doubleValue() * (building != null ? (building.getBuildingLevel() - 1) : 0);
		distance = Math.min(distance, this.maximum.get().doubleValue());

		if (this.yDifferenceCorrection.get().booleanValue() && target != null)
		{
			distance += user.getY() - target.getY();
		}

		if (BuildingHelper.IsGuardsTask(building, GuardTaskSetting.GUARD))
		{
			distance += this.bonusOnGuard.get().doubleValue();
		}

		return distance;
	}

	public static class DefaultValues
	{
		private double base;
		private double increasePerSkillLevel;
		private double increasePerBuildingLevel;
		private double maximum;

		private double bonusOnGuard;
		private boolean yDifferenceCorrection;

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

		public double maximum()
		{
			return this.maximum;
		}

		public DefaultValues maximum(double maximum)
		{
			this.maximum = maximum;
			return this;
		}

		public double bonusOnGuard()
		{
			return this.bonusOnGuard;
		}

		public DefaultValues bonusOnGuard(double bonusOnGuard)
		{
			this.bonusOnGuard = bonusOnGuard;
			return this;
		}

		public boolean yDifferenceCorrection()
		{
			return this.yDifferenceCorrection;
		}

		public DefaultValues yDifferenceCorrection(boolean yDifferenceCorrection)
		{
			this.yDifferenceCorrection = yDifferenceCorrection;
			return this;
		}

	}

}
