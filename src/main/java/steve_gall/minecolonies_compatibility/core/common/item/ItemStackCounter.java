package steve_gall.minecolonies_compatibility.core.common.item;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.minecolonies.api.util.Utils;

import it.unimi.dsi.fastutil.objects.AbstractObject2LongMap.BasicEntry;
import it.unimi.dsi.fastutil.objects.Object2LongMap;
import it.unimi.dsi.fastutil.objects.Object2LongMap.Entry;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import steve_gall.minecolonies_compatibility.core.common.util.NBTUtils2;
import steve_gall.minecolonies_tweaks.core.common.item.ItemSerializationHelper;

public class ItemStackCounter
{
	private final Object2LongMap<ItemStackKey> counter = new Object2LongOpenHashMap<>();
	private final List<CountChangedListener> listeners = new ArrayList<>();

	public void deserializeTag(HolderLookup.Provider provider, CompoundTag tag)
	{
		this.replace(NBTUtils2.deserializeList(tag, "counter", t ->
		{
			var stack = ItemSerializationHelper.deserializeTag(provider, t.getCompound("item"));
			var count = t.getLong("count");
			return new BasicEntry<>(new ItemStackKey(stack), count);
		}));
	}

	public CompoundTag serializeTag(HolderLookup.Provider provider)
	{
		var tag = new CompoundTag();
		NBTUtils2.serializeCollection(tag, "counter", this.entrySet(), entry ->
		{
			var t = new CompoundTag();
			t.put("item", ItemSerializationHelper.serializeTag(provider, entry.getKey().getStack(1)));
			t.putLong("count", entry.getLongValue());
			return t;
		});

		return tag;
	}

	public void deserializeBuffer(RegistryFriendlyByteBuf buffer)
	{
		this.replace(buffer.readList(b ->
		{
			var stack = Utils.deserializeCodecMess(buffer);
			var count = b.readLong();
			return new BasicEntry<>(new ItemStackKey(stack), count);
		}));
	}

	public void serializeBuffer(RegistryFriendlyByteBuf buffer)
	{
		buffer.writeCollection(this.entrySet(), (b, entry) ->
		{
			Utils.serializeCodecMess(buffer, entry.getKey().getStack(1));
			b.writeLong(entry.getLongValue());
		});
	}

	public void replace(Iterable<Entry<ItemStackKey>> from)
	{
		var prevKeys = new HashSet<>(this.keySet());

		for (var entry : from)
		{
			var key = entry.getKey();
			this.set(key, entry.getLongValue());
			prevKeys.remove(key);
		}

		prevKeys.forEach(key -> this.set(key, 0L));
	}

	public long get(ItemStackKey key)
	{
		return this.counter.getOrDefault(key, 0L);
	}

	public long set(ItemStackKey key, long count)
	{
		count = Math.max(0, count);
		var old = count <= 0 ? this.counter.removeLong(key) : this.counter.put(key, count);

		if (old != count)
		{
			final var c = count;
			this.listeners.forEach(l -> l.onItemCountChanged(key, old, c));
		}

		return old;
	}

	public long extract(ItemStackKey key, long count)
	{
		var old = this.get(key);
		return this.set(key, old - count);
	}

	public long insert(ItemStackKey key, long count)
	{
		var old = this.get(key);
		return this.set(key, old + count);
	}

	public ObjectSet<ItemStackKey> keySet()
	{
		return this.counter.keySet();
	}

	public ObjectSet<Entry<ItemStackKey>> entrySet()
	{
		return this.counter.object2LongEntrySet();
	}

	public void addListener(CountChangedListener listener)
	{
		this.listeners.add(listener);
	}

	@FunctionalInterface
	public static interface CountChangedListener
	{
		void onItemCountChanged(ItemStackKey key, long oldCount, long newCount);
	}

}
