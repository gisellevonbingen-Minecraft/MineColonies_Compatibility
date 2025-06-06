package steve_gall.minecolonies_compatibility.module.common.tacz.item;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.tacz.guns.api.DefaultAssets;
import com.tacz.guns.api.item.IGun;
import com.tacz.guns.api.item.attachment.AttachmentType;
import com.tacz.guns.api.item.gun.FireMode;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;

public class DummyGunItem extends Item implements IGun
{
	public static final ResourceLocation GUN_ID = MineColoniesCompatibility.rl("tach_dummy_gun");
	public static final Component TOOLTIP = Component.translatable("item.minecolonies_compatibility.tacz_dummy_gun.tooltip");

	public DummyGunItem(Item.Properties properties)
	{
		super(properties);
	}

	@Override
	public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flag)
	{
		super.appendHoverText(stack, level, tooltip, flag);

		tooltip.add(TOOLTIP);
	}

	@Override
	public void addDummyAmmoAmount(ItemStack arg0, int arg1)
	{

	}

	@Override
	public boolean allowAttachment(ItemStack arg0, ItemStack arg1)
	{
		return false;
	}

	@Override
	public boolean allowAttachmentType(ItemStack arg0, AttachmentType arg1)
	{
		return false;
	}

	@Override
	public void dropAllAmmo(Player arg0, ItemStack arg1)
	{

	}

	@Override
	public float getAimingZoom(ItemStack arg0)
	{
		return 0.0F;
	}

	@Override
	public ItemStack getAttachment(ItemStack arg0, AttachmentType arg1)
	{
		return ItemStack.EMPTY;
	}

	@Override
	public ResourceLocation getAttachmentId(ItemStack arg0, AttachmentType arg1)
	{
		return DefaultAssets.EMPTY_ATTACHMENT_ID;
	}

	@Override
	public CompoundTag getAttachmentTag(ItemStack arg0, AttachmentType arg1)
	{
		return null;
	}

	@Override
	public ResourceLocation getBuiltInAttachmentId(ItemStack arg0, AttachmentType arg1)
	{
		return DefaultAssets.EMPTY_ATTACHMENT_ID;
	}

	@Override
	public ItemStack getBuiltinAttachment(ItemStack arg0, AttachmentType arg1)
	{
		return ItemStack.EMPTY;
	}

	@Override
	public int getCurrentAmmoCount(ItemStack arg0)
	{
		return 0;
	}

	@Override
	public int getDummyAmmoAmount(ItemStack arg0)
	{
		return 0;
	}

	@Override
	public int getExp(int arg0)
	{
		return 0;
	}

	@Override
	public int getExp(ItemStack arg0)
	{
		return 0;
	}

	@Override
	public int getExpCurrentLevel(ItemStack arg0)
	{
		return 0;
	}

	@Override
	public int getExpToNextLevel(ItemStack arg0)
	{
		return 0;
	}

	@Override
	public FireMode getFireMode(ItemStack arg0)
	{
		return FireMode.UNKNOWN;
	}

	@Override
	public @NotNull ResourceLocation getGunDisplayId(ItemStack arg0)
	{
		return DefaultAssets.DEFAULT_GUN_DISPLAY_ID;
	}

	@Override
	public @NotNull ResourceLocation getGunId(ItemStack arg0)
	{
		return DefaultAssets.EMPTY_GUN_ID;
	}

	@Override
	public int getLevel(int arg0)
	{
		return 0;
	}

	@Override
	public int getLevel(ItemStack arg0)
	{
		return 0;
	}

	@Override
	public int getMaxDummyAmmoAmount(ItemStack arg0)
	{
		return 0;
	}

	@Override
	public int getMaxLevel()
	{
		return 0;
	}

	@Override
	public boolean hasAttachmentLock(ItemStack arg0)
	{
		return false;
	}

	@Override
	public boolean hasBulletInBarrel(ItemStack arg0)
	{
		return false;
	}

	@Override
	public boolean hasMaxDummyAmmo(ItemStack arg0)
	{
		return false;
	}

	@Override
	public void installAttachment(ItemStack arg0, ItemStack arg1)
	{

	}

	@Override
	public void reduceCurrentAmmoCount(ItemStack arg0)
	{

	}

	@Override
	public void setAttachmentLock(ItemStack arg0, boolean arg1)
	{

	}

	@Override
	public void setBulletInBarrel(ItemStack arg0, boolean arg1)
	{

	}

	@Override
	public void setCurrentAmmoCount(ItemStack arg0, int arg1)
	{

	}

	@Override
	public void setDummyAmmoAmount(ItemStack arg0, int arg1)
	{

	}

	@Override
	public void setFireMode(ItemStack arg0, FireMode arg1)
	{

	}

	@Override
	public void setGunDisplayId(ItemStack arg0, ResourceLocation arg1)
	{

	}

	@Override
	public void setGunId(ItemStack arg0, ResourceLocation arg1)
	{

	}

	@Override
	public void setMaxDummyAmmoAmount(ItemStack arg0, int arg1)
	{

	}

	@Override
	public void unloadAttachment(ItemStack arg0, AttachmentType arg1)
	{

	}

	@Override
	public boolean useDummyAmmo(ItemStack arg0)
	{
		return false;
	}

}
