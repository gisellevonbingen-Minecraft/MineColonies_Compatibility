package steve_gall.minecolonies_compatibility.module.common.tconstruct;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.ItemHandlerHelper;
import slimeknights.tconstruct.library.materials.MaterialRegistry;
import slimeknights.tconstruct.library.materials.definition.IMaterial;
import slimeknights.tconstruct.library.materials.definition.MaterialVariant;
import slimeknights.tconstruct.library.recipe.TinkerRecipeTypes;
import slimeknights.tconstruct.library.recipe.material.MaterialRecipe;
import slimeknights.tconstruct.library.recipe.tinkerstation.ITinkerStationContainer;
import slimeknights.tconstruct.library.tools.definition.module.material.ToolMaterialHook;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

public class TConstructToolHelper
{
	public static boolean isToolAndBroken(ItemStack stack)
	{
		var system = TConstructToolSystem.INSTANCE;
		return system.isTool(stack) && system.isBroken(stack);
	}

	public static int getRepairRequiredLevel(IToolStackView tool)
	{
		return getRepairVariants(tool).stream().mapToInt(MaterialHelper::getRequiredLevel).max().orElse(-1);
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

	public static ItemStack repair(ItemStack tool, ItemStack material, Level level)
	{
		var container = new RepairTinkerStationContainer(tool, material, level);
		var recipe = level.getRecipeManager().getRecipeFor(TinkerRecipeTypes.TINKER_STATION.get(), container, level).orElse(null);

		if (recipe != null)
		{
			var validatedResult = recipe.getValidatedResult(container);

			if (validatedResult.isSuccess())
			{
				return validatedResult.getResult();
			}

		}

		return tool;
	}

	public static int getRepairCount(ItemStack tool, ItemStack material, int limit, Level level)
	{
		var oldDamage = ToolStack.from(tool).getDamage();

		if (oldDamage == 0)
		{
			return 0;
		}

		var maxStackSize = material.getMaxStackSize();
		var repairCount = 0;

		for (var i = 1;; i++)
		{
			if (repairCount >= limit)
			{
				break;
			}

			material = ItemHandlerHelper.copyStackWithSize(material, i);
			var repairedTool = repair(tool, material, level);
			var newDamage = ToolStack.from(repairedTool).getDamage();

			if (newDamage == 0)
			{
				repairCount++;
				break;
			}
			else if (oldDamage == newDamage)
			{
				break;
			}
			else if (i >= maxStackSize)
			{
				i -= maxStackSize;
				tool = repairedTool;
			}

			oldDamage = newDamage;
			repairCount++;
		}

		return repairCount;
	}

	public static boolean canRepair(ItemStack tool)
	{
		return ToolStack.from(tool).getDamage() > 0;
	}

	private record RepairTinkerStationContainer(ItemStack tinkerableStack, ItemStack input, MaterialRecipe inputMaterial) implements ITinkerStationContainer
	{
		public RepairTinkerStationContainer(ItemStack tinkerableStack, ItemStack input, Level level)
		{
			this(tinkerableStack, input, MaterialHelper.getMaterialRecipe(input, level));
		}

		@Override
		public ItemStack getTinkerableStack()
		{
			return this.tinkerableStack;
		}

		@Override
		public int getInputCount()
		{
			return 1;
		}

		@Override
		public ItemStack getInput(int index)
		{
			return this.input;
		}

		@Override
		public MaterialRecipe getInputMaterial(int index)
		{
			return this.inputMaterial;
		}

	}

	private TConstructToolHelper()
	{

	}

}
