package steve_gall.minecolonies_compatibility.mixin.common.minecolonies;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.minecolonies.api.entity.ai.statemachine.tickratestatemachine.ITickRateStateMachine;
import com.minecolonies.api.equipment.ModEquipmentTypes;
import com.minecolonies.api.equipment.registry.EquipmentTypeEntry;
import com.minecolonies.api.util.constant.GuardConstants;
import com.minecolonies.core.entity.ai.combat.AttackMoveAI;
import com.minecolonies.core.entity.ai.workers.guard.KnightCombatAI;
import com.minecolonies.core.entity.citizen.EntityCitizen;

import net.minecraft.core.Holder;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.neoforged.neoforge.items.IItemHandler;
import steve_gall.minecolonies_compatibility.core.common.init.ModToolTypes;

@Mixin(value = KnightCombatAI.class, remap = false)
public abstract class KnightCombatAIMixin extends AttackMoveAI<EntityCitizen>
{
	public KnightCombatAIMixin(EntityCitizen owner, ITickRateStateMachine<?> stateMachine)
	{
		super(owner, stateMachine);
	}

	@WrapOperation(method = "canAttack", remap = false, at = @At(value = "INVOKE", target = "com/minecolonies/api/util/InventoryUtils.getFirstSlotOfItemHandlerContainingEquipment", remap = false))
	private int canAttack_getFirstSlotOfItemHandlerContainingEquipment(IItemHandler itemHandler, EquipmentTypeEntry equipmentType, int minimalLevel, int maximumLevel, Operation<Integer> operation)
	{
		if (equipmentType == ModEquipmentTypes.sword.get())
		{
			equipmentType = ModToolTypes.KNIGHT_WEAPON.getToolType();
		}

		return operation.call(itemHandler, equipmentType, minimalLevel, maximumLevel);
	}

	@Redirect(method = "getAttackDamage", remap = false, at = @At(value = "INVOKE", target = "com/minecolonies/api/compatibility/tinkers/TinkersToolHelper.getDamage", remap = false))
	private double getAttackDamage_getDamage(ItemStack stack)
	{
		if (stack.getItem() instanceof DiggerItem)
		{
			return this.user.getAttribute(Attributes.ATTACK_DAMAGE).getValue() * this.getSpeedFactor(stack);
		}
		else
		{
			return this.compute(stack, EquipmentSlotGroup.MAINHAND, EquipmentSlot.MAINHAND, Attributes.ATTACK_DAMAGE, GuardConstants.BASE_PHYSICAL_DAMAGE);
		}

	}

	@ModifyConstant(method = "getAttackDelay", remap = false, constant = @Constant(intValue = 32))
	private int modifyDelay(int KNIGHT_ATTACK_DELAY_BASE)
	{
		var stack = this.user.getItemInHand(InteractionHand.MAIN_HAND);
		return (int) (KNIGHT_ATTACK_DELAY_BASE * this.getSpeedFactor(stack));
	}

	private double getSpeedFactor(ItemStack stack)
	{
		var speed = this.compute(stack, EquipmentSlotGroup.MAINHAND, EquipmentSlot.MAINHAND, Attributes.ATTACK_SPEED, Attributes.ATTACK_SPEED.value().getDefaultValue());
		return 1.6D / speed;
	}

	private double compute(ItemStack stack, EquipmentSlotGroup group, EquipmentSlot slot, Holder<Attribute> attribute, double base)
	{
		var builder = ItemAttributeModifiers.builder();
		stack.forEachModifier(group, (holder, modifier) ->
		{
			if (holder == attribute)
			{
				builder.add(holder, modifier, group);
			}
		});
		return builder.build().compute(base, slot);
	}

}
