package steve_gall.minecolonies_compatibility.module.common.butchercraft.crafting;

import java.util.Collections;
import java.util.List;

import com.minecolonies.api.colony.requestsystem.factory.IFactoryController;
import com.minecolonies.api.crafting.ItemStorage;

import net.minecraft.core.HolderLookup;
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

	public GrinderRecipeStorage(HolderLookup.Provider provider, IFactoryController controller, CompoundTag tag)
	{
		super(provider, controller, tag);

		this.attachment = controller.deserializeTag(provider, tag.getCompound("attachment"));
	}

	public GrinderRecipeStorage(ResourceLocation recipeId, List<ItemStorage> ingredients, ItemStorage attachment, ItemStack output)
	{
		super(recipeId, ingredients, output);

		this.attachment = attachment;
	}

	@Override
	public void serialize(HolderLookup.Provider provider, IFactoryController controller, CompoundTag tag)
	{
		super.serialize(provider, controller, tag);

		tag.put("attachment", controller.serializeTag(provider, this.attachment));
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
