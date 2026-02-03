package steve_gall.minecolonies_compatibility.core.common.util;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.neoforged.fml.ModList;

public class ModListUtils
{
	public static String getDisplayName(String namespace)
	{
		return ModList.get().getModContainerById(namespace).map(f -> f.getModInfo().getDisplayName()).orElse(namespace);
	}

	public static Component getDisplayNameForTooltip(String namespace)
	{
		return Component.literal(getDisplayName(namespace)).withStyle(ChatFormatting.BLUE, ChatFormatting.ITALIC);
	}

	private ModListUtils()
	{

	}

}
