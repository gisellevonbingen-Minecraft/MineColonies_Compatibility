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
	private final int count;
	private List<ItemStack> examples;

	public RepairKit(MaterialVariantId variantId, int count)
	{
		this.variantId = variantId;
		this.count = count;
	}

	@Override
	@NotNull
	public ResourceLocation getId()
	{
		return ID;
	}

	public static RepairKit deserialize(IFactoryController controller, CompoundTag tag)
	{
		var variantId = MaterialVariantId.tryParse(tag.getString("variantId"));
		var count = tag.getInt("count");
		return new RepairKit(variantId, count);
	}

	public static void serialize(IFactoryController controller, CompoundTag tag, RepairKit request)
	{
		tag.putString("variantId", request.variantId.toString());
		tag.putInt("count", request.count);
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
			this.examples = Collections.singletonList(TinkerToolParts.repairKit.get().withMaterial(this.variantId));
		}

		return this.examples;
	}

	@Override
	public RepairKit copyWithCount(int newCount)
	{
		return new RepairKit(this.variantId, newCount);
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
		return isRepairKitItem(stack, this.variantId);
	}

	public static boolean isRepairKitItem(ItemStack stack, MaterialVariantId variantId)
	{
		return stack.getItem() instanceof IRepairKitItem item && item.getMaterial(stack).matchesVariant(variantId);
	}

	public MaterialVariantId getVariantId()
	{
		return this.variantId;
	}

}
