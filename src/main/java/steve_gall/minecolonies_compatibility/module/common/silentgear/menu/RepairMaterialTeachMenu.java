package steve_gall.minecolonies_compatibility.module.common.silentgear.menu;

import java.util.Collections;
import java.util.List;

import com.minecolonies.api.colony.buildings.modules.IBuildingModule;
import com.minecolonies.api.colony.requestsystem.factory.IFactoryController;
import com.minecolonies.api.util.constant.Constants;
import com.minecolonies.api.util.constant.TranslationConstants;

import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.silentchaos512.gear.gear.material.MaterialInstance;
import steve_gall.minecolonies_compatibility.api.common.inventory.IMenuRecipeValidator;
import steve_gall.minecolonies_compatibility.core.common.inventory.TeachContainer;
import steve_gall.minecolonies_compatibility.core.common.inventory.TeachInputSlot;
import steve_gall.minecolonies_compatibility.core.common.inventory.TeachRecipeMenu;
import steve_gall.minecolonies_compatibility.module.common.silentgear.init.ModuleMenuTypes;

public class RepairMaterialTeachMenu extends TeachRecipeMenu<ItemStack>
{
	public static final int INVENTORY_X = 8;
	public static final int INVENTORY_Y = 84;

	public static final int RESULT_X = 17;
	public static final int RESULT_Y = 36;

	private final int buildingLevel;

	public RepairMaterialTeachMenu(int windowId, Inventory inventory, IBuildingModule module)
	{
		super(ModuleMenuTypes.REPAIR_MATERIAL_TEACH.get(), windowId, inventory, module);
		this.buildingLevel = module.getBuilding().getBuildingLevel();
		this.setup();
	}

	public RepairMaterialTeachMenu(int windowId, Inventory inventory, FriendlyByteBuf buffer)
	{
		super(ModuleMenuTypes.REPAIR_MATERIAL_TEACH.get(), windowId, inventory, buffer);
		this.buildingLevel = buffer.readInt();
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
	protected IMenuRecipeValidator<ItemStack> createRecipeValidator()
	{
		return new IMenuRecipeValidator<>()
		{
			@Override
			public List<ItemStack> findAll(Container container, ServerPlayer player)
			{
				var stack = container.getItem(0);
				var material = MaterialInstance.from(stack);
				return material != null ? Collections.singletonList(stack.copy()) : Collections.emptyList();
			}

			@Override
			public ItemStack deserialize(IFactoryController controller, CompoundTag tag)
			{
				return ItemStack.of(tag);
			}

			@Override
			public CompoundTag serialize(IFactoryController controller, ItemStack stack)
			{
				return stack.serializeNBT();
			}
		};
	}

	@Override
	public Component getRecipeError(ItemStack stack)
	{
		var material = MaterialInstance.from(stack);
		var requiredLevel = material.getTier();
		var error = this.testRequiredLevel(requiredLevel);

		if (error != null)
		{
			return error;
		}

		return super.getRecipeError(stack);
	}

	public Component testRequiredLevel(int requiredLevel)
	{
		if (this.buildingLevel < requiredLevel)
		{
			var maxLevel = Constants.MAX_BUILDING_LEVEL;

			if (requiredLevel == maxLevel)
			{
				return Component.translatable(TranslationConstants.PARTIAL_JEI_INFO + "onelevelrestriction.tip", requiredLevel);
			}
			else
			{
				return Component.translatable(TranslationConstants.PARTIAL_JEI_INFO + "levelrestriction.tip", requiredLevel, maxLevel);
			}

		}

		return null;
	}

	@Override
	protected void onRecipeChanged(RegistryAccess registryAccess)
	{
		if (this.recipe == null)
		{
			return;
		}

	}

}
