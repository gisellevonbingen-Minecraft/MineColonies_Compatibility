package steve_gall.minecolonies_compatibility.module.client.butchercraft;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.lance5057.butchercraft.workstations.grinder.GrinderRecipe;
import com.minecolonies.api.crafting.ItemStorage;
import com.minecolonies.api.crafting.registry.CraftingType;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.crafting.RecipeHolder;
import steve_gall.minecolonies_compatibility.core.client.gui.TeachCraftingRecipeScreen;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.core.common.crafting.ItemStorageHelper;
import steve_gall.minecolonies_compatibility.module.common.butchercraft.crafting.GrinderRecipeStorage;
import steve_gall.minecolonies_compatibility.module.common.butchercraft.init.ModuleCraftingTypes;
import steve_gall.minecolonies_compatibility.module.common.butchercraft.menu.GrinderTeachMenu;
import steve_gall.minecolonies_tweaks.api.common.crafting.ICustomizedRecipeStorage;

public class GrinderTeachScreen extends TeachCraftingRecipeScreen<GrinderTeachMenu, RecipeHolder<GrinderRecipe>>
{
	public static final ResourceLocation TEXTURE = MineColoniesCompatibility.rl("textures/gui/butchercraft_grinder_teach.png");

	public GrinderTeachScreen(GrinderTeachMenu menu, Inventory inventory, Component title)
	{
		super(menu, inventory, title);

		this.imageWidth = 176;
		this.imageHeight = 166;
	}

	@Override
	public ResourceLocation getTexture()
	{
		return TEXTURE;
	}

	@Override
	public CraftingType getCraftingType()
	{
		return ModuleCraftingTypes.GRINDER.get();
	}

	@Override
	protected ICustomizedRecipeStorage createRecipeStorage(RecipeHolder<GrinderRecipe> recipe, List<ItemStorage> input)
	{
		var ingredient = input.get(0);
		ingredient.setAmount(recipe.value().count());
		var attachment = input.get(1);
		var casing = input.get(2);

		var resultContainer = this.menu.getResultContainer();
		var output = resultContainer.getItem(0);
		return new GrinderRecipeStorage(recipe.id(), ItemStorageHelper.filterNotEmpty(Arrays.asList(ingredient, casing)), attachment, output);
	}

	@Override
	protected void renderTooltip(GuiGraphics graphics, int mouseX, int mouseY)
	{
		if (this.hoveredSlot != null)
		{
			if (this.menu.getCarried().isEmpty() && this.hoveredSlot.hasItem())
			{
				var itemstack = this.hoveredSlot.getItem();
				graphics.renderTooltip(this.font, this.getTooltipFromContainerItem(itemstack), itemstack.getTooltipImage(), itemstack, mouseX, mouseY);
			}
			else if (this.hoveredSlot.container == this.menu.getInputContainer())
			{
				var slotIndex = this.hoveredSlot.getSlotIndex();
				var tooltip = Collections.<Component> emptyList();
				var prefix = MineColoniesCompatibility.tl("text.butchercraft_grinder.");

				if (slotIndex == 0)
				{
					tooltip = Arrays.asList(Component.translatable(prefix + "ingredient"));
				}
				else if (slotIndex == 1)
				{
					tooltip = Arrays.asList(Component.translatable(prefix + "attachment"));
				}
				else if (slotIndex == 2)
				{
					tooltip = Arrays.asList(Component.translatable(prefix + "casing"));
				}

				if (tooltip.size() > 0)
				{
					graphics.renderComponentTooltip(this.font, tooltip, mouseX, mouseY);
				}

			}

		}

	}

}
