package steve_gall.minecolonies_compatibility.module.common.refinedstorage;

import org.jetbrains.annotations.NotNull;

import com.refinedmods.refinedstorage.api.storage.AccessType;
import com.refinedmods.refinedstorage.blockentity.NetworkNodeBlockEntity;
import com.refinedmods.refinedstorage.blockentity.config.IAccessType;
import com.refinedmods.refinedstorage.blockentity.data.BlockEntitySynchronizationParameter;
import com.refinedmods.refinedstorage.blockentity.data.BlockEntitySynchronizationSpec;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import steve_gall.minecolonies_compatibility.api.common.building.module.INetworkStorageView;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.core.common.block.entity.INetworkStorageViewHolder;
import steve_gall.minecolonies_compatibility.module.common.refinedstorage.init.ModuleBlockEntities;

public class CitizenGridBlockEntity extends NetworkNodeBlockEntity<CitizenGridNetworkNode> implements INetworkStorageViewHolder
{
	private static final String TAG_LINK = "link";

	public static final BlockEntitySynchronizationParameter<CompoundTag, CitizenGridBlockEntity> PAIR = new BlockEntitySynchronizationParameter<>(MineColoniesCompatibility.rl(TAG_LINK), EntityDataSerializers.COMPOUND_TAG, new CompoundTag(), //
			t -> t.getNode().getView().writeLink(), //
			(t, v) -> t.getNode().getView().readLink(v), //
			(initial, p) ->
			{
			});
	public static final BlockEntitySynchronizationParameter<AccessType, CitizenGridBlockEntity> ACCESS_TYPE = IAccessType.createParameter(MineColoniesCompatibility.rl("access_type"));

	public static final BlockEntitySynchronizationSpec SYNC_SPEC = BlockEntitySynchronizationSpec.builder()//
			.addWatchedParameter(REDSTONE_MODE)//
			.addWatchedParameter(PAIR)//
			.addWatchedParameter(ACCESS_TYPE).build();

	public CitizenGridBlockEntity(BlockPos pos, BlockState state)
	{
		super(ModuleBlockEntities.CITIZEN_GRID.get(), pos, state, SYNC_SPEC, CitizenGridNetworkNode.class);
	}

	@Override
	public CitizenGridNetworkNode createNode(Level level, BlockPos pos)
	{
		return new CitizenGridNetworkNode(level, pos);
	}

	@Override
	public void onRemovedNotDueToChunkUnload()
	{
		super.onRemovedNotDueToChunkUnload();

		if (!this.level.isClientSide())
		{
			this.getRemovedNode().getView().unlink();
		}

	}

	@Override
	protected void saveAdditional(CompoundTag tag)
	{
		super.saveAdditional(tag);
	}

	@Override
	public void load(CompoundTag tag)
	{
		super.load(tag);
	}

	@Override
	public CompoundTag writeUpdate(CompoundTag tag)
	{
		super.writeUpdate(tag);

		tag.put(TAG_LINK, this.getNode().getView().writeLink());

		return tag;
	}

	@Override
	public void readUpdate(CompoundTag tag)
	{
		super.readUpdate(tag);

		this.getNode().getView().readLink(tag.getCompound(TAG_LINK));
	}

	@Override
	public @NotNull INetworkStorageView getNetworkStorageView()
	{
		return this.getNode().getView();
	}

}
