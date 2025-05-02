package steve_gall.minecolonies_compatibility.module.common.collectorsreap;

import net.brdle.collectorsreap.common.block.CRBlocks;
import net.brdle.collectorsreap.common.block.LimeBushBlock;
import net.brdle.collectorsreap.common.block.PomegranateBushBlock;
import net.brdle.collectorsreap.common.item.CRItems;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import steve_gall.minecolonies_compatibility.api.common.plant.CustomizedFruit;
import steve_gall.minecolonies_compatibility.module.common.AbstractModule;

public class CollectorsReapModule extends AbstractModule
{
	@Override
	protected void onLoad()
	{
		super.onLoad();
	}

	@Override
	protected void onFMLCommonSetup(FMLCommonSetupEvent e)
	{
		super.onFMLCommonSetup(e);
		e.enqueueWork(() ->
		{
			CustomizedFruit.register(new FruitBushFruit<>((LimeBushBlock) CRBlocks.LIME_BUSH.get(), CRItems.LIME_SEEDS.get(), BlockStateProperties.AGE_2, LimeBushBlock.MAX_AGE));
			CustomizedFruit.register(new FruitBushFruit<>((PomegranateBushBlock) CRBlocks.POMEGRANATE_BUSH.get(), CRItems.POMEGRANATE_SEEDS.get(), BlockStateProperties.AGE_2, PomegranateBushBlock.MAX_AGE));
		});
	}

}
