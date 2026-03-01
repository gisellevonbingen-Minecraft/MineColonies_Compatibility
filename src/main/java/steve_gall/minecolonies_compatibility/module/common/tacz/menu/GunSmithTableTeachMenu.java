package steve_gall.minecolonies_compatibility.module.common.tacz.menu;

import java.util.ArrayList;

import com.minecolonies.api.colony.buildings.modules.IBuildingModule;
import com.minecolonies.api.colony.requestsystem.StandardFactoryController;
import com.minecolonies.api.crafting.ItemStorage;
import com.tacz.guns.api.item.IAmmo;
import com.tacz.guns.api.item.IAttachment;
import com.tacz.guns.api.item.IGun;
import com.tacz.guns.crafting.GunSmithTableRecipe;
import com.tacz.guns.init.ModRecipe;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmithingRecipeInput;
import steve_gall.minecolonies_compatibility.api.common.inventory.IItemGhostSlot;
import steve_gall.minecolonies_compatibility.api.common.inventory.IMenuRecipeValidator;
import steve_gall.minecolonies_compatibility.api.common.inventory.MenuRecipeValidatorRecipe;
import steve_gall.minecolonies_compatibility.core.common.inventory.ContainerHelper;
import steve_gall.minecolonies_compatibility.core.common.inventory.TeachContainer;
import steve_gall.minecolonies_compatibility.core.common.inventory.TeachInputSlot;
import steve_gall.minecolonies_compatibility.core.common.inventory.TeachRecipeMenu;
import steve_gall.minecolonies_compatibility.core.common.inventory.TeachResultSlot;
import steve_gall.minecolonies_compatibility.core.common.item.ItemStackHelper;
import steve_gall.minecolonies_compatibility.module.common.tacz.init.ModuleMenuTypes;

public class GunSmithTableTeachMenu extends TeachRecipeMenu<RecipeHolder<GunSmithTableRecipe>, SmithingRecipeInput>
{
	public static final int INVENTORY_X = 8;
	public static final int INVENTORY_Y = 84;

	public static final int CRAFTING_SLOTS = 9;
	public static final int CRAFTING_X = 98;
	public static final int CRAFTING_Y = 17;
	public static final int CRAFTING_COLUMNS = 3;
	public static final int CRAFTING_ROW = 3;

	public static final int RESULT_X = 44;
	public static final int RESULT_Y = 35;
	
	private SmithingRecipeInput dummyInput;

	public GunSmithTableTeachMenu(int windowId, Inventory inventory, IBuildingModule module)
	{
		super(ModuleMenuTypes.GUN_SMITH_TABLE_TEACH.get(), windowId, inventory, module);
		this.setup();
	}

	public GunSmithTableTeachMenu(int windowId, Inventory inventory, FriendlyByteBuf buffer)
	{
		super(ModuleMenuTypes.GUN_SMITH_TABLE_TEACH.get(), windowId, inventory, buffer);
		this.setup();
	}

	private void setup()
	{
		this.dummyInput = new SmithingRecipeInput(ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY);
		
		this.addInventorySlots(INVENTORY_X, INVENTORY_Y);

		this.inputContainer = new TeachContainer(this, CRAFTING_SLOTS);

		for (var i = 0; i < CRAFTING_SLOTS; i++)
		{
			var x = i % CRAFTING_COLUMNS;
			var y = i / CRAFTING_COLUMNS;
			this.inputSlots.add(this.addSlot(new TeachInputSlot(this.inputContainer, i, CRAFTING_X + x * SLOT_OFFSET, CRAFTING_Y + y * SLOT_OFFSET)
			{
				@Override
				public boolean canAccept(ItemStack item)
				{
					return false;
				}
			}));
		}

		this.resultContainer = new TeachContainer(this, 1);
		this.resultSlots.add(this.addSlot(new ResultSlot(this.resultContainer, 0, RESULT_X, RESULT_Y)));
	}

