package steve_gall.minecolonies_compatibility.api.common.building.module;

import org.jetbrains.annotations.NotNull;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.Block;

public interface ICraftingModuleWithExternalWorkingBlocks extends IModuleWithExternalWorkingBlocks
{
	boolean isIntermediate(@NotNull Block block);

	@NotNull
	Component getWorkingBlockNotFoundMessage();

	@NotNull
	default BlockPos getHitPosition(@NotNull BlockPos pos)
	{
		return pos;
	}

	@NotNull
	default BlockPos getParticlePosition(@NotNull BlockPos pos)
	{
		return this.getHitPosition(pos).above();
	}

}
