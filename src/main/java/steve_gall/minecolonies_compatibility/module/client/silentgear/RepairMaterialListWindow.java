package steve_gall.minecolonies_compatibility.module.client.silentgear;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.ldtteam.blockui.Pane;
import com.ldtteam.blockui.controls.Button;
import com.ldtteam.blockui.controls.ButtonImage;
import com.ldtteam.blockui.controls.ItemIcon;
import com.ldtteam.blockui.controls.Text;
import com.ldtteam.blockui.views.ScrollingList;
import com.minecolonies.api.util.constant.Constants;
import com.minecolonies.api.util.constant.WindowConstants;
import com.minecolonies.core.client.gui.AbstractModuleWindow;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.silentchaos512.gear.gear.material.MaterialInstance;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.module.common.silentgear.building.module.RepairMaterialListModule;
import steve_gall.minecolonies_compatibility.module.common.silentgear.network.RepairKitOpenInventoryMessage;
import steve_gall.minecolonies_compatibility.module.common.silentgear.network.RepairMaterialOpenTeachMessage;

public class RepairMaterialListWindow extends AbstractModuleWindow
{
	public static final Component TEXT_REMOVE = Component.translatable("com.minecolonies.coremod.gui.recipe.remove");
	public static final Component TEXT_SURELY = Component.translatable("minecolonies_tweaks.gui.surely").withStyle(ChatFormatting.RED);

	public static final String TEXTURE_ASSIGN_ON_NORMAL = "minecolonies:textures/gui/builderhut/builder_button_mini_check.png";
	public static final String TEXTURE_ASSIGN_ON_DISABLED = "minecolonies:textures/gui/builderhut/builder_button_mini_disabled_check.png";
	public static final String TEXTURE_ASSIGN_OFF_NORMAL = "minecolonies:textures/gui/builderhut/builder_button_mini.png";
	public static final String TEXTURE_ASSIGN_OFF_DISABLED = "minecolonies:textures/gui/builderhut/builder_button_mini_disabled.png";

	protected final RepairMaterialListModule.View moduleView;
	protected final ScrollingList resourceList;
	protected final ButtonImage inventoryButton;

	private List<MaterialCache> currentDisplayedList;
	private Button confirmButton;

	private boolean canUseHigher = false;

	public RepairMaterialListWindow(RepairMaterialListModule.View module, String res)
	{
		super(module.getBuildingView(), res);

		this.moduleView = module;
		this.resourceList = this.window.findPaneOfTypeByID(WindowConstants.LIST_RESOURCES, ScrollingList.class);
		this.window.findPaneOfTypeByID(WindowConstants.DESC_LABEL, Text.class).setText(Component.translatable("com.minecolonies.coremod.gui.workerhuts.tconstruct_repair_materials.menu"));

		var button = this.inventoryButton = new ButtonImage();
		button.setID(MineColoniesCompatibility.rl("silent_gear_repair_kit_inventory").toString());
		button.setColors(0xFF000000, 0xFF000000, 0xFF000000);
		button.setImage(new ResourceLocation("minecolonies:textures/gui/builderhut/builder_button_medium_large.png"), false);
		button.setText(Component.translatable("minecolonies_compatibility.text.silentgear_repair_kit_inventory"));
		button.setSize(129, 17);
		button.setTextSize(129, 17);
		button.setPosition(30, 217);
		this.addChild(button);
	}

	@Override
	public void onOpened()
	{
		super.onOpened();

		this.updateResources();
		this.updateCanUseHigher(true);
	}

	@Override
	public void onUpdate()
	{
		super.onUpdate();

		this.updateResources();
		this.updateCanUseHigher(false);
	}

	private void updateCanUseHigher(boolean force)
	{
		var canUseHigher = this.moduleView.isCanUseHigher();

		if (!force && this.canUseHigher == canUseHigher)
		{
			return;
		}

		this.canUseHigher = canUseHigher;
		var button = this.findPaneOfTypeByID("canusehigher", ButtonImage.class);

		if (canUseHigher)
		{
			button.setImage(new ResourceLocation(TEXTURE_ASSIGN_ON_NORMAL), true);
			button.setImageDisabled(new ResourceLocation(TEXTURE_ASSIGN_ON_DISABLED), true);
		}
		else
		{
			button.setImage(new ResourceLocation(TEXTURE_ASSIGN_OFF_NORMAL), true);
			button.setImageDisabled(new ResourceLocation(TEXTURE_ASSIGN_OFF_DISABLED), true);
		}

	}

	@Override
	public void onButtonClicked(@NotNull Button button)
	{
		super.onButtonClicked(button);

		var confirmButton = this.confirmButton;

		if (confirmButton != null)
		{
			confirmButton.setText(TEXT_REMOVE);
			this.confirmButton = null;
		}

		if (button.getID().equals("canusehigher"))
		{
			this.moduleView.setCanUseHigher(!this.canUseHigher);
		}
		else if (button.getID().equals("crafting"))
		{
			MineColoniesCompatibility.network().sendToServer(new RepairMaterialOpenTeachMessage(this.moduleView));
		}
		else if (button == this.inventoryButton)
		{
			MineColoniesCompatibility.network().sendToServer(new RepairKitOpenInventoryMessage(this.moduleView));
		}

		var idx = this.resourceList.getListElementIndexByPane(button);

		if (idx > -1)
		{
			if (confirmButton == button)
			{
				var cache = this.currentDisplayedList.get(idx);
				this.moduleView.removeRepairMaterial(cache.item);
				this.updateResources();
			}
			else
			{
				this.confirmButton = button;
				this.confirmButton.setText(TEXT_SURELY);
			}

		}

	}

	protected void updateResources()
	{
		var module = this.moduleView;

		this.currentDisplayedList = new ArrayList<>();

		for (var i = 0; i <= Constants.MAX_BUILDING_LEVEL; i++)
		{
			var item = module.getRepairMaterial(i);

			if (!item.isEmpty())
			{
				this.currentDisplayedList.add(new MaterialCache(item));
			}

		}

		this.currentDisplayedList.sort(this::compareMaterials);

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

				var icon = rowPane.findPaneOfTypeByID("outputIcon", ItemIcon.class);
				icon.setItem(cache.item);

				var tierLabel = rowPane.findPaneOfTypeByID("tierText", Text.class);
				tierLabel.setText(cache.tierText);

				var nameLabel = rowPane.findPaneOfTypeByID("outputName", Text.class);
				nameLabel.setText(cache.itemName);
			}
		});
	}

	public int compareMaterials(MaterialCache o1, MaterialCache o2)
	{
		return Integer.compare(o2.tier, o1.tier);
	}

	public class MaterialCache
	{
		public final int tier;
		public final Component tierText;
		public final ItemStack item;
		public final Component itemName;

		public MaterialCache(ItemStack item)
		{
			var instance = MaterialInstance.from(item);
			this.tier = instance.getTier();
			this.tierText = Component.translatable("misc.silentgear.tier", this.tier);
			this.item = item;
			this.itemName = item.getHoverName();
		}

	}

}
