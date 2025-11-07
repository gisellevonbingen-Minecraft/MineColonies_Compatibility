package steve_gall.minecolonies_compatibility.mixin.client.minecolonies;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import com.minecolonies.api.colony.buildings.modules.IBuildingModuleView;
import com.minecolonies.core.client.gui.AbstractModuleWindow;

@Mixin(value = AbstractModuleWindow.class, remap = false)
public interface AbstractModuleWindowAccessor<T extends IBuildingModuleView>
{
	@Accessor(value = "moduleView", remap = false)
	T getModuleView();
}
