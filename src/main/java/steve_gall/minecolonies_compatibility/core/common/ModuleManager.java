package steve_gall.minecolonies_compatibility.core.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import steve_gall.minecolonies_compatibility.core.common.ie.IEModule;

public class ModuleManager
{
	public static final List<AbstractModule> MODULES;
	private static boolean INITIALIZED;
	private static final List<AbstractModule> _LOADED_MODULES;
	public static final List<AbstractModule> LOADED_MODULES;

	public static final IEModule IE;

	static
	{
		var modules = new ArrayList<AbstractModule>();
		modules.add(IE = new IEModule());

		MODULES = Collections.unmodifiableList(modules);
		_LOADED_MODULES = new ArrayList<>();
		LOADED_MODULES = Collections.unmodifiableList(_LOADED_MODULES);
	}

	public static boolean isInitialized()
	{
		return INITIALIZED;
	}

	public static void initialize()
	{
		if (INITIALIZED)
		{
			throw new IllegalCallerException();
		}

		INITIALIZED = true;
		_LOADED_MODULES.clear();

		for (var module : MODULES)
		{
			module.tryLoad();

			if (module.isLoaded())
			{
				_LOADED_MODULES.add(module);
			}

		}

	}

}
