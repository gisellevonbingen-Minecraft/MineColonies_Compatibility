package steve_gall.minecolonies_compatibility.module.common;

import net.minecraft.world.item.crafting.RecipeManager;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import steve_gall.minecolonies_tweaks.api.common.network.MessageRegistrar;

public class AbstractModule
{
	protected void onLoad()
	{

	}

	protected void onInitBuildingModule()
	{

	}

	protected void onFMLCommonSetup(FMLCommonSetupEvent e)
	{

	}

	protected void onFMLClientSetup(FMLClientSetupEvent e)
	{

	}

	protected void onRegisterMenuScreens(RegisterMenuScreensEvent e)
	{

	}

	protected void onRegisterNetwork(MessageRegistrar channel)
	{

	}

	protected void onRecipeReloaded(RecipeManager recipeManager)
	{

	}

}
