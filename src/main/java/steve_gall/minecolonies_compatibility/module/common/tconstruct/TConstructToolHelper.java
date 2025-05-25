package steve_gall.minecolonies_compatibility.module.common.tconstruct;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import net.minecraft.core.RegistryAccess;
import net.minecraft.world.inventory.TransientCraftingContainer;
import net.minecraft.world.item.ItemStack;
import slimeknights.tconstruct.library.materials.MaterialRegistry;
import slimeknights.tconstruct.library.materials.definition.IMaterial;
import slimeknights.tconstruct.library.materials.definition.MaterialVariant;
import slimeknights.tconstruct.library.materials.definition.MaterialVariantId;
import slimeknights.tconstruct.library.tools.definition.module.material.ToolMaterialHook;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.tools.TinkerToolParts;
import steve_gall.minecolonies_compatibility.core.common.inventory.EmptyMenu;

public class TConstructToolHelper
{
	public static boolean isToolAndBroken(ItemStack stack)
	{
		var system = TConstructToolSystem.INSTANCE;
		return system.isTool(stack) && system.isBroken(stack);
	}

	public static List<MaterialVariantId> getRepairVariantIds(IToolStackView tool)
	{
		return getRepairVariants(tool).stream().map(v -> v.getVariant()).toList();
	}

	public static List<MaterialVariant> getRepairVariants(IToolStackView tool)
	{
		var materials = tool.getMaterials();
		var components = ToolMaterialHook.stats(tool.getDefinition());

		var variantIds = new HashSet<String>();
		var variants = new ArrayList<MaterialVariant>();
		var registry = MaterialRegistry.getInstance();

		for (int i = 0; i < components.size(); i++)
		{
			if (i < materials.size() && registry.canRepair(components.get(i)))
			{
				var variant = materials.get(i);
				var variantId = variant.getVariant();

				if (!IMaterial.UNKNOWN_ID.equals(variantId))
				{
					if (variantIds.add(variantId.toString()))
					{
						variants.add(variant);
					}

				}

			}

		}

		return variants;
	}

	public static ItemStack repair(ItemStack tool, ItemStack repairKit, RegistryAccess registryAccess)
	{
		var container = new TransientCraftingContainer(EmptyMenu.INSTANCE, 1, 2);
		container.setItem(0, tool);
		container.setItem(1, repairKit);
		return TConstructModule.REPAIR_RECIPE.assemble(container, registryAccess);
	}

	public static int getRepairCount(ItemStack tool, MaterialVariantId variantId, RegistryAccess registryAccess)
	{
		var repairKit = TinkerToolParts.repairKit.get().withMaterial(variantId);
		return getRepairCount(tool, repairKit, registryAccess);
	}

	public static int getRepairCount(ItemStack tool, ItemStack repairKit, RegistryAccess registryAccess)
	{
		var oldDamage = ToolStack.from(tool).getDamage();

		for (var i = 0;; i++)
		{
			if (oldDamage == 0)
			{
				return i;
			}

			tool = repair(tool, repairKit, registryAccess);

			var newDamage = ToolStack.from(tool).getDamage();

			if (oldDamage == newDamage)
			{
				return i;
			}
			else
			{
				oldDamage = newDamage;
			}

		}

	}

	public static boolean canRepair(ItemStack tool)
	{
		return ToolStack.from(tool).getDamage() > 0;
	}

	private TConstructToolHelper()
	{

	}

}
