package steve_gall.minecolonies_compatibility.module.client.lets_do_bakery;

import java.util.List;

import com.minecolonies.api.crafting.ItemStorage;
import com.minecolonies.api.crafting.registry.CraftingType;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.satisfy.bakery.recipe.CraftingBowlRecipe;
import steve_gall.minecolonies_compatibility.core.client.gui.TeachCraftingRecipeScreen;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.module.common.lets_do_bakery.crafting.BowlRecipeStorage;
import steve_gall.minecolonies_compatibility.module.common.lets_do_bakery.init.ModuleCraftingTypes;
import steve_gall.minecolonies_compatibility.module.common.lets_do_bakery.menu.BowlTeachMenu;
import steve_gall.minecolonies_tweaks.api.common.crafting.ICustomizedRecipeStorage;

public class BowlTeachScreen extends TeachCraftingRecipeScreen<BowlTeachMenu, CraftingBowlRecipe>
{
	public static final ResourceLocation TEXTURE = MineColoniesCompatibility.rl("textures/gui/lets_do_bakery_bowl_teach.png");

	public BowlTeachScreen(BowlTeachMenu menu, Inventory inventory, Component title)
	{
		super(menu, inventory, title);

		this.imageWidth = 176;
		this.imageHeight = 166;
	}

	@Override
	public ResourceLocation getTexture()
	{
		return TEXTURE;
	}

	@Override
	public CraftingType getCraftingType()
	{
		return ModuleCraftingTypes.BOWL.get();
	}

	@Override
	protected ICustomizedRecipeStorage createRecipeStorage(CraftingBowlRecipe recipe, List<ItemStorage> input)
	{
		var resultContainer = this.menu.getResultContainer();
		var output = resultContainer.getItem(0);
		return new BowlRecipeStorage(recipe.getId(), input, output);
	}

}
