package steve_gall.minecolonies_compatibility.core.common.init;

import com.mojang.serialization.Codec;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;

public class ModDataComponents
{
	public static final DeferredRegister<DataComponentType<?>> REGISTER = DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, MineColoniesCompatibility.MOD_ID);

	public static DeferredHolder<DataComponentType<?>, DataComponentType<String>> BUILDING_NAME = REGISTER.register("building_name", () -> DataComponentType.<String> builder().persistent(Codec.STRING).networkSynchronized(ByteBufCodecs.STRING_UTF8).build());
	public static DeferredHolder<DataComponentType<?>, DataComponentType<BlockPos>> RESTRICT_POS_1 = REGISTER.register("restrict_pos_1", () -> DataComponentType.<BlockPos> builder().persistent(BlockPos.CODEC).networkSynchronized(BlockPos.STREAM_CODEC).build());
	public static DeferredHolder<DataComponentType<?>, DataComponentType<BlockPos>> RESTRICT_POS_2 = REGISTER.register("restrict_pos_2", () -> DataComponentType.<BlockPos> builder().persistent(BlockPos.CODEC).networkSynchronized(BlockPos.STREAM_CODEC).build());
}
