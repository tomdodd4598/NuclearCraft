package nc.crafting.processor;

import nc.handler.ProcessorRecipeHandler;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class SupercoolerRecipes extends ProcessorRecipeHandler {
	
	private static final SupercoolerRecipes RECIPES = new SupercoolerRecipes();

	public SupercoolerRecipes() {
		super(0, 1, 0, 1, false);
	}
	
	public static final ProcessorRecipeHandler instance() {
		return RECIPES;
	}
	
	public void addRecipes() {
		addRecipe(fluidStack("helium", 1000), fluidStack("liquidHelium", 1));
		addRecipe(new FluidStack(FluidRegistry.WATER, 1000), fluidStack("ice", 1000));
	}
}
