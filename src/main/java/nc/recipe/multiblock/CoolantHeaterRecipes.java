package nc.recipe.multiblock;

import static nc.config.NCConfig.fission_heater_cooling_rate;
import static nc.init.NCCoolantFluids.COOLANTS;

import java.util.*;

import nc.recipe.BasicRecipeHandler;

public class CoolantHeaterRecipes extends BasicRecipeHandler {
	
	public CoolantHeaterRecipes() {
		super("coolant_heater", 0, 1, 0, 1);
	}
	
	@Override
	public void addRecipes() {
		for (int i = 0; i < COOLANTS.size(); i++) {
			String ruleName = (COOLANTS.get(i).equals("") ? "standard_" : COOLANTS.get(i)) + "heater";
			addRecipe(fluidStack(COOLANTS.get(i) + "nak", 1), fluidStack(COOLANTS.get(i) + "nak_hot", 1), fission_heater_cooling_rate[i], ruleName);
		}
	}
	
	@Override
	public List fixExtras(List extras) {
		List fixed = new ArrayList(2);
		fixed.add(extras.size() > 0 && extras.get(0) instanceof Integer ? (int) extras.get(0) : 0);
		fixed.add(extras.size() > 1 && extras.get(1) instanceof String ? (String) extras.get(1) : "");
		return fixed;
	}
}
