package steve_gall.minecolonies_compatibility.module.common.jade;

import com.minecolonies.core.blocks.MinecoloniesCropBlock;
import com.minecolonies.core.blocks.huts.BlockPostBox;
import com.minecolonies.core.blocks.huts.BlockStash;

import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin
public class JadePlugin implements IWailaPlugin
{
	@Override
	public void registerClient(IWailaClientRegistration registration)
	{
		registration.addConfig(CropAgeComponentProvider.UID, true);
		registration.registerBlockComponent(CropAgeComponentProvider.INSTANCE, MinecoloniesCropBlock.class);

		registration.addConfig(PostBoxRequestedComponentProvider.UID, true);
		registration.registerBlockComponent(PostBoxRequestedComponentProvider.INSTANCE, BlockPostBox.class);

		registration.addConfig(StashRequestedComponentProvider.UID, true);
		registration.registerBlockComponent(StashRequestedComponentProvider.INSTANCE, BlockStash.class);
	}

	@Override
	public void register(IWailaCommonRegistration registration)
	{

	}

}
