package steve_gall.minecolonies_compatibility.module.common.cgm.menu;

import java.util.ArrayList;
import java.util.Arrays;

import org.jetbrains.annotations.NotNull;

import com.minecolonies.api.colony.buildings.modules.IBuildingModule;
import com.mrcrayfish.guns.crafting.WorkbenchRecipe;
import com.mrcrayfish.guns.init.ModRecipeTypes;
import com.mrcrayfish.guns.item.IColored;

import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import steve_gall.minecolonies_compatibility.api.common.inventory.IMenuRecipeValidator;
import steve_gall.minecolonies_compatibility.api.common.inventory.MenuRecipeValidatorRecipe;
import steve_gall.minecolonies_compatibility.core.common.inventory.ContainerHelper;
import steve_gall.minecolonies_compatibility.core.common.inventory.TeachContainer;
import steve_gall.minecolonies_compatibility.core.common.inventory.TeachInputSlot;
import steve_gall.minecolonies_compatibility.core.common.inventory.TeachRecipeMenu;
import steve_gall.minecolonies_compatibility.core.common.inventory.TeachResultSlot;
import steve_gall.minecolonies_compatibility.core.common.item.ItemStackHelper;
import steve_gall.minecolonies_compatibility.core.common.util.NBTUtils2;
import steve_gall.minecolonies_compatibility.module.common.cgm.init.ModuleMenuTypes;

public class WorkbenchTeachMenu extends TeachRecipeMenu<WorkbenchRecipe>
{
	public static final int INVENTORY_X = 8;
	public static final int INVENTORY_Y = 84;

	public static final int DYE_X = 94;
	public static final int DYE_Y = 58;
	public static final int CRAFTING_SLOTS = 9;
	public static final int CRAFTING_X = 31;
	public static final int CRAFTING_Y = 18;
	public static final int CRAFTING_COLUMNS = 3;
	public static final int CRAFTING_ROW = 3;

	public static final int RESULT_X = 125;
	public static final int RESULT_Y = 36;

	public WorkbenchTeachMenu(int windowId, Inventory inventory, IBuildingModule module)
	{
		super(ModuleMenuTypes.WORKBENCH.get(), windowId, inventory, module);
		this.setup();
	}

	public WorkbenchTeachMenu(int windowId, Inventory inventory, FriendlyByteBuf buffer)
	{
		super(ModuleMenuTypes.WORKBENCH.get(), windowId, inventory, buffer);
		this.setup();
	}

	private void setup()
	{
		this.addInventorySlots(INVENTORY_X, INVENTORY_Y);

		this.inputContainer = new TeachContainer(this, CRAFTING_SLOTS + 1);

		for (var i = 0; i < CRAFTING_SLOTS; i++)
		{
			var x = i % CRAFTING_COLUMNS;
			var y = i / CRAFTING_COLUMNS;
			this.inputSlots.add(this.addSlot(new TeachInputSlot(this.inputContainer, i, CRAFTING_X + x * SLOT_OFFSET, CRAFTING_Y + y * SLOT_OFFSET)));
		}

		this.inputSlots.add(this.addSlot(new TeachInputSlot(this.inputContainer, CRAFTING_SLOTS, DYE_X, DYE_Y)
		{
			@Override
			public boolean canAccept(ItemStack item)
			{
				return canDye(item);
			}
		}));

		this.resultContainer = new TeachContainer(this, 1);
		this.resultSlots.add(this.addSlot(new TeachResultSlot(this.resultContainer, 0, RESULT_X, RESULT_Y)));
	}

	private boolean canDye(ItemStack item)
	{
		if (this.recipe == null)
		{
			return false;
		}
		else if (!IColored.isDyeable(this.recipe.getResultItem(this.inventory.player.level().registryAccess())))
		{
			return false;
		}

		return item.getItem() instanceof DyeItem;
	}

	@Override
	protected IMenuRecipeValidator<WorkbenchRecipe> createRecipeValidator()
	{
		return new MenuRecipeValidatorRecipe<>(this.inventory.player.level())
		{
			@Override
			public RecipeType<WorkbenchRecipe> getRecipeType()
			{
				return ModRecipeTypes.WORKBENCH.get();
			}

			@Override
			protected boolean test(WorkbenchRecipe recipe, Container container, ServerPlayer player)
			{
				var materials = recipe.getMaterials();
				var stacks = ItemStackHelper.filterNotEmpty(ContainerHelper.getItemStacks(container, 0, CRAFTING_SLOTS));

				if (materials.size() != stacks.size())
				{
					return false;
				}

				var remainedStacks = new ArrayList<>(stacks);

				for (var material : materials)
				{
					var found = -1;

					for (var i = 0; i < remainedStacks.size(); i++)
					{
						var stack = remainedStacks.get(i);

						if (material.test(stack))
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
	public void handleSlotClick(Slot slot, ItemStack stack)
	{
		if (slot.container == this.inputContainer)
		{
			if (slot.getSlotIndex() == CRAFTING_SLOTS)
			{
				if (stack.isEmpty())
				{

				}
				else if (!this.canDye(stack))
				{
					return;
				}

			}

		}

		super.handleSlotClick(slot, stack);
	}

	@Override
	protected void setContainerByTransfer(@NotNull WorkbenchRecipe recipe, @NotNull CompoundTag payload)
	{
		var dye = this.inputContainer.getItem(CRAFTING_SLOTS);

		super.setContainerByTransfer(recipe, payload);

		var input = NBTUtils2.deserializeList(payload, "input", ItemStack::of);

		for (var stack : input)
		{
			stack.setCount(1);
		}

		for (var i = 0; i < CRAFTING_SLOTS; i++)
		{
			this.inputContainer.setItem(i, i < input.size() ? input.get(i) : ItemStack.EMPTY);
		}

		if (IColored.isDyeable(recipe.getResultItem(this.inventory.player.level().registryAccess())))
		{
			this.inputContainer.setItem(CRAFTING_SLOTS, dye);
		}

	}

	@Override
	protected void onRecipeChanged(RegistryAccess registryAccess)
	{
		if (this.recipe != null)
		{
			var output = this.recipe.getResultItem(registryAccess);

			if (IColored.isDyeable(output))
			{
				if (this.inputContainer.getItem(CRAFTING_SLOTS).getItem() instanceof DyeItem dyeItem)
				{
					output = IColored.dye(output, Arrays.asList(dyeItem));
				}

			}
			else
			{
				this.inputContainer.setItem(CRAFTING_SLOTS, ItemStack.EMPTY);
			}

			this.resultContainer.setItem(0, output);

			var stacks = ItemStackHelper.filterNotEmpty(ContainerHelper.getItemStacks(this.inputContainer));
			var materials = this.recipe.getMaterials();

			for (var i = 0; i < CRAFTING_SLOTS; i++)
			{
				this.inputContainer.setItem(i, ItemStack.EMPTY);
			}

			for (var i = 0; i < materials.size(); i++)
			{
				var material = materials.get(i);
				var found = ItemStack.EMPTY;

				for (var stack : stacks)
				{
					if (material.test(stack))
					{
						found = stack.copy();
						break;
					}

				}

				if (found.isEmpty())
				{
					var examples = material.getItems();
					found = examples.length == 0 ? ItemStack.EMPTY : examples[0].copy();
				}

				found.setCount(material.getCount());
				this.inputContainer.setItem(i, found);
			}

		}
		else
		{
			this.resultContainer.setItem(0, ItemStack.EMPTY);
		}

	}

}
