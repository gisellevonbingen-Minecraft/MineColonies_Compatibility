package steve_gall.minecolonies_compatibility.core.client.gui;

import java.util.List;

import com.minecolonies.api.crafting.ItemStorage;
import com.minecolonies.api.util.constant.Constants;
import com.minecolonies.api.util.constant.TranslationConstants;
import com.minecolonies.api.util.constant.translation.BaseGameTranslationConstants;
import com.minecolonies.core.Network;
import com.minecolonies.core.colony.buildings.moduleviews.CraftingModuleView;
import com.minecolonies.core.network.messages.server.colony.building.worker.AddRemoveRecipeMessage;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.items.wrapper.InvWrapper;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.core.common.inventory.TeachRecipeMenu;
import steve_gall.minecolonies_compatibility.core.common.item.ItemHandlerHelper2;
import steve_gall.minecolonies_compatibility.core.common.network.message.TeachRecipeMenuSwitchingMessage;
import steve_gall.minecolonies_compatibility.module.common.ModuleManager;
import steve_gall.minecolonies_tweaks.api.common.crafting.ICustomizedRecipeStorage;

public abstract class TeachRecipeScreen<MENU extends TeachRecipeMenu<RECIPE>, RECIPE> extends AbstractContainerScreen<MENU>
{
	private static final Component TEXT_WARNING_MAXIMUM_NUMBER_RECIPES = Component.translatable(TranslationConstants.WARNING_MAXIMUM_NUMBER_RECIPES);
	private static final Component TEXT_DONE = Component.translatable(BaseGameTranslationConstants.BASE_GUI_DONE);

	private static final ResourceLocation SWITCH_TEXTURE = new ResourceLocation(Constants.MOD_ID, "textures/gui/craftingswitch.png");
	private static final int SWITCH_WIDTH = 20;
	private static final int SWITCH_HEIGHT = 18;
	private static final int SWITCH_X_OFFSET = 148;
	private static final int SWITCH_Y_OFFSET = 43 - (SWITCH_HEIGHT / 2);

	protected final CraftingModuleView module;

	private Button doneButton;
	private ImageButton switchButton;

	private Component lastError;

	public TeachRecipeScreen(MENU menu, Inventory inventory, Component title)
	{
		super(menu, inventory, title);

		this.module = (CraftingModuleView) menu.getModulePos().getModuleView();
	}

	@Override
	protected void init()
	{
		super.init();

		this.doneButton = Button.builder(TEXT_DONE, this::onDoneButtonPress).bounds(this.leftPos + 1, this.topPos + this.imageHeight + 4, 150, 20).build();
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
	public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks)
	{
		this.renderBackground(graphics);

		super.render(graphics, mouseX, mouseY, partialTicks);

		if (this.lastError != null)
		{
			var x = this.doneButton.getX() + (this.doneButton.getWidth() - this.minecraft.font.width(this.lastError)) / 2;
			var y = this.doneButton.getY() + this.doneButton.getHeight() + 2;
			graphics.drawString(this.minecraft.font, this.lastError, x, y, 0xFFFF0000, true);
		}

		this.switchButton.visible = !ModuleManager.POLYMORPH.isLoaded() && this.menu.getRecipes().size() >= 2;
	}

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
		if (!this.module.canLearn(this.menu.getCraftingType()))
		{
			return TEXT_WARNING_MAXIMUM_NUMBER_RECIPES;
		}

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
			var input = ItemHandlerHelper2.unwrap(new InvWrapper(this.menu.getInputContainer()), false).stream().map(ItemStorage::new).toList();
			var storage = this.createRecipeStorage(recipe, input).wrap();
			Network.getNetwork().sendToServer(new AddRemoveRecipeMessage(this.module.getBuildingView(), false, storage, this.module.getProducer().getRuntimeID()));
		}

	}

	protected abstract ICustomizedRecipeStorage createRecipeStorage(RECIPE recipe, List<ItemStorage> input);

	public CraftingModuleView getModule()
	{
		return this.module;
	}

}
