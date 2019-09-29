package nc.recipe;

public class IngredientMatchResult {
	
	public static final IngredientMatchResult FAIL = new IngredientMatchResult(false, 0);
	public static final IngredientMatchResult PASS_0 = new IngredientMatchResult(true, 0);
	
	private final boolean match;
	
	private final int ingredientNumber;
	
	public IngredientMatchResult(boolean match, int ingredientNumber) {
		this.match = match;
		this.ingredientNumber = ingredientNumber;
	}
	
	public boolean matches() {
		return match;
	}
	
	public int getIngredientNumber() {
		return ingredientNumber;
	}
}
