package steve_gall.minecolonies_compatibility.api.common.butcher;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class ButcherableIconCache
{
	@NotNull
	private final CustomizedButcherable butcherable;
	@NotNull
	private final List<ItemStack> itemIcons;
	@NotNull
	private final List<ItemStack> outputIcons;
	@NotNull
	private final List<BlockState> tableIcons;

	public ButcherableIconCache(@NotNull CustomizedButcherable butcherable)
	{
		this.butcherable = butcherable;
		this.itemIcons = butcherable.getItemIcons();
		this.outputIcons = butcherable.getOutputIcons();
		this.tableIcons = butcherable.getTableIcons();
	}

	@NotNull
	public CustomizedButcherable getButcherable()
	{
		return this.butcherable;
	}

	@NotNull
	public List<ItemStack> getItemIcons()
	{
		return this.itemIcons;
	}

	@NotNull
	public List<ItemStack> getOutputIcons()
	{
		return this.outputIcons;
	}

	@NotNull
	public List<BlockState> getTableIcons()
	{
		return this.tableIcons;
	}

}
