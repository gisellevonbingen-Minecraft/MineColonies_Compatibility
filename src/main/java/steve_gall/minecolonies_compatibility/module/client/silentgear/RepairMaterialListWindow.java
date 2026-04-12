package steve_gall.minecolonies_compatibility.module.client.silentgear;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Predicate;

import org.jetbrains.annotations.NotNull;

import com.ldtteam.blockui.Pane;
import com.ldtteam.blockui.controls.Button;
import com.ldtteam.blockui.controls.ButtonImage;
import com.ldtteam.blockui.controls.ItemIcon;
import com.ldtteam.blockui.controls.Text;
import com.ldtteam.blockui.controls.TextField;
import com.ldtteam.blockui.views.ScrollingList;
import com.minecolonies.api.util.constant.WindowConstants;
import com.minecolonies.core.client.gui.AbstractModuleWindow;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;
import net.silentchaos512.gear.gear.material.MaterialInstance;
import net.silentchaos512.gear.setup.gear.PartTypes;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.core.common.util.StreamUtils;
import steve_gall.minecolonies_compatibility.module.common.silentgear.building.module.RepairMaterialListModule;
import steve_gall.minecolonies_compatibility.module.common.silentgear.network.RepairKitOpenInventoryMessage;
import steve_gall.minecolonies_compatibility.module.common.silentgear.network.RepairMaterialOpenTeachMessage;

public class RepairMaterialListWindow extends AbstractModuleWindow<RepairMaterialListModule.View>
{
	public static final Component TEXT_REMOVE = Component.translatable("com.minecolonies.coremod.gui.recipe.remove");
	public static final Component TEXT_SURELY = Component.translatable("minecolonies_tweaks.gui.surely").withStyle(ChatFormatting.RED);

	public static final String TEXTURE_ASSIGN_ON_NORMAL = "minecolonies:textures/gui/builderhut/builder_button_mini_check.png";
	public static final String TEXTURE_ASSIGN_ON_DISABLED = "minecolonies:textures/gui/builderhut/builder_button_mini_disabled_check.png";
	public static final String TEXTURE_ASSIGN_OFF_NORMAL = "minecolonies:textures/gui/builderhut/builder_button_mini.png";
	public static final String TEXTURE_ASSIGN_OFF_DISABLED = "minecolonies:textures/gui/builderhut/builder_button_mini_disabled.png";

	protected final ScrollingList resourceList;
	protected final ButtonImage inventoryButton;

	private int updateCounter = -1;
	private List<MaterialCache> currentDisplayedList;

	private String filter = "";
	private int tick = 0;

	private Button confirmButton;

	public RepairMaterialListWindow(RepairMaterialListModule.View module, ResourceLocation res)
	{
		super(module, res);

		this.resourceList = this.window.findPaneOfTypeByID(WindowConstants.LIST_RESOURCES, ScrollingList.class);
		this.window.findPaneOfTypeByID(WindowConstants.INPUT_FILTER, TextField.class).setHandler(input ->
		{
			this.setFilter(input.getText());
		});

		var button = this.inventoryButton = new ButtonImage();
		button.setID(MineColoniesCompatibility.rl("silent_gear_repair_kit_inventory").toString());
		button.setColors(0xFF000000, 0xFF000000, 0xFF000000);
		button.setImage(ResourceLocation.parse("minecolonies:textures/gui/builderhut/builder_button_medium_large.png"));
		button.setText(Component.translatable("minecolonies_compatibility.text.silentgear_repair_kit_inventory"));
		button.setSize(129, 17);
		button.setTextSize(129, 17);
		button.setPosition(30, 217);
		this.addChild(button);
	}

	public void setFilter(String newFilter)
	{
		if (!newFilter.equals(this.filter))
		{
			this.filter = newFilter;
			this.tick = 10;
		}

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

		var updateCounter = this.moduleView.getUpdateCounter();

		if (this.updateCounter != updateCounter || this.tick > 0 && --this.tick == 0)
		{
			this.updateCounter = updateCounter;
			this.tick = 0;
			this.updateResources();
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

		if (button.getID().equals("crafting"))
		{
			PacketDistributor.sendToServer(new RepairMaterialOpenTeachMessage(this.moduleView));
		}
		else if (button == this.inventoryButton)
		{
			PacketDistributor.sendToServer(new RepairKitOpenInventoryMessage(this.moduleView));
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

	private boolean testFilter(MaterialCache item, String lowerCaseFilter)
	{
		return item.materialInstance.getId().getPath().contains(lowerCaseFilter);
	}

	protected void updateResources()
	{
		var lowerCase = this.filter.toLowerCase(Locale.US);
		Predicate<MaterialCache> filterPredicate = this.filter.isEmpty() ? (item -> true) : (item -> this.testFilter(item, lowerCase));

		this.currentDisplayedList = new ArrayList<>();

		for (var tuple : StreamUtils.toIterable(this.moduleView.streamRepairMaterial()))
		{
			var cache = new MaterialCache(tuple.getA(), tuple.getB());

			if (filterPredicate.test(cache))
			{
				this.currentDisplayedList.add(cache);
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

				var materialName = rowPane.findPaneOfTypeByID("materialName", Text.class);
				materialName.setText(cache.materialName);

				var itemName = rowPane.findPaneOfTypeByID("itemName", Text.class);
				itemName.setText(cache.itemName);
			}
		});
	}

	public int compareMaterials(MaterialCache o1, MaterialCache o2)
	{
		return o1.materialInstance.getId().compareTo(o2.materialInstance.getId());
	}

	public class MaterialCache
	{
		public final MaterialInstance materialInstance;
		public final Component materialName;
		public final ItemStack item;
		public final Component itemName;

		public MaterialCache(MaterialInstance material, ItemStack item)
		{
			this.materialInstance = material;
			this.materialName = material.getDisplayName(PartTypes.MAIN.get());
			this.item = item;
			this.itemName = item.getHoverName();
		}

	}

}
