package steve_gall.minecolonies_compatibility.module.common.butchercraft.building.module;

import java.util.Collections;
import java.util.Set;

import org.jetbrains.annotations.NotNull;

import com.google.common.collect.ImmutableSet;
import com.lance5057.butchercraft.workstations.grinder.GrinderBlockEntity;
import com.minecolonies.api.colony.ICitizenData;
import com.minecolonies.api.colony.IColonyManager;
import com.minecolonies.api.colony.jobs.registry.JobEntry;
import com.minecolonies.api.colony.requestsystem.token.IToken;
import com.minecolonies.api.crafting.IGenericRecipe;
import com.minecolonies.api.crafting.IRecipeStorage;
import com.minecolonies.api.crafting.registry.CraftingType;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import steve_gall.minecolonies_compatibility.api.common.building.module.AbstractCraftingModuleWithExternalWorkingBlocks;
import steve_gall.minecolonies_compatibility.core.common.util.InteractionMessageHelper;
import steve_gall.minecolonies_compatibility.module.common.butchercraft.crafting.GrinderRecipeStorage;
import steve_gall.minecolonies_compatibility.module.common.butchercraft.init.ModuleCraftingTypes;
import steve_gall.minecolonies_tweaks.api.common.crafting.ICustomizableRecipeStorage;

public class GrinderCraftingModule extends AbstractCraftingModuleWithExternalWorkingBlocks
{
	public GrinderCraftingModule(JobEntry jobEntry)
	{
		super(jobEntry);
	}

	@Override
	public void improveRecipe(IRecipeStorage recipe, int count, ICitizenData citizen)
	{

	}

	@Override
	public boolean isWorkingBlock(@NotNull LevelReader level, @NotNull BlockPos pos, @NotNull BlockState state)
	{
		return level.getBlockEntity(pos) instanceof GrinderBlockEntity;
	}

	@Override
	public boolean canBlockRecipeWorking(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull IRecipeStorage recipeStorage)
	{
		var recipe = toRecipe(recipeStorage);

		if (recipe != null && level.getBlockEntity(pos) instanceof GrinderBlockEntity grinder)
		{
			return ItemStack.isSame(recipe.getAttachment().getItemStack(), grinder.getAttachment());
		}

		return false;
	}

	@Override
	public @NotNull String getId()
	{
		return "butchercraft_grinder";
	}

	@Override
	public Set<CraftingType> getSupportedCraftingTypes()
	{
		return (this.building == null || this.building.getBuildingLevel() >= 3) ? Collections.singleton(ModuleCraftingTypes.GRINDER.get()) : ImmutableSet.of();
	}

	@Override
	public boolean isRecipeCompatible(@NotNull IGenericRecipe recipe)
	{
		return true;
	}

	@Override
	public @NotNull Component getWorkingBlockNotFoundMessage(@NotNull IRecipeStorage recipeStorage)
	{
		var recipe = toRecipe(recipeStorage);

		if (recipe == null)
		{
			return super.getWorkingBlockNotFoundMessage(recipeStorage);
		}

		var block = recipeStorage.getIntermediate();
		var modDisplayName = InteractionMessageHelper.getModDisplayName(block);
		var attachment = recipe.getAttachment().getItemStack();
		return Component.translatable("minecolonies_compatibility.interaction.no_butchercraft_grinder_with_attachment", modDisplayName, block.getName(), attachment.getHoverName());
	}

	public static GrinderRecipeStorage toRecipe(IToken<?> token)
	{
		return toRecipe(IColonyManager.getInstance().getRecipeManager().getRecipe(token));
	}

	public static GrinderRecipeStorage toRecipe(IRecipeStorage recipeStorage)
	{
		if (recipeStorage instanceof ICustomizableRecipeStorage crs)
		{
			if (crs.getImpl() instanceof GrinderRecipeStorage recipe)
			{
				return recipe;
			}

		}

		return null;
	}

}
