package steve_gall.minecolonies_compatibility.core.common.entity.ai.orchardist;

import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.minecolonies.api.util.BlockPosUtil;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelReader;
import steve_gall.minecolonies_compatibility.api.common.entity.plant.CustomizedFruit;
import steve_gall.minecolonies_compatibility.api.common.entity.plant.HarvesterContext;
import steve_gall.minecolonies_compatibility.api.common.entity.plant.PlantBlockContext;

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
	private PlantBlockContext context;

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
		var position = this.getPosition();
		this.context = new PlantBlockContext(level, position, level.getBlockState(position));
	}

	public boolean updateAndIsValid(@NotNull LevelReader level)
	{
		this.update(level);

		var context = this.getContext();
		var fruit = this.getFruit();

		if (fruit != null && fruit.test(context))
		{
			return true;
		}

		this.fruit = CustomizedFruit.select(context);
		return this.fruit != null;
	}

	public boolean canHarvest()
	{
		var fruit = this.fruit;

		if (fruit != null)
		{
			return fruit.canHarvest(this.getContext());
		}
		else
		{
			return false;
		}

	}

	public List<ItemStack> harvest(@NotNull HarvesterContext harvester)
	{
		var fruit = this.fruit;

		if (fruit != null)
		{
			return fruit.harvest(this.getContext(), harvester);
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

	@Nullable
	public PlantBlockContext getContext()
	{
		return this.context;
	}

	@Nullable
	public CustomizedFruit getFruit()
	{
		return this.fruit;
	}

}
