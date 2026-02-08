package steve_gall.minecolonies_compatibility.module.common.tconstruct;

import net.minecraft.nbt.CompoundTag;
import slimeknights.tconstruct.library.materials.definition.MaterialVariantId;

public record RepairValue(MaterialVariantId material, float value, int needed)
{
	public static RepairValue deserializeTag(CompoundTag tag)
	{
		var material = MaterialVariantId.parse(tag.getString("material"));
		var value = tag.getFloat("value");
		var needed = tag.getInt("needed");
		return new RepairValue(material, value, needed);
	}

	public static void serializeTag(CompoundTag tag, RepairValue value)
	{
		tag.putString("material", value.material.toString());
		tag.putFloat("value", value.value);
		tag.putInt("needed", value.needed);
	}

}
