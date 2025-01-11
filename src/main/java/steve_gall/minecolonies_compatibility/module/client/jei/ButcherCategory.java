package steve_gall.minecolonies_compatibility.module.client.jei;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.minecolonies.api.colony.jobs.IJob;
import com.minecolonies.api.util.constant.TranslationConstants;
import com.minecolonies.core.compatibility.jei.JobBasedRecipeCategory;
import com.minecolonies.core.compatibility.jei.RenderHelper;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import steve_gall.minecolonies_compatibility.api.common.butcher.ButcherableIconCache;
import steve_gall.minecolonies_compatibility.api.common.crafting.ToolOrIngredientStack;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;

public class ButcherCategory extends JobBasedRecipeCategory<ButcherableIconCache>
{
	protected static final int TOOL_X = WIDTH - 18;
	protected static final int TOOL_Y = CITIZEN_Y - 20;
	protected static final int BLOCK_X = CITIZEN_X + CITIZEN_W + 16;
	protected static final int BLOCK_Y = CITIZEN_Y - 4;
	protected static final int BLOCK_X2 = BLOCK_X - 12;
	protected static final int BLOCK_Y2 = BLOCK_Y - 8;
	protected static final int ARROW_WIDTH = 24;
	protected static final int ARROW_HEIGHT = 18;
	protected static final int ARROW_X = CITIZEN_X + CITIZEN_W + 4;
	protected static final int ARROW_Y = CITIZEN_Y + (CITIZEN_H - ARROW_HEIGHT) / 2 - 12;
	protected static final int INPUT_X = ARROW_X + ARROW_WIDTH + 4;
	protected static final int INPUT_Y = ARROW_Y;
	protected static final int OUTPUT_X = CITIZEN_X + CITIZEN_W + 8;
	protected static final int OUTPUT_Y = CITIZEN_Y + CITIZEN_H - 25;

	private final IDrawableStatic icon;
	private final IDrawableStatic arrow;

	public ButcherCategory(@NotNull IJob<?> job, @NotNull RecipeType<ButcherableIconCache> type, @NotNull IGuiHelper guiHelper)
	{
		super(job, type, new ItemStack(Blocks.STONE), guiHelper);

		this.icon = guiHelper.drawableBuilder(MineColoniesCompatibility.rl("textures/jei/butcher.png"), 0, 0, 16, 16).setTextureSize(16, 16).build();
		this.arrow = guiHelper.createDrawable(TEXTURE, 20, 121, ARROW_WIDTH, ARROW_HEIGHT);
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, ButcherableIconCache recipe, IFocusGroup focuses)
	{
		var inputSLot = builder.addSlot(RecipeIngredientRole.INPUT, INPUT_X, INPUT_Y);
		inputSLot.addItemStacks(recipe.getItemIcons());

		var outputs = recipe.getOutputIcons();
		var cols = 6;
		var outputY = OUTPUT_Y - 9 * ((outputs.size() - 1) / cols);
		for (int i = 0; i < outputs.size(); i++)
		{
			var xi = i % cols;
			var yi = i / cols;
			var slot = builder.addSlot(RecipeIngredientRole.OUTPUT, OUTPUT_X + xi * 18, outputY + yi * 18);
			slot.setBackground(this.slot, -1, -1);
			slot.addIngredients(outputs.get(i));
		}

		var tools = new ArrayList<>(recipe.getButcherable().getToolsForIcon());
		tools.removeIf(ToolOrIngredientStack::isEmpty);

		var toolsX = TOOL_X - (tools.size() - 1) * 18;

		for (var i = 0; i < tools.size(); i++)
		{
			var ingredient = tools.get(i);
			var x = toolsX + i * 18;
			var y = TOOL_Y;

			if (ingredient.isToolType())
			{
				this.addToolSlot(builder, ingredient.toolType(), x, y, true);
			}
			else
			{
				var slot = builder.addSlot(RecipeIngredientRole.CATALYST, x, y);
				slot.setBackground(this.slot, -1, -1);
				slot.addIngredients(ingredient.stack().ingredient());
			}

		}

	}

	@Override
	public void draw(ButcherableIconCache recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics graphics, double mouseX, double mouseY)
	{
		super.draw(recipe, recipeSlotsView, graphics, mouseX, mouseY);

		this.arrow.draw(graphics, ARROW_X, ARROW_Y);

		var tableIcons = recipe.getTableIcons();
		for (var i = 0; i < tableIcons.size(); i++)
		{
			RenderHelper.renderBlock(graphics.pose(), tableIcons.get(i), BLOCK_X + i * 32, BLOCK_Y, 100, -30F, 30F, 16F);
		}

	}

	@Override
	public @NotNull List<Component> getTooltipStrings(@NotNull ButcherableIconCache recipe, @NotNull IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY)
	{
		var tooltips = super.getTooltipStrings(recipe, recipeSlotsView, mouseX, mouseY);

		var tableIcons = recipe.getTableIcons();
		for (var i = 0; i < tableIcons.size(); i++)
		{
			if (new Rect2i(BLOCK_X2 + i * 32, BLOCK_Y2, 24, 24).contains((int) mouseX, (int) mouseY))
			{
				tooltips.add(Component.translatable(TranslationConstants.PARTIAL_JEI_INFO + "intermediate.tip", tableIcons.get(i).getBlock().getName()));
			}

		}

		return tooltips;
	}

	@Override
	protected @NotNull List<Component> generateInfoBlocks(@NotNull ButcherableIconCache recipe)
	{
		return Collections.emptyList();
	}

	@Override
	public @NotNull IDrawable getIcon()
	{
		return this.icon;
	}

}
