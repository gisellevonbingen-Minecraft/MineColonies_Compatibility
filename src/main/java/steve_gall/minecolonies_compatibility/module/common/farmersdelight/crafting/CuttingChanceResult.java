package steve_gall.minecolonies_compatibility.module.common.farmersdelight.crafting;

import java.util.Objects;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import steve_gall.minecolonies_compatibility.core.common.item.ItemStackHelper;
import vectorwing.farmersdelight.common.crafting.ingredient.ChanceResult;

public class CuttingChanceResult
{
	public static CuttingChanceResult deserialize(CompoundTag tag)
	{
		var stack = ItemStack.of(tag.getCompound("stack"));
		var chance = tag.getFloat("chance");
		return new CuttingChanceResult(stack, chance);
	}

	public static CompoundTag serialize(CuttingChanceResult result)
	{
		var tag = new CompoundTag();
		tag.put("stack", result.stack.serializeNBT());
		tag.putFloat("chance", result.chance);

		return tag;
	}

	private final ItemStack stack;
	private final float chance;

	public CuttingChanceResult(ChanceResult original)
	{
		this.stack = original.getStack();
		this.chance = original.getChance();
	}

	public CuttingChanceResult(ItemStack stack, float chance)
	{
		this.stack = stack;
		this.chance = chance;
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(this.stack.getItem(), this.chance);
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj == this)
		{
			return true;
		}
		else if (obj == null)
		{
			return false;
		}
		else if (obj instanceof CuttingChanceResult other)
		{
			return ItemStackHelper.equals(this.stack, other.stack) && this.chance == other.chance;
		}
		else
		{
			return false;
		}

	}

	public ItemStack getStack()
	{
		return this.stack;
	}

	public float getChance()
	{
		return this.chance;
	}

}
