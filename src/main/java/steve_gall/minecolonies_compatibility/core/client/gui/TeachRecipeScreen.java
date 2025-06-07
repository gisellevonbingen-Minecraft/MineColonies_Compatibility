package steve_gall.minecolonies_compatibility.core.client.gui;

import java.util.List;

import com.minecolonies.api.crafting.ItemStorage;
import com.minecolonies.api.util.constant.Constants;
import com.minecolonies.api.util.constant.translation.BaseGameTranslationConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.items.wrapper.InvWrapper;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.core.common.inventory.TeachRecipeMenu;
import steve_gall.minecolonies_compatibility.core.common.item.ItemHandlerHelper2;
import steve_gall.minecolonies_compatibility.core.common.network.message.TeachRecipeMenuSwitchingMessage;
import steve_gall.minecolonies_compatibility.module.common.ModuleManager;

public abstract class TeachRecipeScreen<MENU extends TeachRecipeMenu<RECIPE>, RECIPE> extends AbstractContainerScreen<MENU>
{
	public static final Component TEXT_DONE = Component.translatable(BaseGameTranslationConstants.BASE_GUI_DONE);

	public static final ResourceLocation SWITCH_TEXTURE = new ResourceLocation(Constants.MOD_ID, "textures/gui/craftingswitch.png");
	public static final int SWITCH_WIDTH = 20;
	public static final int SWITCH_HEIGHT = 18;
	public static final int SWITCH_X_OFFSET = 148;
	public static final int SWITCH_Y_OFFSET = 43 - (SWITCH_HEIGHT / 2);

	private Button doneButton;
	private ImageButton switchButton;

	private Component lastError;

	public TeachRecipeScreen(MENU menu, Inventory inventory, Component title)
	{
		super(menu, inventory, title);
	}

	@Override
	protected void init()
	{
		super.init();

		this.doneButton = new Button(this.leftPos + 1, this.topPos + this.imageHeight + 4, 150, 20, TEXT_DONE, this::onDoneButtonPress);
		this.doneButton.active = false;
		this.addRenderableWidget(this.doneButton);

		this.switchButton = new ImageButton(this.leftPos + this.getSwitchButtonX(), this.topPos + this.getSwitchButtonY(), SWITCH_WIDTH, SWITCH_HEIGHT, 0, 0, SWITCH_HEIGHT + 1, SWITCH_TEXTURE, btn ->
		{
			MineColoniesCompatibility.network().sendToServer(new TeachRecipeMenuSwitchingMessage());
		});
		this.switchButton.visible = false;
		this.addRenderableWidget(this.switchButton);
	}

	public int getSwitchButtonX()
	{
		return SWITCH_X_OFFSET;
	}

	public int getSwitchButtonY()
	{
		return SWITCH_Y_OFFSET;
	}

	@Override
	public void render(PoseStack pose, int mouseX, int mouseY, float partialTicks)
	{
		this.renderBackground(pose);

		super.render(pose, mouseX, mouseY, partialTicks);

		if (this.lastError != null)
		{
			var x = this.doneButton.x + (this.doneButton.getWidth() - this.minecraft.font.width(this.lastError)) / 2;
			var y = this.doneButton.y + this.doneButton.getHeight() + 2;
			this.minecraft.font.drawShadow(pose, this.lastError, x, y, 0xFFFF0000);
		}

		this.switchButton.visible = !ModuleManager.POLYMORPH.isLoaded() && this.menu.getRecipes().size() >= 2;

		this.renderTooltip(pose, mouseX, mouseY);
	}

	@Override
	protected void renderBg(PoseStack pose, float partialTicks, int mouseX, int mouseY)
	{
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, this.getTexture());

		this.blit(pose, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
	}

	public abstract ResourceLocation getTexture();

	@Override
	protected void containerTick()
	{
		super.containerTick();

		var error = this.getError();
		this.doneButton.active = error == null;
		this.lastError = error;
	}

	protected Component getError()
	{
		var recipe = this.menu.getRecipe();

		if (recipe == null)
		{
			return TeachRecipeMenu.TEXT_RECIPE_NOT_FOUND;
		}

		return this.menu.getCurrentError();
	}

	private void onDoneButtonPress(Button button)
	{
		if (this.getError() != null)
		{
			return;
		}

		var recipe = this.menu.getRecipe();

		if (recipe != null)
		{
			var input = ItemHandlerHelper2.unwrap(new InvWrapper(this.menu.getInputContainer()), true).stream().map(ItemStorage::new).toList();
			this.onDone(recipe, input);
		}

	}

	protected abstract void onDone(RECIPE recipe, List<ItemStorage> input);
}
