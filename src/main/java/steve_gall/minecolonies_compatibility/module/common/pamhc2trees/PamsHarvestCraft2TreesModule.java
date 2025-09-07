package steve_gall.minecolonies_compatibility.module.common.pamhc2trees;

import java.util.HashMap;

import com.pam.pamhc2trees.Pamhc2trees;
import com.pam.pamhc2trees.init.BlockRegistration;
import com.pam.pamhc2trees.init.ItemRegistration;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import steve_gall.minecolonies_compatibility.api.common.plant.CustomizedFruit;
import steve_gall.minecolonies_compatibility.module.common.AbstractModule;

public class PamsHarvestCraft2TreesModule extends AbstractModule
{
	public static String FRUIT_PREFIX = "pam";

	@Override
	protected void onFMLCommonSetup(FMLCommonSetupEvent e)
	{
		super.onFMLCommonSetup(e);
		e.enqueueWork(() ->
		{
			var modid = Pamhc2trees.MOD_ID;
			var fruitOverrides = new HashMap<Block, Item>();
			fruitOverrides.put(BlockRegistration.pamapple.get(), Items.APPLE);
			fruitOverrides.put(BlockRegistration.pampaperbark.get(), Items.PAPER);
			fruitOverrides.put(BlockRegistration.pammaple.get(), ItemRegistration.maplesyrupitem.get());
			fruitOverrides.put(BlockRegistration.pamspiderweb.get(), Items.STRING);

			for (var id : BuiltInRegistries.BLOCK.keySet())
			{
				if (id.getNamespace().equals(modid))
				{
					if (id.getPath().startsWith(FRUIT_PREFIX))
					{
						var block = BuiltInRegistries.BLOCK.get(id);
						var name = id.getPath().substring(FRUIT_PREFIX.length());
						var sapling = BuiltInRegistries.BLOCK.get(ResourceLocation.fromNamespaceAndPath(modid, name + "_sapling"));
						var fruit = fruitOverrides.getOrDefault(block, BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath(modid, name + "item")));
						CustomizedFruit.register(new PamFruit(block, sapling, fruit));
					}

				}

			}

		});
	}

}
