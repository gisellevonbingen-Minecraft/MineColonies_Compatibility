package steve_gall.minecolonies_compatibility.module.common.farmersdelight.entity.ai;

import com.minecolonies.api.entity.ai.statemachine.states.IAIState;

public enum FarmersCookAIState implements IAIState
{
	INSERT_INPUT(true),
	EXTRACT_OUTPUT(true),
	//
	;

	private final boolean okayToEat;

	private FarmersCookAIState(boolean okayToEat)
	{
		this.okayToEat = okayToEat;
	}

	@Override
	public boolean isOkayToEat()
	{
		return this.okayToEat;
	}

}
