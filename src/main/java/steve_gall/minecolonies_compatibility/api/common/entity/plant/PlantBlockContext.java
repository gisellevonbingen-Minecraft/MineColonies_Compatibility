package steve_gall.minecolonies_compatibility.api.common.entity.plant;

import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
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
	public List<ItemStack> getDrops()
	{
		if (this.getLevel() instanceof ServerLevel level)
		{
			return Block.getDrops(this.getState(), level, this.getPosition(), null);
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
