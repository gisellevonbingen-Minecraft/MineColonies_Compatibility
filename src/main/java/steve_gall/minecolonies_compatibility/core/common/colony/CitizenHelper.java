package steve_gall.minecolonies_compatibility.core.common.colony;

import java.util.function.Predicate;

import org.jetbrains.annotations.NotNull;

import com.google.common.reflect.TypeToken;
import com.minecolonies.api.colony.ICitizenData;
import com.minecolonies.api.colony.jobs.registry.JobEntry;
import com.minecolonies.api.colony.requestsystem.request.IRequest;
import com.minecolonies.api.util.InventoryUtils;
import com.minecolonies.api.util.constant.IToolType;
import com.minecolonies.api.util.constant.ToolLevelConstants;

public class CitizenHelper
{
	public static int getMaxLevelToolSlot(@NotNull ICitizenData citizen, @NotNull IToolType toolType)
	{
		var inventory = citizen.getInventory();
		var maxToolLevel = citizen.getWorkBuilding().getMaxToolLevel();
		return InventoryUtils.getFirstSlotOfItemHandlerContainingTool(inventory, toolType, ToolLevelConstants.TOOL_LEVEL_WOOD_OR_GOLD, maxToolLevel);
	}

	public static JobEntry getJobEntry(@NotNull ICitizenData citizen)
	{
		var job = citizen.getJob();
		return job != null ? job.getJobRegistryEntry() : null;
	}

	public static <R> boolean isRequested(@NotNull ICitizenData citizen, @NotNull TypeToken<R> token, @NotNull Predicate<IRequest<? extends R>> predicate)
	{
		var building = citizen.getWorkBuilding();

		if (!building.getOpenRequestsOfTypeFiltered(citizen, token, predicate).isEmpty())
		{
			return true;
		}
		else if (!building.getCompletedRequestsOfTypeFiltered(citizen, token, predicate).isEmpty())
		{
			return true;
		}
		else
		{
			return false;
		}

	}

	private CitizenHelper()
	{

	}

}
