package steve_gall.minecolonies_compatibility.module.client.jei;

import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.minecolonies.api.colony.jobs.IJob;
import com.minecolonies.core.compatibility.jei.JobBasedRecipeCategory;
import com.mojang.blaze3d.vertex.PoseStack;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import steve_gall.minecolonies_compatibility.api.common.plant.FruitIconCache;

public class OrchardistCategory extends JobBasedRecipeCategory<FruitIconCache>
{
	private final IDrawableStatic arrow;

	public OrchardistCategory(@NotNull IJob<?> job, @NotNull RecipeType<FruitIconCache> type, @NotNull ItemStack icon, @NotNull IGuiHelper guiHelper)
	{
		super(job, type, icon, guiHelper);

		this.arrow = guiHelper.createDrawable(TEXTURE, 20, 121, 24, 18);
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, FruitIconCache recipe, IFocusGroup focuses)
	{
		var inputs = recipe.getBlockIcons();
		for (var i = 0; i < inputs.size(); i++)
		{
			var x = CITIZEN_X + CITIZEN_X + CITIZEN_W + 32;
			var y = CITIZEN_Y + (CITIZEN_H - this.arrow.getHeight()) / 2;
			var slot = builder.addSlot(RecipeIngredientRole.INPUT, x + i * 18, y);
			slot.addItemStack(inputs.get(i));
		}

		var outputs = recipe.getItemIcons();
		for (int i = 0; i < outputs.size(); i++)
		{
			var x = CITIZEN_X + CITIZEN_W + 8 + i * 18;
			var y = CITIZEN_Y + CITIZEN_H - 17;
			var slot = builder.addSlot(RecipeIngredientRole.OUTPUT, x, y);
			slot.setBackground(this.slot, -1, -1);
			slot.addItemStack(outputs.get(i));
		}

		this.addToolSlot(builder, recipe.getFruit().getHarvestToolType(), WIDTH - 18, CITIZEN_Y - 20, true);
	}

	@Override
	public void draw(FruitIconCache recipe, IRecipeSlotsView recipeSlotsView, PoseStack pose, double mouseX, double mouseY)
	{
		super.draw(recipe, recipeSlotsView, pose, mouseX, mouseY);

		this.arrow.draw(pose, CITIZEN_X + CITIZEN_W + 4, CITIZEN_Y + (CITIZEN_H - this.arrow.getHeight()) / 2);
	}

	@Override
	protected @NotNull List<Component> generateInfoBlocks(@NotNull FruitIconCache recipe)
	{
		return Collections.emptyList();
	}

}
