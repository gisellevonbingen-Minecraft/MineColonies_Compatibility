package steve_gall.minecolonies_compatibility.module.common.butchersdelight;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.jetbrains.annotations.NotNull;

import com.minecolonies.api.entity.citizen.AbstractEntityCitizen;
import com.minecolonies.api.util.constant.ToolType;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;
import steve_gall.minecolonies_compatibility.api.common.butcher.CustomizedButcherable;

public abstract class AbstractButcherable extends CustomizedButcherable
{
	protected final ResourceLocation id;
	protected final Item item;
	protected final Set<Block> blocks;

	protected final List<ItemStack> itemIcons;
	protected final List<Ingredient> outputIcons;

	public AbstractButcherable(Builder builder)
	{
		this.id = ForgeRegistries.ITEMS.getKey(builder.item);
		this.item = builder.item;
		this.blocks = builder.blocks.stream().collect(Collectors.toUnmodifiableSet());

		this.itemIcons = Collections.singletonList(new ItemStack(builder.item));
		this.outputIcons = builder.outputs.stream().map(Ingredient::of).toList();
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
		return this.outputIcons;
	}

	@Override
	public @NotNull ToolType getTableToolType(@NotNull LevelReader level, @NotNull BlockPos position, @NotNull BlockState state)
	{
		return ToolType.NONE;
	}

	@Override
	public boolean testItem(@NotNull ItemStack item)
	{
		return item.is(this.item);
	}

	@Override
	public boolean isButcheringBlock(@NotNull LevelReader level, @NotNull BlockPos position, @NotNull BlockState state)
	{
		return this.blocks.contains(state.getBlock());
	}

	@Override
	public void doButcherBlock(@NotNull Level level, @NotNull BlockPos position, @NotNull BlockState state, @NotNull AbstractEntityCitizen worker)
	{
		super.doButcherBlock(level, position, state, worker);

		if (level instanceof ServerLevel serverLevel)
		{
			var tool = this.getButcherBlockTool();
			this.setButcherBlockProcess(serverLevel.getBlockEntity(position).getPersistentData());
			ButchersDelightModule.rightClick(serverLevel, position, worker, tool.copy());
		}

	}

	protected abstract void setButcherBlockProcess(CompoundTag tag);

	protected abstract ItemStack getButcherBlockTool();

	public Item getItem()
	{
		return this.item;
	}

	public Set<Block> getBlocks()
	{
		return this.blocks;
	}

	public static class Builder
	{
		private Item item = Items.AIR;
		private final List<Block> blocks = new ArrayList<>();
		private final List<ItemStack> outputs = new ArrayList<>();

		public Builder()
		{

		}

		public Builder item(Item item)
		{
			this.item = item;
			return this;
		}

		public Builder item(Supplier<Item> item)
		{
			return this.item(item.get());
		}

		public Builder block(Block block)
		{
			this.blocks.add(block);
			return this;
		}

		public Builder block(Supplier<Block> block)
		{
			return this.block(block.get());
		}

		public Builder output(Item output)
		{
			return this.output(new ItemStack(output));
		}

		public Builder output(Supplier<Item> output)
		{
			return this.output(output.get());
		}

		public Builder output(ItemStack output)
		{
			this.outputs.add(output);
			return this;
		}

	}

}
