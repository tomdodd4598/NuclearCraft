package nc.recipe;

import java.util.List;

import nc.config.NCConfig;
import nc.recipe.ingredient.IFluidIngredient;
import nc.recipe.ingredient.IItemIngredient;
import nc.tile.generator.TileDecayGenerator;
import nc.tile.internal.fluid.Tank;
import nc.util.InfoHelper;
import nc.util.Lang;
import net.minecraft.item.ItemStack;

public class ProcessorRecipe implements IRecipe {
	
	protected List<IItemIngredient> itemIngredients, itemProducts;
	protected List<IFluidIngredient> fluidIngredients, fluidProducts;
	
	private int itemInputSize, fluidInputSize, itemOutputSize, fluidOutputSize;
	
	protected List extras;
	public boolean isShapeless;
	
	public ProcessorRecipe(List<IItemIngredient> itemIngredientsList, List<IFluidIngredient> fluidIngredientsList, List<IItemIngredient> itemProductsList, List<IFluidIngredient> fluidProductsList, List extrasList, boolean shapeless) {
		itemIngredients = itemIngredientsList;
		fluidIngredients = fluidIngredientsList;
		itemProducts = itemProductsList;
		fluidProducts = fluidProductsList;
		
		itemInputSize = itemIngredientsList.size();
		fluidInputSize = fluidIngredientsList.size();
		itemOutputSize = itemProductsList.size();
		fluidOutputSize = fluidProductsList.size();
		
		extras = extrasList;
		isShapeless = shapeless;
	}
	
	@Override
	public List<IItemIngredient> itemIngredients() {
		return itemIngredients;
	}
	
	@Override
	public List<IFluidIngredient> fluidIngredients() {
		return fluidIngredients;
	}
	
	@Override
	public List<IItemIngredient> itemProducts() {
		return itemProducts;
	}
	
	@Override
	public List<IFluidIngredient> fluidProducts() {
		return fluidProducts;
	}
	
	@Override
	public List extras() {
		return extras;
	}

	@Override
	public RecipeMatchResult matchInputs(List<ItemStack> itemInputs, List<Tank> fluidInputs) {
		return RecipeHelper.matchIngredients(IngredientSorption.INPUT, itemIngredients, fluidIngredients, itemInputs, fluidInputs, isShapeless);
	}

	@Override
	public RecipeMatchResult matchOutputs(List<ItemStack> itemOutputs, List<Tank> fluidOutputs) {
		return RecipeHelper.matchIngredients(IngredientSorption.OUTPUT, itemProducts, fluidProducts, itemOutputs, fluidOutputs, isShapeless);
	}
	
	@Override
	public RecipeMatchResult matchIngredients(List<IItemIngredient> itemIngredients, List<IFluidIngredient> fluidIngredients) {
		return RecipeHelper.matchIngredients(IngredientSorption.INPUT, this.itemIngredients, this.fluidIngredients, itemIngredients, fluidIngredients, isShapeless);
	}

	@Override
	public RecipeMatchResult matchProducts(List<IItemIngredient> itemProducts, List<IFluidIngredient> fluidProducts) {
		return RecipeHelper.matchIngredients(IngredientSorption.OUTPUT, this.itemProducts, this.fluidProducts, itemProducts, fluidProducts, isShapeless);
	}
	
	/* ================================== Recipe Extras ===================================== */
	
	// Processors
	
	public double getBaseProcessTime(double defaultProcessTime) {
		if (extras.isEmpty()) return defaultProcessTime;
		else if (extras.get(0) instanceof Double) return ((double) extras.get(0))*defaultProcessTime;
		else return defaultProcessTime;
	}
	
	public double getBaseProcessPower(double defaultProcessPower) {
		if (extras.size() < 2) return defaultProcessPower;
		else if (extras.get(1) instanceof Double) return ((double) extras.get(1))*defaultProcessPower;
		else return defaultProcessPower;
	}
	
	public double getBaseProcessRadiation() {
		if (extras.size() < 3) return 0D;
		else if (extras.get(2) instanceof Double) return (double) extras.get(2);
		else return 0D;
	}
	
	// Active Cooler
	
	public long getActiveFissionCoolingRate() {
		if (extras.isEmpty()) return 0;
		else if (extras.get(0) instanceof Long) return (long) extras.get(0);
		else return 0;
	}
	
	public long getActiveFusionCoolingRate() {
		if (extras.size() < 2) return 0;
		else if (extras.get(1) instanceof Long) return (long) extras.get(1);
		else return 0;
	}
	
	// Decay Generator
	
	public double getDecayLifetime() {
		if (extras.isEmpty()) return TileDecayGenerator.DEFAULT_LIFETIME;
		else if (extras.get(0) instanceof Double) return ((double) extras.get(0))*20D/NCConfig.machine_update_rate;
		return TileDecayGenerator.DEFAULT_LIFETIME;
	}
	
