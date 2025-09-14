package steve_gall.minecolonies_compatibility.core.common.crafting;

import java.util.Arrays;
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

public class SmithingRecipeStorage implements ICustomizedRecipeStorage
{
	public static final ResourceLocation ID = MineColoniesCompatibility.rl("smithing");
	public static final String TAG_RECIPE_ID = "recipeId";
	public static final String TAG_BASE = "base";
	public static final String TAG_ADDITION = "addition";
	public static final String TAG_RESULT = "result";

	public static void serialize(IFactoryController controller, CompoundTag tag, SmithingRecipeStorage recipe)
	{
		tag.putString(TAG_RECIPE_ID, recipe.recipeId.toString());
		tag.put(TAG_BASE, controller.serialize(recipe.base));
		tag.put(TAG_ADDITION, controller.serialize(recipe.addition));
		tag.put(TAG_RESULT, recipe.result.serializeNBT());
	}

	public static SmithingRecipeStorage deserialize(IFactoryController controller, CompoundTag tag)
	{
		var recipeId = new ResourceLocation(tag.getString(TAG_RECIPE_ID));
		ItemStorage base = controller.deserialize(tag.getCompound(TAG_BASE));
		ItemStorage addition = controller.deserialize(tag.getCompound(TAG_ADDITION));
		var result = ItemStack.of(tag.getCompound(TAG_RESULT));
		return new SmithingRecipeStorage(recipeId, base, addition, result);
	}

	private final ResourceLocation recipeId;
	private final ItemStorage base;
	private final ItemStorage addition;
	private final ItemStack result;

	private final List<ItemStorage> input;

	public SmithingRecipeStorage(ResourceLocation recipeId, ItemStorage base, ItemStorage addition, ItemStack result)
	{
		this.recipeId = recipeId;
		this.base = base;
		this.addition = addition;
		this.result = result;

		this.input = Arrays.asList(base, addition);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(this.recipeId, this.base, this.addition, this.result.getItem());
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o)
		{
			return true;
		}
		else if (o instanceof SmithingRecipeStorage other)
		{
			return this.recipeId.equals(other.recipeId) //
					&& this.base.equals(other.base) //
					&& this.addition.equals(other.addition)//
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

	public ItemStorage getBase()
	{
		return this.base;
	}

	public ItemStorage getAddition()
	{
		return this.addition;
	}

	@Override
	public List<ItemStorage> getInput()
	{
		return this.input;
	}

	@Override
	public int getGridSize()
	{
		return 2;
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
