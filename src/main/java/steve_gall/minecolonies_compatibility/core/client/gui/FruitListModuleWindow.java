package steve_gall.minecolonies_compatibility.core.client.gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;

import org.jetbrains.annotations.NotNull;

import com.ldtteam.blockui.Pane;
import com.ldtteam.blockui.controls.Button;
import com.ldtteam.blockui.controls.ItemIcon;
import com.ldtteam.blockui.controls.Text;
import com.ldtteam.blockui.controls.TextField;
import com.ldtteam.blockui.views.ScrollingList;
import com.minecolonies.api.util.constant.WindowConstants;
import com.minecolonies.core.client.gui.AbstractModuleWindow;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import steve_gall.minecolonies_compatibility.api.common.plant.CustomizedFruit;
import steve_gall.minecolonies_compatibility.api.common.plant.FruitIconCache;
import steve_gall.minecolonies_compatibility.core.common.building.module.FruitListModuleView;

public class FruitListModuleWindow extends AbstractModuleWindow
{
	public static final String INPUT_ICON = "inputIcon";
	public static final String OUTPUT_ICON = "outputIcon";
	public static final String OUTPUT_NAME = "outputName";
	public static final Component ON = Component.translatable(WindowConstants.ON);
	public static final Component OFF = Component.translatable(WindowConstants.OFF);

	protected final FruitListModuleView module;
	protected final ScrollingList resourceList;

	private final List<FruitIconCache> groupedItemList;
	private final Map<ItemStack, String> descriptionCache;
	private final Map<ItemStack, String> hoverNameCache;

	private String filter = "";
	private int tick = 0;
	private int tickCounter = 0;
	private List<FruitIconCache> currentDisplayedList;

	public FruitListModuleWindow(String res, FruitListModuleView module)
	{
		super(module.getBuildingView(), res);

		this.module = module;
		this.resourceList = this.window.findPaneOfTypeByID(WindowConstants.LIST_RESOURCES, ScrollingList.class);

		this.groupedItemList = new ArrayList<>(CustomizedFruit.getRegistry().values().stream().filter(module.getDisplayPredicate()::test).map(FruitIconCache::new).toList());
		this.descriptionCache = new HashMap<>();
		this.hoverNameCache = new HashMap<>();

		this.window.findPaneOfTypeByID(WindowConstants.DESC_LABEL, Text.class).setText(Component.translatable(module.getDesc().toLowerCase(Locale.US)));
		this.window.findPaneOfTypeByID(WindowConstants.INPUT_FILTER, TextField.class).setHandler(input ->
		{
			this.setFilter(input.getText());
		});
	}

	@Override
	public void onButtonClicked(@NotNull Button button)
	{
		super.onButtonClicked(button);

		var buttonId = button.getID();

		if (Objects.equals(buttonId, WindowConstants.BUTTON_SWITCH))
		{
			var row = this.resourceList.getListElementIndexByPane(button);
			var fruit = this.currentDisplayedList.get(row);
			this.toggleFruits(Arrays.asList(fruit));
		}
		else if (Objects.equals(buttonId, WindowConstants.BUTTON_RESET_DEFAULT))
		{
			this.clearFruits();
		}
		else if (Objects.equals(buttonId, "toggleInCurrent"))
		{
			this.toggleFruits(this.currentDisplayedList);
		}
		else if (Objects.equals(buttonId, "resetInCurrent"))
		{
			this.removeFruits(this.currentDisplayedList);
		}

	}

	public void addFruits(Collection<FruitIconCache> fruits)
	{
		this.module.addIds(fruits.stream().map(fruit -> fruit.getFruit().getId()).toList());
		this.resourceList.refreshElementPanes();
	}

	public void removeFruits(Collection<FruitIconCache> fruits)
	{
		this.module.removeIds(fruits.stream().map(fruit -> fruit.getFruit().getId()).toList());
		this.resourceList.refreshElementPanes();
	}

	public void toggleFruits(Collection<FruitIconCache> fruits)
	{
		var toRemoves = fruits.stream().map(fruit -> fruit.getFruit().getId()).filter(id -> this.module.containsId(id)).toList();
		var toAddes = fruits.stream().map(fruit -> fruit.getFruit().getId()).filter(id -> !this.module.containsId(id)).toList();

		this.module.removeIds(toRemoves);
		this.module.addIds(toAddes);
		this.resourceList.refreshElementPanes();
	}

