package nc.recipe.multiblock;

import nc.config.NCConfig;
import nc.recipe.ProcessorRecipeHandler;
import nc.recipe.ingredient.IFluidIngredient;

public class HeatExchangerRecipes extends ProcessorRecipeHandler {
	
	public HeatExchangerRecipes() {
		super("heat_exchanger", 0, 1, 0, 1);
	}
	
	@Override
	public void addRecipes() {
		// boolean: is cold -> hot.
		
		// Hot NaK -> NaK
		
		for (int i = 0; i < COOLANTS.length; i++) {
			addHeatExchange(fluidStack(COOLANTS[i] + "nak_hot", NCConfig.salt_fission_cooling_max_rate), fluidStack(COOLANTS[i] + "nak", NCConfig.salt_fission_cooling_max_rate), NCConfig.salt_fission_cooling_rate[i]*NCConfig.heat_exchanger_coolant_mult); // 700 -> 300
		}
		
		// Steam <-> Water
		
		addHeatExchange(fluidStack("water", 200), fluidStack("high_pressure_steam", 1000), 8D*4000D); // 300 -> 1200
		addHeatExchange(fluidStack("preheated_water", 200), fluidStack("high_pressure_steam", 1000), 4D*4000D); // 400 -> 1200
		
		addHeatExchange(fluidStack("exhaust_steam", 1000), fluidStack("low_pressure_steam", 1000), 4D*4000D); // 400 -> 1000
		
		//addHeatExchange(fluidStack("high_pressure_steam", 250), fluidStack("steam", 1000), 1D*4000D); // 1200 -> 800
		addRecipe(fluidStack("high_pressure_steam", 250), fluidStack("steam", 1000), 1D*4000D, 1200, 800);
		
		addHeatExchange(fluidStack("condensate_water", 1000), fluidStack("preheated_water", 1000), 8D*4000D); // 300 -> 400
	}
	
	public void addHeatExchange(IFluidIngredient fluidIn, IFluidIngredient fluidOut, double heatRequired) {
		addRecipe(fluidIn, fluidOut, heatRequired, fluidIn.getStack().getFluid().getTemperature(), fluidOut.getStack().getFluid().getTemperature());
	}
	
	private static final String[] COOLANTS = new String[] {"", "redstone_", "quartz_", "gold_", "glowstone_", "lapis_", "diamond_", "liquidhelium_", "ender_", "cryotheum_", "iron_", "emerald_", "copper_", "tin_", "magnesium_"};
}
