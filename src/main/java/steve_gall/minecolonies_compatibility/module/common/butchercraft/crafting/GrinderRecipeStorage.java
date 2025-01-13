package steve_gall.minecolonies_compatibility.module.common.butchercraft.crafting;

import java.util.Collections;
import java.util.List;

import com.minecolonies.api.colony.requestsystem.StandardFactoryController;
import com.minecolonies.api.crafting.ItemStorage;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import steve_gall.minecolonies_compatibility.api.common.crafting.SimpleRecipeStorage;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;

public class GrinderRecipeStorage extends SimpleRecipeStorage<GrinderGenericRecipe>
{
	public static final ResourceLocation ID = MineColoniesCompatibility.rl("butchercraft_grinder");

	private final ItemStorage attachment;

	public GrinderRecipeStorage(CompoundTag tag)
	{
		super(tag);

		this.attachment = StandardFactoryController.getInstance().deserialize(tag.getCompound("attachment"));
	}

	public GrinderRecipeStorage(ResourceLocation recipeId, List<ItemStorage> ingredients, ItemStorage attachment, ItemStack output)
	{
		super(recipeId, ingredients, output);

		this.attachment = attachment;
	}

	@Override
	public void serialize(CompoundTag tag)
	{
		super.serialize(tag);

		tag.put("attachment", StandardFactoryController.getInstance().serialize(this.attachment));
	}

	@Override
	public ResourceLocation getId()
	{
		return ID;
	}

	@Override
	public List<ItemStack> getSecondaryOutputs()
	{
		return Collections.emptyList();
	}

	@Override
	protected GenericRecipeFactory<GrinderGenericRecipe> getGenericRecipeFactory()
	{
		return (recipeId, ingredients, output) -> new GrinderGenericRecipe(recipeId, ingredients, output, Ingredient.of(this.attachment.getItemStack()));
	}

	public ItemStorage getAttachment()
	{
		return this.attachment;
	}

}
