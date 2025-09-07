package steve_gall.minecolonies_compatibility.module.common.neapolitan;

import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.teamabnormals.neapolitan.common.block.StrawberryBushBlock;
import com.teamabnormals.neapolitan.core.registry.NeapolitanBlocks;
import com.teamabnormals.neapolitan.core.registry.NeapolitanItems;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import steve_gall.minecolonies_compatibility.api.common.plant.CustomizedCrop;
import steve_gall.minecolonies_compatibility.api.common.plant.HarvesterContext;
import steve_gall.minecolonies_compatibility.api.common.plant.PlantBlockContext;
import steve_gall.minecolonies_compatibility.api.common.plant.PlantSeedContext;

public class StrawBerryCrop extends CustomizedCrop
{
	@Override
	public boolean isSeed(@NotNull PlantSeedContext context)
	{
		return context.getSeed().is(NeapolitanItems.STRAWBERRY_PIPS.get());
	}

	@Override
	public boolean isCrop(@NotNull PlantBlockContext context)
	{
		return context.getState().is(NeapolitanBlocks.STRAWBERRY_BUSH.get());
	}

	@Override
	@Nullable
	public SpecialHarvestPositionFunction getSpecialHarvestPosition(@NotNull PlantBlockContext context)
	{
		return this::getHarvestPosition;
	}

	@Override
	public @Nullable SpecialHarvestMethodFunction getSpecialHarvestMethod(@NotNull PlantBlockContext context)
	{
		return this::harvest;
	}

	@Nullable
	private BlockPos getHarvestPosition(@NotNull PlantBlockContext context)
	{
		var state = context.getState();

		if (state.getBlock() instanceof StrawberryBushBlock block && block.isMaxAge(state))
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
		if (context.getLevel() instanceof ServerLevel level)
		{
			var state = context.getState();
			var count = 1 + level.random.nextInt(2);
			var item = state.getValue(StrawberryBushBlock.WHITE) ? NeapolitanItems.WHITE_STRAWBERRIES.get() : NeapolitanItems.STRAWBERRIES.get();

			level.setBlock(context.getPosition(), state.setValue(StrawberryBushBlock.AGE, 1).setValue(StrawberryBushBlock.WHITE, false), Block.UPDATE_CLIENTS);
			return Collections.singletonList(new ItemStack(item, count));
		}
		else
		{
			return Collections.emptyList();
		}

	}

}
