package steve_gall.minecolonies_compatibility.module.common.scguns.crafting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.jetbrains.annotations.NotNull;

import com.minecolonies.api.colony.requestsystem.factory.IFactoryController;
import com.minecolonies.api.crafting.ItemStorage;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import steve_gall.minecolonies_compatibility.api.common.crafting.SimpleRecipeStorage;
import steve_gall.minecolonies_compatibility.core.common.MineColoniesCompatibility;
import steve_gall.minecolonies_compatibility.core.common.crafting.ItemStorageHelper;

public class GunBenchRecipeStorage extends SimpleRecipeStorage<GunBenchGenericRecipe>
{
	public static final ResourceLocation ID = MineColoniesCompatibility.rl("scguns_gun_bench");

	private final ItemStorage blueprint;

	public GunBenchRecipeStorage(HolderLookup.Provider provider, IFactoryController controller, CompoundTag tag)
	{
		super(provider, controller, tag);

		this.blueprint = controller.deserializeTag(provider, tag.getCompound("blueprint"));
	}

	public GunBenchRecipeStorage(ResourceLocation recipeId, List<ItemStorage> ingredients, ItemStorage blueprint, ItemStack output)
	{
		super(recipeId, ingredients, output);

		this.blueprint = blueprint;
	}

	@Override
	public ResourceLocation getId()
	{
		return ID;
	}

	@Override
	public List<ItemStorage> getInput()
	{
		var input = new ArrayList<>(super.getInput());

		if (!this.blueprint.isEmpty())
		{
			input.add(this.blueprint);
		}

		return input;
	}

	@Override
	public List<ItemStack> getSecondaryOutputs()
	{
		var outputs = new ArrayList<>(super.getSecondaryOutputs());

		if (!this.blueprint.isEmpty())
		{
			outputs.add(ItemStorageHelper.getAmountedStack(this.blueprint));
		}

		return outputs;
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(super.hashCode(), this.blueprint);
	}

	@Override
	public boolean equals(@NotNull Object o)
	{
		if (this == o)
		{
			return true;
		}
		else if (o instanceof GunBenchRecipeStorage other)
		{
			return super.equals(other) && this.blueprint.equals(other.blueprint);
		}

		return false;
	}

	@Override
	public void serialize(HolderLookup.Provider provider, IFactoryController controller, CompoundTag tag)
	{
		super.serialize(provider, controller, tag);

		tag.put("blueprint", controller.serializeTag(provider, this.blueprint));
	}

	@Override
	protected GenericRecipeFactory<GunBenchGenericRecipe> getGenericRecipeFactory()
	{
		return (recipeId, ingredients, output) -> new GunBenchGenericRecipe(recipeId, ingredients, Collections.singletonList(ItemStorageHelper.getAmountedStack(this.blueprint)), output);
	}

}
