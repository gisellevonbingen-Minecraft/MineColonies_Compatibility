package steve_gall.minecolonies_compatibility.core.common.entity.ai;

import com.minecolonies.api.entity.citizen.AbstractEntityCitizen;
import com.minecolonies.core.entity.pathfinding.navigation.MinecoloniesAdvancedPathNavigate;

import net.minecraft.util.Mth;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.DoubleValue;

public class MoveSpeedConfig
{
	public final DoubleValue base;
	public final DoubleValue increasePerSkillLevel;
	public final DoubleValue increasePerBuildingLevel;

	public MoveSpeedConfig(ForgeConfigSpec.Builder builder, DefaultValues defaultValues)
	{
		this.base = builder.defineInRange("base", defaultValues.base(), MinecoloniesAdvancedPathNavigate.MIN_SPEED_ALLOWED, MinecoloniesAdvancedPathNavigate.MAX_SPEED_ALLOWED);
		this.increasePerSkillLevel = builder.defineInRange("increasePerSkillLevel", defaultValues.increasePerSkillLevel(), 0, 24.0D);
		this.increasePerBuildingLevel = builder.defineInRange("increasePerBuildingLevel", defaultValues.increasePerBuildingLevel(), 0, 24.0D);
	}

	public double apply(AbstractEntityCitizen user, int skillLevel)
	{
		var building = user.getCitizenData().getWorkBuilding();

		var speed = this.base.get().doubleValue();
		speed += this.increasePerSkillLevel.get().doubleValue() * skillLevel;
		speed += this.increasePerBuildingLevel.get().doubleValue() * (building != null ? (building.getBuildingLevel() - 1) : 0);

		return Mth.clamp(speed, MinecoloniesAdvancedPathNavigate.MIN_SPEED_ALLOWED, MinecoloniesAdvancedPathNavigate.MAX_SPEED_ALLOWED);
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
