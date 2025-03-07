package steve_gall.minecolonies_compatibility.api.common.entity.ai;

import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

import org.jetbrains.annotations.NotNull;

import com.minecolonies.api.entity.ai.statemachine.states.AIBlockingEventType;
import com.minecolonies.api.entity.ai.statemachine.states.IState;
import com.minecolonies.api.entity.ai.statemachine.states.IStateEventType;
import com.minecolonies.api.entity.ai.statemachine.tickratestatemachine.TickingTransition;
import com.minecolonies.api.entity.ai.statemachine.transitions.IStateMachineEvent;

public class AIInterruptEventTarget<S extends IState> extends TickingTransition<S> implements IStateMachineEvent<S>
{
	public AIInterruptEventTarget(@NotNull BooleanSupplier predicate, @NotNull Supplier<S> action, int tickRate)
	{
		super(predicate, action, tickRate);
	}

	public AIInterruptEventTarget(@NotNull Supplier<S> action, int tickRate)
	{
		this(() -> true, action, tickRate);
	}

	@Override
	public IStateEventType getEventType()
	{
		return AIBlockingEventType.STATE_BLOCKING;
	}

}
