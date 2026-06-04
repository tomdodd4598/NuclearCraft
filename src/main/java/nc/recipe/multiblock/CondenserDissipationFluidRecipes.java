package nc.recipe.multiblock;

import nc.recipe.BasicRecipeHandler;
import net.minecraftforge.fluids.FluidRegistry;

import java.util.*;

public class CondenserDissipationFluidRecipes extends BasicRecipeHandler {
	
	public CondenserDissipationFluidRecipes() {
		super("condenser_dissipation_fluid", 0, 1, 0, 0);
	}
	
	@Override
	public void addRecipes() {
		FluidRegistry.getRegisteredFluids().entrySet().stream().filter(x -> x.getValue().getTemperature() <= 300).sorted(Comparator.comparing(x -> x.getValue().getTemperature())).forEach(x -> addRecipe(x.getKey(), x.getValue().getTemperature()));
	}
	
	@Override
	protected List<Object> fixedExtras(List<Object> extras) {
		ExtrasFixer fixer = new ExtrasFixer(extras);
		fixer.add(Integer.class, 300);
		return fixer.fixed;
	}
}
