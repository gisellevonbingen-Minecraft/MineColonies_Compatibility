package steve_gall.minecolonies_compatibility.api.common.plant;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.minecolonies.api.util.constant.IToolType;
import com.minecolonies.api.util.constant.ToolType;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.LevelWriter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public abstract class CustomizedFruit
{
	private static final Map<ResourceLocation, CustomizedFruit> REGISTRY = new HashMap<>();

	public static void register(@NotNull CustomizedFruit fruit)
	{
		REGISTRY.put(fruit.getId(), fruit);
	}

	public static Map<ResourceLocation, CustomizedFruit> getRegistry()
	{
		return Collections.unmodifiableMap(REGISTRY);
	}

	@Nullable
	public static CustomizedFruit select(@NotNull PlantBlockContext context)
	{
		return REGISTRY.values().stream().filter(it -> it.test(context)).findFirst().orElse(null);
	}

	@Override
	public int hashCode()
	{
		return this.getId().hashCode();
	}

	@Override
	public boolean equals(Object obj)
	{
		return this == obj;
	}

	@NotNull
	public abstract ResourceLocation getId();

	@NotNull
	public abstract List<ItemLike> getBlockIcons();

	@NotNull
	public abstract List<Item> getItemIcons();

	public abstract boolean test(@NotNull PlantBlockContext context);

	public abstract boolean canHarvest(@NotNull PlantBlockContext context);

	public abstract boolean isMaxHarvest(@NotNull PlantBlockContext context);

	@NotNull
	public abstract List<ItemStack> harvest(@NotNull PlantBlockContext context, @NotNull HarvesterContext harvester);

	@NotNull
	public IToolType getHarvestToolType()
	{
		return ToolType.SHEARS;
	}

	@NotNull
	public IToolType getHarvestToolType(@NotNull PlantBlockContext context)
	{
		return this.getHarvestToolType();
	}

	@NotNull
	public SoundEvent getHarvestSound(@NotNull PlantBlockContext context)
	{
		return SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES;
	}

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
