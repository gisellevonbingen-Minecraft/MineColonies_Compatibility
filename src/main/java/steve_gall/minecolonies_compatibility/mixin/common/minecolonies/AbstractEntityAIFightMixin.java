package steve_gall.minecolonies_compatibility.mixin.common.minecolonies;

import java.util.Arrays;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.minecolonies.api.entity.ai.citizen.guards.GuardGear;
import com.minecolonies.api.util.InventoryUtils;
import com.minecolonies.core.colony.buildings.AbstractBuildingGuards;
import com.minecolonies.core.colony.jobs.AbstractJobGuard;
import com.minecolonies.core.entity.ai.basic.AbstractEntityAIFight;
import com.minecolonies.core.entity.ai.basic.AbstractEntityAIInteract;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import steve_gall.minecolonies_compatibility.api.common.tool.CustomizedToolSystem;

@Mixin(value = AbstractEntityAIFight.class, remap = false)
public abstract class AbstractEntityAIFightMixin<J extends AbstractJobGuard<J>, B extends AbstractBuildingGuards> extends AbstractEntityAIInteract<J, B>
{
	@Shadow(remap = false)
	private List<List<GuardGear>> itemsNeeded;

	public AbstractEntityAIFightMixin(@NotNull J job)
	{
		super(job);
	}

	@Inject(method = "atBuildingActions", remap = false, at = @At(value = "TAIL"), cancellable = true)
	private void atBuildingActions(CallbackInfo ci)
	{
		this.dumpBrokenArmors();
	}

	@Inject(method = "equipInventoryArmor", remap = false, at = @At(value = "HEAD"), cancellable = false)
	private void equipInventoryArmor(CallbackInfo ci)
	{
		this.dumpBrokenArmors();
	}

	private void dumpBrokenArmors()
	{
		var inventory = this.worker.getInventoryCitizen();

		for (var slot : Arrays.asList(EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.LEGS))
		{
			var armor = inventory.getArmorInSlot(slot);
			var system = CustomizedToolSystem.select(armor);

			if (system != null && system.isBroken(armor))
			{
				if (InventoryUtils.transferItemStackIntoNextBestSlotInItemHandler(armor, this.getBuildingToDump().getCapability(ForgeCapabilities.ITEM_HANDLER, null).orElseGet(null)))
				{
					inventory.forceClearArmorInSlot(slot, armor);
				}
				else
				{
					inventory.moveArmorToInventory(slot);
				}

			}

		}

	}

}
