package steve_gall.minecolonies_compatibility.core.common.inventory;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.minecolonies.api.colony.buildings.modules.IBuildingModule;
import com.minecolonies.api.util.constant.Constants;
import com.minecolonies.api.util.constant.TranslationConstants;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.UpgradeRecipe;
import steve_gall.minecolonies_compatibility.api.common.inventory.IMenuRecipeValidator;
import steve_gall.minecolonies_compatibility.api.common.inventory.MenuRecipeValidatorRecipe;
import steve_gall.minecolonies_compatibility.core.common.crafting.SmithingCraftingType;
import steve_gall.minecolonies_compatibility.core.common.init.ModMenuTypes;
import steve_gall.minecolonies_compatibility.mixin.common.minecraft.SmithingRecipeAccessor;

public class SmithingTeachMenu extends TeachRecipeMenu<UpgradeRecipe>
{
	public static final int INVENTORY_X = 8;
	public static final int INVENTORY_Y = 84;

	public static final int BASE_X = 27;
	public static final int BASE_Y = 48;
	public static final int ADDTION_X = 76;
	public static final int ADDTION_Y = 48;
	public static final int RESULT_X = 134;
	public static final int RESULT_Y = 48;

	private final int buildingLevel;

	public SmithingTeachMenu(int windowId, Inventory inventory, IBuildingModule module)
	{
		super(ModMenuTypes.SMITHING_TEACH.get(), windowId, inventory, module);

		this.buildingLevel = module.getBuilding().getBuildingLevel();
		this.setup();
	}

	public SmithingTeachMenu(int windowId, Inventory inventory, FriendlyByteBuf buffer)
	{
		super(ModMenuTypes.SMITHING_TEACH.get(), windowId, inventory, buffer);

		this.buildingLevel = buffer.readInt();
		this.setup();
	}

	private void setup()
	{
		this.addInventorySlots(INVENTORY_X, INVENTORY_Y);

		this.inputContainer = new TeachContainer(this, 2);
		this.inputSlots.add(this.addSlot(new TeachInputSlot(this.inputContainer, 0, BASE_X, BASE_Y)));
		this.inputSlots.add(this.addSlot(new TeachInputSlot(this.inputContainer, 1, ADDTION_X, ADDTION_Y)));

		this.resultContainer = new TeachContainer(this, 1);
		this.resultSlots.add(this.addSlot(new TeachResultSlot(this.resultContainer, 0, RESULT_X, RESULT_Y)));

	}

	@Override
	protected IMenuRecipeValidator<UpgradeRecipe> createRecipeValidator()
	{
		return new MenuRecipeValidatorRecipe<>(this.inventory.player.level)
		{
			@Override
			public RecipeType<UpgradeRecipe> getRecipeType()
			{
				return RecipeType.SMITHING;
			}

			@Override
			protected boolean test(UpgradeRecipe recipe, Container container, ServerPlayer player)
			{
				return recipe.matches(container, this.level);
			}
		};
	}

	@Override
	public @Nullable Component getCurrentError()
	{
		var base = this.inputContainer.getItem(0);
		var addition = this.inputContainer.getItem(1);
		var error = this.getRecipeError(base, addition);

		if (error != null)
		{
			return error;
		}

		return super.getCurrentError();
	}

	public @Nullable Component getRecipeError(ItemStack base, ItemStack addition)
	{
		return this.testRequiredLevel(SmithingCraftingType.getRequiredLevel(addition));
	}

	@Override
	public Component getRecipeError(UpgradeRecipe recipe)
	{
		if (recipe instanceof SmithingRecipeAccessor accessor)
		{
			var addition = accessor.getAddition();
			var error = this.testRequiredLevel(SmithingCraftingType.getRequiredMinLevel(addition));

			if (error != null)
			{
				return error;
			}

			return super.getRecipeError(recipe);
		}
		else
		{
			return TEXT_RECIPE_NOT_SUPPORTED;
		}

	}

	public Component testRequiredLevel(int requiredLevel)
	{
		if (this.buildingLevel < requiredLevel)
		{
			var maxLevel = Constants.MAX_BUILDING_LEVEL;

			if (requiredLevel == maxLevel)
			{
				return Component.translatable(TranslationConstants.PARTIAL_JEI_INFO + "onelevelrestriction.tip", requiredLevel);
			}
			else
			{
				return Component.translatable(TranslationConstants.PARTIAL_JEI_INFO + "levelrestriction.tip", requiredLevel, maxLevel);
			}

		}

		return null;
	}

	@Override
	protected void setContainerByTransfer(@NotNull UpgradeRecipe recipe, @NotNull CompoundTag payload)
	{
		super.setContainerByTransfer(recipe, payload);

		var input = payload.getList("input", Tag.TAG_COMPOUND);
		this.inputContainer.setItem(0, ItemStack.of(input.getCompound(0)));
		this.inputContainer.setItem(1, ItemStack.of(input.getCompound(1)));
	}

	@Override
	protected void onRecipeChanged()
	{
		if (this.recipe != null)
		{
			this.resultContainer.setItem(0, this.recipe.assemble(this.inputContainer));
		}
		else
		{
			this.resultContainer.setItem(0, ItemStack.EMPTY);
		}

	}

}
