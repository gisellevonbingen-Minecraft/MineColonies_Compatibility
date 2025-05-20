package steve_gall.minecolonies_compatibility.core.common.colony;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.minecolonies.api.colony.IColony;
import com.mojang.authlib.GameProfile;

import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;

public class ColonyHelper
{
	@Nullable
	public static FakePlayer getFakeOwner(IColony colony)
	{
		if (colony.getWorld() instanceof ServerLevel level)
		{
			return getFakeOwner(colony, level);
		}

		return null;
	}

	@NotNull
	public static FakePlayer getFakeOwner(IColony colony, ServerLevel level)
	{
		var permissions = colony.getPermissions();
		var ownerProfile = new GameProfile(permissions.getOwner(), permissions.getOwnerName());
		return FakePlayerFactory.get(level, ownerProfile);
	}

	private ColonyHelper()
	{

	}

}
