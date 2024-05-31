package steve_gall.minecolonies_compatibility.core.common.tool;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

import com.minecolonies.api.util.constant.IToolType;
import com.minecolonies.api.util.constant.ToolType;

import net.minecraft.resources.ResourceLocation;
import steve_gall.minecolonies_compatibility.core.common.config.MineColoniesCompatibilityConfigServer;
import steve_gall.minecolonies_tweaks.api.common.tool.OrToolType;

public class KnightWeaponToolType extends OrToolType
{
	public KnightWeaponToolType(ResourceLocation name, Collection<Supplier<IToolType>> toolTypes)
	{
		super(name, toolTypes);
	}

	@Override
	public List<Supplier<IToolType>> getToolTypes()
	{
		var list = new ArrayList<>(super.getToolTypes());

		if (MineColoniesCompatibilityConfigServer.INSTANCE.jobs.knight.canUseAxe.get().booleanValue())
		{
			list.add(() -> ToolType.AXE);
		}

		return list;
	}

}
