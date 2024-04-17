package steve_gall.minecolonies_compatibility.api.common.entity;

import org.jetbrains.annotations.NotNull;

import com.minecolonies.api.entity.citizen.AbstractEntityCitizen;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.core.common.util.PersistentDataHelper;

public abstract class CustomizedCitizenAI
{
	public static final String PERSISTENT_TAG_KEY = MineColoniesCompatibility.rl("customized_citizen_ai").toString();

	public void atBuildingActions(@NotNull CustomizedAIContext context)
	{

	}

	public boolean canDump(@NotNull CustomizedAIContext context, int slot, @NotNull ItemStack stackToDup)
	{
		return true;
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
