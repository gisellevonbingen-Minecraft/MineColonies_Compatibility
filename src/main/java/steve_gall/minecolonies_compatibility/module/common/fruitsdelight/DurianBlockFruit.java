package steve_gall.minecolonies_compatibility.module.common.fruitsdelight;

import java.util.Arrays;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.minecolonies.api.util.constant.IToolType;
import com.minecolonies.api.util.constant.ToolType;

import dev.xkmc.fruitsdelight.content.block.BaseLeavesBlock;
import dev.xkmc.fruitsdelight.init.plants.FDTrees;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelWriter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraftforge.registries.ForgeRegistries;
import steve_gall.minecolonies_compatibility.api.common.plant.CustomizedFruit;
import steve_gall.minecolonies_compatibility.api.common.plant.HarvesterContext;
import steve_gall.minecolonies_compatibility.api.common.plant.PlantBlockContext;

public class DurianBlockFruit extends CustomizedFruit
{
	private final Block block;

	private final ResourceLocation id;
	private final List<ItemStack> blockIcons;
	private final List<ItemStack> itemIcons;

	public DurianBlockFruit(FDTrees tree)
	{
		this(tree.getLeaves(), tree.getSapling(), tree.getFruit());
	}

	public DurianBlockFruit(BaseLeavesBlock leaves, SaplingBlock sapling, Item fruit)
	{
		this.block = ((BlockItem) fruit).getBlock();

		this.id = ForgeRegistries.BLOCKS.getKey(leaves);
		this.blockIcons = Arrays.asList(new ItemStack(leaves), new ItemStack(sapling));
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
		return context.getState().is(this.block);
	}

	@Override
	public boolean canHarvest(@NotNull PlantBlockContext context)
	{
		return true;
	}

	@Override
	public boolean isMaxHarvest(@NotNull PlantBlockContext context)
	{
		return true;
	}

	@Override
	public IToolType getHarvestToolType()
	{
		return ToolType.AXE;
	}

	@Override
	@NotNull
	public SoundEvent getHarvestSound(@NotNull PlantBlockContext context)
	{
		return SoundEvents.WOOD_BREAK;
	}

	@Override
	@NotNull
	public List<ItemStack> harvest(@NotNull PlantBlockContext context, @NotNull HarvesterContext harvester)
	{
		if (context.getLevel() instanceof LevelWriter level)
		{
			level.setBlock(context.getPosition(), Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL);
		}

		return context.getDrops(harvester);
	}

}
