package steve_gall.minecolonies_compatibility.module.common.silentgear;

import java.util.function.Predicate;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.minecolonies.api.colony.requestsystem.request.RequestState;
import com.minecolonies.api.colony.requestsystem.requestable.Stack;
import com.minecolonies.api.util.InventoryUtils;
import com.minecolonies.api.util.Tuple;
import com.minecolonies.api.util.constant.TypeConstants;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.silentchaos512.gear.gear.material.MaterialInstance;
import net.silentchaos512.gear.gear.part.RepairContext;
import net.silentchaos512.gear.item.RepairKitItem;
import net.silentchaos512.gear.util.GearData;
import steve_gall.minecolonies_compatibility.api.common.repair.CustomizedRepair;
import steve_gall.minecolonies_compatibility.api.common.repair.EntityContext;
import steve_gall.minecolonies_compatibility.api.common.repair.RepairTransaction;
import steve_gall.minecolonies_compatibility.core.common.colony.CitizenHelper;
import steve_gall.minecolonies_compatibility.module.common.ModuleManager;
import steve_gall.minecolonies_compatibility.module.common.silentgear.building.module.RepairMaterialListModule;
import steve_gall.minecolonies_compatibility.module.common.silentgear.init.ModuleBuildingModules;
import steve_gall.minecolonies_tweaks.api.common.requestsystem.CustomizableDeliverable;

public class SilentGearRepair extends CustomizedRepair
{
	@Override
	public @NotNull ResourceLocation getId()
	{
		return new ResourceLocation(ModuleManager.SILENTGEAR.getModId(), "repair_kit");
	}

	@Override
	public @Nullable CheckResult check(@NotNull EntityContext context)
	{
		var ai = context.getAI();
		var worker = context.getWorker();
		var inventory = worker.getInventoryCitizen();
		var citizenData = worker.getCitizenData();
		var module = ai.building.getModule(ModuleBuildingModules.REPAIR_MATERIALS);
		var materialsEmpty = module.sizeRepairMaterials() == 0;
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

		var moduleInventory = module.getRepairKitInventory();
		var kitSlot = -1;
		var kitEfficiency = 0.0F;

		for (var i = 0; i < moduleInventory.getSlots(); i++)
		{
			if (moduleInventory.getStackInSlot(i).getItem() instanceof RepairKitItem item)
			{
				var efficiency = item.getRepairEfficiency(RepairContext.Type.QUICK);

				if (kitSlot == -1 || kitEfficiency > efficiency)
				{
					kitSlot = i;
					kitEfficiency = efficiency;
				}

			}

		}

		if (kitSlot == -1)
		{
			return null;
		}

		var kitStackFirst = kitSlot == -1 ? ItemStack.EMPTY : moduleInventory.getStackInSlot(kitSlot);
		var repairMaterials = new Int2ObjectOpenHashMap<ItemStack>();

		for (var i = 0; i < inventory.getSlots(); i++)
		{
			var item = inventory.getStackInSlot(i);

			if (SilentGearToolHelper.isToolAndBroken(item))
			{
				var repairMaterial = module.findRepairMaterial(GearData.getTier(item));

				if (repairMaterial.isEmpty())
				{
					continue;
				}

				repairMaterials.put(i, repairMaterial);
			}

		}

		var level = worker.level();

		for (var entry : repairMaterials.int2ObjectEntrySet())
		{
			var toolSlot = entry.getIntKey();
			var tool = inventory.getStackInSlot(toolSlot);
			var material = entry.getValue();
			var tier = MaterialInstance.from(material).getTier();
			var kitStack = kitStackFirst.copy();
			setMaterialStorage(kitStack, module.getMaterialStorage(tier));
			var repairMaterialCount = SilentGearToolHelper.getRepairMaterialCount(tool, kitStack, material, Integer.MAX_VALUE, level);

			if (repairMaterialCount == 0)
			{
				return CheckResult.repair(new Transaction(toolSlot, -1, kitStack, tier));
			}

			Predicate<ItemStack> materialPredicate = stack -> ItemStack.isSameItemSameTags(stack, material);
			var materialSlot = InventoryUtils.findFirstSlotInItemHandlerWith(inventory, materialPredicate);

			if (materialSlot > -1)
			{
				return CheckResult.repair(new Transaction(toolSlot, materialSlot, kitStack, tier));
			}
			else if (InventoryUtils.hasItemInProvider(ai.building, materialPredicate))
			{
				return CheckResult.needsCurrently(new Tuple<>(materialPredicate, repairMaterialCount));
			}
			else if (!CitizenHelper.isRequested(citizenData, TypeConstants.DELIVERABLE, r -> r.getRequest() instanceof Stack stack && ItemStack.isSameItemSameTags(stack.getStack(), material)))
			{
				citizenData.createRequestAsync(new Stack(material, repairMaterialCount, 1));
			}

		}

		if (!any)
		{
			citizenData.createRequestAsync(new CustomizableDeliverable(new BrokenItem(ai)));
		}

		var hasTool = InventoryUtils.hasItemInProvider(ai.building, stack -> this.canRepair(module, stack));

		if (hasTool)
		{
			return CheckResult.needsCurrently(new Tuple<>(stack -> this.canRepair(module, stack), 1));
		}

		return null;
	}

