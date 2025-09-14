package steve_gall.minecolonies_compatibility.module.common.lets_do_vinery.menu;

import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.minecolonies.api.colony.buildings.modules.IBuildingModule;
import com.minecolonies.api.colony.requestsystem.factory.IFactoryController;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import steve_gall.minecolonies_compatibility.api.common.inventory.IMenuRecipeValidator;
import steve_gall.minecolonies_compatibility.core.common.inventory.TeachContainer;
import steve_gall.minecolonies_compatibility.core.common.inventory.TeachInputSlot;
import steve_gall.minecolonies_compatibility.core.common.inventory.TeachRecipeMenu;
import steve_gall.minecolonies_compatibility.core.common.inventory.TeachResultSlot;
import steve_gall.minecolonies_compatibility.module.common.lets_do_vinery.crafting.ApplePressCraftingType;
import steve_gall.minecolonies_compatibility.module.common.lets_do_vinery.crafting.ApplePressDummyRecipe;
import steve_gall.minecolonies_compatibility.module.common.lets_do_vinery.init.ModuleMenuTypes;

public class ApplePressTeachMenu extends TeachRecipeMenu<ApplePressDummyRecipe>
{
	public static final int INVENTORY_X = 8;
	public static final int INVENTORY_Y = 84;

	public static final int CRAFTING_X = 44;
	public static final int CRAFTING_Y = 34;

	public static final int RESULT_X = 116;
	public static final int RESULT_Y = 34;

	public ApplePressTeachMenu(int windowId, Inventory inventory, IBuildingModule module)
	{
		super(ModuleMenuTypes.APPLE_PRESS_TEACH.get(), windowId, inventory, module);
		this.setup();
	}

	public ApplePressTeachMenu(int windowId, Inventory inventory, FriendlyByteBuf buffer)
	{
		super(ModuleMenuTypes.APPLE_PRESS_TEACH.get(), windowId, inventory, buffer);
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
	protected IMenuRecipeValidator<ApplePressDummyRecipe> createRecipeValidator()
	{
		return new IMenuRecipeValidator<>()
		{
			@Override
			public CompoundTag serialize(IFactoryController controller, ApplePressDummyRecipe recipe)
			{
				return recipe.serializeNBT();
			}

			@Override
			public List<ApplePressDummyRecipe> findAll(Container container, ServerPlayer player)
			{
				var map = ApplePressCraftingType.getRecipes();
				var ingredient = container.getItem(0).getItem();
				var result = map.get(ingredient);

				if (result != null)
				{
					return Collections.singletonList(new ApplePressDummyRecipe(ingredient, result));
				}
				else
				{
					return Collections.emptyList();
				}

			}

			@Override
			public ApplePressDummyRecipe deserialize(IFactoryController controller, CompoundTag tag)
			{
				return new ApplePressDummyRecipe(tag);
			}
		};
	}

	@Override
	protected void setContainerByTransfer(@NotNull ApplePressDummyRecipe recipe, @NotNull CompoundTag payload)
	{
		super.setContainerByTransfer(recipe, payload);

		this.inputContainer.setItem(0, ItemStack.of(payload.getCompound("input")));
	}

	@Override
	protected void onRecipeChanged()
	{
		this.resultContainer.setItem(0, this.recipe != null ? this.recipe.getResultItem() : ItemStack.EMPTY);
	}

}
