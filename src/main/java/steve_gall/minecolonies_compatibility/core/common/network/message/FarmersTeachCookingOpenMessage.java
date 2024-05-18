package steve_gall.minecolonies_compatibility.core.common.network.message;

import org.jetbrains.annotations.NotNull;

import com.minecolonies.api.colony.buildings.modules.IBuildingModuleView;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import steve_gall.minecolonies_compatibility.module.common.farmersdelight.menu.TeachCookingMenu;

public class FarmersTeachCookingOpenMessage extends ModuleMenuOpenMessage
{
	public FarmersTeachCookingOpenMessage(IBuildingModuleView module)
	{
		super(module);
	}

	public FarmersTeachCookingOpenMessage(FriendlyByteBuf buffer)
	{
		super(buffer);
	}

	@Override
	public void encode(FriendlyByteBuf buffer)
	{
		super.encode(buffer);
	}

	@Override
	protected MenuProvider createMenuProvider()
	{
		return new MenuProvider()
		{
			@NotNull
			@Override
			public Component getDisplayName()
			{
				return Component.translatable("minecolonies_compatibility.gui.farmers_cooking");
			}

			@NotNull
			@Override
			public AbstractContainerMenu createMenu(int id, @NotNull Inventory inv, @NotNull Player player)
			{
				return new TeachCookingMenu(id, inv, getBuildingId(), getModuleId());
			}
		};
	}

	@Override
	protected void toBuffer(FriendlyByteBuf buffer)
	{
		super.toBuffer(buffer);
	}

}
