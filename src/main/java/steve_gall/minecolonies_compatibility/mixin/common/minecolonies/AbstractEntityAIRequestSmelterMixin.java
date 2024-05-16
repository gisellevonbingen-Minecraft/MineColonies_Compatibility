package steve_gall.minecolonies_compatibility.mixin.common.minecolonies;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.minecolonies.core.colony.buildings.AbstractBuilding;
import com.minecolonies.core.colony.jobs.AbstractJobCrafter;
import com.minecolonies.core.entity.ai.basic.AbstractEntityAIRequestSmelter;

@Mixin(value = AbstractEntityAIRequestSmelter.class, remap = false)
public abstract class AbstractEntityAIRequestSmelterMixin<J extends AbstractJobCrafter<?, J>, B extends AbstractBuilding> extends AbstractEntityAICraftingMixin<J, B>
{
	public AbstractEntityAIRequestSmelterMixin(@NotNull J job)
	{
		super(job);
	}

	@Redirect(method = "craft", remap = false, at = @At(value = "INVOKE", target = "walkToBuilding"))
	protected boolean craft_walkToBuilding(AbstractEntityAIRequestSmelter<J, B> self)
	{
		return super.craft_walkToBuilding(self);
	}

}
