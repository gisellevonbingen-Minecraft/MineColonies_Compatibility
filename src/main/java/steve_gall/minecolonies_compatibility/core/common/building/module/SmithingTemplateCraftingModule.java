package steve_gall.minecolonies_compatibility.core.common.building.module;

import org.jetbrains.annotations.NotNull;

import com.minecolonies.api.IMinecoloniesAPI;
import com.minecolonies.api.colony.ICitizenData;
import com.minecolonies.api.colony.buildings.modules.IBuildingEventsModule;
import com.minecolonies.api.colony.jobs.registry.JobEntry;
import com.minecolonies.api.colony.requestsystem.requestable.IDeliverable;
import com.minecolonies.api.colony.requestsystem.token.IToken;
import com.minecolonies.api.crafting.IGenericRecipe;
import com.minecolonies.api.crafting.IRecipeStorage;
import com.minecolonies.api.util.InventoryUtils;
import com.minecolonies.core.colony.buildings.modules.AbstractCraftingBuildingModule;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import steve_gall.minecolonies_compatibility.core.common.crafting.SmithingTemplateRecipeStorage;
import steve_gall.minecolonies_compatibility.core.common.item.ItemStackCounter;
import steve_gall.minecolonies_compatibility.core.common.item.ItemStackHelper;
import steve_gall.minecolonies_compatibility.core.common.item.ItemStackKey;
import steve_gall.minecolonies_tweaks.api.common.crafting.CustomizableRecipeStorage;

public class SmithingTemplateCraftingModule extends AbstractCraftingBuildingModule.Crafting implements IBuildingEventsModule
{
	public static final int INVENTORY_SLOTS = 27;

	public static final String TAG_INVENTORY = "inventory";

	private final ItemStackHandler inventory;
	private final ItemStackCounter counter;

	public SmithingTemplateCraftingModule(JobEntry jobEntry)
	{
		super(jobEntry);

		this.inventory = new ItemStackHandler(INVENTORY_SLOTS)
		{
			@Override
			public boolean isItemValid(int slot, ItemStack stack)
			{
				return ItemStackHelper.isSmithingTemplate(stack);
			}

			@Override
			protected void onContentsChanged(int slot)
			{
				super.onContentsChanged(slot);
				onSlotChanged(slot);
			}
		};

		this.counter = new ItemStackCounter();
		this.counter.addListener(this::onCounterChanged);
	}

	@Override
	public void improveRecipe(IRecipeStorage recipe, int count, ICitizenData citizen)
	{

	}

	public void refreshCounter()
	{
		var newCounter = new ItemStackCounter();
		var sizeslots = this.inventory.getSlots();

		for (var i = 0; i < sizeslots; i++)
		{
			var stack = this.inventory.getStackInSlot(i);

			if (!stack.isEmpty())
			{
				newCounter.insert(new ItemStackKey(stack), stack.getCount());
			}

		}

		this.counter.replace(newCounter.entrySet());
	}

	public void onSlotChanged(int index)
	{
		this.markDirty();
		this.refreshCounter();
	}

	@Override
	public @NotNull String getId()
	{
		return "smithing_template_crafting";
	}

	@Override
	public void deserializeNBT(CompoundTag compound)
	{
		super.deserializeNBT(compound);

		this.inventory.deserializeNBT(compound.getCompound(TAG_INVENTORY));
		this.refreshCounter();
	}

	@Override
	public void serializeNBT(@NotNull CompoundTag compound)
	{
		super.serializeNBT(compound);

		compound.put(TAG_INVENTORY, this.inventory.serializeNBT());
	}

	@Override
	public void serializeToView(@NotNull FriendlyByteBuf buf, boolean fullSync)
	{
		super.serializeToView(buf, fullSync);

		this.counter.serializeBuffer(buf);
	}

	@Override
	public boolean isRecipeCompatible(@NotNull IGenericRecipe recipe)
	{
		return ItemStackHelper.isSmithingTemplate(recipe.getPrimaryOutput()) && super.isRecipeCompatible(recipe);
	}

	@Override
	public boolean holdsRecipe(IToken<?> token)
	{
		if (!super.holdsRecipe(token))
		{
			return false;
		}

		var recipeManager = IMinecoloniesAPI.getInstance().getColonyManager().getRecipeManager();
		var storage = recipeManager.getRecipe(token);

		if (storage instanceof CustomizableRecipeStorage crs && crs.getImpl() instanceof SmithingTemplateRecipeStorage recipe)
		{
			var count = this.counter.get(new ItemStackKey(recipe.getPrimaryOutput()));
			return count >= recipe.getInputTemplateCount();
		}

		return true;
	}

	protected void onCounterChanged(ItemStackKey key, long oldCount, long newCount)
	{
		if (oldCount < newCount)
		{
			var stack = key.getStack(newCount);

			this.building.getColony().getRequestManager().onColonyUpdate(request ->
			{
				return request.getRequest() instanceof IDeliverable deliverable && deliverable.matches(stack);
			});
		}

	}

	@Override
	public void onDestroyed()
	{
		var pos = this.building.getID();
		InventoryUtils.dropItemHandler(this.inventory, this.building.getColony().getWorld(), pos.getX(), pos.getY(), pos.getZ());
	}

	public IItemHandlerModifiable getInventory()
	{
		return this.inventory;
	}

}
