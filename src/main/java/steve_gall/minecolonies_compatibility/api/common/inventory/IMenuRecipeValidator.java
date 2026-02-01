package steve_gall.minecolonies_compatibility.api.common.inventory;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.minecolonies.api.colony.requestsystem.factory.IFactoryController;

import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;

public interface IMenuRecipeValidator<RECIPE>
{
	@NotNull
	List<RECIPE> findAll(@NotNull Container container, @NotNull ServerPlayer player);

	@NotNull
	CompoundTag serialize(@NotNull IFactoryController controller, @NotNull RECIPE recipe);

	@NotNull
	RECIPE deserialize(@NotNull IFactoryController controller, @NotNull CompoundTag tag);

	@NotNull
	default ItemStack getResultItem(@NotNull RECIPE recipe, @NotNull RegistryAccess registryAccess)
	{
		return ItemStack.EMPTY;
	}

	@Nullable
	default ResourceLocation getRecipeId(@NotNull RECIPE recipe)
	{
		return null;
	}

}
