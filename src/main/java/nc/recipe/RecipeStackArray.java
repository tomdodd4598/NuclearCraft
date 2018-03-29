package nc.recipe;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class RecipeStackArray implements IIngredient, IRecipeStack {
	
	public ArrayList<IIngredient> validInputList;
	public ArrayList<Object> cachedObjects = new ArrayList();
	
	public RecipeStackArray(IIngredient... validInputs) {
		this(Lists.newArrayList(validInputs));
	}

	public RecipeStackArray(ArrayList<IIngredient> validInputs) {
		validInputList = validInputs;
		validInputs.forEach(input -> cachedObjects.add(input.getIngredient()));
	}

	@Override
	public Object getIngredient() {
		return cachedObjects;
	}
	
	@Override
	public String getIngredientName() {
		return validInputList.get(0).getIngredientName();
	}

	@Override
	public StackType getIngredientType() {
		return validInputList.get(0).getIngredientType();
	}

	@Override
	public int getStackSize() {
		return validInputList.get(0).getStackSize();
	}

	@Override
	public List<Object> getIngredientList() {
		ArrayList<Object> values = new ArrayList<Object>();
		validInputList.forEach(object -> object.getIngredientList().forEach(obj -> values.add(obj)));
		return values;
	}

	@Override
	public boolean matches(Object object, SorptionType sorption) {
		for (IIngredient ingredient : validInputList) {
			if (ingredient.matches(object, sorption)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Object getOutputStack() {
		IIngredient obj = validInputList.get(0);
		if (obj instanceof IRecipeStack) {
			return ((IRecipeStack) obj).getOutputStack();
		}
		for (Object object : cachedObjects) {
			if (object instanceof ItemStack) {
				return (ItemStack) object;
			}
			if (object instanceof FluidStack) {
				return (FluidStack) object;
			}
		}
		return null;
	}
}
