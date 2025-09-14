package steve_gall.minecolonies_compatibility.core.common.network.message;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent.Context;
import steve_gall.minecolonies_compatibility.core.common.inventory.TeachRecipeMenu;
import steve_gall.minecolonies_tweaks.api.common.network.AbstractMessage;

public class TeachRecipeMenuSwitchingMessage extends AbstractMessage
{
	public TeachRecipeMenuSwitchingMessage()
	{

	}

	public TeachRecipeMenuSwitchingMessage(FriendlyByteBuf buffer)
	{
		super(buffer);
	}

	@Override
	public void encode(FriendlyByteBuf buffer)
	{
		super.encode(buffer);
	}

	@Override
	public void handle(Context context)
	{
		super.handle(context);

		if (context.getSender().containerMenu instanceof TeachRecipeMenu menu)
		{
			menu.setRecipeIndex((menu.getRecipeIndex() + 1) % menu.getRecipes().size());
		}

	}

}
