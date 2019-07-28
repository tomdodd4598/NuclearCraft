package nc.recipe.ingredient;

import java.util.List;

import nc.recipe.IngredientMatchResult;
import nc.recipe.IngredientSorption;

public interface IIngredient<T> {
	
	public T getStack();
	
	public T getNextStack(int ingredientNumber);
	
	public List<T> getInputStackList();
	
	public List<T> getInputStackHashingList();
	
	public List<T> getOutputStackList();
	
	public String getIngredientName();
	
	public String getIngredientNamesConcat();
	
	public default int getNextStackSize(int ingredientNumber) {
		return getMaxStackSize(ingredientNumber);
	}
	
	public int getMaxStackSize(int ingredientNumber);
	
	public void setMaxStackSize(int stackSize);
	
	public IngredientMatchResult match(Object object, IngredientSorption sorption);
	
	public boolean isValid();
}
