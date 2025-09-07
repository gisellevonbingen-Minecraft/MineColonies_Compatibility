package steve_gall.minecolonies_compatibility.core.common.init;

import java.util.function.Function;

import com.minecolonies.api.colony.ICitizen;
import com.minecolonies.api.colony.interactionhandling.IInteractionResponseHandler;
import com.minecolonies.api.colony.interactionhandling.registry.InteractionResponseHandlerEntry;
import com.minecolonies.api.util.constant.Constants;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import steve_gall.minecolonies_compatibility.core.common.colony.WorkingBlockInteraction;

public class ModInteractions
{
	public final static DeferredRegister<InteractionResponseHandlerEntry> REGISTER = DeferredRegister.create(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "interactionresponsehandlers"), Constants.MOD_ID);

	public static final DeferredHolder<InteractionResponseHandlerEntry, InteractionResponseHandlerEntry> WORKING_BLOCK = register("compat_working_block", WorkingBlockInteraction::new);

	private static DeferredHolder<InteractionResponseHandlerEntry, InteractionResponseHandlerEntry> register(String name, Function<ICitizen, IInteractionResponseHandler> producer)
	{
		var id = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, name);
		return REGISTER.register(name, () -> new InteractionResponseHandlerEntry.Builder().setResponseHandlerProducer(producer).setRegistryName(id).createEntry());
	}

	private ModInteractions()
	{

	}

}
