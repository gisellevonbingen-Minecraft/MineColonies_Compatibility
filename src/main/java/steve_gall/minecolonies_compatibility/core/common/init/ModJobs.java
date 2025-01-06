package steve_gall.minecolonies_compatibility.core.common.init;

import com.minecolonies.api.colony.jobs.registry.JobEntry;
import com.minecolonies.core.colony.jobs.views.CrafterJobView;
import com.minecolonies.core.colony.jobs.views.DefaultJobView;

import net.minecraftforge.registries.RegistryObject;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.core.common.job.JobButcher;
import steve_gall.minecolonies_compatibility.core.common.job.JobFluidManager;
import steve_gall.minecolonies_compatibility.core.common.job.JobGunner;
import steve_gall.minecolonies_compatibility.core.common.job.JobOrchardist;
import steve_gall.minecolonies_tweaks.api.registries.JobRegister;

public class ModJobs
{
	public static final JobRegister REGISTER = new JobRegister(MineColoniesCompatibility.MOD_ID);

	public static final RegistryObject<JobEntry> GUNNER = REGISTER.register("gunner", builder ->
	{
		builder.setJobProducer(JobGunner::new);
		builder.setJobViewProducer(() -> DefaultJobView::new);
	});
	public static final RegistryObject<JobEntry> ORCHARDIST = REGISTER.register("orchardist", builder ->
	{
		builder.setJobProducer(JobOrchardist::new);
		builder.setJobViewProducer(() -> DefaultJobView::new);
	});
	public static final RegistryObject<JobEntry> FLUID_MANAGER = REGISTER.register("fluid_manager", builder ->
	{
		builder.setJobProducer(JobFluidManager::new);
		builder.setJobViewProducer(() -> CrafterJobView::new);
	});
	public static final RegistryObject<JobEntry> BUTCHER = REGISTER.register("butcher", builder ->
	{
		builder.setJobProducer(JobButcher::new);
		builder.setJobViewProducer(() -> DefaultJobView::new);
	});

	private ModJobs()
	{

	}

}
