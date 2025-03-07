package steve_gall.minecolonies_compatibility.module.common.tconstruct;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.minecolonies.api.MinecoloniesAPIProxy;
import com.minecolonies.core.entity.ai.citizen.blacksmith.EntityAIWorkBlacksmith;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import slimeknights.tconstruct.library.tools.item.IModifiable;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.library.tools.stat.ToolStats;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_tweaks.api.common.requestsystem.IDeliverableObject;

public class BrokenItem implements IDeliverableObject
{
	public static final ResourceLocation ID = MineColoniesCompatibility.rl("tconstruct_broken_item");
	public static final Component SHORT_DISPLAY_STRING = Component.translatable(MineColoniesCompatibility.tl("tconstruct_broken_item"));
	public static final Component LONG_DISPLAY_STRING = Component.translatable(MineColoniesCompatibility.tl("tconstruct_broken_item.desc"));

	private static List<ItemStack> EXAMPLES;

	private final EntityAIWorkBlacksmith ai;

	public BrokenItem(EntityAIWorkBlacksmith ai)
	{
		this.ai = ai;
	}

	public EntityAIWorkBlacksmith getAI()
	{
		return this.ai;
	}

	@Override
	@NotNull
	public ResourceLocation getId()
	{
		return ID;
	}

	public static BrokenItem deserialize(@NotNull CompoundTag tag)
	{
		return new BrokenItem(null);
	}

	public static void serialize(BrokenItem request, @NotNull CompoundTag tag)
	{

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
			EXAMPLES = MinecoloniesAPIProxy.getInstance().getColonyManager().getCompatibilityManager().getListOfAllItems().stream().filter(stack -> stack.getItem() instanceof IModifiable).map(stack ->
			{
				return getBroken(stack);
			}).toList();
		}

		return EXAMPLES;
	}

	private ItemStack getBroken(ItemStack stack)
	{
		stack = stack.copy();
		var tool = ToolStack.from(stack);
		var durability = tool.getStats().getInt(ToolStats.DURABILITY);
		tool.setDamage(durability);

		return stack;
	}

	@Override
	public BrokenItem copyWithCount(int newCount)
	{
		return new BrokenItem(this.ai);
	}

	@Override
	public int getCount()
	{
		return 1;
	}

	@Override
	public boolean matches(@NotNull ItemStack stack)
	{
		return this.ai != null && ToolHelper.isBroken(stack) && this.ai.building.getBuildingLevel() >= ToolHelper.getTier(stack);
	}

}
