package steve_gall.minecolonies_compatibility.module.common.farmersdelight.crafting;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.jetbrains.annotations.NotNull;

import com.minecolonies.api.colony.requestsystem.factory.IFactoryController;
import com.minecolonies.api.crafting.ItemStorage;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import steve_gall.minecolonies_compatibility.api.common.crafting.GenericedRecipeStorage;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.core.common.crafting.ItemStorageHelper;
import steve_gall.minecolonies_compatibility.core.common.item.ItemStackHelper;
import steve_gall.minecolonies_compatibility.core.common.util.NBTUtils2;

public class CookingRecipeStorage extends GenericedRecipeStorage<CookingGenericRecipe>
{
	public static final ResourceLocation ID = MineColoniesCompatibility.rl("farmerdelight_cooking");

	public static void serialize(IFactoryController controller, CompoundTag tag, CookingRecipeStorage recipe)
	{
		tag.putString("recipeId", recipe.recipeId.toString());
		NBTUtils2.serializeCollection(tag, "ingreidnts", recipe.ingreidnts, controller::serialize);
		tag.put("container", controller.serialize(recipe.container));
		tag.put("output", recipe.output.serializeNBT());
	}

	public static CookingRecipeStorage deserialize(IFactoryController controller, CompoundTag tag)
	{
		var recipeId = new ResourceLocation(tag.getString("recipeId"));
		List<ItemStorage> ingreidnts = NBTUtils2.deserializeList(tag, "ingreidnts", controller::deserialize);
		ItemStorage container = controller.deserialize(tag.getCompound("container"));
		var output = ItemStack.of(tag.getCompound("output"));
		return new CookingRecipeStorage(recipeId, ingreidnts, container, output);
	}

	private final ResourceLocation recipeId;
	private final List<ItemStorage> ingreidnts;
	private final ItemStorage container;
	private final ItemStack output;
	private final List<ItemStack> secondaryOutputs;

	private final CookingGenericRecipe genericRecipe;

	public CookingRecipeStorage(ResourceLocation recipeId, List<ItemStorage> ingreidnts, ItemStorage container, ItemStack output)
	{
		this.recipeId = recipeId;
		this.ingreidnts = ItemStorageHelper.filterNotEmpty(ingreidnts);
		this.container = container;
		this.output = output;
		this.secondaryOutputs = ItemStorageHelper.mapAndFilterNotEmpty(ingreidnts, CookingGenericRecipe::getCraftingRemainingStack);

		this.genericRecipe = new CookingGenericRecipe(recipeId, ItemStorageHelper.getStacksLists(ingreidnts), ItemStorageHelper.getStacks(container), output);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(this.recipeId, this.ingreidnts, this.container.getItem(), this.output.getItem());
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o)
		{
			return true;
		}
		else if (o instanceof CookingRecipeStorage other)
		{
			return this.recipeId.equals(other.recipeId) && this.ingreidnts.equals(other.ingreidnts) && this.container.equals(other.container) && ItemStackHelper.equals(this.output, other.output);
		}

		return false;
	}

	@Override
	public ResourceLocation getId()
	{
		return ID;
	}

	public ResourceLocation getRecipeId()
	{
		return this.recipeId;
	}

	@Override
	public List<ItemStorage> getInput()
	{
		var input = new ArrayList<ItemStorage>();
		input.addAll(this.ingreidnts);

		if (!this.container.isEmpty())
		{
			input.add(this.container);
		}

		return input;
	}

	@Override
	public List<ItemStack> getSecondaryOutputs()
	{
		return this.secondaryOutputs;
	}

	public List<ItemStorage> getIngredients()
	{
		return this.ingreidnts;
	}

	public ItemStorage getContainer()
	{
		return this.container;
	}

	@Override
	public @NotNull CookingGenericRecipe getGenericRecipe()
	{
		return this.genericRecipe;
	}

}
