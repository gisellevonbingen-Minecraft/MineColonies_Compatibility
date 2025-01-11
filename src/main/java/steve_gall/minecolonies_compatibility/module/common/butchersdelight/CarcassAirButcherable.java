package steve_gall.minecolonies_compatibility.module.common.butchersdelight;

import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.minecolonies.api.entity.citizen.AbstractEntityCitizen;

import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import steve_gall.minecolonies_compatibility.api.common.butcher.ButcherBlockContext;

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
	public @Nullable boolean isTableBlock(@NotNull ButcherBlockContext context)
	{
		var below = context.getPosition().below();
		var block = context.getLevel().getBlockState(below);
		return context.getState().is(Blocks.AIR) && block.isFaceSturdy(context.getLevel(), below, Direction.UP);
	}

	@Override
	public void doButcherTable(@NotNull ButcherBlockContext context, @NotNull AbstractEntityCitizen worker, @NotNull InteractionHand hand)
	{
		super.doButcherTable(context, worker, hand);

		if (context.getLevel() instanceof ServerLevel serverLevel)
		{
			ButchersDelightModule.rightClick(serverLevel, context.getPosition().below(), worker, worker.getItemInHand(hand));
		}

	}

}
