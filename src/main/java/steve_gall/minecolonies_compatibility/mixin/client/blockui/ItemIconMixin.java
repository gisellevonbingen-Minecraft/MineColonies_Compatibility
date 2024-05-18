package steve_gall.minecolonies_compatibility.mixin.client.blockui;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.ldtteam.blockui.controls.ItemIcon;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import steve_gall.minecolonies_compatibility.core.client.gui.ItemIconExtension;
import steve_gall.minecolonies_compatibility.module.common.ModuleManager;
import steve_gall.minecolonies_compatibility.module.common.farmersdelight.FarmersDelightModule;

@Mixin(value = ItemIcon.class, remap = false)
public abstract class ItemIconMixin implements ItemIconExtension
{
	@Unique
	private float minecolonies_compatibility$farmersChance = 1.0F;

	@Override
	public void minecolonies_compatibility$setFarmersChance(float chance)
	{
		this.minecolonies_compatibility$farmersChance = chance;
	}

	@Inject(method = "setItem", remap = false, at = @At(value = "TAIL"), cancellable = true)
	private void setItem(ItemStack stack, CallbackInfo ci)
	{
		this.minecolonies_compatibility$farmersChance = 1.0F;
	}

	@Inject(method = "getModifiedItemStackTooltip", remap = false, at = @At(value = "TAIL"), cancellable = true)
	private void getModifiedItemStackTooltip(CallbackInfoReturnable<List<Component>> cir)
	{
		if (ModuleManager.FARMERSDELIGHT.isLoaded())
		{
			if (this.minecolonies_compatibility$farmersChance != 1.0F)
			{
				cir.getReturnValue().addAll(1, FarmersDelightModule.getChanceTooltip(this.minecolonies_compatibility$farmersChance));
			}

		}

	}

}
