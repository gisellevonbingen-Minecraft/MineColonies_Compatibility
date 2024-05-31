package steve_gall.minecolonies_compatibility.mixin.common.minecolonies;

import org.joml.Quaternionf;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.minecolonies.api.entity.ai.statemachine.tickratestatemachine.ITickRateStateMachine;
import com.minecolonies.api.entity.citizen.Skill;
import com.minecolonies.api.research.effects.IResearchEffectManager;
import com.minecolonies.api.research.util.ResearchConstants;
import com.minecolonies.api.util.InventoryUtils;
import com.minecolonies.api.util.ItemStackUtils;
import com.minecolonies.api.util.SoundUtils;
import com.minecolonies.api.util.constant.GuardConstants;
import com.minecolonies.api.util.constant.ToolType;
import com.minecolonies.core.entity.ai.combat.AttackMoveAI;
import com.minecolonies.core.entity.ai.combat.CombatUtils;
import com.minecolonies.core.entity.ai.workers.guard.RangerCombatAI;
import com.minecolonies.core.entity.citizen.EntityCitizen;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import steve_gall.minecolonies_compatibility.core.common.config.MineColoniesCompatibilityConfigServer;
import steve_gall.minecolonies_compatibility.core.common.entity.ai.CombatUtils2;
import steve_gall.minecolonies_compatibility.core.common.init.ModToolTypes;

@Mixin(value = RangerCombatAI.class, remap = false)
public abstract class RangerCombatAIMixin extends AttackMoveAI<EntityCitizen>
{
	public RangerCombatAIMixin(EntityCitizen owner, ITickRateStateMachine<?> stateMachine)
	{
		super(owner, stateMachine);
	}

	@Redirect(method = "canAttack", remap = false, at = @At(value = "FIELD", target = "com/minecolonies/api/util/constant/ToolType.BOW", opcode = Opcodes.GETSTATIC))
	private ToolType canAttack_ToolType()
	{
		return ModToolTypes.RANGER_WEAPON.getToolType();
	}

	@ModifyConstant(method = "doAttack", remap = false, constant = @Constant(intValue = 1, ordinal = 0))
	private int doAttack_amountOfArrows(int amountOfArrows)
	{
		var weapon = this.user.getItemInHand(InteractionHand.MAIN_HAND);

		if (ItemStackUtils.isTool(weapon, ModToolTypes.CROSSBOW.getToolType()))
		{
			return 0;
		}
		else
		{
			return amountOfArrows;
		}

	}

	@Redirect(method = "doAttack", remap = false, at = @At(value = "INVOKE", target = "com/minecolonies/api/research/effects/IResearchEffectManager.getEffectStrength"))
	private double doAttack_getEffectStrength_DOUBLE_ARROWS(IResearchEffectManager researchManager, ResourceLocation id)
	{
		var weapon = this.user.getItemInHand(InteractionHand.MAIN_HAND);

		if (ItemStackUtils.isTool(weapon, ModToolTypes.CROSSBOW.getToolType()))
		{
			if (id.equals(ResearchConstants.DOUBLE_ARROWS))
			{
				return 0.0D;
			}

		}

		return researchManager.getEffectStrength(id);
	}