	@Override
	protected IMenuRecipeValidator<RecipeHolder<GunSmithTableRecipe>, SmithingRecipeInput> createRecipeValidator()
	{
		return new MenuRecipeValidatorRecipe<>(this.inventory.player.level())
		{
			@Override
			public RecipeType<GunSmithTableRecipe> getRecipeType()
			{
				return ModRecipe.GUN_SMITH_TABLE_CRAFTING.get();
			}

			@Override
			public SmithingRecipeInput getInput(Container container, RecipeHolder<GunSmithTableRecipe> recipe)
			{
				return dummyInput;
			}

			@Override
			protected boolean test(RecipeHolder<GunSmithTableRecipe> holder, Container container, ServerPlayer player)
			{
				var recipe = holder.value();
				var inputs = recipe.getInputs();
				var stacks = ItemStackHelper.filterNotEmpty(ContainerHelper.getItemStacks(container));

				if (inputs.size() != stacks.size())
				{
					return false;
				}

				var remainedStacks = new ArrayList<>(stacks);

				for (var input : inputs)
				{
					var found = -1;

					for (var i = 0; i < remainedStacks.size(); i++)
					{
						var stack = remainedStacks.get(i);

						if (input.getIngredient().test(stack))
						{
							found = i;
							remainedStacks.remove(i);
							break;
						}

					}

					if (found == -1)
					{
						return false;
					}

				}

				return true;
			}

		};
	}

	@Override
	protected void setContainerByTransfer(HolderLookup.Provider provider, RecipeHolder<GunSmithTableRecipe> recipe, CompoundTag payload)
	{
		super.setContainerByTransfer(provider, recipe, payload);

		var input = new ArrayList<ItemStorage>(StandardFactoryController.getInstance().deserializeList(provider, payload.getList("input", Tag.TAG_COMPOUND)));

		for (var i = 0; i < CRAFTING_SLOTS; i++)
		{
			this.inputContainer.setItem(i, i < input.size() ? input.get(i).getItemStack() : ItemStack.EMPTY);
		}

	}

	@Override
	protected void onSlotsChanged(Container container)
	{
		if (container == this.inputContainer)
		{
			return;
		}

		super.onSlotsChanged(container);
	}

	@Override
	public void handleSlotClick(Slot slot, ItemStack stack)
	{
		if (slot.container == this.inputContainer)
		{
			if (this.recipe != null)
			{
				var inputs = this.recipe.value().getInputs();
				var index = this.inputSlots.indexOf(slot);

				if (0 <= index && index < inputs.size())
				{
					var input = inputs.get(index);

					if (input.getIngredient().test(stack))
					{
						stack = stack.copy();
						stack.setCount(input.getCount());
						slot.set(stack);
					}

				}

			}

			return;
		}
		else if (slot.container == this.resultContainer)
		{
			var player = this.inventory.player;
			var recipes = player.level().getRecipeManager().getAllRecipesFor(ModRecipe.GUN_SMITH_TABLE_CRAFTING.get());

			for (var recipe : recipes)
			{
				if (this.test(stack, recipe.value()))
				{
					this.recipe = recipe;
					this.onRecipeChanged(this.inventory.player.level().registryAccess(), this.dummyInput);
					this.refreshRecipes(recipe);
					break;
				}

			}

			return;
		}

		super.handleSlotClick(slot, stack);
	}

	private boolean test(ItemStack stack, GunSmithTableRecipe recipe)
	{
		var result = recipe.getResult().getResult();
		var item = result.getItem();

		if (item == stack.getItem())
		{
			if (item instanceof IGun gun)
			{
				return gun.getGunId(stack).equals(gun.getGunId(result));
			}
			else if (item instanceof IAttachment attachment)
			{
				return attachment.getAttachmentId(stack).equals(attachment.getAttachmentId(result));
			}
			else if (item instanceof IAmmo ammo)
			{
				return ammo.getAmmoId(stack).equals(ammo.getAmmoId(result));
			}

		}

		return false;
	}

	@Override
	protected void onRecipeChanged(HolderLookup.Provider provider, SmithingRecipeInput recipeInput)
	{
		if (this.recipe != null)
		{
			this.resultContainer.setItem(0, this.recipe.value().getResultItem(provider));

			var inputs = this.recipe.value().getInputs();

			for (var i = 0; i < this.inputContainer.getContainerSize(); i++)
			{
				if (i < inputs.size())
				{
					var stack = this.inputContainer.getItem(i);
					var input = inputs.get(i);

					if (!input.getIngredient().test(stack))
					{
						var examples = input.getIngredient().getItems();
						stack = examples.length == 0 ? ItemStack.EMPTY : examples[0].copy();
						this.inputContainer.setItem(i, stack);
					}

					stack.setCount(input.getCount());

				}
				else
				{
					this.inputContainer.setItem(i, ItemStack.EMPTY);
				}

			}

		}
		else
		{
			this.resultContainer.setItem(0, ItemStack.EMPTY);
			this.inputContainer.clearContent();
		}

	}

	private class ResultSlot extends TeachResultSlot implements IItemGhostSlot
	{
		public ResultSlot(Container container, int slot, int x, int y)
		{
			super(container, slot, x, y);
		}

	}

}
