package steve_gall.minecolonies_compatibility.core.common.block;

import org.jetbrains.annotations.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;

public class BlockUtils
{
	@Nullable
	public static BlockState getHoeTilledState(Level level, BlockPos pos, InteractionHand hand, ItemStack tool, boolean simulate)
	{
		return getToolModifiedState(level, pos, Direction.UP, hand, tool, ToolActions.HOE_TILL, simulate);
	}

	@Nullable
	public static BlockState getToolModifiedState(Level level, BlockPos pos, Direction direction, InteractionHand hand, ItemStack tool, ToolAction toolAction, boolean simulate)
	{
		var prev = level.getBlockState(pos);
		var context = new UseOnContext(level, null, hand, tool, new BlockHitResult(Vec3.atCenterOf(pos), direction, pos, simulate));
		return prev.getToolModifiedState(context, toolAction, false);
	}

	private BlockUtils()
	{

	}

}
