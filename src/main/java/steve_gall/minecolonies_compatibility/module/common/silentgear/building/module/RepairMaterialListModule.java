package steve_gall.minecolonies_compatibility.module.common.silentgear.building.module;

import com.ldtteam.blockui.views.BOWindow;
import com.minecolonies.api.colony.buildings.modules.AbstractBuildingModule;
import com.minecolonies.api.colony.buildings.modules.AbstractBuildingModuleView;
import com.minecolonies.api.colony.buildings.modules.IBuildingEventsModule;
import com.minecolonies.api.colony.buildings.modules.IPersistentModule;
import com.minecolonies.api.util.InventoryUtils;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import net.silentchaos512.gear.gear.material.MaterialInstance;
import net.silentchaos512.gear.item.RepairKitItem;
import net.silentchaos512.gear.util.GearData;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.core.common.util.NBTUtils2;
import steve_gall.minecolonies_compatibility.module.client.silentgear.RepairMaterialListWindow;
import steve_gall.minecolonies_compatibility.module.common.silentgear.network.RepairMaterialCanUseHigherMessage;
import steve_gall.minecolonies_compatibility.module.common.silentgear.network.RepairMaterialMaterialMessage;

public class RepairMaterialListModule extends AbstractBuildingModule implements IPersistentModule, IBuildingEventsModule
{
	public static final String TAG_REPAIR_MATERIALS = "repairMaterials";
	public static final String TAG_MATERIAL_STORAGES = "materialStorages";
	public static final int INVENTORY_SLOTS = 27;
	public static final String TAG_INVENTORY = "repairKitInventory";
	public static final String TAG_CAN_USE_HIGHER = "canUseHigher";

	private final Int2ObjectMap<ItemStack> repairMaterials = new Int2ObjectOpenHashMap<>();
	private final Int2ObjectMap<CompoundTag> materialStorages = new Int2ObjectOpenHashMap<>();
	private final ItemStackHandler repairKitInventory;
	private boolean canUseHigher;

	public RepairMaterialListModule()
	{
		this.repairKitInventory = new ItemStackHandler(INVENTORY_SLOTS)
		{
			@Override
			public boolean isItemValid(int slot, ItemStack stack)
			{
				return stack.getItem() instanceof RepairKitItem;
			}

			@Override
			protected void onContentsChanged(int slot)
			{
				super.onContentsChanged(slot);
				onSlotChanged(slot);
			}
		};
		this.canUseHigher = false;
	}

	protected void onSlotChanged(int index)
	{
		this.markDirty();
	}

	public boolean canRepair(ItemStack tool)
	{
		var mainPartMaterial = GearData.getPrimaryMainMaterial(tool);

		if (mainPartMaterial == null)
		{
			return false;
		}

		return !this.findRepairMaterial(mainPartMaterial.getTier()).isEmpty();
	}

	public ItemStack findRepairMaterial(int tier)
	{
		var buildingLevel = this.getBuilding().getBuildingLevel();

		if (tier > buildingLevel)
		{
			return ItemStack.EMPTY;
		}

		if (this.canUseHigher)
		{
			for (var i = tier; i <= buildingLevel; i++)
			{
				var item = this.repairMaterials.get(i);

				if (item != null)
				{
					return item.copy();
				}

			}

			return ItemStack.EMPTY;
		}
		else
		{
			return this.getRepairMaterial(tier);
		}

	}

	public int sizeRepairMaterials()
	{
		return this.repairMaterials.size();
	}

	public ItemStack getRepairMaterial(int tier)
	{
		var item = this.repairMaterials.get(tier);
		return item == null ? ItemStack.EMPTY : item.copy();
	}

	public boolean addRepairMaterial(ItemStack item)
	{
		var material = MaterialInstance.from(item);

		if (material == null)
		{
			return false;
		}

		this.repairMaterials.put(material.getTier(), ItemHandlerHelper.copyStackWithSize(item, 1));
		this.markDirty();
		return true;
	}

	public boolean removeRepairMaterial(ItemStack item)
	{
		var material = MaterialInstance.from(item);

		if (material == null)
		{
			return false;
		}

		if (this.repairMaterials.remove(material.getTier()) != null)
		{
			this.markDirty();
			return true;
		}

		return false;
	}

	public CompoundTag getMaterialStorage(int tier)
	{
		var materialStorage = this.materialStorages.get(tier);
		return materialStorage == null ? new CompoundTag() : materialStorage.copy();
	}

	public void setMaterialStorage(int tier, CompoundTag materialStorage)
	{
		if (materialStorage == null)
		{
			this.materialStorages.remove(tier);
		}
		else
		{
			this.materialStorages.put(tier, materialStorage.copy());
		}

		this.markDirty();
	}

