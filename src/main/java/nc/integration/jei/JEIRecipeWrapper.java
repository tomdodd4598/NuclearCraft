package nc.integration.jei;

import mezz.jei.api.IGuiHelper;
import nc.config.NCConfig;
import nc.recipe.ProcessorRecipe;
import nc.recipe.ProcessorRecipeHandler;
import nc.tile.generator.TileDecayGenerator;

public class JEIRecipeWrapper {
	
	public static class Manufactory extends JEIProcessorRecipeWrapper<Manufactory> {

		public Manufactory(IGuiHelper guiHelper, IJEIHandler jeiHandler, ProcessorRecipeHandler recipeHandler, ProcessorRecipe recipe) {
			super(guiHelper, jeiHandler, recipeHandler, recipe, 47, 30, 176, 3, 37, 16, 74, 35);
		}
		
		@Override
		protected double getProcessTime() {
			if (recipe == null) return NCConfig.processor_time[0];
			return recipe.getProcessTime(NCConfig.processor_time[0]);
		}
	}
	
	public static class IsotopeSeparator extends JEIProcessorRecipeWrapper<IsotopeSeparator> {

		public IsotopeSeparator(IGuiHelper guiHelper, IJEIHandler jeiHandler, ProcessorRecipeHandler recipeHandler, ProcessorRecipe recipe) {
			super(guiHelper, jeiHandler, recipeHandler, recipe, 33, 30, 176, 3, 37, 18, 60, 34);
		}
		
		@Override
		protected double getProcessTime() {
			if (recipe == null) return NCConfig.processor_time[1];
			return recipe.getProcessTime(NCConfig.processor_time[1]);
		}
	}
	
	public static class DecayHastener extends JEIProcessorRecipeWrapper<DecayHastener> {

		public DecayHastener(IGuiHelper guiHelper, IJEIHandler jeiHandler, ProcessorRecipeHandler recipeHandler, ProcessorRecipe recipe) {
			super(guiHelper, jeiHandler, recipeHandler, recipe, 47, 30, 176, 3, 37, 16, 74, 35);
		}
		
		@Override
		protected double getProcessTime() {
			if (recipe == null) return NCConfig.processor_time[2];
			return recipe.getProcessTime(NCConfig.processor_time[2]);
		}
	}
	
	public static class FuelReprocessor extends JEIProcessorRecipeWrapper<FuelReprocessor> {

		public FuelReprocessor(IGuiHelper guiHelper, IJEIHandler jeiHandler, ProcessorRecipeHandler recipeHandler, ProcessorRecipe recipe) {
			super(guiHelper, jeiHandler, recipeHandler, recipe, 49, 30, 176, 3, 37, 38, 68, 30);
		}
		
		@Override
		protected double getProcessTime() {
			if (recipe == null) return NCConfig.processor_time[3];
			return recipe.getProcessTime(NCConfig.processor_time[3]);
		}
	}
	
	public static class AlloyFurnace extends JEIProcessorRecipeWrapper<AlloyFurnace> {

		public AlloyFurnace(IGuiHelper guiHelper, IJEIHandler jeiHandler, ProcessorRecipeHandler recipeHandler, ProcessorRecipe recipe) {
			super(guiHelper, jeiHandler, recipeHandler, recipe, 45, 30, 176, 3, 37, 16, 84, 35);
		}
		
		@Override
		protected double getProcessTime() {
			if (recipe == null) return NCConfig.processor_time[4];
			return recipe.getProcessTime(NCConfig.processor_time[4]);
		}
	}
	
	public static class Infuser extends JEIProcessorRecipeWrapper<Infuser> {

		public Infuser(IGuiHelper guiHelper, IJEIHandler jeiHandler, ProcessorRecipeHandler recipeHandler, ProcessorRecipe recipe) {
			super(guiHelper, jeiHandler, recipeHandler, recipe, 45, 30, 176, 3, 37, 16, 84, 35);
		}
		
		@Override
		protected double getProcessTime() {
			if (recipe == null) return NCConfig.processor_time[5];
			return recipe.getProcessTime(NCConfig.processor_time[5]);
		}
	}
	
	public static class Melter extends JEIProcessorRecipeWrapper<Melter> {

