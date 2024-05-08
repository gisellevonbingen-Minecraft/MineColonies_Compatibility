package steve_gall.minecolonies_compatibility.api.common.entity.plant;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public abstract class CustomizedCrop
{
	private static final List<CustomizedCrop> REGISTRY = new ArrayList<>();

	public static void register(@NotNull CustomizedCrop crop)
	{
		REGISTRY.add(crop);
	}

	@Nullable
	public static CustomizedCrop selectBySeed(@NotNull PlantSeedContext context)
	{
		return REGISTRY.stream().filter(it -> it.isSeed(context)).findFirst().orElse(null);
	}

	@Nullable
	public static CustomizedCrop selectByCrop(@NotNull PlantBlockContext context)
	{
		return REGISTRY.stream().filter(it -> it.isCrop(context)).findFirst().orElse(null);
	}

	@Nullable
	public BlockState getPlantState(@NotNull PlantSeedContext context)
	{
		return null;
	}

	public boolean isSeed(@NotNull PlantSeedContext context)
	{
		return false;
	}

	public boolean isCrop(@NotNull PlantBlockContext context)
	{
		return false;
	}

	@Nullable
	public SpecialHarvestPositionFunction getSpecialHarvestPosition(@NotNull PlantBlockContext context)
	{
		return null;
	}

	@Nullable
	public SpecialHarvestMethodFunction getSpecialHarvestMethod(@NotNull PlantBlockContext context)
	{
		return null;
	}

	public interface SpecialHarvestPositionFunction
	{
		@Nullable
		BlockPos apply(@NotNull PlantBlockContext context);
	}

	public interface SpecialHarvestMethodFunction
	{
		@NotNull
		List<ItemStack> harvest(@NotNull PlantBlockContext context, @NotNull HarvesterContext harvester);
	}

}
