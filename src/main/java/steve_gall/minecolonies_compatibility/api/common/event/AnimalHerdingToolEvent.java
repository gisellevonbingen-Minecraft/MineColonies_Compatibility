package steve_gall.minecolonies_compatibility.api.common.event;

import java.util.function.Consumer;

import com.minecolonies.api.equipment.registry.EquipmentTypeEntry;

import net.minecraft.world.entity.animal.Animal;
import net.neoforged.bus.api.Event;
import net.neoforged.neoforge.common.NeoForge;

/**
 * {@link NeoForge#EVENT_BUS}
 */
public class AnimalHerdingToolEvent extends Event
{
	private final Animal animal;
	private final Consumer<EquipmentTypeEntry> register;

	public AnimalHerdingToolEvent(Animal recipe, Consumer<EquipmentTypeEntry> register)
	{
		this.animal = recipe;
		this.register = register;
	}

	public Animal getAnimal()
	{
		return this.animal;
	}

	public void register(EquipmentTypeEntry toolType)
	{
		this.register.accept(toolType);
	}

}