	@Inject(method = "doAttack", remap = false, at = @At(value = "HEAD"), cancellable = true)
	private void doAttack(LivingEntity target, CallbackInfo ci)
	{
		var weapon = this.user.getItemInHand(InteractionHand.MAIN_HAND);

		if (ItemStackUtils.isTool(weapon, ModToolTypes.CROSSBOW.getToolType()))
		{
			var amountOfProjectiles = weapon.getEnchantmentLevel(Enchantments.MULTISHOT) == 0 ? 1 : 3;
			var researchEffects = this.user.getCitizenColonyHandler().getColony().getResearchManager().getResearchEffects();
			var config = MineColoniesCompatibilityConfigServer.INSTANCE.jobs.ranger;
			var ammoSlot = -1;

			if (config.canShootFireworkRocket.get().booleanValue() && researchEffects.getEffectStrength(ResearchConstants.ARCHER_USE_ARROWS) > 0.0D)
			{
				ammoSlot = InventoryUtils.findFirstSlotInItemHandlerWith(this.user.getInventoryCitizen(), item -> item.is(Items.FIREWORK_ROCKET));
			}

			var ammo = ammoSlot == -1 ? ItemStack.EMPTY : this.user.getInventoryCitizen().extractItem(ammoSlot, 1, false);
			var weaponDamage = ammoSlot == -1 ? 1 : 3;
			var projectiles = new Projectile[amountOfProjectiles];
			var chance = GuardConstants.HIT_CHANCE_DIVIDER / (this.user.getCitizenData().getCitizenSkillHandler().getLevel(Skill.Adaptability) + 1);

			for (var i = 0; i < amountOfProjectiles; i++)
			{
				var horizontalAngle = this.getShootHorizontalAngle(i);
				var upVector = this.user.getUpVector(1.0F);
				var quaternion = new Quaternionf().setAngleAxis(horizontalAngle * Mth.DEG_TO_RAD, upVector.x, upVector.y, upVector.z);

				var projectile = this.createProjectile(i, weapon, ammo, target, chance, quaternion, projectiles);
				this.user.level().addFreshEntity(projectile);
				projectiles[i] = projectile;

				this.user.playSound(SoundEvents.CROSSBOW_SHOOT, (float) GuardConstants.BASIC_VOLUME, (float) SoundUtils.getRandomPitch(this.user.getRandom()));
				this.user.getCitizenItemHandler().damageItemInHand(InteractionHand.MAIN_HAND, i == 0 ? (weaponDamage - 1) : weaponDamage);
			}

		}

	}

	private Projectile createProjectile(int i, ItemStack weapon, ItemStack ammo, LivingEntity target, float chance, Quaternionf quaternion, Projectile[] projectiles)
	{
		if (ammo.is(Items.FIREWORK_ROCKET))
		{
			var fireworkRocket = new FireworkRocketEntity(this.user.level(), ammo, this.user, this.user.getX(), this.user.getEyeY() - 0.15D, this.user.getZ(), true);
			CombatUtils2.setVector(fireworkRocket, target, chance, false, quaternion);
			return fireworkRocket;
		}
		else
		{
			var arrow = CombatUtils.createArrowForShooter(this.user);
			arrow.setShotFromCrossbow(true);

			if (i == 0)
			{
				var researchEffects = this.user.getCitizenColonyHandler().getColony().getResearchManager().getResearchEffects();
				var piercing = weapon.getEnchantmentLevel(Enchantments.PIERCING);

				if (researchEffects.getEffectStrength(ResearchConstants.ARROW_PIERCE) > 0)
				{
					piercing += 2;
				}

				if (piercing > 0)
				{
					arrow.setPierceLevel((byte) Math.min(piercing, Byte.MAX_VALUE));
				}

				arrow.setBaseDamage(this.calculateDamage(arrow));
			}
			else
			{
				var first = (AbstractArrow) projectiles[0];
				arrow.setPierceLevel(first.getPierceLevel());
				arrow.setBaseDamage(first.getBaseDamage());
			}

			CombatUtils2.setVector(arrow, target, chance, true, quaternion);
			return arrow;
		}

	}

	private float getShootHorizontalAngle(int i)
	{
		if (i == 0)
		{
			return 0.0F;
		}
		else if (i == 1)
		{
			return -10.0F;
		}
		else if (i == 2)
		{
			return +10.0F;
		}

		return 0.0F;
	}

	@Inject(method = "getAttackDelay", remap = false, at = @At(value = "RETURN"), cancellable = true)
	private void getAttackDelay(CallbackInfoReturnable<Integer> cir)
	{
		var weapon = this.user.getItemInHand(InteractionHand.MAIN_HAND);

		if (!ItemStackUtils.isTool(weapon, ModToolTypes.CROSSBOW.getToolType()))
		{
			return;
		}

		var i = weapon.getEnchantmentLevel(Enchantments.QUICK_CHARGE);

		if (i > 0)
		{
			var attackDelay = cir.getReturnValueI() - (5 * i);
			cir.setReturnValue(Math.max(attackDelay, GuardConstants.PHYSICAL_ATTACK_DELAY_MIN * 2));
		}

	}

	@Shadow(remap = false)
	abstract double calculateDamage(AbstractArrow arrow);
}
