package steve_gall.minecolonies_compatibility.api.common.plant;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.minecolonies.api.util.constant.IToolType;
import com.minecolonies.api.util.constant.ToolType;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;

public abstract class CustomizedFruit
{
	private static final Map<ResourceLocation, CustomizedFruit> REGISTRY = new HashMap<>();
	private static final Map<ResourceLocation, CustomizedFruit> VOLATILE = new HashMap<>();

	public static void register(@NotNull CustomizedFruit fruit)
	{
		REGISTRY.put(fruit.getId(), fruit);
	}

	public static void registerVolatile(@NotNull CustomizedFruit fruit)
	{
		register(fruit);
		VOLATILE.put(fruit.getId(), fruit);
	}

	public static void reload(@NotNull RecipeManager recipeManager)
	{
		VOLATILE.keySet().forEach(REGISTRY::remove);
		VOLATILE.clear();
	}

	public static Map<ResourceLocation, CustomizedFruit> getRegistry()
	{
		return Collections.unmodifiableMap(REGISTRY);
	}

	@Nullable
	public static CustomizedFruit select(@NotNull PlantBlockContext context)
	{
		return REGISTRY.values().stream().filter(it -> it.test(context)).findFirst().orElse(null);
	}

	@Override
	public int hashCode()
	{
		return this.getId().hashCode();
	}

	@Override
	public boolean equals(Object obj)
	{
		return this == obj;
	}

	@NotNull
	public abstract ResourceLocation getId();

	@NotNull
	public abstract List<ItemStack> getBlockIcons();

	@NotNull
	public abstract List<ItemStack> getItemIcons();

	public abstract boolean test(@NotNull PlantBlockContext context);

	public abstract boolean canHarvest(@NotNull PlantBlockContext context);

	public abstract boolean isMaxHarvest(@NotNull PlantBlockContext context);

	@NotNull
	public abstract List<ItemStack> harvest(@NotNull PlantBlockContext context, @NotNull HarvesterContext harvester);

	@NotNull
	public IToolType getHarvestToolType()
	{
		return ToolType.SHEARS;
	}

	@NotNull
	public IToolType getHarvestToolType(@NotNull PlantBlockContext context)
	{
		return this.getHarvestToolType();
	}

	@NotNull
	public SoundEvent getHarvestSound(@NotNull PlantBlockContext context)
	{
		return SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES;
	}

}
