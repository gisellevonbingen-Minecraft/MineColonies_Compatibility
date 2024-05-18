package steve_gall.minecolonies_compatibility.module.common.farmersdelight.building.module;

import org.jetbrains.annotations.NotNull;

import com.minecolonies.api.util.constant.IToolType;
import com.minecolonies.api.util.constant.ToolType;
import com.minecolonies.core.colony.buildings.moduleviews.CraftingModuleView;

import net.minecraft.network.FriendlyByteBuf;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.core.common.network.message.FarmersTeachCuttingOpenMessage;

public class CuttingCraftingModuleView extends CraftingModuleView
{
	private IToolType toolType;

	public CuttingCraftingModuleView()
	{
		this.toolType = ToolType.NONE;
	}

	@Override
	public void deserialize(@NotNull FriendlyByteBuf buf)
	{
		super.deserialize(buf);

		this.toolType = ToolType.getToolType(buf.readUtf());
	}

	@Override
	public void openCraftingGUI()
	{
		MineColoniesCompatibility.network().sendToServer(new FarmersTeachCuttingOpenMessage(this, this.getToolType()));
	}

	public IToolType getToolType()
	{
		return this.toolType;
	}

}
