package steve_gall.minecolonies_compatibility.module.common.storagedrawers;

import com.jaquadro.minecraft.storagedrawers.api.storage.INetworked;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import steve_gall.minecolonies_compatibility.core.common.inventory.AccessDirectionHolderMenu;
import steve_gall.minecolonies_compatibility.module.common.storagedrawers.init.ModuleBlockEntities;

public class CitizenSlaveBlock extends BaseEntityBlock implements INetworked
{
	public CitizenSlaveBlock()
	{
		super(Properties.of(Material.STONE).sound(SoundType.STONE).strength(4.0F, 5.0F));
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
		return new CitizenSlaveTile(pos, state);
	}

	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type)
	{
		return createTickerHelper(type, ModuleBlockEntities.CITIZEN_SLAVE.get(), CitizenSlaveTile::tick);
	}

}
