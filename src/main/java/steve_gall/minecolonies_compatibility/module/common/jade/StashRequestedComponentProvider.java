package steve_gall.minecolonies_compatibility.module.common.jade;

import com.minecolonies.api.tileentities.TileEntityStash;
import com.minecolonies.core.colony.requestsystem.requests.StandardRequests.PickupRequest;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;

public class StashRequestedComponentProvider implements IBlockComponentProvider
{
	public static final ResourceLocation UID = MineColoniesCompatibility.rl("stash.requested");
	public static final StashRequestedComponentProvider INSTANCE = new StashRequestedComponentProvider();

	private StashRequestedComponentProvider()
	{

	}

	@Override
	public ResourceLocation getUid()
	{
		return UID;
	}

	@Override
	public boolean isRequired()
	{
		return true;
	}

	@Override
	public void appendTooltip(ITooltip tooltip, BlockAccessor blockAccessor, IPluginConfig pluginConfig)
	{
		if (!pluginConfig.get(UID))
		{
			return;
		}

		if (blockAccessor.getBlockEntity() instanceof TileEntityStash stash)
		{
			var building = stash.getBuildingView();

			if (building == null)
			{
				return;
			}

			for (var request : building.getOpenRequestsOfBuilding())
			{
				if (request instanceof PickupRequest)
				{
					tooltip.add(Component.translatable("minecolonies_compatibility.text.pickup_requested"));
					break;
				}

			}

		}

	}

}
