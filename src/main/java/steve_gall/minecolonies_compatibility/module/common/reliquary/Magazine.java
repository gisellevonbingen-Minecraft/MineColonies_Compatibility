package steve_gall.minecolonies_compatibility.module.common.reliquary;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.minecolonies.api.MinecoloniesAPIProxy;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import reliquary.init.ModItems;
import reliquary.items.MagazineItem;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_tweaks.api.common.requestsystem.IDeliverableObject;

public class Magazine implements IDeliverableObject
{
	public static final ResourceLocation ID = MineColoniesCompatibility.rl("reliquary_magazine");
	public static final Component SHORT_DISPLAY_STRING = Component.translatable(MineColoniesCompatibility.tl("reliquary_magazine"));
	public static final Component LONG_DISPLAY_STRING = Component.translatable(MineColoniesCompatibility.tl("reliquary_magazine.desc"));

	private static List<ItemStack> MAGAZINE_EXAMPLES = null;

	private final int minCount;

	public Magazine(int minCount)
	{
		this.minCount = minCount;
	}

	@Override
	@NotNull
	public ResourceLocation getId()
	{
		return ID;
	}

	public static Magazine deserialize(@NotNull CompoundTag tag)
	{
		var minCount = tag.getInt("minCount");
		return new Magazine(minCount);
	}

	public static void serialize(@NotNull CompoundTag tag, Magazine request)
	{
		tag.putInt("minCount", request.minCount);
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
		if (MAGAZINE_EXAMPLES == null)
		{
			MAGAZINE_EXAMPLES = MinecoloniesAPIProxy.getInstance().getColonyManager().getCompatibilityManager().getListOfAllItems().stream().filter(this::matches).toList();
		}

		return MAGAZINE_EXAMPLES;
	}

	@Override
	public Magazine copyWithCount(int newCount)
	{
		return new Magazine(this.minCount);
	}

	@Override
	public int getCount()
	{
		return 64;
	}

	@Override
	public int getMinimumCount()
	{
		return this.minCount;
	}

	@Override
	public boolean matches(@NotNull ItemStack stack)
	{
		return stack.getItem() instanceof MagazineItem item && item != ModItems.EMPTY_MAGAZINE.get();
	}

}
