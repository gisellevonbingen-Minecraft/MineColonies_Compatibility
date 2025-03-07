package steve_gall.minecolonies_compatibility.module.common.tconstruct;

import java.util.Collection;

import slimeknights.tconstruct.library.materials.definition.MaterialVariantId;

public class MaterialHelper
{
	public static boolean anyMatchesVariantId(Collection<MaterialVariantId> materials1, Collection<MaterialVariantId> materials2)
	{
		for (var m1 : materials1)
		{
			if (anyMatchesVariantId(materials2, m1))
			{
				return true;
			}

		}

		return false;
	}

	public static boolean anyMatchesVariantId(Collection<MaterialVariantId> materials, MaterialVariantId material)
	{
		for (var m : materials)
		{
			if (m.matchesVariant(material))
			{
				return true;
			}

		}

		return false;
	}

	private MaterialHelper()
	{

	}

}
