package steve_gall.minecolonies_compatibility.core.common.building.module;

import java.util.function.Predicate;

import org.jetbrains.annotations.NotNull;

import com.ldtteam.blockui.views.BOWindow;

import net.minecraft.network.chat.Component;
import steve_gall.minecolonies_compatibility.api.common.plant.CustomizedFruit;
import steve_gall.minecolonies_compatibility.core.client.gui.FruitListModuleWindow;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_tweaks.api.common.building.module.AbstractIdListModuleView;

public class FruitListModuleView extends AbstractIdListModuleView
{
	private final String icon;
	private final Component desc;
	private final boolean inverted;
	private final Predicate<CustomizedFruit> displayPredicate;

	public FruitListModuleView(String icon, Component desc, boolean inverted, Predicate<CustomizedFruit> displayPredicate)
	{
		this.icon = icon;
		this.desc = desc;
		this.inverted = inverted;
		this.displayPredicate = displayPredicate;
	}

	@Override
	public BOWindow getWindow()
	{
		return new FruitListModuleWindow(this, MineColoniesCompatibility.rl("gui/layouthuts/layoutfilterablefruitlist.xml"));
	}

	@Override
	public String getIcon()
	{
		return this.icon;
	}

	@Override
	public Component getDesc()
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
