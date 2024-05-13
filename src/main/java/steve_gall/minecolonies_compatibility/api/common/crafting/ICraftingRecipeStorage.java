package steve_gall.minecolonies_compatibility.api.common.crafting;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootParams;
import steve_gall.minecolonies_tweaks.api.common.crafting.ICustomizedRecipeStorage;

public interface ICraftingRecipeStorage extends ICustomizedRecipeStorage
{
	@NotNull
	default List<ItemStack> rollSecondaryOutputs(@NotNull LootParams context)
	{
		return this.getSecondaryOutputs();
	}

}
