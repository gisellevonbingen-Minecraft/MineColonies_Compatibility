package steve_gall.minecolonies_compatibility.module.common.tconstruct;

import java.util.HashMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.minecolonies.api.colony.requestsystem.request.RequestState;
import com.minecolonies.api.util.InventoryUtils;
import com.minecolonies.api.util.Tuple;

import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemHandlerHelper;
import slimeknights.tconstruct.library.materials.definition.MaterialId;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import steve_gall.minecolonies_compatibility.api.common.repair.CustomizedRepair;
import steve_gall.minecolonies_compatibility.api.common.repair.EntityContext;
import steve_gall.minecolonies_compatibility.api.common.repair.RepairTransaction;
import steve_gall.minecolonies_compatibility.core.common.colony.CitizenHelper;
import steve_gall.minecolonies_compatibility.module.common.ModuleManager;
import steve_gall.minecolonies_compatibility.module.common.tconstruct.building.module.RepairMaterialListModule;
import steve_gall.minecolonies_compatibility.module.common.tconstruct.init.ModuleBuildingModules;
import steve_gall.minecolonies_tweaks.api.common.requestsystem.CustomizableDeliverable;

public class TConstructRepair extends CustomizedRepair
{
	@Override
	public @NotNull ResourceLocation getId()
	{
		return new ResourceLocation(ModuleManager.TCONSTRUCT.getModId(), "repair_kit");
	}

	@Override
	public @Nullable CheckResult check(@NotNull EntityContext context)
	{
		var ai = context.getAI();
		var worker = context.getWorker();
		var inventory = worker.getInventoryCitizen();
		var citizenData = worker.getCitizenData();
		var repairMaterialsModule = ai.building.getModule(ModuleBuildingModules.REPAIR_MATERIALS);
		var materialsEmpty = repairMaterialsModule.size() == 0;
		var any = false;

		for (var request : CitizenHelper.getRequests(citizenData, CustomizableDeliverable.TYPE_TOKEN, r -> r.getRequest().getObject() instanceof BrokenItem))
		{
			if (materialsEmpty || ((BrokenItem) request.getRequest().getObject()).getAI() == null)
			{
				citizenData.getColony().getRequestManager().updateRequestState(request.getId(), RequestState.CANCELLED);
			}
			else
			{
				any = true;
			}

		}

		if (materialsEmpty)
		{
			return null;
		}

		var buildingLevel = ai.building.getBuildingLevel();
		var repairKits = new HashMap<MaterialId, ItemStack>();
		var toolSlots = new Object2IntOpenHashMap<MaterialId>();

		for (var i = 0; i < inventory.getSlots(); i++)
		{
			var item = inventory.getStackInSlot(i);

			if (TConstructToolHelper.isToolAndBroken(item))
			{
				var tool = ToolStack.from(item);

				for (var variant : TConstructToolHelper.getRepairVariants(tool))
				{
					if (buildingLevel < MaterialHelper.getRequiredLevel(variant))
					{
						continue;
					}

					var materialId = variant.getId();
					var kitItem = repairMaterialsModule.get(materialId);

					if (kitItem.isEmpty())
					{
						continue;
					}

					repairKits.put(materialId, kitItem);
					toolSlots.put(materialId, i);
				}

			}

		}

		var level = worker.level;

		for (var materialId : repairKits.keySet())
		{
			var toolSlot = toolSlots.getInt(materialId);
			var kitItem = repairKits.get(materialId);
			Predicate<ItemStack> kitPredicate = stack -> ItemStack.isSameItemSameTags(stack, kitItem);

			var kitSlot = InventoryUtils.findFirstSlotInItemHandlerWith(inventory, kitPredicate);

			if (kitSlot > -1)
			{
				return CheckResult.repair(new Transaction(materialId, toolSlot, kitSlot));
			}

			var tool = inventory.getStackInSlot(toolSlot);

			if (InventoryUtils.hasItemInProvider(ai.building, kitPredicate))
			{
				var kitCount = TConstructToolHelper.getRepairCount(tool, kitItem, Integer.MAX_VALUE, level);
				return CheckResult.needsCurrently(new Tuple<>(kitPredicate, kitCount));
			}
			else if (!CitizenHelper.isRequested(citizenData, CustomizableDeliverable.TYPE_TOKEN, r -> r.getRequest().getObject() instanceof RepairKit kit && kit.getVariantId().getId().equals(materialId)))
			{
				var kitCount = TConstructToolHelper.getRepairCount(tool, kitItem, Integer.MAX_VALUE, level);
				citizenData.createRequestAsync(new CustomizableDeliverable(new RepairKit(materialId, kitItem, kitCount)));
			}

		}

		if (!any)
		{
			citizenData.createRequestAsync(new CustomizableDeliverable(new BrokenItem(ai)));
		}

		var hasTool = InventoryUtils.hasItemInProvider(ai.building, stack -> this.canRepair(repairMaterialsModule, stack));

		if (hasTool)
		{
			return CheckResult.needsCurrently(new Tuple<>(stack -> this.canRepair(repairMaterialsModule, stack), 1));
		}

		return null;
	}

