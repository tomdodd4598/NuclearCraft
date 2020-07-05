package nc.recipe;

public enum IngredientSorption {
	
	INPUT,
	OUTPUT,
	NEUTRAL;
	
	public boolean checkStackSize(int needed, int toCheck) {
		return this == IngredientSorption.OUTPUT ? toCheck == needed : this == IngredientSorption.INPUT ? toCheck >= needed : true;
	}
}