	public void clearFruits()
	{
		this.module.clearIds();
		this.resourceList.refreshElementPanes();
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

		if (this.tick > 0 && --this.tick == 0)
		{
			this.updateResources();
		}

		if (!Screen.hasShiftDown())
		{
			this.tickCounter++;
		}

	}

	private boolean testFilter(ItemStack stack, String lowerCaseFilter)
	{
		return this.descriptionCache.computeIfAbsent(stack, s -> s.getDescriptionId().toLowerCase(Locale.US)).contains(lowerCaseFilter) || //
				this.hoverNameCache.computeIfAbsent(stack, s -> s.getHoverName().getString().toLowerCase(Locale.US)).contains(lowerCaseFilter);
	}

	private boolean testFilter(Collection<ItemStack> stacks, String lowerCaseFilter)
	{
		return stacks.stream().anyMatch(stack -> this.testFilter(stack, lowerCaseFilter));
	}

	private boolean testFilter(FruitIconCache fruit, String lowerCaseFilter)
	{
		return this.testFilter(fruit.getBlockIcons(), lowerCaseFilter) || this.testFilter(fruit.getItemIcons(), lowerCaseFilter);
	}

	protected void updateResources()
	{
		var lowerCase = this.filter.toLowerCase(Locale.US);
		Predicate<FruitIconCache> filterPredicate = this.filter.isEmpty() ? (fruit -> true) : (fruit -> this.testFilter(fruit, lowerCase));

		if (this.currentDisplayedList != null)
		{
			this.currentDisplayedList.clear();
			this.updateResourceList();
		}

		this.currentDisplayedList = new ArrayList<>();
		this.groupedItemList.stream().filter(filterPredicate).forEach(this.currentDisplayedList::add);
		this.currentDisplayedList.sort(this::compareResources);

		this.updateResourceList();
	}

	protected int compareResources(FruitIconCache fruit1, FruitIconCache fruit2)
	{
		var isInverted = this.module.isInverted();
		var contains1 = this.module.containsId(fruit1.getFruit().getId());
		var contains2 = this.module.containsId(fruit2.getFruit().getId());

		if (isInverted)
		{
			return Boolean.compare(contains1, contains2);
		}
		else
		{
			return Boolean.compare(contains2, contains1);
		}

	}

	/**
	 * Updates the resource list in the GUI with the info we need.
	 */
	private void updateResourceList()
	{
		this.resourceList.enable();
		this.resourceList.show();
		this.resourceList.setDataProvider(new ScrollingList.DataProvider()
		{
			@Override
			public int getElementCount()
			{
				return currentDisplayedList.size();
			}

			private ItemStack getIcon(List<ItemStack> stacks)
			{
				var size = stacks.size();

				if (size == 0)
				{
					return ItemStack.EMPTY;
				}

				var index = (tickCounter / WindowConstants.LIFE_COUNT_DIVIDER) % size;
				return stacks.get(index);
			}

			@Override
			public void updateElement(int index, Pane rowPane)
			{
				var fruit = currentDisplayedList.get(index);
				var input = this.getIcon(fruit.getBlockIcons());
				var output = this.getIcon(fruit.getItemIcons());

				var inputIcon = rowPane.findPaneOfTypeByID(INPUT_ICON, ItemIcon.class);
				inputIcon.setItem(input);

				var outputIcon = rowPane.findPaneOfTypeByID(OUTPUT_ICON, ItemIcon.class);
				outputIcon.setItem(output);

				var outputLabel = rowPane.findPaneOfTypeByID(OUTPUT_NAME, Text.class);
				outputLabel.setText(output.getHoverName());
				outputLabel.setColors(0x000000);

				var contains = module.containsId(fruit.getFruit().getId());
				var isInverted = module.isInverted();
				var switchButton = rowPane.findPaneOfTypeByID(WindowConstants.BUTTON_SWITCH, Button.class);
				var on = (isInverted && !contains) || (!isInverted && contains);
				switchButton.setText(on ? ON : OFF);
			}
		});
	}
}
