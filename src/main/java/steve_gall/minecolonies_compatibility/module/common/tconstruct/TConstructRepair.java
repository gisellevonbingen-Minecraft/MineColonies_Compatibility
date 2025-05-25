package steve_gall.minecolonies_compatibility.module.common.tconstruct;

import java.util.HashMap;
import java.util.function.Predicate;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.minecolonies.api.colony.requestsystem.request.RequestState;
import com.minecolonies.api.util.InventoryUtils;
import com.minecolonies.api.util.Tuple;

import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import slimeknights.tconstruct.library.materials.definition.MaterialVariantId;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import steve_gall.minecolonies_compatibility.api.common.repair.CustomizedRepair;
import steve_gall.minecolonies_compatibility.api.common.repair.EntityContext;
import steve_gall.minecolonies_compatibility.api.common.repair.RepairTransaction;
import steve_gall.minecolonies_compatibility.core.common.colony.CitizenHelper;
import steve_gall.minecolonies_compatibility.module.common.ModuleManager;
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
		var registryAccess = worker.level().registryAccess();

		var variantIds = new HashMap<String, MaterialVariantId>();
		var toolSlots = new Object2IntOpenHashMap<String>();

		for (var i = 0; i < inventory.getSlots(); i++)
		{
			var item = inventory.getStackInSlot(i);

			if (TConstructToolHelper.isToolAndBroken(item))
			{
				var tool = ToolStack.from(item);

				for (var variantId : TConstructToolHelper.getRepairVariantIds(tool))
				{
					variantIds.put(variantId.toString(), variantId);
					toolSlots.put(variantId.toString(), i);
				}

			}

		}

		for (var key : variantIds.keySet())
		{
			var variantId = variantIds.get(key);
			var toolSlot = toolSlots.getInt(key);
			Predicate<ItemStack> kitPredicate = stack -> RepairKit.isRepairKitItem(stack, variantId);
			var kitSlot = InventoryUtils.findFirstSlotInItemHandlerWith(inventory, kitPredicate);
			var tool = inventory.getStackInSlot(toolSlot);
			var kitCount = TConstructToolHelper.getRepairCount(tool, variantId, registryAccess);

			if (kitSlot > -1)
			{
				return CheckResult.repair(new Transaction(variantId, toolSlot, kitSlot));
			}
			else if (InventoryUtils.hasItemInProvider(ai.building, kitPredicate))
			{
				return CheckResult.needsCurrently(new Tuple<>(kitPredicate, kitCount));
			}
			else if (!CitizenHelper.isRequested(citizenData, CustomizableDeliverable.TYPE_TOKEN, r -> r.getRequest().getObject() instanceof RepairKit kit && kit.getVariantId().matchesVariant(variantId)))
			{
				citizenData.createRequestAsync(new CustomizableDeliverable(new RepairKit(variantId, kitCount)));
			}

		}

		var any = false;

		for (var request : CitizenHelper.getRequests(citizenData, CustomizableDeliverable.TYPE_TOKEN, r -> r.getRequest().getObject() instanceof BrokenItem))
		{
			if (((BrokenItem) request.getRequest().getObject()).getAI() == null)
			{
				citizenData.getColony().getRequestManager().updateRequestState(request.getId(), RequestState.CANCELLED);
			}
			else
			{
				any = true;
			}

		}

		if (!any)
		{
			citizenData.createRequestAsync(new CustomizableDeliverable(new BrokenItem(ai)));
		}

		var hasTool = InventoryUtils.hasItemInProvider(ai.building, TConstructToolHelper::isToolAndBroken);

		if (hasTool)
		{
			return CheckResult.needsCurrently(new Tuple<>(TConstructToolHelper::isToolAndBroken, 1));
		}

		return null;
	}

	public class Transaction extends RepairTransaction
	{
		private final MaterialVariantId variantId;
		private final int brokenItemSlot;
		private final int kitSlot;

		public Transaction(MaterialVariantId variantId, int brokenItemSlot, int kitSlot)
		{
			this.variantId = variantId;
			this.brokenItemSlot = brokenItemSlot;
			this.kitSlot = kitSlot;
		}

		@Override
		public boolean onHitting(@NotNull EntityContext context)
		{
			var inventory = context.getWorker().getInventoryCitizen();
			var brokenItem = inventory.getStackInSlot(this.brokenItemSlot);

			if (!TConstructToolHelper.canRepair(brokenItem))
			{
				return false;
			}

			var tool = ToolStack.from(brokenItem);
			var variantIds = TConstructToolHelper.getRepairVariantIds(tool);

			if (!MaterialHelper.anyMatchesVariantId(variantIds, this.variantId))
			{
				return false;
			}

			var repairKit = inventory.getStackInSlot(this.kitSlot);

			if (!RepairKit.isRepairKitItem(repairKit, this.variantId))
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

			var repaired = TConstructToolHelper.repair(brokenItem, repairKit, context.getWorker().level().registryAccess());
			inventory.setStackInSlot(this.brokenItemSlot, repaired);
			repairKit.shrink(1);

			Predicate<ItemStack> kitPredicate = stack -> RepairKit.isRepairKitItem(stack, this.variantId);
			var newKitSlot = InventoryUtils.findFirstSlotInItemHandlerWith(inventory, kitPredicate);

			if (newKitSlot == -1 || !TConstructToolHelper.canRepair(repaired))
			{
				return RepairResult.completed();
			}
			else
			{
				return RepairResult.next(new Transaction(this.variantId, this.brokenItemSlot, newKitSlot));
			}

		}

	}

}
