package steve_gall.minecolonies_compatibility.module.common.aether;

import java.util.Arrays;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.aetherteam.aether.block.AetherBlockStateProperties;
import com.aetherteam.aether.block.AetherBlocks;
import com.aetherteam.aether.item.AetherItems;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.LevelWriter;
import net.minecraft.world.level.block.Block;
import steve_gall.minecolonies_compatibility.api.common.plant.CustomizedFruit;
import steve_gall.minecolonies_compatibility.api.common.plant.HarvesterContext;
import steve_gall.minecolonies_compatibility.api.common.plant.PlantBlockContext;

public class BlueBerryFruit extends CustomizedFruit
{
	@Override
	public @NotNull ResourceLocation getId()
	{
		return AetherBlocks.BERRY_BUSH.getId();
	}

	@Override
	public @NotNull List<ItemLike> getBlockIcons()
	{
		return Arrays.asList(AetherBlocks.BERRY_BUSH_STEM.get(), AetherBlocks.BERRY_BUSH.get());
	}

	@Override
	public @NotNull List<Item> getItemIcons()
	{
		return Arrays.asList(AetherItems.BLUE_BERRY.get());
	}

	@Override
	public boolean test(@NotNull PlantBlockContext context)
	{
		var block = context.getState().getBlock();
		return block == AetherBlocks.BERRY_BUSH_STEM.get() || block == AetherBlocks.BERRY_BUSH.get();
	}

	@Override
	public boolean canHarvest(@NotNull PlantBlockContext context)
	{
		var block = context.getState().getBlock();
		return block == AetherBlocks.BERRY_BUSH.get();
	}

	@Override
	public boolean isMaxHarvest(@NotNull PlantBlockContext context)
	{
		return true;
	}

	@Override
	public @NotNull List<ItemStack> harvest(@NotNull PlantBlockContext context, @NotNull HarvesterContext harvester)
	{
		if (context.getLevel() instanceof LevelWriter level)
		{
			var state = context.getState();
			var property = AetherBlockStateProperties.DOUBLE_DROPS;
			var newState = AetherBlocks.BERRY_BUSH_STEM.get().defaultBlockState().setValue(property, state.getValue(property));
			level.setBlock(context.getPosition(), newState, Block.UPDATE_ALL);
		}

		return context.getDrops(harvester.getEntity(), ItemStack.EMPTY);
	}

}
