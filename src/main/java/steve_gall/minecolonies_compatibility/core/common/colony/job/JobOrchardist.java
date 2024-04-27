package steve_gall.minecolonies_compatibility.core.common.colony.job;

import static com.minecolonies.api.util.constant.CitizenConstants.SKILL_BONUS_ADD;

import org.jetbrains.annotations.Nullable;

import com.minecolonies.api.client.render.modeltype.ModModelTypes;
import com.minecolonies.api.colony.ICitizenData;
import com.minecolonies.api.entity.citizen.AbstractEntityCitizen;
import com.minecolonies.core.colony.buildings.modules.WorkerBuildingModule;
import com.minecolonies.core.colony.jobs.AbstractJob;
import com.minecolonies.core.util.AttributeModifierUtils;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import steve_gall.minecolonies_compatibility.core.common.config.MineColoniesCompatibilityConfigServer;
import steve_gall.minecolonies_compatibility.core.common.entity.ai.orchardist.EntityAIWorkOrchardist;
import steve_gall.minecolonies_compatibility.core.common.entity.ai.orchardist.Fruit;

public class JobOrchardist extends AbstractJob<EntityAIWorkOrchardist, JobOrchardist>
{
	public static final String TAG_FRUIT = "Fruit";

	@Nullable
	private Fruit fruit;

	public JobOrchardist(ICitizenData entity)
	{
		super(entity);
	}

	@Override
	public void onLevelUp()
	{
		this.getCitizen().getEntity().ifPresent(this::onLevelUp);
	}

	private void onLevelUp(AbstractEntityCitizen worker)
	{
		var citizen = this.getCitizen();
		var config = MineColoniesCompatibilityConfigServer.INSTANCE.jobs.orchardist;
		var module = (WorkerBuildingModule) this.getWorkModule();
		var amount = citizen.getCitizenSkillHandler().getLevel(module.getSecondarySkill()) * config.moveSpeedBonusPerSkillLevel.get().doubleValue();
		var speedModifier = new AttributeModifier(SKILL_BONUS_ADD, amount, AttributeModifier.Operation.ADDITION);
		AttributeModifierUtils.addModifier(worker, speedModifier, Attributes.MOVEMENT_SPEED);
	}

	@Override
	public CompoundTag serializeNBT()
	{
		var compound = super.serializeNBT();

		if (this.fruit != null)
		{
			var fruitTag = new CompoundTag();
			this.fruit.serialize(fruitTag);
			compound.put(TAG_FRUIT, fruitTag);
		}

		return compound;
	}

	@Override
	public void deserializeNBT(CompoundTag compound)
	{
		super.deserializeNBT(compound);

		if (compound.contains(TAG_FRUIT))
		{
			this.fruit = Fruit.deserialize(compound.getCompound(TAG_FRUIT));
		}

	}

	@Override
	public EntityAIWorkOrchardist generateAI()
	{
		return new EntityAIWorkOrchardist(this);
	}

	@Override
	public ResourceLocation getModel()
	{
		return ModModelTypes.LUMBERJACK_ID;
	}

	@Nullable
	public Fruit getFruit()
	{
		return fruit;
	}

	public void setFruit(@Nullable Fruit fruit)
	{
		this.fruit = fruit;
	}

}
