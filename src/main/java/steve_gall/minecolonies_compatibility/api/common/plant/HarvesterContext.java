package steve_gall.minecolonies_compatibility.api.common.plant;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class HarvesterContext
{
	@Nullable
	private final LivingEntity entity;
	@NotNull
	private final ItemStack tool;

	public HarvesterContext(@Nullable LivingEntity entity, @NotNull ItemStack tool)
	{
		this.entity = entity;
		this.tool = tool;
	}

	@Nullable
	public LivingEntity getEntity()
	{
		return this.entity;
	}

	@NotNull
	public ItemStack getTool()
	{
		return this.tool;
	}

}
