package steve_gall.minecolonies_compatibility.core.client.gui;

import java.util.List;

import com.minecolonies.api.crafting.ItemStorage;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.crafting.SmithingRecipe;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.core.common.crafting.SmithingRecipeStorage;
import steve_gall.minecolonies_compatibility.core.common.inventory.SmithingTeachMenu;
import steve_gall.minecolonies_tweaks.api.common.crafting.ICustomizedRecipeStorage;

public class SmithingTeachScreen extends TeachCraftingRecipeScreen<SmithingTeachMenu, SmithingRecipe>
{
	public static final ResourceLocation TEXTURE = MineColoniesCompatibility.rl("textures/gui/smithing_teach.png");

	public SmithingTeachScreen(SmithingTeachMenu menu, Inventory inventory, Component title)
	{
		super(menu, inventory, title);

		this.imageWidth = 176;
		this.imageHeight = 166;
	}

	@Override
	protected ICustomizedRecipeStorage createRecipeStorage(SmithingRecipe recipe, List<ItemStorage> input)
	{
		var template = input.get(0);
		var base = input.get(1);
		var addition = input.get(2);
		var result = this.menu.getResultContainer().getItem(0);
		return new SmithingRecipeStorage(recipe.getId(), template, base, addition, result);
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
