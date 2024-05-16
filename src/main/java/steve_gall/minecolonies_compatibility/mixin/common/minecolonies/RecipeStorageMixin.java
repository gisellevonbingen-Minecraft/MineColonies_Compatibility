package steve_gall.minecolonies_compatibility.mixin.common.minecolonies;

import java.util.List;

import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.minecolonies.api.crafting.RecipeStorage;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraftforge.items.IItemHandler;
import steve_gall.minecolonies_compatibility.api.common.crafting.ISecondaryRollableRecipeStorage;
import steve_gall.minecolonies_tweaks.api.common.crafting.DelegateRecipeStorage;

@Mixin(value = RecipeStorage.class, remap = false)
public abstract class RecipeStorageMixin
{
	@Unique
	private LootContext minecolonies_compatibility$context;

	@Redirect(method = "insertCraftedItems", at = @At(value = "FIELD", target = "secondaryOutputs", opcode = Opcodes.GETFIELD))
	private List<ItemStack> insertCraftedItems_secondaryOutputs(RecipeStorage self)
	{
		if (self instanceof DelegateRecipeStorage delegate && delegate.getParent().getImpl() instanceof ISecondaryRollableRecipeStorage crafting)
		{
			return crafting.rollSecondaryOutputs(this.minecolonies_compatibility$context);
		}

		return self.getSecondaryOutputs();
	}

	@Inject(method = "insertCraftedItems", remap = false, at = @At(value = "HEAD"), cancellable = true)
	private void insertCraftedItems_Head(List<IItemHandler> handlers, ItemStack outputStack, LootContext context, boolean doInsert, CallbackInfoReturnable<List<ItemStack>> cir)
	{
		this.minecolonies_compatibility$context = context;
	}

}
