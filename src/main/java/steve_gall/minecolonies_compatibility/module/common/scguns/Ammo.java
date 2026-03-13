package steve_gall.minecolonies_compatibility.module.common.scguns;

import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.minecolonies.api.colony.requestsystem.factory.IFactoryController;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import steve_gall.minecolonies_compatibility.api.common.entity.ai.guard.GunnerAmmo;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import top.ribs.scguns.common.Gun;

public class Ammo implements GunnerAmmo
{
	public static final ResourceLocation ID = MineColoniesCompatibility.rl("scguns_ammo");
	public static final Component SHORT_DISPLAY_STRING = Component.translatable(MineColoniesCompatibility.tl("scguns_ammo"));
	public static final Component LONG_DISPLAY_STRING = Component.translatable(MineColoniesCompatibility.tl("scguns_ammo.desc"));

	private final Item ammoItem;
	private final int count;
	private final int minCount;

	private List<ItemStack> example;

	public Ammo(Item ammoItem, int count, int minCount)
	{
		this.ammoItem = ammoItem;
		this.count = Math.max(count, minCount);
		this.minCount = minCount;
	}

	@Override
	@NotNull
	public ResourceLocation getId()
	{
		return ID;
	}

	public static Ammo deserialize(IFactoryController controller, CompoundTag tag)
	{
		var ammoId = ForgeRegistries.ITEMS.getValue(new ResourceLocation(tag.getString("ammoId")));
		var count = tag.getInt("count");
		var minCount = tag.getInt("minCount");
		return new Ammo(ammoId, count, minCount);
	}

	public static void serialize(IFactoryController controller, CompoundTag tag, Ammo request)
	{
		tag.putString("ammoId", ForgeRegistries.ITEMS.getKey(request.ammoItem).toString());
		tag.putInt("count", request.count);
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
			this.example = Collections.singletonList(new ItemStack(this.ammoItem));
		}

		return this.example;
	}

	@Override
	public Ammo copyWithCount(int newCount)
	{
		return new Ammo(this.ammoItem, newCount, this.minCount);
	}

	@Override
	public int getCount()
	{
		return this.count;
	}

	public Item getAmmoItem()
	{
		return this.ammoItem;
	}

	@Override
	public int getMinimumCount()
	{
		return this.minCount;
	}

	@Override
	public boolean matches(@NotNull ItemStack stack)
	{
		return Gun.isAmmo(stack, this.ammoItem);
	}

}
