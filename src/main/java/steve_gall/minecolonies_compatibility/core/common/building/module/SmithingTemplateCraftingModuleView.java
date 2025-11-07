package steve_gall.minecolonies_compatibility.core.common.building.module;

import org.jetbrains.annotations.NotNull;

import com.ldtteam.blockui.views.BOWindow;
import com.minecolonies.core.colony.buildings.moduleviews.CraftingModuleView;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import steve_gall.minecolonies_compatibility.core.client.gui.WindowListSmithingTemplateRecipes;
import steve_gall.minecolonies_compatibility.core.common.item.ItemStackCounter;

public class SmithingTemplateCraftingModuleView extends CraftingModuleView
{
	private final ItemStackCounter counter;

	public SmithingTemplateCraftingModuleView()
	{
		this.counter = new ItemStackCounter();
	}

	@Override
	public void deserialize(@NotNull FriendlyByteBuf buf)
	{
		super.deserialize(buf);

		this.counter.deserializeBuffer(buf);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public BOWindow getWindow()
	{
		return new WindowListSmithingTemplateRecipes(this);
	}

	public ItemStackCounter getCounter()
	{
		return this.counter;
	}

}
