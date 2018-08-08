package nc.recipe.generator;

import nc.config.NCConfig;
import nc.recipe.ProcessorRecipeHandler;
import nc.util.Lang;
import nc.util.RegistryHelper;

public class FissionRecipes extends ProcessorRecipeHandler {
	
	public FissionRecipes() {
		super("fission", 1, 0, 1, 0);
	}
	
	@Override
	public void addRecipes() {
		addFuelRodDepleteRecipes("TBU", NCConfig.fission_thorium_fuel_time, NCConfig.fission_thorium_power, NCConfig.fission_thorium_heat_generation);
		addFuelRodDepleteRecipes("U", NCConfig.fission_uranium_fuel_time, NCConfig.fission_uranium_power, NCConfig.fission_uranium_heat_generation, 233, 235);
		addFuelRodDepleteRecipes("N", NCConfig.fission_neptunium_fuel_time, NCConfig.fission_neptunium_power, NCConfig.fission_neptunium_heat_generation, 236);
		addFuelRodDepleteRecipes("P", NCConfig.fission_plutonium_fuel_time, NCConfig.fission_plutonium_power, NCConfig.fission_plutonium_heat_generation, 239, 241);
		
		addRecipe("fuelMOX239", "depletedFuelMOX239", NCConfig.fission_mox_fuel_time[0], NCConfig.fission_mox_power[0], NCConfig.fission_mox_heat_generation[0], localiseFuel("mox_239"));
		addRecipe("fuelMOX241", "depletedFuelMOX241", NCConfig.fission_mox_fuel_time[1], NCConfig.fission_mox_power[1], NCConfig.fission_mox_heat_generation[1], localiseFuel("mox_241"));
		addRecipe("fuelRodMOX239", "depletedFuelRodMOX239", NCConfig.fission_mox_fuel_time[0], NCConfig.fission_mox_power[0], NCConfig.fission_mox_heat_generation[0], localiseFuel("mox_239"));
		addRecipe("fuelRodMOX241", "depletedFuelRodMOX241", NCConfig.fission_mox_fuel_time[1], NCConfig.fission_mox_power[1], NCConfig.fission_mox_heat_generation[1], localiseFuel("mox_241"));
		
		addFuelRodDepleteRecipes("A", NCConfig.fission_americium_fuel_time, NCConfig.fission_americium_power, NCConfig.fission_americium_heat_generation, 242);
		addFuelRodDepleteRecipes("Cm", NCConfig.fission_curium_fuel_time, NCConfig.fission_curium_power, NCConfig.fission_curium_heat_generation, 243, 245, 247);
		addFuelRodDepleteRecipes("B", NCConfig.fission_berkelium_fuel_time, NCConfig.fission_berkelium_power, NCConfig.fission_berkelium_heat_generation, 248);
		addFuelRodDepleteRecipes("Cf", NCConfig.fission_californium_fuel_time, NCConfig.fission_californium_power, NCConfig.fission_californium_heat_generation, 249, 251);
		
		addRecipe(RegistryHelper.itemStackFromRegistry("ic2", "nuclear", 1, 0), "depletedFuelIC2U", NCConfig.fission_uranium_fuel_time[4]*19D/54D, NCConfig.fission_uranium_power[4]*18D/19D, NCConfig.fission_uranium_heat_generation[4]*18D/19D, "IC2-LEU");
		addRecipe(RegistryHelper.itemStackFromRegistry("ic2", "nuclear", 1, 4), "depletedFuelIC2MOX", NCConfig.fission_mox_fuel_time[0]*7D/3D, NCConfig.fission_mox_power[0]*9D/7D, NCConfig.fission_mox_heat_generation[0]*9D/7D, "IC2-MOX");
		
		addRecipe("ingotYellorium", "ingotCyanite", NCConfig.fission_uranium_fuel_time[4]/8D, NCConfig.fission_uranium_power[4]*8D/9D, NCConfig.fission_uranium_heat_generation[4]*8D/9D, "YELLO");
		addRecipe("ingotBlutonium", "ingotCyanite", NCConfig.fission_uranium_fuel_time[4]/8D, NCConfig.fission_uranium_power[4]*8D/9D, NCConfig.fission_uranium_heat_generation[4]*8D/9D, "BLUTO");
	}
	
	public void addFuelRodDepleteRecipes(String fuel, double[] time, double[] power, double[] heat) {
		addRecipe("fuel" + fuel, "depletedFuel" + fuel, time[0], power[0], heat[0], localiseFuel(fuel.toLowerCase()));
		addRecipe("fuel" + fuel + "Oxide", "depletedFuel" + fuel + "Oxide", time[1], power[1], heat[1], localiseFuel(fuel.toLowerCase()) + "-" + localiseFuel("oxide"));
		addRecipe("fuelRod" + fuel, "depletedFuelRod" + fuel, time[0], power[0], heat[0], localiseFuel(fuel.toLowerCase()));
		addRecipe("fuelRod" + fuel + "Oxide", "depletedFuelRod" + fuel + "Oxide", time[1], power[1], heat[1], localiseFuel(fuel.toLowerCase()) + "-" + localiseFuel("oxide"));
	}
	
	public void addFuelRodDepleteRecipes(String fuel, double[] time, double[] power, double[] heat, int... types) {
		int count = 0;
		for (int type : types) {
			addFuelRodDepleteRecipes("LE" + fuel, type, count, time, power, heat);
			addFuelRodDepleteRecipes("HE" + fuel, type, count + 2, time, power, heat);
			count += 4;
		}
	}
	
	public void addFuelRodDepleteRecipes(String fuel, int type, int meta, double[] time, double[] power, double[] heat) {
		addRecipe("fuel" + fuel + type, "depletedFuel" + fuel + type, time[meta], power[meta], heat[meta], localiseFuel(fuel.toLowerCase()) + "-" + type);
		addRecipe("fuel" + fuel + type + "Oxide", "depletedFuel" + fuel + type + "Oxide", time[meta + 1], power[meta + 1], heat[meta + 1], localiseFuel(fuel.toLowerCase()) + "-" + type + "-" + localiseFuel("oxide"));
		addRecipe("fuelRod" + fuel + type, "depletedFuelRod" + fuel + type, time[meta], power[meta], heat[meta], localiseFuel(fuel.toLowerCase()) + "-" + type);
		addRecipe("fuelRod" + fuel + type + "Oxide", "depletedFuelRod" + fuel + type + "Oxide", time[meta + 1], power[meta + 1], heat[meta + 1], localiseFuel(fuel.toLowerCase()) + "-" + type + "-" + localiseFuel("oxide"));
	}
	
	private static String localiseFuel(String string) {
		return Lang.localise("gui.container.fission_controller." + string);
	}
}
