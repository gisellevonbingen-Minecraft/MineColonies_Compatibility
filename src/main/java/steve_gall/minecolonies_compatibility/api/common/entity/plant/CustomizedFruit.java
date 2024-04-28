package steve_gall.minecolonies_compatibility.api.common.entity.plant;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public abstract class CustomizedFruit
{
	private static final List<CustomizedFruit> REGISTRY = new ArrayList<>();

	public static void register(@NotNull CustomizedFruit fruit)
	{
		REGISTRY.add(fruit);
	}

	@Nullable
	public static CustomizedFruit select(@NotNull BlockState state)
	{
		return REGISTRY.stream().filter(it -> it.test(state)).findFirst().orElse(null);
	}

	public abstract boolean test(@NotNull BlockState state);

	public abstract boolean canHarvest(@NotNull BlockState state);

	@NotNull
	public abstract List<ItemStack> harvest(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos position);
}
