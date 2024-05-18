package steve_gall.minecolonies_compatibility.module.common.farmersdelight.crafting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.jetbrains.annotations.NotNull;

import com.minecolonies.api.colony.requestsystem.StandardFactoryController;
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

	public static void serialize(CompoundTag tag, CookingRecipeStorage recipe)
	{
		tag.putString("recipeId", recipe.recipeId.toString());
		NBTUtils2.serializeList(tag, "ingreidnts", recipe.ingreidnts, StandardFactoryController.getInstance()::serialize);
		tag.put("container", StandardFactoryController.getInstance().serialize(recipe.container));
		tag.put("output", recipe.output.serializeNBT());
	}

	public static CookingRecipeStorage deserialize(CompoundTag tag)
	{
		var recipeId = new ResourceLocation(tag.getString("recipeId"));
		List<ItemStorage> ingreidnts = NBTUtils2.deserializeList(tag, "ingreidnts", StandardFactoryController.getInstance()::deserialize);
		ItemStorage container = StandardFactoryController.getInstance().deserialize(tag.getCompound("container"));
		var output = ItemStack.of(tag.getCompound("output"));
		return new CookingRecipeStorage(recipeId, ingreidnts, container, output);
	}

	private final ResourceLocation recipeId;
	private final List<ItemStorage> ingreidnts;
	private final ItemStorage container;
	private final ItemStack output;

	private final CookingGenericRecipe genericRecipe;

	public CookingRecipeStorage(ResourceLocation recipeId, List<ItemStorage> ingreidnts, ItemStorage container, ItemStack output)
	{
		this.recipeId = recipeId;
		this.ingreidnts = Collections.unmodifiableList(ingreidnts);
		this.container = container;
		this.output = output;
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

	@Override
	public List<ItemStorage> getInput()
	{
		var input = new ArrayList<ItemStorage>();
		input.addAll(this.ingreidnts);
		input.add(this.container);

		return input;
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
	protected @NotNull CookingGenericRecipe getGenericRecipe()
	{
		return this.genericRecipe;
	}

}
