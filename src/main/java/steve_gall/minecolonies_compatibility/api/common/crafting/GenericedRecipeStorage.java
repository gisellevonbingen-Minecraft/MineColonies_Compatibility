package steve_gall.minecolonies_compatibility.api.common.crafting;

import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.minecolonies.api.crafting.IGenericRecipe;
import com.minecolonies.api.equipment.registry.EquipmentTypeEntry;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import steve_gall.minecolonies_tweaks.api.common.crafting.ICustomizedRecipeStorage;

public abstract class GenericedRecipeStorage<GENERIC extends IGenericRecipe> implements ICustomizedRecipeStorage
{
	@NotNull
	public abstract GENERIC getGenericRecipe();

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
		return Collections.emptyList();
	}

	@Override
	public List<ItemStack> getSecondaryOutputs()
	{
		return this.getGenericRecipe().getAdditionalOutputs();
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
	public EquipmentTypeEntry getRequiredTool()
	{
		return this.getGenericRecipe().getRequiredTool();
	}

}
