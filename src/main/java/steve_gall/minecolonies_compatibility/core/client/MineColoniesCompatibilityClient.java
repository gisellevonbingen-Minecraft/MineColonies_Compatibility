package steve_gall.minecolonies_compatibility.core.client;

import com.minecolonies.api.MinecoloniesAPIProxy;
import com.minecolonies.api.sounds.ModSoundEvents;

import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;

public class MineColoniesCompatibilityClient
{
	public MineColoniesCompatibilityClient()
	{
		var fml_bus = FMLJavaModLoadingContext.get().getModEventBus();
		var forge_bus = MinecraftForge.EVENT_BUS;

		forge_bus.addListener(EventPriority.HIGH, this::onPlaySoundEvent);
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
