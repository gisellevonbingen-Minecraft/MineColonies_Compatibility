package steve_gall.minecolonies_compatibility.module.common.farmersdelight.crafting;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.minecolonies.api.colony.requestsystem.factory.IFactoryController;
import com.minecolonies.api.crafting.ItemStorage;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraftforge.registries.ForgeRegistries;
import steve_gall.minecolonies_compatibility.api.common.crafting.GenericedRecipeStorage;
import steve_gall.minecolonies_compatibility.api.common.crafting.ISecondaryRollableRecipeStorage;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import vectorwing.farmersdelight.common.block.FeastBlock;

public class PlatingRecipeStorage extends GenericedRecipeStorage<PlatingGenericRecipe> implements ISecondaryRollableRecipeStorage
{
	public static final ResourceLocation ID = MineColoniesCompatibility.rl("farmerdelight_plating");

	public static void serialize(IFactoryController controller, CompoundTag tag, PlatingRecipeStorage recipe)
	{
		tag.putString("block", ForgeRegistries.BLOCKS.getKey(recipe.block).toString());
	}

	public static PlatingRecipeStorage deserialize(IFactoryController controller, CompoundTag tag)
	{
		var blockId = new ResourceLocation(tag.getString("block"));
		var block = ForgeRegistries.BLOCKS.getValue(blockId);
		return new PlatingRecipeStorage(block);
	}

	private final Block block;
	private final List<ItemStorage> ingredients;
	private final ItemStack container;
	private final ItemStack output;
	private final PlatingGenericRecipe recipe;

	private PlatingRecipeStorage(Block block)
	{
		this.block = block;

		var ingredients = new ArrayList<ItemStorage>();
		var container = ItemStack.EMPTY;
		var output = ItemStack.EMPTY;

		if (block instanceof FeastBlock feastBlock)
		{
			var state = feastBlock.defaultBlockState();
			var maxServings = feastBlock.getMaxServings();
			ingredients.add(new ItemStorage(new ItemStack(block)));

			for (var i = 0; i < maxServings; i++)
			{
				state = state.setValue(feastBlock.getServingsProperty(), maxServings - i);
				var servingItem = feastBlock.getServingItem(state);

				if (!output.isEmpty() && !ItemStack.isSameItemSameTags(output, servingItem))
				{
					output = ItemStack.EMPTY;
					container = ItemStack.EMPTY;
					break;
				}

				if (output.isEmpty())
				{
					output = servingItem.copy();
				}
				else
				{
					output.setCount(output.getCount() + servingItem.getCount());
				}

				if (servingItem.hasCraftingRemainingItem())
				{
					var containerItem = servingItem.getCraftingRemainingItem();

					if (!container.isEmpty() && !ItemStack.isSameItemSameTags(container, containerItem))
					{
						output = ItemStack.EMPTY;
						container = ItemStack.EMPTY;
						break;
					}

					if (container.isEmpty())
					{
						container = containerItem.copy();
					}
					else
					{
						container.setCount(container.getCount() + containerItem.getCount());
					}

				}

			}

		}

		if (!container.isEmpty())
		{
			ingredients.add(new ItemStorage(container));
		}

		this.ingredients = ingredients;
		this.container = container;
		this.output = output;
		this.recipe = new PlatingGenericRecipe(this);
	}

	public PlatingRecipeStorage(FeastBlock block)
	{
		this((Block) block);
	}

	@Override
	public @NotNull List<ItemStack> rollSecondaryOutputs(@NotNull LootParams context)
	{
		var list = new ArrayList<ItemStack>();

		if (this.block instanceof FeastBlock feastBlock)
		{
			var state = feastBlock.defaultBlockState().setValue(feastBlock.getServingsProperty(), 0);
			return Block.getDrops(state, context.getLevel(), BlockPos.ZERO, null);
		}

		return list;
	}

	@Override
	public int hashCode()
	{
		return this.block.hashCode();
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o)
		{
			return true;
		}
		else if (o instanceof PlatingRecipeStorage other)
		{
			return this.block == other.block;
		}

		return false;
	}

	@Override
	public ResourceLocation getId()
	{
		return ID;
	}

	public Block getBlock()
	{
		return this.block;
	}

	@Override
	public List<ItemStorage> getInput()
	{
		return this.ingredients;
	}

	public ItemStack getContainer()
	{
		return this.container;
	}

	@Override
	public ItemStack getPrimaryOutput()
	{
		return this.output;
	}

	@Override
	public PlatingGenericRecipe getGenericRecipe()
	{
		return this.recipe;
	}

}
