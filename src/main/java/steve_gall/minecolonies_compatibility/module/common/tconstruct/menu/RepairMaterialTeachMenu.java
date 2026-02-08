package steve_gall.minecolonies_compatibility.module.common.tconstruct.menu;

import java.util.Collections;
import java.util.List;

import com.minecolonies.api.colony.buildings.modules.IBuildingModule;
import com.minecolonies.api.colony.requestsystem.factory.IFactoryController;

import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import steve_gall.minecolonies_compatibility.api.common.inventory.IMenuRecipeValidator;
import steve_gall.minecolonies_compatibility.core.common.inventory.TeachContainer;
import steve_gall.minecolonies_compatibility.core.common.inventory.TeachInputSlot;
import steve_gall.minecolonies_compatibility.core.common.inventory.TeachRecipeMenu;
import steve_gall.minecolonies_compatibility.module.common.tconstruct.MaterialHelper;
import steve_gall.minecolonies_compatibility.module.common.tconstruct.RepairValue;
import steve_gall.minecolonies_compatibility.module.common.tconstruct.init.ModuleMenuTypes;

public class RepairMaterialTeachMenu extends TeachRecipeMenu<RepairValue>
{
	public static final int INVENTORY_X = 8;
	public static final int INVENTORY_Y = 84;

	public static final int RESULT_X = 17;
	public static final int RESULT_Y = 36;

	public RepairMaterialTeachMenu(int windowId, Inventory inventory, IBuildingModule module)
	{
		super(ModuleMenuTypes.REPAIR_MATERIAL_TEACH.get(), windowId, inventory, module);
		this.setup();
	}

	public RepairMaterialTeachMenu(int windowId, Inventory inventory, FriendlyByteBuf buffer)
	{
		super(ModuleMenuTypes.REPAIR_MATERIAL_TEACH.get(), windowId, inventory, buffer);
		this.setup();
	}

	private void setup()
	{
		this.addInventorySlots(INVENTORY_X, INVENTORY_Y);

		this.inputContainer = new TeachContainer(this, 1);
		this.inputSlots.add(this.addSlot(new TeachInputSlot(this.inputContainer, 0, RESULT_X, RESULT_Y)));
		this.resultContainer = new TeachContainer(this, 0);
	}

	@Override
	protected IMenuRecipeValidator<RepairValue> createRecipeValidator()
	{
		return new IMenuRecipeValidator<>()
		{
			@Override
			public CompoundTag serialize(IFactoryController controller, RepairValue recipe)
			{
				var tag = new CompoundTag();
				RepairValue.serializeTag(tag, recipe);
				return tag;
			}

			@Override
			public List<RepairValue> findAll(Container container, ServerPlayer player)
			{
				var item = container.getItem(0);
				var repairValue = MaterialHelper.getRepairValue(item, player.level());
				return repairValue == null ? Collections.emptyList() : Collections.singletonList(repairValue);
			}

			@Override
			public RepairValue deserialize(IFactoryController controller, CompoundTag tag)
			{
				return RepairValue.deserializeTag(tag);
			}

		};
	}

	@Override
	protected void onRecipeChanged(RegistryAccess registryAccess)
	{

	}

}
