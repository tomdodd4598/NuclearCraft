package nc.recipe;

import java.util.List;

import nc.recipe.ingredient.IFluidIngredient;
import nc.recipe.ingredient.IItemIngredient;
import nc.tile.generator.TileDecayGenerator;
import nc.tile.internal.fluid.Tank;
import nc.util.InfoHelper;
import nc.util.Lang;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;

public class ProcessorRecipe implements IRecipe {
	
	protected List<IItemIngredient> itemIngredients, itemProducts;
	protected List<IFluidIngredient> fluidIngredients, fluidProducts;
	
	//private int itemInputSize, fluidInputSize, itemOutputSize, fluidOutputSize;
	
	protected List extras;
	public boolean isShapeless;
	
	public ProcessorRecipe(List<IItemIngredient> itemIngredientsList, List<IFluidIngredient> fluidIngredientsList, List<IItemIngredient> itemProductsList, List<IFluidIngredient> fluidProductsList, List extrasList, boolean shapeless) {
		itemIngredients = itemIngredientsList;
		fluidIngredients = fluidIngredientsList;
		itemProducts = itemProductsList;
		fluidProducts = fluidProductsList;
		
		/*itemInputSize = itemIngredientsList.size();
		fluidInputSize = fluidIngredientsList.size();
		itemOutputSize = itemProductsList.size();
		fluidOutputSize = fluidProductsList.size();*/
		
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
	
	// Passive Collector
	
	public String getCollectorProductionRate() {
		if (extras.isEmpty()) return null;
		else if (extras.get(0) instanceof String) return (String) extras.get(0);
		return null;
	}
	
	// Decay Generator
	
	public double getDecayLifetime() {
		if (extras.isEmpty()) return TileDecayGenerator.DEFAULT_LIFETIME;
		else if (extras.get(0) instanceof Double) return (double) extras.get(0);
		return TileDecayGenerator.DEFAULT_LIFETIME;
	}
	
	public double getDecayPower() {
		if (extras.size() < 2) return 0D;
		if (extras.get(1) instanceof Double) return (double) extras.get(1);
		return 0D;
	}
	
	public double getDecayRadiation() {
		if (extras.size() < 3) return 0D;
		else if (extras.get(2) instanceof Double) return (double) extras.get(2);
		else return 0D;
	}
	
	// Fission Moderator
	
	public int getFissionModeratorFluxFactor() {
		if (extras.isEmpty()) return 0;
		else if (extras.get(0) instanceof Integer) return (int) extras.get(0);
		else return 0;
	}
	
	public double getFissionModeratorEfficiency() {
		if (extras.size() < 2) return 1D;
		else if (extras.get(1) instanceof Double) return (double) extras.get(1);
		else return 1D;
	}
	
	// Fission Reflector
	
	public double getFissionReflectorEfficiency() {
		if (extras.isEmpty()) return 0D;
		else if (extras.get(0) instanceof Double) return (double) extras.get(0);
		else return 0D;
	}
	
	public double getFissionReflectorReflectivity() {
		if (extras.size() < 2) return 0D;
		else if (extras.get(1) instanceof Double) return (double) extras.get(1);
		else return 0D;
	}
	
	// Fission Irradiator
	
	public int getIrradiatorFluxRequired() {
		if (extras.isEmpty()) return 1;
		else if (extras.get(0) instanceof Integer) return (int) extras.get(0);
		else return 1;
	}
	
	public double getIrradiatorHeatPerFlux() {
		if (extras.size() < 2) return 0D;
		else if (extras.get(1) instanceof Double) return (double) extras.get(1);
		else return 0D;
	}
	
	public double getIrradiatorBaseProcessRadiation() {
		if (extras.size() < 3) return 0D;
		else if (extras.get(2) instanceof Double) return (double) extras.get(2);
		else return 0D;
	}
	
	// Fission
	
	public int getFissionFuelTime() {
		if (extras.isEmpty()) return 1;
		else if (extras.get(0) instanceof Integer) return (int) extras.get(0);
		else return 1;
	}
	
	public int getFissionFuelHeat() {
		if (extras.size() < 2) return 0;
		else if (extras.get(1) instanceof Integer) return (int) extras.get(1);
		else return 0;
	}
	
	public double getFissionFuelEfficiency() {
		if (extras.size() < 3) return 0D;
		else if (extras.get(2) instanceof Double) return (double) extras.get(2);
		else return 0D;
	}
	
	public int getFissionFuelCriticality() {
		if (extras.size() < 4) return 1;
		else if (extras.get(3) instanceof Integer) return (int) extras.get(3);
		else return 1;
	}
	
	public boolean getFissionFuelSelfPriming() {
		if (extras.size() < 5) return false;
		else if (extras.get(4) instanceof Boolean) return (boolean) extras.get(4);
		else return false;
	}
	
	public double getFissionFuelRadiation() {
		if (extras.size() < 6) return 0D;
		else if (extras.get(5) instanceof Double) return (double) extras.get(5);
		else return 0D;
	}
	
	// Fission Heating
	
	public int getFissionHeatingHeatPerInputMB() {
		if (extras.isEmpty()) return 64;
		else if (extras.get(0) instanceof Integer) return (int) extras.get(0);
		else return 64;
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
	
	// Coolant Heater
	
	public double getCoolantHeaterCoolingRate() {
		if (extras.isEmpty()) return 10D;
		else if (extras.get(0) instanceof Double) return (double) extras.get(0);
		else return 10D;
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
	
	public String getTurbineParticleEffect() {
		if (extras.size() < 3) return "cloud";
		else if (extras.get(2) instanceof String) {
			EnumParticleTypes particle = EnumParticleTypes.getByName((String)extras.get(2));
			return particle == null ? "cloud" : (String)extras.get(2);
		}
		else return "cloud";
	}
	
	public double getTurbineParticleSpeedMultiplier() {
		if (extras.size() < 4) return 1D/23.2D;
		else if (extras.get(3) instanceof Double) return (double) extras.get(3);
		else return 1D/23.2D;
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
	
	// Radiation Scrubber
	
	public int getScrubberProcessTime() {
		if (extras.isEmpty()) return 1;
		else if (extras.get(0) instanceof Integer) return (int) extras.get(0);
		else return 1;
	}
	
	public int getScrubberProcessPower() {
		if (extras.size() < 2) return 0;
		else if (extras.get(1) instanceof Integer) return (int) extras.get(1);
		else return 0;
	}
	
	public double getScrubberProcessEfficiency() {
		if (extras.size() < 3) return 0D;
		else if (extras.get(2) instanceof Double) return (double) extras.get(2);
		else return 0D;
	}
	
	// Radiation Block Mutations
	
	public double getBlockMutationThreshold(boolean purification) {
		if (extras.isEmpty()) return purification ? 0D : Double.MAX_VALUE;
		else if (extras.get(0) instanceof Double) return (double) extras.get(0);
		else return purification ? 0D : Double.MAX_VALUE;
	}
}
