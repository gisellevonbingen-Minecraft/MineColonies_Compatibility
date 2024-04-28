package steve_gall.minecolonies_compatibility.core.common.entity.pathfinding;

import org.jetbrains.annotations.Nullable;

import com.minecolonies.api.entity.pathfinding.PathResult;
import com.minecolonies.core.entity.pathfinding.pathjobs.AbstractPathJob;

import steve_gall.minecolonies_compatibility.core.common.entity.ai.orchardist.Fruit;

public class FruitPathResult extends PathResult<AbstractPathJob>
{
	@Nullable
	public Fruit fruit;
}
