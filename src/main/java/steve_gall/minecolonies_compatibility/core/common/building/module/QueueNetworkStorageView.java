package steve_gall.minecolonies_compatibility.core.common.building.module;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Queue;

import net.minecraft.world.item.ItemStack;

public abstract class QueueNetworkStorageView extends AbstractNetworkStorageView
{
	public static final int DEQUEUE_COUNT = 18;

	private final Queue<ItemStack> queue = new ArrayDeque<>();
	private boolean wasCanRequest = false;
	private boolean allRequested = false;

	@Override
	public void tick()
	{
		super.tick();

		if (this.wasActive())
		{
			this.onActiveTick();
		}

	}

	protected void onActiveTick()
	{
		if (this.canRequest())
		{
			if (!this.wasCanRequest)
			{
				this.requestAll();
			}

			this.wasCanRequest = true;
		}
		else
		{
			this.wasCanRequest = false;
			this.allRequested = false;
			this.queue.clear();
		}

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

	@Override
	protected void onActiveChanged(boolean isActive)
	{
		super.onActiveChanged(isActive);

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
		return !this.allRequested && this.wasActive();
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
