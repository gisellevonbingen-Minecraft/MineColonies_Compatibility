package steve_gall.minecolonies_compatibility.core.common.inventory;

import java.util.Collections;
import java.util.List;

import com.minecolonies.api.colony.buildings.modules.IBuildingModule;
import com.minecolonies.api.colony.requestsystem.factory.IFactoryController;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import steve_gall.minecolonies_compatibility.api.common.inventory.IMenuRecipeValidator;
import steve_gall.minecolonies_compatibility.core.common.crafting.BucketFillingCraftingType;
import steve_gall.minecolonies_compatibility.core.common.crafting.BucketFillingRecipeStorage;
import steve_gall.minecolonies_compatibility.core.common.init.ModMenuTypes;

public class BucketFillingTeachMenu extends TeachRecipeMenu<BucketFillingRecipeStorage, SingleRecipeInput>
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
	protected IMenuRecipeValidator<BucketFillingRecipeStorage, SingleRecipeInput> createRecipeValidator()
	{
		return new IMenuRecipeValidator<>()
		{
			@Override
			public SingleRecipeInput getInput(Container container, BucketFillingRecipeStorage recipe)
			{
				return new SingleRecipeInput(container.getItem(0));
			}

			@Override
			public List<BucketFillingRecipeStorage> findAll(Container container, ServerPlayer player)
			{
				var recipe = BucketFillingCraftingType.parse(container.getItem(0));
				return recipe != null ? Collections.singletonList(recipe) : Collections.emptyList();
			}

			@Override
			public BucketFillingRecipeStorage deserialize(HolderLookup.Provider provider, IFactoryController controller, CompoundTag tag)
			{
				return BucketFillingRecipeStorage.deserialize(provider, controller, tag);
			}

			public CompoundTag serialize(HolderLookup.Provider provider, IFactoryController controller, BucketFillingRecipeStorage recipe)
			{
				var tag = new CompoundTag();
				BucketFillingRecipeStorage.serialize(provider, controller, tag, recipe);
				return tag;
			}
		};
	}

	@Override
	protected void setContainerByTransfer(HolderLookup.Provider provider, BucketFillingRecipeStorage recipe, CompoundTag payload)
	{
		super.setContainerByTransfer(provider, recipe, payload);

		this.inputContainer.setItem(0, recipe.getFilledBucket());
	}

	@Override
	protected void onRecipeChanged(HolderLookup.Provider provider, SingleRecipeInput input)
	{

	}

}
