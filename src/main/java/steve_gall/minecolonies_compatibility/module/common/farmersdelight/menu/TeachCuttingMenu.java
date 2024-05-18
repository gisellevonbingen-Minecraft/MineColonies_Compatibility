package steve_gall.minecolonies_compatibility.module.common.farmersdelight.menu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.minecolonies.api.crafting.registry.CraftingType;
import com.minecolonies.api.util.constant.IToolType;
import com.minecolonies.api.util.constant.ToolType;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.TransientCraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import steve_gall.minecolonies_compatibility.api.common.inventory.GhostSlot;
import steve_gall.minecolonies_compatibility.core.common.crafting.IngredientHelper;
import steve_gall.minecolonies_compatibility.core.common.inventory.ReadOnlySlotsContainer;
import steve_gall.minecolonies_compatibility.core.common.inventory.TeachRecipeMenu;
import steve_gall.minecolonies_compatibility.core.common.item.ItemStackHelper;
import steve_gall.minecolonies_compatibility.module.common.farmersdelight.init.ModuleCraftingTypes;
import steve_gall.minecolonies_compatibility.module.common.farmersdelight.init.ModuleMenuTypes;
import vectorwing.farmersdelight.common.crafting.CuttingBoardRecipe;
import vectorwing.farmersdelight.common.crafting.ingredient.ChanceResult;
import vectorwing.farmersdelight.common.registry.ModRecipeTypes;

public class TeachCuttingMenu extends TeachRecipeMenu<CuttingBoardRecipe, RecipeWrapper>
{
	public static final int INVENTORY_X = 8;
	public static final int INVENTORY_Y = 84;

	public static final int CRAFTING_SLOTS = 1;
	public static final int CRAFTING_COLS = 1;
	public static final int CRAFTING_X = 14;
	public static final int CRAFTING_Y = 36;

	public static final int RESULT_X = 73;
	public static final int RESULT_Y = 18;
	public static final int RESULT_COLUMNS = 5;

	private final IToolType toolType;

	private List<ChanceResult> results;

	public TeachCuttingMenu(int windowId, Inventory inventory, BlockPos buildingId, int moduleId, IToolType toolType)
	{
		super(ModuleMenuTypes.TEACH_CUTTING.get(), windowId, inventory, buildingId, moduleId);
		this.toolType = toolType;
		this.setup();
	}

	public TeachCuttingMenu(int windowId, Inventory inventory, FriendlyByteBuf buffer)
	{
		super(ModuleMenuTypes.TEACH_CUTTING.get(), windowId, inventory, buffer);
		this.toolType = ToolType.getToolType(buffer.readUtf());
		this.setup();
	}

	private void setup()
	{
		this.addInventorySlots(INVENTORY_X, INVENTORY_Y);

		this.craftMatrix = new TransientCraftingContainer(this, 1, 1);

		for (var i = 0; i < CRAFTING_SLOTS; i++)
		{
			var col = i % CRAFTING_COLS;
			var row = i / CRAFTING_COLS;
			this.craftSlots.add(this.addSlot(new GhostSlot(this.craftMatrix, i, CRAFTING_X + col * SLOT_OFFSET, CRAFTING_Y + row * SLOT_OFFSET)
			{
				@Override
				public boolean mayPlace(ItemStack stack)
				{
					return true;
				}

				@Override
				public boolean mayPickup(Player stack)
				{
					return false;
				}
			}));
		}

		this.results = new ArrayList<>();
		this.resultContainer = new ReadOnlySlotsContainer(this.results::size, i -> this.results.get(i).getStack());
	}

	@Override
	public void onRecipeTransfer(CuttingBoardRecipe recipe, CompoundTag tag)
	{
		super.onRecipeTransfer(recipe, tag);

		this.craftSlots.get(0).set(ItemStack.of(tag));
	}

	@Override
	protected void onSetRecipe(CuttingBoardRecipe recipe)
	{
		var prevSlots = this.resultSlots.size();
		this.results.clear();

		if (recipe != null && this.testRecipe(recipe))
		{
			this.recipe = recipe;
			this.results.addAll(this.recipe.getRollableResults());
		}
		else
		{
			this.recipe = null;
		}

		var addingSlots = this.results.size() - prevSlots;

		for (var i = 0; i < addingSlots; i++)
		{
			var index = prevSlots + i;
			var xi = index % RESULT_COLUMNS;
			var yi = index / RESULT_COLUMNS;
			var x = RESULT_X + xi * SLOT_OFFSET;
			var y = RESULT_Y + yi * SLOT_OFFSET;
			this.resultSlots.add(this.addSlot(new Slot(this.resultContainer, index, x, y)
			{
				@Override
				public boolean mayPickup(Player player)
				{
					return false;
				}

				@Override
				public boolean isActive()
				{
					return !this.getItem().isEmpty();
				}

			}));

		}

	}

	public boolean testRecipe(CuttingBoardRecipe recipe)
	{
		return ItemStackHelper.isTool(IngredientHelper.getStacks(recipe.getTool()), this.getToolType());
	}

	@Override
	public RecipeType<CuttingBoardRecipe> getRecipeType()
	{
		return ModRecipeTypes.CUTTING.get();
	}

	@Override
	public CraftingType getCraftingType()
	{
		return ModuleCraftingTypes.CUTTING.get();
	}

	@Override
	protected RecipeWrapper createRecipeContainer(CraftingContainer craftContainer)
	{
		return new RecipeWrapper(new InvWrapper(craftContainer));
	}

	public IToolType getToolType()
	{
		return this.toolType;
	}

	public List<ChanceResult> getResults()
	{
		return Collections.unmodifiableList(this.results);
	}

}
