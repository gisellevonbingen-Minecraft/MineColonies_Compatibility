package steve_gall.minecolonies_compatibility.api.common.crafting;

import org.jetbrains.annotations.NotNull;

import com.minecolonies.api.util.constant.ToolType;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import steve_gall.minecolonies_compatibility.core.common.item.ItemStackHelper;
import steve_gall.minecolonies_tweaks.api.common.tool.CustomToolType;

public class ToolOrIngredientStack
{
	public static final ToolOrIngredientStack EMPTY = of(Ingredient.EMPTY);

	private final boolean isToolType;
	@NotNull
	private final ToolType toolType;
	@NotNull
	private final IngredientStack stack;
	private final boolean isEmpty;

	public ToolOrIngredientStack(boolean isToolType, @NotNull ToolType toolType, @NotNull IngredientStack stack)
	{
		this.isToolType = isToolType;
		this.toolType = toolType;
		this.stack = stack;
		this.isEmpty = isToolType ? toolType == ToolType.NONE : stack.isEmpty();
	}

	public boolean isToolType()
	{
		return this.isToolType;
	}

	public @NotNull ToolType toolType()
	{
		return this.toolType;
	}

	public @NotNull IngredientStack stack()
	{
		return this.stack;
	}

	public boolean isEmpty()
	{
		return this.isEmpty;
	}

	public boolean testType(@NotNull ItemStack item)
	{
		return this.isToolType ? ItemStackHelper.isTool(item, this.toolType) : this.stack.testType(item);
	}

	@Override
	public int hashCode()
	{
		return this.isToolType ? this.toolType.hashCode() : this.stack.hashCode();
	}

	@Override
	public boolean equals(Object object)
	{
		if (object instanceof ToolOrIngredientStack other)
		{
			if (this.isToolType != other.isToolType)
			{
				return false;
			}
			else if (this.isToolType)
			{
				return this.toolType == other.toolType;
			}
			else
			{
				return this.stack.equals(other.stack);
			}

		}

		return false;
	}

	public static @NotNull ToolOrIngredientStack of(@NotNull CustomToolType toolType)
	{
		return of(toolType.getToolType());
	}

	public static @NotNull ToolOrIngredientStack of(@NotNull ToolType toolType)
	{
		return new ToolOrIngredientStack(true, toolType, IngredientStack.EMPTY);
	}

	public static @NotNull ToolOrIngredientStack of(@NotNull Ingredient ingredient)
	{
		return new ToolOrIngredientStack(false, ToolType.NONE, IngredientStack.of(ingredient));
	}

	public static @NotNull ToolOrIngredientStack of(@NotNull Ingredient ingredient, int count)
	{
		return new ToolOrIngredientStack(false, ToolType.NONE, IngredientStack.of(ingredient, count));
	}

}
