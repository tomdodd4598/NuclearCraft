package nc.recipe.ingredient;

import java.util.List;

import it.unimi.dsi.fastutil.ints.IntList;
import nc.recipe.*;
import net.minecraftforge.fml.common.Optional;

public interface IIngredient<T> {
	
	default void init() {
		
	}
	
	T getStack();
	
	T getNextStack(int ingredientNumber);
	
	List<T> getInputStackList();
	
	List<T> getInputStackHashingList();
	
	List<T> getOutputStackList();
	
	int getMaxStackSize(int ingredientNumber);
	
	void setMaxStackSize(int stackSize);
	
	default int getNextStackSize(int ingredientNumber) {
		return getMaxStackSize(ingredientNumber);
	}
	
	String getIngredientName();
	
	String getIngredientNamesConcat();
	
	IntList getFactors();
	
	IIngredient<T> getFactoredIngredient(int factor);
	
	IngredientMatchResult match(Object object, IngredientSorption sorption);
	
	boolean isValid();
	
	// CraftTweaker
	
	@Optional.Method(modid = "crafttweaker")
    crafttweaker.api.item.IIngredient ct();
}
