package steve_gall.minecolonies_compatibility.api.common.plant;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import net.minecraft.world.item.ItemStack;

public class FruitIconCache
{
	@NotNull
	private final CustomizedFruit fruit;
	@NotNull
	private final List<ItemStack> blockIcons;
	@NotNull
	private final List<ItemStack> itemIcons;

	public FruitIconCache(@NotNull CustomizedFruit fruit)
	{
		this.fruit = fruit;
		this.blockIcons = fruit.getBlockIcons().stream().map(ItemStack::copy).toList();
		this.itemIcons = fruit.getItemIcons().stream().map(ItemStack::copy).toList();
	}

	@NotNull
	public CustomizedFruit getFruit()
	{
		return fruit;
	}

	@NotNull
	public List<ItemStack> getBlockIcons()
	{
		return blockIcons;
	}

	@NotNull
	public List<ItemStack> getItemIcons()
	{
		return itemIcons;
	}

}
