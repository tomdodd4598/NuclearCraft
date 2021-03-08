package nc.recipe.ingredient;

public interface IChanceFluidIngredient extends IFluidIngredient {
	
	public IFluidIngredient getRawIngredient();
	
	public int getChancePercent();
	
	public int getStackDiff();
	
	public int getMinStackSize();
	
	public int getSizeIncrSteps();
	
	public double getMeanStackSize();
}
