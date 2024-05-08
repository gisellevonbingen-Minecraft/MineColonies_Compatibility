package steve_gall.minecolonies_compatibility.api.common.plant;

import org.jetbrains.annotations.NotNull;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelReader;

public class PlantSeedContext extends PlantPositonContext
{
	@NotNull
	private final ItemStack seed;

	public PlantSeedContext(@NotNull LevelReader level, @NotNull BlockPos position, @NotNull ItemStack seed)
	{
		super(level, position);
		this.seed = seed;
	}

	@NotNull
	public ItemStack getSeed()
	{
		return this.seed;
	}

}
