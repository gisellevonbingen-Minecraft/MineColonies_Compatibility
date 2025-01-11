package steve_gall.minecolonies_compatibility.core.common.entity.ai.butcher;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.minecolonies.api.MinecoloniesAPIProxy;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import steve_gall.minecolonies_compatibility.api.common.butcher.CustomizedButcherable;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.core.common.building.module.ButcherableListModule;
import steve_gall.minecolonies_tweaks.api.common.requestsystem.IDeliverableObject;

public class Butcherable implements IDeliverableObject
{
	public static final ResourceLocation ID = MineColoniesCompatibility.rl("butcherable");
	public static final Component SHORT_DISPLAY_STRING = Component.translatable(MineColoniesCompatibility.tl("butcherable"));
	public static final Component LONG_DISPLAY_STRING = Component.translatable(MineColoniesCompatibility.tl("butcherable.desc"));

	private static List<ItemStack> EXAMPLES = null;

	private final int minCount;
	private final ButcherableListModule blacklist;

	public Butcherable(int minCount, ButcherableListModule blacklist)
	{
		this.minCount = minCount;
		this.blacklist = blacklist;
	}

	@Override
	@NotNull
	public ResourceLocation getId()
	{
		return ID;
	}

	public static @NotNull Butcherable deserialize(@NotNull CompoundTag tag)
	{
		var minCount = tag.getInt("minCount");
		return new Butcherable(minCount, null);
	}

	public static void serialize(@NotNull Butcherable request, @NotNull CompoundTag tag)
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
		if (EXAMPLES == null)
		{
			EXAMPLES = MinecoloniesAPIProxy.getInstance().getColonyManager().getCompatibilityManager().getListOfAllItems().stream().filter(this::matches).toList();
		}

		return EXAMPLES;
	}

	@Override
	public Butcherable copyWithCount(int newCount)
	{
		return new Butcherable(this.minCount, this.blacklist);
	}

	@Override
	public int getCount()
	{
		return 8;
	}

	@Override
	public int getMinimumCount()
	{
		return this.minCount;
	}

	public ButcherableListModule getBlacklist()
	{
		return this.blacklist;
	}

	@Override
	public boolean matches(@NotNull ItemStack stack)
	{
		var butcherable = CustomizedButcherable.selectByItem(stack);
		return butcherable != null && (this.blacklist == null || !this.blacklist.containsId(butcherable.getId()));
	}

}
