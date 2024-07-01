package steve_gall.minecolonies_compatibility.api.common.inventory;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import steve_gall.minecolonies_compatibility.core.common.inventory.TeachRecipeMenu;

public interface IRecipeValidator<RECIPE>
{
	@Nullable
	RECIPE test(@NotNull Level level, @NotNull ServerPlayer player, @NotNull TeachRecipeMenu<RECIPE> menu);

	@NotNull
	CompoundTag serialize(@NotNull RECIPE recipe);

	@NotNull
	RECIPE deserialize(@NotNull CompoundTag tag);
}
