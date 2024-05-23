package steve_gall.minecolonies_compatibility.core.common.building.module;

import java.util.function.Predicate;

import org.jetbrains.annotations.NotNull;

import com.ldtteam.blockui.views.BOWindow;

import steve_gall.minecolonies_compatibility.api.common.plant.CustomizedFruit;
import steve_gall.minecolonies_compatibility.core.client.gui.FruitListModuleWindow;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_tweaks.api.common.building.module.AbstractIdListModuleView;

public class FruitListModuleView extends AbstractIdListModuleView
{
	private final String icon;
	private final String desc;
	private final boolean inverted;
	private final Predicate<CustomizedFruit> displayPredicate;

	public FruitListModuleView(String icon, String desc, boolean inverted, Predicate<CustomizedFruit> displayPredicate)
	{
		this.icon = icon;
		this.desc = desc;
		this.inverted = inverted;
		this.displayPredicate = displayPredicate;
	}

	@Override
	public BOWindow getWindow()
	{
		return new FruitListModuleWindow(MineColoniesCompatibility.rl("gui/layouthuts/layoutfilterablefruitlist.xml").toString(), this);
	}

	@Override
	public String getIcon()
	{
		return this.icon;
	}

	@Override
	public String getDesc()
	{
		return this.desc;
	}

	public boolean isInverted()
	{
		return this.inverted;
	}

	public @NotNull Predicate<CustomizedFruit> getDisplayPredicate()
	{
		return this.displayPredicate;
	}

}
