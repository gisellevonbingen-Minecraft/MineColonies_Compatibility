package steve_gall.minecolonies_compatibility.core.client.gui;

import java.util.Optional;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;

public class NetworkStorageViewScreenUtils
{
	public static final Component TEXT_NOT_LINKED = Component.translatable("minecolonies_compatibility.text.not_linked");
	public static final String TEXT_LINKED = "minecolonies_compatibility.text.linked_pos";

	public static Component getModuleText(Optional<BlockPos> posWrapper)
	{
		if (posWrapper.isPresent())
		{
			var pos = posWrapper.get();
			return Component.translatable(TEXT_LINKED, pos.getX(), pos.getY(), pos.getZ());
		}
		else
		{
			return TEXT_NOT_LINKED;
		}

	}

	private NetworkStorageViewScreenUtils()
	{

	}

}
