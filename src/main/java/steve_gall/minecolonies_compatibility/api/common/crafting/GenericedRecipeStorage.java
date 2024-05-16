package steve_gall.minecolonies_compatibility.api.common.crafting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.minecolonies.api.crafting.IGenericRecipe;
import com.minecolonies.api.util.constant.IToolType;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import steve_gall.minecolonies_compatibility.core.common.crafting.ItemStorageHelper;
import steve_gall.minecolonies_tweaks.api.common.crafting.ICustomizedRecipeStorage;

public abstract class GenericedRecipeStorage<GENERIC extends IGenericRecipe> implements ICustomizedRecipeStorage
{
	@NotNull
	protected abstract GENERIC getGenericRecipe();

	@Override
	public int getGridSize()
	{
		return this.getGenericRecipe().getGridSize();
	}

	@Override
	public ItemStack getPrimaryOutput()
	{
		return this.getGenericRecipe().getPrimaryOutput();
	}

	@Override
	public List<ItemStack> getAlternateOutputs()
	{
		return this.getGenericRecipe().getAdditionalOutputs();
	}

	@Override
	public List<ItemStack> getSecondaryOutputs()
	{
		var list = new ArrayList<ItemStack>();
		list.addAll(this.getGenericRecipe().getAdditionalOutputs());
		list.addAll(ItemStorageHelper.getCraftingRemainings(this.getInput()));
		return Collections.unmodifiableList(list);
	}

	@Override
	public Block getIntermediate()
	{
		return this.getGenericRecipe().getIntermediate();
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
		return this.getGenericRecipe().getLootTable();
	}

	@Override
	public IToolType getRequiredTool()
	{
		return this.getGenericRecipe().getRequiredTool();
	}

}
