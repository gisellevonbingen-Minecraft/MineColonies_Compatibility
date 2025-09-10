package steve_gall.minecolonies_compatibility.core.common.init;

import java.util.function.Function;

import com.minecolonies.api.colony.ICitizen;
import com.minecolonies.api.colony.interactionhandling.IInteractionResponseHandler;
import com.minecolonies.api.colony.interactionhandling.registry.InteractionResponseHandlerEntry;
import com.minecolonies.api.util.constant.Constants;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import steve_gall.minecolonies_compatibility.core.common.colony.WorkingBlockInteraction;

public class ModInteractions
{
	public final static DeferredRegister<InteractionResponseHandlerEntry> REGISTER = DeferredRegister.create(new ResourceLocation(Constants.MOD_ID, "interactionresponsehandlers"), Constants.MOD_ID);

	public static final RegistryObject<InteractionResponseHandlerEntry> WORKING_BLOCK = register("compat_working_block", WorkingBlockInteraction::new);

	private static RegistryObject<InteractionResponseHandlerEntry> register(String name, Function<ICitizen, IInteractionResponseHandler> producer)
	{
		var id = new ResourceLocation(Constants.MOD_ID, name);
		return REGISTER.register(name, () -> new InteractionResponseHandlerEntry.Builder().setResponseHandlerProducer(producer).setRegistryName(id).createEntry());
	}

	private ModInteractions()
	{

	}

}
