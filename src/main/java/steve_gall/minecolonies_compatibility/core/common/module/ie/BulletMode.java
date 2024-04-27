package steve_gall.minecolonies_compatibility.core.common.module.ie;

public enum BulletMode
{
	DONT_USE(false, true),
	CAN_USE(true, true),
	ONLY_USE(true, false)
	//
	;

	private final boolean canUse;
	private final boolean canDefault;

	private BulletMode(boolean canUse, boolean canDefault)
	{
		this.canUse = canUse;
		this.canDefault = canDefault;
	}

	public boolean onlyUse()
	{
		return this.canUse() && !this.canDefault();
	}

	public boolean canUse()
	{
		return this.canUse;
	}

	public boolean canDefault()
	{
		return this.canDefault;
	}

}