		public Melter(IGuiHelper guiHelper, IJEIHandler jeiHandler, ProcessorRecipeHandler recipeHandler, ProcessorRecipe recipe) {
			super(guiHelper, jeiHandler, recipeHandler, recipe, 47, 30, 176, 3, 37, 16, 74, 35);
		}
		
		@Override
		protected double getProcessTime() {
			if (recipe == null) return NCConfig.processor_time[6];
			return recipe.getProcessTime(NCConfig.processor_time[6]);
		}
	}
	
	public static class Supercooler extends JEIProcessorRecipeWrapper<Supercooler> {

		public Supercooler(IGuiHelper guiHelper, IJEIHandler jeiHandler, ProcessorRecipeHandler recipeHandler, ProcessorRecipe recipe) {
			super(guiHelper, jeiHandler, recipeHandler, recipe, 47, 30, 176, 3, 37, 16, 74, 35);
		}
		
		@Override
		protected double getProcessTime() {
			if (recipe == null) return NCConfig.processor_time[7];
			return recipe.getProcessTime(NCConfig.processor_time[7]);
		}
	}
	
	public static class Electrolyser extends JEIProcessorRecipeWrapper<Electrolyser> {

		public Electrolyser(IGuiHelper guiHelper, IJEIHandler jeiHandler, ProcessorRecipeHandler recipeHandler, ProcessorRecipe recipe) {
			super(guiHelper, jeiHandler, recipeHandler, recipe, 49, 30, 176, 3, 37, 38, 68, 30);
		}
		
		@Override
		protected double getProcessTime() {
			if (recipe == null) return NCConfig.processor_time[8];
			return recipe.getProcessTime(NCConfig.processor_time[8]);
		}
	}
	
	public static class Irradiator extends JEIProcessorRecipeWrapper<Irradiator> {

		public Irradiator(IGuiHelper guiHelper, IJEIHandler jeiHandler, ProcessorRecipeHandler recipeHandler, ProcessorRecipe recipe) {
			super(guiHelper, jeiHandler, recipeHandler, recipe, 31, 30, 176, 3, 37, 16, 70, 35);
		}
		
		@Override
		protected double getProcessTime() {
			if (recipe == null) return NCConfig.processor_time[9];
			return recipe.getProcessTime(NCConfig.processor_time[9]);
		}
	}
	
	public static class IngotFormer extends JEIProcessorRecipeWrapper<IngotFormer> {

		public IngotFormer(IGuiHelper guiHelper, IJEIHandler jeiHandler, ProcessorRecipeHandler recipeHandler, ProcessorRecipe recipe) {
			super(guiHelper, jeiHandler, recipeHandler, recipe, 47, 30, 176, 3, 37, 16, 74, 35);
		}
		
		@Override
		protected double getProcessTime() {
			if (recipe == null) return NCConfig.processor_time[10];
			return recipe.getProcessTime(NCConfig.processor_time[10]);
		}
	}
	
	public static class Pressurizer extends JEIProcessorRecipeWrapper<Pressurizer> {

		public Pressurizer(IGuiHelper guiHelper, IJEIHandler jeiHandler, ProcessorRecipeHandler recipeHandler, ProcessorRecipe recipe) {
			super(guiHelper, jeiHandler, recipeHandler, recipe, 47, 30, 176, 3, 37, 16, 74, 35);
		}
		
		@Override
		protected double getProcessTime() {
			if (recipe == null) return NCConfig.processor_time[11];
			return recipe.getProcessTime(NCConfig.processor_time[11]);
		}
	}
	
	public static class ChemicalReactor extends JEIProcessorRecipeWrapper<ChemicalReactor> {

		public ChemicalReactor(IGuiHelper guiHelper, IJEIHandler jeiHandler, ProcessorRecipeHandler recipeHandler, ProcessorRecipe recipe) {
			super(guiHelper, jeiHandler, recipeHandler, recipe, 31, 30, 176, 3, 37, 18, 70, 34);
		}
		
		@Override
		protected double getProcessTime() {
			if (recipe == null) return NCConfig.processor_time[12];
			return recipe.getProcessTime(NCConfig.processor_time[12]);
		}
	}
	
	public static class SaltMixer extends JEIProcessorRecipeWrapper<SaltMixer> {

