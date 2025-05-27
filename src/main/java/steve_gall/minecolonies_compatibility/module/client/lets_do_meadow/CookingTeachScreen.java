package steve_gall.minecolonies_compatibility.module.client.lets_do_meadow;

import java.util.List;

import com.minecolonies.api.crafting.ItemStorage;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.satisfy.meadow.core.recipes.CookingCauldronRecipe;
import steve_gall.minecolonies_compatibility.core.client.gui.TeachCraftingRecipeScreen;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.module.common.lets_do_meadow.crafting.CookingRecipeStorage;
import steve_gall.minecolonies_compatibility.module.common.lets_do_meadow.menu.CookingTeachMenu;
import steve_gall.minecolonies_tweaks.api.common.crafting.ICustomizedRecipeStorage;

public class CookingTeachScreen extends TeachCraftingRecipeScreen<CookingTeachMenu, CookingCauldronRecipe>
{
	public static final ResourceLocation TEXTURE = MineColoniesCompatibility.rl("textures/gui/lets_do_meadow_cooking_teach.png");

	public CookingTeachScreen(CookingTeachMenu menu, Inventory inventory, Component title)
	{
		super(menu, inventory, title);

		this.imageWidth = 176;
		this.imageHeight = 166;
	}

	@Override
	protected ICustomizedRecipeStorage createRecipeStorage(CookingCauldronRecipe recipe, List<ItemStorage> input)
	{
		var resultContainer = this.menu.getResultContainer();
		var output = resultContainer.getItem(0);
		return new CookingRecipeStorage(recipe.getId(), input, output);
	}

	@Override
	public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks)
	{
		this.renderBackground(graphics);

		super.render(graphics, mouseX, mouseY, partialTicks);

		this.renderTooltip(graphics, mouseX, mouseY);
	}

	@Override
	protected void renderBg(GuiGraphics graphics, float partialTicks, int mouseX, int mouseY)
	{
		graphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
	}

}
