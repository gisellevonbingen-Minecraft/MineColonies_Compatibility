package steve_gall.minecolonies_compatibility.core.common.building.module;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.ldtteam.blockui.views.BOWindow;
import com.minecolonies.api.colony.buildings.modules.AbstractBuildingModuleView;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.PacketDistributor;
import steve_gall.minecolonies_compatibility.api.common.building.module.IRestrictableModuleView;
import steve_gall.minecolonies_compatibility.core.client.gui.RestrictableModuleWindow;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.core.common.network.message.RestrictSetAreaMessage;
import steve_gall.minecolonies_compatibility.core.common.network.message.RestrictSetEnabledMessage;

public class RestrictableModuleView extends AbstractBuildingModuleView implements IRestrictableModuleView
{
	private boolean restrictEnabled = false;

	@Nullable
	private BlockPos restrictPos1 = null;
	@Nullable
	private BlockPos restrictPos2 = null;

	@Override
	public void deserialize(RegistryFriendlyByteBuf buf)
	{
		this.restrictEnabled = buf.readBoolean();
		this.restrictPos1 = buf.readNullable(RegistryFriendlyByteBuf::readBlockPos);
		this.restrictPos2 = buf.readNullable(RegistryFriendlyByteBuf::readBlockPos);
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
		return "com.minecolonies.coremod.gui.workerhuts." + this.getIcon();
	}

	@Override
	public void setRestrictEnabled(boolean enabled)
	{
		this.restrictEnabled = enabled;
		PacketDistributor.sendToServer(new RestrictSetEnabledMessage(this, enabled));
	}

	@Override
	public void setRestrictArea(@NotNull BlockPos pos1, @NotNull BlockPos pos2)
	{
		this.restrictPos1 = pos1;
		this.restrictPos2 = pos2;
		PacketDistributor.sendToServer(new RestrictSetAreaMessage(this, pos1, pos2));
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
