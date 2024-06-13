package steve_gall.minecolonies_compatibility.core.common.building.module;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Queue;

import net.minecraft.world.item.ItemStack;

public abstract class QueueNetworkStorageView extends AbstractNetworkStorageView
{
	private static final int DEQUEUE_COUNT = 18;

	private final Queue<ItemStack> queue = new ArrayDeque<>();
	private boolean allRequested = false;
	private boolean wasActive = false;

	public void onTick()
	{
		this.updateActive();

		if (this.allRequested && this.wasActive)
		{
			this.allRequested = false;
			this.queue.clear();
			this.enqueueAll();
		}

		var module = this.getLinkedModule();

		for (var i = 0; i < DEQUEUE_COUNT; i++)
		{
			var stack = this.queue.poll();

			if (stack == null)
			{
				break;
			}
			else if (module != null)
			{
				module.onItemIncremented(stack);
			}

		}

	}

	private void updateActive()
	{
		var isActive = this.isActive();

		if (this.wasActive != isActive)
		{
			this.onActiveChanged(isActive);
		}

		this.wasActive = isActive;
	}

	protected void onActiveChanged(boolean isActive)
	{
		if (isActive)
		{
			this.requestAll();
		}
		else
		{
			this.clearQueue();
		}

	}

	protected abstract void enqueueAll();

	@Override
	public void link(NetworkStorageModule module)
	{
		super.link(module);

		this.requestAll();
	}

	@Override
	public void unlink()
	{
		super.unlink();

		this.clearQueue();
	}

	public void requestAll()
	{
		this.allRequested = true;
	}

	public void clearQueue()
	{
		this.allRequested = false;
		this.queue.clear();
	}

	public boolean canEnqueue()
	{
		return !this.allRequested && this.wasActive;
	}

	public void enqueue(ItemStack stack)
	{
		if (this.canEnqueue())
		{
			this.queue.add(stack);
		}

	}

	public void enqueue(Collection<? extends ItemStack> stacks)
	{
		if (this.canEnqueue())
		{
			this.queue.addAll(stacks);
		}

	}

}
