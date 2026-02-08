package steve_gall.minecolonies_compatibility.module.common.tconstruct;

import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.minecolonies.api.colony.requestsystem.factory.IFactoryController;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import slimeknights.tconstruct.library.materials.definition.MaterialVariantId;
import slimeknights.tconstruct.library.tools.part.IRepairKitItem;
import slimeknights.tconstruct.tools.TinkerToolParts;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_tweaks.api.common.requestsystem.IDeliverableObject;

public class RepairKit implements IDeliverableObject
{
	public static final ResourceLocation ID = MineColoniesCompatibility.rl("tconstruct_repair_kit");
	public static final Component SHORT_DISPLAY_STRING = Component.translatable(MineColoniesCompatibility.tl("tconstruct_repair_kit"));
	public static final Component LONG_DISPLAY_STRING = Component.translatable(MineColoniesCompatibility.tl("tconstruct_repair_kit.desc"));

	private final MaterialVariantId variantId;
	private final ItemStack item;
	private final int count;
	private final boolean legacy;
	private List<ItemStack> examples;

	public RepairKit(MaterialVariantId variantId, ItemStack item, int count)
	{
		this(variantId, item, count, false);
	}

	private RepairKit(MaterialVariantId variantId, ItemStack item, int count, boolean legacy)
	{
		this.variantId = variantId;
		this.item = item;
		this.count = count;
		this.legacy = legacy;
	}

	@Override
	@NotNull
	public ResourceLocation getId()
	{
		return ID;
	}

	public static RepairKit deserialize(IFactoryController controller, CompoundTag tag)
	{
		var item = ItemStack.EMPTY;
		var variantId = MaterialVariantId.tryParse(tag.getString("variantId"));
		var legacy = false;

		if (tag.contains("item"))
		{
			item = ItemStack.of(tag.getCompound("item"));
			legacy = tag.getBoolean("legacy");
		}
		else
		{
			item = TinkerToolParts.repairKit.get().withMaterial(variantId);
			legacy = true;
		}

		var count = tag.getInt("count");
		return new RepairKit(variantId, item, count, legacy);
	}

	public static void serialize(IFactoryController controller, CompoundTag tag, RepairKit request)
	{
		tag.putString("variantId", request.variantId.toString());
		tag.put("item", request.item.serializeNBT());
		tag.putInt("count", request.count);
		tag.putBoolean("legacy", request.legacy);
	}

	@Override
	@NotNull
	public Component getShortDisplayString()
	{
		return SHORT_DISPLAY_STRING;
	}

	@Override
	@NotNull
	public Component getLongDisplayString()
	{
		return LONG_DISPLAY_STRING;
	}

	@Override
	@NotNull
	public List<ItemStack> getDisplayStacks()
	{
		if (this.examples == null)
		{
			this.examples = Collections.singletonList(this.item);
		}

		return this.examples;
	}

	@Override
	public RepairKit copyWithCount(int newCount)
	{
		return new RepairKit(this.variantId, this.item.copy(), newCount, this.legacy);
	}

	@Override
	public int getCount()
	{
		return this.count;
	}

	@Override
	public int getMinimumCount()
	{
		return 1;
	}

	@Override
	public boolean matches(@NotNull ItemStack stack)
	{
		if (this.legacy)
		{
			return stack.getItem() instanceof IRepairKitItem item && item.getMaterial(stack).matchesVariant(this.variantId);
		}
		else
		{
			return ItemStack.isSameItemSameTags(this.item, stack);
		}

	}

	public MaterialVariantId getVariantId()
	{
		return this.variantId;
	}

	public ItemStack getItem()
	{
		return this.item;
	}

}
