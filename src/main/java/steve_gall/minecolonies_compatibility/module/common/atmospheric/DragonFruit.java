package steve_gall.minecolonies_compatibility.module.common.atmospheric;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.teamabnormals.atmospheric.common.block.DragonRootsBlock;
import com.teamabnormals.atmospheric.common.block.state.properties.DragonRootsStage;
import com.teamabnormals.atmospheric.core.registry.AtmosphericBlocks;
import com.teamabnormals.atmospheric.core.registry.AtmosphericItems;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import steve_gall.minecolonies_compatibility.api.common.plant.CustomizedFruit;
import steve_gall.minecolonies_compatibility.api.common.plant.HarvesterContext;
import steve_gall.minecolonies_compatibility.api.common.plant.PlantBlockContext;

public class DragonFruit extends CustomizedFruit
{
	@Override
	public @NotNull ResourceLocation getId()
	{
		return AtmosphericItems.DRAGON_FRUIT.getId();
	}

	@Override
	public @NotNull List<ItemStack> getBlockIcons()
	{
		return Collections.singletonList(new ItemStack(AtmosphericBlocks.DRAGON_ROOTS.get()));
	}

	@Override
	public @NotNull List<ItemStack> getItemIcons()
	{
		return Arrays.asList(new ItemStack(AtmosphericItems.DRAGON_FRUIT.get()), new ItemStack(AtmosphericItems.ENDER_DRAGON_FRUIT.get()));
	}

	@Override
	public boolean test(@NotNull PlantBlockContext context)
	{
		return context.getState().getBlock() == AtmosphericBlocks.DRAGON_ROOTS.get();
	}

	@Override
	public boolean canHarvest(@NotNull PlantBlockContext context)
	{
		return DragonRootsBlock.hasFruit(context.getState());
	}

	@Override
	public boolean isMaxHarvest(@NotNull PlantBlockContext context)
	{
		return true;
	}

	@Override
	public @NotNull List<ItemStack> harvest(@NotNull PlantBlockContext context, @NotNull HarvesterContext harvester)
	{
		if (context.getLevel() instanceof ServerLevel level)
		{
			var state = context.getState();
			var newState = state;
			var list = new ArrayList<ItemStack>();

			for (var property : Arrays.asList(DragonRootsBlock.TOP_STAGE, DragonRootsBlock.BOTTOM_STAGE))
			{
				if (DragonRootsBlock.hasFruit(property, state))
				{
					newState = newState.setValue(property, DragonRootsStage.ROOTS);
					list.add(new ItemStack(DragonRootsBlock.isEnder(property, state) ? AtmosphericItems.ENDER_DRAGON_FRUIT.get() : AtmosphericItems.DRAGON_FRUIT.get()));
				}

			}

			level.setBlock(context.getPosition(), newState, Block.UPDATE_CLIENTS);
			return list;
		}
		else
		{
			return Collections.emptyList();
		}

	}

}
