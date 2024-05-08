package steve_gall.minecolonies_compatibility.core.common.module.farmersdelight;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import steve_gall.minecolonies_compatibility.api.common.plant.CustomizedCrop;
import steve_gall.minecolonies_compatibility.api.common.plant.HarvesterContext;
import steve_gall.minecolonies_compatibility.api.common.plant.PlantBlockContext;
import steve_gall.minecolonies_compatibility.api.common.plant.PlantSeedContext;
import vectorwing.farmersdelight.common.block.TomatoVineBlock;
import vectorwing.farmersdelight.common.registry.ModBlocks;
import vectorwing.farmersdelight.common.registry.ModItems;

public class TomatoCrop extends CustomizedCrop
{
	public static final int MAX_VINE_HEIGHT = 3;

	@Override
	public boolean isSeed(@NotNull PlantSeedContext context)
	{
		return context.getSeed().getItem() == ModItems.TOMATO_SEEDS.get();
	}

	@Override
	public boolean isCrop(@NotNull PlantBlockContext context)
	{
		var block = context.getState().getBlock();
		return block == ModBlocks.BUDDING_TOMATO_CROP.get() || block == ModBlocks.TOMATO_CROP.get();
	}

	@Override
	@Nullable
	public BlockState getPlantState(@NotNull PlantSeedContext context)
	{
		return ((BlockItem) context.getSeed().getItem()).getBlock().defaultBlockState();
	}

	@Override
	@Nullable
	public SpecialHarvestPositionFunction getSpecialHarvestPosition(@NotNull PlantBlockContext context)
	{
		return this::getPositionIfAnyVineHarvestable;
	}

	@Override
	@Nullable
	public SpecialHarvestMethodFunction getSpecialHarvestMethod(@NotNull PlantBlockContext context)
	{
		return this::harvestVines;
	}

	private BlockPos getPositionIfAnyVineHarvestable(@NotNull PlantBlockContext context)
	{
		var level = context.getLevel();
		var basePosition = context.getPosition();

		for (var i = 0; i < MAX_VINE_HEIGHT; i++)
		{
			var vinePosition = basePosition.above(i);
			var state = level.getBlockState(vinePosition);

			if (state.getBlock() instanceof TomatoVineBlock vine)
			{
				if (vine.isMaxAge(state))
				{
					return basePosition;
				}

			}
			else
			{
				break;
			}

		}

		return null;
	}

	private List<ItemStack> harvestVines(@NotNull PlantBlockContext context, @NotNull HarvesterContext harvester)
	{
		if (context.getLevel() instanceof ServerLevel level)
		{
			var basePosition = context.getPosition();
			var random = level.random;
			var list = new ArrayList<ItemStack>();

			for (var i = 0; i < MAX_VINE_HEIGHT; i++)
			{
				var vinePosition = basePosition.above(i);
				var vineState = level.getBlockState(vinePosition);

				if (vineState.getBlock() instanceof TomatoVineBlock vine)
				{
					if (vine.isMaxAge(vineState))
					{
						list.add(new ItemStack(ModItems.TOMATO.get(), 1 + random.nextInt(2)));

						if (random.nextFloat() < 0.05D)
						{
							list.add(new ItemStack(ModItems.ROTTEN_TOMATO.get()));
						}

						var vineNewState = vineState.setValue(vine.getAgeProperty(), 0);
						level.setBlock(vinePosition, vineNewState, Block.UPDATE_CLIENTS);
					}

				}
				else
				{
					break;
				}

			}

			return list;
		}
		else
		{
			return Collections.emptyList();
		}

	}

}
