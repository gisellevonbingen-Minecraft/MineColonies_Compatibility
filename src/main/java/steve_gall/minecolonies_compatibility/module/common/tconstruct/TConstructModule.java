package steve_gall.minecolonies_compatibility.module.common.tconstruct;

import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import slimeknights.tconstruct.tables.recipe.CraftingTableRepairKitRecipe;
import steve_gall.minecolonies_compatibility.api.common.repair.CustomizedRepair;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.module.common.AbstractModule;
import steve_gall.minecolonies_tweaks.api.common.requestsystem.DeliverableObjectRegistry;

public class TConstructModule extends AbstractModule
{
	public static final CraftingTableRepairKitRecipe REPAIR_RECIPE = new CraftingTableRepairKitRecipe(MineColoniesCompatibility.rl("dummy"));

	@Override
	protected void onLoad()
	{
		super.onLoad();

		DeliverableObjectRegistry.INSTANCE.register(BrokenItem.ID, BrokenItem::serialize, BrokenItem::deserialize);
		DeliverableObjectRegistry.INSTANCE.register(RepairKit.ID, RepairKit::serialize, RepairKit::deserialize);
	}

	@Override
	protected void onFMLCommonSetup(FMLCommonSetupEvent e)
	{
		super.onFMLCommonSetup(e);
		e.enqueueWork(() ->
		{
			CustomizedRepair.register(new TConstructRepair());
		});
	}

}
