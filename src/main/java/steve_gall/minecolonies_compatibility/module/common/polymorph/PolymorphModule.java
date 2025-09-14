package steve_gall.minecolonies_compatibility.module.common.polymorph;

import java.util.List;
import java.util.TreeSet;

import com.illusivesoulworks.polymorph.api.PolymorphApi;
import com.illusivesoulworks.polymorph.api.client.base.IPolymorphClient.IRecipesWidgetFactory;
import com.illusivesoulworks.polymorph.api.client.base.IRecipesWidget;
import com.illusivesoulworks.polymorph.api.client.widget.AbstractRecipesWidget;
import com.illusivesoulworks.polymorph.api.common.base.IRecipePair;
import com.illusivesoulworks.polymorph.common.impl.RecipePair;
import com.minecolonies.api.util.Tuple;
import com.minecolonies.core.client.gui.containers.WindowCrafting;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraftforge.fml.loading.FMLEnvironment;
import steve_gall.minecolonies_compatibility.api.common.inventory.MenuRecipeValidatorRecipe;
import steve_gall.minecolonies_compatibility.core.client.gui.TeachRecipeScreen;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.core.common.inventory.TeachRecipeMenu;
import steve_gall.minecolonies_compatibility.core.common.item.ItemStackHelper;
import steve_gall.minecolonies_compatibility.core.common.network.message.TeachRecipeMenuSelectMessage;
import steve_gall.minecolonies_compatibility.module.common.AbstractModule;

public class PolymorphModule extends AbstractModule
{
	@Override
	protected void onLoad()
	{
		super.onLoad();

		if (FMLEnvironment.dist.isClient())
		{
			PolymorphApi.client().registerWidget(new IRecipesWidgetFactory()
			{
				@Override
				public IRecipesWidget createWidget(AbstractContainerScreen<?> screen)
				{
					if (screen instanceof WindowCrafting)
					{
						return new AbstractRecipesWidget(screen)
						{
							@Override
							public Slot getOutputSlot()
							{
								var menu = this.containerScreen.getMenu();
								return menu.getSlot(0);
							}

							@Override
							public void selectRecipe(ResourceLocation id)
							{
								MineColoniesCompatibility.network().sendToServer(new TeachRecipeMenuSelectMessage(id));
							}

						};
					}
					else if (screen instanceof TeachRecipeScreen)
					{
						return new AbstractRecipesWidget(screen)
						{
							@Override
							public Slot getOutputSlot()
							{
								var menu = (TeachRecipeMenu<?>) this.containerScreen.getMenu();
								return menu.getResultSlots().get(0);
							}

							@Override
							public void selectRecipe(ResourceLocation id)
							{
								MineColoniesCompatibility.network().sendToServer(new TeachRecipeMenuSelectMessage(id));
							}

							@Override
							public int getXPos()
							{
								return ((TeachRecipeScreen<?, ?>) this.containerScreen).getSwitchButtonX();
							}

							@Override
							public int getYPos()
							{
								return ((TeachRecipeScreen<?, ?>) this.containerScreen).getSwitchButtonY();
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

	public static <RECIPE> void sendRecipesList(ServerPlayer player, TeachRecipeMenu<RECIPE> menu)
	{
		var recipeValidator = menu.getRecipeValidator();

		if (recipeValidator instanceof MenuRecipeValidatorRecipe)
		{
			var pairs = new TreeSet<IRecipePair>();
			ResourceLocation selected = null;
			var recipes = menu.getRecipes();

			for (var i = 0; i < recipes.size(); i++)
			{
				var recipe = (Recipe<?>) recipes.get(i);
				pairs.add(new RecipePair(recipe.getId(), recipe.getResultItem()));

				if (menu.getRecipeIndex() == i)
				{
					selected = recipe.getId();
				}

			}

			PolymorphApi.common().getPacketDistributor().sendRecipesListS2C(player, pairs, selected);
		}

	}

	public static void sendHighlightRecipe(ServerPlayer player, ResourceLocation recipeId)
	{
		PolymorphApi.common().getPacketDistributor().sendHighlightRecipeS2C(player, recipeId);
	}

}
