package steve_gall.minecolonies_compatibility.module.common.butchercraft.butcherable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.function.Predicate;

import org.jetbrains.annotations.NotNull;

import com.lance5057.butchercraft.ButchercraftItems;
import com.lance5057.butchercraft.workstations.bases.recipes.AnimatedRecipeItemUse;
import com.minecolonies.api.util.constant.ToolType;

import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import steve_gall.minecolonies_compatibility.api.common.butcher.ButcherBlockContext;
import steve_gall.minecolonies_compatibility.api.common.butcher.ButcherCitizenContext;
import steve_gall.minecolonies_compatibility.api.common.butcher.CustomizedButcherable;
import steve_gall.minecolonies_compatibility.api.common.crafting.ToolOrIngredientStack;
import steve_gall.minecolonies_compatibility.core.common.crafting.IngredientHelper;
import steve_gall.minecolonies_compatibility.core.common.entity.ai.butcher.EntityAIWorkButcher;
import steve_gall.minecolonies_compatibility.module.common.butchercraft.init.ModuleBuildingModules;

public abstract class AbstractButcherable extends CustomizedButcherable
{
	private static List<ItemStack> FIXEDS;

	public AbstractButcherable()
	{

	}

	protected @NotNull List<ToolOrIngredientStack> getToolIcons(@NotNull List<AnimatedRecipeItemUse> itemUses)
	{
		var stackingIdsSet = new HashSet<IntList>();
		var toolIcons = new ArrayList<ToolOrIngredientStack>();

		for (var itemUse : itemUses)
		{
			if (stackingIdsSet.add(itemUse.tool.getStackingIds()))
			{
				toolIcons.add(this.getTool(itemUse));
			}

		}
		return Collections.unmodifiableList(toolIcons);
	}

	protected @NotNull ToolOrIngredientStack getTool(@NotNull AnimatedRecipeItemUse itemUse)
	{
		var tool = itemUse.tool;
		var count = IngredientHelper.isDamageable(tool) ? itemUse.count : itemUse.count * itemUse.uses;

		if (FIXEDS == null)
		{
			var list = new ArrayList<ItemStack>();
			list.add(new ItemStack(Items.GLASS_BOTTLE));
			list.add(new ItemStack(Items.BUCKET));
			list.add(new ItemStack(ButchercraftItems.BUTCHER_KNIFE.get()));
			list.add(new ItemStack(ButchercraftItems.SKINNING_KNIFE.get()));
			list.add(new ItemStack(ButchercraftItems.BONE_SAW.get()));
			list.add(new ItemStack(ButchercraftItems.GUT_KNIFE.get()));
			FIXEDS = Collections.unmodifiableList(list);
		}

		for (var fix : FIXEDS)
		{
			if (tool.test(fix))
			{
				return ToolOrIngredientStack.of(tool, count);
			}

		}

		var toolType = IngredientHelper.findFirstToolType(tool);
		return toolType == ToolType.NONE ? ToolOrIngredientStack.of(tool, count) : ToolOrIngredientStack.of(toolType);
	}

	protected boolean isSkippable(@NotNull Predicate<ItemStack> predicate)
	{
		return predicate.test(new ItemStack(Items.GLASS_BOTTLE)) || predicate.test(new ItemStack(Items.BUCKET));
	}

	protected boolean trySkip(@NotNull Predicate<ItemStack> predicate, @NotNull EntityAIWorkButcher ai)
	{
		return ai.building.getSetting(ModuleBuildingModules.SKIP_BLOOD).getValue().booleanValue() && this.isSkippable(predicate);
	}

	@Override
	public @NotNull ToolOrIngredientStack getTableTool(@NotNull ButcherBlockContext context, @NotNull ButcherCitizenContext citizen)
	{
		return ToolOrIngredientStack.EMPTY;
	}

}
