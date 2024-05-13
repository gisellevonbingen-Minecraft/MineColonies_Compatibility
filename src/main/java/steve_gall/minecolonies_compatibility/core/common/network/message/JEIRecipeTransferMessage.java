package steve_gall.minecolonies_compatibility.core.common.network.message;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.network.NetworkEvent.Context;
import net.minecraftforge.registries.ForgeRegistries;
import steve_gall.minecolonies_compatibility.api.common.inventory.IRecipeTransferableMenu;
import steve_gall.minecolonies_compatibility.core.common.network.AbstractMessage;

public class JEIRecipeTransferMessage extends AbstractMessage
{
	private final RecipeType<?> type;
	private final ResourceLocation id;
	private final CompoundTag tag;

	public JEIRecipeTransferMessage(Recipe<?> recipe, CompoundTag tag)
	{
		this.type = recipe.getType();
		this.id = recipe.getId();
		this.tag = tag;
	}

	public JEIRecipeTransferMessage(FriendlyByteBuf buffer)
	{
		super(buffer);

		this.type = ForgeRegistries.RECIPE_TYPES.getValue(buffer.readResourceLocation());
		this.id = buffer.readResourceLocation();
		this.tag = buffer.readNbt();
	}

	@Override
	public void encode(FriendlyByteBuf buffer)
	{
		super.encode(buffer);

		buffer.writeResourceLocation(ForgeRegistries.RECIPE_TYPES.getKey(this.type));
		buffer.writeResourceLocation(this.id);
		buffer.writeNbt(this.tag);
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	@Override
	public void handle(Context context)
	{
		super.handle(context);

		var player = context.getSender();

		if (player == null)
		{
			return;
		}

		player.level.getRecipeManager().byKey(this.id).ifPresent(recipe ->
		{
			if (player.containerMenu instanceof IRecipeTransferableMenu menu)
			{
				menu.onRecipeTransfer(recipe, this.tag);
			}

		});

	}

	public RecipeType<?> getType()
	{
		return this.type;
	}

	public ResourceLocation getId()
	{
		return this.id;
	}

	public CompoundTag getTag()
	{
		return this.tag;
	}

}
