package steve_gall.minecolonies_compatibility.core.common.crafting;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.minecolonies.api.crafting.IGenericRecipe;
import com.minecolonies.api.crafting.ItemStorage;
import com.minecolonies.api.equipment.registry.EquipmentTypeEntry;
import com.minecolonies.api.util.OptionalPredicate;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.registries.ForgeRegistries;

public class AnimalHerdingLootGenericRecipe implements IGenericRecipe
{
	private final EntityType<?> entityType;
	private final ResourceLocation entityTypeKey;
	private final ResourceLocation lootTable;
	private final List<List<ItemStack>> breedingItems;
	private final EquipmentTypeEntry toolType;

	public AnimalHerdingLootGenericRecipe(EntityType<?> entityType, List<List<ItemStorage>> breedingItems, ResourceLocation lootTable, EquipmentTypeEntry toolType)
	{
		this.entityType = entityType;
		this.entityTypeKey = ForgeRegistries.ENTITY_TYPES.getKey(entityType);
		this.lootTable = lootTable;
		this.breedingItems = breedingItems.stream().map(l -> l.stream().map(ItemStorage::getItemStack).toList()).toList();
		this.toolType = toolType;
	}

	@Override
	public int getGridSize()
	{
		return 0;
	}

	@Override
	public @Nullable ResourceLocation getRecipeId()
	{
		return this.entityTypeKey;
	}

	@Override
	public @NotNull ItemStack getPrimaryOutput()
	{
		return ItemStack.EMPTY;
	}

	@Override
	public @NotNull List<ItemStack> getAllMultiOutputs()
	{
		return Collections.emptyList();
	}

	@Override
	public @NotNull List<ItemStack> getAdditionalOutputs()
	{
		return Collections.emptyList();
	}

	@Override
	public @NotNull List<List<ItemStack>> getInputs()
	{
		return this.breedingItems;
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
	public @Nullable ResourceLocation getLootTable()
	{
		return this.lootTable;
	}

	@Override
	public @NotNull EquipmentTypeEntry getRequiredTool()
	{
		return this.toolType;
	}

	@Override
	public @Nullable EntityType<?> getRequiredEntity()
	{
		return this.entityType;
	}

	@Override
	public @NotNull Supplier<List<Component>> getRestrictions()
	{
		return Collections::emptyList;
	}

	@Override
	public int getLevelSort()
	{
		return 0;
	}

}
