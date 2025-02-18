package steve_gall.minecolonies_compatibility.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import steve_gall.minecolonies_compatibility.core.client.gui.TeachRecipeScreen;
import steve_gall.minecolonies_tweaks.core.client.gui.CloseableContainerScreenExtension;

@Mixin(value = TeachRecipeScreen.class, remap = false)
public abstract class TeachRecipeScreenMixin implements CloseableContainerScreenExtension
{
	@Shadow(remap = false)
	private Button doneButton;

	@Unique
	private Screen minecolonies_tweaks$parent;

	@Override
	public void minecolonies_tweaks$onInit(int leftPos, int topPos, int imageWidth, int imageHeight, addCloseButton addCloseButton)
	{
		addCloseButton.invoke(this.doneButton.x + this.doneButton.getWidth() + 5, this.doneButton.y, this.doneButton.getHeight(), this.doneButton.getHeight());
	}

	@Override
	public Screen minecolonies_tweaks$getParent()
	{
		return this.minecolonies_tweaks$parent;
	}

	@Override
	public void minecolonies_tweaks$setParent(Screen screen)
	{
		this.minecolonies_tweaks$parent = screen;
	}

}
