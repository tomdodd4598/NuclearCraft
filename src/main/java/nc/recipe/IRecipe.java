package nc.recipe;

import java.util.ArrayList;

public interface IRecipe {
	
	public ArrayList<IIngredient> inputs();

	public ArrayList<IIngredient> outputs();
	
	public ArrayList extras();

	public boolean matchingInputs(Object[] inputs);
	
	public boolean matchingOutputs(Object[] outputs);
}