	private static CompoundTag getMaterialStorage(ItemStack stack)
	{
		var materialStorage = stack.getTagElement("Storage");
		return materialStorage == null ? new CompoundTag() : materialStorage.copy();
	}

	private static void setMaterialStorage(ItemStack stack, CompoundTag materialStorage)
	{
		stack.addTagElement("Storage", materialStorage.copy());
	}

	private boolean canRepair(RepairMaterialListModule module, ItemStack stack)
	{
		return SilentGearToolHelper.isToolAndBroken(stack) && module.canRepair(stack);
	}

	public class Transaction extends RepairTransaction
	{
		private final int brokenItemSlot;
		private final int materialSlot;
		private final ItemStack repairKit;
		private final int tier;

		public Transaction(int brokenItemSlot, int materialSlot, ItemStack repairKit, int tier)
		{
			this.brokenItemSlot = brokenItemSlot;
			this.repairKit = repairKit;
			this.materialSlot = materialSlot;
			this.tier = tier;
		}

		@Override
		public boolean onHitting(EntityContext context)
		{
			var inventory = context.getWorker().getInventoryCitizen();
			var brokenItem = inventory.getStackInSlot(this.brokenItemSlot);

			if (!SilentGearToolSystem.INSTANCE.isTool(brokenItem) || !SilentGearToolHelper.canRepair(brokenItem))
			{
				return false;
			}

			var repairMaterialsModule = context.getAI().building.getModule(ModuleBuildingModules.REPAIR_MATERIALS);

			if (this.materialSlot != -1)
			{
				var material = inventory.getStackInSlot(this.materialSlot);

				if (!ItemStack.isSameItemSameTags(repairMaterialsModule.getRepairMaterial(this.tier), material))
				{
					return false;
				}

			}

			context.setHands(this.materialSlot == -1 ? this.brokenItemSlot : this.materialSlot, this.materialSlot == -1 ? -1 : this.brokenItemSlot);
			return true;
		}

		@Override
		public RepairResult onHitComplete(EntityContext context)
		{
			var inventory = context.getWorker().getInventoryCitizen();
			var brokenItem = inventory.getStackInSlot(this.brokenItemSlot);
			var material = this.materialSlot == -1 ? ItemStack.EMPTY : inventory.getStackInSlot(this.materialSlot);
			var materialFirst = material.copy();
			var module = context.getAI().building.getModule(ModuleBuildingModules.REPAIR_MATERIALS);
			var repairKit = this.repairKit.copy();
			setMaterialStorage(repairKit, module.getMaterialStorage(this.tier));

			var level = context.getWorker().level();
			var repairCount = this.materialSlot == -1 ? 1 : material.getCount();

			for (var i = 0; i < repairCount; i++)
			{
				var repairResult = SilentGearToolHelper.fillAndRepair(brokenItem, repairKit, material, level);

				if (!repairResult.success())
				{
					break;
				}

				brokenItem = repairResult.tool();
				repairKit = repairResult.repairKit();
				inventory.setStackInSlot(this.brokenItemSlot, brokenItem);
				module.setMaterialStorage(this.tier, getMaterialStorage(repairKit));
				material.shrink(1);

				if (!SilentGearToolHelper.canRepair(brokenItem))
				{
					return RepairResult.completed();
				}

			}

			Predicate<ItemStack> kitPredicate = stack -> ItemStack.isSameItemSameTags(stack, materialFirst);
			var newNaterialSlot = InventoryUtils.findFirstSlotInItemHandlerWith(inventory, kitPredicate);

			if (newNaterialSlot == -1)
			{
				return RepairResult.completed();
			}

			return RepairResult.next(new Transaction(this.brokenItemSlot, newNaterialSlot, repairKit, this.tier));
		}

	}

}
