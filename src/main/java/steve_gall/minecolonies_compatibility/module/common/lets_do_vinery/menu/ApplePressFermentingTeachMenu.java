package steve_gall.minecolonies_compatibility.module.common.lets_do_vinery.menu;

import com.minecolonies.api.colony.buildings.modules.IBuildingModule;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.satisfy.vinery.core.recipe.ApplePressFermentingRecipe;
import net.satisfy.vinery.core.recipe.input.ApplePressFermentingRecipeInput;
import net.satisfy.vinery.core.registry.RecipeTypesRegistry;
import steve_gall.minecolonies_compatibility.api.common.inventory.IMenuRecipeValidator;
import steve_gall.minecolonies_compatibility.api.common.inventory.MenuRecipeValidatorRecipe;
import steve_gall.minecolonies_compatibility.core.common.inventory.TeachContainer;
import steve_gall.minecolonies_compatibility.core.common.inventory.TeachInputSlot;
import steve_gall.minecolonies_compatibility.core.common.inventory.TeachRecipeMenu;
import steve_gall.minecolonies_compatibility.core.common.inventory.TeachResultSlot;
import steve_gall.minecolonies_compatibility.module.common.lets_do_vinery.init.ModuleMenuTypes;
import steve_gall.minecolonies_tweaks.core.common.item.ItemSerializationHelper;

public class ApplePressFermentingTeachMenu extends TeachRecipeMenu<RecipeHolder<ApplePressFermentingRecipe>, ApplePressFermentingRecipeInput>
{
	public static final int INVENTORY_X = 8;
	public static final int INVENTORY_Y = 84;

	public static final int CRAFTING_X = 44;
	public static final int CRAFTING_Y = 34;

	public static final int RESULT_X = 116;
	public static final int RESULT_Y = 34;

	public ApplePressFermentingTeachMenu(int windowId, Inventory inventory, IBuildingModule module)
	{
		super(ModuleMenuTypes.APPLE_PRESS_FERMENTING.get(), windowId, inventory, module);
		this.setup();
	}

	public ApplePressFermentingTeachMenu(int windowId, Inventory inventory, FriendlyByteBuf buffer)
	{
		super(ModuleMenuTypes.APPLE_PRESS_FERMENTING.get(), windowId, inventory, buffer);
		this.setup();
	}

	private void setup()
	{
		this.addInventorySlots(INVENTORY_X, INVENTORY_Y);

		this.inputContainer = new TeachContainer(this, 1);
		this.inputSlots.add(this.addSlot(new TeachInputSlot(this.inputContainer, 0, CRAFTING_X, CRAFTING_Y)));

		this.resultContainer = new TeachContainer(this, 1);
		this.resultSlots.add(this.addSlot(new TeachResultSlot(this.resultContainer, 0, RESULT_X, RESULT_Y)));
	}

	@Override
	protected IMenuRecipeValidator<RecipeHolder<ApplePressFermentingRecipe>, ApplePressFermentingRecipeInput> createRecipeValidator()
	{
		return new MenuRecipeValidatorRecipe<>(this.inventory.player.level())
		{
			@Override
			public RecipeType<ApplePressFermentingRecipe> getRecipeType()
			{
				return RecipeTypesRegistry.APPLE_PRESS_FERMENTING_RECIPE_TYPE.get();
			}

			@Override
			public ApplePressFermentingRecipeInput getInput(Container container, RecipeHolder<ApplePressFermentingRecipe> recipe)
			{
				return new ApplePressFermentingRecipeInput(container.getItem(0));
			}

		};
	}

	@Override
	protected void setContainerByTransfer(HolderLookup.Provider provider, RecipeHolder<ApplePressFermentingRecipe> recipe, CompoundTag payload)
	{
		super.setContainerByTransfer(provider, recipe, payload);

		this.inputContainer.setItem(0, ItemSerializationHelper.deserializeTag(provider, payload.getCompound("input")));
	}

	@Override
	protected void onRecipeChanged(HolderLookup.Provider provider, ApplePressFermentingRecipeInput input)
	{
		this.resultContainer.setItem(0, this.recipe != null ? this.recipe.value().getResultItem(provider) : ItemStack.EMPTY);
	}

}
