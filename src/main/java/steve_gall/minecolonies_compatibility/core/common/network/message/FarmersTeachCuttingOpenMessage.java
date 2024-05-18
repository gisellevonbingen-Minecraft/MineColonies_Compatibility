package steve_gall.minecolonies_compatibility.core.common.network.message;

import org.jetbrains.annotations.NotNull;

import com.minecolonies.api.colony.buildings.modules.IBuildingModuleView;
import com.minecolonies.api.util.constant.IToolType;
import com.minecolonies.api.util.constant.ToolType;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import steve_gall.minecolonies_compatibility.module.common.farmersdelight.menu.TeachCuttingMenu;

public class FarmersTeachCuttingOpenMessage extends ModuleMenuOpenMessage
{
	private final IToolType toolType;

	public FarmersTeachCuttingOpenMessage(IBuildingModuleView module, IToolType toolType)
	{
		super(module);

		this.toolType = toolType;
	}

	public FarmersTeachCuttingOpenMessage(FriendlyByteBuf buffer)
	{
		super(buffer);

		this.toolType = ToolType.getToolType(buffer.readUtf());
	}

	@Override
	public void encode(FriendlyByteBuf buffer)
	{
		super.encode(buffer);

		buffer.writeUtf(this.toolType.getName());
	}

	@Override
	protected MenuProvider createMenuProvider()
	{
		var toolType = this.getToolType();
		return new MenuProvider()
		{
			@NotNull
			@Override
			public Component getDisplayName()
			{
				return Component.translatable("minecolonies_compatibility.gui.farmers_cutting_" + toolType.getName());
			}

			@NotNull
			@Override
			public AbstractContainerMenu createMenu(int id, @NotNull Inventory inv, @NotNull Player player)
			{
				return new TeachCuttingMenu(id, inv, getBuildingId(), getModuleId(), toolType);
			}
		};
	}

	@Override
	protected void toBuffer(FriendlyByteBuf buffer)
	{
		super.toBuffer(buffer);

		buffer.writeUtf(this.getToolType().getName());
	}

	public IToolType getToolType()
	{
		return this.toolType;
	}

}
