package steve_gall.minecolonies_compatibility.core.common.colony;

import java.util.Collections;
import java.util.List;

import com.minecolonies.api.IMinecoloniesAPI;
import com.minecolonies.api.colony.ICitizen;
import com.minecolonies.api.colony.ICitizenData;
import com.minecolonies.api.colony.interactionhandling.IChatPriority;
import com.minecolonies.api.colony.interactionhandling.IInteractionResponseHandler;
import com.minecolonies.api.colony.requestsystem.StandardFactoryController;
import com.minecolonies.api.colony.requestsystem.request.IRequest;
import com.minecolonies.api.colony.requestsystem.requestable.crafting.PublicCrafting;
import com.minecolonies.api.colony.requestsystem.token.IToken;
import com.minecolonies.api.crafting.IRecipeStorage;
import com.minecolonies.api.util.Tuple;
import com.minecolonies.core.colony.interactionhandling.ServerCitizenInteraction;
import com.minecolonies.core.colony.interactionhandling.StandardInteraction;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import steve_gall.minecolonies_compatibility.api.common.building.module.ICraftingModuleWithExternalWorkingBlocks;
import steve_gall.minecolonies_compatibility.core.common.init.ModInteractions;
import steve_gall.minecolonies_tweaks.api.common.building.module.ModulePos;

public class WorkingBlockInteraction extends ServerCitizenInteraction
{
	@SuppressWarnings("unchecked")
	private static final Tuple<Component, Component>[] tuples = new Tuple[]{//
			new Tuple<>(Component.translatable(StandardInteraction.INTERACTION_R_OKAY), null), //
			new Tuple<>(Component.translatable(StandardInteraction.INTERACTION_R_IGNORE), null), //
			new Tuple<>(Component.translatable(StandardInteraction.INTERACTION_R_REMIND), null), //
			new Tuple<>(Component.translatable(StandardInteraction.INTERACTION_R_SKIP), null)//
	};

	private ModulePos modulePos;
	private IToken<?> recipeId;
	private IToken<?> requestId;

	public WorkingBlockInteraction(IChatPriority priority, ICraftingModuleWithExternalWorkingBlocks module, IRecipeStorage recipeStorage, IRequest<? extends PublicCrafting> request)
	{
		super(module.getWorkingBlockNotFoundMessage(recipeStorage), true, priority, null, Component.empty(), tuples);

		this.modulePos = new ModulePos(module);
		this.recipeId = recipeStorage.getToken();
		this.requestId = request.getId();
	}

	public WorkingBlockInteraction(ICitizen citizen)
	{
		super(citizen);

		this.modulePos = null;
		this.recipeId = null;
		this.requestId = null;
	}

	@Override
	public boolean isValid(ICitizenData citizen)
	{
		if (this.modulePos.getModule() instanceof ICraftingModuleWithExternalWorkingBlocks module)
		{
			if (module.getBuilding().getColony().getRequestManager().getRequestForToken(this.requestId) == null)
			{
				return false;
			}

			var recipe = IMinecoloniesAPI.getInstance().getColonyManager().getRecipeManager().getRecipe(this.recipeId);

			if (recipe != null)
			{
				var pos = module.getRecipeWorkingBlocks(recipe).findAny().orElse(null);
				return pos == null;
			}

		}

		return false;
	}

	@Override
	protected void loadValidator()
	{

	}

	@Override
	public CompoundTag serializeNBT()
	{
		var controller = StandardFactoryController.getInstance();
		var tag = super.serializeNBT();
		tag.put("modulePos", this.modulePos.serializeNBT());
		tag.put("recipeId", controller.serialize(this.recipeId));
		tag.put("requestId", controller.serialize(this.requestId));

		return tag;
	}

	@Override
	public void deserializeNBT(CompoundTag tag)
	{
		var controller = StandardFactoryController.getInstance();
		super.deserializeNBT(tag);

		this.modulePos = new ModulePos(tag.getCompound("modulePos"));
		this.recipeId = controller.deserialize(tag.getCompound("recipeId"));
		this.requestId = controller.deserialize(tag.getCompound("requestId"));
	}

	@Override
	public List<IInteractionResponseHandler> genChildInteractions()
	{
		return Collections.emptyList();
	}

	@Override
	public String getType()
	{
		return ModInteractions.WORKING_BLOCK.getId().getPath();
	}

	public ModulePos getModulePos()
	{
		return this.modulePos;
	}

	public IToken<?> getRecipeId()
	{
		return this.recipeId;
	}

	public IToken<?> getRequestId()
	{
		return this.requestId;
	}

}
