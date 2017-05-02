package nc.crafting.processor;

import nc.config.NCConfig;
import nc.handler.ProcessorRecipeHandler;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class SupercoolerRecipes extends ProcessorRecipeHandler {
	
	private static final SupercoolerRecipes RECIPES = new SupercoolerRecipes();

	public SupercoolerRecipes() {
		super(0, 1, 0, 1, false, true);
	}
	
	public static final ProcessorRecipeHandler instance() {
		return RECIPES;
	}
	
	public void addRecipes() {
		addRecipe(fluidStack("helium", 1000), fluidStack("liquidHelium", 1), NCConfig.processor_time[7]);
		addRecipe(new FluidStack(FluidRegistry.WATER, 1000), fluidStack("ice", 1000), NCConfig.processor_time[7]/4);
	}
}
