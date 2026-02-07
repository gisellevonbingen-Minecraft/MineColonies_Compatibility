package steve_gall.minecolonies_compatibility.module.common.tconstruct.building.module;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.ldtteam.blockui.views.BOWindow;
import com.minecolonies.api.colony.buildings.modules.AbstractBuildingModule;
import com.minecolonies.api.colony.buildings.modules.AbstractBuildingModuleView;
import com.minecolonies.api.colony.buildings.modules.IPersistentModule;
import com.minecolonies.api.util.Tuple;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import slimeknights.tconstruct.library.materials.definition.MaterialId;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.core.common.util.NBTUtils2;
import steve_gall.minecolonies_compatibility.module.client.tconstruct.RepairMaterialListWindow;
import steve_gall.minecolonies_compatibility.module.common.tconstruct.MaterialHelper;
import steve_gall.minecolonies_compatibility.module.common.tconstruct.network.RepairMaterialUpdateMessage;

public class RepairMaterialListModule extends AbstractBuildingModule implements IPersistentModule
{
	public static final String TAG_REPAIR_MATERIALS = MineColoniesCompatibility.rl("tconstruct_repair_materials").toString();

	private final Map<MaterialId, ItemStack> repairMaterials;

	public RepairMaterialListModule()
	{
		this.repairMaterials = new HashMap<>();
	}

	public Set<Map.Entry<MaterialId, ItemStack>> entrySet()
	{
		return this.repairMaterials.entrySet();
	}

	public ItemStack get(MaterialId materialId)
	{
		var item = this.repairMaterials.get(materialId);
		return item == null ? ItemStack.EMPTY : item;
	}

	public boolean add(ItemStack item)
	{
		var materialValue = MaterialHelper.getMaterialValue(item, this.building.getColony().getWorld());

		if (materialValue == null)
		{
			return false;
		}

		this.repairMaterials.put(materialValue.getMaterial().getId(), item);
		this.markDirty();
		return true;
	}

	public boolean remove(MaterialId materialId)
	{
		if (this.repairMaterials.remove(materialId) == null)
		{
			return false;
		}

		this.markDirty();
		return true;
	}

	@Override
	public void deserializeNBT(CompoundTag compound)
	{
		this.repairMaterials.clear();

		for (var tuple : NBTUtils2.deserializeList(compound, TAG_REPAIR_MATERIALS, RepairMaterialListModule::deserializeTag))
		{
			this.repairMaterials.put(tuple.getA(), tuple.getB());
		}

	}

	@Override
	public void serializeNBT(CompoundTag compound)
	{
		NBTUtils2.serializeCollection(compound, TAG_REPAIR_MATERIALS, this.repairMaterials.entrySet(), RepairMaterialListModule::serializeTag);
	}

	@Override
	public void serializeToView(FriendlyByteBuf buf)
	{
		super.serializeToView(buf);

		buf.writeCollection(this.repairMaterials.entrySet(), RepairMaterialListModule::serializeBuffer);
	}

	public static class View extends AbstractBuildingModuleView
	{
		public static final String DESC = "com.minecolonies.coremod.gui.workerhuts.tconstruct_repair_materials";

		private final Map<MaterialId, ItemStack> repairMaterials;
		private int updateCounter = 0;

		public View()
		{
			this.repairMaterials = new HashMap<>();
		}

		public Set<Map.Entry<MaterialId, ItemStack>> entrySet()
		{
			return this.repairMaterials.entrySet();
		}

		public ItemStack get(MaterialId materialId)
		{
			var item = this.repairMaterials.get(materialId);
			return item == null ? ItemStack.EMPTY : item;
		}

		public boolean add(ItemStack item)
		{
			var materialValue = MaterialHelper.getMaterialValue(item, this.buildingView.getColony().getWorld());

			if (materialValue == null)
			{
				return false;
			}

			MineColoniesCompatibility.network().sendToServer(RepairMaterialUpdateMessage.add(this, item));
			this.repairMaterials.put(materialValue.getMaterial().getId(), item);
			this.updateCounter++;
			return true;
		}

		public boolean remove(MaterialId materialId)
		{
			MineColoniesCompatibility.network().sendToServer(RepairMaterialUpdateMessage.remove(this, materialId));

			if (this.repairMaterials.remove(materialId) == null)
			{
				return false;
			}

			this.updateCounter++;
			return true;
		}

		@Override
		public void deserialize(FriendlyByteBuf buf)
		{
			this.repairMaterials.clear();

			for (var tuple : buf.readCollection(ArrayList::new, RepairMaterialListModule::deserializeBuffer))
			{
				this.repairMaterials.put(tuple.getA(), tuple.getB());
			}

			this.updateCounter++;
		}

		@Override
		public BOWindow getWindow()
		{
			return new RepairMaterialListWindow(MineColoniesCompatibility.rl("gui/layouthuts/layoutstconstructrepairmateriallist.xml").toString(), this);
		}

		@Override
		public String getIcon()
		{
			return "tconstruct_repair_materials";
		}

		@Override
		public String getDesc()
		{
			return DESC;
		}

		public int getUpdateCounter()
		{
			return this.updateCounter;
		}

	}

	private static Tuple<MaterialId, ItemStack> deserializeTag(CompoundTag compound)
	{
		var key = new MaterialId(compound.getString("key"));
		var value = ItemStack.of(compound.getCompound("value"));
		return new Tuple<>(key, value);
	}

	private static CompoundTag serializeTag(Map.Entry<MaterialId, ItemStack> entry)
	{
		var compound = new CompoundTag();
		compound.putString("key", entry.getKey().toString());
		compound.put("value", entry.getValue().serializeNBT());
		return compound;
	}

	private static Tuple<MaterialId, ItemStack> deserializeBuffer(FriendlyByteBuf buf)
	{
		var key = new MaterialId(buf.readUtf());
		var value = buf.readItem();
		return new Tuple<>(key, value);
	}

	private static void serializeBuffer(FriendlyByteBuf buf, Map.Entry<MaterialId, ItemStack> entry)
	{
		buf.writeUtf(entry.getKey().toString());
		buf.writeItem(entry.getValue());
	}

}
