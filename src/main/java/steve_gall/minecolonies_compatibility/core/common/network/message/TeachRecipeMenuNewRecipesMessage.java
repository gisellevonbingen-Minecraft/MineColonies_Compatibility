package steve_gall.minecolonies_compatibility.core.common.network.message;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.minecolonies.api.colony.requestsystem.StandardFactoryController;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.core.common.inventory.TeachRecipeMenu;
import steve_gall.minecolonies_tweaks.api.common.network.AbstractMessage;

public class TeachRecipeMenuNewRecipesMessage extends AbstractMessage
{
	public static final CustomPacketPayload.Type<TeachRecipeMenuNewRecipesMessage> TYPE = new CustomPacketPayload.Type<>(MineColoniesCompatibility.rl("teach_recipe_menu_new_recipes"));

	private final List<CompoundTag> recipes;

	public TeachRecipeMenuNewRecipesMessage(List<CompoundTag> recipes)
	{
		this.recipes = ImmutableList.copyOf(recipes);
	}

	public TeachRecipeMenuNewRecipesMessage(RegistryFriendlyByteBuf buffer)
	{
		super(buffer);

		this.recipes = buffer.readList(RegistryFriendlyByteBuf::readNbt);
	}

	@Override
	public void encode(RegistryFriendlyByteBuf buffer)
	{
		super.encode(buffer);

		buffer.writeCollection(this.recipes, RegistryFriendlyByteBuf::writeNbt);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void handle(IPayloadContext context)
	{
		super.handle(context);

		var mc = Minecraft.getInstance();

		if (mc.player.containerMenu instanceof TeachRecipeMenu menu)
		{
			var registryAccess = menu.getInventory().player.registryAccess();
			var recipes = this.recipes.stream().map(r -> menu.getRecipeValidator().deserialize(registryAccess, StandardFactoryController.getInstance(), r)).toList();
			menu.onNewRecipesTransfer(recipes);
		}

	}

	@Override
	public CustomPacketPayload.Type<TeachRecipeMenuNewRecipesMessage> type()
	{
		return TYPE;
	}

	public List<CompoundTag> getRecipes()
	{
		return this.recipes;
	}

}
