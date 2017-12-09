package nc.recipe.generator;

import nc.config.NCConfig;
import nc.recipe.BaseRecipeHandler;
import net.minecraft.util.text.translation.I18n;

public class FissionRecipes extends BaseRecipeHandler {
	
private static final FissionRecipes RECIPES = new FissionRecipes();
	
	public FissionRecipes() {
		super(1, 0, 1, 0, false);
	}

	public static final FissionRecipes instance() {
		return RECIPES;
	}
	
	public void addRecipes() {
		fuelRodDeplete("TBU", NCConfig.fission_thorium_fuel_time, NCConfig.fission_thorium_power, NCConfig.fission_thorium_heat_generation);
		fuelRodDeplete("U", NCConfig.fission_uranium_fuel_time, NCConfig.fission_uranium_power, NCConfig.fission_uranium_heat_generation, 233, 235);
		fuelRodDeplete("N", NCConfig.fission_neptunium_fuel_time, NCConfig.fission_neptunium_power, NCConfig.fission_neptunium_heat_generation, 236);
		fuelRodDeplete("P", NCConfig.fission_plutonium_fuel_time, NCConfig.fission_plutonium_power, NCConfig.fission_plutonium_heat_generation, 239, 241);
		
		addRecipe("fuelRodMOX239", "depletedFuelRodMOX239", NCConfig.fission_mox_fuel_time[0], NCConfig.fission_mox_power[0], NCConfig.fission_mox_heat_generation[0], localise("mox_239"));
		addRecipe("fuelRodMOX241", "depletedFuelRodMOX241", NCConfig.fission_mox_fuel_time[1], NCConfig.fission_mox_power[1], NCConfig.fission_mox_heat_generation[1], localise("mox_241"));
		
		fuelRodDeplete("A", NCConfig.fission_americium_fuel_time, NCConfig.fission_americium_power, NCConfig.fission_americium_heat_generation, 242);
		fuelRodDeplete("Cm", NCConfig.fission_curium_fuel_time, NCConfig.fission_curium_power, NCConfig.fission_curium_heat_generation, 243, 245, 247);
		fuelRodDeplete("B", NCConfig.fission_berkelium_fuel_time, NCConfig.fission_berkelium_power, NCConfig.fission_berkelium_heat_generation, 248);
		fuelRodDeplete("Cf", NCConfig.fission_californium_fuel_time, NCConfig.fission_californium_power, NCConfig.fission_californium_heat_generation, 249, 251);
	}
	
	public void fuelRodDeplete(String fuel, double[] time, double[] power, double[] heat) {
		addRecipe("fuelRod" + fuel, "depletedFuelRod" + fuel, time[0], power[0], heat[0], localise(fuel.toLowerCase()));
		addRecipe("fuelRod" + fuel + "Oxide", "depletedFuelRod" + fuel + "Oxide", time[1], power[1], heat[1], localise(fuel.toLowerCase()) + "-" + localise("oxide"));
	}
	
	public void fuelRodDeplete(String fuel, double[] time, double[] power, double[] heat, int... types) {
		int count = 0;
		for (int type : types) {
			fuelRodDeplete("LE" + fuel, type, count, time, power, heat);
			fuelRodDeplete("HE" + fuel, type, count + 2, time, power, heat);
			count += 4;
		}
	}
	
	public void fuelRodDeplete(String fuel, int type, int meta, double[] time, double[] power, double[] heat) {
		addRecipe("fuelRod" + fuel + type, "depletedFuelRod" + fuel + type, time[meta], power[meta], heat[meta], localise(fuel.toLowerCase()) + "-" + type);
		addRecipe("fuelRod" + fuel + type + "Oxide", "depletedFuelRod" + fuel + type + "Oxide", time[meta + 1], power[meta + 1], heat[meta + 1], localise(fuel.toLowerCase()) + "-" + type + "-" + localise("oxide"));
	}
	
	private static String localise(String string) {
		return I18n.translateToLocalFormatted("gui.container.fission_controller." + string);
	}
	
	public String getRecipeName() {
		return "fission";
	}
}
