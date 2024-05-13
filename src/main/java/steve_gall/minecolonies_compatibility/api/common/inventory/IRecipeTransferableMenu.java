package steve_gall.minecolonies_compatibility.api.common.inventory;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.crafting.Recipe;

public interface IRecipeTransferableMenu<RECIPE extends Recipe<?>>
{
	void onRecipeTransfer(RECIPE recipe, CompoundTag tag);
}