	public int getDecayPower() {
		if (extras.size() < 2) return 0;
		if (extras.get(1) instanceof Double) return (int) (((double) extras.get(1))*NCConfig.machine_update_rate/20D);
		return 0;
	}
	
	public double getDecayRadiation() {
		if (extras.size() < 3) return 0D;
		else if (extras.get(2) instanceof Double) return ((double) extras.get(2))*NCConfig.machine_update_rate/20D;
		else return 0D;
	}
	
	// Fission
	
	public double getFissionFuelTime() {
		if (extras.isEmpty()) return 1D;
		else if (extras.get(0) instanceof Double) return (double) extras.get(0);
		else return 1D;
	}
	
	public double getFissionFuelPower() {
		if (extras.size() < 2) return 0D;
		else if (extras.get(1) instanceof Double) return (double) extras.get(1);
		else return 0D;
	}
	
	public double getFissionFuelHeat() {
		if (extras.size() < 3) return 0D;
		else if (extras.get(2) instanceof Double) return (double) extras.get(2);
		else return 0D;
	}
	
	public double getFissionFuelRadiation() {
		if (extras.size() < 5) return 0D;
		else if (extras.get(4) instanceof Double) return (double) extras.get(4);
		else return 0D;
	}
	
	// Fusion
	
	public double getFusionComboTime() {
		if (extras.isEmpty()) return 1D;
		else if (extras.get(0) instanceof Double) return (double) extras.get(0);
		else return 1D;
	}
	
	public double getFusionComboPower() {
		if (extras.size() < 2) return 0D;
		else if (extras.get(1) instanceof Double) return (double) extras.get(1);
		else return 0D;
	}
	
	public double getFusionComboHeatVariable() {
		if (extras.size() < 3) return 1000D;
		else if (extras.get(2) instanceof Double) return (double) extras.get(2);
		else return 1000D;
	}
	
	public double getFusionComboRadiation() {
		if (extras.size() < 4) return 0D;
		else if (extras.get(3) instanceof Double) return (double) extras.get(3);
		else return 0D;
	}
	
	// Salt Fission Vessel
	
	public double getSaltFissionFuelTime() {
		if (extras.isEmpty()) return 1D;
		else if (extras.get(0) instanceof Double) return (double) extras.get(0);
		else return 1D;
	}
	
	public double getSaltFissionFuelHeat() {
		if (extras.size() < 2) return 0D;
		else if (extras.get(1) instanceof Double) return (double) extras.get(1);
		else return 0D;
	}
	
	public double getSaltFissionFuelRadiation() {
		if (extras.size() < 3) return 0D;
		else if (extras.get(2) instanceof Double) return (double) extras.get(2);
		else return 0D;
	}
	
	// Coolant Heater
	
	public double getCoolantHeaterCoolingRate() {
		if (extras.isEmpty()) return 10D;
		else if (extras.get(0) instanceof Double) return (double) extras.get(0);
		else return 0D;
	}
	
	public String[] getCoolantHeaterJEIInfo() {
		if (extras.size() < 2) return null;
		else if (extras.get(1) instanceof String) return InfoHelper.formattedInfo(Lang.localise((String) extras.get(1)));
		else return null;
	}
	
	// Heat Exchanger
	
	public double getHeatExchangerProcessTime(double defaultProcessTime) {
		if (extras.isEmpty()) return defaultProcessTime;
		else if (extras.get(0) instanceof Double) return (double) extras.get(0);
		else return defaultProcessTime;
	}
	
	public int getHeatExchangerInputTemperature() {
		if (extras.size() < 2) return 0;
		else if (extras.get(1) instanceof Integer) return (int) extras.get(1);
		else return 0;
	}
	
	public int getHeatExchangerOutputTemperature() {
		if (extras.size() < 3) return 0;
		else if (extras.get(2) instanceof Integer) return (int) extras.get(2);
		else return 0;
	}
	
	public boolean getHeatExchangerIsHeating() {
		return getHeatExchangerInputTemperature() - getHeatExchangerOutputTemperature() < 0;
	}
	
	// Turbine
	
	public double getTurbinePowerPerMB() {
		if (extras.isEmpty()) return 0D;
		else if (extras.get(0) instanceof Double) return (double) extras.get(0);
		else return 0D;
	}
	
	public double getTurbineExpansionLevel() {
		if (extras.size() < 2) return 1D;
		else if (extras.get(1) instanceof Double) return (double) extras.get(1);
		else return 1D;
	}
	
	// Condenser
	
	public double getCondenserProcessTime(double defaultProcessTime) {
		if (extras.isEmpty()) return defaultProcessTime;
		else if (extras.get(0) instanceof Double) return (double) extras.get(0);
		else return defaultProcessTime;
	}
	
	public int getCondenserCondensingTemperature() {
		if (extras.size() < 2) return 300;
		else if (extras.get(1) instanceof Integer) return (int) extras.get(1);
		else return 300;
	}
}
