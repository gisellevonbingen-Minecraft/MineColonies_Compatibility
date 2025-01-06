package steve_gall.minecolonies_compatibility.core.common.entity.pathfinding;

import java.util.ArrayList;
import java.util.List;

import com.minecolonies.api.entity.pathfinding.PathResult;

import net.minecraft.core.BlockPos;

@SuppressWarnings("rawtypes")
public class ButcherPositionsPathResult extends PathResult
{
	public final List<BlockPos> blocks = new ArrayList<>();

	public final List<BlockPos> tables = new ArrayList<>();
}
