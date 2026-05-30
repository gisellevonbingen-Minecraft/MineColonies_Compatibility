package steve_gall.minecolonies_compatibility.api.common.entity.ai;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.minecolonies.api.colony.jobs.registry.JobEntry;
import com.minecolonies.api.entity.citizen.AbstractEntityCitizen;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.core.common.colony.CitizenHelper;
import steve_gall.minecolonies_compatibility.core.common.util.PersistentDataHelper;

public abstract class CustomizedAI
{
	private static final List<CustomizedAI> REGISTRY = new ArrayList<>();

	public static void register(@NotNull CustomizedAI ai)
	{
		REGISTRY.add(ai);
	}

	public static List<CustomizedAI> getValues()
	{
		return Collections.unmodifiableList(REGISTRY);
	}

	@Nullable
	public static CustomizedAI select(@NotNull CustomizedAIContext context)
	{
		return REGISTRY.stream().filter(it -> it.test(context)).findFirst().orElse(null);
	}

	public static final String PERSISTENT_TAG_KEY = MineColoniesCompatibility.rl("customized_citizen_ai").toString();

	@NotNull
	public abstract JobEntry getJobEntry();

	public boolean test(@NotNull CustomizedAIContext context)
	{
		var jobEntry = CitizenHelper.getJobEntry(context.getUser().getCitizenData());
		return this.getJobEntry() == jobEntry && this.testTool(context.getTool());
	}

	public boolean testTool(@NotNull ItemStack tool)
	{
		return false;
	}

	public void onDeselected(@NotNull AbstractEntityCitizen user)
	{

	}

	public void onSelected(@NotNull AbstractEntityCitizen user)
	{

	}

	public void tick(@NotNull AbstractEntityCitizen user)
	{

	}

	public boolean canDump(@NotNull AbstractEntityCitizen user, int slot, @NotNull ItemStack stackToDup)
	{
		return true;
	}

	public int getMainHandSlot(@NotNull AbstractEntityCitizen user)
	{
		return user.getInventoryCitizen().getHeldItemSlot(InteractionHand.MAIN_HAND);
	}

	@NotNull
	public ItemStack getMainHandItem(@NotNull AbstractEntityCitizen user)
	{
		return user.getInventoryCitizen().getHeldItem(InteractionHand.MAIN_HAND);
	}

	/**
	 *
	 * @return Key for store custom data at citizen persistent data
	 */
	@NotNull
	public abstract String getTagKey();

	@NotNull
	protected CompoundTag getOrEmptyTag(@NotNull AbstractEntityCitizen user)
	{
		var tag = PersistentDataHelper.getOrEmpty(user, PERSISTENT_TAG_KEY);
		return PersistentDataHelper.getOrEmpty(tag, this.getTagKey());
	}

	@NotNull
	protected CompoundTag getOrCreateTag(@NotNull AbstractEntityCitizen user)
	{
		var tag = PersistentDataHelper.getOrCreate(user, PERSISTENT_TAG_KEY);
		return PersistentDataHelper.getOrCreate(tag, this.getTagKey());
	}

}
