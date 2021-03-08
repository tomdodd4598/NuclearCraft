package nc.recipe.ingredient;

public interface IChanceItemIngredient extends IItemIngredient {
	
	public IItemIngredient getRawIngredient();
	
	public int getChancePercent();
	
	public int getMinStackSize();
	
	public double getMeanStackSize();
}
