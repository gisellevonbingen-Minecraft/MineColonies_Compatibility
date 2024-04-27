package steve_gall.minecolonies_compatibility.api.common.building;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.minecolonies.api.colony.buildings.IBuilding;
import com.minecolonies.core.colony.buildings.AbstractBuildingGuards;
import com.minecolonies.core.colony.buildings.modules.settings.GuardTaskSetting;

public class BuildingHelper
{
	/**
	 * @param building
	 * @return EMPTY when building is null or not GuardsBuilding
	 */
	@NotNull
	public static String GetGuardsTask(@Nullable IBuilding building)
	{
		return building instanceof AbstractBuildingGuards buildingGuards ? buildingGuards.getTask() : "";
	}

	/**
	 * @see GuardTaskSetting
	 * @param building
	 * @param task
	 * @return
	 */
	public static boolean IsGuardsTask(@Nullable IBuilding building, @Nullable String taskName)
	{
		return GetGuardsTask(building).equals(GuardTaskSetting.GUARD);
	}

	private BuildingHelper()
	{

	}

}
