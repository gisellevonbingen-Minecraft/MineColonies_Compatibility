package steve_gall.minecolonies_compatibility.core.common.entity.ai.butcher;

import com.minecolonies.api.entity.ai.statemachine.states.IAIState;

public enum ButcherAIState implements IAIState
{
	SEARCH(true),
	BUTCHER(true),
	//
	;

	private boolean isOkayToEat;

	ButcherAIState(boolean isOkayToEat)
	{
		this.isOkayToEat = isOkayToEat;
	}

	@Override
	public boolean isOkayToEat()
	{
		return this.isOkayToEat;
	}

}
