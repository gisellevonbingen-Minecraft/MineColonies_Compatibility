package steve_gall.minecolonies_compatibility.module.common.butchersdelight;

import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.minecolonies.api.util.constant.IToolType;

import net.mcreator.butchersdelight.init.ButchersdelightModItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import steve_gall.minecolonies_compatibility.api.common.butcher.ButcherBlockContext;
import steve_gall.minecolonies_compatibility.core.common.init.ModToolTypes;

public abstract class CarcassButcherable extends AbstractButcherable
{
	public CarcassButcherable(AbstractButcherable.Builder builder)
	{
		super(builder);
	}

	@Override
	public @NotNull List<IToolType> getToolTypesForIcon()
	{
		return Collections.singletonList(ModToolTypes.BUTCHER_TOOL.getToolType());
	}

	@Override
	public @NotNull IToolType getBlockToolType(@NotNull ButcherBlockContext context)
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
