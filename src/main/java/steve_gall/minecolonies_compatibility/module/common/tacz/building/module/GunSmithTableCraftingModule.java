package steve_gall.minecolonies_compatibility.module.common.tacz.building.module;

import java.util.Collections;
import java.util.Set;

import org.jetbrains.annotations.NotNull;

import com.minecolonies.api.colony.ICitizenData;
import com.minecolonies.api.colony.jobs.registry.JobEntry;
import com.minecolonies.api.crafting.IGenericRecipe;
import com.minecolonies.api.crafting.IRecipeStorage;
import com.minecolonies.api.crafting.registry.CraftingType;
import com.minecolonies.core.colony.buildings.moduleviews.CraftingModuleView;
import com.tacz.guns.block.AbstractGunSmithTableBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import steve_gall.minecolonies_compatibility.api.common.building.module.AbstractCraftingModuleWithExternalWorkingBlocks;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.module.common.tacz.init.ModuleCraftingTypes;
import steve_gall.minecolonies_compatibility.module.common.tacz.network.GunSmithTableOpenTeachMessage;

public class GunSmithTableCraftingModule extends AbstractCraftingModuleWithExternalWorkingBlocks
{
	public GunSmithTableCraftingModule(JobEntry jobEntry)
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
		return level.getBlockState(pos).getBlock() instanceof AbstractGunSmithTableBlock;
	}

	@Override
	public @NotNull String getId()
	{
		return "tacz_gun_smith_table";
	}

	@Override
	public Set<CraftingType> getSupportedCraftingTypes()
	{
		return Collections.singleton(ModuleCraftingTypes.GUN_SMITH_TABLE.get());
	}

	@Override
	public boolean isRecipeCompatible(@NotNull IGenericRecipe recipe)
	{
		return true;
	}

	public static class View extends CraftingModuleView
	{
		public View()
		{

		}

		@Override
		public void openCraftingGUI()
		{
			MineColoniesCompatibility.network().sendToServer(new GunSmithTableOpenTeachMessage(this));
		}

	}

}
