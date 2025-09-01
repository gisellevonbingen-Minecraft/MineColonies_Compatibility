package steve_gall.minecolonies_compatibility.api.common.inventory;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.minecolonies.api.colony.requestsystem.factory.IFactoryController;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;

public interface IMenuRecipeValidator<RECIPE>
{
	@NotNull
	List<RECIPE> findAll(@NotNull Container container, @NotNull ServerPlayer player);

	@NotNull
	CompoundTag serialize(@NotNull IFactoryController controller, @NotNull RECIPE recipe);

	@NotNull
	RECIPE deserialize(@NotNull IFactoryController controller, @NotNull CompoundTag tag);
}
