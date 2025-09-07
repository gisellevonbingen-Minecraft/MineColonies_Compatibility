package steve_gall.minecolonies_compatibility.module.common.ie;

import java.util.UUID;

import javax.annotation.Nullable;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import blusunrize.immersiveengineering.api.tool.BulletHandler.CodecsAndDefault;
import blusunrize.immersiveengineering.api.tool.BulletHandler.IBullet;
import blusunrize.immersiveengineering.common.entities.RevolvershotEntity;
import blusunrize.immersiveengineering.common.util.IEDamageSources;
import malte0811.dualcodecs.DualCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.core.common.config.MineColoniesCompatibilityConfigServer;

public class DefaultBullet implements IBullet<DefaultBullet.Data>
{
	public static final CodecsAndDefault<Data> DATA_CODEC = new CodecsAndDefault<>(new DualCodec<>(Data.CODEC, Data.STREAM_CODEC), new Data(0.0D));

	public static final ResourceLocation ID = MineColoniesCompatibility.rl("ie_default");
	public static final DefaultBullet INSTANCE = new DefaultBullet();

	private final ResourceLocation[] textures;

	private DefaultBullet()
	{
		this.textures = new ResourceLocation[]{ResourceLocation.parse("immersiveengineering:item/bullet_casull")};
	}

	@Override
	public boolean isProperCartridge()
	{
		return false;
	}

	@Override
	public void onHitTarget(Level level, HitResult rtr, @Nullable UUID shooterUUID, Entity projectile, boolean headshot, Data data)
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

		var damage = data.damage();

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
	public CodecsAndDefault<Data> getCodec()
	{
		return DATA_CODEC;
	}

	public record Data(double damage)
	{
		public static final Codec<Data> CODEC = RecordCodecBuilder.create(builder -> builder.group(//
				Codec.DOUBLE.fieldOf("damage").forGetter(Data::damage) //
		).apply(builder, Data::new));

		public static final StreamCodec<RegistryFriendlyByteBuf, Data> STREAM_CODEC = StreamCodec.composite(//
				ByteBufCodecs.DOUBLE, Data::damage, //
				Data::new);

	}

}
