package steve_gall.minecolonies_compatibility.module.common.silentgear;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.minecolonies.api.MinecoloniesAPIProxy;
import com.minecolonies.api.colony.requestsystem.factory.IFactoryController;
import com.minecolonies.core.entity.ai.workers.crafting.EntityAIWorkBlacksmith;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import steve_gall.minecolonies_compatibility.api.common.repair.ToolSystemBrokenItem;
import steve_gall.minecolonies_compatibility.api.common.tool.CustomizedToolSystem;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.module.common.silentgear.init.ModuleBuildingModules;

public class BrokenItem extends ToolSystemBrokenItem
{
	public static final ResourceLocation ID = MineColoniesCompatibility.rl("silentgear_broken_item");
	public static final Component SHORT_DISPLAY_STRING = Component.translatable(MineColoniesCompatibility.tl("silentgear_broken_item"));
	public static final Component LONG_DISPLAY_STRING = Component.translatable(MineColoniesCompatibility.tl("silentgear_broken_item.desc"));

	private static List<ItemStack> EXAMPLES;

	public BrokenItem(EntityAIWorkBlacksmith ai)
	{
		super(ai);
	}

	@Override
	@NotNull
	public ResourceLocation getId()
	{
		return ID;
	}

	@Override
	public CustomizedToolSystem getToolSystem()
	{
		return SilentGearToolSystem.INSTANCE;
	}

	@Override
	public boolean canRepair(ItemStack stack)
	{
		var ai = this.getAI();

		if (ai == null)
		{
			return false;
		}

		return ai.building.getModule(ModuleBuildingModules.REPAIR_MATERIALS).canRepair(stack);
	}

	public static BrokenItem deserialize(IFactoryController controller, CompoundTag tag)
	{
		return new BrokenItem(null);
	}

	public static void serialize(IFactoryController controller, CompoundTag tag, BrokenItem request)
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
			EXAMPLES = MinecoloniesAPIProxy.getInstance().getColonyManager().getCompatibilityManager().getListOfAllItems().stream().filter(this.getToolSystem()::isTool).map(this::getBroken).toList();
		}

		return EXAMPLES;
	}

	private ItemStack getBroken(ItemStack stack)
	{
		stack = stack.copy();
		stack.setDamageValue(stack.getMaxDamage() - 1);
		return stack;
	}

	@Override
	public BrokenItem copyWithCount(int newCount)
	{
		return new BrokenItem(this.getAI());
	}

}
