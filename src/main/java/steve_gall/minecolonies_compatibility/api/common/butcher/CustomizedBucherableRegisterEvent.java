package steve_gall.minecolonies_compatibility.api.common.butcher;

import java.util.function.Consumer;

import org.jetbrains.annotations.NotNull;

import net.minecraft.world.item.crafting.RecipeManager;
import net.neoforged.bus.api.Event;
import net.neoforged.neoforge.common.NeoForge;

/**
 * {@link NeoForge#EVENT_BUS}
 */
public class CustomizedBucherableRegisterEvent extends Event
{
	@NotNull
	private final Consumer<CustomizedButcherable> register;
	@NotNull
	private final RecipeManager recipeManager;

	public CustomizedBucherableRegisterEvent(@NotNull Consumer<CustomizedButcherable> register, @NotNull RecipeManager recipeManager)
	{
		this.register = register;
		this.recipeManager = recipeManager;
	}

	public void register(@NotNull CustomizedButcherable butcherable)
	{
		this.register.accept(butcherable);
	}

	public @NotNull RecipeManager getRecipeManager()
	{
		return this.recipeManager;
	}

}
