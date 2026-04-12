package steve_gall.minecolonies_compatibility.api.common.event;

import java.util.function.Consumer;

import org.jetbrains.annotations.NotNull;

import com.minecolonies.core.colony.crafting.LootTableAnalyzer;

import net.neoforged.bus.api.Event;
import net.neoforged.neoforge.common.NeoForge;
import steve_gall.minecolonies_compatibility.core.common.crafting.AnimalHerdingLootGenericRecipe;

/**
 * {@link NeoForge#EVENT_BUS}
 */
public class AnimalHerdingLootEvent extends Event
{
	@NotNull
	private final AnimalHerdingLootGenericRecipe recipe;
	@NotNull
	private final Consumer<LootTableAnalyzer.LootDrop> register;

	public AnimalHerdingLootEvent(@NotNull AnimalHerdingLootGenericRecipe recipe, @NotNull Consumer<LootTableAnalyzer.LootDrop> register)
	{
		this.recipe = recipe;
		this.register = register;
	}

	@NotNull
	public AnimalHerdingLootGenericRecipe getRecipe()
	{
		return this.recipe;
	}

	public void register(@NotNull LootTableAnalyzer.LootDrop drop)
	{
		this.register.accept(drop);
	}

}
