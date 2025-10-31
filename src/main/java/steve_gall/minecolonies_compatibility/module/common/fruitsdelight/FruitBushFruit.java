package steve_gall.minecolonies_compatibility.module.common.fruitsdelight;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import dev.xkmc.fruitsdelight.content.block.BaseBushBlock;
import dev.xkmc.fruitsdelight.content.block.DoubleFruitBushBlock;
import dev.xkmc.fruitsdelight.init.plants.FDBushes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelWriter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraftforge.registries.ForgeRegistries;
import steve_gall.minecolonies_compatibility.api.common.plant.CustomizedFruit;
import steve_gall.minecolonies_compatibility.api.common.plant.HarvesterContext;
import steve_gall.minecolonies_compatibility.api.common.plant.PlantBlockContext;

public class FruitBushFruit extends CustomizedFruit
{
	private final BushBlock bush;
	private final Item fruit;

	private final ResourceLocation id;
	private final List<ItemStack> blockIcons;
	private final List<ItemStack> itemIcons;

	public FruitBushFruit(FDBushes bush)
	{
		this(bush.getBush(), bush.getSeed(), bush.getFruit());
	}

	public FruitBushFruit(BushBlock bush, Item seed, Item fruit)
	{
		this.bush = bush;
		this.fruit = fruit;

		this.id = ForgeRegistries.BLOCKS.getKey(bush);
		this.blockIcons = Arrays.asList(new ItemStack(seed));
		this.itemIcons = Arrays.asList(new ItemStack(fruit));
	}

	@Override
	public @NotNull ResourceLocation getId()
	{
		return this.id;
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
		return context.getState().is(this.bush);
	}

	@Override
	public boolean canHarvest(@NotNull PlantBlockContext context)
	{
		return context.getState().getValue(BaseBushBlock.AGE) == BaseBushBlock.MAX_AGE;
	}

	@Override
	public boolean isMaxHarvest(@NotNull PlantBlockContext context)
	{
		return true;
	}

	@Override
	@NotNull
	public List<ItemStack> harvest(@NotNull PlantBlockContext context, @NotNull HarvesterContext harvester)
	{
		if (context.getLevel() instanceof LevelWriter level)
		{
			var newState = context.getState().setValue(BaseBushBlock.AGE, 2);
			level.setBlock(context.getPosition(), newState, Block.UPDATE_CLIENTS);

			if (this.bush instanceof DoubleFruitBushBlock)
			{
				var oppositeState = newState;
				var oppositePos = context.getPosition();

				if (newState.getValue(DoublePlantBlock.HALF) == DoubleBlockHalf.LOWER)
				{
					oppositeState = newState.setValue(DoublePlantBlock.HALF, DoubleBlockHalf.UPPER);
					oppositePos = oppositePos.above();
				}
				else
				{
					oppositeState = newState.setValue(DoublePlantBlock.HALF, DoubleBlockHalf.LOWER);
					oppositePos = oppositePos.below();
				}

				level.setBlock(oppositePos, oppositeState, Block.UPDATE_CLIENTS);
			}

		}

		if (context.getLevel() instanceof ServerLevel level)
		{
			var j = 1 + level.random.nextInt(2);
			return Collections.singletonList(new ItemStack(this.fruit, j));
		}

		return Collections.emptyList();
	}

}
