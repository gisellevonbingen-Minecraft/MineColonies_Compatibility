package steve_gall.minecolonies_compatibility.core.common.job;

import org.jetbrains.annotations.Nullable;

import com.minecolonies.api.client.render.modeltype.ModModelTypes;
import com.minecolonies.api.colony.ICitizenData;
import com.minecolonies.core.colony.jobs.AbstractJob;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import steve_gall.minecolonies_compatibility.api.common.butcher.CustomizedButcherable;
import steve_gall.minecolonies_compatibility.core.common.entity.ai.butcher.EntityAIWorkButcher;

public class JobButcher extends AbstractJob<EntityAIWorkButcher, JobButcher>
{
	public static final String TAG_TABLE_NEEDED = "TableNeeded";

	@Nullable
	private CustomizedButcherable tableNeeded = null;

	public JobButcher(ICitizenData entity)
	{
		super(entity);
	}

	public @Nullable CustomizedButcherable getTableNeeded()
	{
		return tableNeeded;
	}

	public void setTableNeeded(@Nullable CustomizedButcherable tableNeeded)
	{
		this.tableNeeded = tableNeeded;
	}

	@Override
	public CompoundTag serializeNBT(HolderLookup.Provider provider)
	{
		var compound = super.serializeNBT(provider);

		if (this.tableNeeded != null)
		{
			compound.putString(TAG_TABLE_NEEDED, this.tableNeeded.getId().toString());
		}

		return compound;
	}

	@Override
	public void deserializeNBT(HolderLookup.Provider provider, CompoundTag compound)
	{
		super.deserializeNBT(provider, compound);

		if (compound.contains(TAG_TABLE_NEEDED, Tag.TAG_STRING))
		{
			this.tableNeeded = CustomizedButcherable.getRegistry().get(ResourceLocation.parse(compound.getString(TAG_TABLE_NEEDED)));
		}

	}

	@Override
	public EntityAIWorkButcher generateAI()
	{
		return new EntityAIWorkButcher(this);
	}

	@Override
	public ResourceLocation getModel()
	{
		return ModModelTypes.COOK_ID;
	}

}
