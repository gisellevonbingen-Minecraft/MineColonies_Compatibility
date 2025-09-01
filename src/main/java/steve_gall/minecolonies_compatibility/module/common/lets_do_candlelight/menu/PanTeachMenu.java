package steve_gall.minecolonies_compatibility.module.common.lets_do_candlelight.menu;

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
import net.satisfy.candlelight.recipe.CookingPanRecipe;
import net.satisfy.candlelight.registry.RecipeTypeRegistry;
import steve_gall.minecolonies_compatibility.api.common.inventory.IMenuRecipeValidator;
import steve_gall.minecolonies_compatibility.api.common.inventory.MenuRecipeValidatorRecipe;
import steve_gall.minecolonies_compatibility.core.common.inventory.TeachContainer;
import steve_gall.minecolonies_compatibility.core.common.inventory.TeachInputSlot;
import steve_gall.minecolonies_compatibility.core.common.inventory.TeachRecipeMenu;
import steve_gall.minecolonies_compatibility.core.common.inventory.TeachResultSlot;
import steve_gall.minecolonies_compatibility.core.common.util.NBTUtils2;
import steve_gall.minecolonies_compatibility.module.common.lets_do_candlelight.init.ModuleMenuTypes;

public class PanTeachMenu extends TeachRecipeMenu<CookingPanRecipe>
{
	public static final int INVENTORY_X = 8;
	public static final int INVENTORY_Y = 84;

	public static final int CRAFTING_SLOTS = 6;
	public static final int CRAFTING_COLS = 3;
	public static final int CRAFTING_X = 26;
	public static final int CRAFTING_Y = 17;

	public static final int RESULT_X = 120;
	public static final int RESULT_Y = 28;
	public static final int CONTAINER_X = 91;
	public static final int CONTAINER_Y = 55;

	public PanTeachMenu(int windowId, Inventory inventory, IBuildingModule module)
	{
		super(ModuleMenuTypes.PAN_TEACH.get(), windowId, inventory, module);
		this.setup();
	}

	public PanTeachMenu(int windowId, Inventory inventory, FriendlyByteBuf buffer)
	{
		super(ModuleMenuTypes.PAN_TEACH.get(), windowId, inventory, buffer);
		this.setup();
	}

	private void setup()
	{
		this.addInventorySlots(INVENTORY_X, INVENTORY_Y);

		this.inputContainer = new TeachContainer(this, CRAFTING_SLOTS);

		for (var i = 0; i < CRAFTING_SLOTS; i++)
		{
			var col = i % CRAFTING_COLS;
			var row = i / CRAFTING_COLS;
			this.inputSlots.add(this.addSlot(new TeachInputSlot(this.inputContainer, i, CRAFTING_X + col * SLOT_OFFSET, CRAFTING_Y + row * SLOT_OFFSET)));
		}

		this.resultContainer = new TeachContainer(this, 2);
		this.resultSlots.add(this.addSlot(new TeachResultSlot(this.resultContainer, 0, RESULT_X, RESULT_Y)));
		this.resultSlots.add(this.addSlot(new TeachResultSlot(this.resultContainer, 1, CONTAINER_X, CONTAINER_Y)));
	}

	@Override
	protected IMenuRecipeValidator<CookingPanRecipe> createRecipeValidator()
	{
		return new MenuRecipeValidatorRecipe<>(this.inventory.player.level())
		{
			@Override
			public RecipeType<CookingPanRecipe> getRecipeType()
			{
				return RecipeTypeRegistry.COOKING_PAN_RECIPE_TYPE.get();
			}

			@Override
			protected boolean test(CookingPanRecipe recipe, Container container, ServerPlayer player)
			{
				return this.matchesWithIngredientsCount(recipe, container);
			}

		};
	}

	@Override
	protected void setContainerByTransfer(@NotNull CookingPanRecipe recipe, @NotNull CompoundTag payload)
	{
		super.setContainerByTransfer(recipe, payload);

		var input = NBTUtils2.deserializeList(payload, "input", ItemStack::of);

		for (var i = 0; i < CRAFTING_SLOTS; i++)
		{
			this.inputContainer.setItem(i, i < input.size() ? input.get(i) : ItemStack.EMPTY);
		}

	}

	@Override
	protected void onRecipeChanged(RegistryAccess registryAccess)
	{
		this.resultContainer.setItem(0, this.recipe != null ? this.recipe.getResultItem(registryAccess) : ItemStack.EMPTY);
		this.resultContainer.setItem(1, this.recipe != null ? this.recipe.getContainer() : ItemStack.EMPTY);
	}

}
