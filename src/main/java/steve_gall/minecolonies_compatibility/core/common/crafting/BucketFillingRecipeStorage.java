package steve_gall.minecolonies_compatibility.core.common.crafting;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.jetbrains.annotations.NotNull;

import com.minecolonies.api.colony.requestsystem.factory.IFactoryController;
import com.minecolonies.api.crafting.ItemStorage;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import steve_gall.minecolonies_compatibility.api.common.crafting.GenericedRecipeStorage;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_tweaks.core.common.item.ItemSerializationHelper;

public class BucketFillingRecipeStorage extends GenericedRecipeStorage<BucketFillingGenericRecipe>
{
	public static final ResourceLocation ID = MineColoniesCompatibility.rl("bucket_filling");
	public static String TAG_EMPTY_BUCKET = "emptyBucket";
	public static String TAG_FLUID = "fluid";
	public static String TAG_FLUID_AMOUNT = "fluidAmount";
	public static String TAG_DATA_COMPONENT_PATCH = "dataComponentPatch";
	public static String TAG_FILLED_BUCKET = "filledBucket";

	public static void serialize(HolderLookup.Provider provider, IFactoryController controller, CompoundTag tag, BucketFillingRecipeStorage recipe)
	{
		tag.put(TAG_EMPTY_BUCKET, ItemSerializationHelper.serializeTag(provider, recipe.emptyBucket));
		tag.putString(TAG_FLUID, BuiltInRegistries.FLUID.getKey(recipe.fluid).toString());
		tag.putInt(TAG_FLUID_AMOUNT, recipe.fluidAmount);
		tag.put(TAG_DATA_COMPONENT_PATCH, DataComponentPatch.CODEC.encodeStart(NbtOps.INSTANCE, recipe.dataComponentPatch).getOrThrow());
		tag.put(TAG_FILLED_BUCKET, ItemSerializationHelper.serializeTag(provider, recipe.filledBucket));
	}

	public static BucketFillingRecipeStorage deserialize(HolderLookup.Provider provider, IFactoryController controller, CompoundTag tag)
	{
		var emptyBucket = ItemSerializationHelper.deserializeTag(provider, tag.getCompound(TAG_EMPTY_BUCKET));
		var fluidId = ResourceLocation.parse(tag.getString(TAG_FLUID));
		var fluid = BuiltInRegistries.FLUID.get(fluidId);
		var fluidAmount = tag.contains(TAG_FLUID_AMOUNT) ? tag.getInt(TAG_FLUID_AMOUNT) : FluidType.BUCKET_VOLUME;
		var dataComponentPatch = DataComponentPatch.CODEC.decode(NbtOps.INSTANCE, tag.getCompound(TAG_DATA_COMPONENT_PATCH)).getOrThrow().getFirst();
		var filledBucket = ItemSerializationHelper.deserializeTag(provider, tag.getCompound(TAG_FILLED_BUCKET));
		return new BucketFillingRecipeStorage(emptyBucket, fluid, fluidAmount, dataComponentPatch, filledBucket);
	}

	private final ItemStack emptyBucket;
	private final Fluid fluid;
	private final int fluidAmount;
	private final DataComponentPatch dataComponentPatch;
	private final ItemStack filledBucket;

	private final List<ItemStorage> input;
	private final BucketFillingGenericRecipe recipe;

	public BucketFillingRecipeStorage(ItemStack emptyBucket, Fluid fluid, int fluidAmount, DataComponentPatch dataComponentPatch, ItemStack filledBucket)
	{
		this.emptyBucket = emptyBucket;
		this.fluid = fluid;
		this.fluidAmount = fluidAmount;
		this.dataComponentPatch = dataComponentPatch;
		this.filledBucket = filledBucket;

		this.input = Collections.singletonList(new ItemStorage(emptyBucket));
		this.recipe = new BucketFillingGenericRecipe(emptyBucket, fluid, fluidAmount, dataComponentPatch, filledBucket);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(//
				this.emptyBucket.getItem().hashCode(), //
				this.fluid.hashCode(), //
				this.fluidAmount, //
				this.dataComponentPatch == null ? 0 : this.dataComponentPatch.hashCode(), //
				this.filledBucket.getItem().hashCode()//
		);
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o)
		{
			return true;
		}
		else if (o instanceof BucketFillingRecipeStorage other)
		{
			return ItemStack.matches(this.emptyBucket, other.emptyBucket)//
					&& this.fluid == other.fluid//
					&& this.fluidAmount == other.fluidAmount//
					&& Objects.equals(this.dataComponentPatch, other.dataComponentPatch)//
					&& ItemStack.matches(this.filledBucket, other.filledBucket);
		}

		return false;
	}

	@Override
	public ResourceLocation getId()
	{
		return ID;
	}

	public ItemStack getEmptyBucket()
	{
		return this.emptyBucket;
	}

	public Fluid getFluid()
	{
		return this.fluid;
	}

	public int getFluidAmount()
	{
		return this.fluidAmount;
	}

	public DataComponentPatch getDataComponentPatch()
	{
		return this.dataComponentPatch;
	}

	public FluidStack getFluidStack()
	{
		return this.getFluidStack(this.fluidAmount);
	}

	public FluidStack getFluidStack(int amount)
	{
		var stack = new FluidStack(this.fluid, amount);
		stack.applyComponents(this.dataComponentPatch);
		return stack;
	}

	public ItemStack getFilledBucket()
	{
		return this.filledBucket;
	}

	@Override
	public List<ItemStorage> getInput()
	{
		return this.input;
	}

	@Override
	public @NotNull BucketFillingGenericRecipe getGenericRecipe()
	{
		return this.recipe;
	}

}
