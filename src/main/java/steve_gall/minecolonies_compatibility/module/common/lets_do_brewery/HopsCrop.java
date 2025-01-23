package steve_gall.minecolonies_compatibility.module.common.lets_do_brewery;

import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.satisfy.brewery.block.HopsCropBlock;
import net.satisfy.brewery.registry.ObjectRegistry;
import steve_gall.minecolonies_compatibility.api.common.plant.CustomizedCrop;
import steve_gall.minecolonies_compatibility.api.common.plant.HarvesterContext;
import steve_gall.minecolonies_compatibility.api.common.plant.PlantBlockContext;
import steve_gall.minecolonies_compatibility.api.common.plant.PlantSeedContext;

public class HopsCrop extends CustomizedCrop
{
	public static final int MAX_AGE = 3;

	@Override
	public boolean isSeed(@NotNull PlantSeedContext context)
	{
		return context.getSeed().getItem() == ObjectRegistry.HOPS_SEEDS.get();
	}

	@Override
	public boolean isCrop(@NotNull PlantBlockContext context)
	{
		return context.getState().getBlock() instanceof HopsCropBlock;
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

	private @Nullable BlockPos getHarvestPosition(@NotNull PlantBlockContext context)
	{
		var pos = context.getPosition();

		while (true)
		{
			var state = context.getLevel().getBlockState(pos);

			if (state.getBlock() instanceof HopsCropBlock)
			{
				if (state.getValue(HopsCropBlock.AGE) >= MAX_AGE)
				{
					return pos;
				}
				else
				{
					pos = pos.above();
				}

			}
			else
			{
				break;
			}

		}

		return null;
	}

	private @NotNull List<ItemStack> harvest(@NotNull PlantBlockContext context, @NotNull HarvesterContext harvester)
	{
		var pos = this.getHarvestPosition(context);

		if (pos != null && context.getLevel() instanceof LevelAccessor level)
		{
			var state = level.getBlockState(pos);
			var amount = level.getRandom().nextInt(2) + 1;
			var drop = new ItemStack(ObjectRegistry.HOPS.get(), amount);

			level.setBlock(pos, state.setValue(HopsCropBlock.AGE, 1), Block.UPDATE_CLIENTS);
			return Collections.singletonList(drop);
		}

		return Collections.emptyList();
	}

}
