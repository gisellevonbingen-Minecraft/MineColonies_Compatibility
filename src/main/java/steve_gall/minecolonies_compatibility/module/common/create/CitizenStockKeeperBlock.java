package steve_gall.minecolonies_compatibility.module.common.create;

import com.mojang.serialization.MapCodec;
import com.simibubi.create.content.equipment.wrench.IWrenchable;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import steve_gall.minecolonies_compatibility.module.common.create.init.ModuleBlockEntities;

public class CitizenStockKeeperBlock extends BaseEntityBlock implements IWrenchable
{
	public static final MapCodec<CitizenStockKeeperBlock> CODEC = simpleCodec(CitizenStockKeeperBlock::new);

	@Override
	public MapCodec<CitizenStockKeeperBlock> codec()
	{
		return CODEC;
	}

	public CitizenStockKeeperBlock(BlockBehaviour.Properties properties)
	{
		super(properties);
	}

	@Override
	public RenderShape getRenderShape(BlockState bs)
	{
		return RenderShape.MODEL;
	}
	
	@Override
	public InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult result)
	{
		if (level.getBlockEntity(pos) instanceof CitizenStockKeeperBlockEntity blockEntity && player instanceof ServerPlayer serverPlayer)
		{
			serverPlayer.openMenu(new MenuProvider()
			{
				@Override
				public AbstractContainerMenu createMenu(int windowId, Inventory inventory, Player player)
				{
					return new CitizenStockKeeperMenu(windowId, inventory, blockEntity);
				}

				@Override
				public Component getDisplayName()
				{
					return blockEntity.getBlockState().getBlock().getName();
				}
			}, pos);
		}

		return InteractionResult.SUCCESS;
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
	{
		return new CitizenStockKeeperBlockEntity(pos, state);
	}

	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type)
	{
		return createTickerHelper(type, ModuleBlockEntities.CITIZEN_STOCK_KEEPER.get(), CitizenStockKeeperBlockEntity::tick);
	}

}
