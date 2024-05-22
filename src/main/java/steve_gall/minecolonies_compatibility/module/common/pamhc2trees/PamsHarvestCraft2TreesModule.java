package steve_gall.minecolonies_compatibility.module.common.pamhc2trees;

import java.util.HashMap;

import com.pam.pamhc2trees.Pamhc2trees;
import com.pam.pamhc2trees.init.BlockRegistration;
import com.pam.pamhc2trees.init.ItemRegistration;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import steve_gall.minecolonies_compatibility.api.common.plant.CustomizedFruit;

public class PamsHarvestCraft2TreesModule
{
	public static String FRUIT_PREFIX = "pam";

	public static void onLoad()
	{
		var fml_bus = FMLJavaModLoadingContext.get().getModEventBus();
		fml_bus.addListener(PamsHarvestCraft2TreesModule::onFMLCommonSetup);
	}

	private static void onFMLCommonSetup(FMLCommonSetupEvent e)
	{
		e.enqueueWork(() ->
		{
			var modid = Pamhc2trees.MOD_ID;
			var fruitOverrides = new HashMap<Block, Item>();
			fruitOverrides.put(BlockRegistration.pamapple.get(), Items.APPLE);
			fruitOverrides.put(BlockRegistration.pampaperbark.get(), Items.PAPER);
			fruitOverrides.put(BlockRegistration.pammaple.get(), ItemRegistration.maplesyrupitem.get());
			fruitOverrides.put(BlockRegistration.pamspiderweb.get(), Items.STRING);

			for (var id : ForgeRegistries.BLOCKS.getKeys())
			{
				if (id.getNamespace().equals(modid))
				{
					if (id.getPath().startsWith(FRUIT_PREFIX))
					{
						var block = ForgeRegistries.BLOCKS.getValue(id);
						var name = id.getPath().substring(FRUIT_PREFIX.length());
						var sapling = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(modid, name + "_sapling"));
						var fruit = fruitOverrides.getOrDefault(block, ForgeRegistries.ITEMS.getValue(new ResourceLocation(modid, name + "item")));
						CustomizedFruit.register(new PamFruit(block, sapling, fruit));
					}

				}

			}

		});
	}

}
