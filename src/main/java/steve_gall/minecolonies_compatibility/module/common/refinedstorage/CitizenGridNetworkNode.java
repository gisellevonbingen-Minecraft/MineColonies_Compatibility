package steve_gall.minecolonies_compatibility.module.common.refinedstorage;

import java.util.ArrayList;
import java.util.List;

import com.refinedmods.refinedstorage.api.autocrafting.Pattern;
import com.refinedmods.refinedstorage.api.network.Network;
import com.refinedmods.refinedstorage.api.network.autocrafting.AutocraftingNetworkComponent;
import com.refinedmods.refinedstorage.api.network.autocrafting.PatternListener;
import com.refinedmods.refinedstorage.api.network.impl.node.AbstractNetworkNode;
import com.refinedmods.refinedstorage.api.network.storage.StorageNetworkComponent;
import com.refinedmods.refinedstorage.api.resource.list.MutableResourceList.OperationResult;
import com.refinedmods.refinedstorage.api.storage.root.RootStorageListener;
import com.refinedmods.refinedstorage.common.support.resource.ItemResource;

import net.minecraft.world.item.ItemStack;
import steve_gall.minecolonies_compatibility.core.common.config.MineColoniesCompatibilityConfigServer;

public class CitizenGridNetworkNode extends AbstractNetworkNode
{
	private final InternalListener internalListener;
	private final List<ExternalListener> externalListeners;

	public CitizenGridNetworkNode()
	{
		this.internalListener = new InternalListener();
		this.externalListeners = new ArrayList<>();
	}

	public boolean addExternalListener(ExternalListener listener)
	{
		return this.externalListeners.add(listener);
	}

	public boolean removeExternalListener(ExternalListener listener)
	{
		return this.externalListeners.remove(listener);
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
			storage.removeListener(this.internalListener);

			var autocrafting = this.network.getComponent(AutocraftingNetworkComponent.class);
			autocrafting.removeListener(this.internalListener);
		}

		super.setNetwork(network);

		if (this.network != null)
		{
			var storage = this.network.getComponent(StorageNetworkComponent.class);
			storage.addListener(this.internalListener);

			var autocrafting = this.network.getComponent(AutocraftingNetworkComponent.class);
			autocrafting.addListener(this.internalListener);
		}

	}

	public interface ExternalListener
	{
		void onChanged(ItemStack item);

		public void onAdded(Pattern pattern);

		void onRemoved(Pattern pattern);
	}

	public class InternalListener implements RootStorageListener, PatternListener
	{
		@Override
		public void changed(OperationResult result)
		{
			if (result.resource() instanceof ItemResource resource)
			{
				for (var listener : externalListeners)
				{
					if (result.change() > 0L)
					{
						listener.onChanged(resource.toItemStack());
					}

				}

			}

		}

		@Override
		public void onAdded(Pattern pattern)
		{
			externalListeners.forEach(p -> p.onAdded(pattern));
		}

		@Override
		public void onRemoved(Pattern pattern)
		{
			externalListeners.forEach(p -> p.onRemoved(pattern));
		}

	}

}
