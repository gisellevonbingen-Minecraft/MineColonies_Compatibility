package steve_gall.minecolonies_compatibility.module.common.butchercraft.menu;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.lance5057.butchercraft.ButchercraftItems;
import com.lance5057.butchercraft.ButchercraftRecipes;
import com.lance5057.butchercraft.tags.ButchercraftItemTags;
import com.lance5057.butchercraft.workstations.grinder.GrinderContainer;
import com.lance5057.butchercraft.workstations.grinder.GrinderRecipe;
import com.minecolonies.api.colony.buildings.modules.IBuildingModule;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import steve_gall.minecolonies_compatibility.api.common.inventory.IMenuRecipeValidator;
import steve_gall.minecolonies_compatibility.api.common.inventory.MenuRecipeValidatorRecipe;
import steve_gall.minecolonies_compatibility.core.common.inventory.TeachContainer;
import steve_gall.minecolonies_compatibility.core.common.inventory.TeachInputSlot;
import steve_gall.minecolonies_compatibility.core.common.inventory.TeachRecipeMenu;
import steve_gall.minecolonies_compatibility.core.common.inventory.TeachResultSlot;
import steve_gall.minecolonies_compatibility.core.common.util.NBTUtils2;
import steve_gall.minecolonies_compatibility.module.common.butchercraft.init.ModuleMenuTypes;
import steve_gall.minecolonies_tweaks.core.common.item.ItemSerializationHelper;

public class GrinderTeachMenu extends TeachRecipeMenu<RecipeHolder<GrinderRecipe>, GrinderContainer>
{
	public static final int INVENTORY_X = 8;
	public static final int INVENTORY_Y = 84;

	public static final int INGREDIENT_X = 26;
	public static final int INGREDIENT_Y = 35;
	public static final int ATTACHMENT_X = 44;
	public static final int ATTACHMENT_Y = 35;
	public static final int CASING_X = 62;
	public static final int CASING_Y = 35;
	public static final int RESULT_X = 116;
	public static final int RESULT_Y = 35;

	public GrinderTeachMenu(int windowId, Inventory inventory, IBuildingModule module)
	{
		super(ModuleMenuTypes.GRINDER_TEACH.get(), windowId, inventory, module);
		this.setup();
	}

	public GrinderTeachMenu(int windowId, Inventory inventory, RegistryFriendlyByteBuf buffer)
	{
		super(ModuleMenuTypes.GRINDER_TEACH.get(), windowId, inventory, buffer);
		this.setup();
	}

	private void setup()
	{
		this.addInventorySlots(INVENTORY_X, INVENTORY_Y);

		this.inputContainer = new TeachContainer(this, 3);
		this.inputSlots.add(this.addSlot(new TeachInputSlot(this.inputContainer, 0, INGREDIENT_X, INGREDIENT_Y)));
		this.inputSlots.add(this.addSlot(new TeachInputSlot(this.inputContainer, 1, ATTACHMENT_X, ATTACHMENT_Y)));
		this.inputSlots.add(this.addSlot(new TeachInputSlot(this.inputContainer, 2, CASING_X, CASING_Y)));

		this.resultContainer = new TeachContainer(this, 1);
		this.resultSlots.add(this.addSlot(new TeachResultSlot(this.resultContainer, 0, RESULT_X, RESULT_Y)));
	}

	@Override
	protected IMenuRecipeValidator<RecipeHolder<GrinderRecipe>, GrinderContainer> createRecipeValidator()
	{
		return new MenuRecipeValidatorRecipe<>(this.inventory.player.level())
		{
			@Override
			public RecipeType<GrinderRecipe> getRecipeType()
			{
				return ButchercraftRecipes.GRINDER.get();
			}

			@Override
			public @NotNull GrinderContainer getInput(@NotNull Container container, @Nullable RecipeHolder<GrinderRecipe> recipe)
			{
				return new GrinderContainer(container.getItem(0), container.getItem(1));
			}

			@Override
			protected boolean test(RecipeHolder<GrinderRecipe> recipeHolder, Container container, ServerPlayer player)
			{
				if (!super.test(recipeHolder, container, player))
				{
					return false;
				}

				var attachment = container.getItem(1);
				var casing = container.getItem(2);

				if (attachment.is(ButchercraftItems.EXTRUDER_TIP.get()))
				{
					return casing.is(ButchercraftItemTags.SAUSAGE_CASING);
				}
				else
				{
					return casing.isEmpty();
				}
			}

		};

	}

	@Override
	protected void setContainerByTransfer(@NotNull HolderLookup.Provider provider, @NotNull RecipeHolder<GrinderRecipe> recipe, @NotNull CompoundTag payload)
	{
		super.setContainerByTransfer(provider, recipe, payload);

		var input = NBTUtils2.deserializeList(payload, "input", ItemSerializationHelper.deserializerTag(provider));
		this.inputContainer.setItem(0, input.get(0));
		this.inputContainer.setItem(1, input.get(1));
		this.inputContainer.setItem(2, input.get(2));
	}

	@Override
	protected void onRecipeChanged(HolderLookup.Provider provider, GrinderContainer input)
	{
		this.resultContainer.setItem(0, this.recipe != null ? this.recipe.value().assemble(input, provider) : ItemStack.EMPTY);
	}

}
