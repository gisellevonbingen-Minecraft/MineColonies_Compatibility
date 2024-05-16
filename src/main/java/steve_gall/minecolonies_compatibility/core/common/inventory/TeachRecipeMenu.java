package steve_gall.minecolonies_compatibility.core.common.inventory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.minecolonies.api.crafting.registry.CraftingType;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.GameRules;
import steve_gall.minecolonies_compatibility.api.common.inventory.IItemGhostMenu;
import steve_gall.minecolonies_compatibility.api.common.inventory.IRecipeTransferableMenu;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.core.common.network.message.TeachRecipeMenuNewResultMessage;

public abstract class TeachRecipeMenu<RECIPE extends Recipe<CONTAINER>, CONTAINER extends Container> extends ModuleMenu implements IItemGhostMenu, IRecipeTransferableMenu<RECIPE>
{
	protected CraftingContainer craftMatrix;
	protected List<Slot> craftSlots;

	protected Container resultContainer;
	protected List<Slot> resultSlots;

	protected RECIPE recipe;

	public TeachRecipeMenu(MenuType<?> menuType, int windowId, Inventory inventory, BlockPos buildingId, int moduleId)
	{
		super(menuType, windowId, inventory, buildingId, moduleId);
		this.setup();
	}

	public TeachRecipeMenu(MenuType<?> menuType, int windowId, Inventory inventory, FriendlyByteBuf buffer)
	{
		super(menuType, windowId, inventory, buffer);
		this.setup();
	}

	private void setup()
	{
		this.craftSlots = new ArrayList<>();
		this.resultSlots = new ArrayList<>();

		this.craftMatrix = null;
		this.resultContainer = null;

		this.recipe = null;
	}

	public abstract RecipeType<RECIPE> getRecipeType();

	public abstract CraftingType getCraftingType();

	protected abstract void onSetRecipe(RECIPE recipe);

	protected abstract CONTAINER createRecipeContainer(CraftingContainer craftContainer);

	@Override
	public void onRecipeTransfer(RECIPE recipe, CompoundTag tag)
	{
		for (var slot : this.craftSlots)
		{
			slot.set(ItemStack.EMPTY);
		}

		this.setRecipe(recipe);
	}

	@Override
	public void onGhostAccept(Slot slot, ItemStack stack)
	{
		if (this.craftSlots.contains(slot))
		{
			slot.set(stack);
			this.slotsChanged(this.craftMatrix);
		}

	}

	@Override
	public void slotsChanged(Container container)
	{
		if (this.inventory.player instanceof ServerPlayer player)
		{
			if (container == this.craftMatrix)
			{
				var level = player.level();
				var recipeContainer = this.createRecipeContainer(this.craftMatrix);
				var recipe = level.getRecipeManager().getRecipeFor(this.getRecipeType(), recipeContainer, level).orElse(null);

				if (recipe != null)
				{
					var canLearn = recipe.isSpecial() || !level.getGameRules().getBoolean(GameRules.RULE_LIMITED_CRAFTING) || player.getRecipeBook().contains(recipe) || player.isCreative();
					this.setRecipe(canLearn ? recipe : null);
				}
				else
				{
					this.setRecipe(null);
				}

			}
			else if (container == this.resultContainer)
			{
				var recipeId = recipe != null ? recipe.getId() : null;
				MineColoniesCompatibility.network().sendToPlayer(new TeachRecipeMenuNewResultMessage(recipeId), player);
			}

		}

		super.slotsChanged(container);
	}

	@SuppressWarnings("unchecked")
	public void setRecipeId(ResourceLocation recipeId)
	{
		this.setRecipe((RECIPE) this.inventory.player.level().getRecipeManager().byKey(recipeId).orElse(null));
	}

	public void setRecipe(RECIPE recipe)
	{
		var prev = this.recipe;
		this.onSetRecipe(recipe);
		var next = this.recipe;

		if (prev != next)
		{
			this.slotsChanged(this.resultContainer);
		}

	}

	@Override
	public void clicked(int slotNumber, int clickedButton, ClickType mode, Player player)
	{
		if (0 <= slotNumber && slotNumber < this.slots.size())
		{
			var slot = this.slots.get(slotNumber);

			if (this.craftSlots.contains(slot))
			{
				// 1 is shift-click
				if (mode == ClickType.PICKUP || mode == ClickType.PICKUP_ALL || mode == ClickType.SWAP)
				{
					this.handleSlotClick(slot, this.getCarried());
				}

				return;
			}
			else if (this.resultSlots.contains((slot)))
			{
				return;
			}

			if (mode == ClickType.QUICK_MOVE)
			{
				return;
			}

		}

		super.clicked(slotNumber, clickedButton, mode, player);
	}

	public ItemStack handleSlotClick(final Slot slot, final ItemStack stack)
	{
		if (stack.getCount() > 0)
		{
			var copy = stack.copy();
			copy.setCount(1);
			slot.set(copy);
		}
		else if (slot.getItem().getCount() > 0)
		{
			slot.set(ItemStack.EMPTY);
		}

		return slot.getItem().copy();
	}

	@Override
	public boolean canTakeItemForPickAll(ItemStack stack, Slot slot)
	{
		return !this.resultSlots.contains(slot) && super.canTakeItemForPickAll(stack, slot);
	}

	@Override
	public ItemStack quickMoveStack(Player player, int slot)
	{
		return ItemStack.EMPTY;
	}

	@Override
	public boolean stillValid(Player player)
	{
		return true;
	}

	public CraftingContainer getCraftMatrix()
	{
		return this.craftMatrix;
	}

	public List<Slot> getCraftSlots()
	{
		return Collections.unmodifiableList(this.craftSlots);
	}

	public Container getResultContainer()
	{
		return this.resultContainer;
	}

	public List<Slot> getResultSlots()
	{
		return Collections.unmodifiableList(this.resultSlots);
	}

	public RECIPE getRecipe()
	{
		return this.recipe;
	}

}
