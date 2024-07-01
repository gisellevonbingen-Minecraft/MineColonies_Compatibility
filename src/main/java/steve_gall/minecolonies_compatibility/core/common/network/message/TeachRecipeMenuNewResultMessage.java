package steve_gall.minecolonies_compatibility.core.common.network.message;

import org.jetbrains.annotations.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent.Context;
import steve_gall.minecolonies_compatibility.core.common.inventory.TeachRecipeMenu;
import steve_gall.minecolonies_compatibility.core.common.network.AbstractMessage;

public class TeachRecipeMenuNewResultMessage extends AbstractMessage
{
	@Nullable
	private final CompoundTag tag;

	public TeachRecipeMenuNewResultMessage(@Nullable CompoundTag tag)
	{
		this.tag = tag;
	}

	public TeachRecipeMenuNewResultMessage(FriendlyByteBuf buffer)
	{
		super(buffer);

		this.tag = buffer.readNullable(FriendlyByteBuf::readNbt);
	}

	@Override
	public void encode(FriendlyByteBuf buffer)
	{
		super.encode(buffer);

		buffer.writeNullable(this.tag, FriendlyByteBuf::writeNbt);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void handle(Context context)
	{
		super.handle(context);

		var mc = Minecraft.getInstance();

		if (mc.player.containerMenu instanceof TeachRecipeMenu menu)
		{
			if (this.tag != null)
			{
				var recipe = menu.getRecipeValidator().deserialize(this.tag);
				menu.setRecipe(recipe);
			}
			else
			{
				menu.setRecipe(null);
			}

		}

	}

	@Nullable
	public CompoundTag getTag()
	{
		return this.tag;
	}

}
