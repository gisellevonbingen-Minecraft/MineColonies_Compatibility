package steve_gall.minecolonies_compatibility.module.common.refinedstorage;

import com.minecolonies.api.colony.IColony;
import com.mojang.authlib.GameProfile;
import com.refinedmods.refinedstorage.api.network.INetwork;
import com.refinedmods.refinedstorage.api.network.security.Permission;
import com.refinedmods.refinedstorage.apiimpl.API;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import steve_gall.minecolonies_compatibility.api.common.building.module.NetworkStorageViewRegistry;
import steve_gall.minecolonies_compatibility.module.client.refinedstorage.CitizenGridScreen;
import steve_gall.minecolonies_compatibility.module.common.AbstractModule;
import steve_gall.minecolonies_compatibility.module.common.refinedstorage.init.ModuleBlockEntities;
import steve_gall.minecolonies_compatibility.module.common.refinedstorage.init.ModuleBlocks;
import steve_gall.minecolonies_compatibility.module.common.refinedstorage.init.ModuleItems;
import steve_gall.minecolonies_compatibility.module.common.refinedstorage.init.ModuleMenuTypes;

public class RefinedStorageModule extends AbstractModule
{
	@Override
	protected void onLoad()
	{
		super.onLoad();

		var fml_bus = FMLJavaModLoadingContext.get().getModEventBus();
		ModuleBlocks.REGISTER.register(fml_bus);
		ModuleItems.REGISTER.register(fml_bus);
		ModuleBlockEntities.REGISTER.register(fml_bus);
		ModuleMenuTypes.REGISTER.register(fml_bus);
	}

	@Override
	protected void onFMLCommonSetup(FMLCommonSetupEvent e)
	{
		super.onFMLCommonSetup(e);
		e.enqueueWork(() ->
		{
			NetworkStorageViewRegistry.register((be, direction) -> be instanceof CitizenGridBlockEntity grid ? grid.getNode().getView() : null);
			API.instance().getNetworkNodeRegistry().add(CitizenGridNetworkNode.ID, CitizenGridNetworkNode::new);
		});
	}

	@Override
	protected void onFMLClientSetup(FMLClientSetupEvent e)
	{
		super.onFMLClientSetup(e);

		MenuScreens.register(ModuleMenuTypes.CITIZEN_GRID.get(), CitizenGridScreen::new);
	}

	public static boolean hasPermission(IColony colony, INetwork network, Permission permission)
	{
		var permissions = colony.getPermissions();
		var ownerProfile = new GameProfile(permissions.getOwner(), permissions.getOwnerName());
		var owner = FakePlayerFactory.get((ServerLevel) colony.getWorld(), ownerProfile);
		return network.getSecurityManager().hasPermission(permission, owner);
	}

}
