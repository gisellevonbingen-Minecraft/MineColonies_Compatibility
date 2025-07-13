package steve_gall.minecolonies_compatibility.module.common.jade;

import com.minecolonies.api.tileentities.TileEntityColonyBuilding;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;

public class PostBoxRequestedComponentProvider implements IBlockComponentProvider
{
	public static final ResourceLocation UID = MineColoniesCompatibility.rl("postbox.requested");
	public static final PostBoxRequestedComponentProvider INSTANCE = new PostBoxRequestedComponentProvider();

	private PostBoxRequestedComponentProvider()
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

		if (blockAccessor.getBlockEntity() instanceof TileEntityColonyBuilding postBox)
		{
			var building = postBox.getBuildingView();

			if (building == null)
			{
				return;
			}

			var requests = building.getOpenRequestsOfBuilding();

			if (requests.size() > 0)
			{
				tooltip.add(Component.translatable("minecolonies_compatibility.text.delivery_requested"));

				for (var request : requests)
				{
					tooltip.add(Component.literal("- ").append(request.getShortDisplayString()));
				}

			}

		}

	}

}
