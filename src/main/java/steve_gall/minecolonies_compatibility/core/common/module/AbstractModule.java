package steve_gall.minecolonies_compatibility.core.common.module;

import net.minecraftforge.fml.ModList;

public abstract class AbstractModule
{
	private boolean isLoaded;

	public abstract String getModId();

	protected boolean canLoad()
	{
		return ModList.get().isLoaded(this.getModId());
	}

	public void tryLoad()
	{
		if (this.canLoad())
		{
			this.isLoaded = true;
			this.onLoad();
		}

	}

	protected abstract void onLoad();

	public boolean isLoaded()
	{
		return this.isLoaded;
	}

}
