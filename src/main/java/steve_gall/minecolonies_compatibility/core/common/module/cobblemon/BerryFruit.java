package steve_gall.minecolonies_compatibility.core.common.module.cobblemon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.cobblemon.mod.common.block.BerryBlock;
import com.cobblemon.mod.common.block.entity.BerryBlockEntity;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.FakePlayerFactory;
import steve_gall.minecolonies_compatibility.api.common.plant.CustomizedFruit;
import steve_gall.minecolonies_compatibility.api.common.plant.HarvesterContext;
import steve_gall.minecolonies_compatibility.api.common.plant.PlantBlockContext;
import steve_gall.minecolonies_compatibility.core.common.mixin.cobblemon.BerryBlockAccessor;

public class BerryFruit extends CustomizedFruit
{
	@Override
	public boolean test(@NotNull PlantBlockContext context)
	{
		return context.getState().getBlock() instanceof BerryBlock;
	}

	@Override
	public boolean canHarvest(@NotNull PlantBlockContext context)
	{
		var state = context.getState();
		return ((BerryBlockAccessor) state.getBlock()).invokeIsMaxAage(state);
	}
	
	@Override
	public boolean isMaxHarvest(@NotNull PlantBlockContext context)
	{
		return true;
	}

	@Override
	public @NotNull List<ItemStack> harvest(@NotNull PlantBlockContext context, @NotNull HarvesterContext harvester)
	{
		var state = context.getState();
		var position = context.getPosition();

		if (context.getLevel() instanceof ServerLevel level && level.getBlockEntity(position) instanceof BerryBlockEntity blockEntity)
		{
			var player = FakePlayerFactory.getMinecraft(level);
			return new ArrayList<>(blockEntity.harvest(level, state, position, player));
		}

		return Collections.emptyList();
	}

}
