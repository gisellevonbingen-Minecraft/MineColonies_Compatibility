package steve_gall.minecolonies_compatibility.module.common.dynamictrees;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.dtteam.dynamictrees.block.fruit.Fruit;
import com.dtteam.dynamictrees.block.fruit.FruitBlock;
import com.dtteam.dynamictrees.item.Seed;
import com.dtteam.dynamictrees.tree.species.Species;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import steve_gall.minecolonies_compatibility.api.common.plant.CustomizedFruit;
import steve_gall.minecolonies_compatibility.api.common.plant.HarvesterContext;
import steve_gall.minecolonies_compatibility.api.common.plant.PlantBlockContext;

public class SpeciesFruit extends CustomizedFruit
{
	private final Species species;
	private final Set<FruitBlock> fruitBlocks;
	private final List<ItemStack> blockIcons;
	private final List<ItemStack> itemIcons;

	public SpeciesFruit(Species species, Seed seed, Collection<Fruit> fruits)
	{
		this.species = species;
		this.fruitBlocks = fruits.stream().map(Fruit::getBlock).collect(Collectors.toSet());
		this.blockIcons = Collections.singletonList(new ItemStack(seed));
		this.itemIcons = fruits.stream().map(Fruit::getItemStack).toList();
	}

	@Override
	public ResourceLocation getId()
	{
		return this.species.getRegistryName();
	}

	@Override
	public List<ItemStack> getBlockIcons()
	{
		return this.blockIcons;
	}

	@Override
	public List<ItemStack> getItemIcons()
	{
		return this.itemIcons;
	}

	@Override
	public boolean test(PlantBlockContext context)
	{
		var state = context.getState();
		return state.getBlock() instanceof FruitBlock fruit && this.fruitBlocks.contains(fruit);
	}

	@Override
	public boolean canHarvest(PlantBlockContext context)
	{
		var state = context.getState();
		return state.getBlock() instanceof FruitBlock fruit && fruit.getAge(state) >= fruit.getMaxAge();
	}

	@Override
	public boolean isMaxHarvest(PlantBlockContext context)
	{
		return true;
	}

	@Override
	public List<ItemStack> harvest(PlantBlockContext context, HarvesterContext harvester)
	{
		var drops = context.getDrops(harvester);

		if (context.getLevel() instanceof ServerLevel level)
		{
			level.setBlock(context.getPosition(), Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL);
		}

		return drops;
	}

}
