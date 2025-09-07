package steve_gall.minecolonies_compatibility.module.common.farmersdelight.crafting;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.jetbrains.annotations.NotNull;

import com.google.common.collect.ImmutableList;
import com.minecolonies.api.IMinecoloniesAPI;
import com.minecolonies.api.colony.requestsystem.factory.IFactoryController;
import com.minecolonies.api.crafting.ItemStorage;
import com.minecolonies.api.equipment.registry.EquipmentTypeEntry;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootParams;
import steve_gall.minecolonies_compatibility.api.common.crafting.GenericedRecipeStorage;
import steve_gall.minecolonies_compatibility.api.common.crafting.ISecondaryRollableRecipeStorage;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.core.common.crafting.ItemStorageHelper;
import steve_gall.minecolonies_compatibility.core.common.util.NBTUtils2;
import steve_gall.minecolonies_tweaks.core.common.util.SerializationHelper;

public class CuttingRecipeStorage extends GenericedRecipeStorage<CuttingGenericRecipe> implements ISecondaryRollableRecipeStorage
{
	public static final ResourceLocation ID = MineColoniesCompatibility.rl("farmerdelight_cutting");

	public static void serialize(HolderLookup.Provider provider, IFactoryController controller, CompoundTag tag, CuttingRecipeStorage recipe)
	{
		tag.putString("recipeId", recipe.recipeId.toString());
		NBTUtils2.serializeCollection(tag, "ingredients", recipe.ingredients, SerializationHelper.serializerTag(provider));
		NBTUtils2.serializeCollection(tag, "results", recipe.results, SerializationHelper.apply(provider, CuttingChanceResult::serialize));
		tag.putString("toolType", recipe.toolType.getRegistryName().toString());
	}

	public static CuttingRecipeStorage deserialize(HolderLookup.Provider provider, IFactoryController controller, CompoundTag tag)
	{
		var recipeId = ResourceLocation.parse(tag.getString("recipeId"));
		List<ItemStorage> ingredients = NBTUtils2.deserializeList(tag, "ingredients", SerializationHelper.deserializerTag(provider));
		var results = NBTUtils2.deserializeList(tag, "results", SerializationHelper.apply(provider, CuttingChanceResult::deserialize));
		var toolType = IMinecoloniesAPI.getInstance().getEquipmentTypeRegistry().get(ResourceLocation.parse(tag.getString("toolType")));

		return new CuttingRecipeStorage(recipeId, ingredients, results, toolType);
	}

	private final ResourceLocation recipeId;
	private final List<ItemStorage> ingredients;
	private final List<CuttingChanceResult> results;
	private final EquipmentTypeEntry toolType;

	private final CuttingGenericRecipe genericRecipe;

	public CuttingRecipeStorage(ResourceLocation recipeId, List<ItemStorage> ingredients, List<CuttingChanceResult> results, EquipmentTypeEntry toolType)
	{
		this.recipeId = recipeId;
		this.ingredients = ItemStorageHelper.filterNotEmpty(ingredients);
		this.results = ImmutableList.copyOf(results);
		this.toolType = toolType;
		this.genericRecipe = new CuttingGenericRecipe(recipeId, ItemStorageHelper.getStacksLists(ingredients), results, toolType);
	}

	@Override
	public @NotNull List<ItemStack> rollSecondaryOutputs(@NotNull LootParams context)
	{
		var list = new ArrayList<ItemStack>();

		for (var result : this.genericRecipe.getAdditionalResults())
		{
			var roll = context.getLevel().getRandom().nextDouble();

			if (roll <= result.getChance())
			{
				list.add(result.getStack());
			}

		}

		return list;
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(this.recipeId, this.ingredients, this.results, this.toolType);
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o)
		{
			return true;
		}
		else if (o instanceof CuttingRecipeStorage other)
		{
			return this.recipeId.equals(other.recipeId) && this.ingredients.equals(other.ingredients) && this.results.equals(other.results) && this.toolType.equals(other.toolType);
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
		return this.ingredients;
	}

	public List<CuttingChanceResult> getResults()
	{
		return this.results;
	}

	@Override
	public @NotNull CuttingGenericRecipe getGenericRecipe()
	{
		return this.genericRecipe;
	}

}