		public SaltMixer(IGuiHelper guiHelper, IJEIHandler jeiHandler, ProcessorRecipeHandler recipeHandler, ProcessorRecipe recipe) {
			super(guiHelper, jeiHandler, recipeHandler, recipe, 45, 30, 176, 3, 37, 18, 84, 34);
		}
		
		@Override
		protected double getProcessTime() {
			if (recipe == null) return NCConfig.processor_time[13];
			return recipe.getProcessTime(NCConfig.processor_time[13]);
		}
	}
	
	public static class Crystallizer extends JEIProcessorRecipeWrapper<Crystallizer> {

		public Crystallizer(IGuiHelper guiHelper, IJEIHandler jeiHandler, ProcessorRecipeHandler recipeHandler, ProcessorRecipe recipe) {
			super(guiHelper, jeiHandler, recipeHandler, recipe, 47, 30, 176, 3, 37, 16, 74, 35);
		}
		
		@Override
		protected double getProcessTime() {
			if (recipe == null) return NCConfig.processor_time[14];
			return recipe.getProcessTime(NCConfig.processor_time[14]);
		}
	}
	
	public static class Dissolver extends JEIProcessorRecipeWrapper<Dissolver> {

		public Dissolver(IGuiHelper guiHelper, IJEIHandler jeiHandler, ProcessorRecipeHandler recipeHandler, ProcessorRecipe recipe) {
			super(guiHelper, jeiHandler, recipeHandler, recipe, 45, 30, 176, 3, 37, 16, 84, 35);
		}
		
		@Override
		protected double getProcessTime() {
			if (recipe == null) return NCConfig.processor_time[15];
			return recipe.getProcessTime(NCConfig.processor_time[15]);
		}
	}
	
	public static class Extractor extends JEIProcessorRecipeWrapper<Extractor> {

		public Extractor(IGuiHelper guiHelper, IJEIHandler jeiHandler, ProcessorRecipeHandler recipeHandler, ProcessorRecipe recipe) {
			super(guiHelper, jeiHandler, recipeHandler, recipe, 33, 30, 176, 3, 37, 18, 60, 34);
		}
		
		@Override
		protected double getProcessTime() {
			if (recipe == null) return NCConfig.processor_time[16];
			return recipe.getProcessTime(NCConfig.processor_time[16]);
		}
	}
	
	public static class Centrifuge extends JEIProcessorRecipeWrapper<Centrifuge> {

		public Centrifuge(IGuiHelper guiHelper, IJEIHandler jeiHandler, ProcessorRecipeHandler recipeHandler, ProcessorRecipe recipe) {
			super(guiHelper, jeiHandler, recipeHandler, recipe, 49, 30, 176, 3, 37, 38, 68, 30);
		}
		
		@Override
		protected double getProcessTime() {
			if (recipe == null) return NCConfig.processor_time[17];
			return recipe.getProcessTime(NCConfig.processor_time[17]);
		}
	}
	
	public static class RockCrusher extends JEIProcessorRecipeWrapper<RockCrusher> {

		public RockCrusher(IGuiHelper guiHelper, IJEIHandler jeiHandler, ProcessorRecipeHandler recipeHandler, ProcessorRecipe recipe) {
			super(guiHelper, jeiHandler, recipeHandler, recipe, 29, 30, 176, 3, 37, 16, 56, 35);
		}
		
		@Override
		protected double getProcessTime() {
			if (recipe == null) return NCConfig.processor_time[18];
			return recipe.getProcessTime(NCConfig.processor_time[18]);
		}
	}
	
	public static class Collector extends JEIProcessorRecipeWrapper<Collector> {

		public Collector(IGuiHelper guiHelper, IJEIHandler jeiHandler, ProcessorRecipeHandler recipeHandler, ProcessorRecipe recipe) {
			super(guiHelper, jeiHandler, recipeHandler, recipe, 33, 30, 176, 3, 37, 18, 60, 34);
		}
		
		@Override
		protected double getProcessTime() {
			return 5D*NCConfig.machine_update_rate;
		}
	}
	
	public static class ActiveCooler extends JEIProcessorRecipeWrapper<ActiveCooler> {

