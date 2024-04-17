package steve_gall.minecolonies_compatibility.core.common.ie;

import java.util.UUID;

import blusunrize.immersiveengineering.api.tool.BulletHandler.IBullet;
import blusunrize.immersiveengineering.common.entities.RevolvershotEntity;
import blusunrize.immersiveengineering.common.util.IEDamageSources;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.core.common.config.MineColoniesCompatibilityConfigServer;
import steve_gall.minecolonies_compatibility.core.common.util.PersistentDataHelper;

public class DefaultBullet implements IBullet
{
	public static final ResourceLocation ID = MineColoniesCompatibility.rl("ie_default");
	public static final DefaultBullet INSTANCE = new DefaultBullet();

	public static final String TAG_KEY = MineColoniesCompatibility.rl("bullet/ie_default").toString();

	private final ResourceLocation[] textures;

	private DefaultBullet()
	{
		this.textures = new ResourceLocation[]{new ResourceLocation("immersiveengineering:item/bullet_casull")};
	}

	@Override
	public boolean isProperCartridge()
	{
		return false;
	}

	@Override
	public Entity getProjectile(Player shooter, ItemStack cartridge, Entity projectile, boolean charged)
	{
		var payload = PersistentDataHelper.getOrEmpty(cartridge, TAG_KEY);
		projectile.getPersistentData().put(TAG_KEY, payload);
		return projectile;
	}

	public static void putDamage(ItemStack cartridge, double damage)
	{
		PersistentDataHelper.getOrCreate(cartridge, TAG_KEY).putDouble("Damage", damage);
	}

	@Override
	public void onHitTarget(Level level, HitResult rtr, UUID shooterUUID, Entity projectile, boolean headshot)
	{
		if (level.isClientSide())
		{
			return;
		}

		Entity hitEntity = null;

		if (rtr instanceof EntityHitResult target)
		{
			hitEntity = target.getEntity();

			if (hitEntity == null)
			{
				return;
			}

		}
		else
		{
			return;
		}

		Entity shooter = null;

		if (shooterUUID != null && level instanceof ServerLevel serverLevel)
		{
			shooter = serverLevel.getEntity(shooterUUID);
		}

		var damage = PersistentDataHelper.getOrEmpty(projectile, TAG_KEY).getDouble("Damage");

		if (headshot)
		{
			damage *= MineColoniesCompatibilityConfigServer.INSTANCE.modules.IE.job.gunnerRevolver.defaultBulletHeadshotMultiplier.get().doubleValue();
		}

		hitEntity.hurt(IEDamageSources.causeHomingDamage((RevolvershotEntity) projectile, shooter), (float) damage);
	}

	@Override
	public ItemStack getCasing(ItemStack stack)
	{
		return ItemStack.EMPTY;
	}

	@Override
	public ResourceLocation[] getTextures()
	{
		return this.textures;
	}

	@Override
	public int getColour(ItemStack stack, int layer)
	{
		return 0xFFFFFFFF;
	}

}
