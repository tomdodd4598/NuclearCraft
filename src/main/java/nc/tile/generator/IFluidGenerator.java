package nc.tile.generator;

import java.util.List;

import nc.recipe.ingredient.IFluidIngredient;
import nc.tile.internal.fluid.Tank;
import nc.tile.processor.IProcessor;

public interface IFluidGenerator extends IProcessor {
	
	public List<Tank> getFluidInputs(boolean consumed);
	
	public List<IFluidIngredient> getFluidIngredients();
	
	public List<IFluidIngredient> getFluidProducts();
	
	public List<Integer> getFluidInputOrder();

}
