package steve_gall.minecolonies_compatibility.module.common.butchersdelight;

import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import net.mcreator.butchersdelight.init.ButchersdelightModItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import steve_gall.minecolonies_compatibility.api.common.butcher.ButcherBlockContext;
import steve_gall.minecolonies_compatibility.api.common.crafting.ToolOrIngredientStack;
import steve_gall.minecolonies_compatibility.core.common.init.ModToolTypes;

public abstract class CarcassButcherable extends AbstractButcherable
{
	public CarcassButcherable(AbstractButcherable.Builder builder)
	{
		super(builder);
	}

	@Override
	public @NotNull List<ToolOrIngredientStack> getToolsForIcon()
	{
		return Collections.singletonList(ToolOrIngredientStack.of(ModToolTypes.BUTCHER_TOOL));
	}

	@Override
	public @NotNull ToolOrIngredientStack getBlockTool(@NotNull ButcherBlockContext context)
	{
		return ToolOrIngredientStack.of(ModToolTypes.BUTCHER_TOOL);
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
