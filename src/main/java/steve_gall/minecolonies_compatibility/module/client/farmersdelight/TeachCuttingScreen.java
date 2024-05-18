package steve_gall.minecolonies_compatibility.module.client.farmersdelight;

import java.util.List;

import com.minecolonies.api.crafting.ItemStorage;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import steve_gall.minecolonies_compatibility.core.client.gui.TeachRecipeScreen;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.module.common.farmersdelight.FarmersDelightModule;
import steve_gall.minecolonies_compatibility.module.common.farmersdelight.crafting.CuttingChanceResult;
import steve_gall.minecolonies_compatibility.module.common.farmersdelight.crafting.CuttingRecipeStorage;
import steve_gall.minecolonies_compatibility.module.common.farmersdelight.menu.TeachCuttingMenu;
import steve_gall.minecolonies_tweaks.api.common.crafting.ICustomizedRecipeStorage;
import vectorwing.farmersdelight.common.crafting.CuttingBoardRecipe;

public class TeachCuttingScreen extends TeachRecipeScreen<TeachCuttingMenu, CuttingBoardRecipe>
{
	public static final ResourceLocation TEXTURE = MineColoniesCompatibility.rl("textures/gui/farmers/teach_cutting.png");

	public TeachCuttingScreen(TeachCuttingMenu menu, Inventory inventory, Component title)
	{
		super(menu, inventory, title);

		this.imageWidth = 176;
		this.imageHeight = 166;
	}

	@Override
	protected ICustomizedRecipeStorage createRecipeStorage(CuttingBoardRecipe recipe, List<ItemStorage> input)
	{
		var results = recipe.getRollableResults().stream().map(CuttingChanceResult::new).toList();
		return new CuttingRecipeStorage(recipe.getId(), input, results, this.menu.getToolType());
	}

	@Override
	public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks)
	{
		this.renderBackground(graphics);

		super.render(graphics, mouseX, mouseY, partialTicks);

		if (this.menu.getCarried().isEmpty() && this.hoveredSlot != null && this.hoveredSlot.hasItem())
		{
			var item = this.hoveredSlot.getItem();
			var tooltip = getTooltipFromItem(this.minecraft, item);
			var tooltipImage = item.getTooltipImage();

			var resultIndex = this.menu.getResultSlots().indexOf(this.hoveredSlot);

			if (resultIndex > -1)
			{
				tooltip.addAll(1, FarmersDelightModule.getChanceTooltip(this.menu.getResults().get(resultIndex).getChance()));
			}

			graphics.renderTooltip(this.font, tooltip, tooltipImage, mouseX, mouseY);
		}

	}

	@Override
	protected void renderBg(GuiGraphics graphics, float partialTicks, int mouseX, int mouseY)
	{
		graphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

		var results = this.menu.getResults();
		var resultSlots = this.menu.getResultSlots();

		for (int i = 0; i < results.size(); i++)
		{
			var slot = resultSlots.get(i);
			var alt = results.get(i).getChance() < 1.0F;
			graphics.blit(TEXTURE, this.leftPos + slot.x - 1, this.topPos + slot.y - 1, this.imageWidth + (alt ? 18 : 0), 0, 18, 18);
		}

	}

}
