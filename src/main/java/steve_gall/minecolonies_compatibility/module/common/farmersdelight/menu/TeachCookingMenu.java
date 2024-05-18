package steve_gall.minecolonies_compatibility.module.common.farmersdelight.menu;

import com.minecolonies.api.crafting.registry.CraftingType;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import steve_gall.minecolonies_compatibility.api.common.inventory.GhostSlot;
import steve_gall.minecolonies_compatibility.core.common.inventory.ReadOnlySlotsContainer;
import steve_gall.minecolonies_compatibility.core.common.inventory.TeachRecipeMenu;
import steve_gall.minecolonies_compatibility.core.common.util.NBTUtils2;
import steve_gall.minecolonies_compatibility.module.common.farmersdelight.init.ModuleCraftingTypes;
import steve_gall.minecolonies_compatibility.module.common.farmersdelight.init.ModuleMenuTypes;
import vectorwing.farmersdelight.common.crafting.CookingPotRecipe;
import vectorwing.farmersdelight.common.registry.ModRecipeTypes;

public class TeachCookingMenu extends TeachRecipeMenu<CookingPotRecipe, RecipeWrapper>
{
	public static final int INVENTORY_X = 8;
	public static final int INVENTORY_Y = 84;

	public static final int CRAFTING_SLOTS = CookingPotRecipe.INPUT_SLOTS;
	public static final int CRAFTING_COLS = 3;
	public static final int CRAFTING_X = 26;
	public static final int CRAFTING_Y = 17;
	public static final int CRAFTING_COLUMNS = 3;
	public static final int CRAFTING_ROW = 2;

	public static final int RESULT_X = 130;
	public static final int RESULT_Y = 30;
	public static final int CONTAINER_X = 88;
	public static final int CONTAINER_Y = 45;

	public TeachCookingMenu(int windowId, Inventory inventory, BlockPos buildingId, int moduleId)
	{
		super(ModuleMenuTypes.TEACH_COOKING.get(), windowId, inventory, buildingId, moduleId);
		this.setup();
	}

	public TeachCookingMenu(int windowId, Inventory inventory, FriendlyByteBuf buffer)
	{
		super(ModuleMenuTypes.TEACH_COOKING.get(), windowId, inventory, buffer);
		this.setup();
	}

	private void setup()
	{
		this.addInventorySlots(INVENTORY_X, INVENTORY_Y);

		this.craftMatrix = new CraftingContainer(this, CRAFTING_COLUMNS, CRAFTING_ROW);

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

		this.resultContainer = new ReadOnlySlotsContainer(this.resultSlots::size, i ->
		{
			if (this.recipe == null)
			{
				return ItemStack.EMPTY;
			}
			else if (i == 0)
			{
				return this.recipe.getResultItem();
			}
			else if (i == 1)
			{
				return recipe.getOutputContainer();
			}

			return ItemStack.EMPTY;
		});
		this.resultSlots.add(this.addSlot(new Slot(this.resultContainer, 0, RESULT_X, RESULT_Y)));
		this.resultSlots.add(this.addSlot(new Slot(this.resultContainer, 1, CONTAINER_X, CONTAINER_Y)));
	}

	@Override
	public void onRecipeTransfer(CookingPotRecipe recipe, CompoundTag tag)
	{
		super.onRecipeTransfer(recipe, tag);

		var input = NBTUtils2.deserializeList(tag, "input", ItemStack::of);

		for (var i = 0; i < CRAFTING_SLOTS; i++)
		{
			this.craftSlots.get(i).set(i < input.size() ? input.get(i) : ItemStack.EMPTY);
		}

	}

	@Override
	protected void onSetRecipe(CookingPotRecipe recipe)
	{
		this.recipe = recipe;
		this.resultSlots.get(0).set(recipe != null ? recipe.getResultItem() : ItemStack.EMPTY);
	}

	@Override
	public RecipeType<CookingPotRecipe> getRecipeType()
	{
		return ModRecipeTypes.COOKING.get();
	}

	@Override
	public CraftingType getCraftingType()
	{
		return ModuleCraftingTypes.COOKING.get();
	}

	@Override
	protected RecipeWrapper createRecipeContainer(CraftingContainer craftContainer)
	{
		return new RecipeWrapper(new InvWrapper(craftContainer));
	}

}
