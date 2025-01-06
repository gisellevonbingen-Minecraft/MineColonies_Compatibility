package steve_gall.minecolonies_compatibility.module.common.butchersdelight;

import java.util.function.Consumer;

import com.minecolonies.api.entity.citizen.AbstractEntityCitizen;

import net.mcreator.butchersdelight.init.ButchersdelightModBlocks;
import net.mcreator.butchersdelight.init.ButchersdelightModItems;
import net.mcreator.butchersdelightfoods.init.ButchersdelightfoodsModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import steve_gall.minecolonies_compatibility.api.common.butcher.CustomizedButcherable;
import steve_gall.minecolonies_compatibility.core.common.colony.ColonyHelper;
import steve_gall.minecolonies_compatibility.module.common.AbstractModule;
import steve_gall.minecolonies_compatibility.module.common.ModuleManager;
import umpaz.nethersdelight.common.registry.NDItems;
import vectorwing.farmersdelight.common.registry.ModItems;

public class ButchersDelightModule extends AbstractModule
{
	@Override
	protected void onFMLCommonSetup(FMLCommonSetupEvent e)
	{
		super.onFMLCommonSetup(e);
		e.enqueueWork(() ->
		{
			this.registerCarcasses();
			this.registerSkins();
		});

	}

	private void registerCarcasses()
	{
		CustomizedButcherable.register(new CarcassHookButcherable(this.builder(b ->
		{
			b.item(ButchersdelightModItems.DEAD_COW);
			b.block(ButchersdelightModBlocks.HOOKEDCOW);
			b.block(ButchersdelightModBlocks.HOOKEDCOWSKINNED);
			b.block(ButchersdelightModBlocks.HOOKEDCOWPROCESS_1);
			b.block(ButchersdelightModBlocks.HOOKEDCOWPROCESS_2);
			b.block(ButchersdelightModBlocks.HOOKEDCOWPROCESS_3);

			b.output(Items.BEEF);
			b.output(ButchersdelightModItems.SKULLCOW);
			b.output(ButchersdelightModItems.COW_HIDE);

			if (ModuleManager.BUTCHERSDELIGHTFOODS.isLoaded())
			{
				b.output(ButchersdelightfoodsModItems.LEGCOW);
				b.output(ButchersdelightfoodsModItems.BEEFRIBS);
				b.output(ButchersdelightfoodsModItems.BEEFTENDERLOIN);
			}
			else
			{
				b.output(Items.BONE);
			}
		})));

		CustomizedButcherable.register(new CarcassHookButcherable(this.builder(b ->
		{
			b.item(ButchersdelightModItems.DEADGOAT);
			b.block(ButchersdelightModBlocks.HOOKED_GOAT);
			b.block(ButchersdelightModBlocks.HOOKEDGOATSKINNED);
			b.block(ButchersdelightModBlocks.HOOKEDGOATP_1);
			b.block(ButchersdelightModBlocks.HOOKEDGOATP_2);
			b.block(ButchersdelightModBlocks.HOOKEDGOATP_3);

			b.output(Items.MUTTON);
			b.output(ButchersdelightModItems.GOAT_FUR);

			if (ModuleManager.BUTCHERSDELIGHTFOODS.isLoaded())
			{
				b.output(ButchersdelightfoodsModItems.GOAT_SHANK);
				b.output(ButchersdelightfoodsModItems.GOATRACK);
				b.output(ButchersdelightfoodsModItems.GOAT_LOIN);
			}
			else
			{
				b.output(Items.BONE);
			}
		})));

		CustomizedButcherable.register(new CarcassHookButcherable(this.builder(b ->
		{
			b.item(ButchersdelightModItems.DEADHOGLIN);
			b.block(ButchersdelightModBlocks.HOKEDHOGLIN);
			b.block(ButchersdelightModBlocks.HOOKEDHOGLIN_1);
			b.block(ButchersdelightModBlocks.HOOKEDHOGLINP_2);
			b.block(ButchersdelightModBlocks.HOOKEDHOGLINP_3);

			b.output(Items.PORKCHOP);

			if (ModuleManager.FARMERSDELIGHT.isLoaded())
			{
				b.output(ModItems.HAM);
			}

			if (ModuleManager.NETHERS_DELIGHT.isLoaded())
			{
				b.output(NDItems.HOGLIN_LOIN);
				b.output(NDItems.HOGLIN_HIDE);
			}
			else
			{
				b.output(ButchersdelightModItems.HOGLINSKIN);
			}

			if (ModuleManager.BUTCHERSDELIGHTFOODS.isLoaded())
			{
				b.output(ButchersdelightfoodsModItems.HAM);
				b.output(ButchersdelightfoodsModItems.PORKRIBS);
				b.output(ButchersdelightfoodsModItems.PORKLOIN);
			}
			else
			{
				b.output(Items.BONE);
			}
		})));
		CustomizedButcherable.register(new CarcassHookButcherable(this.builder(b ->
		{
			b.item(ButchersdelightModItems.DEADLLAMA);
			b.block(ButchersdelightModBlocks.HOOKEDLLAMA);
			b.block(ButchersdelightModBlocks.HOOKEDLLAMASKINNED);
			b.block(ButchersdelightModBlocks.HOOKEDLLAMAP_1);
			b.block(ButchersdelightModBlocks.HOOKEDLLAMAP_2);
			b.block(ButchersdelightModBlocks.HOOKEDLLAMAP_3);

			b.output(Items.LEATHER);
			b.output(Items.MUTTON);
			b.output(ButchersdelightModItems.GOAT_FUR);

			if (ModuleManager.BUTCHERSDELIGHTFOODS.isLoaded())
			{
				b.output(ButchersdelightfoodsModItems.LLAMA_LEG);
				b.output(ButchersdelightfoodsModItems.LLAMMA_RIBS);
				b.output(ButchersdelightfoodsModItems.LLAMA_LOIN);
			}
			else
			{
				b.output(Items.BONE);
			}
		})));

		CustomizedButcherable.register(new CarcassHookButcherable(this.builder(b ->
		{
			b.item(ButchersdelightModItems.DEADPIG);
			b.block(ButchersdelightModBlocks.HOOKEDPIG);
			b.block(ButchersdelightModBlocks.HOOKEDPIGP_1);
			b.block(ButchersdelightModBlocks.HOOKEDPIGP_2);
			b.block(ButchersdelightModBlocks.HOOKEDPIGP_3);

			b.output(Items.PORKCHOP);

			if (ModuleManager.FARMERSDELIGHT.isLoaded())
			{
				b.output(ModItems.HAM);
			}

			if (ModuleManager.BUTCHERSDELIGHTFOODS.isLoaded())
			{
				b.output(ButchersdelightfoodsModItems.HAM);
				b.output(ButchersdelightfoodsModItems.PORKRIBS);
				b.output(ButchersdelightfoodsModItems.PORKLOIN);
			}
			else
			{
				b.output(Items.BONE);
			}
		})));

		CustomizedButcherable.register(new CarcassHookButcherable(this.builder(b ->
		{
			b.item(ButchersdelightModItems.DEADSHEEP);
			b.block(ButchersdelightModBlocks.HOOKEDSHEEP);
			b.block(ButchersdelightModBlocks.HOOKEDSHEEPSKINNED);
			b.block(ButchersdelightModBlocks.HOOKEDSHEEPP_1);
			b.block(ButchersdelightModBlocks.HOOKEDSHEEPP_2);
			b.block(ButchersdelightModBlocks.HOOKEDSHEEPP_3);

			b.output(Items.MUTTON);
			b.output(ButchersdelightModItems.SHEEPHIDE);

			if (ModuleManager.BUTCHERSDELIGHTFOODS.isLoaded())
			{
				b.output(ButchersdelightfoodsModItems.SHEEPSHANK);
				b.output(ButchersdelightfoodsModItems.SHEEPRACK);
				b.output(ButchersdelightfoodsModItems.SHEEPLOIN);
			}
			else
			{
				b.output(Items.BONE);
			}
		})));

		CustomizedButcherable.register(new CarcassAirButcherable(this.builder(b ->
		{
			b.item(ButchersdelightModItems.DEADCHIKEN);
			b.block(ButchersdelightModBlocks.DEADCHICKENBLOCK);
			b.block(ButchersdelightModBlocks.DEADCHICKENBLOCKSKINNED);

			b.output(Items.CHICKEN);
			b.output(Items.FEATHER);
		})));

		CustomizedButcherable.register(new CarcassAirButcherable(this.builder(b ->
		{
			b.item(ButchersdelightModItems.DEADRABBITBROWN);
			b.block(ButchersdelightModBlocks.DEADRABBITBLOCK);
			b.block(ButchersdelightModBlocks.DEADRABIITSKINNEDBLOCK);

			b.output(Items.RABBIT);
			b.output(Items.RABBIT_HIDE);
			b.output(Items.RABBIT_FOOT);
		})));

		CustomizedButcherable.register(new CarcassAirButcherable(this.builder(b ->
		{
			b.item(ButchersdelightModItems.DEADSTRIDER);
			b.block(ButchersdelightModBlocks.DEADSTRIDERBLOCK);
			b.block(ButchersdelightModBlocks.DEADSTRIDERBLOCKSKINNED);

			b.output(Items.STRING);
			b.output(Items.BASALT);

			if (ModuleManager.NETHERS_DELIGHT.isLoaded())
			{
				b.output(NDItems.STRIDER_SLICE);
			}
		})));
	}

