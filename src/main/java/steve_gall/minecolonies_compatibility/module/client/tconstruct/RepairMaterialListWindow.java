package steve_gall.minecolonies_compatibility.module.client.tconstruct;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.function.Predicate;

import com.ldtteam.blockui.Pane;
import com.ldtteam.blockui.controls.Button;
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
import slimeknights.tconstruct.library.client.materials.MaterialTooltipCache;
import slimeknights.tconstruct.library.materials.definition.MaterialId;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.core.common.util.StreamUtils;
import steve_gall.minecolonies_compatibility.module.common.tconstruct.building.module.RepairMaterialListModule;
import steve_gall.minecolonies_compatibility.module.common.tconstruct.network.RepairMaterialOpenTeachMessage;

public class RepairMaterialListWindow extends AbstractModuleWindow<RepairMaterialListModule.View>
{
	public static final Component TEXT_REMOVE = Component.translatable("com.minecolonies.coremod.gui.recipe.remove");

	protected final ScrollingList resourceList;

	private int updateCounter = -1;
	private List<MaterialCache> currentDisplayedList = Collections.emptyList();

	private String filter = "";
	private int tick = 0;

	private Button confirmButton;

	public RepairMaterialListWindow(RepairMaterialListModule.View module, ResourceLocation res)
	{
		super(module, res);

		this.resourceList = this.window.findPaneOfTypeByID(WindowConstants.LIST_RESOURCES, ScrollingList.class);

		this.window.findPaneOfTypeByID(WindowConstants.DESC_LABEL, Text.class).setText(Component.translatable("com.minecolonies.coremod.gui.workerhuts.tconstruct_repair_materials.menu"));
		this.window.findPaneOfTypeByID(WindowConstants.INPUT_FILTER, TextField.class).setHandler(input ->
		{
			this.setFilter(input.getText());
		});
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
	public void onButtonClicked(Button button)
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
			MineColoniesCompatibility.network().sendToServer(new RepairMaterialOpenTeachMessage(this.moduleView));
		}

		var idx = this.resourceList.getListElementIndexByPane(button);

		if (idx > -1)
		{
			if (confirmButton == button)
			{
				var cache = this.currentDisplayedList.get(idx);
				this.moduleView.remove(cache.materialId);
				this.updateResources();
			}
			else
			{
				this.confirmButton = button;
				this.confirmButton.setText(Component.translatable("minecolonies_tweaks.gui.surely").withStyle(ChatFormatting.RED));
			}

		}

	}

	private boolean testFilter(MaterialCache item, String lowerCaseFilter)
	{
		return item.materialId.getPath().contains(lowerCaseFilter);
	}

	protected void updateResources()
	{
		var lowerCase = this.filter.toLowerCase(Locale.US);
		Predicate<MaterialCache> filterPredicate = this.filter.isEmpty() ? (item -> true) : (item -> this.testFilter(item, lowerCase));

		this.currentDisplayedList = new ArrayList<>();

		for (var tuple : StreamUtils.toIterable(this.moduleView.stream()))
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
		return o1.materialId.toString().compareTo(o2.materialId.toString());
	}

	public class MaterialCache
	{
		public final MaterialId materialId;
		public final Component materialName;
		public final ItemStack item;
		public final Component itemName;

		public MaterialCache(MaterialId materialId, ItemStack item)
		{
			this.materialId = materialId;
			this.materialName = MaterialTooltipCache.getDisplayName(materialId);
			this.item = item;
			this.itemName = item.getHoverName();
		}

	}

}
