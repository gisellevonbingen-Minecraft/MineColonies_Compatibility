package steve_gall.minecolonies_compatibility.module.common;

import java.util.function.Supplier;

import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class OptionalModule<MODULE extends AbstractModule>
{
	private final String modId;
	private final Supplier<Supplier<MODULE>> initializer;
	private boolean isLoaded;

	private MODULE module;

	public OptionalModule(String modid, Supplier<Supplier<MODULE>> initializer)
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
			this.module = this.initializer.get().get();
			this.module.onLoad();

			var fml_bus = FMLJavaModLoadingContext.get().getModEventBus();
			fml_bus.addListener(this.module::onFMLCommonSetup);
			fml_bus.addListener(this.module::onFMLClientSetup);
		}

	}

	public boolean isLoaded()
	{
		return this.isLoaded;
	}

	public MODULE getModule()
	{
		return this.module;
	}

}
