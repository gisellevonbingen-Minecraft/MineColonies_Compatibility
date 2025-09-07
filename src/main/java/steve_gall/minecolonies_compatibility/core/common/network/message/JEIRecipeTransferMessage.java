package steve_gall.minecolonies_compatibility.core.common.network.message;

import com.minecolonies.api.colony.requestsystem.StandardFactoryController;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import steve_gall.minecolonies_compatibility.api.common.inventory.IRecipeTransferableMenu;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_tweaks.api.common.network.AbstractMessage;

public class JEIRecipeTransferMessage<RECIPE, RECIPE_INPUT> extends AbstractMessage
{
	public static final CustomPacketPayload.Type<JEIRecipeTransferMessage<?, ?>> TYPE = new CustomPacketPayload.Type<>(MineColoniesCompatibility.rl("jei_recipe_transfer"));

	public static final String RECIPE_TRANSFER_TAG_RECIPE = "recipe";
	public static final String RECIPE_TRANSFER_TAG_PAYLOAD = "payload";

	private final CompoundTag tag;

	public JEIRecipeTransferMessage(IRecipeTransferableMenu<RECIPE, RECIPE_INPUT> menu, RECIPE recipe)
	{
		this(menu, recipe, new CompoundTag());
	}

	public JEIRecipeTransferMessage(IRecipeTransferableMenu<RECIPE, RECIPE_INPUT> menu, RECIPE recipe, CompoundTag payload)
	{
		var registryAccess = menu.getInventory().player.registryAccess();
		this.tag = new CompoundTag();
		this.tag.put(RECIPE_TRANSFER_TAG_RECIPE, menu.getRecipeValidator().serialize(registryAccess, StandardFactoryController.getInstance(), recipe));
		this.tag.put(RECIPE_TRANSFER_TAG_PAYLOAD, payload);
	}

	public JEIRecipeTransferMessage(RegistryFriendlyByteBuf buffer)
	{
		super(buffer);

		this.tag = buffer.readNbt();
	}

	@Override
	public void encode(RegistryFriendlyByteBuf buffer)
	{
		super.encode(buffer);

		buffer.writeNbt(this.tag);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void handle(IPayloadContext context)
	{
		super.handle(context);

		var player = context.player();

		if (player.containerMenu instanceof IRecipeTransferableMenu menu)
		{
			var registryAccess = menu.getInventory().player.registryAccess();
			var recipe = menu.getRecipeValidator().deserialize(registryAccess, StandardFactoryController.getInstance(), this.tag.getCompound(RECIPE_TRANSFER_TAG_RECIPE));
			var payload = this.tag.getCompound(RECIPE_TRANSFER_TAG_PAYLOAD);
			menu.onRecipeTransfer(recipe, payload);
		}

	}

	@Override
	public CustomPacketPayload.Type<JEIRecipeTransferMessage<?, ?>> type()
	{
		return TYPE;
	}

	public CompoundTag getTag()
	{
		return this.tag;
	}

}
