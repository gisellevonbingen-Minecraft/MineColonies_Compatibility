package steve_gall.minecolonies_compatibility.core.common.inventory;

import org.jetbrains.annotations.NotNull;

import com.minecolonies.api.colony.buildings.modules.IBuildingModule;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.item.crafting.StonecutterRecipe;
import steve_gall.minecolonies_compatibility.api.common.inventory.IMenuRecipeValidator;
import steve_gall.minecolonies_compatibility.api.common.inventory.MenuRecipeValidatorRecipe;
import steve_gall.minecolonies_compatibility.core.common.init.ModMenuTypes;
import steve_gall.minecolonies_tweaks.core.common.item.ItemSerializationHelper;

public class StonecutterTeachMenu extends TeachRecipeMenu<RecipeHolder<StonecutterRecipe>, SingleRecipeInput>
{
	public static final int INVENTORY_X = 8;
	public static final int INVENTORY_Y = 84;

	public static final int INGREDIENT_X = 44;
	public static final int INGREDIENT_Y = 35;
	public static final int RESULT_X = 116;
	public static final int RESULT_Y = 35;

	public StonecutterTeachMenu(int windowId, Inventory inventory, IBuildingModule module)
	{
		super(ModMenuTypes.STONECUTTING_TEACH.get(), windowId, inventory, module);

		this.setup();
	}

	public StonecutterTeachMenu(int windowId, Inventory inventory, FriendlyByteBuf buffer)
	{
		super(ModMenuTypes.STONECUTTING_TEACH.get(), windowId, inventory, buffer);

		this.setup();
	}

	private void setup()
	{
		this.addInventorySlots(INVENTORY_X, INVENTORY_Y);

		this.inputContainer = new TeachContainer(this, 1);
		this.inputSlots.add(this.addSlot(new TeachInputSlot(this.inputContainer, 0, INGREDIENT_X, INGREDIENT_Y)));

		this.resultContainer = new TeachContainer(this, 1);
		this.resultSlots.add(this.addSlot(new TeachResultSlot(this.resultContainer, 0, RESULT_X, RESULT_Y)));
	}

	@Override
	protected IMenuRecipeValidator<RecipeHolder<StonecutterRecipe>, SingleRecipeInput> createRecipeValidator()
	{
		return new MenuRecipeValidatorRecipe<>(this.inventory.player.level())
		{
			@Override
			public RecipeType<StonecutterRecipe> getRecipeType()
			{
				return RecipeType.STONECUTTING;
			}

			@Override
			public SingleRecipeInput getInput(Container container, RecipeHolder<StonecutterRecipe> recipe)
			{
				return new SingleRecipeInput(container.getItem(0));
			}

		};
	}

	@Override
	protected void setContainerByTransfer(@NotNull HolderLookup.Provider provider, @NotNull RecipeHolder<StonecutterRecipe> recipe, @NotNull CompoundTag payload)
	{
		super.setContainerByTransfer(provider, recipe, payload);

		var input = payload.getList("input", Tag.TAG_COMPOUND);
		this.inputContainer.setItem(0, ItemSerializationHelper.deserializeTag(provider, input.getCompound(0)));
	}

	@Override
	protected void onRecipeChanged(HolderLookup.Provider provider, SingleRecipeInput input)
	{
		if (this.recipe != null)
		{
			this.resultContainer.setItem(0, this.recipe.value().assemble(input, provider));
		}
		else
		{
			this.resultContainer.setItem(0, ItemStack.EMPTY);
		}

	}

}
