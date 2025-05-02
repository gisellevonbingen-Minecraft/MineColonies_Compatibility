package steve_gall.minecolonies_compatibility.module.common.collectorsreap;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import net.brdle.collectorsreap.common.block.IFruiting;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraftforge.registries.ForgeRegistries;
import steve_gall.minecolonies_compatibility.api.common.plant.CustomizedFruit;
import steve_gall.minecolonies_compatibility.api.common.plant.HarvesterContext;
import steve_gall.minecolonies_compatibility.api.common.plant.PlantBlockContext;

public class FruitBushFruit<BLOCK extends Block & IFruiting> extends CustomizedFruit
{
	private final BLOCK block;
	private final IntegerProperty ageProperty;
	private final int maxAge;

	private final List<ItemStack> blockIcons;
	private final List<ItemStack> itemIcons;

	public FruitBushFruit(BLOCK block, Item seed, IntegerProperty ageProperty, int maxAge)
	{
		this.block = block;
		this.ageProperty = ageProperty;
		this.maxAge = maxAge;

		this.blockIcons = Arrays.asList(new ItemStack(seed));
		this.itemIcons = Arrays.asList(new ItemStack(block.getFruit()));
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
		return context.getState().getValue(this.ageProperty) == this.maxAge;
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
			var drops = Arrays.asList(new ItemStack(this.block.getFruit(), this.block.getNumFruit(level)));

			var state = context.getState();
			level.setBlock(context.getPosition(), state.setValue(this.ageProperty, 0), Block.UPDATE_ALL);

			return drops;
		}
		else
		{
			return Collections.emptyList();
		}

	}

}
