package steve_gall.minecolonies_compatibility.core.common.crafting;

import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.minecolonies.api.crafting.IGenericRecipe;
import com.minecolonies.api.crafting.registry.CraftingType;
import com.minecolonies.api.items.ModItems;

import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

public class BucketFillingCraftingType extends CraftingType
{
	public BucketFillingCraftingType(@NotNull ResourceLocation id)
	{
		super(id);
	}

	@Override
	public @NotNull List<IGenericRecipe> findRecipes(@NotNull RecipeManager recipeManager, @Nullable Level world)
	{
		return Collections.emptyList();
	}

	public static BucketFillingRecipeStorage parse(ItemStack filledBucket)
	{
		if (filledBucket.is(ModItems.large_water_bottle))
		{
			return new BucketFillingRecipeStorage(new ItemStack(ModItems.large_empty_bottle), Fluids.WATER, FluidType.BUCKET_VOLUME, DataComponentPatch.EMPTY, filledBucket);
		}
		else if (filledBucket.is(ModItems.large_milk_bottle))
		{
			return new BucketFillingRecipeStorage(new ItemStack(ModItems.large_empty_bottle), NeoForgeMod.MILK.get(), FluidType.BUCKET_VOLUME, DataComponentPatch.EMPTY, filledBucket);
		}

		var tank = new FluidTank(FluidType.BUCKET_VOLUME);
		var emptyResult = FluidUtil.tryEmptyContainer(filledBucket, tank, tank.getCapacity(), null, true);
		var emptyBucket = emptyResult.getResult();

		if (emptyResult.isSuccess() && emptyBucket.getItem() == Items.BUCKET)
		{
			var fluidStack = tank.getFluid();
			var fluid = fluidStack.getFluid();

			if (!fluid.isSame(Fluids.EMPTY) && fluid.isSource(fluid.defaultFluidState()))
			{
				var fillResult = FluidUtil.tryFillContainer(emptyBucket, tank, tank.getCapacity(), null, true);

				if (fillResult.isSuccess() && ItemStack.matches(fillResult.getResult(), filledBucket))
				{
					return new BucketFillingRecipeStorage(emptyBucket, fluid, tank.getCapacity(), fluidStack.getComponentsPatch(), filledBucket);
				}

			}

		}

		return null;

	}

}
