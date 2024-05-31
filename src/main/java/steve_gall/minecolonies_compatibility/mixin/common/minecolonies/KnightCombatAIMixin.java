package steve_gall.minecolonies_compatibility.mixin.common.minecolonies;

import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.minecolonies.api.compatibility.tinkers.TinkersToolHelper;
import com.minecolonies.api.entity.ai.statemachine.tickratestatemachine.ITickRateStateMachine;
import com.minecolonies.api.util.ItemStackUtils;
import com.minecolonies.api.util.constant.GuardConstants;
import com.minecolonies.api.util.constant.ToolType;
import com.minecolonies.core.entity.ai.combat.AttackMoveAI;
import com.minecolonies.core.entity.ai.workers.guard.KnightCombatAI;
import com.minecolonies.core.entity.citizen.EntityCitizen;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.ItemStack;
import steve_gall.minecolonies_compatibility.core.common.init.ModToolTypes;

@Mixin(value = KnightCombatAI.class, remap = false)
public abstract class KnightCombatAIMixin extends AttackMoveAI<EntityCitizen>
{
	public KnightCombatAIMixin(EntityCitizen owner, ITickRateStateMachine<?> stateMachine)
	{
		super(owner, stateMachine);
	}

	@Redirect(method = "canAttack", remap = false, at = @At(value = "FIELD", target = "com/minecolonies/api/util/constant/ToolType.SWORD", opcode = Opcodes.GETSTATIC))
	private ToolType canAttack_ToolType()
	{
		return ModToolTypes.KNIGHT_WEAPON.getToolType();
	}

	@Redirect(method = "getAttackDamage", remap = false, at = @At(value = "INVOKE", target = "com/minecolonies/api/compatibility/tinkers/TinkersToolHelper.getDamage"))
	private double getAttackDamage_getDamage(ItemStack stack)
	{
		if (ItemStackUtils.isTool(stack, ModToolTypes.KNIGHT_WEAPON.getToolType()) && stack.getItem() instanceof DiggerItem digger)
		{
			var amount = this.getAdditionsAmount(digger, Attributes.ATTACK_DAMAGE);
			return amount + GuardConstants.BASE_PHYSICAL_DAMAGE;
		}
		else
		{
			return TinkersToolHelper.getDamage(stack);
		}

	}

	@ModifyConstant(method = "getAttackDelay", remap = false, constant = @Constant(intValue = 32))
	private int modifyDelay(int KNIGHT_ATTACK_DELAY_BASE)
	{
		var stack = user.getItemInHand(InteractionHand.MAIN_HAND);

		if (ItemStackUtils.isTool(stack, ModToolTypes.KNIGHT_WEAPON.getToolType()) && stack.getItem() instanceof DiggerItem digger)
		{
			var base = Attributes.ATTACK_SPEED.getDefaultValue();
			var amount = this.getAdditionsAmount(digger, Attributes.ATTACK_SPEED);
			return (int) (KNIGHT_ATTACK_DELAY_BASE * ((base - 2.4D) / (base - amount)));

		}
		else
		{
			return KNIGHT_ATTACK_DELAY_BASE;
		}

	}

	private double getAdditionsAmount(DiggerItem digger, Attribute attribute)
	{
		var amount = 0.0D;
		var modifiers = digger.getDefaultAttributeModifiers(EquipmentSlot.MAINHAND).get(attribute);

		for (var modifier : modifiers)
		{
			if (modifier.getOperation() == AttributeModifier.Operation.ADDITION)
			{
				amount += modifier.getAmount();
			}

		}

		return amount;
	}

}
