package steve_gall.minecolonies_compatibility.core.common.entity.pathfinding;

import org.jetbrains.annotations.Nullable;

import com.minecolonies.api.entity.pathfinding.PathResult;

import steve_gall.minecolonies_compatibility.core.common.entity.ai.orchardist.Fruit;

@SuppressWarnings("rawtypes")
public class FruitPathResult extends PathResult
{
	@Nullable
	public Fruit fruit;
}
