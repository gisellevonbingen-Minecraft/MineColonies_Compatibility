package steve_gall.minecolonies_compatibility.api.common.event;

import java.util.function.Consumer;

import com.minecolonies.core.colony.crafting.LootTableAnalyzer;

import net.neoforged.bus.api.Event;
import net.neoforged.neoforge.common.NeoForge;
import steve_gall.minecolonies_compatibility.core.common.crafting.AnimalHerdingLootGenericRecipe;

/**
 * {@link NeoForge#EVENT_BUS}
 */
public class AnimalHerdingLootEvent extends Event
{
	private final AnimalHerdingLootGenericRecipe recipe;
	private final Consumer<LootTableAnalyzer.LootDrop> register;

	public AnimalHerdingLootEvent(AnimalHerdingLootGenericRecipe recipe, Consumer<LootTableAnalyzer.LootDrop> register)
	{
		this.recipe = recipe;
		this.register = register;
	}

	public AnimalHerdingLootGenericRecipe getRecipe()
	{
		return this.recipe;
	}

	public void register(LootTableAnalyzer.LootDrop drop)
	{
		this.register.accept(drop);
	}

}
