package steve_gall.minecolonies_compatibility.mixin.common.minecolonies;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.minecolonies.api.util.constant.ToolType;
import com.minecolonies.core.colony.buildings.AbstractBuilding;
import com.minecolonies.core.colony.jobs.AbstractJob;
import com.minecolonies.core.entity.ai.basic.AbstractEntityAIInteract;
import com.minecolonies.core.entity.ai.citizen.herders.AbstractEntityAIHerder;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.entity.animal.Animal;
import net.minecraftforge.common.util.FakePlayer;
import steve_gall.minecolonies_compatibility.core.common.init.ModToolTypes;
import steve_gall.minecolonies_compatibility.module.common.ModuleManager;
import steve_gall.minecolonies_compatibility.module.common.butchercraft.ButchercraftModule;

@Mixin(value = AbstractEntityAIHerder.class, remap = false)
public abstract class AbstractEntityAIHerderMixin<J extends AbstractJob<?, J>, B extends AbstractBuilding> extends AbstractEntityAIInteract<J, B>
{
	public AbstractEntityAIHerderMixin(@NotNull J job)
	{
		super(job);
	}

	@Inject(method = "getExtraToolsNeeded", remap = false, at = @At(value = "TAIL"), cancellable = true)
	private void getExtraToolsNeeded(CallbackInfoReturnable<List<ToolType>> cir)
	{
		var changed = false;
		var newList = new ArrayList<ToolType>();

		for (var toolType : cir.getReturnValue())
		{
			if (toolType == ToolType.AXE)
			{
				newList.add(ModToolTypes.BUTCHER_TOOL.getToolType());
				changed = true;
			}
			else
			{
				newList.add(toolType);
			}

		}

		if (changed)
		{
			cir.setReturnValue(newList);
		}

	}

	@ModifyVariable(method = "getToolSlot", remap = false, at = @At(value = "HEAD"), ordinal = 0)
	private ToolType getToolSlot(ToolType toolType)
	{
		if (toolType == ToolType.AXE)
		{
			return ModToolTypes.BUTCHER_TOOL.getToolType();
		}

		return toolType;
	}

	@Redirect(method = "butcherAnimal", remap = false, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/Animal;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z", remap = true))
	private boolean butcherAnimal_hurt(Animal animal, DamageSource source, float damage)
	{
		if (source instanceof EntityDamageSource source2 && source2.getEntity() instanceof FakePlayer player)
		{
			var hand = InteractionHand.MAIN_HAND;
			player.setItemInHand(hand, this.worker.getItemInHand(hand).copy());

			if (ModuleManager.BUTCHERCRAFT.isLoaded())
			{
				if (ButchercraftModule.slaughter(player, animal, hand))
				{
					return true;
				}

			}

		}

		return animal.hurt(source, damage);
	}

}
