package steve_gall.minecolonies_compatibility.module.common.tconstruct;

import com.minecolonies.api.util.constant.BuildingConstants;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import slimeknights.mantle.recipe.container.ISingleStackContainer;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.library.materials.definition.IMaterial;
import slimeknights.tconstruct.library.materials.definition.MaterialVariant;
import slimeknights.tconstruct.library.recipe.TinkerRecipeTypes;
import slimeknights.tconstruct.library.recipe.casting.material.MaterialCastingLookup;
import slimeknights.tconstruct.library.recipe.material.IMaterialValue;
import slimeknights.tconstruct.library.recipe.material.MaterialRecipe;
import slimeknights.tconstruct.library.recipe.material.MaterialValue;
import slimeknights.tconstruct.library.tools.part.IMaterialItem;
import slimeknights.tconstruct.tools.item.RepairKitItem;

public class MaterialHelper
{
	public static int getRequiredLevel(MaterialVariant variant)
	{
		return getRequiredLevel(variant.get());
	}

	public static int getRequiredLevel(IMaterial material)
	{
		return Math.min(material.getTier(), BuildingConstants.CONST_DEFAULT_MAX_BUILDING_LEVEL);
	}

	public static IMaterialValue getMaterialValue(ItemStack item, Level level)
	{
		if (item.isEmpty())
		{
			return null;
		}
		else if (item.is(TinkerTags.Items.TOOL_PARTS))
		{
			var material = IMaterialItem.getMaterialFromStack(item);
			var cost = MaterialCastingLookup.getItemCost(item.getItem());

			if (cost == 0 || IMaterial.UNKNOWN_ID.matchesVariant(material))
			{
				return null;
			}
			else
			{
				return new MaterialValue(material, cost);
			}

		}
		else
		{
			return getMaterialRecipe(item, level);
		}

	}

	public static RepairValue getRepairValue(ItemStack item, Level level)
	{
		if (item.getItem() instanceof RepairKitItem kit)
		{
			return new RepairValue(kit.getMaterial(item), kit.getRepairAmount(), 1);
		}

		var materialRecipe = getMaterialRecipe(item, level);

		if (materialRecipe != null)
		{
			return new RepairValue(materialRecipe.getMaterial().getVariant(), materialRecipe.getValue(), materialRecipe.getNeeded());
		}

		return null;
	}

	public static MaterialRecipe getMaterialRecipe(ItemStack item, Level level)
	{
		return level.getRecipeManager().getRecipeFor(TinkerRecipeTypes.MATERIAL.get(), new SingleStackContainer(item), level).orElse(null);
	}

	private record SingleStackContainer(ItemStack item) implements ISingleStackContainer
	{
		@Override
		public ItemStack getStack()
		{
			return this.item;
		}

	}

	private MaterialHelper()
	{

	}

}
