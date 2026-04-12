package steve_gall.minecolonies_compatibility.api.common.event;

import java.util.function.Consumer;

import org.jetbrains.annotations.NotNull;

import com.minecolonies.core.colony.crafting.LootTableAnalyzer;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Event;
import steve_gall.minecolonies_compatibility.core.common.crafting.AnimalHerdingLootGenericRecipe;

/**
 * {@link MinecraftForge#EVENT_BUS}
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
