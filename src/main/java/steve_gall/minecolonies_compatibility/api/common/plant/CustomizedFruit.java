package steve_gall.minecolonies_compatibility.api.common.plant;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.world.item.ItemStack;

public abstract class CustomizedFruit
{
	private static final List<CustomizedFruit> REGISTRY = new ArrayList<>();

	public static void register(@NotNull CustomizedFruit fruit)
	{
		REGISTRY.add(fruit);
	}

	@Nullable
	public static CustomizedFruit select(@NotNull PlantBlockContext context)
	{
		return REGISTRY.stream().filter(it -> it.test(context)).findFirst().orElse(null);
	}

	public abstract boolean test(@NotNull PlantBlockContext context);

	public abstract boolean canHarvest(@NotNull PlantBlockContext context);

	@NotNull
	public abstract List<ItemStack> harvest(@NotNull PlantBlockContext context, @NotNull HarvesterContext harvester);
}
