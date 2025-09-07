package steve_gall.minecolonies_compatibility.core.common.network.message;

import org.jetbrains.annotations.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.core.common.inventory.TeachRecipeMenu;
import steve_gall.minecolonies_tweaks.api.common.network.AbstractMessage;

public class TeachRecipeMenuNewResultMessage extends AbstractMessage
{
	public static final CustomPacketPayload.Type<TeachRecipeMenuNewResultMessage> TYPE = new CustomPacketPayload.Type<>(MineColoniesCompatibility.rl("teach_recipe_menu_new_result"));

	@Nullable
	private final CompoundTag tag;

	public TeachRecipeMenuNewResultMessage(@Nullable CompoundTag tag)
	{
		this.tag = tag;
	}

	public TeachRecipeMenuNewResultMessage(RegistryFriendlyByteBuf buffer)
	{
		super(buffer);

		this.tag = buffer.readNullable(RegistryFriendlyByteBuf::readNbt);
	}

	@Override
	public void encode(RegistryFriendlyByteBuf buffer)
	{
		super.encode(buffer);

		buffer.writeNullable(this.tag, RegistryFriendlyByteBuf::writeNbt);
	}

	@Override
	public void handle(IPayloadContext context)
	{
		super.handle(context);

		var mc = Minecraft.getInstance();

		if (mc.player.containerMenu instanceof TeachRecipeMenu menu)
		{
			menu.onNewResultTransfer(this.tag);
		}

	}

	@Override
	public CustomPacketPayload.Type<TeachRecipeMenuNewResultMessage> type()
	{
		return TYPE;
	}

	@Nullable
	public CompoundTag getTag()
	{
		return this.tag;
	}

}
