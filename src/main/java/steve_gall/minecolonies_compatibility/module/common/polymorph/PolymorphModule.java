package steve_gall.minecolonies_compatibility.module.common.polymorph;

import java.util.List;
import java.util.TreeSet;

import com.illusivesoulworks.polymorph.api.PolymorphApi;
import com.illusivesoulworks.polymorph.api.client.base.IPolymorphClient.IRecipesWidgetFactory;
import com.illusivesoulworks.polymorph.api.client.base.IRecipesWidget;
import com.illusivesoulworks.polymorph.api.client.widget.AbstractRecipesWidget;
import com.illusivesoulworks.polymorph.api.common.base.IPolymorphCommon.IItemStack2RecipeData;
import com.illusivesoulworks.polymorph.api.common.base.IRecipePair;
import com.illusivesoulworks.polymorph.api.common.capability.IStackRecipeData;
import com.illusivesoulworks.polymorph.common.impl.RecipePair;
import com.minecolonies.api.blocks.ModBlocks;
import com.minecolonies.api.inventory.container.ContainerCrafting;
import com.minecolonies.api.util.Tuple;
import com.minecolonies.core.client.gui.containers.WindowCrafting;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraftforge.fml.loading.FMLEnvironment;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.core.common.item.ItemStackHelper;
import steve_gall.minecolonies_compatibility.module.common.AbstractModule;

public class PolymorphModule extends AbstractModule
{
	@Override
	protected void onLoad()
	{
		super.onLoad();

		PolymorphApi.common().registerContainer2ItemStack(menu ->
		{
			if (menu instanceof ContainerCrafting crafting)
			{
				var stack = new ItemStack(ModBlocks.blockHutTownHall);
				var tag = stack.getOrCreateTagElement(MineColoniesCompatibility.MOD_ID);
				tag.putBoolean("polymorph", true);
				tag.putString("player", crafting.getPlayer().getStringUUID());
				tag.put("building", NbtUtils.writeBlockPos(crafting.getPos()));
				return stack;
			}
			return null;

		});
		PolymorphApi.common().registerItemStack2RecipeData(new IItemStack2RecipeData()
		{
			@Override
			public IStackRecipeData createRecipeData(ItemStack stack)
			{
				var tag = stack.getTagElement(MineColoniesCompatibility.MOD_ID);

				if (tag != null && tag.getBoolean("polymorph"))
				{
					return new CraftingWindowRecipeData(stack);
				}

				return null;
			}
		});

		if (FMLEnvironment.dist.isClient())
		{
			PolymorphApi.client().registerWidget(new IRecipesWidgetFactory()
			{
				@Override
				public IRecipesWidget createWidget(AbstractContainerScreen<?> screen)
				{
					if (screen instanceof WindowCrafting window)
					{
						return new AbstractRecipesWidget(screen)
						{
							@Override
							public Slot getOutputSlot()
							{
								return this.containerScreen.getMenu().getSlot(0);
							}

							@Override
							public void selectRecipe(ResourceLocation id)
							{
								PolymorphApi.common().getPacketDistributor().sendStackRecipeSelectionC2S(id);
							}

						};
					}

					return null;
				}
			});

		}

	}

	public static void sendRecipesList(ServerPlayer player, List<Tuple<CraftingRecipe, ItemStack>> tuples, ItemStack current)
	{
		var pairs = new TreeSet<IRecipePair>();
		ResourceLocation selected = null;

		for (var tuple : tuples)
		{
			var id = tuple.getA().getId();
			var stack = tuple.getB();
			pairs.add(new RecipePair(id, stack));

			if (ItemStackHelper.equals(current, stack))
			{
				selected = id;
			}

		}

		PolymorphApi.common().getPacketDistributor().sendRecipesListS2C(player, pairs, selected);
	}

	public static void sendHighlightRecipe(ServerPlayer player, ResourceLocation recipeId)
	{
		PolymorphApi.common().getPacketDistributor().sendHighlightRecipeS2C(player, recipeId);
	}

}
