package nc.recipe;

import java.util.List;

public interface IIngredient {
	
	public Object getIngredient();
	
	public String getIngredientName();
	
	public StackType getIngredientType();
	
	public int getStackSize();
	
	public List<Object> getIngredientList();
	
	public boolean matches(Object object, SorptionType sorption);
}
