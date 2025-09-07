package steve_gall.minecolonies_compatibility.mixin.common.minecolonies;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.minecolonies.api.inventory.container.ContainerCrafting;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.level.Level;
import steve_gall.minecolonies_compatibility.module.common.ModuleManager;
import steve_gall.minecolonies_compatibility.module.common.polymorph.PolymorphModule;

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
			PolymorphModule.sendRecipesList((ServerPlayer) this.inv.player, this.craftMatrix.asCraftInput(), this.craftResultSlot.getItem());
		}

	}

}
