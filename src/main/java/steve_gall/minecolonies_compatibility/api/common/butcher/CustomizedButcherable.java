package steve_gall.minecolonies_compatibility.api.common.butcher;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.minecolonies.api.entity.citizen.AbstractEntityCitizen;
import com.minecolonies.api.util.constant.ToolType;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import steve_gall.minecolonies_compatibility.core.common.init.ModToolTypes;
import steve_gall.minecolonies_compatibility.core.common.util.InteractionMessageHelper;

public abstract class CustomizedButcherable
{
	private static final Map<ResourceLocation, CustomizedButcherable> REGISTRY = new HashMap<>();

	public static void register(@NotNull CustomizedButcherable fruit)
	{
		REGISTRY.put(fruit.getId(), fruit);
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
	public static CustomizedButcherable selectByButcheringBlock(@NotNull LevelReader level, @NotNull BlockPos position, @NotNull BlockState state)
	{
		return REGISTRY.values().stream().filter(it -> it.isButcheringBlock(level, position, state)).findFirst().orElse(null);
	}

	@Nullable
	public static CustomizedButcherable selectByTableBlock(@NotNull LevelReader level, @NotNull BlockPos position, @NotNull BlockState state)
	{
		return REGISTRY.values().stream().filter(it -> it.isTableBlock(level, position, state)).findFirst().orElse(null);
	}

	public static boolean isButcherable(@NotNull ItemStack stack)
	{
		return selectByItem(stack) != null;
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
	public abstract List<ItemStack> getOutputIcons();

	@NotNull
	public abstract List<BlockState> getTableIcons();

	@NotNull
	public ToolType getBlockToolType()
	{
		return ModToolTypes.BUTCHER_TOOL.getToolType();
	}

	@NotNull
	public ToolType getTableToolType()
	{
		return ModToolTypes.BUTCHER_TOOL.getToolType();
	}

	public boolean testItem(@NotNull ItemStack item)
	{
		return false;
	}

	public boolean isButcheringBlock(@NotNull LevelReader level, @NotNull BlockPos position, @NotNull BlockState state)
	{
		return false;
	}

	public boolean isTableBlock(@NotNull LevelReader level, @NotNull BlockPos position, @NotNull BlockState state)
	{
		return false;
	}

	public void doButcherBlock(@NotNull Level level, @NotNull BlockPos position, @NotNull BlockState state, @NotNull AbstractEntityCitizen worker)
	{

	}

	public void doButcherTable(@NotNull Level level, @NotNull BlockPos position, @NotNull BlockState state, @NotNull AbstractEntityCitizen worker, @NotNull InteractionHand hand)
	{

	}

	@NotNull
	public SoundEvent getBlockSound(Level level, BlockPos position, BlockState state)
	{
		return state.getSoundType(level, position, null).getHitSound();
	}

	@NotNull
	public SoundEvent getTableSound(Level level, BlockPos position, BlockState state)
	{
		return state.getSoundType(level, position, null).getHitSound();
	}

	public Component getTableNotFoundMessage()
	{
		return InteractionMessageHelper.getWorkingBlockNotFound();
	}

}
