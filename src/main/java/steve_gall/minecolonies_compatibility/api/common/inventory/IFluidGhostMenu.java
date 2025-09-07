package steve_gall.minecolonies_compatibility.api.common.inventory;

import net.neoforged.neoforge.fluids.FluidStack;

public interface IFluidGhostMenu
{
	void onGhostAcceptFluid(int slotNumber, FluidStack stack);
}
