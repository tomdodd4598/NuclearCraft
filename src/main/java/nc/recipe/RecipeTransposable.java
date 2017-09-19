package nc.recipe;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class RecipeTransposable implements IIngredient, IRecipeStack {
	
	public ArrayList<IIngredient> validInputList;
	public ArrayList<Object> cachedObjects = new ArrayList();

	public RecipeTransposable(ArrayList<IIngredient> validInputs) {
		validInputList = validInputs;
		validInputs.forEach(input -> cachedObjects.add(input.getIngredient()));
	}

	public Object getIngredient() {
		return cachedObjects;
	}

	public int getStackSize() {
		return validInputList.get(0).getStackSize();
	}

	public List<Object> getIngredientList() {
		ArrayList<Object> values = new ArrayList<Object>();
		validInputList.forEach(object -> object.getIngredientList().forEach(obj -> values.add(obj)));
		return values;
	}

	public boolean matches(Object object, SorptionType sorption) {
		for (IIngredient ingredient : validInputList) {
			if (ingredient.matches(object, sorption)) {
				return true;
			}
		}
		return false;
	}

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
