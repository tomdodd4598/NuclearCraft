package nc.recipe.ingredient;

public interface IChanceFluidIngredient extends IFluidIngredient {
	
	IFluidIngredient getRawIngredient();
	
	int getChancePercent();
	
	int getStackDiff();
	
	int getMinStackSize();
	
	int getSizeIncrSteps();
	
	double getMeanStackSize();
}
