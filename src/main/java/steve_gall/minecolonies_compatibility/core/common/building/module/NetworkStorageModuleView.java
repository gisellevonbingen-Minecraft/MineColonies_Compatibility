package steve_gall.minecolonies_compatibility.core.common.building.module;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.NotNull;

import com.ldtteam.blockui.views.BOWindow;
import com.minecolonies.api.colony.buildings.modules.AbstractBuildingModuleView;
import com.minecolonies.api.util.Tuple;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import steve_gall.minecolonies_compatibility.core.client.gui.NetworkStorageModuleWindow;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;

public class NetworkStorageModuleView extends AbstractBuildingModuleView
{
	private final List<BlockPos> blocks;
	private final Map<BlockPos, Direction> directions;
	private int revision;

	public NetworkStorageModuleView()
	{
		this.blocks = new ArrayList<>();
		this.directions = new HashMap<>();
		this.revision = 0;
	}

	@Override
	public void deserialize(@NotNull FriendlyByteBuf buf)
	{
		this.blocks.clear();
		this.blocks.addAll(buf.readList(FriendlyByteBuf::readBlockPos));
		this.directions.clear();
		buf.readList(buf2 ->
		{
			var pos = buf2.readBlockPos();
			var direction = buf2.readEnum(Direction.class);
			return new Tuple<>(pos, direction);
		}).forEach(tuple -> this.directions.put(tuple.getA(), tuple.getB()));
		this.revision++;
	}

	public List<BlockPos> getBlocks()
	{
		return new ArrayList<>(this.blocks);
	}

	public Direction getDirection(BlockPos pos)
	{
		return this.directions.get(pos);
	}

	public int getRevision()
	{
		return revision;
	}

	@Override
	public BOWindow getWindow()
	{
		return new NetworkStorageModuleWindow(MineColoniesCompatibility.rl("gui/layouthuts/layoutnetworkstorage.xml").toString(), this);
	}

	@Override
	public String getIcon()
	{
		return "network_storage";
	}

	@Override
	public String getDesc()
	{
		return "com.minecolonies.coremod.gui.workerhuts." + this.getIcon();
	}

}
