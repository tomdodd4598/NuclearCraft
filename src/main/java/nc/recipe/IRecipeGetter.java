package nc.recipe;

import java.util.ArrayList;

public interface IRecipeGetter<T extends IRecipe> {
	
	public ArrayList<T> getRecipes();
	
	public String getRecipeName();
}
