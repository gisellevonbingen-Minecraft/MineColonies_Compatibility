package steve_gall.minecolonies_compatibility.core.common.inventory;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.minecolonies.api.colony.buildings.modules.IBuildingModule;
import com.minecolonies.api.util.constant.Constants;
import com.minecolonies.api.util.constant.TranslationConstants;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmithingRecipe;
import net.minecraft.world.item.crafting.SmithingRecipeInput;
import steve_gall.minecolonies_compatibility.api.common.inventory.IMenuRecipeValidator;
import steve_gall.minecolonies_compatibility.api.common.inventory.MenuRecipeValidatorRecipe;
import steve_gall.minecolonies_compatibility.core.common.crafting.SmithingCraftingType;
import steve_gall.minecolonies_compatibility.core.common.crafting.SmithingRecipeAccessor;
import steve_gall.minecolonies_compatibility.core.common.init.ModMenuTypes;
import steve_gall.minecolonies_tweaks.core.common.item.ItemSerializationHelper;

public class SmithingTeachMenu extends TeachRecipeMenu<RecipeHolder<SmithingRecipe>, SmithingRecipeInput>
{
	public static final int INVENTORY_X = 8;
	public static final int INVENTORY_Y = 84;

	public static final int TEMPLATE_X = 8;
	public static final int TEMPLATE_Y = 48;
	public static final int BASE_X = 26;
	public static final int BASE_Y = 48;
	public static final int ADDTION_X = 44;
	public static final int ADDTION_Y = 48;
	public static final int RESULT_X = 98;
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

		this.inputContainer = new TeachContainer(this, 3);
		this.inputSlots.add(this.addSlot(new TeachInputSlot(this.inputContainer, 0, TEMPLATE_X, TEMPLATE_Y)));
		this.inputSlots.add(this.addSlot(new TeachInputSlot(this.inputContainer, 1, BASE_X, BASE_Y)));
		this.inputSlots.add(this.addSlot(new TeachInputSlot(this.inputContainer, 2, ADDTION_X, ADDTION_Y)));

		this.resultContainer = new TeachContainer(this, 1);
		this.resultSlots.add(this.addSlot(new TeachResultSlot(this.resultContainer, 0, RESULT_X, RESULT_Y)));

	}

	@Override
	protected IMenuRecipeValidator<RecipeHolder<SmithingRecipe>, SmithingRecipeInput> createRecipeValidator()
	{
		return new MenuRecipeValidatorRecipe<>(this.inventory.player.level())
		{
			@Override
			public RecipeType<SmithingRecipe> getRecipeType()
			{
				return RecipeType.SMITHING;
			}

			@Override
			public @NotNull SmithingRecipeInput getInput(Container container, RecipeHolder<SmithingRecipe> recipe)
			{
				return new SmithingRecipeInput(container.getItem(0), container.getItem(1), container.getItem(2));
			}

		};
	}

	@Override
	public @Nullable Component getCurrentError()
	{
		var error = this.getRecipeError(this.getRecipeInput(this.recipe));

		if (error != null)
		{
			return error;
		}

		return super.getCurrentError();
	}

	public @Nullable Component getRecipeError(SmithingRecipeInput input)
	{
		return this.testRequiredLevel(SmithingCraftingType.getRequiredLevel(input.addition()));
	}

	@Override
	public Component getRecipeError(RecipeHolder<SmithingRecipe> recipe)
	{
		if (recipe.value() instanceof SmithingRecipeAccessor accessor)
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
	protected void setContainerByTransfer(@NotNull HolderLookup.Provider provider, @NotNull RecipeHolder<SmithingRecipe> recipe, @NotNull CompoundTag payload)
	{
		super.setContainerByTransfer(provider, recipe, payload);

		var input = payload.getList("input", Tag.TAG_COMPOUND);
		this.inputContainer.setItem(0, ItemSerializationHelper.deserializeTag(provider, input.getCompound(0)));
		this.inputContainer.setItem(1, ItemSerializationHelper.deserializeTag(provider, input.getCompound(1)));
		this.inputContainer.setItem(2, ItemSerializationHelper.deserializeTag(provider, input.getCompound(2)));
	}

	@Override
	protected void onRecipeChanged(HolderLookup.Provider provider, SmithingRecipeInput input)
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
