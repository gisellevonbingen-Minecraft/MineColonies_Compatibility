package steve_gall.minecolonies_compatibility.core.common.network.message;

import com.minecolonies.api.colony.buildings.modules.IBuildingModule;
import com.minecolonies.api.colony.buildings.modules.IBuildingModuleView;
import com.minecolonies.api.util.Utils;
import com.minecolonies.core.colony.buildings.moduleviews.CraftingModuleView;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import steve_gall.minecolonies_compatibility.api.common.building.module.IMenuBuildingModuleView;

public abstract class ModuleMenuOpenMessage extends BuildingModuleMessage
{
	protected final Component desc;

	public ModuleMenuOpenMessage(IBuildingModuleView module)
	{
		super(module);

		if (module instanceof IMenuBuildingModuleView menuBuilding)
		{
			this.desc = menuBuilding.getMenuDesc();
		}
		else if (module instanceof CraftingModuleView craftingModule)
		{
			@SuppressWarnings("deprecation")
			var id = craftingModule.getId();
			this.desc = Component.translatable("com.minecolonies.coremod.gui.workerhuts.recipe." + id + ".menu");
		}
		else
		{
			this.desc = module.getDesc();
		}

	}

	public ModuleMenuOpenMessage(RegistryFriendlyByteBuf buffer)
	{
		super(buffer);

		this.desc = Utils.deserializeCodecMess(ComponentSerialization.STREAM_CODEC, buffer);
	}

	@Override
	public void encode(RegistryFriendlyByteBuf buffer)
	{
		super.encode(buffer);

		Utils.serializeCodecMess(ComponentSerialization.STREAM_CODEC, buffer, this.desc);
	}

	@Override
	public void handle(IPayloadContext context)
	{
		super.handle(context);

		var player = context.player();
		var module = this.getModule();

		if (module == null)
		{
			return;
		}

		player.openMenu(new MenuProvider()
		{
			@Override
			public Component getDisplayName()
			{
				return ModuleMenuOpenMessage.this.getDisplayName();
			}

			@Override
			public AbstractContainerMenu createMenu(int windowId, Inventory inventory, Player player)
			{
				return ModuleMenuOpenMessage.this.createMenu(windowId, inventory, player, module);
			}

		}, buffer -> this.toBuffer(buffer, module));
	}

	protected Component getDisplayName()
	{
		return this.desc;
	}

	protected abstract AbstractContainerMenu createMenu(int windowId, Inventory inventory, Player player, IBuildingModule module);

	protected void toBuffer(RegistryFriendlyByteBuf buffer, IBuildingModule module)
	{
		this.getModulePos().serializeBuffer(buffer);
	}

}
