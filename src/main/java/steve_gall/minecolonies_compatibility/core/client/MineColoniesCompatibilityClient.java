package steve_gall.minecolonies_compatibility.core.client;

import com.minecolonies.api.MinecoloniesAPIProxy;
import com.minecolonies.api.sounds.ModSoundEvents;

import net.neoforged.bus.api.EventPriority;
import net.neoforged.fml.javafmlmod.FMLModContainer;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.event.sound.PlaySoundEvent;
import net.neoforged.neoforge.common.NeoForge;
import steve_gall.minecolonies_compatibility.core.client.gui.AccessDirectionHolderScreen;
import steve_gall.minecolonies_compatibility.core.client.gui.BucketFillingTeachScreen;
import steve_gall.minecolonies_compatibility.core.client.gui.SmithingTeachScreen;
import steve_gall.minecolonies_compatibility.core.client.gui.SmithingTemplateInventoryScreen;
import steve_gall.minecolonies_compatibility.core.client.gui.StonecutterTeachScreen;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.core.common.init.ModMenuTypes;

public class MineColoniesCompatibilityClient
{
	public MineColoniesCompatibilityClient(FMLModContainer modContainer)
	{
		var fml_bus = modContainer.getEventBus();
		fml_bus.addListener(this::onRegisterMenuScreens);

		var forge_bus = NeoForge.EVENT_BUS;
		forge_bus.addListener(EventPriority.HIGH, this::onPlaySoundEvent);
	}

	private void onRegisterMenuScreens(RegisterMenuScreensEvent e)
	{
		e.register(ModMenuTypes.BUCKET_FILLING_TEACH.get(), BucketFillingTeachScreen::new);
		e.register(ModMenuTypes.SMITHING_TEACH.get(), SmithingTeachScreen::new);
		e.register(ModMenuTypes.SMITHING_TEMPLATE_INVENTORY.get(), SmithingTemplateInventoryScreen::new);
		e.register(ModMenuTypes.ACCESS_DIRECTION_HOLDER.get(), AccessDirectionHolderScreen::new);
		e.register(ModMenuTypes.STONECUTTING_TEACH.get(), StonecutterTeachScreen::new);
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
