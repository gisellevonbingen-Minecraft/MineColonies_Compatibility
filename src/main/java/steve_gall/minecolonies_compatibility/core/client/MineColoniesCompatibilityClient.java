package steve_gall.minecolonies_compatibility.core.client;

import com.minecolonies.api.MinecoloniesAPIProxy;
import com.minecolonies.api.sounds.ModSoundEvents;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import steve_gall.minecolonies_compatibility.core.client.gui.AccessDirectionHolderScreen;
import steve_gall.minecolonies_compatibility.core.client.gui.BucketFillingTeachScreen;
import steve_gall.minecolonies_compatibility.core.client.gui.SmithingTeachScreen;
import steve_gall.minecolonies_compatibility.core.client.gui.SmithingTemplateInventoryScreen;
import steve_gall.minecolonies_compatibility.core.client.gui.StonecutterTeachScreen;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.core.common.init.ModMenuTypes;

public class MineColoniesCompatibilityClient
{
	public MineColoniesCompatibilityClient()
	{
		var fml_bus = FMLJavaModLoadingContext.get().getModEventBus();
		fml_bus.addListener(this::onFMLClientSetup);

		var forge_bus = MinecraftForge.EVENT_BUS;
		forge_bus.addListener(EventPriority.HIGH, this::onPlaySoundEvent);
	}

	private void onFMLClientSetup(FMLClientSetupEvent e)
	{
		MenuScreens.register(ModMenuTypes.BUCKET_FILLING_TEACH.get(), BucketFillingTeachScreen::new);
		MenuScreens.register(ModMenuTypes.SMITHING_TEACH.get(), SmithingTeachScreen::new);
		MenuScreens.register(ModMenuTypes.SMITHING_TEMPLATE_INVENTORY.get(), SmithingTemplateInventoryScreen::new);
		MenuScreens.register(ModMenuTypes.ACCESS_DIRECTION_HOLDER.get(), AccessDirectionHolderScreen::new);
		MenuScreens.register(ModMenuTypes.STONECUTTING_TEACH.get(), StonecutterTeachScreen::new);
	}

	private void onPlaySoundEvent(PlaySoundEvent event)
	{
		if (event.getSound() == null)
		{
			return;
		}

		var soundLocation = event.getSound().getLocation();

		if (!MinecoloniesAPIProxy.getInstance().getConfig().getClient().citizenVoices.get() && soundLocation.getNamespace().equals(MineColoniesCompatibility.MOD_ID) && soundLocation.getPath().startsWith(ModSoundEvents.CITIZEN_SOUND_EVENT_PREFIX))
		{
			event.setSound(null);
		}

	}

}
