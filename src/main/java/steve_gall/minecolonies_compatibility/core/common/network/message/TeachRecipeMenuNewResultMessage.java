package steve_gall.minecolonies_compatibility.core.common.network.message;

import org.jetbrains.annotations.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent.Context;
import steve_gall.minecolonies_compatibility.core.common.inventory.TeachRecipeMenu;
import steve_gall.minecolonies_compatibility.core.common.network.AbstractMessage;

public class TeachRecipeMenuNewResultMessage extends AbstractMessage
{
	@Nullable
	private final ResourceLocation recipeId;

	public TeachRecipeMenuNewResultMessage(@Nullable ResourceLocation recipeId)
	{
		this.recipeId = recipeId;
	}

	public TeachRecipeMenuNewResultMessage(FriendlyByteBuf buffer)
	{
		super(buffer);

		this.recipeId = buffer.readNullable(FriendlyByteBuf::readResourceLocation);
	}

	@Override
	public void encode(FriendlyByteBuf buffer)
	{
		super.encode(buffer);

		buffer.writeNullable(this.recipeId, FriendlyByteBuf::writeResourceLocation);
	}

	@Override
	public void handle(Context context)
	{
		super.handle(context);

		var mc = Minecraft.getInstance();

		if (mc.player.containerMenu instanceof TeachRecipeMenu<?, ?> menu)
		{
			menu.setRecipeId(this.recipeId);
		}

	}

	@Nullable
	public ResourceLocation getRecipeId()
	{
		return this.recipeId;
	}

}
