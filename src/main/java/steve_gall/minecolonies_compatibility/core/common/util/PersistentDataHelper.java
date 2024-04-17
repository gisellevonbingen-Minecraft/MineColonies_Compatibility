package steve_gall.minecolonies_compatibility.core.common.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;

public class PersistentDataHelper
{
	@NotNull
	public static CompoundTag getOrCreate(@Nullable CompoundTag persistentData, @NotNull String key)
	{
		return NBTUtils2.getOrCreate(persistentData, key);
	}

	@NotNull
	public static CompoundTag getOrEmpty(@Nullable CompoundTag persistentData, @NotNull String key)
	{
		return NBTUtils2.getOrEmpty(persistentData, key);
	}

	@NotNull
	public static CompoundTag getOrCreate(@NotNull Entity entity, @NotNull String key)
	{
		return NBTUtils2.getOrCreate(entity.getPersistentData(), key);
	}

	@NotNull
	public static CompoundTag getOrEmpty(@NotNull Entity entity, @NotNull String key)
	{
		return NBTUtils2.getOrEmpty(entity.getPersistentData(), key);
	}

	@NotNull
	public static CompoundTag getOrCreate(@NotNull ItemStack stack, @NotNull String key)
	{
		return NBTUtils2.getOrCreate(stack.getOrCreateTag(), key);
	}

	@NotNull
	public static CompoundTag getOrEmpty(@NotNull ItemStack stack, @NotNull String key)
	{
		var tag = stack.getTag();

		if (tag == null)
		{
			return new CompoundTag();
		}
		else
		{
			return NBTUtils2.getOrEmpty(tag, key);
		}

	}

	private PersistentDataHelper()
	{

	}

}
