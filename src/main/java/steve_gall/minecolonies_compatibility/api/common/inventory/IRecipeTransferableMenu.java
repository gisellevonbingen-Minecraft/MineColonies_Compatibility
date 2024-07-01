package steve_gall.minecolonies_compatibility.api.common.inventory;

import org.jetbrains.annotations.NotNull;

import net.minecraft.nbt.CompoundTag;

public interface IRecipeTransferableMenu<RECIPE>
{
	@NotNull
	IMenuRecipeValidator<RECIPE> getRecipeValidator();

	void onRecipeTransfer(@NotNull RECIPE recipe, @NotNull CompoundTag payload);
}
