package steve_gall.minecolonies_compatibility.core.common.network.message;

import com.minecolonies.api.inventory.container.ContainerCrafting;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.core.common.inventory.TeachRecipeMenu;
import steve_gall.minecolonies_compatibility.module.common.ModuleManager;
import steve_gall.minecolonies_compatibility.module.common.polymorph.PolymorphModule;
import steve_gall.minecolonies_tweaks.api.common.network.AbstractMessage;

public class TeachRecipeMenuSelectMessage extends AbstractMessage
{
	public static final CustomPacketPayload.Type<TeachRecipeMenuSelectMessage> TYPE = new CustomPacketPayload.Type<>(MineColoniesCompatibility.rl("teach_recipe_menu_select"));

	private final ResourceLocation recipeId;

	public TeachRecipeMenuSelectMessage(ResourceLocation recipeId)
	{
		super();

		this.recipeId = recipeId;
	}

	public TeachRecipeMenuSelectMessage(RegistryFriendlyByteBuf buffer)
	{
		super(buffer);

		this.recipeId = buffer.readResourceLocation();
	}

	@Override
	public void encode(RegistryFriendlyByteBuf buffer)
	{
		super.encode(buffer);

		buffer.writeResourceLocation(this.recipeId);
	}

	@Override
	public void handle(IPayloadContext context)
	{
		super.handle(context);

		var player = context.player();
		var registryAccess = player.level().registryAccess();
		var holder = player.level().getRecipeManager().byKey(this.recipeId).orElse(null);

		if (holder == null)
		{
			return;
		}

		if (player.containerMenu instanceof ContainerCrafting menu)
		{
			var input = menu.craftMatrix.asCraftInput();
			var output = ((CraftingRecipe) holder.value()).assemble(input, registryAccess);
			menu.craftResult.setItem(0, output);

			if (ModuleManager.POLYMORPH.isLoaded())
			{
				PolymorphModule.sendRecipesList((ServerPlayer) player, input, output);
			}

		}
		else if (player.containerMenu instanceof TeachRecipeMenu<?, ?> menu)
		{
			menu.setRecipeIndex(menu.getRecipes().indexOf(holder));
		}

	}

	@Override
	public CustomPacketPayload.Type<TeachRecipeMenuSelectMessage> type()
	{
		return TYPE;
	}

	public ResourceLocation getRecipeId()
	{
		return this.recipeId;
	}

}
