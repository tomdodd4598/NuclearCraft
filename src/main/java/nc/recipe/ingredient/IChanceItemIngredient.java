package nc.recipe.ingredient;

public interface IChanceItemIngredient extends IItemIngredient {
	
	IItemIngredient getRawIngredient();
	
	int getChancePercent();
	
	int getMinStackSize();
	
	double getMeanStackSize();
}
