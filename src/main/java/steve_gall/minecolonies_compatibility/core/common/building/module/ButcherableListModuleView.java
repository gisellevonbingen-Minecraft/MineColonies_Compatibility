package steve_gall.minecolonies_compatibility.core.common.building.module;

import java.util.function.Predicate;

import org.jetbrains.annotations.NotNull;

import com.ldtteam.blockui.views.BOWindow;

import steve_gall.minecolonies_compatibility.api.common.butcher.CustomizedButcherable;
import steve_gall.minecolonies_compatibility.core.client.gui.ButcherableListModuleWindow;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_tweaks.api.common.building.module.AbstractIdListModuleView;

public class ButcherableListModuleView extends AbstractIdListModuleView
{
	private final String icon;
	private final String desc;
	private final boolean inverted;
	private final Predicate<CustomizedButcherable> displayPredicate;

	public ButcherableListModuleView(String icon, String desc, boolean inverted, Predicate<CustomizedButcherable> displayPredicate)
	{
		this.icon = icon;
		this.desc = desc;
		this.inverted = inverted;
		this.displayPredicate = displayPredicate;
	}

	@Override
	public BOWindow getWindow()
	{
		return new ButcherableListModuleWindow(MineColoniesCompatibility.rl("gui/layouthuts/layoutfilterablebutcherablelist.xml").toString(), this);
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

	public @NotNull Predicate<CustomizedButcherable> getDisplayPredicate()
	{
		return this.displayPredicate;
	}

}
