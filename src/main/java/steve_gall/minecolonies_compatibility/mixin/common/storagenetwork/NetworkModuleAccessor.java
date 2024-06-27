package steve_gall.minecolonies_compatibility.mixin.common.storagenetwork;

import java.util.Set;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import com.lothrazar.storagenetwork.api.IConnectableLink;
import com.lothrazar.storagenetwork.block.main.NetworkModule;

@Mixin(value = NetworkModule.class, remap = false)
public interface NetworkModuleAccessor
{
	@Invoker(value = "getConnectableStorage", remap = false)
	Set<IConnectableLink> invokeGetConnectableStorage();
}
