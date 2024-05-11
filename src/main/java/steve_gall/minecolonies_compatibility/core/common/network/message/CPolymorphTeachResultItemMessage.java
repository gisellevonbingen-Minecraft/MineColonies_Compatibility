package steve_gall.minecolonies_compatibility.core.common.network.message;

import com.minecolonies.api.inventory.container.ContainerCrafting;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import steve_gall.minecolonies_compatibility.core.common.network.AbstractMessage;
import steve_gall.minecolonies_compatibility.module.common.ModuleManager;
import steve_gall.minecolonies_compatibility.module.common.polymorph.PolymorphModule;

public class CPolymorphTeachResultItemMessage extends AbstractMessage
{
	private final ResourceLocation recipeId;
	private final ItemStack reuslt;

	public CPolymorphTeachResultItemMessage(ResourceLocation recipeId, ItemStack result)
	{
		super();

		this.recipeId = recipeId;
		this.reuslt = result;
	}

	public CPolymorphTeachResultItemMessage(FriendlyByteBuf buffer)
	{
		super(buffer);

		this.recipeId = buffer.readResourceLocation();
		this.reuslt = buffer.readItem();
	}

	@Override
	public void encode(FriendlyByteBuf buffer)
	{
		super.encode(buffer);

		buffer.writeResourceLocation(this.recipeId);
		buffer.writeItem(this.reuslt);
	}

	@Override
	public void handle(NetworkEvent.Context context)
	{
		super.handle(context);

		var player = context.getSender();

		if (player.containerMenu instanceof ContainerCrafting crafting && ModuleManager.POLYMORPH.isLoaded())
		{
			crafting.craftResult.setItem(0, this.reuslt);
			PolymorphModule.sendHighlightRecipe(player, this.recipeId);
		}

	}

	public ItemStack getResult()
	{
		return this.reuslt;
	}

}
