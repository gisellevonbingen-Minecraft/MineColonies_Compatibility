package steve_gall.minecolonies_compatibility.api.common.inventory;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.minecolonies.api.colony.requestsystem.factory.IFactoryController;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;

public interface IMenuRecipeValidator<RECIPE, RECIPE_INPUT>
{
	@NotNull
	RECIPE_INPUT getInput(@NotNull Container container, @Nullable RECIPE recipe);

	@NotNull
	List<RECIPE> findAll(@NotNull Container container, @NotNull ServerPlayer player);

	@NotNull
	CompoundTag serialize(@NotNull HolderLookup.Provider provider, @NotNull IFactoryController controller, @NotNull RECIPE recipe);

	@NotNull
	RECIPE deserialize(@NotNull HolderLookup.Provider provider, @NotNull IFactoryController controller, @NotNull CompoundTag tag);

	@NotNull
	default ItemStack getResultItem(@NotNull RECIPE recipe, @NotNull HolderLookup.Provider provider)
	{
		return ItemStack.EMPTY;
	}

	@Nullable
	default ResourceLocation getRecipeId(@NotNull RECIPE recipe)
	{
		return null;
	}

}
