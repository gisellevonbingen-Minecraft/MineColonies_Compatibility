package steve_gall.minecolonies_compatibility.module.common.functionalstorage;

import com.hrznstudio.titanium.block.BasicTileBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType.BlockEntitySupplier;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import steve_gall.minecolonies_compatibility.core.common.inventory.AccessDirectionHolderMenu;
import steve_gall.minecolonies_compatibility.module.common.functionalstorage.init.ModuleBlockEntities;

public class CitizenExtensionBlock extends BasicTileBlock<CitizenExtensionTile>
{
	public CitizenExtensionBlock(String name)
	{
		super(name, Properties.copy(Blocks.IRON_BLOCK), CitizenExtensionTile.class);
	}

	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result)
	{
		AccessDirectionHolderMenu.open(world, pos, player);
		return InteractionResult.SUCCESS;
	}

	@Override
	public BlockEntitySupplier<CitizenExtensionTile> getTileEntityFactory()
	{
		return (pos, state) -> new CitizenExtensionTile(this, ModuleBlockEntities.CITIZEN_EXTENSION.get(), pos, state);
	}

}
