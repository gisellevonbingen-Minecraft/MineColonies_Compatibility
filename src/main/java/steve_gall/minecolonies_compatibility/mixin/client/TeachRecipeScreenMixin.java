package steve_gall.minecolonies_compatibility.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import steve_gall.minecolonies_compatibility.core.client.gui.TeachRecipeScreen;
import steve_gall.minecolonies_compatibility.mixin.client.minecraft.AbstractContainerScreenMixin;
import steve_gall.minecolonies_tweaks.core.client.gui.CloseableWindowExtension;

@Mixin(value = TeachRecipeScreen.class, remap = false)
public abstract class TeachRecipeScreenMixin extends AbstractContainerScreenMixin implements CloseableWindowExtension
{
	@Shadow(remap = false)
	private Button doneButton;

	@Unique
	private Screen minecolonies_tweaks$parent;

	protected TeachRecipeScreenMixin(Component p_96550_)
	{
		super(p_96550_);
	}

	@Inject(method = "init", remap = true, at = @At(value = "TAIL"))
	protected void init(CallbackInfo ci)
	{
		this.addCloseButton(this.doneButton.getX() + this.doneButton.getWidth() + 5, this.doneButton.getY(), this.doneButton.getHeight(), this.doneButton.getHeight());
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
