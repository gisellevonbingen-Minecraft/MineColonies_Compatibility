package steve_gall.minecolonies_compatibility.module.common.tconstruct;

import java.util.Collection;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import slimeknights.mantle.recipe.container.ISingleStackContainer;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.library.materials.definition.IMaterial;
import slimeknights.tconstruct.library.materials.definition.MaterialVariantId;
import slimeknights.tconstruct.library.recipe.TinkerRecipeTypes;
import slimeknights.tconstruct.library.recipe.casting.material.MaterialCastingLookup;
import slimeknights.tconstruct.library.recipe.material.IMaterialValue;
import slimeknights.tconstruct.library.recipe.material.MaterialRecipe;
import slimeknights.tconstruct.library.recipe.material.MaterialValue;
import slimeknights.tconstruct.library.tools.part.IMaterialItem;
import slimeknights.tconstruct.tools.item.RepairKitItem;

public class MaterialHelper
{
	public static boolean anyMatchesVariantId(Collection<MaterialVariantId> materials1, Collection<MaterialVariantId> materials2)
	{
		for (var m1 : materials1)
		{
			if (anyMatchesVariantId(materials2, m1))
			{
				return true;
			}

		}

		return false;
	}

	public static boolean anyMatchesVariantId(Collection<MaterialVariantId> materials, MaterialVariantId material)
	{
		for (var m : materials)
		{
			if (m.matchesVariant(material))
			{
				return true;
			}

		}

		return false;
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
