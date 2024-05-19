package steve_gall.minecolonies_compatibility.api.common.plant;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelWriter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public abstract class CustomizedFruit
{
	private static final List<CustomizedFruit> REGISTRY = new ArrayList<>();

	public static void register(@NotNull CustomizedFruit fruit)
	{
		REGISTRY.add(fruit);
	}

	@Nullable
	public static CustomizedFruit select(@NotNull PlantBlockContext context)
	{
		return REGISTRY.stream().filter(it -> it.test(context)).findFirst().orElse(null);
	}

	public abstract boolean test(@NotNull PlantBlockContext context);

	public abstract boolean canHarvest(@NotNull PlantBlockContext context);

	public abstract boolean isMaxHarvest(@NotNull PlantBlockContext context);

	@NotNull
	public abstract List<ItemStack> harvest(@NotNull PlantBlockContext context, @NotNull HarvesterContext harvester);

	protected void replant(@NotNull PlantBlockContext context, @NotNull List<ItemStack> drops, BlockState replantState)
	{
		var block = context.getState().getBlock();
		var canReplant = false;

		for (int i = 0; i < drops.size(); i++)
		{
			var stack = drops.get(i);

			if (stack.getItem() instanceof BlockItem item && item.getBlock() == block)
			{
				canReplant = true;
				stack.shrink(1);

				if (stack.isEmpty())
				{
					drops.remove(i);
				}

				break;
			}

		}

		if (context.getLevel() instanceof LevelWriter level)
		{
			level.setBlock(context.getPosition(), canReplant ? replantState : Blocks.AIR.defaultBlockState(), Block.UPDATE_CLIENTS);
		}

	}

}
