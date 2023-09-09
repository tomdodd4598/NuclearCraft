package nc.recipe.processor;

import java.util.List;

import javax.annotation.Nonnull;

import nc.recipe.*;
import nc.util.MinMax.MinMaxDouble;

public abstract class BasicProcessorRecipeHandler extends BasicRecipeHandler {
	
	public double maxProcessTimeMult = 1D;
	public double maxProcessPowerMult = 1D;
	
	public BasicProcessorRecipeHandler(@Nonnull String name, int itemInputSize, int fluidInputSize, int itemOutputSize, int fluidOutputSize) {
		super(name, itemInputSize, fluidInputSize, itemOutputSize, fluidOutputSize);
	}
	
	@Override
	protected void setStats() {
		MinMaxDouble mmProcessTimeMult = new MinMaxDouble(), mmProcessPowerMult = new MinMaxDouble();
		for (BasicRecipe recipe : recipeList) {
			mmProcessTimeMult.update(recipe.getProcessTimeMultiplier());
			mmProcessPowerMult.update(recipe.getProcessPowerMultiplier());
		}
		
		maxProcessTimeMult = mmProcessTimeMult.getMax();
		maxProcessPowerMult = mmProcessPowerMult.getMax();
	}
	
	@Override
	protected List<Object> fixedExtras(List<Object> extras) {
		ExtrasFixer fixer = new ExtrasFixer(extras);
		fixer.add(Double.class, 1D);
		fixer.add(Double.class, 1D);
		fixer.add(Double.class, 0D);
		return fixer.fixed;
	}
}
