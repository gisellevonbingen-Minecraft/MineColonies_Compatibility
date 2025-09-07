package steve_gall.minecolonies_compatibility.core.common.building.module;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.minecolonies.api.colony.buildings.modules.AbstractBuildingModule;
import com.minecolonies.api.colony.buildings.modules.IPersistentModule;
import com.minecolonies.api.util.BlockPosUtil;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import steve_gall.minecolonies_compatibility.api.common.building.module.IRestrictableModule;

public abstract class RestrictableModule extends AbstractBuildingModule implements IRestrictableModule, IPersistentModule
{
	public static final String TAG_RESTRICT_ENABLED = "restrict_enabled";
	public static final String TAG_RESTRICT_POS_1 = "restrict_pos1";
	public static final String TAG_RESTRICT_POS_2 = "restrict_pos2";

	private boolean restrictEnabled = false;

	@Nullable
	private BlockPos restrictPos1 = null;
	@Nullable
	private BlockPos restrictPos2 = null;

	@Override
	public void setRestrictEnabled(boolean enabled)
	{
		this.restrictEnabled = enabled;
		this.markDirty();
	}

	@Override
	public boolean isRestrictEnabled()
	{
		return this.restrictEnabled;
	}

	@Override
	public void setRestrictArea(@NotNull BlockPos pos1, @NotNull BlockPos pos2)
	{
		this.restrictPos1 = pos1;
		this.restrictPos2 = pos2;
		this.setRestrictEnabled(true);
	}

	@Override
	public @NotNull BlockPos getRestrictAreaPos1()
	{
		var pos1 = this.restrictPos1;
		return pos1 != null ? pos1 : this.building.getPosition();
	}

	@Override
	public @NotNull BlockPos getRestrictAreaPos2()
	{
		var pos2 = this.restrictPos2;
		return pos2 != null ? pos2 : this.building.getPosition();
	}

	@Override
	public void deserializeNBT(HolderLookup.Provider provider, CompoundTag compound)
	{
		this.restrictEnabled = compound.getBoolean(TAG_RESTRICT_ENABLED);

		if (compound.contains(TAG_RESTRICT_POS_1))
		{
			this.restrictPos1 = BlockPosUtil.read(compound, TAG_RESTRICT_POS_1);
		}

		if (compound.contains(TAG_RESTRICT_POS_2))
		{
			this.restrictPos2 = BlockPosUtil.read(compound, TAG_RESTRICT_POS_2);
		}

	}

	@Override
	public void serializeNBT(HolderLookup.Provider provider, CompoundTag compound)
	{
		compound.putBoolean(TAG_RESTRICT_ENABLED, this.restrictEnabled);

		if (this.restrictPos1 != null)
		{
			BlockPosUtil.write(compound, TAG_RESTRICT_POS_1, this.restrictPos1);
		}

		if (this.restrictPos2 != null)
		{
			BlockPosUtil.write(compound, TAG_RESTRICT_POS_2, this.restrictPos2);
		}

	}

	@Override
	public void serializeToView(RegistryFriendlyByteBuf buf)
	{
		super.serializeToView(buf);

		buf.writeBoolean(this.restrictEnabled);
		buf.writeNullable(this.restrictPos1, RegistryFriendlyByteBuf::writeBlockPos);
		buf.writeNullable(this.restrictPos2, RegistryFriendlyByteBuf::writeBlockPos);
	}

}
