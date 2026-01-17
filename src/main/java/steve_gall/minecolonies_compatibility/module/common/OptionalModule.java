package steve_gall.minecolonies_compatibility.module.common;

import java.util.function.Supplier;

import com.minecolonies.api.IMinecoloniesAPI;

import net.neoforged.fml.ModList;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.neoforge.registries.RegisterEvent;
import steve_gall.minecolonies_tweaks.api.common.network.MessageRegistrar;

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

	public boolean isInstalled()
	{
		return ModList.get().isLoaded(this.getModId());
	}

	protected boolean canLoad()
	{
		return this.isInstalled();
	}

	public void tryLoad()
	{
		if (this.canLoad())
		{
			this.isLoaded = true;
			this.module = this.initializer.get().get();
			this.module.onLoad();

			var fml_bus = ModLoadingContext.get().getActiveContainer().getEventBus();
			fml_bus.addListener(this::onRegister);
			fml_bus.addListener(this.module::onFMLCommonSetup);
			fml_bus.addListener(this.module::onFMLClientSetup);
			fml_bus.addListener(this.module::onRegisterMenuScreens);
		}

	}

	private void onRegister(RegisterEvent e)
	{
		if (e.getRegistryKey() == IMinecoloniesAPI.getInstance().getBuildingRegistry().key())
		{
			this.module.onInitBuildingModule();
		}

	}

	public void onRegisterNetwork(MessageRegistrar channel)
	{
		if (this.isLoaded())
		{
			this.module.onRegisterNetwork(channel);
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
