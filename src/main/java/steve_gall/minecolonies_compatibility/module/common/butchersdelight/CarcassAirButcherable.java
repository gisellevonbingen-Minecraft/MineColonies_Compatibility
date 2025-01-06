package steve_gall.minecolonies_compatibility.module.common.butchersdelight;

import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.minecolonies.api.entity.citizen.AbstractEntityCitizen;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class CarcassAirButcherable extends CarcassButcherable
{
	public CarcassAirButcherable(AbstractButcherable.Builder builder)
	{
		super(builder);
	}

	@Override
	public @NotNull List<BlockState> getTableIcons()
	{
		return Collections.emptyList();
	}

	@Override
	public @Nullable boolean isTableBlock(@NotNull LevelReader level, @NotNull BlockPos position, @NotNull BlockState state)
	{
		var block = level.getBlockState(position.below());
		return state.is(Blocks.AIR) && block.isFaceSturdy(level, position.below(), Direction.UP);
	}

	@Override
	public void doButcherTable(@NotNull Level level, @NotNull BlockPos position, @NotNull BlockState state, @NotNull AbstractEntityCitizen worker, @NotNull InteractionHand hand)
	{
		super.doButcherTable(level, position, state, worker, hand);

		if (level instanceof ServerLevel serverLevel)
		{
			ButchersDelightModule.rightClick(serverLevel, position.below(), worker, worker.getItemInHand(hand));
		}

	}

}
