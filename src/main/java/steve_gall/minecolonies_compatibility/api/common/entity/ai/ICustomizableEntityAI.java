package steve_gall.minecolonies_compatibility.api.common.entity.ai;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.minecolonies.api.util.constant.ToolType;

import steve_gall.minecolonies_compatibility.core.common.entity.AbstractEntityAIBasicExtension;

public interface ICustomizableEntityAI
{
	@NotNull
	ToolType getHandToolType();

	@Nullable
	default CustomizedAI getSelectedAI()
	{
		return ((AbstractEntityAIBasicExtension) this).minecolonies_compatibility$getSelectedAI();
	}

	@Nullable
	default CustomizedAIContext getAIContext()
	{
		return ((AbstractEntityAIBasicExtension) this).minecolonies_compatibility$getAIContext();
	}

}
