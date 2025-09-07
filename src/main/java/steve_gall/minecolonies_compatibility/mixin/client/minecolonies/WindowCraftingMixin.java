package steve_gall.minecolonies_compatibility.mixin.client.minecolonies;

import java.util.LinkedList;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.minecolonies.api.crafting.ItemStorage;
import com.minecolonies.api.inventory.container.ContainerCrafting;
import com.minecolonies.api.util.ItemStackUtils;
import com.minecolonies.core.client.gui.containers.WindowCrafting;
import com.minecolonies.core.colony.buildings.moduleviews.CraftingModuleView;
import com.minecolonies.core.colony.buildings.views.AbstractBuildingView;
import com.minecolonies.core.network.messages.server.colony.building.worker.AddRemoveRecipeMessage;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import steve_gall.minecolonies_compatibility.core.common.building.module.SmithingTemplateCraftingModuleView;
import steve_gall.minecolonies_compatibility.core.common.crafting.SmithingTemplateRecipeStorage;

@Mixin(value = WindowCrafting.class, remap = false)
public abstract class WindowCraftingMixin extends AbstractContainerScreen<ContainerCrafting>
{
	@Shadow(remap = false)
	private AbstractBuildingView building;
	@Shadow(remap = false)
	private CraftingModuleView module;
	@Shadow(remap = false)
	private boolean completeCrafting;

	public WindowCraftingMixin(ContainerCrafting p_97741_, Inventory p_97742_, Component p_97743_)
	{
		super(p_97741_, p_97742_, p_97743_);
	}

	@Inject(method = "onDoneClicked", remap = false, at = @At(value = "HEAD"), cancellable = true)
	private void onDoneClicked(Button button, CallbackInfo ci)
	{
		if (this.module instanceof SmithingTemplateCraftingModuleView)
		{
			ci.cancel();

			var primaryOutput = this.menu.craftResult.getItem(0);

			if (primaryOutput.isEmpty())
			{
				return;
			}

			var outputItem = primaryOutput.getItem();
			var inputCount = 0;
			var input = new LinkedList<ItemStorage>();
			var matrix = this.menu.craftMatrix;

			for (var i = 0; i < matrix.getContainerSize(); i++)
			{
				var stack = matrix.getItem(i);

				if (!stack.isEmpty())
				{
					if (outputItem == stack.getItem())
					{
						inputCount++;
						continue;
					}

					input.add(new ItemStorage(stack.copyWithCount(1)));
				}

			}

			primaryOutput = primaryOutput.copyWithCount(primaryOutput.getCount() - inputCount);

			if (primaryOutput.isEmpty())
			{
				return;
			}

			var secondaryOutputs = this.menu.getRemainingItems();

			if (!ItemStackUtils.isEmpty(primaryOutput))
			{
				var storage = new SmithingTemplateRecipeStorage(this.completeCrafting ? 3 : 2, input, inputCount, primaryOutput, secondaryOutputs);
				new AddRemoveRecipeMessage(this.building, false, storage.wrap(), this.module.getProducer().getRuntimeID()).sendToServer();
			}

		}

	}

}
