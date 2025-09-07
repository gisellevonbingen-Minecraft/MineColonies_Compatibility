package steve_gall.minecolonies_compatibility.module.common.farmersdelight.menu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.minecolonies.api.IMinecoloniesAPI;
import com.minecolonies.api.colony.buildings.modules.IBuildingModule;
import com.minecolonies.api.equipment.registry.EquipmentTypeEntry;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import steve_gall.minecolonies_compatibility.api.common.inventory.IMenuRecipeValidator;
import steve_gall.minecolonies_compatibility.api.common.inventory.MenuRecipeValidatorRecipe;
import steve_gall.minecolonies_compatibility.core.common.crafting.IngredientHelper;
import steve_gall.minecolonies_compatibility.core.common.inventory.ReadOnlySlotsContainer;
import steve_gall.minecolonies_compatibility.core.common.inventory.TeachContainer;
import steve_gall.minecolonies_compatibility.core.common.inventory.TeachInputSlot;
import steve_gall.minecolonies_compatibility.core.common.inventory.TeachRecipeMenu;
import steve_gall.minecolonies_compatibility.core.common.inventory.TeachResultSlot;
import steve_gall.minecolonies_compatibility.module.common.farmersdelight.init.ModuleMenuTypes;
import steve_gall.minecolonies_tweaks.core.common.item.ItemSerializationHelper;
import vectorwing.farmersdelight.common.crafting.CuttingBoardRecipe;
import vectorwing.farmersdelight.common.crafting.CuttingBoardRecipeInput;
import vectorwing.farmersdelight.common.crafting.ingredient.ChanceResult;
import vectorwing.farmersdelight.common.registry.ModRecipeTypes;

public class CuttingTeachMenu extends TeachRecipeMenu<RecipeHolder<CuttingBoardRecipe>, CuttingBoardRecipeInput>
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

	private final EquipmentTypeEntry toolType;

	private List<ChanceResult> results;

	public CuttingTeachMenu(int windowId, Inventory inventory, IBuildingModule module, EquipmentTypeEntry toolType)
	{
		super(ModuleMenuTypes.CUTTING_TEACH.get(), windowId, inventory, module);
		this.toolType = toolType;
		this.setup();
	}

	public CuttingTeachMenu(int windowId, Inventory inventory, RegistryFriendlyByteBuf buffer)
	{
		super(ModuleMenuTypes.CUTTING_TEACH.get(), windowId, inventory, buffer);
		this.toolType = IMinecoloniesAPI.getInstance().getEquipmentTypeRegistry().get(buffer.readResourceLocation());
		this.setup();
	}

	private void setup()
	{
		this.addInventorySlots(INVENTORY_X, INVENTORY_Y);

		this.inputContainer = new TeachContainer(this, 1);

		for (var i = 0; i < CRAFTING_SLOTS; i++)
		{
			var col = i % CRAFTING_COLS;
			var row = i / CRAFTING_COLS;
			this.inputSlots.add(this.addSlot(new TeachInputSlot(this.inputContainer, i, CRAFTING_X + col * SLOT_OFFSET, CRAFTING_Y + row * SLOT_OFFSET)));
		}

		this.results = new ArrayList<>();
		this.resultContainer = new ReadOnlySlotsContainer(this.results::size, i -> this.results.get(i).stack());
	}

	@Override
	protected IMenuRecipeValidator<RecipeHolder<CuttingBoardRecipe>, CuttingBoardRecipeInput> createRecipeValidator()
	{
		return new MenuRecipeValidatorRecipe<>(this.inventory.player.level())
		{
			@Override
			public RecipeType<CuttingBoardRecipe> getRecipeType()
			{
				return ModRecipeTypes.CUTTING.get();
			}

			@Override
			public @NotNull CuttingBoardRecipeInput getInput(Container container, RecipeHolder<CuttingBoardRecipe> recipe)
			{
				return new CuttingBoardRecipeInput(container.getItem(0), this.findTool(recipe));
			}

			private ItemStack findTool(RecipeHolder<CuttingBoardRecipe> recipe)
			{
				if (recipe == null)
				{
					return ItemStack.EMPTY;
				}

				var tools = recipe.value().getTool().getItems();
				return tools.length == 0 ? ItemStack.EMPTY : tools[0];
			}

		};
	}

	@Override
	protected void setContainerByTransfer(@NotNull HolderLookup.Provider provider, @NotNull RecipeHolder<CuttingBoardRecipe> recipe, @NotNull CompoundTag payload)
	{
		super.setContainerByTransfer(provider, recipe, payload);

		this.inputContainer.setItem(0, ItemSerializationHelper.deserializeTag(provider, payload.getCompound("input")));
	}

	@Override
	protected void onRecipeChanged(HolderLookup.Provider provider, CuttingBoardRecipeInput input)
	{
		var prevSlots = this.resultSlots.size();
		this.results.clear();

		if (this.recipe != null)
		{
			this.results.addAll(this.recipe.value().getRollableResults());
		}

		var addingSlots = this.results.size() - prevSlots;

		for (var i = 0; i < addingSlots; i++)
		{
			var index = prevSlots + i;
			var xi = index % RESULT_COLUMNS;
			var yi = index / RESULT_COLUMNS;
			var x = RESULT_X + xi * SLOT_OFFSET;
			var y = RESULT_Y + yi * SLOT_OFFSET;
			this.resultSlots.add(this.addSlot(new TeachResultSlot(this.resultContainer, index, x, y)
			{
				@Override
				public boolean isActive()
				{
					return !this.getItem().isEmpty();
				}
			}));
		}

	}

	@Override
	public @Nullable Component getRecipeError(@NotNull RecipeHolder<CuttingBoardRecipe> recipe)
	{
		if (!IngredientHelper.isTool(recipe.value().getTool(), this.getToolType()))
		{
			return Component.translatable("minecolonies_compatibility.text.unsupported_tool");
		}

		var anyPrimary = recipe.value().getRollableResults().stream().anyMatch(r -> r.chance() >= 1.0D);

		if (!anyPrimary)
		{
			return Component.translatable("minecolonies_compatibility.text.no_primary_result_item");
		}

		return super.getRecipeError(recipe);
	}

	public EquipmentTypeEntry getToolType()
	{
		return this.toolType;
	}

	public List<ChanceResult> getResults()
	{
		return Collections.unmodifiableList(this.results);
	}

}
