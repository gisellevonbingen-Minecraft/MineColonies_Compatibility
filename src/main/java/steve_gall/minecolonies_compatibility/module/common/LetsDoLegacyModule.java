package steve_gall.minecolonies_compatibility.module.common;

import java.util.function.Supplier;

import net.neoforged.fml.ModList;

public class LetsDoLegacyModule<MODULE extends AbstractModule> extends OptionalModule<MODULE>
{
	public LetsDoLegacyModule(String modid, Supplier<Supplier<MODULE>> initializer)
	{
		super(modid, initializer);
	}

	@Override
	protected boolean canLoad()
	{
		return super.canLoad() && ModList.get().getModContainerById(this.getModId()).filter(c ->
		{
			var majorVersion = c.getModInfo().getVersion().getMajorVersion();
			return majorVersion < 2;
		}).isPresent();
	}

}
