package steve_gall.minecolonies_compatibility.core.common.entity.ai;

import org.joml.Quaternionf;
import org.joml.Vector3f;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;

public class CombatUtils2
{
	public static final double AIM_HEIGHT = 2.0D;
	public static final double ARROW_SPEED = 1.4D;
	public static final double AIM_SLIGHTLY_HIGHER_MULTIPLIER = 0.18;
	public static final double SPEED_FOR_DIST = 35;

	public static void setVector(Projectile projectile, LivingEntity target, float hitChance, boolean aimHigher, Quaternionf quaternion)
	{
		var xVector = target.getX() - projectile.getX();
		var yVector = (target.getBoundingBox().minY + target.getBbHeight() / AIM_HEIGHT) - projectile.getY();
		var zVector = target.getZ() - projectile.getZ();
		var distance = Mth.sqrt((float) (xVector * xVector + zVector * zVector));
		var dist3d = Mth.sqrt((float) (yVector * yVector + xVector * xVector + zVector * zVector));

		var vector = new Vector3f((float) xVector, (float) yVector, (float) zVector);
		vector.rotate(quaternion);

		projectile.shoot(vector.x(), vector.y() + (aimHigher ? distance * AIM_SLIGHTLY_HIGHER_MULTIPLIER : 0.0D), vector.z(), (float) (ARROW_SPEED * 1 + (dist3d / SPEED_FOR_DIST)), hitChance);
	}

	private CombatUtils2()
	{

	}

}
