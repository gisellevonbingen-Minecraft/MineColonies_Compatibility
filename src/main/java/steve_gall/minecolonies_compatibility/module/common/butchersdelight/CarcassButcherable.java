package steve_gall.minecolonies_compatibility.module.common.butchersdelight;

import com.minecolonies.api.util.constant.ToolType;

import net.mcreator.butchersdelight.init.ButchersdelightModItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import steve_gall.minecolonies_compatibility.core.common.init.ModToolTypes;

public abstract class CarcassButcherable extends AbstractButcherable
{
	public CarcassButcherable(AbstractButcherable.Builder builder)
	{
		super(builder);
	}

	@Override
	public ToolType getBlockToolType()
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
