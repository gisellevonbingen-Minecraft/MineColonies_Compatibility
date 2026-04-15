package steve_gall.minecolonies_compatibility.module.client.silentgear;

import java.util.List;

import com.minecolonies.api.crafting.ItemStorage;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.silentchaos512.gear.api.part.PartType;
import net.silentchaos512.gear.gear.material.MaterialInstance;
import steve_gall.minecolonies_compatibility.core.client.gui.TeachRecipeScreen;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.module.common.silentgear.menu.RepairMaterialTeachMenu;
import steve_gall.minecolonies_compatibility.module.common.silentgear.network.RepairMaterialMaterialMessage;

public class RepairMaterialTeachScreen extends TeachRecipeScreen<RepairMaterialTeachMenu, ItemStack>
{
	public static final ResourceLocation TEXTURE = MineColoniesCompatibility.rl("textures/gui/silentgear_repair_material_teach.png");
	public static final MutableComponent TEXT_TIER_PREFIX = Component.translatable("minecolonies_compatibility.text.silentgear_teach_repair_material.tier");
	public static final MutableComponent TEXT_MATERIAL_PREFIX = Component.translatable("minecolonies_compatibility.text.silentgear_teach_repair_material.material");
	public static final MutableComponent TEXT_VALUE_PREFIX = Component.translatable("minecolonies_compatibility.text.silentgear_teach_repair_material.value");

	private ItemStack lastRecipe = null;
	private Component materialText = null;
	private Component tierText = null;
	private Component valueText = null;

	public RepairMaterialTeachScreen(RepairMaterialTeachMenu menu, Inventory inventory, Component title)
	{
		super(menu, inventory, title);

		this.imageWidth = 176;
		this.imageHeight = 166;
	}

	@Override
	protected void onDone(ItemStack item, List<ItemStorage> input)
	{
		MineColoniesCompatibility.network().sendToServer(new RepairMaterialMaterialMessage(this.menu.getModulePos().getModuleView(), true, item));
	}

	@Override
	protected void renderBg(GuiGraphics graphics, float partialTicks, int mouseX, int mouseY)
	{
		super.renderBg(graphics, partialTicks, mouseX, mouseY);

		var recipe = this.menu.getRecipe();

		if (this.lastRecipe != recipe)
		{
			this.lastRecipe = recipe;

			if (recipe != null)
			{
				var material = MaterialInstance.from(recipe);
				this.materialText = Component.translatable("%s: %s", TEXT_MATERIAL_PREFIX, material.getDisplayName(PartType.MAIN));
				this.tierText = Component.translatable("%s: %s", TEXT_TIER_PREFIX, material.getTier());
				this.valueText = Component.translatable("%s: %s", TEXT_VALUE_PREFIX, material.getRepairValue(recipe));
			}

		}

		if (this.lastRecipe != null)
		{
			var x = this.leftPos + 46;
			var y = this.topPos + 31;
			graphics.drawString(this.font, this.materialText, x, y, 0xFFFFFFFF);
			y += this.font.lineHeight;
			graphics.drawString(this.font, this.tierText, x, y, 0xFFFFFFFF);
			y += this.font.lineHeight;
			graphics.drawString(this.font, this.valueText, x, y, 0xFFFFFFFF);
			y += this.font.lineHeight;
		}

	}

	@Override
	public ResourceLocation getTexture()
	{
		return TEXTURE;
	}

}
