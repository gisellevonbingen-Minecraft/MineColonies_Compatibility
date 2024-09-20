package steve_gall.minecolonies_compatibility.core.common.crafting;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import com.minecolonies.api.colony.requestsystem.StandardFactoryController;
import com.minecolonies.api.crafting.ItemStorage;
import com.minecolonies.api.equipment.ModEquipmentTypes;
import com.minecolonies.api.equipment.registry.EquipmentTypeEntry;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.core.common.item.ItemStackHelper;
import steve_gall.minecolonies_compatibility.core.common.util.NBTUtils2;
import steve_gall.minecolonies_tweaks.api.common.crafting.ICustomizedRecipeStorage;

public class SmithingTemplateRecipeStorage implements ICustomizedRecipeStorage
{
	public static final ResourceLocation ID = MineColoniesCompatibility.rl("smithing_template");
	public static final String TAG_GRID_SIZE = "gridSize";
	public static final String TAG_INPUT = "input";
	public static final String TAG_INPUT_TEMPLATE_COUNT = "inputTemplateCount";
	public static final String TAG_PRIMARY_OUTPUT = "primaryOutput";
	public static final String TAG_SECONDARY_OUTPUTS = "secondaryOutputs";

	public static void serialize(SmithingTemplateRecipeStorage recipe, CompoundTag tag)
	{
		tag.putInt(TAG_GRID_SIZE, recipe.gridSize);
		NBTUtils2.serializeCollection(tag, TAG_INPUT, recipe.input, StandardFactoryController.getInstance()::serialize);
		tag.putInt(TAG_INPUT_TEMPLATE_COUNT, recipe.inputTemplateCount);
		tag.put(TAG_PRIMARY_OUTPUT, recipe.primaryOutput.serializeNBT());
		NBTUtils2.serializeCollection(tag, TAG_SECONDARY_OUTPUTS, recipe.secondaryOutputs, ItemStack::serializeNBT);
	}

	public static SmithingTemplateRecipeStorage deserialize(CompoundTag tag)
	{
		var gridSize = tag.getInt(TAG_GRID_SIZE);
		List<ItemStorage> input = NBTUtils2.deserializeList(tag, TAG_INPUT, StandardFactoryController.getInstance()::deserialize);
		var inputTemplateCount = tag.getInt(TAG_INPUT_TEMPLATE_COUNT);
		var primaryOutput = ItemStack.of(tag.getCompound(TAG_PRIMARY_OUTPUT));
		var secondaryOutputs = NBTUtils2.deserializeList(tag, TAG_SECONDARY_OUTPUTS, ItemStack::of);
		return new SmithingTemplateRecipeStorage(gridSize, input, inputTemplateCount, primaryOutput, secondaryOutputs);
	}

	private final int gridSize;
	private final List<ItemStorage> input;
	private final int inputTemplateCount;
	private final ItemStack primaryOutput;
	private final List<ItemStack> secondaryOutputs;

	public SmithingTemplateRecipeStorage(int gridSize, List<ItemStorage> input, int inputTemplateCount, ItemStack primaryOutput, List<ItemStack> secondaryOutputs)
	{
		this.gridSize = gridSize;
		this.input = input.stream().map(ItemStorage::copy).toList();
		this.inputTemplateCount = inputTemplateCount;
		this.primaryOutput = primaryOutput.copy();
		this.secondaryOutputs = secondaryOutputs.stream().map(ItemStack::copy).toList();
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(this.gridSize, this.input, this.inputTemplateCount, this.primaryOutput.getItem(), this.hashCodeItemStacks(this.secondaryOutputs));
	}

	private int hashCodeItemStacks(List<ItemStack> stacks)
	{
		var map = new HashMap<Item, Integer>();

		for (var item : stacks)
		{
			map.put(item.getItem(), item.getCount());
		}

		return map.hashCode();
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o)
		{
			return true;
		}
		else if (o instanceof SmithingTemplateRecipeStorage other)
		{
			if (!(this.gridSize == other.gridSize && this.input.equals(other.input) && this.inputTemplateCount == other.inputTemplateCount))
			{
				return false;
			}
			else if (!ItemStackHelper.equals(this.primaryOutput, other.primaryOutput))
			{
				return false;
			}
			else if (this.secondaryOutputs.size() != other.secondaryOutputs.size())
			{
				return false;
			}

			for (var i = 0; i < this.secondaryOutputs.size(); i++)
			{
				if (!ItemStackHelper.equals(this.secondaryOutputs.get(i), other.secondaryOutputs.get(i)))
				{
					return false;
				}

			}

			return true;
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
		return this.input;
	}

	public int getInputTemplateCount()
	{
		return this.inputTemplateCount;
	}

	@Override
	public int getGridSize()
	{
		return this.gridSize;
	}

	@Override
	public ItemStack getPrimaryOutput()
	{
		return this.primaryOutput;
	}

	@Override
	public List<ItemStack> getAlternateOutputs()
	{
		return Collections.emptyList();
	}

	@Override
	public List<ItemStack> getSecondaryOutputs()
	{
		return this.secondaryOutputs;
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
	public EquipmentTypeEntry getRequiredTool()
	{
		return ModEquipmentTypes.none.get();
	}

}
