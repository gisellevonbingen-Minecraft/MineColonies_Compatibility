package steve_gall.minecolonies_compatibility.core.common.building.module;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.jetbrains.annotations.NotNull;

import com.minecolonies.api.colony.ICitizenData;
import com.minecolonies.api.colony.IColonyManager;
import com.minecolonies.api.colony.jobs.registry.JobEntry;
import com.minecolonies.api.colony.requestsystem.token.IToken;
import com.minecolonies.api.crafting.IGenericRecipe;
import com.minecolonies.api.crafting.IRecipeStorage;
import com.minecolonies.api.crafting.registry.CraftingType;
import com.minecolonies.api.entity.citizen.AbstractEntityCitizen;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.registries.ForgeRegistries;
import steve_gall.minecolonies_compatibility.api.common.building.module.AbstractCraftingModuleWithExternalWorkingBlocks;
import steve_gall.minecolonies_compatibility.api.common.building.module.ICraftingResultListenerModule;
import steve_gall.minecolonies_compatibility.core.common.crafting.BucketFillingCraftingType;
import steve_gall.minecolonies_compatibility.core.common.crafting.BucketFillingRecipeStorage;
import steve_gall.minecolonies_compatibility.core.common.init.ModCraftingTypes;
import steve_gall.minecolonies_tweaks.api.common.crafting.ICustomizableRecipeStorage;

public class BucketFillingCraftingModule extends AbstractCraftingModuleWithExternalWorkingBlocks implements ICraftingResultListenerModule
{
	public BucketFillingCraftingModule(String id, JobEntry jobEntry)
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
		var blockEntity = level.getBlockEntity(pos);

		if (blockEntity != null)
		{
			var capability = blockEntity.getCapability(ForgeCapabilities.FLUID_HANDLER);
			var fluidHandler = capability != null ? capability.orElse(null) : null;

			if (fluidHandler != null)
			{
				return true;
			}

		}

		if (state.getBlock() instanceof LiquidBlock)
		{
			if (state.getFluidState().isSource())
			{
				return true;
			}

		}

		return false;
	}

	@Override
	public boolean needWorkingBlock(@NotNull IRecipeStorage recipeStorage)
	{
		return toRecipe(recipeStorage) != null;
	}

	@Override
	public @NotNull String getId()
	{
		return ModCraftingTypes.BUCKET_FILLING.getId().getPath();
	}

	@Override
	public Set<CraftingType> getSupportedCraftingTypes()
	{
		return Set.of(ModCraftingTypes.BUCKET_FILLING.get());
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

		if (recipe != null)
		{
			return Component.translatable("minecolonies_compatibility.interaction.no_fluid_source", recipe.getFluidStack(FluidType.BUCKET_VOLUME).getDisplayName());
		}

		return super.getWorkingBlockNotFoundMessage(recipeStorage);
	}

	@Override
	public boolean canBlockRecipeWorking(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull IRecipeStorage recipeStorage)
	{
		var recipe = toRecipe(recipeStorage);

		if (recipe != null)
		{
			return this.drain(level, pos, state, recipe, true);
		}

		return false;
	}

	@Override
	public void onCrafted(@NotNull AbstractEntityCitizen worker, @NotNull BlockPos workingPos, @NotNull IRecipeStorage recipeStorage)
	{
		var recipe = toRecipe(recipeStorage);

		if (recipe != null)
		{
			var level = worker.getLevel();
			this.drain(level, workingPos, level.getBlockState(workingPos), recipe, false);
		}

	}

	public boolean drain(Level level, BlockPos pos, BlockState state, BucketFillingRecipeStorage recipe, boolean simulate)
	{
		var blockEntity = level.getBlockEntity(pos);

		if (blockEntity != null)
		{
			for (var direction : Direction.values())
			{
				var capability = blockEntity.getCapability(ForgeCapabilities.FLUID_HANDLER, direction);
				var fluidHandler = capability != null ? capability.orElse(null) : null;

				if (fluidHandler != null)
				{
					var stack = recipe.getFluidStack(FluidType.BUCKET_VOLUME);
					var drained = fluidHandler.drain(stack, simulate ? FluidAction.SIMULATE : FluidAction.EXECUTE);
					return drained.getAmount() >= stack.getAmount();
				}

			}

		}

		if (recipe.getFluidTag() == null && state.getBlock() instanceof LiquidBlock liquid)
		{
			if (state.getFluidState().isSource() && liquid.getFluid() == recipe.getFluid())
			{
				if (!simulate)
				{
					level.setBlock(pos, Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL_IMMEDIATE);
				}

				return true;
			}

		}

		return false;
	}

	@Override
	public @NotNull List<IGenericRecipe> getAdditionalRecipesForDisplayPurposesOnly()
	{
		var recipes = new ArrayList<IGenericRecipe>();

		for (var fluid : ForgeRegistries.FLUIDS.getValues())
		{
			if (!fluid.isSource(fluid.defaultFluidState()))
			{
				continue;
			}

			var recipe = BucketFillingCraftingType.parse(new ItemStack(fluid.getBucket()));

			if (recipe != null)
			{
				recipes.add(recipe.getGenericRecipe());
			}

		}

		return recipes;
	}

	public static BucketFillingRecipeStorage toRecipe(IToken<?> token)
	{
		return toRecipe(IColonyManager.getInstance().getRecipeManager().getRecipe(token));
	}

	public static BucketFillingRecipeStorage toRecipe(IRecipeStorage recipeStorage)
	{
		if (recipeStorage instanceof ICustomizableRecipeStorage crs)
		{
			if (crs.getImpl() instanceof BucketFillingRecipeStorage recipe)
			{
				return recipe;
			}

		}

		return null;
	}

}
