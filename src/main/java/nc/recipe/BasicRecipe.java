package nc.recipe;

import static nc.config.NCConfig.*;

import java.util.List;

import crafttweaker.annotations.ZenRegister;
import nc.multiblock.fission.FissionPlacement;
import nc.recipe.ingredient.*;
import nc.tile.internal.fluid.Tank;
import nc.util.*;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import stanhebben.zenscript.annotations.ZenClass;

@ZenClass("mods.nuclearcraft.ProcessorRecipe")
@ZenRegister
public class BasicRecipe implements IRecipe {
	
	protected List<IItemIngredient> itemIngredients, itemProducts;
	protected List<IFluidIngredient> fluidIngredients, fluidProducts;
	
	protected List<Object> extras;
	protected boolean isShapeless;
	
	public BasicRecipe(List<IItemIngredient> itemIngredients, List<IFluidIngredient> fluidIngredients, List<IItemIngredient> itemProducts, List<IFluidIngredient> fluidProducts, List<Object> extras, boolean shapeless) {
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
	public List<Object> getExtras() {
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
	public RecipeMatchResult matchIngredients(List<IItemIngredient> itemIngredientsIn, List<IFluidIngredient> fluidIngredientsIn) {
		return RecipeHelper.matchIngredients(IngredientSorption.INPUT, itemIngredients, fluidIngredients, itemIngredientsIn, fluidIngredientsIn, isShapeless);
	}
	
	@Override
	public RecipeMatchResult matchProducts(List<IItemIngredient> itemProductsIn, List<IFluidIngredient> fluidProductsIn) {
		return RecipeHelper.matchIngredients(IngredientSorption.OUTPUT, itemProducts, fluidProducts, itemProductsIn, fluidProductsIn, isShapeless);
	}
	
	// Recipe Extras
	
	// Processors
	
	public double getBaseProcessTime(double defaultProcessTime) {
		return (double) extras.get(0) * defaultProcessTime;
	}
	
	public double getBaseProcessPower(double defaultProcessPower) {
		return (double) extras.get(1) * defaultProcessPower;
	}
	
	public double getBaseProcessRadiation() {
		return (double) extras.get(2);
	}
	
	// Passive Collector
	
	public String getCollectorProductionRate() {
		return (String) extras.get(0);
	}
	
	// Decay Generator
	
	public double getDecayGeneratorLifetime() {
		return (double) extras.get(0);
	}
	
	public double getDecayGeneratorPower() {
		return (double) extras.get(1);
	}
	
	public double getDecayGeneratorRadiation() {
		return (double) extras.get(2);
	}
	
	// Placement Rule
	
	public String getPlacementRuleID() {
		return (String) extras.get(0);
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
	
	public int getIrradiatorMinFluxPerTick() {
		return (int) extras.get(3);
	}
	
	public int getIrradiatorMaxFluxPerTick() {
		return (int) extras.get(4);
	}
	
	public double getIrradiatorBaseProcessRadiation() {
		return (double) extras.get(5);
	}
	
	// Fission
	
	public int getFissionFuelTime() {
		return (int) (fission_fuel_time_multiplier * (int) extras.get(0));
	}
	
	public double getSaltFissionFuelTime() {
		return fission_fuel_time_multiplier * (double) extras.get(0);
	}
	
	public int getFissionFuelHeat() {
		return (int) (fission_fuel_heat_multiplier * (int) extras.get(1));
	}
	
	public double getFissionFuelEfficiency() {
		return fission_fuel_efficiency_multiplier * (double) extras.get(2);
	}
	
	public int getFissionFuelCriticality() {
		return (int) extras.get(3);
	}
	
	public double getFissionFuelDecayFactor() {
		return (double) extras.get(4);
	}
	
	public boolean getFissionFuelSelfPriming() {
		return (boolean) extras.get(5);
	}
	
	public double getFissionFuelRadiation() {
		return fission_fuel_radiation_multiplier * (double) extras.get(6);
	}
	
	// Fission Heating
	
	public int getFissionHeatingHeatPerInputMB() {
		return (int) extras.get(0);
	}
	
	// Fission Emergency Cooling
	
	public double getEmergencyCoolingHeatPerInputMB() {
		return (double) extras.get(0);
	}
	
	// Fusion
	
	public double getFusionComboTime() {
		return fusion_fuel_time_multiplier * (double) extras.get(0);
	}
	
	public double getFusionComboHeat() {
		return fusion_fuel_heat_multiplier * (double) extras.get(1);
	}
	
	public double getFusionComboOptimalTemperature() {
		return (double) extras.get(2);
	}
	
	public double getFusionComboRadiation() {
		return fusion_fuel_radiation_multiplier * (double) extras.get(3);
	}
	
	// Coolant Heater
	
	public int getCoolantHeaterCoolingRate() {
		return (int) extras.get(0);
	}
	
	public String getCoolantHeaterPlacementRule() {
		return (String) extras.get(1);
	}
	
	public String[] getCoolantHeaterJEIInfo() {
		String rule = FissionPlacement.TOOLTIP_MAP.get(getCoolantHeaterPlacementRule());
		if (rule != null) {
			return FontRenderHelper.wrapString(rule, InfoHelper.MAXIMUM_TEXT_WIDTH);
		}
		return InfoHelper.EMPTY_ARRAY;
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
	
	public double getTurbineSpinUpMultiplier() {
		return (double) extras.get(2);
	}
	
	public String getTurbineParticleEffect() {
		EnumParticleTypes particle = EnumParticleTypes.getByName((String) extras.get(3));
		return particle == null ? "cloud" : (String) extras.get(3);
	}
	
	public double getTurbineParticleSpeedMultiplier() {
		return (double) extras.get(4);
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
