package steve_gall.minecolonies_compatibility.module.common.butchersdelight;

import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.minecolonies.api.entity.citizen.AbstractEntityCitizen;

import net.mcreator.butchersdelight.init.ButchersdelightModBlocks;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.block.state.BlockState;
import steve_gall.minecolonies_compatibility.api.common.butcher.ButcherBlockContext;
import steve_gall.minecolonies_compatibility.core.common.util.InteractionMessageHelper;

public class CarcassHookButcherable extends CarcassButcherable
{
	protected final List<BlockState> tableIcons;

	public CarcassHookButcherable(AbstractButcherable.Builder builder)
	{
		super(builder);

		this.tableIcons = Collections.singletonList(ButchersdelightModBlocks.HOOK.get().defaultBlockState());
	}

	@Override
	public @NotNull List<BlockState> getTableIcons()
	{
		return this.tableIcons;
	}

	@Override
	public @Nullable boolean isTableBlock(@NotNull ButcherBlockContext context)
	{
		return context.getState().is(ButchersdelightModBlocks.HOOK.get());
	}

	@Override
	public void doButcherTable(@NotNull ButcherBlockContext context, @NotNull AbstractEntityCitizen worker, @NotNull InteractionHand hand)
	{
		super.doButcherTable(context, worker, hand);

		if (context.getLevel() instanceof ServerLevel serverLevel)
		{
			ButchersDelightModule.rightClick(serverLevel, context.getPosition(), worker, worker.getItemInHand(hand));
		}

	}

	@Override
	public Component getTableNotFoundMessage()
	{
		return InteractionMessageHelper.getWorkingBlockNotFound(ButchersdelightModBlocks.HOOK.get());
	}

}
