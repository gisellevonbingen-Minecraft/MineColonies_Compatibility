package steve_gall.minecolonies_compatibility.api.common.crafting;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import steve_gall.minecolonies_tweaks.api.common.crafting.ICustomizedRecipeStorage;

public interface ISecondaryRollableRecipeStorage extends ICustomizedRecipeStorage
{
	@NotNull
	default List<ItemStack> rollSecondaryOutputs(@NotNull LootContext context)
	{
		return this.getSecondaryOutputs();
	}

}
