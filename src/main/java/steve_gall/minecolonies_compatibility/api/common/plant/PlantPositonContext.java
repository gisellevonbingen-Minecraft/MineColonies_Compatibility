package steve_gall.minecolonies_compatibility.api.common.plant;

import org.jetbrains.annotations.NotNull;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;

public class PlantPositonContext
{
	@NotNull
	private final LevelReader level;
	@NotNull
	private final BlockPos position;

	public PlantPositonContext(@NotNull LevelReader level, @NotNull BlockPos position)
	{
		this.level = level;
		this.position = position;
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

}
