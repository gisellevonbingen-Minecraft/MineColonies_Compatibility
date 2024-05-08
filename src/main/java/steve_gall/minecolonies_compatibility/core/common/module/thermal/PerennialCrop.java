package steve_gall.minecolonies_compatibility.core.common.module.thermal;

import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import cofh.lib.block.CropBlockPerennial;
import cofh.lib.util.helpers.MathHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelWriter;
import net.minecraft.world.level.block.Block;
import steve_gall.minecolonies_compatibility.api.common.entity.plant.CustomizedCrop;
import steve_gall.minecolonies_compatibility.api.common.entity.plant.HarvesterContext;
import steve_gall.minecolonies_compatibility.api.common.entity.plant.PlantBlockContext;
import steve_gall.minecolonies_compatibility.core.common.mixin.thermal.CropBlockCoFHAccessor;

public class PerennialCrop extends CustomizedCrop
{
	@Override
	public boolean isCrop(@NotNull PlantBlockContext context)
	{
		return context.getState().getBlock() instanceof CropBlockPerennial;
	}

	@Override
	public @Nullable SpecialHarvestPositionFunction getSpecialHarvestPosition(@NotNull PlantBlockContext context)
	{
		return this::getHarvestPosition;
	}

	@Override
	public @Nullable SpecialHarvestMethodFunction getSpecialHarvestMethod(@NotNull PlantBlockContext context)
	{
		return this::harvest;
	}

	private BlockPos getHarvestPosition(@NotNull PlantBlockContext context)
	{
		var state = context.getState();

		if (state.getBlock() instanceof CropBlockPerennial block && block.canHarvest(state))
		{
			return context.getPosition();
		}
		else
		{
			return null;
		}

	}

	private List<ItemStack> harvest(@NotNull PlantBlockContext context, @NotNull HarvesterContext harvester)
	{
		if (context.getLevel() instanceof LevelWriter level)
		{
			var state = context.getState();
			var block = (CropBlockPerennial) state.getBlock();
			var accessor = (CropBlockCoFHAccessor) block;
			var fortune = 0;
			var drop = new ItemStack(accessor.invokeGetCropItem(), 2 + MathHelper.binomialDist(fortune, 0.5D));
			level.setBlock(context.getPosition(), block.getStateForAge(accessor.invokeGetPostHarvestAge()), Block.UPDATE_CLIENTS);
			return Collections.singletonList(drop);
		}
		else
		{
			return Collections.emptyList();
		}

	}

}
