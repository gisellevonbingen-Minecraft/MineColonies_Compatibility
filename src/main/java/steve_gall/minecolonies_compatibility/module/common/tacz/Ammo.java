package steve_gall.minecolonies_compatibility.module.common.tacz;

import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.tacz.guns.api.item.IAmmo;
import com.tacz.guns.api.item.builder.AmmoItemBuilder;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_tweaks.api.common.requestsystem.IDeliverableObject;

public class Ammo implements IDeliverableObject
{
	public static final ResourceLocation ID = MineColoniesCompatibility.rl("tacz_ammo");
	public static final Component SHORT_DISPLAY_STRING = Component.translatable(MineColoniesCompatibility.tl("tacz_ammo"));
	public static final Component LONG_DISPLAY_STRING = Component.translatable(MineColoniesCompatibility.tl("tacz_ammo.desc"));

	private ResourceLocation ammoId;
	private final int minCount;

	private List<ItemStack> example;

	public Ammo(ResourceLocation ammoId, int minCount)
	{
		this.ammoId = ammoId;
		this.minCount = minCount;
	}

	@Override
	@NotNull
	public ResourceLocation getId()
	{
		return ID;
	}

	public static Ammo deserialize(@NotNull CompoundTag tag)
	{
		var ammoId = new ResourceLocation(tag.getString("ammoId"));
		var minCount = tag.getInt("minCount");
		return new Ammo(ammoId, minCount);
	}

	public static void serialize(Ammo request, @NotNull CompoundTag tag)
	{
		tag.putString("ammoId", request.ammoId.toString());
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
		if (this.example == null)
		{
			this.example = Collections.singletonList(AmmoItemBuilder.create().setId(this.ammoId).build());
		}

		return this.example;
	}

	@Override
	public Ammo copyWithCount(int newCount)
	{
		return new Ammo(this.ammoId, this.minCount);
	}

	@Override
	public int getCount()
	{
		return 64;
	}

	public ResourceLocation getAmmoId()
	{
		return this.ammoId;
	}

	@Override
	public int getMinimumCount()
	{
		return this.minCount;
	}

	@Override
	public boolean matches(@NotNull ItemStack stack)
	{
		return stack.getItem() instanceof IAmmo item && item.getAmmoId(stack).equals(this.ammoId);
	}

}
