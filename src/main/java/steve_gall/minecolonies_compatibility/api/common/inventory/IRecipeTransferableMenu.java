package steve_gall.minecolonies_compatibility.api.common.inventory;

import org.jetbrains.annotations.NotNull;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Inventory;

public interface IRecipeTransferableMenu<RECIPE>
{
	@NotNull
	Inventory getInventory();

	@NotNull
	IMenuRecipeValidator<RECIPE> getRecipeValidator();

	void onRecipeTransfer(@NotNull RECIPE recipe, @NotNull CompoundTag payload);
}
