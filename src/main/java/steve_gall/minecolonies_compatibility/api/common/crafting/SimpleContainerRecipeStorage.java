package steve_gall.minecolonies_compatibility.api.common.crafting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.jetbrains.annotations.NotNull;

import com.minecolonies.api.colony.requestsystem.factory.IFactoryController;
import com.minecolonies.api.crafting.ItemStorage;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public abstract class SimpleContainerRecipeStorage<GENERIC_RECIPE extends SimpleContainerGenericRecipe> extends SimpleRecipeStorage<GENERIC_RECIPE>
{
	private final ItemStorage container;

	public SimpleContainerRecipeStorage(HolderLookup.Provider provider, IFactoryController controller, CompoundTag tag)
	{
		super(provider, controller, tag);

		this.container = controller.deserializeTag(provider, tag.getCompound("container"));
	}

	public SimpleContainerRecipeStorage(ResourceLocation recipeId, List<ItemStorage> ingredients, ItemStorage container, ItemStack output)
	{
		super(recipeId, ingredients, output);

		this.container = container;
	}

	@Override
	public List<ItemStorage> getInput()
	{
		var input = new ArrayList<>(super.getInput());

		if (!this.container.isEmpty())
		{
			input.add(this.container);
		}

		return input;
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(super.hashCode(), this.container);
	}

	@Override
	public boolean equals(@NotNull Object o)
	{
		if (this == o)
		{
			return true;
		}
		else if (o instanceof SimpleContainerRecipeStorage other)
		{
			return super.equals(other) && this.container.equals(other.container);
		}

		return false;
	}

	@Override
	public void serialize(HolderLookup.Provider provider, IFactoryController controller, CompoundTag tag)
	{
		super.serialize(provider, controller, tag);

		tag.put("container", controller.serializeTag(provider, this.container));
	}

	@Override
	protected GenericRecipeFactory<GENERIC_RECIPE> getGenericRecipeFactory()
	{
		return (recipeId, ingredients, output) -> this.getContainerGenericRecipeFactory().create(recipeId, ingredients, Arrays.asList(this.container.getItemStack()), output);
	}

	protected abstract ContainerGenericRecipeFactory<GENERIC_RECIPE> getContainerGenericRecipeFactory();

	public interface ContainerGenericRecipeFactory<GENERIC_RECIPE>
	{
		GENERIC_RECIPE create(ResourceLocation recipeId, List<List<ItemStack>> ingredients, List<ItemStack> container, ItemStack output);
	}

}