		public ActiveCooler(IGuiHelper guiHelper, IJEIHandler jeiHandler, ProcessorRecipeHandler recipeHandler, ProcessorRecipe recipe) {
			super(guiHelper, jeiHandler, recipeHandler, recipe, 47, 30, 176, 3, -1, -1, 74, 35);
		}
		
		@Override
		protected double getProcessTime() {
			return 1D;
		}
	}
	
	public static class DecayGenerator extends JEIProcessorRecipeWrapper<DecayGenerator> {

		public DecayGenerator(IGuiHelper guiHelper, IJEIHandler jeiHandler, ProcessorRecipeHandler recipeHandler, ProcessorRecipe recipe) {
			super(guiHelper, jeiHandler, recipeHandler, recipe, 47, 30, 176, 3, 37, 16, 74, 35);
		}
		
		@Override
		protected double getProcessTime() {
			if (recipe == null) return TileDecayGenerator.DEFAULT_LIFETIME;
			return recipe.getDecayLifetime();
		}
	}
	
	public static class Fission extends JEIProcessorRecipeWrapper<Fission> {

		public Fission(IGuiHelper guiHelper, IJEIHandler jeiHandler, ProcessorRecipeHandler recipeHandler, ProcessorRecipe recipe) {
			super(guiHelper, jeiHandler, recipeHandler, recipe, "_jei", 47, 30, 176, 3, 37, 16, 74, 35);
		}
		
		@Override
		protected double getProcessTime() {
			if (recipe == null) return 1D;
			return recipe.getFissionFuelTime()/200D;
		}
	}
	
	public static class Fusion extends JEIProcessorRecipeWrapper<Fusion> {

		public Fusion(IGuiHelper guiHelper, IJEIHandler jeiHandler, ProcessorRecipeHandler recipeHandler, ProcessorRecipe recipe) {
			super(guiHelper, jeiHandler, recipeHandler, recipe, "_jei", 55, 30, 176, 3, 37, 16, 74, 35);
		}
		
		@Override
		protected double getProcessTime() {
			if (recipe == null) return 1D;
			return recipe.getFusionComboTime();
		}
	}
	
	public static class SaltFission extends JEIProcessorRecipeWrapper<SaltFission> {

		public SaltFission(IGuiHelper guiHelper, IJEIHandler jeiHandler, ProcessorRecipeHandler recipeHandler, ProcessorRecipe recipe) {
			super(guiHelper, jeiHandler, recipeHandler, recipe, 47, 30, 176, 3, 37, 16, 74, 35);
		}
		
		@Override
		protected double getProcessTime() {
			if (recipe == null) return 1D;
			return 3.24D*recipe.getSaltFissionFuelTime();
		}
	}
	
	public static class CoolantHeater extends JEIProcessorRecipeWrapper<CoolantHeater> {

		public CoolantHeater(IGuiHelper guiHelper, IJEIHandler jeiHandler, ProcessorRecipeHandler recipeHandler, ProcessorRecipe recipe) {
			super(guiHelper, jeiHandler, recipeHandler, recipe, 47, 30, 176, 3, 37, 16, 74, 35);
		}
		
		@Override
		protected double getProcessTime() {
			return 100D;
		}
	}
	
	public static class HeatExchanger extends JEIProcessorRecipeWrapper<HeatExchanger> {

		public HeatExchanger(IGuiHelper guiHelper, IJEIHandler jeiHandler, ProcessorRecipeHandler recipeHandler, ProcessorRecipe recipe) {
			super(guiHelper, jeiHandler, recipeHandler, recipe, 47, 30, 176, recipe != null && recipe.getHeatExchangerIsHeating() ? 3 : 19, 37, 16, 74, 35);
		}
		
		@Override
		protected double getProcessTime() {
			return (recipe != null ? recipe.getHeatExchangerProcessTime(16000)*recipe.getHeatExchangerInputTemperature() : 1600000D)/12000D;
		}
	}
	
	public static class Turbine extends JEIProcessorRecipeWrapper<Turbine> {

		public Turbine(IGuiHelper guiHelper, IJEIHandler jeiHandler, ProcessorRecipeHandler recipeHandler, ProcessorRecipe recipe) {
			super(guiHelper, jeiHandler, recipeHandler, recipe, 47, 30, 176, 3, 37, 16, 74, 35);
		}
		
		@Override
		protected double getProcessTime() {
			return 100D;
		}
	}
}
