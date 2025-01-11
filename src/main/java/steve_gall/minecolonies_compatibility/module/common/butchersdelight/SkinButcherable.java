package steve_gall.minecolonies_compatibility.module.common.butchersdelight;

import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.minecolonies.api.entity.citizen.AbstractEntityCitizen;
import com.minecolonies.api.util.constant.ToolType;

import net.mcreator.butchersdelight.init.ButchersdelightModBlocks;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import steve_gall.minecolonies_compatibility.api.common.butcher.ButcherBlockContext;
import steve_gall.minecolonies_compatibility.api.common.crafting.ToolOrIngredientStack;
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
	public @NotNull List<ToolOrIngredientStack> getToolsForIcon()
	{
		return Collections.singletonList(ToolOrIngredientStack.of(ToolType.SHEARS));
	}

	@Override
	public @NotNull ToolOrIngredientStack getBlockTool(@NotNull ButcherBlockContext context)
	{
		return ToolOrIngredientStack.of(ToolType.SHEARS);
	}

	@Override
	public @Nullable boolean isTableBlock(@NotNull ButcherBlockContext context)
	{
		return context.getState().is(ButchersdelightModBlocks.RACK.get());
	}

	@Override
	public void doButcherTable(@NotNull ButcherBlockContext context, @NotNull AbstractEntityCitizen worker, @NotNull InteractionHand hand)
	{
		super.doButcherTable(context, worker, hand);

		if (context.getLevel() instanceof ServerLevel serverLevel)
		{
			ButchersDelightModule.rightClick(serverLevel, context.getPosition(), worker, worker.getItemInHand(hand));
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
	public @NotNull Component getTableNotFoundMessage()
	{
		return InteractionMessageHelper.getWorkingBlockNotFound(ButchersdelightModBlocks.RACK.get());
	}

}
