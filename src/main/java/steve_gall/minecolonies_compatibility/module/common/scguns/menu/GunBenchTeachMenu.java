package steve_gall.minecolonies_compatibility.module.common.scguns.menu;

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
import steve_gall.minecolonies_compatibility.api.common.inventory.IMenuRecipeValidator;
import steve_gall.minecolonies_compatibility.api.common.inventory.MenuRecipeValidatorRecipe;
import steve_gall.minecolonies_compatibility.core.common.inventory.TeachContainer;
import steve_gall.minecolonies_compatibility.core.common.inventory.TeachInputSlot;
import steve_gall.minecolonies_compatibility.core.common.inventory.TeachRecipeMenu;
import steve_gall.minecolonies_compatibility.core.common.inventory.TeachResultSlot;
import steve_gall.minecolonies_compatibility.core.common.util.NBTUtils2;
import steve_gall.minecolonies_compatibility.module.common.scguns.init.ModuleMenuTypes;
import top.ribs.scguns.client.screen.GunBenchRecipe;

public class GunBenchTeachMenu extends TeachRecipeMenu<GunBenchRecipe>
{
	public static final int INVENTORY_X = 8;
	public static final int INVENTORY_Y = 84;

	public static final int INGREDIENT_SLOTS = 10;
	public static final int BLUEPRINT_SLOT = 10;

	public static final int RESULT_X = 140;
	public static final int RESULT_Y = 44;

	public GunBenchTeachMenu(int windowId, Inventory inventory, FriendlyByteBuf buffer)
	{
		super(ModuleMenuTypes.GUN_BENCH.get(), windowId, inventory, buffer);
		this.setup();
	}

	public GunBenchTeachMenu(int windowId, Inventory inventory, IBuildingModule module)
	{
		super(ModuleMenuTypes.GUN_BENCH.get(), windowId, inventory, module);
		this.setup();
	}

	private void setup()
	{
		this.addInventorySlots(INVENTORY_X, INVENTORY_Y);

		this.inputContainer = new TeachContainer(this, INGREDIENT_SLOTS + 1);
		var ingredientX = new int[]{26, 44, 62, 80, 26, 44, 62, 80, 26, 62};
		var ingredientY = new int[]{17, 17, 17, 17, 35, 35, 35, 35, 53, 53};

		for (var i = 0; i < INGREDIENT_SLOTS; i++)
		{
			this.inputSlots.add(this.addSlot(new TeachInputSlot(this.inputContainer, i, ingredientX[i], ingredientY[i])));
		}

		this.inputSlots.add(this.addSlot(new TeachInputSlot(this.inputContainer, BLUEPRINT_SLOT, 116, 17)));

		this.resultContainer = new TeachContainer(this, 1);
		this.resultSlots.add(this.addSlot(new TeachResultSlot(this.resultContainer, 0, RESULT_X, RESULT_Y)));
	}

	@Override
	protected IMenuRecipeValidator<GunBenchRecipe> createRecipeValidator()
	{
		return new MenuRecipeValidatorRecipe<>(this.inventory.player.level())
		{
			@Override
			public RecipeType<GunBenchRecipe> getRecipeType()
			{
				return GunBenchRecipe.Type.INSTANCE;
			}

			@Override
			protected boolean test(GunBenchRecipe recipe, Container container, ServerPlayer player)
			{
				if (!recipe.getBlueprint().test(container.getItem(BLUEPRINT_SLOT)))
				{
					return false;
				}

				var ingredients = recipe.getIngredients();

				for (var i = 0; i < ingredients.size(); i++)
				{
					if (!ingredients.get(i).test(container.getItem(i)))
					{
						return false;
					}

				}

				return true;
			}

		};
	}

	@Override
	protected void setContainerByTransfer(@NotNull GunBenchRecipe recipe, @NotNull CompoundTag payload)
	{
		super.setContainerByTransfer(recipe, payload);

		var ingredients = NBTUtils2.deserializeList(payload, "ingredients", ItemStack::of);

		for (var i = 0; i < INGREDIENT_SLOTS; i++)
		{
			this.inputContainer.setItem(i, i < ingredients.size() ? ingredients.get(i) : ItemStack.EMPTY);
		}

		this.inputContainer.setItem(BLUEPRINT_SLOT, ItemStack.of(payload.getCompound("blueprint")));
	}

	@Override
	protected void onRecipeChanged(RegistryAccess registryAccess)
	{
		this.resultContainer.setItem(0, this.recipe != null ? this.recipe.getResultItem(registryAccess) : ItemStack.EMPTY);
	}

}
