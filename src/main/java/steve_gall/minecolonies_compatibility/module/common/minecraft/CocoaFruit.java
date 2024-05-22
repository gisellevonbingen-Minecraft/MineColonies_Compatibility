package steve_gall.minecolonies_compatibility.module.common.minecraft;

import java.util.Arrays;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.minecolonies.api.util.constant.IToolType;
import com.minecolonies.api.util.constant.ToolType;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CocoaBlock;
import net.minecraftforge.registries.ForgeRegistries;
import steve_gall.minecolonies_compatibility.api.common.plant.CustomizedFruit;
import steve_gall.minecolonies_compatibility.api.common.plant.HarvesterContext;
import steve_gall.minecolonies_compatibility.api.common.plant.PlantBlockContext;

public class CocoaFruit extends CustomizedFruit
{
	@Override
	public @NotNull ResourceLocation getId()
	{
		return ForgeRegistries.BLOCKS.getKey(Blocks.COCOA);
	}

	@Override
	public @NotNull List<ItemLike> getBlockIcons()
	{
		return Arrays.asList(Items.COCOA_BEANS);
	}

	@Override
	public @NotNull List<Item> getItemIcons()
	{
		return Arrays.asList(Items.COCOA_BEANS);
	}

	@Override
	public boolean test(@NotNull PlantBlockContext context)
	{
		return context.getState().getBlock() == Blocks.COCOA;
	}

	@Override
	public boolean canHarvest(@NotNull PlantBlockContext context)
	{
		return context.getState().getValue(CocoaBlock.AGE) == CocoaBlock.MAX_AGE;
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
	public @NotNull List<ItemStack> harvest(@NotNull PlantBlockContext context, @NotNull HarvesterContext harvester)
	{
		var drops = context.getDrops(null);
		var newState = context.getState().setValue(CocoaBlock.AGE, 0);
		this.replant(context, drops, newState);
		return drops;
	}

}
