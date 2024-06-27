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

		if (this.wasActive)
		{
			this.onActiveTick();
		}

	}

	protected void onActiveTick()
	{
		if (this.allRequested)
		{
			this.allRequested = false;
			this.queue.clear();
			this.getAllStacks().forEach(this.queue::add);
		}

		var module = this.getLinkedModule();

		if (module != null)
		{
			for (var i = 0; i < DEQUEUE_COUNT; i++)
			{
				var stack = this.queue.poll();

				if (stack == null)
				{
					break;
				}

				module.onItemIncremented(stack);
			}

		}

	}

	private void updateActive()
	{
		var isActive = this.isActive() && this.getLinkedModule() != null;

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
