package steve_gall.minecolonies_compatibility.api.common.inventory;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;

public interface IMenuRecipeValidator<RECIPE>
{
	@Nullable
	RECIPE find(@NotNull ServerPlayer player, @NotNull Container container);

	@NotNull
	CompoundTag serialize(@NotNull RECIPE recipe);

	@NotNull
	RECIPE deserialize(@NotNull CompoundTag tag);
}
