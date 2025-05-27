package steve_gall.minecolonies_compatibility.module.client.lets_do_vinery;

import java.util.List;

import com.minecolonies.api.crafting.ItemStorage;
import com.minecolonies.api.crafting.registry.CraftingType;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import steve_gall.minecolonies_compatibility.core.client.gui.TeachCraftingRecipeScreen;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.module.common.lets_do_vinery.crafting.ApplePressDummyRecipe;
import steve_gall.minecolonies_compatibility.module.common.lets_do_vinery.crafting.ApplePressRecipeStorage;
import steve_gall.minecolonies_compatibility.module.common.lets_do_vinery.init.ModuleCraftingTypes;
import steve_gall.minecolonies_compatibility.module.common.lets_do_vinery.menu.ApplePressTeachMenu;
import steve_gall.minecolonies_tweaks.api.common.crafting.ICustomizedRecipeStorage;

public class ApplePressTeachScreen extends TeachCraftingRecipeScreen<ApplePressTeachMenu, ApplePressDummyRecipe>
{
	public static final ResourceLocation TEXTURE = MineColoniesCompatibility.rl("textures/gui/lets_do_vinery_apple_press_teach.png");

	public ApplePressTeachScreen(ApplePressTeachMenu menu, Inventory inventory, Component title)
	{
		super(menu, inventory, title);

		this.imageWidth = 176;
		this.imageHeight = 166;
	}

	@Override
	public CraftingType getCraftingType()
	{
		return ModuleCraftingTypes.APPLE_PRESS.get();
	}

	@Override
	protected ICustomizedRecipeStorage createRecipeStorage(ApplePressDummyRecipe recipe, List<ItemStorage> input)
	{
		var resultContainer = this.menu.getResultContainer();
		var output = resultContainer.getItem(0);
		return new ApplePressRecipeStorage(recipe.getId(), input, output);
	}

	@Override
	public void render(PoseStack pose, int mouseX, int mouseY, float partialTicks)
	{
		this.renderBackground(pose);

		super.render(pose, mouseX, mouseY, partialTicks);

		this.renderTooltip(pose, mouseX, mouseY);
	}

	@Override
	protected void renderBg(PoseStack pose, float partialTicks, int mouseX, int mouseY)
	{
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, TEXTURE);

		this.blit(pose, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
	}

}
