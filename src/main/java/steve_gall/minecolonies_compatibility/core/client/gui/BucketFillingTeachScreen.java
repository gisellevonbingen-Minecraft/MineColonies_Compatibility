package steve_gall.minecolonies_compatibility.core.client.gui;

import java.util.List;

import com.minecolonies.api.crafting.ItemStorage;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.core.common.crafting.BucketFillingRecipeStorage;
import steve_gall.minecolonies_compatibility.core.common.inventory.BucketFillingTeachMenu;
import steve_gall.minecolonies_tweaks.api.common.crafting.ICustomizedRecipeStorage;

public class BucketFillingTeachScreen extends TeachCraftingRecipeScreen<BucketFillingTeachMenu, BucketFillingRecipeStorage>
{
	public static final ResourceLocation TEXTURE = MineColoniesCompatibility.rl("textures/gui/bucket_filling_teach.png");

	public BucketFillingTeachScreen(BucketFillingTeachMenu menu, Inventory inventory, Component title)
	{
		super(menu, inventory, title);

		this.imageWidth = 176;
		this.imageHeight = 166;
	}

	@Override
	protected ICustomizedRecipeStorage createRecipeStorage(BucketFillingRecipeStorage recipe, List<ItemStorage> input)
	{
		return recipe;
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
