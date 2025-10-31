package steve_gall.minecolonies_compatibility.core.common.network;

import steve_gall.minecolonies_compatibility.core.common.network.message.AccessDirectionMessage;
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
import steve_gall.minecolonies_tweaks.api.common.network.NetworkChannel;

public class ModMessagesRegistrar
{
	public static void register(NetworkChannel channel)
	{
		channel.registerMessage(TeachRecipeMenuNewRecipesMessage.class, TeachRecipeMenuNewRecipesMessage::new);
		channel.registerMessage(TeachRecipeMenuNewResultMessage.class, TeachRecipeMenuNewResultMessage::new);
		channel.registerMessage(TeachRecipeMenuSwitchingMessage.class, TeachRecipeMenuSwitchingMessage::new);
		channel.registerMessage(TeachRecipeMenuSelectMessage.class, TeachRecipeMenuSelectMessage::new);
		channel.registerMessage(JEIGhostAcceptItemMessage.class, JEIGhostAcceptItemMessage::new);
		channel.registerMessage(JEIGhostAcceptFluidMessage.class, JEIGhostAcceptFluidMessage::new);
		channel.registerMessage(JEIRecipeTransferMessage.class, JEIRecipeTransferMessage::new);
		channel.registerMessage(NetworkStorageRefreshMessage.class, NetworkStorageRefreshMessage::new);
		channel.registerMessage(RestrictSetEnabledMessage.class, RestrictSetEnabledMessage::new);
		channel.registerMessage(RestrictSetAreaMessage.class, RestrictSetAreaMessage::new);
		channel.registerMessage(RestrictGiveToolMessage.class, RestrictGiveToolMessage::new);
		channel.registerMessage(BucketFillingOpenTeachMessage.class, BucketFillingOpenTeachMessage::new);
		channel.registerMessage(SmithingOpenTeachMessage.class, SmithingOpenTeachMessage::new);
		channel.registerMessage(SmithingTemplateOpenInventoryMessage.class, SmithingTemplateOpenInventoryMessage::new);
		channel.registerMessage(AccessDirectionMessage.class, AccessDirectionMessage::new);
		channel.registerMessage(StonecutterOpenTeachMessage.class, StonecutterOpenTeachMessage::new);
	}

	private ModMessagesRegistrar()
	{

	}

}
