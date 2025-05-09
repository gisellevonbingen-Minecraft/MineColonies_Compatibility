package steve_gall.minecolonies_compatibility.mixin.common.minecolonies;

import java.util.List;

import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.minecolonies.api.crafting.RecipeStorage;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraftforge.items.IItemHandler;
import steve_gall.minecolonies_compatibility.api.common.crafting.ISecondaryRollableRecipeStorage;
import steve_gall.minecolonies_tweaks.core.common.crafting.RecipeStorageExtension;

@Mixin(value = RecipeStorage.class, remap = false)
public abstract class RecipeStorageMixin
{
	@Unique
	private LootParams minecolonies_compatibility$context;

	@WrapOperation(method = "insertCraftedItems", at = @At(value = "FIELD", target = "secondaryOutputs", opcode = Opcodes.GETFIELD))
	private List<ItemStack> insertCraftedItems_secondaryOutputs(RecipeStorage self, Operation<List<ItemStack>> operation)
	{
		if (((RecipeStorageExtension) self).minecolonies_tweaks$getCustomized() instanceof ISecondaryRollableRecipeStorage crafting)
		{
			return crafting.rollSecondaryOutputs(this.minecolonies_compatibility$context);
		}

		return operation.call(self);
	}

	@Inject(method = "insertCraftedItems", remap = false, at = @At(value = "HEAD"), cancellable = true)
	private void insertCraftedItems_Head(List<IItemHandler> handlers, ItemStack outputStack, LootParams context, boolean doInsert, CallbackInfoReturnable<List<ItemStack>> cir)
	{
		this.minecolonies_compatibility$context = context;
	}

}
