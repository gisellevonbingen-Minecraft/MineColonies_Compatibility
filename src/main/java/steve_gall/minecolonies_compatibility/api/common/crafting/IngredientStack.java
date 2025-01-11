package steve_gall.minecolonies_compatibility.api.common.crafting;

import java.util.Objects;

import org.jetbrains.annotations.NotNull;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public class IngredientStack
{
	public static final IngredientStack EMPTY = of(Ingredient.EMPTY, 0);

	public static @NotNull IngredientStack of(@NotNull Ingredient ingredient)
	{
		return of(ingredient, 1);
	}

	public static @NotNull IngredientStack of(@NotNull Ingredient ingredient, int count)
	{
		return new IngredientStack(ingredient, count);
	}

	@NotNull
	private final Ingredient ingredient;
	private final int count;
	private final boolean isEmpty;

	public IngredientStack(@NotNull Ingredient ingredient, int count)
	{
		this.ingredient = ingredient;
		this.count = ingredient.isEmpty() ? 0 : Math.max(count, 0);
		this.isEmpty = ingredient.isEmpty() || count <= 0;
	}

	public boolean testType(@NotNull ItemStack item)
	{
		return this.ingredient.test(item);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(this.ingredient.getStackingIds().hashCode(), this.count);
	}

	@Override
	public boolean equals(Object object)
	{
		if (object instanceof IngredientStack other)
		{
			return this.ingredient.getStackingIds().equals(other.ingredient.getStackingIds()) && this.count == other.count;
		}

		return false;
	}

	public boolean isEmpty()
	{
		return this.isEmpty;
	}

	public @NotNull Ingredient ingredient()
	{
		return this.ingredient;
	}

	public int count()
	{
		return this.count;
	}

}
