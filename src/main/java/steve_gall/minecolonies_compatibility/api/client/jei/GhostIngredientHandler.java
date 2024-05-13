package steve_gall.minecolonies_compatibility.api.client.jei;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import mezz.jei.api.gui.handlers.IGhostIngredientHandler;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.item.ItemStack;
import steve_gall.minecolonies_compatibility.api.common.inventory.IItemGhostSlot;

public class GhostIngredientHandler<SCREEN extends AbstractContainerScreen<?>> implements IGhostIngredientHandler<SCREEN>
{
	@SuppressWarnings("unchecked")
	@Override
	public <I> List<Target<I>> getTargets(SCREEN screen, I ingredient, boolean doStart)
	{
		if (ingredient instanceof ItemStack)
		{
			var targets = new ArrayList<Target<I>>();
			var slots = screen.getMenu().slots;

			for (var i = 0; i < slots.size(); i++)
			{
				var slot = slots.get(i);

				if (slot instanceof IItemGhostSlot)
				{
					targets.add((Target<I>) new ItemGhostTarget(screen, slot, i));
				}

			}

			return targets;
		}

		return Collections.emptyList();
	}

	@Override
	public void onComplete()
	{

	}

}
