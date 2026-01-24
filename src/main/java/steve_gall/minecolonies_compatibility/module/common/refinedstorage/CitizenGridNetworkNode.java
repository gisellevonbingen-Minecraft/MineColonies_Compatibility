package steve_gall.minecolonies_compatibility.module.common.refinedstorage;

import java.util.ArrayList;
import java.util.List;

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
	private final List<StorageListener> storageListeners;
	private final StorageChangedListener storageChangedListener;

	public CitizenGridNetworkNode()
	{
		this.storageListeners = new ArrayList<>();
		this.storageChangedListener = new StorageChangedListener();
		this.patternListeners = new ArrayList<>();
		this.patternChangedListener = new PatternChangedListener();
	}

	public boolean addStorageListener(StorageListener listener)
	{
		return this.storageListeners.add(listener);
	}

	public boolean removeStorageListener(StorageListener listener)
	{
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
			storage.removeListener(this.storageChangedListener);
		}

		super.setNetwork(network);

		if (this.network != null)
		{
			var storage = this.network.getComponent(StorageNetworkComponent.class);
			storage.addListener(this.storageChangedListener);
		}

	}

	@FunctionalInterface
	public interface StorageListener
	{
		void onChanged(ItemStack item);
	}

	public class StorageChangedListener implements RootStorageListener
	{
		@Override
		public void changed(OperationResult result)
		{
			if (result.resource() instanceof ItemResource resource)
			{
				for (var listener : storageListeners)
				{
					if (result.change() > 0L)
					{
						listener.onChanged(resource.toItemStack());
					}

				}

			}

		}

	}

}
