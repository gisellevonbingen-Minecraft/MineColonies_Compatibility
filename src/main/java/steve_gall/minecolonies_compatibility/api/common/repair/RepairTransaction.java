package steve_gall.minecolonies_compatibility.api.common.repair;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class RepairTransaction
{
	public abstract boolean onHitting(@NotNull EntityContext context);

	@NotNull
	public abstract RepairResult onHitComplete(@NotNull EntityContext context);

	public static class RepairResult
	{
		@NotNull
		public final State state;
		@Nullable
		public final RepairTransaction next;

		@NotNull
		public static RepairResult next(@Nullable RepairTransaction next)
		{
			return new RepairResult(State.NEXT, next);
		}

		@NotNull
		public static RepairResult failed()
		{
			return new RepairResult(State.FAILED, null);
		}

		@NotNull
		public static RepairResult completed()
		{
			return new RepairResult(State.COMPLETED, null);
		}

		private RepairResult(@NotNull State state, @Nullable RepairTransaction next)
		{
			this.state = state;
			this.next = next;
		}

		public enum State
		{
			NEXT,
			FAILED,
			COMPLETED;
		}

	}

}
