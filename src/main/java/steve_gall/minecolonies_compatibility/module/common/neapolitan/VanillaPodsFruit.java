package steve_gall.minecolonies_compatibility.module.common.neapolitan;

import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.teamabnormals.neapolitan.common.block.VanillaVineBlock;
import com.teamabnormals.neapolitan.core.registry.NeapolitanBlocks;
import com.teamabnormals.neapolitan.core.registry.NeapolitanItems;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelWriter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import steve_gall.minecolonies_compatibility.api.common.plant.CustomizedFruit;
import steve_gall.minecolonies_compatibility.api.common.plant.HarvesterContext;
import steve_gall.minecolonies_compatibility.api.common.plant.PlantBlockContext;

public class VanillaPodsFruit extends CustomizedFruit
{
	@Override
	public @NotNull ResourceLocation getId()
	{
		return NeapolitanItems.VANILLA_PODS.getId();
	}

	@Override
	public @NotNull List<ItemStack> getBlockIcons()
	{
		return Collections.singletonList(new ItemStack(NeapolitanBlocks.VANILLA_VINE.get()));
	}

	@Override
	public @NotNull List<ItemStack> getItemIcons()
	{
		return Collections.singletonList(new ItemStack(NeapolitanItems.VANILLA_PODS.get()));
	}

	@Override
	public boolean test(@NotNull PlantBlockContext context)
	{
		return context.getState().is(NeapolitanBlocks.VANILLA_VINE.get());
	}

	@Override
	public boolean canHarvest(@NotNull PlantBlockContext context)
	{
		if (context.getState().is(NeapolitanBlocks.VANILLA_VINE.get()))
		{
			var opposite = context.getState().getValue(VanillaVineBlock.FACING).getOpposite();
			var basePosition = context.getPosition().relative(opposite);
			return context.getLevel().getBlockState(basePosition).is(NeapolitanBlocks.VANILLA_VINE_PLANT.get());
		}
		else
		{
			return false;
		}

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
			level.setBlock(context.getPosition(), Blocks.AIR.defaultBlockState(), Block.UPDATE_CLIENTS);
		}

		return context.getDrops(harvester);
	}

}
