package steve_gall.minecolonies_compatibility.module.common.fruitfulfun;

import java.util.Arrays;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelWriter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.registries.ForgeRegistries;
import snownee.fruits.block.FruitLeavesBlock;
import steve_gall.minecolonies_compatibility.api.common.plant.CustomizedFruit;
import steve_gall.minecolonies_compatibility.api.common.plant.HarvesterContext;
import steve_gall.minecolonies_compatibility.api.common.plant.PlantBlockContext;

public class FruitfulFunFruit extends CustomizedFruit
{
	private final FruitLeavesBlock leaves;

	private final ResourceLocation id;
	private final List<ItemStack> blockIcons;
	private final List<ItemStack> itemIcons;

	public FruitfulFunFruit(FruitLeavesBlock leaves)
	{
		this.leaves = leaves;

		var type = leaves.type.get();
		this.id = ForgeRegistries.BLOCKS.getKey(this.leaves);
		this.blockIcons = Arrays.asList(new ItemStack(type.sapling.get()), new ItemStack(this.leaves));
		this.itemIcons = Arrays.asList(new ItemStack(type.fruit.get()));
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
		return context.getState().is(this.leaves);
	}

	@Override
	public boolean canHarvest(@NotNull PlantBlockContext context)
	{
		return context.getState().getValue(BlockStateProperties.AGE_3) == 3;
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
			var newState = context.getState().getBlock().defaultBlockState();
			level.setBlock(context.getPosition(), newState, Block.UPDATE_CLIENTS);
		}

		return Arrays.asList(this.leaves.type.get().fruit.get().getDefaultInstance());
	}

}
