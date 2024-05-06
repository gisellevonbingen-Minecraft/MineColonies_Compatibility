package steve_gall.minecolonies_compatibility.core.common.mixin.minecolonies;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.minecolonies.api.inventory.container.ContainerCrafting;
import com.minecolonies.api.util.Tuple;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import steve_gall.minecolonies_compatibility.core.common.module.ModuleManager;
import steve_gall.minecolonies_compatibility.core.common.module.polymorph.PolymorphModule;

@Mixin(value = ContainerCrafting.class, remap = false)
public abstract class ContainerCraftingMixin extends AbstractContainerMenu
{
	@Shadow(remap = false)
	private Level world;
	@Shadow(remap = false)
	private Inventory inv;
	@Shadow(remap = false)
	private CraftingContainer craftMatrix;
	@Shadow(remap = false)
	private Slot craftResultSlot;

	protected ContainerCraftingMixin(MenuType<?> p_38851_, int p_38852_)
	{
		super(p_38851_, p_38852_);
	}

	@Inject(method = "slotsChanged", remap = true, at = @At(value = "TAIL"), cancellable = true)
	private void slotsChanged(Container inventoryIn, CallbackInfo ci)
	{
		if (!this.world.isClientSide && ModuleManager.POLYMORPH.isLoaded())
		{
			var notlimited = !this.world.getGameRules().getBoolean(GameRules.RULE_LIMITED_CRAFTING);
			var player = (ServerPlayer) this.inv.player;
			var tuples = this.world.getRecipeManager().getRecipesFor(RecipeType.CRAFTING, this.craftMatrix, this.world).stream().filter(//
					recipe -> recipe.isSpecial() || (notlimited || player.getRecipeBook().contains(recipe) || player.isCreative())//
			).map(recipe -> new Tuple<>(recipe, recipe.assemble(this.craftMatrix))).toList();
			var output = this.craftResultSlot.getItem();
			PolymorphModule.sendRecipesList(player, tuples, output);
		}

	}

}
