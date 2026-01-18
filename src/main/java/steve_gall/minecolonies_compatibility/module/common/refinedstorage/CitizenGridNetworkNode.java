package steve_gall.minecolonies_compatibility.module.common.refinedstorage;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import com.refinedmods.refinedstorage.api.network.Network;
import com.refinedmods.refinedstorage.api.network.impl.node.AbstractNetworkNode;
import com.refinedmods.refinedstorage.api.network.storage.StorageNetworkComponent;
import com.refinedmods.refinedstorage.api.resource.list.MutableResourceList.OperationResult;
import com.refinedmods.refinedstorage.api.storage.root.RootStorageListener;
import com.refinedmods.refinedstorage.common.support.resource.ItemResource;

import net.minecraft.world.item.ItemStack;
import steve_gall.minecolonies_compatibility.core.common.config.MineColoniesCompatibilityConfigServer;

public class CitizenGridNetworkNode extends AbstractNetworkNode
{
	private final List<Consumer<ItemStack>> listeners;
	private final StorageListener listener;

	public CitizenGridNetworkNode()
	{
		this.listeners = new ArrayList<Consumer<ItemStack>>();
		this.listener = new StorageListener();
	}

	public boolean addListener(Consumer<ItemStack> listener)
	{
		return this.listeners.add(listener);
	}

	public boolean removeListener(Consumer<ItemStack> listener)
	{
		return this.listeners.remove(listener);
	}

	@Override
	public long getEnergyUsage()
	{
		return MineColoniesCompatibilityConfigServer.INSTANCE.modules.RS.citizen_grid_energyUsage.get().intValue();
	}

	@Override
	public void setNetwork(Network network)
	{
		if (this.network != null)
		{
			var storage = this.network.getComponent(StorageNetworkComponent.class);
			storage.removeListener(this.listener);
		}

		super.setNetwork(network);

		if (this.network != null)
		{
			var storage = this.network.getComponent(StorageNetworkComponent.class);
			storage.addListener(this.listener);
		}

	}

	public class StorageListener implements RootStorageListener
	{
		@Override
		public void changed(OperationResult result)
		{
			if (result.resource() instanceof ItemResource resource)
			{
				for (var listener : listeners)
				{
					if (result.change() > 0L)
					{
						listener.accept(resource.toItemStack());
					}

				}

			}

		}

	}

}
