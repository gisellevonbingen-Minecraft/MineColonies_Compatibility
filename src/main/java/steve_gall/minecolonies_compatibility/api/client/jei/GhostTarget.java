package steve_gall.minecolonies_compatibility.api.client.jei;

import org.jetbrains.annotations.NotNull;

import mezz.jei.api.gui.handlers.IGhostIngredientHandler.Target;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.world.inventory.Slot;

public abstract class GhostTarget<I> implements Target<I>
{
	@NotNull
	private AbstractContainerScreen<?> screen;
	@NotNull
	private final Slot slot;
	private final int slotNumber;

	public GhostTarget(@NotNull AbstractContainerScreen<?> screen, @NotNull Slot slot, int slotNumber)
	{
		this.screen = screen;
		this.slot = slot;
		this.slotNumber = slotNumber;
	}

	@NotNull
	public AbstractContainerScreen<?> getScreen()
	{
		return this.screen;
	}

	@NotNull
	public Slot getSlot()
	{
		return this.slot;
	}

	public int getSlotNumber()
	{
		return this.slotNumber;
	}

	@Override
	public Rect2i getArea()
	{
		var screen = this.getScreen();
		var slot = this.getSlot();
		return new Rect2i(screen.getGuiLeft() + slot.x, screen.getGuiTop() + slot.y, 16, 16);
	}

}
