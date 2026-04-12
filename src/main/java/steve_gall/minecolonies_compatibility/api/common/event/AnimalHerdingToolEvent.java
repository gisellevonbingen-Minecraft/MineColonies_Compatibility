package steve_gall.minecolonies_compatibility.api.common.event;

import java.util.function.Consumer;

import org.jetbrains.annotations.NotNull;

import com.minecolonies.api.equipment.registry.EquipmentTypeEntry;

import net.minecraft.world.entity.animal.Animal;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Event;

/**
 * {@link MinecraftForge#EVENT_BUS}
 */
public class AnimalHerdingToolEvent extends Event
{
	@NotNull
	private final Animal animal;
	@NotNull
	private final Consumer<EquipmentTypeEntry> register;

	public AnimalHerdingToolEvent(@NotNull Animal recipe, @NotNull Consumer<EquipmentTypeEntry> register)
	{
		this.animal = recipe;
		this.register = register;
	}

	@NotNull
	public Animal getAnimal()
	{
		return this.animal;
	}

	public void register(@NotNull EquipmentTypeEntry toolType)
	{
		this.register.accept(toolType);
	}

}
