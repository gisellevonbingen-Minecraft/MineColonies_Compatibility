package steve_gall.minecolonies_compatibility.core.common.building.module;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.NumericTag;
import net.minecraft.nbt.Tag;

public enum AccessDirection
{
	INSERT(0),
	EXTRACT(1),
	INSERT_EXTRACT(2),
	// EOL
	;

	private final int id;

	@NotNull
	public static AccessDirection deserialize(@Nullable Tag tag)
	{
		if (tag instanceof NumericTag numeric)
		{
			return byId(numeric.getAsInt());
		}
		else
		{
			return INSERT_EXTRACT;
		}

	}

	@NotNull
	public static Tag serialize(@NotNull AccessDirection value)
	{
		return IntTag.valueOf(value.id);
	}

	@NotNull
	public static AccessDirection byId(int id)
	{
		for (var value : AccessDirection.values())
		{
			if (value.getId() == id)
			{
				return value;
			}

		}

		return INSERT_EXTRACT;
	}

	private AccessDirection(int id)
	{
		this.id = id;
	}

	@NotNull
	public AccessDirection prev()
	{
		var values = values();
		return values[(this.ordinal() - 1) % values.length];
	}

	@NotNull
	public AccessDirection next()
	{
		var values = values();
		return values[(this.ordinal() + 1) % values.length];
	}

	@NotNull
	public Tag serialize()
	{
		return serialize(this);
	}

	public int getId()
	{
		return this.id;
	}

	public boolean canExtract()
	{
		return this != INSERT;
	}

	public boolean canInsert()
	{
		return this != EXTRACT;
	}

}
