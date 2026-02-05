package steve_gall.minecolonies_compatibility.api.client.jei;

import java.util.ArrayList;
import java.util.List;

import mezz.jei.api.gui.handlers.IGhostIngredientHandler;
import mezz.jei.api.ingredients.ITypedIngredient;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;
import steve_gall.minecolonies_compatibility.api.client.IFluidGhostScreen;
import steve_gall.minecolonies_compatibility.api.client.IItemGhostScreen;
import steve_gall.minecolonies_compatibility.api.common.inventory.IItemGhostSlot;

public class GhostIngredientHandler<SCREEN extends AbstractContainerScreen<?>> implements IGhostIngredientHandler<SCREEN>
{
	@SuppressWarnings("unchecked")
	@Override
	public <I> List<Target<I>> getTargetsTyped(SCREEN screen, ITypedIngredient<I> ingredient, boolean doStart)
	{
		var targets = new ArrayList<Target<I>>();

		if (ingredient.getIngredient() instanceof ItemStack item)
		{
			var slots = screen.getMenu().slots;

			for (var i = 0; i < slots.size(); i++)
			{
				var slot = slots.get(i);

				if (slot instanceof IItemGhostSlot ghostSlot && ghostSlot.canAccept(item))
				{
					targets.add((Target<I>) this.createTarget(screen, new ItemGhostSlotTarget(slot, i)));
				}

			}

			if (screen instanceof IItemGhostScreen ghostScreen)
			{
				for (var target : ghostScreen.getItemGhostSlots())
				{
					targets.add((Target<I>) this.createTarget(screen, target));
				}

			}

		}
		else if (ingredient.getIngredient() instanceof FluidStack)
		{
			if (screen instanceof IFluidGhostScreen ghostScreen)
			{
				for (var target : ghostScreen.getFluidGhostSlots())
				{
					targets.add((Target<I>) this.createTarget(screen, target));
				}

			}

		}

		return targets;
	}

	private <I> Target<I> createTarget(SCREEN screen, GhostTarget<I> target)
	{
		return new Target<>()
		{
			@Override
			public Rect2i getArea()
			{
				var area = target.getArea();
				return new Rect2i(screen.getGuiLeft() + area.getX(), screen.getGuiTop() + area.getY(), area.getWidth(), area.getHeight());
			}

			@Override
			public void accept(I ingredient)
			{
				target.accept(ingredient);
			}
		};
	}

	@Override
	public void onComplete()
	{

	}

}
