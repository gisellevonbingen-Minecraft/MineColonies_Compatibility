package steve_gall.minecolonies_compatibility.core.common.block;

import org.jetbrains.annotations.Nullable;

import com.minecolonies.api.colony.IColony;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;
import steve_gall.minecolonies_compatibility.core.common.colony.ColonyHelper;

public class BlockUtils
{
	@Nullable
	public static BlockState getHoeTilledState(@Nullable IColony colony, Level level, BlockPos pos, InteractionHand hand, ItemStack tool, boolean simulate)
	{
		return getToolModifiedState(colony, level, pos, Direction.UP, hand, tool, ItemAbilities.HOE_TILL, simulate);
	}

	@Nullable
	public static BlockState getToolModifiedState(@Nullable IColony colony, Level level, BlockPos pos, Direction direction, InteractionHand hand, ItemStack tool, ItemAbility itemAbility, boolean simulate)
	{
		Player player = null;

		if (colony != null && level instanceof ServerLevel serverLevel)
		{
			var fakeOwner = ColonyHelper.getFakeOwner(colony, serverLevel);
			fakeOwner.setItemInHand(hand, tool.copy());
			player = fakeOwner;
		}

		var prev = level.getBlockState(pos);
		var context = new UseOnContext(level, player, hand, tool, new BlockHitResult(Vec3.atCenterOf(pos), direction, pos, simulate));
		return prev.getToolModifiedState(context, itemAbility, false);
	}

	private BlockUtils()
	{

	}

}
