package steve_gall.minecolonies_compatibility.api.common.crafting;

import java.util.List;
import java.util.Objects;

import org.jetbrains.annotations.NotNull;

import com.minecolonies.api.colony.requestsystem.factory.IFactoryController;
import com.minecolonies.api.crafting.ItemStorage;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import steve_gall.minecolonies_compatibility.core.common.crafting.ItemStorageHelper;
import steve_gall.minecolonies_compatibility.core.common.item.ItemStackHelper;
import steve_gall.minecolonies_compatibility.core.common.util.NBTUtils2;

public abstract class SimpleRecipeStorage<GENERIC_RECIPE extends SimpleGenericRecipe> extends GenericedRecipeStorage<GENERIC_RECIPE>
{
	private final ResourceLocation recipeId;
	private final List<ItemStorage> ingredients;
	private final ItemStack output;
	private final List<ItemStack> secondaryOutputs;

	private GENERIC_RECIPE genericRecipe;

	public SimpleRecipeStorage(@NotNull IFactoryController controller, @NotNull CompoundTag tag)
	{
		this.recipeId = new ResourceLocation(tag.getString("recipeId"));
		this.ingredients = NBTUtils2.deserializeList(tag, "ingredients", controller::deserialize);
		this.output = ItemStack.of(tag.getCompound("output"));
		this.secondaryOutputs = ItemStorageHelper.getCraftingRemainings(this.ingredients);
	}

	public SimpleRecipeStorage(@NotNull ResourceLocation recipeId, @NotNull List<ItemStorage> ingredients, @NotNull ItemStack output)
	{
		this.recipeId = recipeId;
		this.ingredients = ItemStorageHelper.filterNotEmpty(ingredients);
		this.output = output;
		this.secondaryOutputs = ItemStorageHelper.getCraftingRemainings(ingredients);
	}

	public void serialize(@NotNull IFactoryController controller, @NotNull CompoundTag tag)
	{
		tag.putString("recipeId", this.recipeId.toString());
		NBTUtils2.serializeCollection(tag, "ingredients", this.ingredients, controller::serialize);
		tag.put("output", this.output.serializeNBT());
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(this.recipeId, this.ingredients, this.output.getItem());
	}

	@Override
	public boolean equals(@NotNull Object o)
	{
		if (this == o)
		{
			return true;
		}
		else if (o instanceof SimpleRecipeStorage other)
		{
			return this.recipeId.equals(other.recipeId) && this.ingredients.equals(other.ingredients) && ItemStackHelper.equals(this.output, other.output);
		}

		return false;
	}

	@NotNull
	public ResourceLocation getRecipeId()
	{
		return this.recipeId;
	}

	@Override
	@NotNull
	public List<ItemStorage> getInput()
	{
		return this.ingredients;
	}

	@Override
	@NotNull
	public List<ItemStack> getSecondaryOutputs()
	{
		return this.secondaryOutputs;
	}

	@NotNull
	public List<ItemStorage> getIngredients()
	{
		return this.ingredients;
	}

	@Override
	@NotNull
	public GENERIC_RECIPE getGenericRecipe()
	{
		if (this.genericRecipe == null)
		{
			this.genericRecipe = this.getGenericRecipeFactory().create(this.recipeId, ItemStorageHelper.getAmountedStacksLists(this.ingredients), this.output);
		}

		return this.genericRecipe;
	}

	@NotNull
	protected abstract GenericRecipeFactory<GENERIC_RECIPE> getGenericRecipeFactory();

	public interface GenericRecipeFactory<GENERIC_RECIPE>
	{
		@NotNull
		GENERIC_RECIPE create(ResourceLocation recipeId, List<List<ItemStack>> ingredients, ItemStack output);
	}

}
