package steve_gall.minecolonies_compatibility.core.common.network.message;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.minecolonies.api.colony.requestsystem.StandardFactoryController;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent.Context;
import steve_gall.minecolonies_compatibility.core.common.inventory.TeachRecipeMenu;
import steve_gall.minecolonies_tweaks.api.common.network.AbstractMessage;

public class TeachRecipeMenuNewRecipesMessage extends AbstractMessage
{
	private final List<CompoundTag> recipes;

	public TeachRecipeMenuNewRecipesMessage(List<CompoundTag> recipes)
	{
		this.recipes = ImmutableList.copyOf(recipes);
	}

	public TeachRecipeMenuNewRecipesMessage(FriendlyByteBuf buffer)
	{
		super(buffer);

		this.recipes = buffer.readList(FriendlyByteBuf::readNbt);
	}

	@Override
	public void encode(FriendlyByteBuf buffer)
	{
		super.encode(buffer);

		buffer.writeCollection(this.recipes, FriendlyByteBuf::writeNbt);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void handle(Context context)
	{
		super.handle(context);

		var mc = Minecraft.getInstance();

		if (mc.player.containerMenu instanceof TeachRecipeMenu menu)
		{
			var recipes = this.recipes.stream().map(r -> menu.getRecipeValidator().deserialize(StandardFactoryController.getInstance(), r)).toList();
			menu.onNewRecipesTransfer(recipes);
		}

	}

	public List<CompoundTag> getRecipes()
	{
		return this.recipes;
	}

}
