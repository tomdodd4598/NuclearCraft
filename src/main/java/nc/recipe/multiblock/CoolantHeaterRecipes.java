package nc.recipe.multiblock;

import static nc.init.NCCoolantFluids.COOLANTS;

import java.util.ArrayList;
import java.util.List;

import nc.config.NCConfig;
import nc.recipe.ProcessorRecipeHandler;

public class CoolantHeaterRecipes extends ProcessorRecipeHandler {
	
	public CoolantHeaterRecipes() {
		super("coolant_heater", 0, 1, 0, 1);
	}
	
	@Override
	public void addRecipes() {
		for (int i = 0; i < COOLANTS.size(); i++) {
			addRecipe(fluidStack(COOLANTS.get(i) + "nak", 1), fluidStack(COOLANTS.get(i) + "nak_hot", 1), NCConfig.fission_heater_cooling_rate[i], "jei.nuclearcraft.coolant_heater." + COOLANTS.get(i) + "nak");
		}
	}
	
	@Override
	public List fixExtras(List extras) {
		List fixed = new ArrayList(2);
		fixed.add(extras.size() > 0 && extras.get(0) instanceof Integer ? (int) extras.get(0) : 40);
		fixed.add(extras.size() > 1 && extras.get(1) instanceof String ? (String) extras.get(1) : 300);
		return fixed;
	}
}
