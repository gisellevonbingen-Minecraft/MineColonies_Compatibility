package steve_gall.minecolonies_compatibility.module.common.silentgear.building.module;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import com.ldtteam.blockui.views.BOWindow;
import com.minecolonies.api.colony.buildings.modules.AbstractBuildingModule;
import com.minecolonies.api.colony.buildings.modules.AbstractBuildingModuleView;
import com.minecolonies.api.colony.buildings.modules.IBuildingEventsModule;
import com.minecolonies.api.colony.buildings.modules.IPersistentModule;
import com.minecolonies.api.util.InventoryUtils;
import com.minecolonies.api.util.Tuple;
import com.minecolonies.api.util.Utils;

import it.unimi.dsi.fastutil.objects.Object2FloatMap;
import it.unimi.dsi.fastutil.objects.Object2FloatOpenHashMap;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.network.PacketDistributor;
import net.silentchaos512.gear.gear.material.MaterialInstance;
import net.silentchaos512.gear.item.RepairKitItem;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.core.common.util.NBTUtils2;
import steve_gall.minecolonies_compatibility.module.client.silentgear.RepairMaterialListWindow;
import steve_gall.minecolonies_compatibility.module.common.silentgear.SilentGearToolHelper;
import steve_gall.minecolonies_compatibility.module.common.silentgear.network.RepairMaterialMaterialMessage;
import steve_gall.minecolonies_tweaks.core.common.item.ItemSerializationHelper;

public class RepairMaterialListModule extends AbstractBuildingModule implements IPersistentModule, IBuildingEventsModule
{
	public static final String TAG_REPAIR_MATERIALS = "repairMaterials";
	public static final String TAG_MATERIAL_STORAGES = "materialStorages";
	public static final int INVENTORY_SLOTS = 27;
	public static final String TAG_INVENTORY = "repairKitInventory";

	private final Map<MaterialInstance, ItemStack> repairMaterials = new HashMap<>();
	private final Object2FloatMap<MaterialInstance> materialStorages = new Object2FloatOpenHashMap<>();
	private final ItemStackHandler repairKitInventory;

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
	}

	protected void onSlotChanged(int index)
	{
		this.markDirty();
	}

	public boolean canRepair(ItemStack tool)
	{
		return !this.getRepairMaterial(SilentGearToolHelper.getPrimaryMaterial(tool)).isEmpty();
	}

	public int sizeRepairMaterials()
	{
		return this.repairMaterials.size();
	}

	public ItemStack getRepairMaterial(MaterialInstance material)
	{
		if (material == null)
		{
			return ItemStack.EMPTY;
		}

		var item = this.repairMaterials.get(material);
		return item == null ? ItemStack.EMPTY : item.copy();
	}

	public boolean addRepairMaterial(ItemStack item)
	{
		var material = MaterialInstance.from(item);

		if (material == null)
		{
			return false;
		}

		this.repairMaterials.put(material, item.copyWithCount(1));
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

		if (this.repairMaterials.remove(material) != null)
		{
			this.markDirty();
			return true;
		}

		return false;
	}

	public float getMaterialStorage(MaterialInstance material)
	{
		if (material == null)
		{
			return 0.0F;
		}

		return this.materialStorages.getFloat(material);
	}

	public void setMaterialStorage(MaterialInstance material, float amount)
	{
		if (material == null)
		{
			return;
		}

		if (amount <= 0.0F)
		{
			this.materialStorages.removeFloat(material);
		}
		else
		{
			this.materialStorages.put(material, amount);
		}

		this.markDirty();
	}

	@Override
	public void deserializeNBT(HolderLookup.Provider provider, CompoundTag compound)
	{
		this.repairMaterials.clear();

		for (var item : NBTUtils2.deserializeList(compound, TAG_REPAIR_MATERIALS, ItemSerializationHelper.deserializerTag(provider)))
		{
			var material = MaterialInstance.from(item);

			if (material == null)
			{
				continue;
			}

			this.repairMaterials.put(material, item);
		}

		this.materialStorages.clear();

		for (var tag : NBTUtils2.deserializeList(compound, TAG_MATERIAL_STORAGES, t -> t))
		{
			var material = Utils.deserializeCodecMess(MaterialInstance.CODEC, provider, tag.get("material"));
			this.materialStorages.put(material, tag.getFloat("amount"));
		}

		this.repairKitInventory.deserializeNBT(provider, compound.getCompound(TAG_INVENTORY));
	}

	@Override
	public void serializeNBT(HolderLookup.Provider provider, CompoundTag compound)
	{
		NBTUtils2.serializeCollection(compound, TAG_REPAIR_MATERIALS, this.repairMaterials.values(), ItemSerializationHelper.serializerTag(provider));
		NBTUtils2.serializeCollection(compound, TAG_MATERIAL_STORAGES, this.materialStorages.object2FloatEntrySet(), entry ->
		{
			var tag = new CompoundTag();
			tag.put("material", Utils.serializeCodecMess(MaterialInstance.CODEC, provider, entry.getKey()));
			tag.putFloat("amount", entry.getFloatValue());
			return tag;
		});

		compound.put(TAG_INVENTORY, this.repairKitInventory.serializeNBT(provider));
	}

	@Override
	public void serializeToView(RegistryFriendlyByteBuf buf)
	{
		super.serializeToView(buf);

		buf.writeCollection(this.repairMaterials.values(), ItemSerializationHelper::serialize);
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

	public static class View extends AbstractBuildingModuleView
	{
		public static final Component DESC = Component.translatable("com.minecolonies.coremod.gui.workerhuts.silentgear_repair_material_list");

		private final Map<MaterialInstance, ItemStack> repairMaterials = new HashMap<>();
		private int updateCounter = 0;

		@Override
		public void deserialize(RegistryFriendlyByteBuf buf)
		{
			this.repairMaterials.clear();

			for (var item : buf.readList(ItemSerializationHelper::deserialize))
			{
				var material = MaterialInstance.from(item);

				if (material == null)
				{
					continue;
				}

				this.repairMaterials.put(material, item);
			}

			this.updateCounter++;
		}

		public Stream<Tuple<MaterialInstance, ItemStack>> streamRepairMaterial()
		{
			return this.repairMaterials.entrySet().stream().map(entry -> new Tuple<>(entry.getKey(), entry.getValue()));
		}

		public ItemStack getRepairMaterial(MaterialInstance material)
		{
			if (material == null)
			{
				return ItemStack.EMPTY;
			}

			var item = this.repairMaterials.get(material);
			return item == null ? ItemStack.EMPTY : item.copy();
		}

		public void addRepairMaterial(ItemStack item)
		{
			var material = MaterialInstance.from(item);

			if (material != null)
			{
				this.repairMaterials.put(material, item.copyWithCount(1));
			}

			PacketDistributor.sendToServer(RepairMaterialMaterialMessage.add(this, item));
			this.updateCounter++;
		}

		public void removeRepairMaterial(ItemStack item)
		{
			var material = MaterialInstance.from(item);

			if (material != null)
			{
				this.repairMaterials.remove(material);
			}

			PacketDistributor.sendToServer(RepairMaterialMaterialMessage.remove(this, item));
			this.updateCounter++;
		}

		@Override
		public BOWindow getWindow()
		{
			return new RepairMaterialListWindow(this, MineColoniesCompatibility.rl("gui/layouthuts/layoutsilentgearrepairmateriallist.xml"));
		}

		@Override
		public String getIcon()
		{
			return "silentgear_repair_materials";
		}

		@Override
		public Component getDesc()
		{
			return DESC;
		}

		public int getUpdateCounter()
		{
			return this.updateCounter;
		}

	}

}
