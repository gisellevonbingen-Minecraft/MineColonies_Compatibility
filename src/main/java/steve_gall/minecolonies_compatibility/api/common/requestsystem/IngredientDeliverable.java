package steve_gall.minecolonies_compatibility.api.common.requestsystem;

import java.util.Arrays;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_tweaks.api.common.requestsystem.IDeliverableObject;

public class IngredientDeliverable implements IDeliverableObject
{
	public static final ResourceLocation ID = MineColoniesCompatibility.rl("ingredient");
	private static final Gson GSON = new Gson();

	public static IngredientDeliverable deserialize(@NotNull CompoundTag tag)
	{
		var ingredient = Ingredient.fromJson(GSON.fromJson(tag.getString("ingredient"), JsonElement.class));
		var description = tag.getString("description");
		var count = tag.getInt("count");
		var minCount = tag.getInt("minCount");
		return new IngredientDeliverable(ingredient, description, count, minCount);
	}

	public static void serialize(@NotNull IngredientDeliverable request, @NotNull CompoundTag tag)
	{
		tag.putString("ingredient", GSON.toJson(request.ingredient.toJson()));
		tag.putString("description", request.description);
		tag.putInt("count", request.count);
		tag.putInt("minCount", request.minCount);
	}

	@NotNull
	private final Ingredient ingredient;
	@NotNull
	private final String description;

	private final int count;
	private final int minCount;

	public IngredientDeliverable(@NotNull Ingredient ingredient, @NotNull String description, int count)
	{
		this(ingredient, description, count, count);
	}

	public IngredientDeliverable(@NotNull Ingredient ingredient, @NotNull String description, int count, int minCount)
	{
		this.ingredient = ingredient;
		this.description = description;

		this.count = count;
		this.minCount = Math.min(count, minCount);
	}

	@Override
	public @NotNull ResourceLocation getId()
	{
		return ID;
	}

	@Override
	public @NotNull Component getShortDisplayString()
	{
		return Component.translatable(this.description);
	}

	@Override
	public @NotNull List<ItemStack> getDisplayStacks()
	{
		return Arrays.asList(this.ingredient.getItems());
	}

	@Override
	public @NotNull IDeliverableObject copyWithCount(int newCount)
	{
		return new IngredientDeliverable(this.ingredient, this.description, newCount, this.minCount);
	}

	@Override
	public int getCount()
	{
		return this.count;
	}

	@Override
	public int getMinimumCount()
	{
		return this.minCount;
	}

	@Override
	public boolean matches(@NotNull ItemStack stack)
	{
		return this.ingredient.test(stack);
	}

	public @NotNull Ingredient getIngredient()
	{
		return this.ingredient;
	}

}
