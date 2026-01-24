package steve_gall.minecolonies_compatibility.mixin.common.refinedstorage;

import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import com.refinedmods.refinedstorage.api.autocrafting.task.TaskId;
import com.refinedmods.refinedstorage.api.network.autocrafting.PatternProvider;
import com.refinedmods.refinedstorage.api.network.impl.autocrafting.AutocraftingNetworkComponentImpl;

@Mixin(value = AutocraftingNetworkComponentImpl.class, remap = false)
public interface AutocraftingNetworkComponentImplAccessor
{
	@Accessor(value = "providerByTaskId", remap = false)
	Map<TaskId, PatternProvider> getProviderByTaskId();
}
