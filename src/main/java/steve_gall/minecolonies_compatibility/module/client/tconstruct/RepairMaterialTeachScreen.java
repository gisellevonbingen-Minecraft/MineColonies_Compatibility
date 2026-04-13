package steve_gall.minecolonies_compatibility.module.client.tconstruct;

import java.util.List;

import com.minecolonies.api.crafting.ItemStorage;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import slimeknights.tconstruct.library.client.materials.MaterialTooltipCache;
import slimeknights.tconstruct.library.materials.definition.MaterialVariant;
import steve_gall.minecolonies_compatibility.core.client.gui.TeachRecipeScreen;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.module.common.tconstruct.MaterialHelper;
import steve_gall.minecolonies_compatibility.module.common.tconstruct.RepairValue;
import steve_gall.minecolonies_compatibility.module.common.tconstruct.menu.RepairMaterialTeachMenu;
import steve_gall.minecolonies_compatibility.module.common.tconstruct.network.RepairMaterialUpdateMessage;

public class RepairMaterialTeachScreen extends TeachRecipeScreen<RepairMaterialTeachMenu, RepairValue>
{
	public static final ResourceLocation TEXTURE = MineColoniesCompatibility.rl("textures/gui/tconstruct_repair_material_teach.png");
	public static final MutableComponent TEXT_MATERIAL_PREFIX = Component.translatable("minecolonies_compatibility.text.tconstruct_teach_repair_material.material");
	public static final MutableComponent TEXT_TIER_PREFIX = Component.translatable("minecolonies_compatibility.text.tconstruct_teach_repair_material.tier");
	public static final MutableComponent TEXT_VALUE_PREFIX = Component.translatable("minecolonies_compatibility.text.tconstruct_teach_repair_material.value");

	private RepairValue lastRecipe = null;
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
	protected void init()
	{
		super.init();
	}

	@Override
	protected void onDone(RepairValue recipe, List<ItemStorage> input)
	{
		MineColoniesCompatibility.network().sendToServer(RepairMaterialUpdateMessage.add(this.menu.getModulePos().getModuleView(), input.get(0).getItemStack()));
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
				this.materialText = Component.translatable("%s: %s", TEXT_MATERIAL_PREFIX, MaterialTooltipCache.getDisplayName(recipe.material().getId()));
				this.tierText = Component.translatable("%s: %s", TEXT_TIER_PREFIX, MaterialHelper.getRequiredLevel(MaterialVariant.of(recipe.material())));

				if (recipe.needed() > 1)
				{
					this.valueText = Component.translatable("%s: %s/%s", TEXT_VALUE_PREFIX, recipe.value(), recipe.needed());
				}
				else
				{
					this.valueText = Component.translatable("%s: %s", TEXT_VALUE_PREFIX, recipe.value());
				}

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
