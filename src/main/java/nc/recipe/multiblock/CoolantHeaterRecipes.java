package nc.recipe.multiblock;

import nc.init.NCBlocks;
import net.minecraft.item.ItemStack;

import static nc.config.NCConfig.fission_heater_cooling_rate;
import static nc.init.NCCoolantFluids.COOLANTS;

public class CoolantHeaterRecipes extends FissionCoolingRecipes {
	
	public CoolantHeaterRecipes() {
		super("coolant_heater", "_heater");
	}
	
	@Override
	public void addRecipes() {
		addRecipe(new ItemStack(NCBlocks.salt_fission_heater, 1, 0), fluidStack("nak", 1), fluidStack("nak_hot", 1), fission_heater_cooling_rate[0], "standard_heater");
		for (int i = 1; i < COOLANTS.size(); ++i) {
			ItemStack heater = new ItemStack(i < 16 ? NCBlocks.salt_fission_heater : NCBlocks.salt_fission_heater2, 1, i % 16);
			String ruleName = COOLANTS.get(i) + "_heater";
			addRecipe(heater, fluidStack(COOLANTS.get(i) + "_nak", 1), fluidStack(COOLANTS.get(i) + "_nak_hot", 1), fission_heater_cooling_rate[i], ruleName);
		}
	}
}
