package steve_gall.minecolonies_compatibility.module.common.collectorsreap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import net.brdle.collectorsreap.common.block.FruitBushBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraftforge.registries.ForgeRegistries;
import steve_gall.minecolonies_compatibility.api.common.plant.CustomizedFruit;
import steve_gall.minecolonies_compatibility.api.common.plant.HarvesterContext;
import steve_gall.minecolonies_compatibility.api.common.plant.PlantBlockContext;

public class FruitBushFruit extends CustomizedFruit
{
	private final FruitBushBlock block;
	private final List<ItemStack> blockIcons;
	private final List<ItemStack> itemIcons;

	public FruitBushFruit(FruitBushBlock block)
	{
		this.block = block;
		this.blockIcons = Arrays.asList(new ItemStack(block.getSeeds()));

		var itemIcons = new ArrayList<ItemStack>();
		itemIcons.add(new ItemStack(block.getFruit()));

		var special = block.getSpecialFruit();

		if (!special.isEmpty())
		{
			itemIcons.add(special.copy());
		}

		this.itemIcons = itemIcons;
	}

	@Override
	public @NotNull ResourceLocation getId()
	{
		return ForgeRegistries.BLOCKS.getKey(this.block);
	}

	@Override
	public @NotNull List<ItemStack> getBlockIcons()
	{
		return this.blockIcons;
	}

	@Override
	public @NotNull List<ItemStack> getItemIcons()
	{
		return this.itemIcons;
	}

	@Override
	public boolean test(@NotNull PlantBlockContext context)
	{
		return context.getState().getBlock() == this.block;
	}

	@Override
	public boolean canHarvest(@NotNull PlantBlockContext context)
	{
		return context.getState().getValue(FruitBushBlock.AGE) == FruitBushBlock.MAX_AGE;
	}

	@Override
	public boolean isMaxHarvest(@NotNull PlantBlockContext context)
	{
		return true;
	}

	@Override
	public @NotNull List<ItemStack> harvest(@NotNull PlantBlockContext context, @NotNull HarvesterContext harvester)
	{
		if (context.getLevel() instanceof Level level)
		{
			var state = context.getState();
			BlockPos lowerPos = null;
			BlockState lowerState = null;

			if (state.getValue(DoublePlantBlock.HALF) == DoubleBlockHalf.UPPER)
			{
				lowerPos = context.getPosition().below();
				lowerState = level.getBlockState(lowerPos);
			}
			else
			{
				lowerPos = context.getPosition();
				lowerState = state;
			}

			var drops = this.getDrops(level, lowerPos);

			var newLowerState = lowerState.setValue(FruitBushBlock.AGE, FruitBushBlock.MAX_AGE - 2);
			level.setBlock(lowerPos, newLowerState, Block.UPDATE_CLIENTS);
			level.setBlock(lowerPos.above(), newLowerState.setValue(DoublePlantBlock.HALF, DoubleBlockHalf.UPPER), Block.UPDATE_CLIENTS);

			return drops;
		}
		else
		{
			return Collections.emptyList();
		}

	}

	private List<ItemStack> getDrops(Level level, BlockPos pos)
	{
		ItemStack stack;

		if (this.block.isSpecial(level, pos))
		{
			stack = this.block.getSpecialFruit().copy();
		}
		else
		{
			var additional = level.getRandom().nextIntBetweenInclusive(1, 2);
			stack = new ItemStack(this.block.getFruit(), this.block.getNumFruit(additional));
		}

		return Arrays.asList(stack);
	}

}
