package steve_gall.minecolonies_compatibility.core.common.init;

import java.util.function.Consumer;

import com.minecolonies.api.colony.jobs.registry.JobEntry;
import com.minecolonies.core.colony.jobs.views.DefaultJobView;

import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.core.common.colony.jobs.JobGunner;
import steve_gall.minecolonies_tweaks.api.registries.DeferredRegisterHelper;

public class ModJobs
{
	public static final DeferredRegister<JobEntry> REGISTER = DeferredRegisterHelper.jobs(MineColoniesCompatibility.MOD_ID);

	public static final RegistryObject<JobEntry> GUNNER = register("gunner", builder ->
	{
		builder.setJobProducer(JobGunner::new);
		builder.setJobViewProducer(() -> DefaultJobView::new);
	});

	public static RegistryObject<JobEntry> register(String path, Consumer<JobEntry.Builder> consumer)
	{
		return DeferredRegisterHelper.registerJobEntry(REGISTER, path, consumer);
	}

	private ModJobs()
	{

	}

}
