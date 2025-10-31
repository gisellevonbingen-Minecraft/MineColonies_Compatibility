package steve_gall.minecolonies_compatibility.module.common.fruitsdelight;

import dev.xkmc.fruitsdelight.content.block.PassableLeavesBlock;
import dev.xkmc.fruitsdelight.init.plants.FDBushes;
import dev.xkmc.fruitsdelight.init.plants.FDTrees;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import steve_gall.minecolonies_compatibility.api.common.plant.CustomizedFruit;
import steve_gall.minecolonies_compatibility.module.common.AbstractModule;

public class FruitsDelightModule extends AbstractModule
{
	@Override
	protected void onFMLCommonSetup(FMLCommonSetupEvent e)
	{
		super.onFMLCommonSetup(e);
		e.enqueueWork(() ->
		{
			for (var tree : FDTrees.values())
			{
				if (tree == FDTrees.DURIAN)
				{
					CustomizedFruit.register(new DurianBlockFruit(tree));
				}
				else if (tree.getLeaves() instanceof PassableLeavesBlock)
				{
					CustomizedFruit.register(new TreeLeavesFruit(tree));
				}

			}

			for (var bush : FDBushes.values())
			{
				CustomizedFruit.register(new FruitBushFruit(bush));
			}

		});
	}

}
