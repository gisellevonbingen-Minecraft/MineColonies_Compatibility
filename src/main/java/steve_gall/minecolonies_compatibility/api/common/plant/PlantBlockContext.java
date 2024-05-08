package steve_gall.minecolonies_compatibility.api.common.plant;

import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class PlantBlockContext extends PlantPositonContext
{
	@NotNull
	private final BlockState state;

	public PlantBlockContext(@NotNull LevelReader level, @NotNull BlockPos position, @NotNull BlockState state)
	{
		super(level, position);
		this.state = state;
	}

	@NotNull
	public List<ItemStack> getDrops(@Nullable HarvesterContext harvester)
	{
		LivingEntity entity = null;
		ItemStack tool = ItemStack.EMPTY;

		if (harvester != null)
		{
			entity = harvester.getEntity();
			tool = harvester.getTool();
		}

		return this.getDrops(entity, tool);
	}

	@NotNull
	public List<ItemStack> getDrops(@Nullable LivingEntity entity, @NotNull ItemStack tool)
	{
		if (this.getLevel() instanceof ServerLevel level)
		{
			var state = this.getState();
			var position = this.getPosition();
			return Block.getDrops(state, level, position, null, entity, tool);
		}
		else
		{
			return Collections.emptyList();
		}

	}

	@NotNull
	public BlockState getState()
	{
		return this.state;
	}

}
