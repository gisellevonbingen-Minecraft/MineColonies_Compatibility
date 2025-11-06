package steve_gall.minecolonies_compatibility.core.common.building.module;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.ldtteam.blockui.views.BOWindow;
import com.minecolonies.api.colony.buildings.modules.AbstractBuildingModuleView;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import steve_gall.minecolonies_compatibility.api.common.building.module.IRestrictableModuleView;
import steve_gall.minecolonies_compatibility.core.client.gui.RestrictableModuleWindow;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.core.common.network.message.RestrictSetAreaMessage;
import steve_gall.minecolonies_compatibility.core.common.network.message.RestrictSetEnabledMessage;

public class RestrictableModuleView extends AbstractBuildingModuleView implements IRestrictableModuleView
{
	public static final String DESC = "com.minecolonies.coremod.gui.workerhuts.restrict";

	private boolean restrictEnabled = false;

	@Nullable
	private BlockPos restrictPos1 = null;
	@Nullable
	private BlockPos restrictPos2 = null;

	@Override
	public void deserialize(@NotNull FriendlyByteBuf buf)
	{
		this.restrictEnabled = buf.readBoolean();
		this.restrictPos1 = buf.readNullable(FriendlyByteBuf::readBlockPos);
		this.restrictPos2 = buf.readNullable(FriendlyByteBuf::readBlockPos);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public BOWindow getWindow()
	{
		return new RestrictableModuleWindow(MineColoniesCompatibility.rl("gui/layouthuts/layoutrestrictable.xml").toString(), this);
	}

	@Override
	public String getIcon()
	{
		return "restrict";
	}

	@Override
	public String getDesc()
	{
		return DESC;
	}

	@Override
	public void setRestrictEnabled(boolean enabled)
	{
		this.restrictEnabled = enabled;
		MineColoniesCompatibility.network().sendToServer(new RestrictSetEnabledMessage(this, enabled));
	}

	@Override
	public void setRestrictArea(@NotNull BlockPos pos1, @NotNull BlockPos pos2)
	{
		this.restrictPos1 = pos1;
		this.restrictPos2 = pos2;
		MineColoniesCompatibility.network().sendToServer(new RestrictSetAreaMessage(this, pos1, pos2));
	}

	@Override
	public boolean isRestrictEnabled()
	{
		return this.restrictEnabled;
	}

	@Override
	public @NotNull BlockPos getRestrictAreaPos1()
	{
		var pos1 = this.restrictPos1;
		return pos1 != null ? pos1 : this.getBuildingView().getPosition();
	}

	@Override
	public @NotNull BlockPos getRestrictAreaPos2()
	{
		var pos2 = this.restrictPos2;
		return pos2 != null ? pos2 : this.getBuildingView().getPosition();
	}

}
