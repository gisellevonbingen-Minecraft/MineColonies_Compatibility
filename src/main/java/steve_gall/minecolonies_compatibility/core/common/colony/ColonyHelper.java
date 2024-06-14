package steve_gall.minecolonies_compatibility.core.common.colony;

import com.minecolonies.api.colony.IColony;
import com.mojang.authlib.GameProfile;

import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;

public class ColonyHelper
{
	public static FakePlayer getFakeOwner(IColony colony)
	{
		var permissions = colony.getPermissions();
		var ownerProfile = new GameProfile(permissions.getOwner(), permissions.getOwnerName());
		return FakePlayerFactory.get((ServerLevel) colony.getWorld(), ownerProfile);
	}

	private ColonyHelper()
	{

	}

}
