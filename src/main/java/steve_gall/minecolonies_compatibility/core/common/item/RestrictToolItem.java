package steve_gall.minecolonies_compatibility.core.common.item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.minecolonies.api.items.IBlockOverlayItem;
import com.minecolonies.api.util.MessageUtils;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.AABB;
import steve_gall.minecolonies_compatibility.api.common.building.module.IRestrictableModule;
import steve_gall.minecolonies_compatibility.api.common.building.module.IRestrictableModuleView;
import steve_gall.minecolonies_compatibility.core.common.init.ModDataComponents;
import steve_gall.minecolonies_tweaks.api.common.building.module.ModulePos;
import steve_gall.minecolonies_tweaks.core.common.init.MCTweaksDataComponents;

public class RestrictToolItem extends Item implements IBlockOverlayItem
{
	public RestrictToolItem(Item.Properties properties)
	{
		super(properties.stacksTo(1));
	}

	@Override
	public boolean canAttackBlock(BlockState state, Level level, BlockPos pos, Player player)
	{
		if (!level.isClientSide())
		{
			var stack = player.getMainHandItem();
			this.setPos1(stack, pos);
			MessageUtils.format("minecolonies_compatibility.text.restrict.set1").sendTo(player);
			this.updateModuleArea(player, stack);
		}

		return false;
	}

	@Override
	public InteractionResult useOn(UseOnContext context)
	{
		if (!context.getLevel().isClientSide())
		{
			var player = context.getPlayer();
			var stack = context.getItemInHand();
			this.setPos2(stack, context.getClickedPos());
			MessageUtils.format("minecolonies_compatibility.text.restrict.set2").sendTo(player);
			this.updateModuleArea(player, stack);
		}

		return InteractionResult.FAIL;
	}

	public void setModule(ItemStack stack, IRestrictableModule module, Component moduleDesc)
	{
		stack.set(DataComponents.CUSTOM_NAME, Component.empty().append(super.getName(stack)).append(": ").append(moduleDesc));
		stack.set(MCTweaksDataComponents.MODULE_POS, new ModulePos(module));
		stack.set(ModDataComponents.BUILDING_NAME, module.getBuilding().getBuildingDisplayName());
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag)
	{
		super.appendHoverText(stack, context, tooltip, flag);

		var modulePos = this.getModulePos(stack);

		if (modulePos != null)
		{
			var pos = modulePos.getBuildingId();
			tooltip.add(Component.translatable("item.minecolonies_compatibility.restrict_tool.name", Component.translatable(stack.getOrDefault(ModDataComponents.BUILDING_NAME, ""))));
			tooltip.add(Component.translatable("item.minecolonies_compatibility.restrict_tool.pos", pos.getX(), pos.getY(), pos.getZ()));
		}

	}

	@Nullable
	public ModulePos getModulePos(ItemStack stack)
	{
		return stack.get(MCTweaksDataComponents.MODULE_POS);
	}

	@Nullable
	public IRestrictableModule getModule(ItemStack stack)
	{
		var pos = this.getModulePos(stack);
		return pos != null && pos.getModule() instanceof IRestrictableModule module ? module : null;
	}

	@Nullable
	public IRestrictableModuleView getModuleView(ItemStack stack)
	{
		var pos = this.getModulePos(stack);
		return pos != null && pos.getModuleView() instanceof IRestrictableModuleView module ? module : null;
	}

	public void updateModuleArea(Player player, ItemStack stack)
	{
		var pos1 = this.getPos1(stack);
		var pos2 = this.getPos2(stack);

		if (pos1 == null || pos2 == null)
		{
			return;
		}

		var module = this.getModule(stack);

		if (module == null)
		{
			return;
		}

		var box = BoundingBox.fromCorners(pos1, pos2);
		var distX = box.maxX() - box.minX() + 1;
		var distY = box.maxY() - box.minY() + 1;
		var distZ = box.maxZ() - box.minZ() + 1;
		var volume = distX * distY * distZ;
		var maxVolume = (int) Math.floor(2 * Math.pow(module.getSearchRange(), 3));

		if (volume > maxVolume)
		{
			MessageUtils.format("minecolonies_compatibility.text.restrict.too_big", volume, maxVolume).sendTo(player);
			return;
		}

		MessageUtils.format("minecolonies_compatibility.text.restrict.update", //
				box.minX(), box.maxX(), box.minY(), box.maxY(), box.minZ(), box.maxZ(), //
				volume, maxVolume).sendTo(player);
		module.setRestrictArea(pos1, pos2);
	}

	protected void setPos(ItemStack stack, DataComponentType<BlockPos> key, @Nullable BlockPos pos)
	{
		if (pos == null)
		{
			stack.remove(key);
		}
		else
		{
			stack.set(key, pos);
		}

	}

	@Nullable
	protected BlockPos getPos(ItemStack stack, DataComponentType<BlockPos> key)
	{
		return stack.get(key);
	}

	public void setPos1(ItemStack stack, @Nullable BlockPos pos)
	{
		this.setPos(stack, ModDataComponents.RESTRICT_POS_1.get(), pos);
	}

	public void setPos2(ItemStack stack, @Nullable BlockPos pos)
	{
		this.setPos(stack, ModDataComponents.RESTRICT_POS_2.get(), pos);
	}

	@Nullable
	public BlockPos getPos1(ItemStack stack)
	{
		return this.getPos(stack, ModDataComponents.RESTRICT_POS_1.get());
	}

	@Nullable
	public BlockPos getPos2(ItemStack stack)
	{
		return this.getPos(stack, ModDataComponents.RESTRICT_POS_2.get());
	}

	@Override
	public @NotNull List<OverlayBox> getOverlayBoxes(@NotNull Level world, @NotNull Player player, @NotNull ItemStack stack)
	{
		var module = this.getModuleView(stack);

		if (module != null)
		{
			var pos = module.getBuildingView().getID();
			var pos1 = module.getRestrictAreaPos1();
			var pos2 = module.getRestrictAreaPos2();
			var p1 = this.getPos1(stack);
			var p2 = this.getPos2(stack);

			var boxes = new ArrayList<OverlayBox>();
			if (p1 != null && p2 != null)
			{
				boxes.add(new OverlayBox(new AABB(p1).minmax(new AABB(p2)), 0xFF00FF00, 0.04F, true));
			}
			else
			{
				boxes.add(new OverlayBox(new AABB(pos1).minmax(new AABB(pos2)), 0xFF00FF00, 0.04F, true));
			}

			if (p1 != null)
			{
				boxes.add(new OverlayBox(new AABB(p1), 0xFFFF0000, 0.02F, true));
			}

			if (p2 != null)
			{
				boxes.add(new OverlayBox(new AABB(p2), 0xFF0000FF, 0.02F, true));
			}

			boxes.add(new OverlayBox(new AABB(pos), 0xFFFFFFFF, 0.02F, true));
			return boxes;
		}

		return Collections.emptyList();
	}

}
