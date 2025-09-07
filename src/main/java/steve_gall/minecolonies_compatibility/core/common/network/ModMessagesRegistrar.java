package steve_gall.minecolonies_compatibility.core.common.network;

import steve_gall.minecolonies_compatibility.core.common.network.message.AccessDirectionMessage;
import steve_gall.minecolonies_compatibility.core.common.network.message.BlockEntityRequestModelDataUpdateMessage;
import steve_gall.minecolonies_compatibility.core.common.network.message.BucketFillingOpenTeachMessage;
import steve_gall.minecolonies_compatibility.core.common.network.message.JEIGhostAcceptFluidMessage;
import steve_gall.minecolonies_compatibility.core.common.network.message.JEIGhostAcceptItemMessage;
import steve_gall.minecolonies_compatibility.core.common.network.message.JEIRecipeTransferMessage;
import steve_gall.minecolonies_compatibility.core.common.network.message.NetworkStorageRefreshMessage;
import steve_gall.minecolonies_compatibility.core.common.network.message.RestrictGiveToolMessage;
import steve_gall.minecolonies_compatibility.core.common.network.message.RestrictSetAreaMessage;
import steve_gall.minecolonies_compatibility.core.common.network.message.RestrictSetEnabledMessage;
import steve_gall.minecolonies_compatibility.core.common.network.message.SmithingOpenTeachMessage;
import steve_gall.minecolonies_compatibility.core.common.network.message.SmithingTemplateOpenInventoryMessage;
import steve_gall.minecolonies_compatibility.core.common.network.message.StonecutterOpenTeachMessage;
import steve_gall.minecolonies_compatibility.core.common.network.message.TeachRecipeMenuNewRecipesMessage;
import steve_gall.minecolonies_compatibility.core.common.network.message.TeachRecipeMenuNewResultMessage;
import steve_gall.minecolonies_compatibility.core.common.network.message.TeachRecipeMenuSelectMessage;
import steve_gall.minecolonies_compatibility.core.common.network.message.TeachRecipeMenuSwitchingMessage;
import steve_gall.minecolonies_compatibility.module.common.ModuleManager;
import steve_gall.minecolonies_tweaks.api.common.network.MessageRegistrar;

public class ModMessagesRegistrar
{
	public static void register(MessageRegistrar channel)
	{
		channel.playToClient(TeachRecipeMenuNewRecipesMessage.TYPE, TeachRecipeMenuNewRecipesMessage::new);
		channel.playToClient(TeachRecipeMenuNewResultMessage.TYPE, TeachRecipeMenuNewResultMessage::new);
		channel.playToServer(TeachRecipeMenuSwitchingMessage.TYPE, TeachRecipeMenuSwitchingMessage::new);
		channel.playToServer(TeachRecipeMenuSelectMessage.TYPE, TeachRecipeMenuSelectMessage::new);
		channel.playToServer(JEIGhostAcceptItemMessage.TYPE, JEIGhostAcceptItemMessage::new);
		channel.playToServer(JEIGhostAcceptFluidMessage.TYPE, JEIGhostAcceptFluidMessage::new);
		channel.playToServer(JEIRecipeTransferMessage.TYPE, JEIRecipeTransferMessage::new);
		channel.playToServer(NetworkStorageRefreshMessage.TYPE, NetworkStorageRefreshMessage::new);
		channel.playToServer(RestrictSetEnabledMessage.TYPE, RestrictSetEnabledMessage::new);
		channel.playToServer(RestrictSetAreaMessage.TYPE, RestrictSetAreaMessage::new);
		channel.playToServer(RestrictGiveToolMessage.TYPE, RestrictGiveToolMessage::new);
		channel.playToServer(BucketFillingOpenTeachMessage.TYPE, BucketFillingOpenTeachMessage::new);
		channel.playToServer(SmithingOpenTeachMessage.TYPE, SmithingOpenTeachMessage::new);
		channel.playToServer(SmithingTemplateOpenInventoryMessage.TYPE, SmithingTemplateOpenInventoryMessage::new);
		channel.playToServer(AccessDirectionMessage.TYPE, AccessDirectionMessage::new);
		channel.playToServer(StonecutterOpenTeachMessage.TYPE, StonecutterOpenTeachMessage::new);
		channel.playToClient(BlockEntityRequestModelDataUpdateMessage.TYPE, BlockEntityRequestModelDataUpdateMessage::new);

		ModuleManager.LOADED_MODULES.forEach(m -> m.onRegisterNetwork(channel));
	}

	private ModMessagesRegistrar()
	{

	}

}
