package steve_gall.minecolonies_compatibility.core.common.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;

public class NBTUtils2
{
	public static CompoundTag getOrEmpty(@Nullable CompoundTag tag, @NotNull String key)
	{
		if (tag != null)
		{
			if (tag.contains(key, Tag.TAG_COMPOUND))
			{
				return tag.getCompound(key);
			}

		}

		return new CompoundTag();
	}

	public static CompoundTag getOrCreate(@Nullable CompoundTag tag, @NotNull String key)
	{
		if (tag != null)
		{
			if (tag.contains(key, Tag.TAG_COMPOUND))
			{
				return tag.getCompound(key);
			}

			var child = new CompoundTag();
			tag.put(key, child);
			return child;
		}

		return new CompoundTag();
	}

	private NBTUtils2()
	{

	}

	public static interface TagProvider
	{
		@NotNull
		CompoundTag get(@Nullable CompoundTag tag, @NotNull String key);
	}

}
