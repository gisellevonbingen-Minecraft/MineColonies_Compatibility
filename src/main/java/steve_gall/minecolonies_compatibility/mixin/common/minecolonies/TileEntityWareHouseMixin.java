package steve_gall.minecolonies_compatibility.mixin.common.minecolonies;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.minecolonies.api.inventory.InventoryCitizen;
import com.minecolonies.api.tileentities.AbstractTileEntityWareHouse;
import com.minecolonies.api.util.Tuple;
import com.minecolonies.core.tileentities.TileEntityWareHouse;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import steve_gall.minecolonies_compatibility.core.common.init.ModBuildingModules;

@Mixin(value = TileEntityWareHouse.class, remap = false)
public abstract class TileEntityWareHouseMixin extends AbstractTileEntityWareHouse
{
	public TileEntityWareHouseMixin(BlockEntityType<? extends AbstractTileEntityWareHouse> warehouse, BlockPos pos, BlockState state)
	{
		super(warehouse, pos, state);
	}

	@Inject(method = "hasMatchingItemStackInWarehouse(Lnet/minecraft/world/item/ItemStack;IZZI)Z", remap = false, at = @At("TAIL"), cancellable = true)
	public void hasMatchingItemStackInWarehouse(ItemStack itemStack, int count, boolean ignoreNBT, boolean ignoreDamage, int leftOver, CallbackInfoReturnable<Boolean> cir)
	{
		var module = this.getBuilding().getModule(ModBuildingModules.NETWORK_STORAGE);

		if (module != null && module.hasMatchingItemStack(itemStack, count, ignoreNBT, ignoreDamage, leftOver))
		{
			cir.setReturnValue(true);
		}

	}

	@Inject(method = "getMatchingItemStacksInWarehouse", remap = false, at = @At("TAIL"), cancellable = true)
	private void getMatchingItemStacksInWarehouse(Predicate<ItemStack> itemStackSelectionPredicate, CallbackInfoReturnable<List<Tuple<ItemStack, BlockPos>>> cir)
	{
		var module = this.getBuilding().getModule(ModBuildingModules.NETWORK_STORAGE);

		if (module != null)
		{
			var list = module.getMatchingItemStacks(itemStackSelectionPredicate).toList();

			if (list.size() > 0)
			{
				var ret = new ArrayList<>(cir.getReturnValue());
				ret.addAll(list);
				cir.setReturnValue(ret);
			}

		}

	}

	@Inject(method = "dumpInventoryIntoWareHouse", remap = false, at = @At("HEAD"), cancellable = true)
	private void dumpInventoryIntoWareHouse(InventoryCitizen inventoryCitizen, CallbackInfo ci)
	{
		var module = this.getBuilding().getModule(ModBuildingModules.NETWORK_STORAGE);

		if (module != null)
		{
			module.dump(inventoryCitizen);
		}

	}

}
