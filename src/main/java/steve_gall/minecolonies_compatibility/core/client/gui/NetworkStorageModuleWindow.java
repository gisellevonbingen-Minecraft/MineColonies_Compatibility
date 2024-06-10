package steve_gall.minecolonies_compatibility.core.client.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.jetbrains.annotations.NotNull;

import com.ldtteam.blockui.Pane;
import com.ldtteam.blockui.controls.Button;
import com.ldtteam.blockui.controls.ItemIcon;
import com.ldtteam.blockui.controls.Text;
import com.ldtteam.blockui.views.ScrollingList;
import com.minecolonies.api.util.constant.WindowConstants;
import com.minecolonies.core.client.gui.AbstractModuleWindow;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.core.common.building.module.INetworkStorageView;
import steve_gall.minecolonies_compatibility.core.common.building.module.NetworkStorageModule;
import steve_gall.minecolonies_compatibility.core.common.building.module.NetworkStorageModuleView;
import steve_gall.minecolonies_compatibility.core.common.network.message.NetworkStorageRefreshMessage;

public class NetworkStorageModuleWindow extends AbstractModuleWindow
{
	public static final Component TEXT_REFRESH = Component.translatable("minecolonies_compatibility.gui.refresh");
	public static final Component TEXT_REFRESHING = Component.translatable("minecolonies_compatibility.gui.refreshing");

	private static final String BUTTON_REFRESH = "refresh";

	protected final NetworkStorageModuleView module;
	protected final ScrollingList resourceList;

	private int refreshTicks;
	private int revision;
	private List<ViewCache> currentDisplayedList;

	public NetworkStorageModuleWindow(String res, NetworkStorageModuleView module)
	{
		super(module.getBuildingView(), res);

		this.module = module;
		this.resourceList = this.window.findPaneOfTypeByID(WindowConstants.LIST_RESOURCES, ScrollingList.class);

		this.window.findPaneOfTypeByID(WindowConstants.DESC_LABEL, Text.class).setText(Component.translatable(module.getDesc().toLowerCase(Locale.US)));
	}

	@Override
	public void onOpened()
	{
		super.onOpened();

		this.updateResources();
	}

	@Override
	public void onUpdate()
	{
		super.onUpdate();

		if (this.refreshTicks > 0)
		{
			--this.refreshTicks;

			if (this.refreshTicks % 20 == 0)
			{
				MineColoniesCompatibility.network().sendToServer(new NetworkStorageRefreshMessage(this.module));
			}

			if (this.refreshTicks == 0)
			{
				var button = this.findPaneOfTypeByID(BUTTON_REFRESH, Button.class);
				button.setEnabled(true);
				button.setText(TEXT_REFRESH);
			}

		}

		if (this.revision != this.module.getRevision())
		{
			this.updateResources();
		}

	}

	@Override
	public void onButtonClicked(@NotNull Button button)
	{
		super.onButtonClicked(button);

		if (button.getID().equals(BUTTON_REFRESH))
		{
			MineColoniesCompatibility.network().sendToServer(new NetworkStorageRefreshMessage(this.module));

			button.setEnabled(false);
			button.setText(TEXT_REFRESHING);
			this.refreshTicks = 80;
		}

	}

	protected void updateResources()
	{
		this.revision = this.module.getRevision();

		var level = module.getBuildingView().getColony().getWorld();
		this.currentDisplayedList = new ArrayList<>();

		for (var pos : this.getModule().getBlocks())
		{
			this.currentDisplayedList.add(new ViewCache(level, pos));
		}

		this.resourceList.enable();
		this.resourceList.show();
		this.resourceList.setDataProvider(new ScrollingList.DataProvider()
		{
			@Override
			public int getElementCount()
			{
				return currentDisplayedList.size();
			}

			@Override
			public void updateElement(int index, Pane rowPane)
			{
				var cache = currentDisplayedList.get(index);

				var icon = rowPane.findPaneOfTypeByID(WindowConstants.RESOURCE_ICON, ItemIcon.class);
				icon.setItem(cache.icon);

				var nameLabel = rowPane.findPaneOfTypeByID(WindowConstants.RESOURCE_NAME, Text.class);
				nameLabel.setText(cache.name);

				var posLabel = rowPane.findPaneOfTypeByID(WindowConstants.POS_LABEL, Text.class);
				posLabel.setText(cache.posText);
			}
		});
	}

	public NetworkStorageModuleView getModule()
	{
		return this.module;
	}

	public class ViewCache
	{
		public final BlockPos pos;
		public final Component posText;
		public final ItemStack icon;
		public final Component name;
		public final INetworkStorageView view;

		public ViewCache(Level level, BlockPos pos)
		{
			this.pos = pos;
			this.posText = Component.translatable("minecolonies_compatibility.gui.pos", pos.getX(), pos.getY(), pos.getZ());

			var state = level.getBlockState(pos);
			var mc = Minecraft.getInstance();
			this.icon = state.getCloneItemStack(mc.hitResult, level, pos, mc.player);
			this.name = this.icon.getHoverName();

			BlockEntity blockEntity = level.getBlockEntity(pos);
			this.view = NetworkStorageModule.resolveView(blockEntity);
		}

	}

}
