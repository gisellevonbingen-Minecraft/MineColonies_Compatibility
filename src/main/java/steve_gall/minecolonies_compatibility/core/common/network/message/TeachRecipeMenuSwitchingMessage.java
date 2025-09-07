package steve_gall.minecolonies_compatibility.core.common.network.message;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.core.common.inventory.TeachRecipeMenu;
import steve_gall.minecolonies_tweaks.api.common.network.AbstractMessage;

public class TeachRecipeMenuSwitchingMessage extends AbstractMessage
{
	public static final CustomPacketPayload.Type<TeachRecipeMenuSwitchingMessage> TYPE = new CustomPacketPayload.Type<>(MineColoniesCompatibility.rl("teach_recipe_menu_switching"));

	public TeachRecipeMenuSwitchingMessage()
	{

	}

	public TeachRecipeMenuSwitchingMessage(RegistryFriendlyByteBuf buffer)
	{
		super(buffer);
	}

	@Override
	public void encode(RegistryFriendlyByteBuf buffer)
	{
		super.encode(buffer);
	}

	@Override
	public void handle(IPayloadContext context)
	{
		super.handle(context);

		if (context.player().containerMenu instanceof TeachRecipeMenu menu)
		{
			menu.setRecipeIndex((menu.getRecipeIndex() + 1) % menu.getRecipes().size());
		}

	}

	@Override
	public CustomPacketPayload.Type<TeachRecipeMenuSwitchingMessage> type()
	{
		return TYPE;
	}

}
