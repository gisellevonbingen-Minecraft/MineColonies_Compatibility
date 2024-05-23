package steve_gall.minecolonies_compatibility.module.common.cyclic;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import org.jetbrains.annotations.NotNull;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;
import steve_gall.minecolonies_compatibility.api.common.plant.CustomizedFruit;
import steve_gall.minecolonies_compatibility.api.common.plant.HarvesterContext;
import steve_gall.minecolonies_compatibility.api.common.plant.PlantBlockContext;
import steve_gall.minecolonies_compatibility.mixin.common.cyclic.AppleCropBlockAccessor;

public class AppleSproutFruit extends CustomizedFruit
{
	private final RegistryObject<Block> sprout;
	private final Supplier<Item> fruit;

	public AppleSproutFruit(RegistryObject<Block> sprout, Supplier<Item> fruit)
	{
		this.sprout = sprout;
		this.fruit = fruit;
	}

	@Override
	public @NotNull ResourceLocation getId()
	{
		return this.sprout.getId();
	}

	@Override
	public @NotNull List<ItemStack> getBlockIcons()
	{
		return Arrays.asList(new ItemStack(this.sprout.get()));
	}

	@Override
	public @NotNull List<ItemStack> getItemIcons()
	{
		return Arrays.asList(new ItemStack(this.fruit.get()));
	}

	@Override
	public boolean test(@NotNull PlantBlockContext context)
	{
		return context.getState().getBlock() == this.sprout.get();
	}

	@Override
	public boolean canHarvest(@NotNull PlantBlockContext context)
	{
		return context.getState().getValue(AppleCropBlockAccessor.getAge()) >= AppleCropBlockAccessor.getMaxAge();
	}

	@Override
	public boolean isMaxHarvest(@NotNull PlantBlockContext context)
	{
		return true;
	}

	@Override
	public @NotNull List<ItemStack> harvest(@NotNull PlantBlockContext context, @NotNull HarvesterContext harvester)
	{
		// For apply fortune
		// https://github.com/Lothrazar/Cyclic/blob/trunk/1.20.1/src/main/resources/data/cyclic/loot_tables/blocks/apple_sprout.json
		var drops = context.getDrops(harvester);
		var newState = context.getState().getBlock().defaultBlockState();
		this.replant(context, drops, newState);
		return drops;
	}

}
