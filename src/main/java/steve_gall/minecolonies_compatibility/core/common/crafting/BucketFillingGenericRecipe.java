package steve_gall.minecolonies_compatibility.core.common.crafting;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.minecolonies.api.crafting.IGenericRecipe;
import com.minecolonies.api.equipment.ModEquipmentTypes;
import com.minecolonies.api.equipment.registry.EquipmentTypeEntry;
import com.minecolonies.api.util.OptionalPredicate;

import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.storage.loot.LootTable;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;

public class BucketFillingGenericRecipe implements IGenericRecipe
{
	private final ItemStack emptyBucket;
	private final Fluid fluid;
	private final DataComponentPatch dataComponentPatch;
	private final ItemStack filledBucket;

	private final List<ItemStack> allMultiOutputs;
	private final List<List<ItemStack>> inputs;

	public BucketFillingGenericRecipe(ItemStack emptyBucket, Fluid fluid, DataComponentPatch dataComponentPatch, ItemStack filledBucket)
	{
		this.emptyBucket = emptyBucket;
		this.fluid = fluid;
		this.dataComponentPatch = dataComponentPatch;
		this.filledBucket = filledBucket;

		this.allMultiOutputs = Collections.singletonList(this.getPrimaryOutput());
		this.inputs = Collections.singletonList(Collections.singletonList(this.emptyBucket));
	}

	@Override
	public @Nullable ResourceLocation getRecipeId()
	{
		return MineColoniesCompatibility.rl("bucket_filling."//
				+ this.getSegment(BuiltInRegistries.ITEM.getKey(this.emptyBucket.getItem())) + "."//
				+ this.getSegment(BuiltInRegistries.FLUID.getKey(this.getFluid())) + "."//
				+ this.getSegment(BuiltInRegistries.ITEM.getKey(this.filledBucket.getItem()))//
		);
	}

	public ItemStack getEmptyBucket()
	{
		return this.emptyBucket;
	}

	public Fluid getFluid()
	{
		return this.fluid;
	}

	public DataComponentPatch getDataComponentPatch()
	{
		return this.dataComponentPatch;
	}

	public ItemStack getFilledBucket()
	{
		return this.filledBucket;
	}

	protected String getSegment(ResourceLocation id)
	{
		return id.getNamespace() + "_" + id.getPath();
	}

	@Override
	public @NotNull ItemStack getPrimaryOutput()
	{
		return this.filledBucket;
	}

	@Override
	public @NotNull List<ItemStack> getAllMultiOutputs()
	{
		return this.allMultiOutputs;
	}

	@Override
	public @NotNull List<ItemStack> getAdditionalOutputs()
	{
		return Collections.emptyList();
	}

	@Override
	public @NotNull List<List<ItemStack>> getInputs()
	{
		return this.inputs;
	}

	@Override
	public Optional<Boolean> matchesOutput(@NotNull OptionalPredicate<ItemStack> predicate)
	{
		return Optional.empty();
	}

	@Override
	public Optional<Boolean> matchesInput(@NotNull OptionalPredicate<ItemStack> predicate)
	{
		return Optional.empty();
	}

	@Override
	public @NotNull Block getIntermediate()
	{
		return Blocks.AIR;
	}

	@Override
	public int getGridSize()
	{
		return 1;
	}

	@Override
	public @Nullable ResourceKey<LootTable> getLootTable()
	{
		return null;
	}

	@Override
	public @NotNull EquipmentTypeEntry getRequiredTool()
	{
		return ModEquipmentTypes.none.get();
	}

	@Override
	public @Nullable EntityType<?> getRequiredEntity()
	{
		return null;
	}

	@Override
	public @NotNull Supplier<List<Component>> getRestrictions()
	{
		return Collections::emptyList;
	}

	@Override
	public int getLevelSort()
	{
		return -1;
	}

}
