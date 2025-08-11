package steve_gall.minecolonies_compatibility.api.common.entity.ai;

import org.jetbrains.annotations.NotNull;

import com.minecolonies.api.entity.ai.statemachine.states.AIBlockingEventType;
import com.minecolonies.api.entity.ai.statemachine.states.IState;
import com.minecolonies.api.entity.ai.statemachine.states.IStateEventType;
import com.minecolonies.api.entity.ai.statemachine.tickratestatemachine.IBooleanConditionSupplier;
import com.minecolonies.api.entity.ai.statemachine.tickratestatemachine.IStateSupplier;
import com.minecolonies.api.entity.ai.statemachine.tickratestatemachine.TickingTransition;
import com.minecolonies.api.entity.ai.statemachine.transitions.IStateMachineEvent;

public class AIInterruptEventTarget<S extends IState> extends TickingTransition<S> implements IStateMachineEvent<S>
{
	public AIInterruptEventTarget(@NotNull IBooleanConditionSupplier predicate, @NotNull IStateSupplier<S> action, int tickRate)
	{
		super(predicate, action, tickRate);
	}

	public AIInterruptEventTarget(@NotNull IStateSupplier<S> action, int tickRate)
	{
		this(() -> true, action, tickRate);
	}

	@Override
	public IStateEventType getEventType()
	{
		return AIBlockingEventType.STATE_BLOCKING;
	}

}
