package steve_gall.minecolonies_compatibility.core.common.init;

import com.minecolonies.api.util.constant.Constants;

import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;

public class ModTags
{
	@SuppressWarnings("unchecked")
	public static class Items
	{
		public static final TagKey<Item>[] SMITHING_REQUIRED_LEVEL;
		public static final TagKey<Item> SMITHING_TEMPLATES;

		static
		{
			SMITHING_REQUIRED_LEVEL = new TagKey[Constants.MAX_BUILDING_LEVEL + 1];

			for (var i = 0; i < SMITHING_REQUIRED_LEVEL.length; i++)
			{
				SMITHING_REQUIRED_LEVEL[i] = ItemTags.create(MineColoniesCompatibility.rl("smithing_required_level_" + i));
			}

			SMITHING_TEMPLATES = ItemTags.create(MineColoniesCompatibility.rl("smithing_templates"));
		}

	}

	private ModTags()
	{

	}

}
