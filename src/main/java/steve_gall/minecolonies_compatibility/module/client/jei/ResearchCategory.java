package steve_gall.minecolonies_compatibility.module.client.jei;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import com.minecolonies.api.IMinecoloniesAPI;
import com.minecolonies.api.MinecoloniesAPIProxy;
import com.minecolonies.api.research.IGlobalResearch;
import com.minecolonies.api.research.IResearchRequirement;
import com.minecolonies.api.research.util.ResearchConstants;
import com.minecolonies.api.util.Tuple;
import com.minecolonies.api.util.constant.Constants;
import com.minecolonies.core.research.AlternateBuildingResearchRequirement;
import com.minecolonies.core.research.BuildingResearchRequirement;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.module.client.jei.ResearchCategory.ResearchCache;

public class ResearchCategory implements IRecipeCategory<ResearchCache>
{
	public static final Component TITLE = Component.translatable("com.minecolonies.coremod.research.research");
	public static final ResourceLocation TEXTURE = MineColoniesCompatibility.rl("textures/jei/research.png");

	private final IDrawable background;
	private final IDrawable slot;

	public ResearchCategory(IGuiHelper guiHelper)
	{
		this.background = guiHelper.createDrawable(TEXTURE, 0, 0, 176, 63);
		this.slot = guiHelper.getSlotDrawable();
	}

	@Override
	public RecipeType<ResearchCache> getRecipeType()
	{
		return ModJeiRecipeTypes.RESEARCH;
	}

	@Override
	public Component getTitle()
	{
		return TITLE;
	}

	@Override
	public IDrawable getBackground()
	{
		return this.background;
	}

	@Override
	public IDrawable getIcon()
	{
		return null;
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, ResearchCache cache, IFocusGroup focuses)
	{
		var requirementIndex = 0;

		for (var tuple : cache.requirements)
		{
			for (var building : tuple.getB())
			{
				var xi = requirementIndex % 9;
				var yi = requirementIndex / 9;
				var slotBuilder = builder.addSlot(RecipeIngredientRole.RENDER_ONLY, 8 + 18 * xi, 22 + 18 * yi);
				slotBuilder.setBackground(this.slot, -1, -1);
				slotBuilder.addItemStack(building);
				slotBuilder.addTooltipCallback((recipeSlotView, tooltip) ->
				{
					tooltip.clear();
					tooltip.add(tuple.getA().getDesc());
				});
				requirementIndex++;
			}

		}

		var costs = cache.costs;

		for (var i = 0; i < costs.size(); i++)
		{
			var cost = costs.get(i);
			var xi = i % 9;
			var yi = i / 9;
			var slotBuilder = builder.addSlot(RecipeIngredientRole.INPUT, 8 + 18 * xi, 45 + 18 * yi);
			slotBuilder.setBackground(this.slot, -1, -1);
			slotBuilder.addIngredients(VanillaTypes.ITEM_STACK, cost);
		}

	}

	@Override
	public List<Component> getTooltipStrings(ResearchCache cache, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY)
	{
		var mc = Minecraft.getInstance();
		var font = mc.font;

		if (0.0F <= mouseY && mouseY <= 10.0F + font.lineHeight)
		{
			return cache.tooltip;
		}

		return Collections.emptyList();
	}

	@Override
	public void draw(ResearchCache cache, IRecipeSlotsView recipeSlotsView, GuiGraphics graphics, double mouseX, double mouseY)
	{
		var mc = Minecraft.getInstance();
		var font = mc.font;
		graphics.drawString(font, cache.header, 0, 0, 0xFF000000, false);
		graphics.drawString(font, cache.name, 0, 10, 0xFF000000, false);
	}

	public static class ResearchCache
	{
		public final IGlobalResearch research;
		public final Component header;
		public final Component name;
		public final List<Component> effects;
		public final List<Tuple<IResearchRequirement, List<ItemStack>>> requirements;
		public final List<List<ItemStack>> costs;
		public final List<Component> tooltip;

		public ResearchCache(IGlobalResearch research)
		{
			var branch = MinecoloniesAPIProxy.getInstance().getGlobalResearchTree().getBranchData(research.getBranch());
			var depth = research.getDepth();
			var shosingDepth = (depth > Constants.MAX_BUILDING_LEVEL) ? Constants.MAX_BUILDING_LEVEL : depth;
			var level = Component.translatable("com.minecolonies.coremod.gui.research.tier.header", shosingDepth, branch.getHoursTime(depth));

			this.research = research;
			this.header = MutableComponent.create(branch.getName()).append(" - ").append(level);
			this.name = MutableComponent.create(research.getName());
			this.effects = research.getEffects().stream().map(effect -> (Component) MutableComponent.create(effect.getDesc())).toList();
			this.requirements = research.getResearchRequirement().stream().map(requirement ->
			{
				return new Tuple<>(requirement, this.getBuildingTuples(requirement).map(tuple ->
				{
					var stack = new ItemStack(this.getBuildingItem(tuple.getA()));
					stack.setCount(tuple.getB());
					return stack;
				}).toList());
			}).toList();

			this.costs = research.getCostList().stream().map(cost -> cost.getItems().stream().map(item ->
			{
				return new ItemStack(item, cost.getCount());
			}).toList()).toList();

			this.tooltip = new ArrayList<>();
			this.tooltip.add(this.header);
			this.tooltip.add(Component.empty().append(this.name).withStyle(ChatFormatting.BOLD).withStyle(ChatFormatting.GOLD));
			this.tooltip.addAll(this.effects);
			var style = Style.EMPTY.withColor(ResearchConstants.COLOR_TEXT_UNFULFILLED);

			for (var requirement : research.getResearchRequirement())
			{
				this.tooltip.add(Component.literal(" - ").append(requirement.getDesc()).withStyle(style));
			}

			for (var cost : research.getCostList())
			{
				this.tooltip.add(Component.literal(" - ").append(Component.translatable("com.minecolonies.coremod.research.limit.requirement", cost.getCount(), cost.getTranslatedName())).withStyle(style));
			}

		}

		private ItemLike getBuildingItem(String buildingName)
		{
			var buildingPath = new ResourceLocation(Constants.MOD_ID, buildingName);
			var buildingRegistry = IMinecoloniesAPI.getInstance().getBuildingRegistry();

			if (buildingRegistry.containsKey(buildingPath))
			{
				return buildingRegistry.getValue(buildingPath).getBuildingBlock();
			}
			else
			{
				return Items.AIR;
			}

		}

		private Stream<Tuple<String, Integer>> getBuildingTuples(IResearchRequirement requirement)
		{
			if (requirement instanceof AlternateBuildingResearchRequirement alternateBuildingRequirement)
			{
				return alternateBuildingRequirement.getBuildings().entrySet().stream().map(entry ->
				{
					return new Tuple<>(entry.getKey(), entry.getValue());
				});
			}
			else if (requirement instanceof BuildingResearchRequirement buildingRequirement)
			{
				return Stream.of(new Tuple<>(buildingRequirement.getBuilding(), buildingRequirement.getBuildingLevel()));
			}

			return Stream.empty();
		}

	}

}
