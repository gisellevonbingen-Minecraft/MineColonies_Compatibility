package steve_gall.minecolonies_compatibility.module.common.butchercraft.butcherable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.lance5057.butchercraft.ButchercraftBlocks;
import com.lance5057.butchercraft.workstations.butcherblock.ButcherBlockBlockEntity;
import com.lance5057.butchercraft.workstations.butcherblock.ButcherBlockRecipe;
import com.minecolonies.core.util.citizenutils.CitizenItemUtils;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.network.PacketDistributor;
import steve_gall.minecolonies_compatibility.api.common.butcher.ButcherBlockContext;
import steve_gall.minecolonies_compatibility.api.common.butcher.ButcherCitizenContext;
import steve_gall.minecolonies_compatibility.api.common.crafting.ToolOrIngredientStack;
import steve_gall.minecolonies_compatibility.core.common.colony.ColonyHelper;
import steve_gall.minecolonies_compatibility.core.common.inventory.InventoryHelper;
import steve_gall.minecolonies_compatibility.core.common.network.message.BlockEntityRequestModelDataUpdateMessage;
import steve_gall.minecolonies_compatibility.core.common.util.InteractionMessageHelper;
import steve_gall.minecolonies_compatibility.mixin.common.butchercraft.ButcherBlockBlockEntityAccessor;

public class ButcherBlockButcherable extends AbstractButcherable
{
	private final ResourceLocation id;
	private final ButcherBlockRecipe recipe;

	private final List<ItemStack> itemIcons;
	private final List<BlockState> tableIcons;
	private final List<ToolOrIngredientStack> toolIcons;

	public ButcherBlockButcherable(RecipeHolder<ButcherBlockRecipe> recipe)
	{
		this.id = recipe.id();
		this.recipe = recipe.value();

		this.itemIcons = Arrays.asList(this.recipe.carcass().getItems());
		this.tableIcons = Collections.singletonList(ButchercraftBlocks.BUTCHER_BLOCK.get().defaultBlockState());
		this.toolIcons = this.getToolIcons(this.recipe.tools());
	}

	@Override
	public @NotNull ResourceLocation getId()
	{
		return this.id;
	}

	@Override
	public @NotNull List<ItemStack> getItemIcons()
	{
		return this.itemIcons;
	}

	@Override
	public @NotNull List<Ingredient> getOutputIcons()
	{
		return this.recipe.jei();
	}

	@Override
	public @NotNull List<BlockState> getTableIcons()
	{
		return this.tableIcons;
	}

	@Override
	public @NotNull List<ToolOrIngredientStack> getToolsForIcon()
	{
		return this.toolIcons;
	}

	@Override
	public boolean testItem(@NotNull ItemStack item)
	{
		return this.recipe.carcass().test(item);
	}

	@Override
	public boolean isTableBlock(@NotNull ButcherBlockContext context)
	{
		if (context.getLevel().getBlockEntity(context.getPosition()) instanceof ButcherBlockBlockEntity blockEntity)
		{
			return blockEntity.getInsertedItem().isEmpty();
		}

		return false;
	}

	@Override
	public void doButcherTable(@NotNull ButcherBlockContext context, @NotNull ButcherCitizenContext citizen, @NotNull InteractionHand itemHand)
	{
		super.doButcherTable(context, citizen, itemHand);

		if (context.getLevel().getBlockEntity(context.getPosition()) instanceof ButcherBlockBlockEntity blockEntity)
		{
			blockEntity.insertItem(citizen.getWorker().getItemInHand(itemHand));
			PacketDistributor.sendToPlayersTrackingEntity(citizen.getWorker(), new BlockEntityRequestModelDataUpdateMessage(blockEntity));
		}

	}

	@Override
	public @NotNull ToolOrIngredientStack getBlockTool(@NotNull ButcherBlockContext context, @NotNull ButcherCitizenContext citizen)
	{
		if (context.getLevel().getBlockEntity(context.getPosition()) instanceof ButcherBlockBlockEntity blockEntity)
		{
			var tool = blockEntity.getCurrentTool().map(this::getTool).orElse(ToolOrIngredientStack.EMPTY);
			return this.trySkip(tool::testType, citizen.getAI()) ? ToolOrIngredientStack.EMPTY : tool;
		}

		return ToolOrIngredientStack.EMPTY;
	}

	@Override
	public boolean isButcheringBlock(@NotNull ButcherBlockContext context)
	{
		if (context.getLevel().getBlockEntity(context.getPosition()) instanceof ButcherBlockBlockEntity blockEntity)
		{
			return this.recipe.carcass().test(blockEntity.getInsertedItem());
		}

		return false;
	}

	@Override
	public void doButcherBlock(@NotNull ButcherBlockContext context, @NotNull ButcherCitizenContext citizen)
	{
		super.doButcherBlock(context, citizen);

		if (context.getLevel().getBlockEntity(context.getPosition()) instanceof ButcherBlockBlockEntity blockEntity)
		{
			var worker = citizen.getWorker();
			var player = ColonyHelper.getFakeOwner(worker.getCitizenData().getColony());

			if (player == null)
			{
				return;
			}
			else if (blockEntity.maxProgress == 0)
			{
				((ButcherBlockBlockEntityAccessor) blockEntity).invokeSetupStage(this.recipe, 0);
			}

			var stage = blockEntity.stage;
			var tools = this.recipe.tools();
			var itemUse = tools.get(stage);

			if (this.trySkip(itemUse.tool()::test, citizen.getAI()))
			{
				if (stage + 1 < tools.size())
				{
					((ButcherBlockBlockEntityAccessor) blockEntity).invokeSetupStage(this.recipe, stage + 1);
					PacketDistributor.sendToPlayersTrackingEntity(worker, new BlockEntityRequestModelDataUpdateMessage(blockEntity));
				}
				else
				{
					blockEntity.finishRecipe();
				}

				blockEntity.updateInventory();
				return;
			}
			else
			{
				var maxProgress = blockEntity.maxProgress;
				var maxToolCount = blockEntity.toolCount * maxProgress;
				var toolHand = InteractionHand.MAIN_HAND;
				var tool = worker.getItemInHand(toolHand).copy();
				tool.setCount(maxToolCount);

				blockEntity.progress = maxProgress;
				blockEntity.butcher(player, tool);

				if (tool.isDamageableItem())
				{
					CitizenItemUtils.damageItemInHand(worker, toolHand, maxProgress);
				}
				else
				{
					var inventory = worker.getInventoryCitizen();
					var toolType = this.getTool(itemUse);
					InventoryHelper.removeStacksFromItemHandler(inventory, maxToolCount, toolType::testType);
				}

			}

		}

	}

	@Override
	public @NotNull Component getTableNotFoundMessage()
	{
		return InteractionMessageHelper.getWorkingBlockNotFound(ButchercraftBlocks.BUTCHER_BLOCK.get());
	}

	public @NotNull ButcherBlockRecipe getRecipe()
	{
		return this.recipe;
	}

}
