package nc.recipe;

import java.util.List;

import crafttweaker.annotations.ZenRegister;
import nc.recipe.ingredient.IFluidIngredient;
import nc.recipe.ingredient.IItemIngredient;
import nc.tile.internal.fluid.Tank;
import nc.util.InfoHelper;
import nc.util.Lang;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import stanhebben.zenscript.annotations.ZenClass;

@ZenClass("mods.nuclearcraft.ProcessorRecipe")
@ZenRegister
public class ProcessorRecipe implements IRecipe {
	
	protected List<IItemIngredient> itemIngredients, itemProducts;
	protected List<IFluidIngredient> fluidIngredients, fluidProducts;
	
	protected List extras;
	protected boolean isShapeless;
	
	public ProcessorRecipe(List<IItemIngredient> itemIngredients, List<IFluidIngredient> fluidIngredients, List<IItemIngredient> itemProducts, List<IFluidIngredient> fluidProducts, List extras, boolean shapeless) {
		this.itemIngredients = itemIngredients;
		this.fluidIngredients = fluidIngredients;
		this.itemProducts = itemProducts;
		this.fluidProducts = fluidProducts;
		
		this.extras = extras;
		isShapeless = shapeless;
	}
	
	@Override
	public List<IItemIngredient> getItemIngredients() {
		return itemIngredients;
	}
	
	@Override
	public List<IFluidIngredient> getFluidIngredients() {
		return fluidIngredients;
	}
	
	@Override
	public List<IItemIngredient> getItemProducts() {
		return itemProducts;
	}
	
	@Override
	public List<IFluidIngredient> getFluidProducts() {
		return fluidProducts;
	}
	
	@Override
	public List getExtras() {
		return extras;
	}
	
	public boolean isShapeless() {
		return isShapeless;
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
		return ((double) extras.get(0))*defaultProcessTime;
	}
	
	public double getBaseProcessPower(double defaultProcessPower) {
		return ((double) extras.get(1))*defaultProcessPower;
	}
	
	public double getBaseProcessRadiation() {
		return (double) extras.get(2);
	}
	
	// Passive Collector
	
	public String getCollectorProductionRate() {
		return (String) extras.get(0);
	}
	
	// Decay Generator
	
	public double getDecayLifetime() {
		return (double) extras.get(0);
	}
	
	public double getDecayPower() {
		return (double) extras.get(1);
	}
	
	public double getDecayRadiation() {
		return (double) extras.get(2);
	}
	
	// Fission Moderator
	
	public int getFissionModeratorFluxFactor() {
		return (int) extras.get(0);
	}
	
	public double getFissionModeratorEfficiency() {
		return (double) extras.get(1);
	}
	
	// Fission Reflector
	
	public double getFissionReflectorEfficiency() {
		return (double) extras.get(0);
	}
	
	public double getFissionReflectorReflectivity() {
		return (double) extras.get(1);
	}
	
	// Fission Irradiator
	
	public int getIrradiatorFluxRequired() {
		return (int) extras.get(0);
	}
	
	public double getIrradiatorHeatPerFlux() {
		return (double) extras.get(1);
	}
	
	public double getIrradiatorProcessEfficiency() {
		return (double) extras.get(2);
	}
	
	public double getIrradiatorBaseProcessRadiation() {
		return (double) extras.get(3);
	}
	
	// Fission
	
	public int getFissionFuelTime() {
		return (int) extras.get(0);
	}
	
	public double getSaltFissionFuelTime() {
		return (double) extras.get(0);
	}
	
	public int getFissionFuelHeat() {
		return (int) extras.get(1);
	}
	
	public double getFissionFuelEfficiency() {
		return (double) extras.get(2);
	}
	
	public int getFissionFuelCriticality() {
		return (int) extras.get(3);
	}
	
	public boolean getFissionFuelSelfPriming() {
		return (boolean) extras.get(4);
	}
	
	public double getFissionFuelRadiation() {
		return (double) extras.get(5);
	}
	
	// Fission Heating
	
	public int getFissionHeatingHeatPerInputMB() {
		return (int) extras.get(0);
	}
	
	// Fusion
	
	public double getFusionComboTime() {
		return (double) extras.get(0);
	}
	
	public double getFusionComboPower() {
		return (double) extras.get(1);
	}
	
	public double getFusionComboHeatVariable() {
		return (double) extras.get(2);
	}
	
	public double getFusionComboRadiation() {
		return (double) extras.get(3);
	}
	
	// Coolant Heater
	
	public int getCoolantHeaterCoolingRate() {
		return (int) extras.get(0);
	}
	
	public String[] getCoolantHeaterJEIInfo() {
		return InfoHelper.formattedInfo(Lang.localise((String) extras.get(1)));
	}
	
	// Heat Exchanger
	
	public double getHeatExchangerProcessTime() {
		return (double) extras.get(0);
	}
	
	public int getHeatExchangerInputTemperature() {
		return (int) extras.get(1);
	}
	
	public int getHeatExchangerOutputTemperature() {
		return (int) extras.get(2);
	}
	
	public boolean getHeatExchangerIsHeating() {
		return getHeatExchangerInputTemperature() - getHeatExchangerOutputTemperature() < 0;
	}
	
	// Turbine
	
	public double getTurbinePowerPerMB() {
		return (double) extras.get(0);
	}
	
	public double getTurbineExpansionLevel() {
		return (double) extras.get(1);
	}
	
	public String getTurbineParticleEffect() {
		EnumParticleTypes particle = EnumParticleTypes.getByName((String)extras.get(2));
		return particle == null ? "cloud" : (String)extras.get(2);
	}
	
	public double getTurbineParticleSpeedMultiplier() {
		return (double) extras.get(3);
	}
	
	// Condenser
	
	public double getCondenserProcessTime() {
		return (double) extras.get(0);
	}
	
	public int getCondenserCondensingTemperature() {
		return (int) extras.get(1);
	}
	
	// Radiation Scrubber
	
	public int getScrubberProcessTime() {
		return (int) extras.get(0);
	}
	
	public int getScrubberProcessPower() {
		return (int) extras.get(1);
	}
	
	public double getScrubberProcessEfficiency() {
		return (double) extras.get(2);
	}
	
	// Radiation Block Mutations
	
	public double getBlockMutationThreshold() {
		return (double) extras.get(0);
	}
}
