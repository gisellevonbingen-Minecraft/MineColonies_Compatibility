package steve_gall.minecolonies_compatibility.core.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import steve_gall.minecolonies_compatibility.core.common.block.entity.CommonNetworkStorageBlockEntity;
import steve_gall.minecolonies_compatibility.core.common.init.ModBlockEntities;
import steve_gall.minecolonies_compatibility.core.common.inventory.AccessDirectionHolderMenu;

public class CommonNetworkStorageBlock extends BaseEntityBlock
{
	public CommonNetworkStorageBlock(BlockBehaviour.Properties properites)
	{
		super(properites);
	}

	@Override
	public RenderShape getRenderShape(BlockState bs)
	{
		return RenderShape.MODEL;
	}

	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result)
	{
		AccessDirectionHolderMenu.open(world, pos, player);
		return InteractionResult.SUCCESS;
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
	{
		return new CommonNetworkStorageBlockEntity(pos, state);
	}

	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type)
	{
		return createTickerHelper(type, ModBlockEntities.COMMON_NETWORK_STORAGE.get(), CommonNetworkStorageBlockEntity::tick);
	}

}
