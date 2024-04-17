package steve_gall.minecolonies_compatibility.api.common.entity;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.minecolonies.api.util.constant.ToolType;

public interface ICustomizableEntityAI
{
	@NotNull
	ToolType getHandToolType();

	@Nullable
	CustomizedCitizenAI getSelectedAI();

	@Nullable
	CustomizedAIContext getAIContext();
}
