package steve_gall.minecolonies_compatibility.core.common.network.message;

import com.minecolonies.api.advancements.AdvancementTriggers;
import com.minecolonies.api.colony.IColonyManager;
import com.minecolonies.api.colony.buildings.modules.ICraftingBuildingModule;
import com.minecolonies.api.colony.requestsystem.StandardFactoryController;
import com.minecolonies.api.crafting.IRecipeStorage;
import com.minecolonies.api.util.MessageUtils;
import com.minecolonies.api.util.SoundUtils;
import com.minecolonies.api.util.constant.TranslationConstants;
import com.minecolonies.core.colony.buildings.moduleviews.CraftingModuleView;
import com.minecolonies.core.util.AdvancementUtils;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;

public class AddRemoveRecipeMessage2 extends BuildingModuleMessage
{
	public static final CustomPacketPayload.Type<AddRemoveRecipeMessage2> TYPE = new CustomPacketPayload.Type<>(MineColoniesCompatibility.rl("add_remove_recipe_2"));

	private final boolean remove;
	private final IRecipeStorage recipeStorage;

	public AddRemoveRecipeMessage2(CraftingModuleView building, boolean remove, IRecipeStorage recipeStorage)
	{
		super(building);

		this.remove = remove;
		this.recipeStorage = recipeStorage;
	}

	public AddRemoveRecipeMessage2(RegistryFriendlyByteBuf buffer)
	{
		super(buffer);

		this.remove = buffer.readBoolean();
		this.recipeStorage = StandardFactoryController.getInstance().deserialize(buffer);
	}

	@Override
	public void encode(RegistryFriendlyByteBuf buffer)
	{
		super.encode(buffer);

		buffer.writeBoolean(this.remove);
		StandardFactoryController.getInstance().serialize(buffer, this.recipeStorage);
	}

	@Override
	public void handle(IPayloadContext context)
	{
		super.handle(context);

		var player = context.player();

		if (this.getModule() instanceof ICraftingBuildingModule module)
		{
			var building = module.getBuilding();

			if (this.remove)
			{
				module.removeRecipe(this.recipeStorage.getToken());
				SoundUtils.playSuccessSound(player, player.blockPosition());
			}
			else
			{
				var token = IColonyManager.getInstance().getRecipeManager().checkOrAddRecipe(this.recipeStorage);

				if (!module.addRecipe(token))
				{
					SoundUtils.playErrorSound(player, player.blockPosition());
					MessageUtils.format(TranslationConstants.UNABLE_TO_ADD_RECIPE_MESSAGE, Component.translatableEscape(building.getBuildingDisplayName())).sendTo(player);
				}
				else
				{
					SoundUtils.playSuccessSound(player, player.blockPosition());
					AdvancementUtils.TriggerAdvancementPlayersForColony(building.getColony(), playerMP -> AdvancementTriggers.BUILDING_ADD_RECIPE.get().trigger(playerMP, this.recipeStorage));
					MessageUtils.format(TranslationConstants.MESSAGE_RECIPE_SAVED).sendTo(player);
				}

			}

			building.markDirty();
		}

	}

	@Override
	public CustomPacketPayload.Type<AddRemoveRecipeMessage2> type()
	{
		return TYPE;
	}

	public boolean isRemove()
	{
		return remove;
	}

	public IRecipeStorage getRecipeStorage()
	{
		return this.recipeStorage;
	}

}
