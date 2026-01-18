package steve_gall.minecolonies_compatibility.module.common.refinedstorage;

import java.util.Optional;
import java.util.function.Predicate;

import com.refinedmods.refinedstorage.api.storage.AccessMode;
import com.refinedmods.refinedstorage.common.storage.StoragePropertyTypes;
import com.refinedmods.refinedstorage.common.support.AbstractBaseContainerMenu;
import com.refinedmods.refinedstorage.common.support.RedstoneMode;
import com.refinedmods.refinedstorage.common.support.containermenu.ClientProperty;
import com.refinedmods.refinedstorage.common.support.containermenu.PropertyTypes;
import com.refinedmods.refinedstorage.common.support.containermenu.ServerProperty;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.DataSlot;
import steve_gall.minecolonies_compatibility.module.common.refinedstorage.init.ModuleMenuTypes;

public class CitizenGridContainerMenu extends AbstractBaseContainerMenu
{
	private CitizenGridBlockEntity grid;
	private final Predicate<Player> stillValid;
	private final int[] linkedPos = new int[4];

	public CitizenGridContainerMenu(int windowId, Inventory playerInventory, RegistryFriendlyByteBuf data)
	{
		super(ModuleMenuTypes.CITIZEN_GRID.get(), windowId);

		this.grid = null;
		this.addPlayerInventory(playerInventory, 8, 55);
		this.registerProperty(new ClientProperty<>(PropertyTypes.REDSTONE_MODE, RedstoneMode.IGNORE));
		this.registerProperty(new ClientProperty<>(StoragePropertyTypes.ACCESS_MODE, AccessMode.INSERT_EXTRACT));
		this.stillValid = p -> true;

		this.addDataSlot(DataSlot.shared(this.linkedPos, 0));
		this.addDataSlot(DataSlot.shared(this.linkedPos, 1));
		this.addDataSlot(DataSlot.shared(this.linkedPos, 2));
		this.addDataSlot(DataSlot.shared(this.linkedPos, 3));
	}

	public CitizenGridContainerMenu(CitizenGridBlockEntity grid, Player player, int windowId)
	{
		super(ModuleMenuTypes.CITIZEN_GRID.get(), windowId);

		this.grid = grid;
		this.addPlayerInventory(player.getInventory(), 8, 55);
		this.registerProperty(new ServerProperty<>(PropertyTypes.REDSTONE_MODE, grid::getRedstoneMode, grid::setRedstoneMode));
		this.registerProperty(new ServerProperty<>(StoragePropertyTypes.ACCESS_MODE, grid::getAccessMode, grid::setAccessMode));
		this.stillValid = p -> Container.stillValidBlockEntity(grid, p);

		this.addDataSlot(DataSlot.shared(this.linkedPos, 0));
		this.addDataSlot(DataSlot.shared(this.linkedPos, 1));
		this.addDataSlot(DataSlot.shared(this.linkedPos, 2));
		this.addDataSlot(DataSlot.shared(this.linkedPos, 3));

	}

	@Override
	public void broadcastChanges()
	{
		super.broadcastChanges();

		if (this.grid != null)
		{
			var posWrapper = this.grid.getNetworkStorageView().getLinkedPos();
			this.linkedPos[0] = posWrapper.isPresent() ? 1 : 0;

			if (posWrapper.isPresent())
			{
				var pos = posWrapper.get();
				this.linkedPos[1] = pos.getX();
				this.linkedPos[2] = pos.getY();
				this.linkedPos[3] = pos.getZ();
			}

		}

	}

	public Optional<BlockPos> getLinkedPos()
	{
		if (this.linkedPos[0] > 0)
		{
			return Optional.of(new BlockPos(this.linkedPos[1], this.linkedPos[2], this.linkedPos[3]));
		}
		else
		{
			return Optional.empty();
		}

	}

	@Override
	public boolean stillValid(Player player)
	{
		return stillValid.test(player);
	}

}
