package steve_gall.minecolonies_compatibility.core.common.inventory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.minecolonies.api.colony.buildings.modules.IBuildingModule;
import com.minecolonies.api.colony.requestsystem.StandardFactoryController;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;
import steve_gall.minecolonies_compatibility.api.common.inventory.IItemGhostMenu;
import steve_gall.minecolonies_compatibility.api.common.inventory.IItemGhostSlot;
import steve_gall.minecolonies_compatibility.api.common.inventory.IMenuRecipeValidator;
import steve_gall.minecolonies_compatibility.api.common.inventory.IRecipeTransferableMenu;
import steve_gall.minecolonies_compatibility.core.common.network.message.TeachRecipeMenuNewRecipesMessage;
import steve_gall.minecolonies_compatibility.core.common.network.message.TeachRecipeMenuNewResultMessage;
import steve_gall.minecolonies_compatibility.module.common.ModuleManager;
import steve_gall.minecolonies_compatibility.module.common.polymorph.PolymorphModule;

public abstract class TeachRecipeMenu<RECIPE, RECIPE_INPUT> extends ModuleMenu implements IItemGhostMenu, IRecipeTransferableMenu<RECIPE, RECIPE_INPUT>
{
	public static final Component TEXT_RECIPE_NOT_FOUND = Component.translatable("minecolonies_compatibility.text.recipe_not_found");
	public static final Component TEXT_RECIPE_NOT_SUPPORTED = Component.translatable("minecolonies_compatibility.text.recipe_not_supported");

	protected TeachContainer inputContainer;
	protected List<Slot> inputSlots;

	protected TeachContainer catalystContainer;
	protected List<Slot> catalystSlots;

	protected Container resultContainer;
	protected List<Slot> resultSlots;

	private IMenuRecipeValidator<RECIPE, RECIPE_INPUT> recipeValidator;
	private List<RECIPE> recipes;
	private int recipeIndex = -1;
	protected RECIPE recipe;

	public TeachRecipeMenu(MenuType<?> menuType, int windowId, Inventory inventory, IBuildingModule module)
	{
		super(menuType, windowId, inventory, module);
		this.setup();
	}

	public TeachRecipeMenu(MenuType<?> menuType, int windowId, Inventory inventory, FriendlyByteBuf buffer)
	{
		super(menuType, windowId, inventory, buffer);
		this.setup();
	}

	private void setup()
	{
		this.inputSlots = new ArrayList<>();
		this.catalystSlots = new ArrayList<>();
		this.resultSlots = new ArrayList<>();

		this.inputContainer = null;
		this.resultContainer = null;

		this.recipeValidator = null;
		this.recipes = Collections.emptyList();
		this.recipeIndex = -1;
		this.recipe = null;
	}

	protected abstract IMenuRecipeValidator<RECIPE, RECIPE_INPUT> createRecipeValidator();

	protected abstract void onRecipeChanged(@NotNull HolderLookup.Provider provider, @Nullable RECIPE_INPUT input);

	@Override
	public IMenuRecipeValidator<RECIPE, RECIPE_INPUT> getRecipeValidator()
	{
		if (this.recipeValidator == null)
		{
			this.recipeValidator = this.createRecipeValidator();
		}

		return this.recipeValidator;
	}

	@Override
	public final void onRecipeTransfer(@NotNull RECIPE recipe, @NotNull CompoundTag payload)
	{
		var registryAccess = this.inventory.player.registryAccess();
		this.setContainerByTransfer(registryAccess, recipe, payload);
		this.refreshRecipes(recipe);
	}

	protected void setContainerByTransfer(@NotNull HolderLookup.Provider provider, @NotNull RECIPE recipe, @NotNull CompoundTag payload)
	{
		this.inputContainer.clearContent();

		if (this.catalystContainer != null)
		{
			this.catalystContainer.clearContent();
		}

	}

	@Override
	public void onGhostAcceptItem(int slotNumber, ItemStack stack, boolean isVirtual)
	{
		if (!isVirtual)
		{
			var slot = this.slots.get(slotNumber);
			this.handleSlotClick(slot, stack);
		}

	}

	@Override
	public void slotsChanged(Container container)
	{
		this.onSlotsChanged(container);

		super.slotsChanged(container);
	}

	protected void onSlotsChanged(Container container)
	{
		if (container == this.inputContainer)
		{
			this.refreshRecipes(null);
		}

	}

	protected void refreshRecipes(RECIPE show)
	{
		if (this.inventory.player instanceof ServerPlayer player)
		{
			var recipeValidator = this.getRecipeValidator();
			this.recipes = new ArrayList<>(recipeValidator.findAll(this.inputContainer, player));
			this.onRecipesChanged();

			var tags = this.recipes.stream().map(r -> recipeValidator.serialize(player.registryAccess(), StandardFactoryController.getInstance(), r)).toList();
			PacketDistributor.sendToPlayer(player, new TeachRecipeMenuNewRecipesMessage(tags));

			if (show == null)
			{
				this.setRecipeIndex(Math.max(this.recipes.indexOf(this.recipe), 0));
			}
			else
			{
				this.setRecipeIndex(this.recipes.indexOf(show));
			}

		}

	}

