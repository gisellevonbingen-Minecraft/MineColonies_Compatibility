package steve_gall.minecolonies_compatibility.core.common.tool;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

import org.jetbrains.annotations.NotNull;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import steve_gall.minecolonies_tweaks.api.common.tool.CustomToolType;

public class GunToolType extends CustomToolType
{
	private final Set<Predicate<Item>> predicates = new HashSet<>();
	private final Set<Item> items = new HashSet<>();
	private final Set<Item> tested = new HashSet<>();

	public GunToolType(@NotNull ResourceLocation name)
	{
		super(name);
	}

	@Override
	public int getToolLevel(@NotNull ItemStack stack)
	{
		return 1;
	}

	@Override
	public boolean isTool(@NotNull ItemStack stack)
	{
		var item = stack.getItem();

		if (this.items.contains(item))
		{
			return true;
		}
		else if (this.tested.contains(item))
		{
			return false;
		}
		else
		{
			this.tested.add(item);

			for (var predicate : this.predicates)
			{
				if (predicate.test(item))
				{
					this.items.add(item);
					return true;
				}

			}

			return false;
		}

	}

	public boolean register(Predicate<Item> predicate)
	{
		return this.predicates.add(predicate);
	}

	public boolean register(Item item)
	{
		this.tested.add(item);
		return this.items.add(item);
	}

}
