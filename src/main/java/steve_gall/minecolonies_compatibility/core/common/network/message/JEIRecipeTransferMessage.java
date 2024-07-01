package steve_gall.minecolonies_compatibility.core.common.network.message;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent.Context;
import steve_gall.minecolonies_compatibility.api.common.inventory.IRecipeTransferableMenu;
import steve_gall.minecolonies_compatibility.core.common.network.AbstractMessage;

public class JEIRecipeTransferMessage<RECIPE> extends AbstractMessage
{
	public static final String RECIPE_TRANSFER_TAG_RECIPE = "recipe";
	public static final String RECIPE_TRANSFER_TAG_PAYLOAD = "payload";

	private final CompoundTag tag;

	public JEIRecipeTransferMessage(IRecipeTransferableMenu<RECIPE> menu, RECIPE recipe, CompoundTag payload)
	{
		this.tag = new CompoundTag();
		this.tag.put(RECIPE_TRANSFER_TAG_RECIPE, menu.getRecipeValidator().serialize(recipe));
		this.tag.put(RECIPE_TRANSFER_TAG_PAYLOAD, payload);
	}

	public JEIRecipeTransferMessage(FriendlyByteBuf buffer)
	{
		super(buffer);

		this.tag = buffer.readNbt();
	}

	@Override
	public void encode(FriendlyByteBuf buffer)
	{
		super.encode(buffer);

		buffer.writeNbt(this.tag);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void handle(Context context)
	{
		super.handle(context);

		var player = context.getSender();

		if (player == null)
		{
			return;
		}

		if (player.containerMenu instanceof IRecipeTransferableMenu menu)
		{
			var recipe = menu.getRecipeValidator().deserialize(this.tag.getCompound(RECIPE_TRANSFER_TAG_RECIPE));
			var payload = this.tag.getCompound(RECIPE_TRANSFER_TAG_PAYLOAD);
			menu.onRecipeTransfer(recipe, payload);
		}

	}

	public CompoundTag getTag()
	{
		return this.tag;
	}

}
