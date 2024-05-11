package steve_gall.minecolonies_compatibility.module.common.polymorph;

import java.util.UUID;

import com.illusivesoulworks.polymorph.api.PolymorphApi;
import com.illusivesoulworks.polymorph.common.capability.StackRecipeData;
import com.minecolonies.api.inventory.container.ContainerCrafting;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;

public class CraftingWindowRecipeData extends StackRecipeData
{
	public CraftingWindowRecipeData(ItemStack owner)
	{
		super(owner);
	}

	@Override
	public void selectRecipe(Recipe<?> recipe)
	{
		super.selectRecipe(recipe);

		var tag = this.getOwner().getTagElement(MineColoniesCompatibility.MOD_ID);

		if (tag == null)
		{
			return;
		}

		var craftingRecipe = (CraftingRecipe) recipe;
		var id = recipe.getId();
		var uuid = UUID.fromString(tag.getString("player"));
		var server = PolymorphApi.common().getServer().orElse(null);

		for (ServerLevel level : server.getAllLevels())
		{
			if (level.getPlayerByUUID(uuid) instanceof ServerPlayer player && player.containerMenu instanceof ContainerCrafting crafting)
			{
				crafting.craftResult.setItem(0, craftingRecipe.assemble(crafting.craftMatrix));
				PolymorphApi.common().getPacketDistributor().sendHighlightRecipeS2C(player, id);
			}

		}

	}

}
