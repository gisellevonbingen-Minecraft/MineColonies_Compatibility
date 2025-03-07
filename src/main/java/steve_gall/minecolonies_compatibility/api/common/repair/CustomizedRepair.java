package steve_gall.minecolonies_compatibility.api.common.repair;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.minecolonies.api.util.Tuple;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public abstract class CustomizedRepair
{
	private static final Map<ResourceLocation, CustomizedRepair> REGISTRY = new HashMap<>();

	public static void register(@NotNull CustomizedRepair repair)
	{
		REGISTRY.put(repair.getId(), repair);
	}

	public static Map<ResourceLocation, CustomizedRepair> getRegistry()
	{
		return Collections.unmodifiableMap(REGISTRY);
	}

	@Override
	public int hashCode()
	{
		return this.getId().hashCode();
	}

	@Override
	public boolean equals(Object obj)
	{
		return this == obj;
	}

	@NotNull
	public abstract ResourceLocation getId();

	@Nullable
	public abstract CheckResult check(EntityContext context);

	public static class CheckResult
	{
		@Nullable
		public final Tuple<Predicate<ItemStack>, Integer> needsCurrently;
		@Nullable
		public final RepairTransaction transaction;

		@NotNull
		public static CheckResult needsCurrently(@NotNull Tuple<Predicate<ItemStack>, Integer> needsCurrently)
		{
			return new CheckResult(needsCurrently, null);
		}

		@NotNull
		public static CheckResult repair(@NotNull RepairTransaction repair)
		{
			return new CheckResult(null, repair);
		}

		private CheckResult(@Nullable Tuple<Predicate<ItemStack>, Integer> needsCurrently, @Nullable RepairTransaction repair)
		{
			this.needsCurrently = needsCurrently;
			this.transaction = repair;
		}

	}

}
