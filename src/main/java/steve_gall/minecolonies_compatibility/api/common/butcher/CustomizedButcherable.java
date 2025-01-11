package steve_gall.minecolonies_compatibility.api.common.butcher;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.minecolonies.api.entity.citizen.AbstractEntityCitizen;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.state.BlockState;
import steve_gall.minecolonies_compatibility.api.common.crafting.ToolOrIngredientStack;
import steve_gall.minecolonies_compatibility.core.common.init.ModToolTypes;
import steve_gall.minecolonies_compatibility.core.common.util.InteractionMessageHelper;

public abstract class CustomizedButcherable
{
	private static final Map<ResourceLocation, CustomizedButcherable> REGISTRY = new HashMap<>();

	public static void register(@NotNull CustomizedButcherable butcherable)
	{
		REGISTRY.put(butcherable.getId(), butcherable);
	}

	public static Map<ResourceLocation, CustomizedButcherable> getRegistry()
	{
		return Collections.unmodifiableMap(REGISTRY);
	}

	@Nullable
	public static CustomizedButcherable selectByItem(@NotNull ItemStack item)
	{
		return REGISTRY.values().stream().filter(it -> it.testItem(item)).findFirst().orElse(null);
	}

	@Nullable
	public static CustomizedButcherable selectByButcheringBlock(@NotNull ButcherBlockContext context)
	{
		return REGISTRY.values().stream().filter(it -> it.isButcheringBlock(context)).findFirst().orElse(null);
	}

	@Nullable
	public static CustomizedButcherable selectByTableBlock(@NotNull ButcherBlockContext context)
	{
		return REGISTRY.values().stream().filter(it -> it.isTableBlock(context)).findFirst().orElse(null);
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
	public abstract List<ItemStack> getItemIcons();

	@NotNull
	public abstract List<Ingredient> getOutputIcons();

	@NotNull
	public abstract List<BlockState> getTableIcons();

	@NotNull
	public List<ToolOrIngredientStack> getToolsForIcon()
	{
		return Collections.singletonList(ToolOrIngredientStack.of(ModToolTypes.BUTCHER_TOOL));
	}

	@NotNull
	public ToolOrIngredientStack getBlockTool(@NotNull ButcherBlockContext context)
	{
		return ToolOrIngredientStack.of(ModToolTypes.BUTCHER_TOOL);
	}

	@NotNull
	public ToolOrIngredientStack getTableTool(@NotNull ButcherBlockContext context)
	{
		return ToolOrIngredientStack.of(ModToolTypes.BUTCHER_TOOL);
	}

	public boolean testItem(@NotNull ItemStack item)
	{
		return false;
	}

	public boolean isButcheringBlock(@NotNull ButcherBlockContext context)
	{
		return false;
	}

	public boolean isTableBlock(@NotNull ButcherBlockContext context)
	{
		return false;
	}

	public void doButcherBlock(@NotNull ButcherBlockContext context, @NotNull AbstractEntityCitizen worker)
	{

	}

	public void doButcherTable(@NotNull ButcherBlockContext context, @NotNull AbstractEntityCitizen worker, @NotNull InteractionHand hand)
	{

	}

	@NotNull
	public SoundEvent getBlockSound(@NotNull ButcherBlockContext context)
	{
		return context.getState().getSoundType(context.getLevel(), context.getPosition(), null).getHitSound();
	}

	@NotNull
	public SoundEvent getTableSound(@NotNull ButcherBlockContext context)
	{
		return context.getState().getSoundType(context.getLevel(), context.getPosition(), null).getHitSound();
	}

	@NotNull
	public Component getTableNotFoundMessage()
	{
		return InteractionMessageHelper.getWorkingBlockNotFound();
	}

}