	private boolean canRepair(RepairMaterialListModule module, ItemStack stack)
	{
		return TConstructToolHelper.isToolAndBroken(stack) && module.canRepair(ToolStack.from(stack));
	}

	public class Transaction extends RepairTransaction
	{
		private final MaterialId materialId;
		private final int brokenItemSlot;
		private final int kitSlot;

		public Transaction(MaterialId materialId, int brokenItemSlot, int kitSlot)
		{
			this.materialId = materialId;
			this.brokenItemSlot = brokenItemSlot;
			this.kitSlot = kitSlot;
		}

		@Override
		public boolean onHitting(@NotNull EntityContext context)
		{
			var inventory = context.getWorker().getInventoryCitizen();
			var brokenItem = inventory.getStackInSlot(this.brokenItemSlot);

			if (!TConstructToolSystem.INSTANCE.isTool(brokenItem) || !TConstructToolHelper.canRepair(brokenItem))
			{
				return false;
			}

			var tool = ToolStack.from(brokenItem);

			if (!TConstructToolHelper.getRepairVariants(tool).stream().map(i -> i.getId()).collect(Collectors.toSet()).contains(this.materialId))
			{
				return false;
			}

			var itemInSlot = inventory.getStackInSlot(this.kitSlot);
			var repairMaterialsModule = context.getAI().building.getModule(ModuleBuildingModules.REPAIR_MATERIALS);
			var kitItem = repairMaterialsModule.get(this.materialId);

			if (kitItem.isEmpty() || !ItemStack.isSameItemSameTags(itemInSlot, kitItem))
			{
				return false;
			}

			context.setHands(this.kitSlot, this.brokenItemSlot);
			return true;
		}

		@Override
		public @NotNull RepairResult onHitComplete(@NotNull EntityContext context)
		{
			var inventory = context.getWorker().getInventoryCitizen();
			var brokenItem = inventory.getStackInSlot(this.brokenItemSlot);
			var repairKit = inventory.getStackInSlot(this.kitSlot);

			var level = context.getWorker().level;
			var repairCount = TConstructToolHelper.getRepairCount(brokenItem, repairKit, repairKit.getCount(), level);
			var repaired = TConstructToolHelper.repair(brokenItem, ItemHandlerHelper.copyStackWithSize(repairKit, repairCount), level);
			inventory.setStackInSlot(this.brokenItemSlot, repaired);
			repairKit.shrink(repairCount);

			if (!TConstructToolHelper.canRepair(repaired))
			{
				return RepairResult.completed();
			}

			var repairMaterialsModule = context.getAI().building.getModule(ModuleBuildingModules.REPAIR_MATERIALS);
			var newKit = repairMaterialsModule.get(this.materialId);

			if (newKit.isEmpty())
			{
				return RepairResult.completed();
			}

			Predicate<ItemStack> kitPredicate = stack -> ItemStack.isSameItemSameTags(stack, newKit);
			var newKitSlot = InventoryUtils.findFirstSlotInItemHandlerWith(inventory, kitPredicate);

			if (newKitSlot == -1)
			{
				return RepairResult.completed();
			}

			return RepairResult.next(new Transaction(this.materialId, this.brokenItemSlot, newKitSlot));
		}

	}

}
