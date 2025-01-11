package steve_gall.minecolonies_compatibility.api.common.butcher;

import org.jetbrains.annotations.NotNull;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;

public class ButcherBlockContext
{
	@NotNull
	private final LevelReader level;
	@NotNull
	private final BlockPos position;
	@NotNull
	private final BlockState state;

	public ButcherBlockContext(@NotNull LevelReader level, @NotNull BlockPos position, @NotNull BlockState state)
	{
		this.level = level;
		this.position = position;
		this.state = state;
	}

	@NotNull
	public LevelReader getLevel()
	{
		return this.level;
	}

	@NotNull
	public BlockPos getPosition()
	{
		return this.position;
	}

	@NotNull
	public BlockState getState()
	{
		return this.state;
	}

}
