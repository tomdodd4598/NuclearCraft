package nc.recipe.multiblock;

import nc.recipe.*;
import nc.util.*;

import java.util.List;

import static nc.radiation.RadSources.*;
import static nc.util.FluidStackHelper.INGOT_VOLUME;

public class DecayPoolHeatSourceRecipes extends BasicRecipeHandler {
	
	public DecayPoolHeatSourceRecipes() {
		super("decay_pool_heat_source", 1, 1, 1, 1);
	}
	
	@Override
	public void addRecipes() {
		addItemDecayRecipe("dustStrontium90", "dustZirconium", STRONTIUM_90);
		addItemDecayRecipe("dustRuthenium106", "dustPalladium", RUTHENIUM_106);
		addItemDecayRecipe(OreDictHelper.oreExists("dustCesium137") ? "dustCesium137" : "dustCaesium137", OreDictHelper.oreExists("dustBarium") ? "dustBarium" : "ingotBarium", CAESIUM_137);
		addItemDecayRecipe("dustPromethium147", OreDictHelper.oreExists("dustSamarium") ? "dustSamarium" : "ingotSamarium", PROMETHIUM_147);
		addItemDecayRecipe("dustEuropium155", "dustGadolinium", EUROPIUM_155);
		
		addFluidDecayRecipe("strontium_90", "zirconium", STRONTIUM_90);
		addFluidDecayRecipe("ruthenium_106", "palladium", RUTHENIUM_106);
		addFluidDecayRecipe(FluidRegHelper.fluidExists("cesium_137") ? "cesium_137" : "caesium_137", "barium", CAESIUM_137);
		addFluidDecayRecipe("promethium_147", "samarium", PROMETHIUM_147);
		addFluidDecayRecipe("europium_155", "gadolinium", EUROPIUM_155);
	}
	
	public void addItemDecayRecipe(String input, String output, double radiation) {
		addDecayRecipe(input, emptyFluidStack(), output, emptyFluidStack(), radiation);
	}
	
	public void addFluidDecayRecipe(String input, String output, double radiation) {
		addDecayRecipe(emptyItemStack(), fluidStack(input, INGOT_VOLUME), emptyItemStack(), fluidStack(output, INGOT_VOLUME), radiation);
	}
	
	public void addDecayRecipe(Object itemInput, Object fluidInput, Object itemOutput, Object fluidOutput, double radiation) {
		double mult = RecipeHelper.getDecayTimeMultiplier(radiation / 9D, 1E-6D);
		addRecipe(itemInput, fluidInput, itemOutput, fluidOutput, NCMath.roundTo(20D * 12000D * mult, 20D), NCMath.roundTo(20D / mult, 5D));
	}
	
	@Override
	protected List<Object> fixedExtras(List<Object> extras) {
		ExtrasFixer fixer = new ExtrasFixer(extras);
		fixer.add(Double.class, 1D);
		fixer.add(Double.class, 0D);
		return fixer.fixed;
	}
}
