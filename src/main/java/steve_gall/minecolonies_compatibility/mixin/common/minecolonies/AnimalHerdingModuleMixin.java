package steve_gall.minecolonies_compatibility.mixin.common.minecolonies;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.minecolonies.api.colony.buildings.modules.AbstractBuildingModule;
import com.minecolonies.api.crafting.IGenericRecipe;
import com.minecolonies.api.equipment.registry.EquipmentTypeEntry;
import com.minecolonies.core.colony.buildings.modules.AnimalHerdingModule;

import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import steve_gall.minecolonies_compatibility.api.common.event.AnimalHerdingToolEvent;
import steve_gall.minecolonies_compatibility.core.common.crafting.AnimalHerdingLootGenericRecipe;

@Mixin(value = AnimalHerdingModule.class, remap = false)
public abstract class AnimalHerdingModuleMixin extends AbstractBuildingModule
{
	@Shadow(remap = false)
	abstract @NotNull List<ItemStack> getBreedingItems();

	@Inject(method = "getRecipesForDisplayPurposesOnly", remap = false, at = @At(value = "TAIL"), cancellable = true)
	private void getRecipesForDisplayPurposesOnly(@NotNull Animal animal, CallbackInfoReturnable<List<IGenericRecipe>> cir)
	{
		var toolTypes = new HashSet<EquipmentTypeEntry>();
		var event = new AnimalHerdingToolEvent(animal, toolTypes::add);
		MinecraftForge.EVENT_BUS.post(event);

		if (toolTypes.size() > 0)
		{
			var breedingItems = Collections.singletonList(this.getBreedingItems());
			var newList = new ArrayList<>(cir.getReturnValue());

			for (var toolType : toolTypes)
			{
				newList.add(new AnimalHerdingLootGenericRecipe(animal.getType(), breedingItems, animal.getLootTable(), toolType));
			}

			cir.setReturnValue(newList);
		}

	}

}
