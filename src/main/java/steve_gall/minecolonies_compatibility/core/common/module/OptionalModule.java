package steve_gall.minecolonies_compatibility.core.common.module;

import java.util.function.Supplier;

import net.minecraftforge.fml.ModList;

public class OptionalModule
{
	private final String modId;
	private final Supplier<Runnable> initializer;
	private boolean isLoaded;

	public OptionalModule(String modid, Supplier<Runnable> initializer)
	{
		this.modId = modid;
		this.initializer = initializer;
		this.isLoaded = false;
	}

	public String getModId()
	{
		return this.modId;
	}

	protected boolean canLoad()
	{
		return ModList.get().isLoaded(this.getModId());
	}

	public void tryLoad()
	{
		if (this.canLoad())
		{
			this.isLoaded = true;
			this.initializer.get().run();
		}

	}

	public boolean isLoaded()
	{
		return this.isLoaded;
	}

}
