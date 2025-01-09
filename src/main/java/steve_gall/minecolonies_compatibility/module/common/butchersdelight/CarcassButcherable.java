package steve_gall.minecolonies_compatibility.module.common.butchersdelight;

import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.minecolonies.api.equipment.registry.EquipmentTypeEntry;

import net.mcreator.butchersdelight.init.ButchersdelightModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import steve_gall.minecolonies_compatibility.core.common.init.ModToolTypes;

public abstract class CarcassButcherable extends AbstractButcherable
{
	public CarcassButcherable(AbstractButcherable.Builder builder)
	{
		super(builder);
	}

	@Override
	public @NotNull List<EquipmentTypeEntry> getToolTypesForIcon()
	{
		return Collections.singletonList(ModToolTypes.BUTCHER_TOOL.getToolType());
	}

	@Override
	public @NotNull EquipmentTypeEntry getBlockToolType(@NotNull LevelReader level, @NotNull BlockPos position, @NotNull BlockState state)
	{
		return ModToolTypes.BUTCHER_TOOL.getToolType();
	}

	@Override
	protected void setButcherBlockProcess(CompoundTag tag)
	{
		tag.putDouble("ButcherProcess", 100.0D);
	}

	@Override
	protected ItemStack getButcherBlockTool()
	{
		return new ItemStack(ButchersdelightModItems.CLEAVER.get());
	}

}
