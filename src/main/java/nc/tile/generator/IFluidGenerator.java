package nc.tile.generator;

import java.util.List;

import nc.recipe.ingredient.IFluidIngredient;
import nc.tile.fluid.ITileFluid;
import nc.tile.internal.fluid.Tank;

public interface IFluidGenerator extends IGenerator, ITileFluid {
	
	public int getFluidInputSize();
	
	public int getFluidOutputputSize();
	
	public List<Tank> getFluidInputs(boolean consumed);
	
	public List<IFluidIngredient> getFluidIngredients();
	
	public List<IFluidIngredient> getFluidProducts();

}
