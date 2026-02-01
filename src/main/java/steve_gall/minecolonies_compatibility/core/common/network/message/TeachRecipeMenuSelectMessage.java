package steve_gall.minecolonies_compatibility.core.common.network.message;

import com.minecolonies.api.inventory.container.ContainerCrafting;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraftforge.network.NetworkEvent;
import steve_gall.minecolonies_compatibility.core.common.inventory.TeachRecipeMenu;
import steve_gall.minecolonies_compatibility.module.common.ModuleManager;
import steve_gall.minecolonies_compatibility.module.common.polymorph.PolymorphModule;
import steve_gall.minecolonies_tweaks.api.common.network.AbstractMessage;

public class TeachRecipeMenuSelectMessage extends AbstractMessage
{
	private final ResourceLocation recipeId;

	public TeachRecipeMenuSelectMessage(ResourceLocation recipeId)
	{
		super();

		this.recipeId = recipeId;
	}

	public TeachRecipeMenuSelectMessage(FriendlyByteBuf buffer)
	{
		super(buffer);

		this.recipeId = buffer.readResourceLocation();
	}

	@Override
	public void encode(FriendlyByteBuf buffer)
	{
		super.encode(buffer);

		buffer.writeResourceLocation(this.recipeId);
	}

	@Override
	public void handle(NetworkEvent.Context context)
	{
		super.handle(context);

		var player = context.getSender();

		if (player.containerMenu instanceof ContainerCrafting menu)
		{
			var recipe = player.level.getRecipeManager().byKey(this.recipeId).orElse(null);

			if (recipe == null)
			{
				return;
			}

			menu.craftResult.setItem(0, ((CraftingRecipe) recipe).assemble(menu.craftMatrix));

			if (ModuleManager.POLYMORPH.isLoaded())
			{
				PolymorphModule.sendHighlightRecipe(player, this.recipeId);
			}

		}
		else if (player.containerMenu instanceof TeachRecipeMenu menu)
		{
			menu.setRecipeIndex(menu.findRecipeIndex(this.recipeId));
		}

	}

	public ResourceLocation getRecipeId()
	{
		return this.recipeId;
	}

}
