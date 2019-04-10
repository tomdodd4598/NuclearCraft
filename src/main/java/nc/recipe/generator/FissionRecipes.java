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
		addFuelRodDepleteRecipes("TBU", NCConfig.fission_thorium_fuel_time, NCConfig.fission_thorium_power, NCConfig.fission_thorium_heat_generation, NCConfig.fission_thorium_radiation);
		addFuelRodDepleteRecipes("U", NCConfig.fission_uranium_fuel_time, NCConfig.fission_uranium_power, NCConfig.fission_uranium_heat_generation, NCConfig.fission_uranium_radiation, 233, 235);
		addFuelRodDepleteRecipes("N", NCConfig.fission_neptunium_fuel_time, NCConfig.fission_neptunium_power, NCConfig.fission_neptunium_heat_generation, NCConfig.fission_neptunium_radiation, 236);
		addFuelRodDepleteRecipes("P", NCConfig.fission_plutonium_fuel_time, NCConfig.fission_plutonium_power, NCConfig.fission_plutonium_heat_generation, NCConfig.fission_plutonium_radiation, 239, 241);
		
		addRecipe("fuelMOX239", "depletedFuelMOX239", NCConfig.fission_mox_fuel_time[0], NCConfig.fission_mox_power[0], NCConfig.fission_mox_heat_generation[0], localiseFuel("mox_239"), NCConfig.fission_mox_radiation[0]);
		addRecipe("fuelMOX241", "depletedFuelMOX241", NCConfig.fission_mox_fuel_time[1], NCConfig.fission_mox_power[1], NCConfig.fission_mox_heat_generation[1], localiseFuel("mox_241"), NCConfig.fission_mox_radiation[1]);
		addRecipe("fuelRodMOX239", "depletedFuelRodMOX239", NCConfig.fission_mox_fuel_time[0], NCConfig.fission_mox_power[0], NCConfig.fission_mox_heat_generation[0], localiseFuel("mox_239"), NCConfig.fission_mox_radiation[0]);
		addRecipe("fuelRodMOX241", "depletedFuelRodMOX241", NCConfig.fission_mox_fuel_time[1], NCConfig.fission_mox_power[1], NCConfig.fission_mox_heat_generation[1], localiseFuel("mox_241"), NCConfig.fission_mox_radiation[1]);
		
		addFuelRodDepleteRecipes("A", NCConfig.fission_americium_fuel_time, NCConfig.fission_americium_power, NCConfig.fission_americium_heat_generation, NCConfig.fission_americium_radiation, 242);
		addFuelRodDepleteRecipes("Cm", NCConfig.fission_curium_fuel_time, NCConfig.fission_curium_power, NCConfig.fission_curium_heat_generation, NCConfig.fission_curium_radiation, 243, 245, 247);
		addFuelRodDepleteRecipes("B", NCConfig.fission_berkelium_fuel_time, NCConfig.fission_berkelium_power, NCConfig.fission_berkelium_heat_generation, NCConfig.fission_berkelium_radiation, 248);
		addFuelRodDepleteRecipes("Cf", NCConfig.fission_californium_fuel_time, NCConfig.fission_californium_power, NCConfig.fission_californium_heat_generation, NCConfig.fission_californium_radiation, 249, 251);
		
		addRecipe(RegistryHelper.itemStackFromRegistry("ic2:nuclear:0", 1), "depletedFuelIC2U", NCConfig.fission_uranium_fuel_time[4]*19D/54D, NCConfig.fission_uranium_power[4]*18D/19D, NCConfig.fission_uranium_heat_generation[4]*18D/19D, "IC2-LEU", NCConfig.fission_uranium_radiation[4]*18D/19D);
		addRecipe(RegistryHelper.itemStackFromRegistry("ic2:nuclear:4", 1), "depletedFuelIC2MOX", NCConfig.fission_mox_fuel_time[0]*7D/3D, NCConfig.fission_mox_power[0]*9D/7D, NCConfig.fission_mox_heat_generation[0]*9D/7D, "IC2-MOX", NCConfig.fission_mox_radiation[0]*9D/7D);
		
		addRecipe("ingotYellorium", "ingotCyanite", NCConfig.fission_uranium_fuel_time[4]/8D, NCConfig.fission_uranium_power[4]*8D/9D, NCConfig.fission_uranium_heat_generation[4]*8D/9D, "YELLO", NCConfig.fission_uranium_radiation[4]*8D/9D);
		addRecipe("ingotBlutonium", "ingotCyanite", NCConfig.fission_uranium_fuel_time[4]/8D, NCConfig.fission_uranium_power[4]*8D/9D, NCConfig.fission_uranium_heat_generation[4]*8D/9D, "BLUTO", NCConfig.fission_uranium_radiation[4]*8D/9D);
	}
	
	public void addFuelRodDepleteRecipes(String fuel, double[] time, double[] power, double[] heat, double[] radiation) {
		addRecipe("fuel" + fuel, "depletedFuel" + fuel, time[0], power[0], heat[0], localiseFuel(fuel.toLowerCase()), radiation[0]);
		addRecipe("fuel" + fuel + "Oxide", "depletedFuel" + fuel + "Oxide", time[1], power[1], heat[1], localiseFuel(fuel.toLowerCase()) + "-" + localiseFuel("oxide"), radiation[1]);
		addRecipe("fuelRod" + fuel, "depletedFuelRod" + fuel, time[0], power[0], heat[0], localiseFuel(fuel.toLowerCase()), radiation[0]);
		addRecipe("fuelRod" + fuel + "Oxide", "depletedFuelRod" + fuel + "Oxide", time[1], power[1], heat[1], localiseFuel(fuel.toLowerCase()) + "-" + localiseFuel("oxide"), radiation[1]);
	}
	
	public void addFuelRodDepleteRecipes(String fuel, double[] time, double[] power, double[] heat, double[] radiation, int... types) {
		int count = 0;
		for (int type : types) {
			addFuelRodDepleteRecipes("LE" + fuel, type, count, time, power, heat, radiation);
			addFuelRodDepleteRecipes("HE" + fuel, type, count + 2, time, power, heat, radiation);
			count += 4;
		}
	}
	
	public void addFuelRodDepleteRecipes(String fuel, int type, int meta, double[] time, double[] power, double[] heat, double[] radiation) {
		addRecipe("fuel" + fuel + type, "depletedFuel" + fuel + type, time[meta], power[meta], heat[meta], localiseFuel(fuel.toLowerCase()) + "-" + type, radiation[meta]);
		addRecipe("fuel" + fuel + type + "Oxide", "depletedFuel" + fuel + type + "Oxide", time[meta + 1], power[meta + 1], heat[meta + 1], localiseFuel(fuel.toLowerCase()) + "-" + type + "-" + localiseFuel("oxide"), radiation[meta + 1]);
		addRecipe("fuelRod" + fuel + type, "depletedFuelRod" + fuel + type, time[meta], power[meta], heat[meta], localiseFuel(fuel.toLowerCase()) + "-" + type, radiation[meta]);
		addRecipe("fuelRod" + fuel + type + "Oxide", "depletedFuelRod" + fuel + type + "Oxide", time[meta + 1], power[meta + 1], heat[meta + 1], localiseFuel(fuel.toLowerCase()) + "-" + type + "-" + localiseFuel("oxide"), radiation[meta + 1]);
	}
	
	private static String localiseFuel(String string) {
		return Lang.localise("gui.container.fission_controller." + string);
	}
}
