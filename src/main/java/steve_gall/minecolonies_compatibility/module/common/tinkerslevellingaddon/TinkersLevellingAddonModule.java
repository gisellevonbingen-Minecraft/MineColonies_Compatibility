package steve_gall.minecolonies_compatibility.module.common.tinkerslevellingaddon;

import java.util.function.Consumer;

import com.minecolonies.core.entity.citizen.EntityCitizen;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import pyre.tinkerslevellingaddon.setup.Registration;
import pyre.tinkerslevellingaddon.util.ToolLevellingUtil;
import slimeknights.tconstruct.library.tools.item.ModifiableItem;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import steve_gall.minecolonies_compatibility.module.common.AbstractModule;

public class TinkersLevellingAddonModule extends AbstractModule
{
	public static void onHurtAndBreak(ItemStack stack, int damage, EntityCitizen citizen, Consumer<? extends LivingEntity> consumer)
	{
		if (stack.getItem() instanceof ModifiableItem)
		{
			var tool = ToolStack.from(stack);

			if (tool.getModifier(Registration.IMPROVABLE.get()) != null)
			{
				ToolLevellingUtil.addExperience(tool, damage, null);
			}

		}

	}

}
