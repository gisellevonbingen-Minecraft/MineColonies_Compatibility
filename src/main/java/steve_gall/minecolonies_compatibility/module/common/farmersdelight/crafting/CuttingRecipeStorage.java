package steve_gall.minecolonies_compatibility.module.common.farmersdelight.crafting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.jetbrains.annotations.NotNull;

import com.minecolonies.api.colony.requestsystem.StandardFactoryController;
import com.minecolonies.api.crafting.ItemStorage;
import com.minecolonies.api.util.constant.IToolType;
import com.minecolonies.api.util.constant.ToolType;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import steve_gall.minecolonies_compatibility.api.common.crafting.GenericedRecipeStorage;
import steve_gall.minecolonies_compatibility.api.common.crafting.ISecondaryRollableRecipeStorage;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.core.common.crafting.ItemStorageHelper;
import steve_gall.minecolonies_compatibility.core.common.util.NBTUtils2;

public class CuttingRecipeStorage extends GenericedRecipeStorage<CuttingGenericRecipe> implements ISecondaryRollableRecipeStorage
{
	public static final ResourceLocation ID = MineColoniesCompatibility.rl("farmerdelight_cutting");

	public static void serialize(CompoundTag tag, CuttingRecipeStorage recipe)
	{
		tag.putString("recipeId", recipe.recipeId.toString());
		NBTUtils2.serializeList(tag, "ingreidnts", recipe.ingreidnts, StandardFactoryController.getInstance()::serialize);
		NBTUtils2.serializeList(tag, "results", recipe.results, CuttingChanceResult::serializeNBT);
		tag.putString("toolType", recipe.toolType.getName());
	}

	public static CuttingRecipeStorage deserialize(CompoundTag tag)
	{
		var recipeId = new ResourceLocation(tag.getString("recipeId"));
		List<ItemStorage> ingreidnts = NBTUtils2.deserializeList(tag, "ingreidnts", StandardFactoryController.getInstance()::deserialize);
		var results = NBTUtils2.deserializeList(tag, "results", CuttingChanceResult::new);
		var toolType = ToolType.getToolType(tag.getString("toolType"));
		return new CuttingRecipeStorage(recipeId, ingreidnts, results, toolType);
	}

	private final ResourceLocation recipeId;
	private final List<ItemStorage> ingreidnts;
	private final List<CuttingChanceResult> results;
	private final IToolType toolType;

	private final CuttingGenericRecipe genericRecipe;

	public CuttingRecipeStorage(ResourceLocation recipeId, List<ItemStorage> ingreidnts, List<CuttingChanceResult> results, IToolType toolType)
	{
		this.recipeId = recipeId;
		this.ingreidnts = Collections.unmodifiableList(ingreidnts);
		this.results = Collections.unmodifiableList(results);
		this.toolType = toolType;
		this.genericRecipe = new CuttingGenericRecipe(recipeId, ItemStorageHelper.getStacksLists(ingreidnts), results, toolType);
	}

	@Override
	public @NotNull List<ItemStack> rollSecondaryOutputs(@NotNull LootContext context)
	{
		var list = new ArrayList<ItemStack>();

		for (var result : this.genericRecipe.getAdditionalResults())
		{
			var roll = context.getRandom().nextDouble();

			if (roll <= result.getChance())
			{
				list.add(result.getStack());
			}

		}

		list.addAll(ItemStorageHelper.getCraftingRemainings(this.ingreidnts));
		return list;
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(this.recipeId, this.ingreidnts, this.results, this.toolType);
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
			return this.recipeId.equals(other.recipeId) && this.ingreidnts.equals(other.ingreidnts) && this.results.equals(other.results) && this.toolType.equals(other.toolType);
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
		return this.ingreidnts;
	}

	public List<CuttingChanceResult> getResults()
	{
		return this.results;
	}

	@Override
	protected @NotNull CuttingGenericRecipe getGenericRecipe()
	{
		return this.genericRecipe;
	}

}
