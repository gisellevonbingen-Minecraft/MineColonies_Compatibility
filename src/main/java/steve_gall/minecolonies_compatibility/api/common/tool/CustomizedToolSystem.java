package steve_gall.minecolonies_compatibility.api.common.tool;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.minecolonies.api.equipment.registry.EquipmentTypeEntry;
import com.minecolonies.api.util.constant.BuildingConstants;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public abstract class CustomizedToolSystem
{
	private static final Map<ResourceLocation, CustomizedToolSystem> REGISTRY = new HashMap<>();
	private static final Set<EquipmentTypeEntry> DURABILITY_BASED_LEVEL_TOOL_TYPES = new HashSet<>();

	public static void register(@NotNull CustomizedToolSystem toolSystem)
	{
		REGISTRY.put(toolSystem.getId(), toolSystem);
	}

	public static Map<ResourceLocation, CustomizedToolSystem> getRegistry()
	{
		return Collections.unmodifiableMap(REGISTRY);
	}

	@Nullable
	public static CustomizedToolSystem select(@NotNull ItemStack stack)
	{
		return REGISTRY.values().stream().filter(it -> it.isTool(stack)).findFirst().orElse(null);
	}

	public static void registerDurabilityBasedLevel(@NotNull EquipmentTypeEntry toolType)
	{
		DURABILITY_BASED_LEVEL_TOOL_TYPES.add(toolType);
	}

	public static boolean isDurabilityBasedLevel(@NotNull EquipmentTypeEntry toolType)
	{
		return DURABILITY_BASED_LEVEL_TOOL_TYPES.contains(toolType);
	}

	public int getLevel(@NotNull ItemStack stack)
	{
		var unclamped = this.getLevelUnclamped(stack);
		return Math.min(unclamped, BuildingConstants.CONST_DEFAULT_MAX_BUILDING_LEVEL);
	}

	@NotNull
	public abstract ResourceLocation getId();

	public abstract boolean isTool(@NotNull ItemStack stack);

	public abstract boolean isSword(@NotNull ItemStack stack);

	public abstract boolean isSpecialTool(@NotNull ItemStack stack, @NotNull EquipmentTypeEntry toolType);

	public abstract boolean isBroken(@NotNull ItemStack stack);

	public abstract int getLevelUnclamped(@NotNull ItemStack stack);

	public abstract double getAttackDamage(@NotNull ItemStack stack);
}
