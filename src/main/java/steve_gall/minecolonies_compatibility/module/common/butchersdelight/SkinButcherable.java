package steve_gall.minecolonies_compatibility.module.common.butchersdelight;

import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.minecolonies.api.entity.citizen.AbstractEntityCitizen;
import com.minecolonies.api.equipment.ModEquipmentTypes;
import com.minecolonies.api.equipment.registry.EquipmentTypeEntry;

import net.mcreator.butchersdelight.init.ButchersdelightModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import steve_gall.minecolonies_compatibility.core.common.util.InteractionMessageHelper;

public class SkinButcherable extends AbstractButcherable
{
	protected final List<BlockState> tableIcons;

	public SkinButcherable(AbstractButcherable.Builder builder)
	{
		super(builder);

		this.tableIcons = Collections.singletonList(ButchersdelightModBlocks.RACK.get().defaultBlockState());
	}

	@Override
	public @NotNull List<BlockState> getTableIcons()
	{
		return this.tableIcons;
	}

	@Override
	public @NotNull List<EquipmentTypeEntry> getToolTypesForIcon()
	{
		return Collections.singletonList(ModEquipmentTypes.shears.get());
	}

	@Override
	public @NotNull EquipmentTypeEntry getBlockToolType(@NotNull LevelReader level, @NotNull BlockPos position, @NotNull BlockState state)
	{
		return ModEquipmentTypes.shears.get();
	}

	@Override
	public @Nullable boolean isTableBlock(@NotNull LevelReader level, @NotNull BlockPos position, @NotNull BlockState state)
	{
		return state.is(ButchersdelightModBlocks.RACK.get());
	}

	@Override
	public void doButcherTable(@NotNull Level level, @NotNull BlockPos position, @NotNull BlockState state, @NotNull AbstractEntityCitizen worker, @NotNull InteractionHand hand)
	{
		super.doButcherTable(level, position, state, worker, hand);

		if (level instanceof ServerLevel serverLevel)
		{
			ButchersDelightModule.rightClick(serverLevel, position, worker, worker.getItemInHand(hand));
		}

	}

	@Override
	protected ItemStack getButcherBlockTool()
	{
		return new ItemStack(Items.SHEARS);
	}

	@Override
	protected void setButcherBlockProcess(CompoundTag tag)
	{
		tag.putDouble("tannerProcess", 100.0D);
	}

	@Override
	public Component getTableNotFoundMessage()
	{
		return InteractionMessageHelper.getWorkingBlockNotFound(ButchersdelightModBlocks.RACK.get());
	}

}
