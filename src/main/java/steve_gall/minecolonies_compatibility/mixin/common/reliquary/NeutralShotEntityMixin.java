package steve_gall.minecolonies_compatibility.mixin.common.reliquary;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import reliquary.entities.shot.NeutralShotEntity;
import reliquary.entities.shot.ShotEntityBase;
import steve_gall.minecolonies_compatibility.module.common.reliquary.GunnerHandgunAI;

@Mixin(value = NeutralShotEntity.class, remap = false)
public abstract class NeutralShotEntityMixin extends ShotEntityBase
{
	protected NeutralShotEntityMixin(EntityType<ShotEntityBase> entityType, Level world, Player player, InteractionHand hand)
	{
		super(entityType, world, player, hand);
	}

	@Inject(method = "getDamageOfShot", remap = false, at = @At(value = "HEAD"), cancellable = true)
	void getDamageOfShot(LivingEntity mop, CallbackInfoReturnable<Integer> cir)
	{
		var tag = this.getPersistentData();

		if (tag.contains(GunnerHandgunAI.TAG_DAMAGE))
		{
			cir.setReturnValue(tag.getInt(GunnerHandgunAI.TAG_DAMAGE));
		}

	}

}
