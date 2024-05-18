package steve_gall.minecolonies_compatibility.core.common.entity.pathfinding;

import java.util.ArrayList;
import java.util.List;

import com.minecolonies.api.entity.pathfinding.PathResult;

import net.minecraft.core.BlockPos;

@SuppressWarnings("rawtypes")
public class MatchBlocksPathResult extends PathResult
{
	public final List<BlockPos> positions = new ArrayList<>();
}
