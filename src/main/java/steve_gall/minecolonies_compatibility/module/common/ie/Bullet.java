package steve_gall.minecolonies_compatibility.module.common.ie;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.minecolonies.api.MinecoloniesAPIProxy;

import blusunrize.immersiveengineering.common.items.BulletItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_tweaks.api.common.requestsystem.IDeliverableObject;

public class Bullet implements IDeliverableObject
{
	public static final ResourceLocation ID = MineColoniesCompatibility.rl("ie_bullet");
	public static final Component SHORT_DISPLAY_STRING = Component.translatable(MineColoniesCompatibility.tl("ie_bullet"));
	public static final Component LONG_DISPLAY_STRING = Component.translatable(MineColoniesCompatibility.tl("ie_bullet.desc"));

	private static List<ItemStack> BULLET_EXAMPLES = null;

	private final int minCount;

	public Bullet(int minCount)
	{
		this.minCount = minCount;
	}

	@Override
	@NotNull
	public ResourceLocation getId()
	{
		return ID;
	}

	public static Bullet deserialize(@NotNull CompoundTag tag)
	{
		var minCount = tag.getInt("minCount");
		return new Bullet(minCount);
	}

	public static void serialize(@NotNull CompoundTag tag, Bullet request)
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
		if (BULLET_EXAMPLES == null)
		{
			BULLET_EXAMPLES = MinecoloniesAPIProxy.getInstance().getColonyManager().getCompatibilityManager().getListOfAllItems().stream().filter(this::matches).toList();
		}

		return BULLET_EXAMPLES;
	}

	@Override
	public Bullet copyWithCount(int newCount)
	{
		return new Bullet(this.minCount);
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
		return stack.getItem() instanceof BulletItem;
	}

}
