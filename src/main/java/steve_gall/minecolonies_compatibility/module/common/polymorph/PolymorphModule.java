package steve_gall.minecolonies_compatibility.module.common.polymorph;

import java.util.TreeSet;

import com.illusivesoulworks.polymorph.api.PolymorphApi;
import com.illusivesoulworks.polymorph.api.client.PolymorphWidgets;
import com.illusivesoulworks.polymorph.api.client.PolymorphWidgets.IRecipesWidgetFactory;
import com.illusivesoulworks.polymorph.api.client.base.AbstractRecipesWidget;
import com.illusivesoulworks.polymorph.api.client.base.IRecipesWidget;
import com.illusivesoulworks.polymorph.api.common.base.IRecipePair;
import com.illusivesoulworks.polymorph.common.util.RecipePair;
import com.minecolonies.api.util.Tuple;
import com.minecolonies.core.client.gui.containers.WindowCrafting;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.GameRules;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.PacketDistributor;
import steve_gall.minecolonies_compatibility.core.client.gui.TeachRecipeScreen;
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
			PolymorphWidgets.getInstance().registerWidget(new IRecipesWidgetFactory()
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
								PacketDistributor.sendToServer(new TeachRecipeMenuSelectMessage(id));
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
								var menu = (TeachRecipeMenu<?, ?>) this.containerScreen.getMenu();
								return menu.getResultSlots().get(0);
							}

							@Override
							public void selectRecipe(ResourceLocation id)
							{
								PacketDistributor.sendToServer(new TeachRecipeMenuSelectMessage(id));
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

	public static void sendRecipesList(ServerPlayer player, CraftingInput input, ItemStack output)
	{
		var level = player.level();
		var registryAccess = level.registryAccess();
		var notlimited = !level.getGameRules().getBoolean(GameRules.RULE_LIMITED_CRAFTING);
		var tuples = level.getRecipeManager().getRecipesFor(RecipeType.CRAFTING, input, level).stream().filter(//
				holder -> holder.value().isSpecial() || (notlimited || player.getRecipeBook().contains(holder) || player.isCreative())//
		).map(holder -> new Tuple<>(holder, holder.value().assemble(input, registryAccess))).toList();

		var pairs = new TreeSet<IRecipePair>();
		ResourceLocation selected = null;

		for (var tuple : tuples)
		{
			var id = tuple.getA().id();
			var stack = tuple.getB();

			if (stack.isEmpty())
			{
				continue;
			}

			pairs.add(new RecipePair(id, stack));

			if (ItemStackHelper.equals(output, stack))
			{
				selected = id;
			}

		}

		PolymorphApi.getInstance().getNetwork().sendRecipesListS2C(player, pairs, selected);
	}

	public static <RECIPE, RECIPE_INPUT> void sendRecipesList(ServerPlayer player, TeachRecipeMenu<RECIPE, RECIPE_INPUT> menu)
	{
		var recipeValidator = menu.getRecipeValidator();
		var pairs = new TreeSet<IRecipePair>();
		ResourceLocation selected = null;
		var recipes = menu.getRecipes();
		var registryAccess = menu.getInventory().player.level().registryAccess();

		for (var i = 0; i < recipes.size(); i++)
		{
			var holder = recipes.get(i);
			var result = recipeValidator.getResultItem(holder, registryAccess);

			if (result.isEmpty())
			{
				continue;
			}

			var recipeId = recipeValidator.getRecipeId(holder);

			if (recipeId == null)
			{
				continue;
			}

			pairs.add(new RecipePair(recipeId, result));

			if (menu.getRecipeIndex() == i)
			{
				selected = recipeId;
			}

		}

		PolymorphApi.getInstance().getNetwork().sendRecipesListS2C(player, pairs, selected);
	}

}
