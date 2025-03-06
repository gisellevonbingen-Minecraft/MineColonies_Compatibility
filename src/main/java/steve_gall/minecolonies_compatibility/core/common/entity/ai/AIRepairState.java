package steve_gall.minecolonies_compatibility.core.common.entity.ai;

import com.minecolonies.api.entity.ai.statemachine.states.IAIState;

public enum AIRepairState implements IAIState
{
	REPAIR(true),
	//
	;

	private boolean isOkayToEat;

	AIRepairState(boolean isOkayToEat)
	{
		this.isOkayToEat = isOkayToEat;
	}

	@Override
	public boolean isOkayToEat()
	{
		return this.isOkayToEat;
	}

}
