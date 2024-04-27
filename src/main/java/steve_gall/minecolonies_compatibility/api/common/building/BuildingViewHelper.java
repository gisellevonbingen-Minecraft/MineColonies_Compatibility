package steve_gall.minecolonies_compatibility.api.common.building;

import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.minecolonies.api.colony.buildings.views.IBuildingView;
import com.minecolonies.api.colony.jobs.registry.JobEntry;
import com.minecolonies.core.colony.buildings.moduleviews.WorkerBuildingModuleView;

public class BuildingViewHelper
{
	@NotNull
	public static List<Integer> getAssignedCitizens(@NotNull IBuildingView view, JobEntry jobEntry)
	{
		for (var moduleView : view.getModuleViews(WorkerBuildingModuleView.class))
		{
			if (moduleView.getJobEntry() == jobEntry)
			{
				return moduleView.getAssignedCitizens();
			}

		}

		return Collections.emptyList();
	}

	private BuildingViewHelper()
	{

	}

}
