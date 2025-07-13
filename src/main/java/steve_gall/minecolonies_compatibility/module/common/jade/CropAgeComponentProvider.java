package steve_gall.minecolonies_compatibility.module.common.jade;

import com.minecolonies.core.blocks.MinecoloniesCropBlock;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;

public class CropAgeComponentProvider implements IBlockComponentProvider
{
	public static final ResourceLocation UID = MineColoniesCompatibility.rl("crop.age");
	public static final CropAgeComponentProvider INSTANCE = new CropAgeComponentProvider();

	private CropAgeComponentProvider()
	{

	}

	@Override
	public ResourceLocation getUid()
	{
		return UID;
	}

	@Override
	public boolean isRequired()
	{
		return true;
	}

	@Override
	public void appendTooltip(ITooltip tooltip, BlockAccessor blockAccessor, IPluginConfig pluginConfig)
	{
		if (!pluginConfig.get(UID))
		{
			return;
		}

		var growthValue = (blockAccessor.getBlockState().getValue(MinecoloniesCropBlock.AGE) / 6.0F) * 100.0F;

		if (growthValue < 100.0F)
		{
			tooltip.add(Component.translatable("tooltip.jade.crop_growth", String.format("%.0f%%", growthValue)));
		}
		else
		{
			tooltip.add(Component.translatable("tooltip.jade.crop_growth", Component.translatable("tooltip.jade.crop_mature").withStyle(ChatFormatting.GREEN)));
		}

	}

}
