package steve_gall.minecolonies_compatibility.core.common;

import net.minecraftforge.fml.ModList;

public abstract class AbstractModule
{
	private boolean isLoaded;

	public abstract String getModId();

	public void tryLoad()
	{
		if (ModList.get().isLoaded(this.getModId()))
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
