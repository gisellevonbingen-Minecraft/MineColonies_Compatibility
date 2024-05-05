package steve_gall.minecolonies_compatibility.core.common.mixin.minecolonies;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.minecolonies.api.inventory.container.ContainerCrafting;
import com.minecolonies.core.compatibility.jei.transfer.PrivateCraftingTeachingTransferHandler;

import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.transfer.IRecipeTransferError;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.core.common.module.ModuleManager;
import steve_gall.minecolonies_compatibility.core.common.network.message.CPolymorphTeachResultItemMessage;

@Mixin(value = PrivateCraftingTeachingTransferHandler.class, remap = false)
public class PrivateCraftingTeachingTransferHandlerMixin
{
	@Inject(method = "transferRecipe", at = @At(value = "TAIL"), cancellable = true)
	private void transferRecipe(@NotNull ContainerCrafting craftingGUIBuilding, @NotNull CraftingRecipe recipe, @NotNull IRecipeSlotsView recipeSlots, @NotNull Player player, boolean maxTransfer, boolean doTransfer, CallbackInfoReturnable<IRecipeTransferError> cir)
	{
		if (doTransfer && ModuleManager.POLYMORPH.isLoaded())
		{
			var output = recipeSlots.getSlotViews(RecipeIngredientRole.OUTPUT).get(0).getItemStacks().findAny().orElse(ItemStack.EMPTY);
			MineColoniesCompatibility.network().sendToServer(new CPolymorphTeachResultItemMessage(recipe.getId(), output));
		}

	}

}
