package steve_gall.minecolonies_compatibility.core.common.inventory;

import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.minecolonies.api.colony.buildings.modules.IBuildingModule;
import com.minecolonies.api.colony.requestsystem.factory.IFactoryController;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import steve_gall.minecolonies_compatibility.api.common.inventory.IMenuRecipeValidator;
import steve_gall.minecolonies_compatibility.core.common.crafting.BucketFillingCraftingType;
import steve_gall.minecolonies_compatibility.core.common.crafting.BucketFillingRecipeStorage;
import steve_gall.minecolonies_compatibility.core.common.init.ModMenuTypes;

public class BucketFillingTeachMenu extends TeachRecipeMenu<BucketFillingRecipeStorage>
{
	public static final int INVENTORY_X = 8;
	public static final int INVENTORY_Y = 84;

	public static final int RESULT_X = 80;
	public static final int RESULT_Y = 35;

	public BucketFillingTeachMenu(int windowId, Inventory inventory, IBuildingModule module)
	{
		super(ModMenuTypes.BUCKET_FILLING_TEACH.get(), windowId, inventory, module);
		this.setup();
	}

	public BucketFillingTeachMenu(int windowId, Inventory inventory, FriendlyByteBuf buffer)
	{
		super(ModMenuTypes.BUCKET_FILLING_TEACH.get(), windowId, inventory, buffer);
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
	protected IMenuRecipeValidator<BucketFillingRecipeStorage> createRecipeValidator()
	{
		return new IMenuRecipeValidator<>()
		{
			@Override
			public List<BucketFillingRecipeStorage> findAll(Container container, ServerPlayer player)
			{
				var recipe = BucketFillingCraftingType.parse(container.getItem(0));
				return recipe != null ? Collections.singletonList(recipe) : Collections.emptyList();
			}

			@Override
			public BucketFillingRecipeStorage deserialize(IFactoryController controller, CompoundTag tag)
			{
				return BucketFillingRecipeStorage.deserialize(controller, tag);
			}

			public CompoundTag serialize(IFactoryController controller, BucketFillingRecipeStorage recipe)
			{
				var tag = new CompoundTag();
				BucketFillingRecipeStorage.serialize(controller, tag, recipe);
				return tag;
			}
		};
	}

	@Override
	protected void setContainerByTransfer(BucketFillingRecipeStorage recipe, @NotNull CompoundTag payload)
	{
		super.setContainerByTransfer(recipe, payload);

		this.inputContainer.setItem(0, recipe.getFilledBucket());
	}

	@Override
	protected void onRecipeChanged()
	{

	}

}
