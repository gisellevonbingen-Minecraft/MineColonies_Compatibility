package steve_gall.minecolonies_compatibility.module.common.lets_do_meadow.menu;

import org.jetbrains.annotations.NotNull;

import com.minecolonies.api.colony.buildings.modules.IBuildingModule;

import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.satisfy.meadow.core.recipes.WoodcuttingRecipe;
import net.satisfy.meadow.core.registry.RecipeRegistry;
import steve_gall.minecolonies_compatibility.api.common.inventory.IMenuRecipeValidator;
import steve_gall.minecolonies_compatibility.api.common.inventory.MenuRecipeValidatorRecipe;
import steve_gall.minecolonies_compatibility.core.common.inventory.TeachContainer;
import steve_gall.minecolonies_compatibility.core.common.inventory.TeachInputSlot;
import steve_gall.minecolonies_compatibility.core.common.inventory.TeachRecipeMenu;
import steve_gall.minecolonies_compatibility.core.common.inventory.TeachResultSlot;
import steve_gall.minecolonies_compatibility.module.common.lets_do_meadow.init.ModuleMenuTypes;

public class WoodcuttingTeachMenu extends TeachRecipeMenu<WoodcuttingRecipe>
{
	public static final int INVENTORY_X = 8;
	public static final int INVENTORY_Y = 84;

	public static final int CRAFTING_X = 44;
	public static final int CRAFTING_Y = 26;

	public static final int RESULT_X = 116;
	public static final int RESULT_Y = 26;

	public WoodcuttingTeachMenu(int windowId, Inventory inventory, IBuildingModule module)
	{
		super(ModuleMenuTypes.WOODCUTTING_TEACH.get(), windowId, inventory, module);
		this.setup();
	}

	public WoodcuttingTeachMenu(int windowId, Inventory inventory, FriendlyByteBuf buffer)
	{
		super(ModuleMenuTypes.WOODCUTTING_TEACH.get(), windowId, inventory, buffer);
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
	protected IMenuRecipeValidator<WoodcuttingRecipe> createRecipeValidator()
	{
		return new MenuRecipeValidatorRecipe<>(this.inventory.player.level())
		{
			@Override
			public RecipeType<WoodcuttingRecipe> getRecipeType()
			{
				return RecipeRegistry.WOODCUTTING.get();
			}

			@Override
			protected boolean test(WoodcuttingRecipe recipe, Container container, ServerPlayer player)
			{
				return this.matchesWithIngredientsCount(recipe, container);
			}

		};
	}

	@Override
	protected void setContainerByTransfer(@NotNull WoodcuttingRecipe recipe, @NotNull CompoundTag payload)
	{
		super.setContainerByTransfer(recipe, payload);

		this.inputContainer.setItem(0, ItemStack.of(payload.getCompound("input")));
	}

	@Override
	protected void onRecipeChanged(RegistryAccess registryAccess)
	{
		this.resultContainer.setItem(0, this.recipe != null ? this.recipe.getResultItem(registryAccess) : ItemStack.EMPTY);
	}

}