	@Override
	public void deserializeNBT(CompoundTag compound)
	{
		this.repairMaterials.clear();

		for (var item : NBTUtils2.deserializeList(compound, TAG_REPAIR_MATERIALS, ItemStack::of))
		{
			var material = MaterialInstance.from(item);

			if (material == null)
			{
				continue;
			}

			this.repairMaterials.put(material.getTier(), item);
		}

		this.materialStorages.clear();

		for (var tag : NBTUtils2.deserializeList(compound, TAG_MATERIAL_STORAGES, t -> t))
		{
			var tier = tag.getInt("tier");
			this.materialStorages.put(tier, tag.getCompound("tag"));
		}

		this.repairKitInventory.deserializeNBT(compound.getCompound(TAG_INVENTORY));
		this.canUseHigher = compound.getBoolean(TAG_CAN_USE_HIGHER);
	}

	@Override
	public void serializeNBT(CompoundTag compound)
	{
		NBTUtils2.serializeCollection(compound, TAG_REPAIR_MATERIALS, this.repairMaterials.values(), ItemStack::serializeNBT);
		NBTUtils2.serializeCollection(compound, TAG_MATERIAL_STORAGES, this.materialStorages.int2ObjectEntrySet(), entry ->
		{
			var tag = new CompoundTag();
			tag.putInt("tier", entry.getIntKey());
			tag.put("tag", entry.getValue());
			return tag;
		});

		compound.put(TAG_INVENTORY, this.repairKitInventory.serializeNBT());
		compound.putBoolean(TAG_CAN_USE_HIGHER, this.canUseHigher);
	}

	@Override
	public void serializeToView(FriendlyByteBuf buf)
	{
		super.serializeToView(buf);

		buf.writeCollection(this.repairMaterials.values(), FriendlyByteBuf::writeItem);
		buf.writeBoolean(this.canUseHigher);
	}

	@Override
	public void onDestroyed()
	{
		var pos = this.building.getID();
		InventoryUtils.dropItemHandler(this.repairKitInventory, this.building.getColony().getWorld(), pos.getX(), pos.getY(), pos.getZ());
	}

	public IItemHandlerModifiable getRepairKitInventory()
	{
		return this.repairKitInventory;
	}

	public boolean isCanUseHigher()
	{
		return this.canUseHigher;
	}

	public void setCanUseHigher(boolean canUseHigher)
	{
		if (this.canUseHigher != canUseHigher)
		{
			this.canUseHigher = canUseHigher;
			this.markDirty();
		}

	}

	public static class View extends AbstractBuildingModuleView
	{
		private final Int2ObjectMap<ItemStack> repairMaterials = new Int2ObjectOpenHashMap<>();
		private boolean canUseHigher;

		@Override
		public void deserialize(FriendlyByteBuf buf)
		{
			this.repairMaterials.clear();

			for (var item : buf.readList(FriendlyByteBuf::readItem))
			{
				var material = MaterialInstance.from(item);

				if (material == null)
				{
					continue;
				}

				this.repairMaterials.put(material.getTier(), item);
			}

			this.canUseHigher = buf.readBoolean();
		}

		public ItemStack getRepairMaterial(int tier)
		{
			var item = this.repairMaterials.get(tier);
			return item == null ? ItemStack.EMPTY : item.copy();
		}

		public void addRepairMaterial(ItemStack item)
		{
			var material = MaterialInstance.from(item);

			if (material != null)
			{
				this.repairMaterials.put(material.getTier(), ItemHandlerHelper.copyStackWithSize(item, 1));
			}

			MineColoniesCompatibility.network().sendToServer(RepairMaterialMaterialMessage.add(this, item));
		}

		public void removeRepairMaterial(ItemStack item)
		{
			var material = MaterialInstance.from(item);

			if (material != null)
			{
				this.repairMaterials.remove(material.getTier());
			}

			MineColoniesCompatibility.network().sendToServer(RepairMaterialMaterialMessage.remove(this, item));
		}

		@Override
		public BOWindow getWindow()
		{
			return new RepairMaterialListWindow(this, MineColoniesCompatibility.rl("gui/layouthuts/layoutsilentgearrepairmateriallist.xml").toString());
		}

		public boolean isCanUseHigher()
		{
			return this.canUseHigher;
		}

		public void setCanUseHigher(boolean canUseHigher)
		{
			this.canUseHigher = canUseHigher;
			MineColoniesCompatibility.network().sendToServer(new RepairMaterialCanUseHigherMessage(this, canUseHigher));
		}

		@Override
		public String getIcon()
		{
			return "silentgear_repair_materials";
		}

		@Override
		public String getDesc()
		{
			return "com.minecolonies.coremod.gui.workerhuts.silentgear_repair_material_list";
		}

	}

}