	private void registerSkins()
	{
		CustomizedButcherable.register(new SkinButcherable(this.builder(b ->
		{
			b.item(ButchersdelightModItems.COW_HIDE);
			b.block(ButchersdelightModBlocks.RACKCOW);
			b.output(Items.LEATHER);
		})));

		CustomizedButcherable.register(new SkinButcherable(this.builder(b ->
		{
			b.item(ButchersdelightModItems.GOAT_FUR);
			b.block(ButchersdelightModBlocks.RACKGOAT);
			b.output(Items.LEATHER);
			b.output(Items.STRING);
		})));

		CustomizedButcherable.register(new SkinButcherable(this.builder(b ->
		{
			b.item(ButchersdelightModItems.SHEEPHIDE);
			b.block(ButchersdelightModBlocks.RACKSHEEP);
			b.output(Items.LEATHER);
			b.output(Items.WHITE_WOOL);
		})));

		CustomizedButcherable.register(new SkinButcherable(this.builder(b ->
		{
			b.item(ButchersdelightModItems.HOGLINSKIN);
			b.block(ButchersdelightModBlocks.RACKHOGLIN);
			b.output(Items.LEATHER);
		})));
	}

	private AbstractButcherable.Builder builder(Consumer<AbstractButcherable.Builder> consumer)
	{
		var builder = new CarcassHookButcherable.Builder();
		consumer.accept(builder);
		return builder;
	}

	public static void rightClick(ServerLevel level, BlockPos position, AbstractEntityCitizen worker, ItemStack stack)
	{
		var hand = InteractionHand.MAIN_HAND;
		var player = ColonyHelper.getFakeOwner(worker.getCitizenData().getColony());
		var hitVec = new BlockHitResult(Vec3.atCenterOf(position), Direction.UP, position, true);

		player.setItemInHand(hand, stack);
		player.setXRot(worker.getXRot());
		player.setYRot(worker.getYRot());
		player.setYHeadRot(worker.getYHeadRot());
		ForgeHooks.onRightClickBlock(player, hand, position, hitVec);
	}

}
