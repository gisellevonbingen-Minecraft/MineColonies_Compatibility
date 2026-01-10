package steve_gall.minecolonies_compatibility.module.common.farmersdelight.menu;

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
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import steve_gall.minecolonies_compatibility.api.common.inventory.IMenuRecipeValidator;
import steve_gall.minecolonies_compatibility.core.common.inventory.TeachContainer;
import steve_gall.minecolonies_compatibility.core.common.inventory.TeachInputSlot;
import steve_gall.minecolonies_compatibility.core.common.inventory.TeachRecipeMenu;
import steve_gall.minecolonies_compatibility.core.common.inventory.TeachResultSlot;
import steve_gall.minecolonies_compatibility.module.common.farmersdelight.crafting.PlatingRecipeStorage;
import steve_gall.minecolonies_compatibility.module.common.farmersdelight.init.ModuleMenuTypes;
import vectorwing.farmersdelight.common.block.FeastBlock;

public class PlatingTeachMenu extends TeachRecipeMenu<PlatingRecipeStorage>
{
	public static final int INVENTORY_X = 8;
	public static final int INVENTORY_Y = 84;

	public static final int CRAFTING_X = 52;
	public static final int CRAFTING_Y = 26;

	public static final int RESULT_X = 108;
	public static final int RESULT_Y = 26;
	public static final int CONTAINER_X = 80;
	public static final int CONTAINER_Y = 45;

	public PlatingTeachMenu(int windowId, Inventory inventory, IBuildingModule module)
	{
		super(ModuleMenuTypes.PLATING_TEACH.get(), windowId, inventory, module);
		this.setup();
	}

	public PlatingTeachMenu(int windowId, Inventory inventory, FriendlyByteBuf buffer)
	{
		super(ModuleMenuTypes.PLATING_TEACH.get(), windowId, inventory, buffer);
		this.setup();
	}

	private void setup()
	{
		this.addInventorySlots(INVENTORY_X, INVENTORY_Y);

		this.inputContainer = new TeachContainer(this, 1);
		this.inputSlots.add(this.addSlot(new TeachInputSlot(this.inputContainer, 0, CRAFTING_X, CRAFTING_Y)));

		this.resultContainer = new TeachContainer(this, 2);
		this.resultSlots.add(this.addSlot(new TeachResultSlot(this.resultContainer, 0, RESULT_X, RESULT_Y)));
		this.resultSlots.add(this.addSlot(new TeachResultSlot(this.resultContainer, 1, CONTAINER_X, CONTAINER_Y)));
	}

	@Override
	protected IMenuRecipeValidator<PlatingRecipeStorage> createRecipeValidator()
	{
		return new IMenuRecipeValidator<>()
		{
			@Override
			public List<PlatingRecipeStorage> findAll(Container container, ServerPlayer player)
			{
				if (container.getItem(0).getItem() instanceof BlockItem item && item.getBlock() instanceof FeastBlock block)
				{
					var recipe = new PlatingRecipeStorage(block);

					if (!recipe.getPrimaryOutput().isEmpty())
					{
						return Collections.singletonList(recipe);
					}

				}

				return Collections.emptyList();
			}

			@Override
			public PlatingRecipeStorage deserialize(IFactoryController controller, CompoundTag tag)
			{
				return PlatingRecipeStorage.deserialize(controller, tag);
			}

			public CompoundTag serialize(IFactoryController controller, PlatingRecipeStorage recipe)
			{
				var tag = new CompoundTag();
				PlatingRecipeStorage.serialize(controller, tag, recipe);
				return tag;
			}
		};
	}

	@Override
	protected void setContainerByTransfer(PlatingRecipeStorage recipe, CompoundTag payload)
	{
		super.setContainerByTransfer(recipe, payload);

		this.inputContainer.setItem(0, ItemStack.of(payload.getCompound("input")));
	}

	@Override
	protected void onRecipeChanged(RegistryAccess registryAccess)
	{
		this.resultContainer.setItem(0, this.recipe != null ? this.recipe.getPrimaryOutput() : ItemStack.EMPTY);
		this.resultContainer.setItem(1, this.recipe != null ? this.recipe.getContainer() : ItemStack.EMPTY);
	}

}
