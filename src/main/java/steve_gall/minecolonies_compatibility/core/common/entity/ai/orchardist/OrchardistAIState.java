package steve_gall.minecolonies_compatibility.core.common.entity.ai.orchardist;

import com.minecolonies.api.entity.ai.statemachine.states.IAIState;

public enum OrchardistAIState implements IAIState
{
	SEARCH(true),
	HARVEST(false),
	//
	;

	private boolean isOkayToEat;

	OrchardistAIState(boolean isOkayToEat)
	{
		this.isOkayToEat = isOkayToEat;
	}

	@Override
	public boolean isOkayToEat()
	{
		return this.isOkayToEat;
	}

}
