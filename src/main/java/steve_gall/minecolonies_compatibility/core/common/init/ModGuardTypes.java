package steve_gall.minecolonies_compatibility.core.common.init;

import java.util.function.Consumer;

import com.minecolonies.api.colony.guardtype.GuardType;
import com.minecolonies.api.colony.jobs.registry.JobEntry;
import com.minecolonies.api.entity.citizen.Skill;

import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.core.common.job.JobGunner;
import steve_gall.minecolonies_tweaks.api.registries.DeferredRegisterHelper;

public class ModGuardTypes
{
	public static final DeferredRegister<GuardType> REGISTER = DeferredRegisterHelper.guardTypes(MineColoniesCompatibility.MOD_ID);

	public static final DeferredHolder<GuardType, GuardType> GUNNER = register(ModJobs.GUNNER, builder ->
	{
		builder.setPrimarySkill(Skill.Agility).setSecondarySkill(Skill.Adaptability);
		builder.setClazz(JobGunner.class);
	});

	public static DeferredHolder<GuardType, GuardType> register(DeferredHolder<JobEntry, JobEntry> job, Consumer<GuardType.Builder> consumer)
	{
		return DeferredRegisterHelper.registerGuardType(REGISTER, job, consumer);
	}

	private ModGuardTypes()
	{

	}

}
