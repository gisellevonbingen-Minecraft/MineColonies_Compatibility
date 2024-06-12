package steve_gall.minecolonies_compatibility.module.common.refinedstorage;

import com.refinedmods.refinedstorage.blockentity.NetworkNodeBlockEntity;
import com.refinedmods.refinedstorage.blockentity.data.BlockEntitySynchronizationParameter;
import com.refinedmods.refinedstorage.blockentity.data.BlockEntitySynchronizationSpec;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.module.common.refinedstorage.init.ModuleBlockEntities;

public class CitizenGridBlockEntity extends NetworkNodeBlockEntity<CitizenGridNetworkNode>
{
	private static final String TAG_LINK = "link";

	public static final BlockEntitySynchronizationParameter<CompoundTag, CitizenGridBlockEntity> PAIR = new BlockEntitySynchronizationParameter<>(MineColoniesCompatibility.rl(TAG_LINK), EntityDataSerializers.COMPOUND_TAG, new CompoundTag(), //
			t -> t.getNode().getView().write(), //
			(t, v) -> t.getNode().getView().read(v), //
			(initial, p) ->
			{
			});

	public static final BlockEntitySynchronizationSpec SYNC_SPEC = BlockEntitySynchronizationSpec.builder()//
			.addWatchedParameter(REDSTONE_MODE)//
			.addWatchedParameter(PAIR).build();

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
	public CompoundTag writeUpdate(CompoundTag tag)
	{
		super.writeUpdate(tag);

		tag.put(TAG_LINK, this.getNode().getView().write());

		return tag;
	}

	@Override
	public void readUpdate(CompoundTag tag)
	{
		super.readUpdate(tag);

		this.getNode().getView().read(tag.getCompound(TAG_LINK));
	}

}
