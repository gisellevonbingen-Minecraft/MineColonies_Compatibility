package steve_gall.minecolonies_compatibility.core.common.crafting;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.minecolonies.api.colony.requestsystem.factory.IFactoryController;
import com.minecolonies.api.crafting.ItemStorage;
import com.minecolonies.api.util.constant.IToolType;
import com.minecolonies.api.util.constant.ToolType;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.core.common.item.ItemStackHelper;
import steve_gall.minecolonies_tweaks.api.common.crafting.ICustomizedRecipeStorage;

public class StonecutterRecipeStorage implements ICustomizedRecipeStorage
{
	public static final ResourceLocation ID = MineColoniesCompatibility.rl("stonecutting");
	public static final String TAG_RECIPE_ID = "recipeId";
	public static final String TAG_INGREDIENT = "ingredient";
	public static final String TAG_RESULT = "result";

	public static void serialize(IFactoryController controller, CompoundTag tag, StonecutterRecipeStorage recipe)
	{
		tag.putString(TAG_RECIPE_ID, recipe.recipeId.toString());
		tag.put(TAG_INGREDIENT, controller.serialize(recipe.ingredient));
		tag.put(TAG_RESULT, recipe.result.serializeNBT());
	}

	public static StonecutterRecipeStorage deserialize(IFactoryController controller, CompoundTag tag)
	{
		var recipeId = new ResourceLocation(tag.getString(TAG_RECIPE_ID));
		ItemStorage ingredient = controller.deserialize(tag.getCompound(TAG_INGREDIENT));
		var result = ItemStack.of(tag.getCompound(TAG_RESULT));
		return new StonecutterRecipeStorage(recipeId, ingredient, result);
	}

	private final ResourceLocation recipeId;
	private final ItemStorage ingredient;
	private final ItemStack result;

	private final List<ItemStorage> input;

	public StonecutterRecipeStorage(ResourceLocation recipeId, ItemStorage ingredient, ItemStack result)
	{
		this.recipeId = recipeId;
		this.ingredient = ingredient;
		this.result = result;

		this.input = Collections.singletonList(ingredient);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(this.recipeId, this.ingredient, this.result.getItem());
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o)
		{
			return true;
		}
		else if (o instanceof StonecutterRecipeStorage other)
		{
			return this.recipeId.equals(other.recipeId) //
					&& this.ingredient.equals(other.ingredient) //
					&& ItemStackHelper.equals(this.result, other.result);
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

	public ItemStorage getIngredient()
	{
		return this.ingredient;
	}

	@Override
	public List<ItemStorage> getInput()
	{
		return this.input;
	}

	@Override
	public int getGridSize()
	{
		return 1;
	}

	@Override
	public ItemStack getPrimaryOutput()
	{
		return this.result;
	}

	@Override
	public List<ItemStack> getAlternateOutputs()
	{
		return Collections.emptyList();
	}

	@Override
	public List<ItemStack> getSecondaryOutputs()
	{
		return Collections.emptyList();
	}

	@Override
	public Block getIntermediate()
	{
		return Blocks.AIR;
	}

	@Override
	public ResourceLocation getRecipeSource()
	{
		return null;
	}

	@Override
	public ResourceLocation getRecipeType()
	{
		return null;
	}

	@Override
	public ResourceLocation getLootTable()
	{
		return null;
	}

	@Override
	public IToolType getRequiredTool()
	{
		return ToolType.NONE;
	}

}
