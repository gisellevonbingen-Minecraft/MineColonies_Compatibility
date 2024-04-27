package steve_gall.minecolonies_compatibility.api.common.entity.plant;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public abstract class CustomizedCrop
{
	private static final List<CustomizedCrop> REGISTRY = new ArrayList<>();

	public static void register(@NotNull CustomizedCrop crop)
	{
		REGISTRY.add(crop);
	}

	@Nullable
	public static CustomizedCrop selectBySeed(@NotNull ItemStack seedStack)
	{
		return REGISTRY.stream().filter(it -> it.isSeed(seedStack)).findFirst().orElse(null);
	}

	@Nullable
	public static CustomizedCrop selectByCrop(@NotNull BlockState cropState)
	{
		return REGISTRY.stream().filter(it -> it.isCrop(cropState)).findFirst().orElse(null);
	}

	@Nullable
	public abstract BlockState getPlantState(@NotNull ItemStack seedStack, @NotNull Level level, @NotNull BlockPos plantPosition);

	public abstract boolean isSeed(@NotNull ItemStack seedStack);

	public abstract boolean isCrop(@NotNull BlockState cropState);

	public boolean hasSpecialHarvestPosition(@NotNull BlockState cropState, @NotNull Level level, @NotNull BlockPos plantPosition)
	{
		return false;
	}

	@Nullable
	public BlockPos getSpecialHarvestPosition(@NotNull BlockState cropState, @NotNull Level level, @NotNull BlockPos plantPosition)
	{
		return null;
	}

}
