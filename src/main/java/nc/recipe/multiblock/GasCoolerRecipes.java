package nc.recipe.multiblock;

import nc.init.NCBlocks;
import net.minecraft.item.ItemStack;

import static nc.config.NCConfig.fission_cooler_cooling_rate;

public class GasCoolerRecipes extends FissionCoolingRecipes {
	
	public GasCoolerRecipes() {
		super("gas_cooler", "_cooler");
	}
	
	public static final String[] COOLANTS = {
			"oxygen",
			"hydrogen",
			"helium",
			"nitrogen",
			"fluorine",
			"methane",
			"carbon_dioxide",
			"carbon_monoxide",
			"ethene",
			"ethyne",
			"fluoromethane",
			"ammonia",
			"diborane",
			"sulfur_dioxide",
			"sulfur_trioxide",
			"sulfur_hexafluoride"
	};
	
	@Override
	public void addRecipes() {
		for (int i = 0; i < 16; ++i) {
			addRecipe(new ItemStack(NCBlocks.pebble_fission_cooler, 1, i), fluidStack(COOLANTS[i], 1), fluidStack(COOLANTS[i] + "_hot", 1), fission_cooler_cooling_rate[i], COOLANTS[i] + "_cooler");
		}
	}
}
