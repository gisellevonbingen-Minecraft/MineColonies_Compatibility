package steve_gall.minecolonies_compatibility.core.common.inventory;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkHooks;
import steve_gall.minecolonies_compatibility.core.common.block.entity.IAccessDirectionHolder;
import steve_gall.minecolonies_compatibility.core.common.block.entity.INetworkStorageViewHolder;
import steve_gall.minecolonies_compatibility.core.common.init.ModMenuTypes;

public class AccessDirectionHolderMenu<BLOCK_ENTITY extends BlockEntity & INetworkStorageViewHolder & IAccessDirectionHolder> extends BaseMenu
{
	@SuppressWarnings("unchecked")
	public static <BLOCK_ENTITY extends BlockEntity & INetworkStorageViewHolder & IAccessDirectionHolder> void open(Level level, BlockPos pos, Player player)
	{
		if (level.isClientSide())
		{
			return;
		}

		var blockEntity = (BLOCK_ENTITY) level.getBlockEntity(pos);

		if (player instanceof ServerPlayer serverPlayer)
		{
			NetworkHooks.openScreen(serverPlayer, new MenuProvider()
			{
				@Override
				public AbstractContainerMenu createMenu(int windowId, Inventory inventory, Player player)
				{
					return new AccessDirectionHolderMenu<>(windowId, inventory, blockEntity);
				}

				@Override
				public Component getDisplayName()
				{
					return blockEntity.getBlockState().getBlock().getName();
				}
			}, pos);
		}

	}

	private final BLOCK_ENTITY blockEntity;

	public AccessDirectionHolderMenu(int windowId, Inventory inventory, BLOCK_ENTITY blockEntity)
	{
		super(ModMenuTypes.ACCESS_DIRECTION_HOLDER.get(), windowId, inventory);
		this.blockEntity = blockEntity;

		this.setup();
	}

	@SuppressWarnings("unchecked")
	public AccessDirectionHolderMenu(int windowId, Inventory inventory, FriendlyByteBuf data)
	{
		super(ModMenuTypes.ACCESS_DIRECTION_HOLDER.get(), windowId, inventory);

		var pos = data.readBlockPos();
		this.blockEntity = (BLOCK_ENTITY) inventory.player.level.getBlockEntity(pos);

		this.setup();
	}

	private void setup()
	{
		this.addInventorySlots(8, 55);
	}

	public BLOCK_ENTITY getBlockEntity()
	{
		return this.blockEntity;
	}

	@Override
	public ItemStack quickMoveStack(Player p_38941_, int p_38942_)
	{
		return ItemStack.EMPTY;
	}

	@Override
	public boolean stillValid(Player p_38874_)
	{
		return true;
	}

}
