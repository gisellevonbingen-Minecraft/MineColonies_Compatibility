package steve_gall.minecolonies_compatibility.module.common;

import java.util.function.Supplier;

import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import steve_gall.minecolonies_tweaks.api.common.building.module.ModuleRegisterEvent;

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

			var fml_bus = FMLJavaModLoadingContext.get().getModEventBus();
			fml_bus.addListener(this::onModuleRegister);
			fml_bus.addListener(this.module::onFMLCommonSetup);
			fml_bus.addListener(this.module::onFMLClientSetup);
		}

	}

	private void onModuleRegister(ModuleRegisterEvent e)
	{
		this.module.onInitBuildingModule();
	}

	public void onRecipeReloaded(RecipeManager recipeManager)
	{
		this.module.onRecipeReloaded(recipeManager);
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
