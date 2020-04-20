package nc.recipe.multiblock;

import java.util.ArrayList;
import java.util.List;

import nc.config.NCConfig;
import nc.recipe.ProcessorRecipeHandler;
import nc.util.FluidStackHelper;

public class SaltFissionRecipes extends ProcessorRecipeHandler {
	
	public SaltFissionRecipes() {
		super("salt_fission", 0, 1, 0, 1);
	}
	
	@Override
	public void addRecipes() {
		addFuelDepleteRecipes(NCConfig.fission_thorium_fuel_time, NCConfig.fission_thorium_heat_generation, NCConfig.fission_thorium_efficiency, NCConfig.fission_thorium_criticality, NCConfig.fission_thorium_self_priming, NCConfig.fission_thorium_radiation, "tbu");
		addFuelDepleteRecipes(NCConfig.fission_uranium_fuel_time, NCConfig.fission_uranium_heat_generation, NCConfig.fission_uranium_efficiency, NCConfig.fission_uranium_criticality, NCConfig.fission_uranium_self_priming, NCConfig.fission_uranium_radiation, "leu_233", "heu_233", "leu_235", "heu_235");
		addFuelDepleteRecipes(NCConfig.fission_neptunium_fuel_time, NCConfig.fission_neptunium_heat_generation, NCConfig.fission_neptunium_efficiency, NCConfig.fission_neptunium_criticality, NCConfig.fission_neptunium_self_priming, NCConfig.fission_neptunium_radiation, "len_236", "hen_236");
		addFuelDepleteRecipes(NCConfig.fission_plutonium_fuel_time, NCConfig.fission_plutonium_heat_generation, NCConfig.fission_plutonium_efficiency, NCConfig.fission_plutonium_criticality, NCConfig.fission_plutonium_self_priming, NCConfig.fission_plutonium_radiation, "lep_239", "hep_239", "lep_241", "hep_241");
		addFuelDepleteRecipes(NCConfig.fission_mixed_fuel_time, NCConfig.fission_mixed_heat_generation, NCConfig.fission_mixed_efficiency, NCConfig.fission_mixed_criticality, NCConfig.fission_mixed_self_priming, NCConfig.fission_mixed_radiation, "mix_239", "mix_241");
		addFuelDepleteRecipes(NCConfig.fission_americium_fuel_time, NCConfig.fission_americium_heat_generation, NCConfig.fission_americium_efficiency, NCConfig.fission_americium_criticality, NCConfig.fission_americium_self_priming, NCConfig.fission_americium_radiation, "lea_242", "hea_242");
		addFuelDepleteRecipes(NCConfig.fission_curium_fuel_time, NCConfig.fission_curium_heat_generation, NCConfig.fission_curium_efficiency, NCConfig.fission_curium_criticality, NCConfig.fission_curium_self_priming, NCConfig.fission_curium_radiation, "lecm_243", "hecm_243", "lecm_245", "hecm_245", "lecm_247", "hecm_247");
		addFuelDepleteRecipes(NCConfig.fission_berkelium_fuel_time, NCConfig.fission_berkelium_heat_generation, NCConfig.fission_berkelium_efficiency, NCConfig.fission_berkelium_criticality, NCConfig.fission_berkelium_self_priming, NCConfig.fission_berkelium_radiation, "leb_248", "heb_248");
		addFuelDepleteRecipes(NCConfig.fission_californium_fuel_time, NCConfig.fission_californium_heat_generation, NCConfig.fission_californium_efficiency, NCConfig.fission_californium_criticality, NCConfig.fission_californium_self_priming, NCConfig.fission_californium_radiation, "lecf_249", "hecf_249", "lecf_251", "hecf_251");
	}
	
	public void addFuelDepleteRecipe(String fuel, int time, int heat, double efficiency, int criticality, boolean selfPriming, double radiation) {
		addRecipe(fluidStack(fuel + "_fluoride_flibe", 1), fluidStack("depleted_" + fuel + "_fluoride_flibe", 1), (double)time/(double)FluidStackHelper.INGOT_VOLUME, heat, efficiency, criticality, selfPriming, radiation);
	}
	
	public void addFuelDepleteRecipes(int[] time, int[] heat, double[] efficiency, int[] criticality, boolean[] selfPriming, double[] radiation, String... fuelTypes) {
		int id = 0;
		for (String fuelType : fuelTypes) {
			addFuelDepleteRecipe(fuelType, time[id + 4], heat[id + 4], efficiency[id + 4], criticality[id + 4], selfPriming[id + 4], radiation[id + 4]);
			id += 5;
		}
	}
	
	@Override
	public List fixExtras(List extras) {
		List fixed = new ArrayList(6);
		fixed.add(extras.size() > 0 && extras.get(0) instanceof Double ? (double) extras.get(0) : 1D);
		fixed.add(extras.size() > 1 && extras.get(1) instanceof Integer ? (int) extras.get(1) : 0);
		fixed.add(extras.size() > 2 && extras.get(2) instanceof Double ? (double) extras.get(2) : 0D);
		fixed.add(extras.size() > 3 && extras.get(3) instanceof Integer ? (int) extras.get(3) : 1);
		fixed.add(extras.size() > 4 && extras.get(4) instanceof Boolean ? (boolean) extras.get(4) : false);
		fixed.add(extras.size() > 5 && extras.get(5) instanceof Double ? (double) extras.get(5) : 0D);
		return fixed;
	}
	
	@Override
	public List getFactoredExtras(List extras, int factor) {
		List factored = new ArrayList(extras);
		factored.set(0, (int)extras.get(0)/factor);
		return factored;
	}
}