	public final void onNewRecipesTransfer(List<RECIPE> recipes)
	{
		this.recipes = new ArrayList<>(recipes);
		this.onRecipesChanged();
		this.setRecipeIndex(-1);
	}

	protected void onRecipesChanged()
	{

	}

	protected void setRecipe(RECIPE recipe)
	{
		this.recipe = recipe;
		this.onRecipeChanged(this.inventory.player.registryAccess(), this.getRecipeInput(recipe));

		if (this.inventory.player instanceof ServerPlayer player)
		{
			var tag = recipe != null ? this.getRecipeValidator().serialize(player.registryAccess(), StandardFactoryController.getInstance(), recipe) : null;
			PacketDistributor.sendToPlayer(player, new TeachRecipeMenuNewResultMessage(tag));

			if (ModuleManager.POLYMORPH.isLoaded())
			{
				PolymorphModule.sendRecipesList(player, this);
			}

		}

	}

	public final void onNewResultTransfer(CompoundTag tag)
	{
		if (tag != null)
		{
			var recipe = this.getRecipeValidator().deserialize(this.inventory.player.registryAccess(), StandardFactoryController.getInstance(), tag);
			this.setRecipe(recipe);
		}
		else
		{
			this.setRecipe(null);
		}

	}

	@Nullable
	public Component getCurrentError()
	{
		return this.recipe != null ? this.getRecipeError(this.recipe) : TEXT_RECIPE_NOT_FOUND;
	}

	@Nullable
	public Component getRecipeError(@NotNull RECIPE recipe)
	{
		return null;
	}

	@Override
	public void clicked(int slotNumber, int clickedButton, ClickType mode, Player player)
	{
		if (0 <= slotNumber && slotNumber < this.slots.size())
		{
			var slot = this.slots.get(slotNumber);

			if (slot.container == this.inputContainer || slot.container == this.catalystContainer)
			{
				if (mode == ClickType.PICKUP || mode == ClickType.PICKUP_ALL || mode == ClickType.SWAP)
				{
					this.handleSlotClick(slot, this.getCarried());
				}

				return;
			}

			if (mode == ClickType.QUICK_MOVE)
			{
				return;
			}

		}

		super.clicked(slotNumber, clickedButton, mode, player);
	}

	public void handleSlotClick(Slot slot, ItemStack stack)
	{
		if (stack.isEmpty())
		{

		}
		else if (slot instanceof IItemGhostSlot ghostSlot && !ghostSlot.canAccept(stack))
		{
			return;
		}

		this.setSlot(slot, stack);
	}

	protected void setSlot(Slot slot, ItemStack stack)
	{
		if (stack.getCount() > 0)
		{
			var copy = stack.copy();
			copy.setCount(1);
			slot.set(copy);
		}
		else if (slot.getItem().getCount() > 0)
		{
			slot.set(ItemStack.EMPTY);
		}

	}

	@Override
	public boolean canTakeItemForPickAll(ItemStack stack, Slot slot)
	{
		return !this.resultSlots.contains(slot) && super.canTakeItemForPickAll(stack, slot);
	}

	@Override
	public ItemStack quickMoveStack(Player player, int slot)
	{
		return ItemStack.EMPTY;
	}

	@Override
	public boolean stillValid(Player player)
	{
		return true;
	}

	public RECIPE_INPUT getRecipeInput(RECIPE recipe)
	{
		return this.getRecipeValidator().getInput(this.inputContainer, recipe);
	}

	public TeachContainer getInputContainer()
	{
		return this.inputContainer;
	}

	public List<Slot> getInputSlots()
	{
		return Collections.unmodifiableList(this.inputSlots);
	}

	public TeachContainer getCatalystContainer()
	{
		return this.catalystContainer;
	}

	public List<Slot> getCatalystSlots()
	{
		return Collections.unmodifiableList(this.catalystSlots);
	}

	public Container getResultContainer()
	{
		return this.resultContainer;
	}

	public List<Slot> getResultSlots()
	{
		return Collections.unmodifiableList(this.resultSlots);
	}

	public RECIPE getRecipe()
	{
		return this.recipe;
	}

	public int getRecipeIndex()
	{
		return this.recipeIndex;
	}

	public void setRecipeIndex(int index)
	{
		if (0 <= index && index < this.recipes.size())
		{
			this.recipeIndex = index;
			this.setRecipe(this.recipes.get(index));
		}
		else
		{
			this.recipeIndex = -1;
			this.setRecipe(null);
		}

	}

	public int findRecipeIndex(ResourceLocation recipeId)
	{
		if (recipeId != null)
		{
			var recipeValidator = this.getRecipeValidator();

			for (var i = 0; i < this.recipes.size(); i++)
			{
				if (recipeId.equals(recipeValidator.getRecipeId(this.recipes.get(i))))
				{
					return i;
				}

			}

		}

		return -1;
	}

	public List<RECIPE> getRecipes()
	{
		return this.recipes;
	}

}
