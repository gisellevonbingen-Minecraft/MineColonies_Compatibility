package steve_gall.minecolonies_compatibility.core.common.util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;

public class NBTUtils2
{
	public static <T> List<T> deserializeList(@NotNull CompoundTag tag, @NotNull String key, Function<CompoundTag, T> func)
	{
		var listTag = tag.getList(key, Tag.TAG_COMPOUND);
		var list = new ArrayList<T>();

		for (var i = 0; i < listTag.size(); i++)
		{
			list.add(func.apply(listTag.getCompound(i)));
		}

		return list;
	}

	public static <T> ListTag serializeList(@NotNull CompoundTag tag, @NotNull String key, @NotNull List<T> list, @NotNull Function<T, CompoundTag> func)
	{
		var listTag = new ListTag();
		tag.put(key, listTag);

		for (T element : list)
		{
			listTag.add(func.apply(element));
		}

		return listTag;
	}

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

}
