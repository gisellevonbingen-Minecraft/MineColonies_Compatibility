package steve_gall.minecolonies_compatibility.core.common.entity.ai.orchardist;

import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.minecolonies.api.util.BlockPosUtil;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import steve_gall.minecolonies_compatibility.api.common.entity.plant.CustomizedFruit;

public class Fruit
{
	public static String TAG_POSITION = "position";

	public static Fruit deserialize(@NotNull CompoundTag tag)
	{
		var fruit = new Fruit();
		fruit.position = BlockPosUtil.read(tag, TAG_POSITION);

		return fruit;
	}

	@NotNull
	private BlockPos position;

	@Nullable
	private BlockState state;

	@Nullable
	private CustomizedFruit fruit;

	private Fruit()
	{

	}

	public Fruit(@NotNull BlockPos position)
	{
		this.position = position;
	}

	public void serialize(@NotNull CompoundTag tag)
	{
		BlockPosUtil.write(tag, TAG_POSITION, this.position);
	}

	private void update(LevelReader level)
	{
		this.state = level.getBlockState(this.getPosition());
	}

	public boolean updateAndIsValid(@NotNull LevelReader level)
	{
		this.update(level);

		var state = this.getState();
		var fruit = this.getFruit();

		if (fruit != null && fruit.test(state))
		{
			return true;
		}

		this.fruit = CustomizedFruit.select(state);
		return this.fruit != null;
	}

	public boolean canHarvest()
	{
		var fruit = this.fruit;

		if (fruit != null)
		{
			var state = this.getState();
			return fruit.canHarvest(state);
		}
		else
		{
			return false;
		}

	}

	public List<ItemStack> harvest(@NotNull Level level)
	{
		var fruit = this.fruit;

		if (fruit != null)
		{
			var position = this.getPosition();
			var state = this.getState();
			return fruit.harvest(state, level, position);
		}
		else
		{
			return Collections.emptyList();
		}

	}

	@NotNull
	public BlockPos getPosition()
	{
		return this.position;
	}

	public BlockState getState()
	{
		return this.state;
	}

	@Nullable
	public CustomizedFruit getFruit()
	{
		return this.fruit;
	}

}
