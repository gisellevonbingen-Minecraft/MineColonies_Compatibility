package steve_gall.minecolonies_compatibility.module.common.silentgear;

import com.minecolonies.api.util.constant.BuildingConstants;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.world.item.ItemStack;
import net.silentchaos512.gear.api.property.HarvestTier;
import net.silentchaos512.gear.gear.material.MaterialInstance;
import net.silentchaos512.gear.setup.gear.GearProperties;
import net.silentchaos512.gear.setup.gear.PartTypes;
import net.silentchaos512.gear.util.GearData;

public class TierHelper
{
	private static final Object2IntMap<HarvestTier> LEVELS = new Object2IntOpenHashMap<>();

	public static int getGearTier(ItemStack stack)
	{
		if (stack.isEmpty())
		{
			return 0;
		}

		var mainPart = GearData.getPartOfType(stack, PartTypes.MAIN.get());

		if (mainPart == null)
		{
			return 0;
		}

		return getMaterialTier(mainPart.getPrimaryMaterial());
	}

	public static int getMaterialTier(ItemStack stack)
	{
		if (stack.isEmpty())
		{
			return 0;
		}

		return getMaterialTier(MaterialInstance.from(stack));
	}

	public static int getMaterialTier(MaterialInstance material)
	{
		if (material == null)
		{
			return 0;
		}

		return getTier(material.getProperty(PartTypes.MAIN.get(), GearProperties.HARVEST_TIER.get()));
	}

	public static int getTier(HarvestTier harvestTier)
	{
		return LEVELS.computeIfAbsent(harvestTier, key ->
		{
			try
			{
				return Math.min(((HarvestTier) key).levelHint().map(Integer::parseInt).orElse(0), BuildingConstants.CONST_DEFAULT_MAX_BUILDING_LEVEL);
			}
			catch (Exception e)
			{
				return 0;
			}

		});
	}

	private TierHelper()
	{

	}

}
