package steve_gall.minecolonies_compatibility.core.common;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import steve_gall.minecolonies_compatibility.mixin.common.minecraft.StemBlockAccessor;

public class CompatibilityManager
{
	private final Map<Block, Block> grownToStem = new HashMap<>();

	public CompatibilityManager()
	{

	}

	public void initialize()
	{
		this.discoverStem();
	}

	private void discoverStem()
	{
		this.grownToStem.clear();

		for (var block : BuiltInRegistries.BLOCK)
		{
			if (block instanceof StemBlockAccessor stemBlock)
			{
				this.grownToStem.put(BuiltInRegistries.BLOCK.get(stemBlock.getFruit()), block);
			}

		}

	}

	public Block getStem(Block grown)
	{
		return this.grownToStem.get(grown);
	}

}
