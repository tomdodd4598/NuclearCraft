package nc.integration.crafttweaker;

import com.google.common.collect.Lists;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import nc.ModCheck;
import nc.config.NCConfig;
import nc.recipe.NCRecipes;
import nc.recipe.IngredientSorption;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

public class NCCraftTweaker {
	
	public static class RecipeHandler {
		static {
			ModCheck.init();
			NCRecipes.init();
		}
	}
	
	@ZenClass("mods.nuclearcraft.manufactory")
	@ZenRegister
	public static class ManufactoryHandler extends RecipeHandler {
		
		@ZenMethod
		public static void addRecipe(Object[] objects) {
			CraftTweakerAPI.apply(new AddProcessorRecipe(NCRecipes.Type.MANUFACTORY, Lists.newArrayList(objects)));
		}

		@ZenMethod
		public static void removeRecipeWithInput(IIngredient[] inputs) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.Type.MANUFACTORY, IngredientSorption.INPUT, Lists.newArrayList(inputs)));
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient[] outputs) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.Type.MANUFACTORY, IngredientSorption.OUTPUT, Lists.newArrayList(outputs)));
		}
		
		@ZenMethod
		public static void removeAllRecipes() {
			CraftTweakerAPI.apply(new RemoveAllProcessorRecipes(NCRecipes.Type.MANUFACTORY));
		}
		
		// OLD METHODS
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient output1) {
			CraftTweakerAPI.apply(new AddProcessorRecipe(NCRecipes.Type.MANUFACTORY, Lists.newArrayList(input1, output1, 1D)));
		}
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient output1, int processTime) {
			CraftTweakerAPI.apply(new AddProcessorRecipe(NCRecipes.Type.MANUFACTORY, Lists.newArrayList(input1, output1, (double)processTime/(double)NCConfig.processor_time[0])));
		}

		@ZenMethod
		public static void removeRecipeWithInput(IIngredient input1) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.Type.MANUFACTORY, IngredientSorption.INPUT, Lists.newArrayList(input1)));
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient output1) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.Type.MANUFACTORY, IngredientSorption.OUTPUT, Lists.newArrayList(output1)));
		}
	}
	
	@ZenClass("mods.nuclearcraft.isotope_separator")
	@ZenRegister
	public static class IsotopeSeparatorHandler extends RecipeHandler {
		
		@ZenMethod
		public static void addRecipe(Object[] objects) {
			CraftTweakerAPI.apply(new AddProcessorRecipe(NCRecipes.Type.ISOTOPE_SEPARATOR, Lists.newArrayList(objects)));
		}

		@ZenMethod
		public static void removeRecipeWithInput(IIngredient[] inputs) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.Type.ISOTOPE_SEPARATOR, IngredientSorption.INPUT, Lists.newArrayList(inputs)));
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient[] outputs) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.Type.ISOTOPE_SEPARATOR, IngredientSorption.OUTPUT, Lists.newArrayList(outputs)));
		}
		
		@ZenMethod
		public static void removeAllRecipes() {
			CraftTweakerAPI.apply(new RemoveAllProcessorRecipes(NCRecipes.Type.ISOTOPE_SEPARATOR));
		}
		
		// OLD METHODS
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient output1, IIngredient output2) {
			CraftTweakerAPI.apply(new AddProcessorRecipe(NCRecipes.Type.ISOTOPE_SEPARATOR, Lists.newArrayList(input1, output1, output2, 1D)));
		}
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient output1, IIngredient output2, int processTime) {
			CraftTweakerAPI.apply(new AddProcessorRecipe(NCRecipes.Type.ISOTOPE_SEPARATOR, Lists.newArrayList(input1, output1, output2, (double)processTime/(double)NCConfig.processor_time[1])));
		}
		
		@ZenMethod
		public static void removeRecipeWithInput(IIngredient input1) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.Type.ISOTOPE_SEPARATOR, IngredientSorption.INPUT, Lists.newArrayList(input1)));
		}

		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient output1, IIngredient output2) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.Type.ISOTOPE_SEPARATOR, IngredientSorption.OUTPUT, Lists.newArrayList(output1, output2)));
		}
	}
	
	@ZenClass("mods.nuclearcraft.decay_hastener")
	@ZenRegister
	public static class DecayHastenerHandler extends RecipeHandler {
		
		@ZenMethod
		public static void addRecipe(Object[] objects) {
			CraftTweakerAPI.apply(new AddProcessorRecipe(NCRecipes.Type.DECAY_HASTENER, Lists.newArrayList(objects)));
		}

		@ZenMethod
		public static void removeRecipeWithInput(IIngredient[] inputs) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.Type.DECAY_HASTENER, IngredientSorption.INPUT, Lists.newArrayList(inputs)));
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient[] outputs) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.Type.DECAY_HASTENER, IngredientSorption.OUTPUT, Lists.newArrayList(outputs)));
		}
		
		@ZenMethod
		public static void removeAllRecipes() {
			CraftTweakerAPI.apply(new RemoveAllProcessorRecipes(NCRecipes.Type.DECAY_HASTENER));
		}
		
		// OLD METHODS
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient output1) {
			CraftTweakerAPI.apply(new AddProcessorRecipe(NCRecipes.Type.DECAY_HASTENER, Lists.newArrayList(input1, output1, 1D)));
		}
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient output1, int processTime) {
			CraftTweakerAPI.apply(new AddProcessorRecipe(NCRecipes.Type.DECAY_HASTENER, Lists.newArrayList(input1, output1, (double)processTime/(double)NCConfig.processor_time[2])));
		}

		@ZenMethod
		public static void removeRecipeWithInput(IIngredient input1) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.Type.DECAY_HASTENER, IngredientSorption.INPUT, Lists.newArrayList(input1)));
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient output1) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.Type.DECAY_HASTENER, IngredientSorption.OUTPUT, Lists.newArrayList(output1)));
		}
	}
	
	@ZenClass("mods.nuclearcraft.fuel_reprocessor")
	@ZenRegister
	public static class FuelReprocessorHandler extends RecipeHandler {
		
		@ZenMethod
		public static void addRecipe(Object[] objects) {
			CraftTweakerAPI.apply(new AddProcessorRecipe(NCRecipes.Type.FUEL_REPROCESSOR, Lists.newArrayList(objects)));
		}

		@ZenMethod
		public static void removeRecipeWithInput(IIngredient[] inputs) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.Type.FUEL_REPROCESSOR, IngredientSorption.INPUT, Lists.newArrayList(inputs)));
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient[] outputs) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.Type.FUEL_REPROCESSOR, IngredientSorption.OUTPUT, Lists.newArrayList(outputs)));
		}
		
		@ZenMethod
		public static void removeAllRecipes() {
			CraftTweakerAPI.apply(new RemoveAllProcessorRecipes(NCRecipes.Type.FUEL_REPROCESSOR));
		}
		
		// OLD METHODS
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient output1, IIngredient output2, IIngredient output3, IIngredient output4) {
			CraftTweakerAPI.apply(new AddProcessorRecipe(NCRecipes.Type.FUEL_REPROCESSOR, Lists.newArrayList(input1, output1, output2, output3, output4, 1D)));
		}
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient output1, IIngredient output2, IIngredient output3, IIngredient output4, int processTime) {
			CraftTweakerAPI.apply(new AddProcessorRecipe(NCRecipes.Type.FUEL_REPROCESSOR, Lists.newArrayList(input1, output1, output2, output3, output4, (double)processTime/(double)NCConfig.processor_time[3])));
		}

		@ZenMethod
		public static void removeRecipeWithInput(IIngredient input1) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.Type.FUEL_REPROCESSOR, IngredientSorption.INPUT, Lists.newArrayList(input1)));
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient output1, IIngredient output2, IIngredient output3, IIngredient output4) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.Type.FUEL_REPROCESSOR, IngredientSorption.OUTPUT, Lists.newArrayList(output1, output2, output3, output4)));
		}
	}
	
	@ZenClass("mods.nuclearcraft.alloy_furnace")
	@ZenRegister
	public static class AlloyFurnaceHandler extends RecipeHandler {
		
		@ZenMethod
		public static void addRecipe(Object[] objects) {
			CraftTweakerAPI.apply(new AddProcessorRecipe(NCRecipes.Type.ALLOY_FURNACE, Lists.newArrayList(objects)));
		}

		@ZenMethod
		public static void removeRecipeWithInput(IIngredient[] inputs) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.Type.ALLOY_FURNACE, IngredientSorption.INPUT, Lists.newArrayList(inputs)));
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient[] outputs) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.Type.ALLOY_FURNACE, IngredientSorption.OUTPUT, Lists.newArrayList(outputs)));
		}
		
		@ZenMethod
		public static void removeAllRecipes() {
			CraftTweakerAPI.apply(new RemoveAllProcessorRecipes(NCRecipes.Type.ALLOY_FURNACE));
		}
		
		// OLD METHODS
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient input2, IIngredient output1) {
			CraftTweakerAPI.apply(new AddProcessorRecipe(NCRecipes.Type.ALLOY_FURNACE, Lists.newArrayList(input1, input2, output1, 1D)));
		}
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient input2, IIngredient output1, int processTime) {
			CraftTweakerAPI.apply(new AddProcessorRecipe(NCRecipes.Type.ALLOY_FURNACE, Lists.newArrayList(input1, input2, output1, (double)processTime/(double)NCConfig.processor_time[4])));
		}

		@ZenMethod
		public static void removeRecipeWithInput(IIngredient input1, IIngredient input2) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.Type.ALLOY_FURNACE, IngredientSorption.INPUT, Lists.newArrayList(input1, input2)));
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient output1) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.Type.ALLOY_FURNACE, IngredientSorption.OUTPUT, Lists.newArrayList(output1)));
		}
	}
	
	@ZenClass("mods.nuclearcraft.infuser")
	@ZenRegister
	public static class InfuserHandler extends RecipeHandler {
		
		@ZenMethod
		public static void addRecipe(Object[] objects) {
			CraftTweakerAPI.apply(new AddProcessorRecipe(NCRecipes.Type.INFUSER, Lists.newArrayList(objects)));
		}

		@ZenMethod
		public static void removeRecipeWithInput(IIngredient[] inputs) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.Type.INFUSER, IngredientSorption.INPUT, Lists.newArrayList(inputs)));
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient[] outputs) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.Type.INFUSER, IngredientSorption.OUTPUT, Lists.newArrayList(outputs)));
		}
		
		@ZenMethod
		public static void removeAllRecipes() {
			CraftTweakerAPI.apply(new RemoveAllProcessorRecipes(NCRecipes.Type.INFUSER));
		}
		
		// OLD METHODS
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient input2, IIngredient output1) {
			CraftTweakerAPI.apply(new AddProcessorRecipe(NCRecipes.Type.INFUSER, Lists.newArrayList(input1, input2, output1, 1D)));
		}
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient input2, IIngredient output1, int processTime) {
			CraftTweakerAPI.apply(new AddProcessorRecipe(NCRecipes.Type.INFUSER, Lists.newArrayList(input1, input2, output1, (double)processTime/(double)NCConfig.processor_time[5])));
		}

		@ZenMethod
		public static void removeRecipeWithInput(IIngredient input1, IIngredient input2) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.Type.INFUSER, IngredientSorption.INPUT, Lists.newArrayList(input1, input2)));
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient output1) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.Type.INFUSER, IngredientSorption.OUTPUT, Lists.newArrayList(output1)));
		}
	}
	
	@ZenClass("mods.nuclearcraft.melter")
	@ZenRegister
	public static class MelterHandler extends RecipeHandler {
		
		@ZenMethod
		public static void addRecipe(Object[] objects) {
			CraftTweakerAPI.apply(new AddProcessorRecipe(NCRecipes.Type.MELTER, Lists.newArrayList(objects)));
		}

		@ZenMethod
		public static void removeRecipeWithInput(IIngredient[] inputs) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.Type.MELTER, IngredientSorption.INPUT, Lists.newArrayList(inputs)));
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient[] outputs) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.Type.MELTER, IngredientSorption.OUTPUT, Lists.newArrayList(outputs)));
		}
		
		@ZenMethod
		public static void removeAllRecipes() {
			CraftTweakerAPI.apply(new RemoveAllProcessorRecipes(NCRecipes.Type.MELTER));
		}
		
		// OLD METHODS
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient output1) {
			CraftTweakerAPI.apply(new AddProcessorRecipe(NCRecipes.Type.MELTER, Lists.newArrayList(input1, output1, 1D)));
		}
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient output1, int processTime) {
			CraftTweakerAPI.apply(new AddProcessorRecipe(NCRecipes.Type.MELTER, Lists.newArrayList(input1, output1, (double)processTime/(double)NCConfig.processor_time[6])));
		}

		@ZenMethod
		public static void removeRecipeWithInput(IIngredient input1) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.Type.MELTER, IngredientSorption.INPUT, Lists.newArrayList(input1)));
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient output1) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.Type.MELTER, IngredientSorption.OUTPUT, Lists.newArrayList(output1)));
		}
	}
	
	@ZenClass("mods.nuclearcraft.supercooler")
	@ZenRegister
	public static class SupercoolerHandler extends RecipeHandler {
		
		@ZenMethod
		public static void addRecipe(Object[] objects) {
			CraftTweakerAPI.apply(new AddProcessorRecipe(NCRecipes.Type.SUPERCOOLER, Lists.newArrayList(objects)));
		}

		@ZenMethod
		public static void removeRecipeWithInput(IIngredient[] inputs) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.Type.SUPERCOOLER, IngredientSorption.INPUT, Lists.newArrayList(inputs)));
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient[] outputs) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.Type.SUPERCOOLER, IngredientSorption.OUTPUT, Lists.newArrayList(outputs)));
		}
		
		@ZenMethod
		public static void removeAllRecipes() {
			CraftTweakerAPI.apply(new RemoveAllProcessorRecipes(NCRecipes.Type.SUPERCOOLER));
		}
		
		// OLD METHODS
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient output1) {
			CraftTweakerAPI.apply(new AddProcessorRecipe(NCRecipes.Type.SUPERCOOLER, Lists.newArrayList(input1, output1, 1D)));
		}
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient output1, int processTime) {
			CraftTweakerAPI.apply(new AddProcessorRecipe(NCRecipes.Type.SUPERCOOLER, Lists.newArrayList(input1, output1, (double)processTime/(double)NCConfig.processor_time[7])));
		}

		@ZenMethod
		public static void removeRecipeWithInput(IIngredient input1) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.Type.SUPERCOOLER, IngredientSorption.INPUT, Lists.newArrayList(input1)));
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient output1) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.Type.SUPERCOOLER, IngredientSorption.OUTPUT, Lists.newArrayList(output1)));
		}
	}
	
	@ZenClass("mods.nuclearcraft.electrolyser")
	@ZenRegister
	public static class ElectrolyserHandler extends RecipeHandler {
		
		@ZenMethod
		public static void addRecipe(Object[] objects) {
			CraftTweakerAPI.apply(new AddProcessorRecipe(NCRecipes.Type.ELECTROLYSER, Lists.newArrayList(objects)));
		}

		@ZenMethod
		public static void removeRecipeWithInput(IIngredient[] inputs) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.Type.ELECTROLYSER, IngredientSorption.INPUT, Lists.newArrayList(inputs)));
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient[] outputs) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.Type.ELECTROLYSER, IngredientSorption.OUTPUT, Lists.newArrayList(outputs)));
		}
		
		@ZenMethod
		public static void removeAllRecipes() {
			CraftTweakerAPI.apply(new RemoveAllProcessorRecipes(NCRecipes.Type.ELECTROLYSER));
		}
		
		// OLD METHODS
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient output1, IIngredient output2, IIngredient output3, IIngredient output4) {
			CraftTweakerAPI.apply(new AddProcessorRecipe(NCRecipes.Type.ELECTROLYSER, Lists.newArrayList(input1, output1, output2, output3, output4, 1D)));
		}
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient output1, IIngredient output2, IIngredient output3, IIngredient output4, int processTime) {
			CraftTweakerAPI.apply(new AddProcessorRecipe(NCRecipes.Type.ELECTROLYSER, Lists.newArrayList(input1, output1, output2, output3, output4, (double)processTime/(double)NCConfig.processor_time[8])));
		}
		
		@ZenMethod
		public static void removeRecipeWithInput(IIngredient input1) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.Type.ELECTROLYSER, IngredientSorption.INPUT, Lists.newArrayList(input1)));
		}

		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient output1, IIngredient output2, IIngredient output3, IIngredient output4) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.Type.ELECTROLYSER, IngredientSorption.OUTPUT, Lists.newArrayList(output1, output2, output3, output4)));
		}
	}
	
	@ZenClass("mods.nuclearcraft.irradiator")
	@ZenRegister
	public static class IrradiatorHandler extends RecipeHandler {
		
		@ZenMethod
		public static void addRecipe(Object[] objects) {
			CraftTweakerAPI.apply(new AddProcessorRecipe(NCRecipes.Type.IRRADIATOR, Lists.newArrayList(objects)));
		}

		@ZenMethod
		public static void removeRecipeWithInput(IIngredient[] inputs) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.Type.IRRADIATOR, IngredientSorption.INPUT, Lists.newArrayList(inputs)));
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient[] outputs) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.Type.IRRADIATOR, IngredientSorption.OUTPUT, Lists.newArrayList(outputs)));
		}
		
		@ZenMethod
		public static void removeAllRecipes() {
			CraftTweakerAPI.apply(new RemoveAllProcessorRecipes(NCRecipes.Type.IRRADIATOR));
		}
		
		// OLD METHODS
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient input2, IIngredient output1, IIngredient output2) {
			CraftTweakerAPI.apply(new AddProcessorRecipe(NCRecipes.Type.IRRADIATOR, Lists.newArrayList(input1, input2, output1, output2, 1D)));
		}
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient input2, IIngredient output1, IIngredient output2, int processTime) {
			CraftTweakerAPI.apply(new AddProcessorRecipe(NCRecipes.Type.IRRADIATOR, Lists.newArrayList(input1, input2, output1, output2, (double)processTime/(double)NCConfig.processor_time[9])));
		}

		@ZenMethod
		public static void removeRecipeWithInput(IIngredient input1, IIngredient input2) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.Type.IRRADIATOR, IngredientSorption.INPUT, Lists.newArrayList(input1, input2)));
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient output1, IIngredient output2) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.Type.IRRADIATOR, IngredientSorption.OUTPUT, Lists.newArrayList(output1, output2)));
		}
	}
	
	@ZenClass("mods.nuclearcraft.ingot_former")
	@ZenRegister
	public static class IngotFormerHandler extends RecipeHandler {
		
		@ZenMethod
		public static void addRecipe(Object[] objects) {
			CraftTweakerAPI.apply(new AddProcessorRecipe(NCRecipes.Type.INGOT_FORMER, Lists.newArrayList(objects)));
		}

		@ZenMethod
		public static void removeRecipeWithInput(IIngredient[] inputs) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.Type.INGOT_FORMER, IngredientSorption.INPUT, Lists.newArrayList(inputs)));
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient[] outputs) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.Type.INGOT_FORMER, IngredientSorption.OUTPUT, Lists.newArrayList(outputs)));
		}
		
		@ZenMethod
		public static void removeAllRecipes() {
			CraftTweakerAPI.apply(new RemoveAllProcessorRecipes(NCRecipes.Type.INGOT_FORMER));
		}
		
		// OLD METHODS
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient output1) {
			CraftTweakerAPI.apply(new AddProcessorRecipe(NCRecipes.Type.INGOT_FORMER, Lists.newArrayList(input1, output1, 1D)));
		}
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient output1, int processTime) {
			CraftTweakerAPI.apply(new AddProcessorRecipe(NCRecipes.Type.INGOT_FORMER, Lists.newArrayList(input1, output1, (double)processTime/(double)NCConfig.processor_time[10])));
		}

		@ZenMethod
		public static void removeRecipeWithInput(IIngredient input1) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.Type.INGOT_FORMER, IngredientSorption.INPUT, Lists.newArrayList(input1)));
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient output1) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.Type.INGOT_FORMER, IngredientSorption.OUTPUT, Lists.newArrayList(output1)));
		}
	}
	
	@ZenClass("mods.nuclearcraft.pressurizer")
	@ZenRegister
	public static class PressurizerHandler extends RecipeHandler {
		
		@ZenMethod
		public static void addRecipe(Object[] objects) {
			CraftTweakerAPI.apply(new AddProcessorRecipe(NCRecipes.Type.PRESSURIZER, Lists.newArrayList(objects)));
		}

		@ZenMethod
		public static void removeRecipeWithInput(IIngredient[] inputs) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.Type.PRESSURIZER, IngredientSorption.INPUT, Lists.newArrayList(inputs)));
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient[] outputs) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.Type.PRESSURIZER, IngredientSorption.OUTPUT, Lists.newArrayList(outputs)));
		}
		
		@ZenMethod
		public static void removeAllRecipes() {
			CraftTweakerAPI.apply(new RemoveAllProcessorRecipes(NCRecipes.Type.PRESSURIZER));
		}
		
		// OLD METHODS
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient output1) {
			CraftTweakerAPI.apply(new AddProcessorRecipe(NCRecipes.Type.PRESSURIZER, Lists.newArrayList(input1, output1, 1D)));
		}
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient output1, int processTime) {
			CraftTweakerAPI.apply(new AddProcessorRecipe(NCRecipes.Type.PRESSURIZER, Lists.newArrayList(input1, output1, (double)processTime/(double)NCConfig.processor_time[11])));
		}

		@ZenMethod
		public static void removeRecipeWithInput(IIngredient input1) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.Type.PRESSURIZER, IngredientSorption.INPUT, Lists.newArrayList(input1)));
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient output1) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.Type.PRESSURIZER, IngredientSorption.OUTPUT, Lists.newArrayList(output1)));
		}
	}
	
	@ZenClass("mods.nuclearcraft.chemical_reactor")
	@ZenRegister
	public static class ChemicalReactorHandler extends RecipeHandler {
		
		@ZenMethod
		public static void addRecipe(Object[] objects) {
			CraftTweakerAPI.apply(new AddProcessorRecipe(NCRecipes.Type.CHEMICAL_REACTOR, Lists.newArrayList(objects)));
		}

		@ZenMethod
		public static void removeRecipeWithInput(IIngredient[] inputs) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.Type.CHEMICAL_REACTOR, IngredientSorption.INPUT, Lists.newArrayList(inputs)));
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient[] outputs) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.Type.CHEMICAL_REACTOR, IngredientSorption.OUTPUT, Lists.newArrayList(outputs)));
		}
		
		@ZenMethod
		public static void removeAllRecipes() {
			CraftTweakerAPI.apply(new RemoveAllProcessorRecipes(NCRecipes.Type.CHEMICAL_REACTOR));
		}
		
		// OLD METHODS
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient input2, IIngredient output1, IIngredient output2) {
			CraftTweakerAPI.apply(new AddProcessorRecipe(NCRecipes.Type.CHEMICAL_REACTOR, Lists.newArrayList(input1, input2, output1, output2, 1D)));
		}
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient input2, IIngredient output1, IIngredient output2, int processTime) {
			CraftTweakerAPI.apply(new AddProcessorRecipe(NCRecipes.Type.CHEMICAL_REACTOR, Lists.newArrayList(input1, input2, output1, output2, (double)processTime/(double)NCConfig.processor_time[12])));
		}

		@ZenMethod
		public static void removeRecipeWithInput(IIngredient input1, IIngredient input2) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.Type.CHEMICAL_REACTOR, IngredientSorption.INPUT, Lists.newArrayList(input1, input2)));
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient output1, IIngredient output2) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.Type.CHEMICAL_REACTOR, IngredientSorption.OUTPUT, Lists.newArrayList(output1, output2)));
		}
	}
	
	@ZenClass("mods.nuclearcraft.salt_mixer")
	@ZenRegister
	public static class SaltMixerHandler extends RecipeHandler {
		
		@ZenMethod
		public static void addRecipe(Object[] objects) {
			CraftTweakerAPI.apply(new AddProcessorRecipe(NCRecipes.Type.SALT_MIXER, Lists.newArrayList(objects)));
		}

		@ZenMethod
		public static void removeRecipeWithInput(IIngredient[] inputs) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.Type.SALT_MIXER, IngredientSorption.INPUT, Lists.newArrayList(inputs)));
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient[] outputs) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.Type.SALT_MIXER, IngredientSorption.OUTPUT, Lists.newArrayList(outputs)));
		}
		
		@ZenMethod
		public static void removeAllRecipes() {
			CraftTweakerAPI.apply(new RemoveAllProcessorRecipes(NCRecipes.Type.SALT_MIXER));
		}
		
		// OLD METHODS
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient input2, IIngredient output1) {
			CraftTweakerAPI.apply(new AddProcessorRecipe(NCRecipes.Type.SALT_MIXER, Lists.newArrayList(input1, input2, output1, 1D)));
		}
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient input2, IIngredient output1, int processTime) {
			CraftTweakerAPI.apply(new AddProcessorRecipe(NCRecipes.Type.SALT_MIXER, Lists.newArrayList(input1, input2, output1, (double)processTime/(double)NCConfig.processor_time[13])));
		}

		@ZenMethod
		public static void removeRecipeWithInput(IIngredient input1, IIngredient input2) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.Type.SALT_MIXER, IngredientSorption.INPUT, Lists.newArrayList(input1, input2)));
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient output1) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.Type.SALT_MIXER, IngredientSorption.OUTPUT, Lists.newArrayList(output1)));
		}
	}
	
	@ZenClass("mods.nuclearcraft.crystallizer")
	@ZenRegister
	public static class CrystallizerHandler extends RecipeHandler {
		
		@ZenMethod
		public static void addRecipe(Object[] objects) {
			CraftTweakerAPI.apply(new AddProcessorRecipe(NCRecipes.Type.CRYSTALLIZER, Lists.newArrayList(objects)));
		}

		@ZenMethod
		public static void removeRecipeWithInput(IIngredient[] inputs) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.Type.CRYSTALLIZER, IngredientSorption.INPUT, Lists.newArrayList(inputs)));
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient[] outputs) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.Type.CRYSTALLIZER, IngredientSorption.OUTPUT, Lists.newArrayList(outputs)));
		}
		
		@ZenMethod
		public static void removeAllRecipes() {
			CraftTweakerAPI.apply(new RemoveAllProcessorRecipes(NCRecipes.Type.CRYSTALLIZER));
		}
		
		// OLD METHODS
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient output1) {
			CraftTweakerAPI.apply(new AddProcessorRecipe(NCRecipes.Type.CRYSTALLIZER, Lists.newArrayList(input1, output1, 1D)));
		}
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient output1, int processTime) {
			CraftTweakerAPI.apply(new AddProcessorRecipe(NCRecipes.Type.CRYSTALLIZER, Lists.newArrayList(input1, output1, (double)processTime/(double)NCConfig.processor_time[14])));
		}

		@ZenMethod
		public static void removeRecipeWithInput(IIngredient input1) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.Type.CRYSTALLIZER, IngredientSorption.INPUT, Lists.newArrayList(input1)));
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient output1) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.Type.CRYSTALLIZER, IngredientSorption.OUTPUT, Lists.newArrayList(output1)));
		}
	}
	
	@ZenClass("mods.nuclearcraft.dissolver")
	@ZenRegister
	public static class DissolverHandler extends RecipeHandler {
		
		@ZenMethod
		public static void addRecipe(Object[] objects) {
			CraftTweakerAPI.apply(new AddProcessorRecipe(NCRecipes.Type.DISSOLVER, Lists.newArrayList(objects)));
		}

		@ZenMethod
		public static void removeRecipeWithInput(IIngredient[] inputs) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.Type.DISSOLVER, IngredientSorption.INPUT, Lists.newArrayList(inputs)));
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient[] outputs) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.Type.DISSOLVER, IngredientSorption.OUTPUT, Lists.newArrayList(outputs)));
		}
		
		@ZenMethod
		public static void removeAllRecipes() {
			CraftTweakerAPI.apply(new RemoveAllProcessorRecipes(NCRecipes.Type.DISSOLVER));
		}
		
		// OLD METHODS
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient input2, IIngredient output1) {
			CraftTweakerAPI.apply(new AddProcessorRecipe(NCRecipes.Type.DISSOLVER, Lists.newArrayList(input1, input2, output1, 1D)));
		}
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient input2, IIngredient output1, int processTime) {
			CraftTweakerAPI.apply(new AddProcessorRecipe(NCRecipes.Type.DISSOLVER, Lists.newArrayList(input1, input2, output1, (double)processTime/(double)NCConfig.processor_time[15])));
		}

		@ZenMethod
		public static void removeRecipeWithInput(IIngredient input1, IIngredient input2) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.Type.DISSOLVER, IngredientSorption.INPUT, Lists.newArrayList(input1, input2)));
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient output1) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.Type.DISSOLVER, IngredientSorption.OUTPUT, Lists.newArrayList(output1)));
		}
	}
	
	@ZenClass("mods.nuclearcraft.extractor")
	@ZenRegister
	public static class ExtractorHandler extends RecipeHandler {
		
		@ZenMethod
		public static void addRecipe(Object[] objects) {
			CraftTweakerAPI.apply(new AddProcessorRecipe(NCRecipes.Type.EXTRACTOR, Lists.newArrayList(objects)));
		}

		@ZenMethod
		public static void removeRecipeWithInput(IIngredient[] inputs) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.Type.EXTRACTOR, IngredientSorption.INPUT, Lists.newArrayList(inputs)));
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient[] outputs) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.Type.EXTRACTOR, IngredientSorption.OUTPUT, Lists.newArrayList(outputs)));
		}
		
		@ZenMethod
		public static void removeAllRecipes() {
			CraftTweakerAPI.apply(new RemoveAllProcessorRecipes(NCRecipes.Type.EXTRACTOR));
		}
		
		// OLD METHODS
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient output1, IIngredient output2) {
			CraftTweakerAPI.apply(new AddProcessorRecipe(NCRecipes.Type.EXTRACTOR, Lists.newArrayList(input1, output1, output2, 1D)));
		}
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient output1, IIngredient output2, int processTime) {
			CraftTweakerAPI.apply(new AddProcessorRecipe(NCRecipes.Type.EXTRACTOR, Lists.newArrayList(input1, output1, output2, (double)processTime/(double)NCConfig.processor_time[16])));
		}

		@ZenMethod
		public static void removeRecipeWithInput(IIngredient input1) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.Type.EXTRACTOR, IngredientSorption.INPUT, Lists.newArrayList(input1)));
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient output1, IIngredient output2) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.Type.EXTRACTOR, IngredientSorption.OUTPUT, Lists.newArrayList(output1, output2)));
		}
	}
	
	@ZenClass("mods.nuclearcraft.centrifuge")
	@ZenRegister
	public static class CentrifugeHandler extends RecipeHandler {
		
		@ZenMethod
		public static void addRecipe(Object[] objects) {
			CraftTweakerAPI.apply(new AddProcessorRecipe(NCRecipes.Type.CENTRIFUGE, Lists.newArrayList(objects)));
		}

		@ZenMethod
		public static void removeRecipeWithInput(IIngredient[] inputs) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.Type.CENTRIFUGE, IngredientSorption.INPUT, Lists.newArrayList(inputs)));
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient[] outputs) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.Type.CENTRIFUGE, IngredientSorption.OUTPUT, Lists.newArrayList(outputs)));
		}
		
		@ZenMethod
		public static void removeAllRecipes() {
			CraftTweakerAPI.apply(new RemoveAllProcessorRecipes(NCRecipes.Type.CENTRIFUGE));
		}
		
		// OLD METHODS
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient output1, IIngredient output2, IIngredient output3, IIngredient output4) {
			CraftTweakerAPI.apply(new AddProcessorRecipe(NCRecipes.Type.CENTRIFUGE, Lists.newArrayList(input1, output1, output2, output3, output4, 1D)));
		}
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient output1, IIngredient output2, IIngredient output3, IIngredient output4, int processTime) {
			CraftTweakerAPI.apply(new AddProcessorRecipe(NCRecipes.Type.CENTRIFUGE, Lists.newArrayList(input1, output1, output2, output3, output4, (double)processTime/(double)NCConfig.processor_time[17])));
		}
		
		@ZenMethod
		public static void removeRecipeWithInput(IIngredient input1) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.Type.CENTRIFUGE, IngredientSorption.INPUT, Lists.newArrayList(input1)));
		}

		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient output1, IIngredient output2, IIngredient output3, IIngredient output4) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.Type.CENTRIFUGE, IngredientSorption.OUTPUT, Lists.newArrayList(output1, output2, output3, output4)));
		}
	}
	
	@ZenClass("mods.nuclearcraft.rock_crusher")
	@ZenRegister
	public static class RockCrusherHandler extends RecipeHandler {
		
		@ZenMethod
		public static void addRecipe(Object[] objects) {
			CraftTweakerAPI.apply(new AddProcessorRecipe(NCRecipes.Type.ROCK_CRUSHER, Lists.newArrayList(objects)));
		}

		@ZenMethod
		public static void removeRecipeWithInput(IIngredient[] inputs) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.Type.ROCK_CRUSHER, IngredientSorption.INPUT, Lists.newArrayList(inputs)));
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient[] outputs) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.Type.ROCK_CRUSHER, IngredientSorption.OUTPUT, Lists.newArrayList(outputs)));
		}
		
		@ZenMethod
		public static void removeAllRecipes() {
			CraftTweakerAPI.apply(new RemoveAllProcessorRecipes(NCRecipes.Type.ROCK_CRUSHER));
		}
		
		// OLD METHODS
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient output1, IIngredient output2, IIngredient output3) {
			CraftTweakerAPI.apply(new AddProcessorRecipe(NCRecipes.Type.ROCK_CRUSHER, Lists.newArrayList(input1, output1, output2, output3, 1D)));
		}
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient output1, IIngredient output2, IIngredient output3, int processTime) {
			CraftTweakerAPI.apply(new AddProcessorRecipe(NCRecipes.Type.ROCK_CRUSHER, Lists.newArrayList(input1, output1, output2, output3, (double)processTime/(double)NCConfig.processor_time[18])));
		}
		
		@ZenMethod
		public static void removeRecipeWithInput(IIngredient input1) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.Type.ROCK_CRUSHER, IngredientSorption.INPUT, Lists.newArrayList(input1)));
		}

		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient output1, IIngredient output2, IIngredient output3) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.Type.ROCK_CRUSHER, IngredientSorption.OUTPUT, Lists.newArrayList(output1, output2, output3)));
		}
	}
	
	@ZenClass("mods.nuclearcraft.decay_generator")
	@ZenRegister
	public static class DecayGeneratorHandler extends RecipeHandler {
		
		@ZenMethod
		public static void addRecipe(Object[] objects) {
			CraftTweakerAPI.apply(new AddProcessorRecipe(NCRecipes.Type.DECAY_GENERATOR, Lists.newArrayList(objects)));
		}

		@ZenMethod
		public static void removeRecipeWithInput(IIngredient[] inputs) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.Type.DECAY_GENERATOR, IngredientSorption.INPUT, Lists.newArrayList(inputs)));
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient[] outputs) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.Type.DECAY_GENERATOR, IngredientSorption.OUTPUT, Lists.newArrayList(outputs)));
		}
		
		@ZenMethod
		public static void removeAllRecipes() {
			CraftTweakerAPI.apply(new RemoveAllProcessorRecipes(NCRecipes.Type.DECAY_GENERATOR));
		}
		
		// OLD METHODS
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient output1, double lifetime, int power) {
			CraftTweakerAPI.apply(new AddProcessorRecipe(NCRecipes.Type.DECAY_GENERATOR, Lists.newArrayList(input1, output1, lifetime, (double)power)));
		}

		@ZenMethod
		public static void removeRecipeWithInput(IIngredient input1) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.Type.DECAY_GENERATOR, IngredientSorption.INPUT, Lists.newArrayList(input1)));
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient output1) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.Type.DECAY_GENERATOR, IngredientSorption.OUTPUT, Lists.newArrayList(output1)));
		}
	}
	
	@ZenClass("mods.nuclearcraft.fission")
	@ZenRegister
	public static class FissionHandler extends RecipeHandler {
		
		@ZenMethod
		public static void addRecipe(Object[] objects) {
			CraftTweakerAPI.apply(new AddProcessorRecipe(NCRecipes.Type.FISSION, Lists.newArrayList(objects)));
		}

		@ZenMethod
		public static void removeRecipeWithInput(IIngredient[] inputs) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.Type.FISSION, IngredientSorption.INPUT, Lists.newArrayList(inputs)));
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient[] outputs) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.Type.FISSION, IngredientSorption.OUTPUT, Lists.newArrayList(outputs)));
		}
		
		@ZenMethod
		public static void removeAllRecipes() {
			CraftTweakerAPI.apply(new RemoveAllProcessorRecipes(NCRecipes.Type.FISSION));
		}
		
		// OLD METHODS
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient output1, double fuelTime, double power, double heat, String name) {
			CraftTweakerAPI.apply(new AddProcessorRecipe(NCRecipes.Type.FISSION, Lists.newArrayList(input1, output1, fuelTime, power, heat, name)));
		}

		@ZenMethod
		public static void removeRecipeWithInput(IIngredient input1) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.Type.FISSION, IngredientSorption.INPUT, Lists.newArrayList(input1)));
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient output1) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.Type.FISSION, IngredientSorption.OUTPUT, Lists.newArrayList(output1)));
		}
	}
	
	@ZenClass("mods.nuclearcraft.fusion")
	@ZenRegister
	public static class FusionHandler extends RecipeHandler {
		
		@ZenMethod
		public static void addRecipe(Object[] objects) {
			CraftTweakerAPI.apply(new AddProcessorRecipe(NCRecipes.Type.FUSION, Lists.newArrayList(objects)));
		}

		@ZenMethod
		public static void removeRecipeWithInput(IIngredient[] inputs) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.Type.FUSION, IngredientSorption.INPUT, Lists.newArrayList(inputs)));
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient[] outputs) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.Type.FUSION, IngredientSorption.OUTPUT, Lists.newArrayList(outputs)));
		}
		
		@ZenMethod
		public static void removeAllRecipes() {
			CraftTweakerAPI.apply(new RemoveAllProcessorRecipes(NCRecipes.Type.FUSION));
		}
		
		// OLD METHODS
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient input2, IIngredient output1, IIngredient output2, IIngredient output3, IIngredient output4, double fuelTime, double power, double heatVar) {
			CraftTweakerAPI.apply(new AddProcessorRecipe(NCRecipes.Type.FUSION, Lists.newArrayList(input1, input2, output1, output2, output3, output4, fuelTime, power, heatVar)));
		}

		@ZenMethod
		public static void removeRecipeWithInput(IIngredient input1, IIngredient input2) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.Type.FUSION, IngredientSorption.INPUT, Lists.newArrayList(input1, input2)));
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient output1, IIngredient output2, IIngredient output3, IIngredient output4) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.Type.FUSION, IngredientSorption.OUTPUT, Lists.newArrayList(output1, output2, output3, output4)));
		}
	}
	
	@ZenClass("mods.nuclearcraft.salt_fission")
	@ZenRegister
	public static class SaltFissionHandler extends RecipeHandler {
		
		@ZenMethod
		public static void addRecipe(Object[] objects) {
			CraftTweakerAPI.apply(new AddProcessorRecipe(NCRecipes.Type.SALT_FISSION, Lists.newArrayList(objects)));
		}

		@ZenMethod
		public static void removeRecipeWithInput(IIngredient[] inputs) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.Type.SALT_FISSION, IngredientSorption.INPUT, Lists.newArrayList(inputs)));
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient[] outputs) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.Type.SALT_FISSION, IngredientSorption.OUTPUT, Lists.newArrayList(outputs)));
		}
		
		@ZenMethod
		public static void removeAllRecipes() {
			CraftTweakerAPI.apply(new RemoveAllProcessorRecipes(NCRecipes.Type.SALT_FISSION));
		}
		
		// OLD METHODS
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient output1, double fuelTime, double power) {
			CraftTweakerAPI.apply(new AddProcessorRecipe(NCRecipes.Type.SALT_FISSION, Lists.newArrayList(input1, output1, fuelTime, power)));
		}

		@ZenMethod
		public static void removeRecipeWithInput(IIngredient input1) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.Type.SALT_FISSION, IngredientSorption.INPUT, Lists.newArrayList(input1)));
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient output1) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.Type.SALT_FISSION, IngredientSorption.OUTPUT, Lists.newArrayList(output1)));
		}
	}
	
	@ZenClass("mods.nuclearcraft.coolant_heater")
	@ZenRegister
	public static class CoolantHeaterHandler extends RecipeHandler {
		
		@ZenMethod
		public static void addRecipe(Object[] objects) {
			CraftTweakerAPI.apply(new AddProcessorRecipe(NCRecipes.Type.COOLANT_HEATER, Lists.newArrayList(objects)));
		}

		@ZenMethod
		public static void removeRecipeWithInput(IIngredient[] inputs) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.Type.COOLANT_HEATER, IngredientSorption.INPUT, Lists.newArrayList(inputs)));
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient[] outputs) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.Type.COOLANT_HEATER, IngredientSorption.OUTPUT, Lists.newArrayList(outputs)));
		}
		
		@ZenMethod
		public static void removeAllRecipes() {
			CraftTweakerAPI.apply(new RemoveAllProcessorRecipes(NCRecipes.Type.COOLANT_HEATER));
		}
		
		// OLD METHODS
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient output1, double coolingRate) {
			CraftTweakerAPI.apply(new AddProcessorRecipe(NCRecipes.Type.COOLANT_HEATER, Lists.newArrayList(input1, output1, coolingRate)));
		}

		@ZenMethod
		public static void removeRecipeWithInput(IIngredient input1) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.Type.COOLANT_HEATER, IngredientSorption.INPUT, Lists.newArrayList(input1)));
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient output1) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.Type.COOLANT_HEATER, IngredientSorption.OUTPUT, Lists.newArrayList(output1)));
		}
	}
	
	@ZenClass("mods.nuclearcraft.heat_exchanger")
	@ZenRegister
	public static class HeatExchangerHandler extends RecipeHandler {
		
		@ZenMethod
		public static void addRecipe(Object[] objects) {
			CraftTweakerAPI.apply(new AddProcessorRecipe(NCRecipes.Type.HEAT_EXCHANGER, Lists.newArrayList(objects)));
		}

		@ZenMethod
		public static void removeRecipeWithInput(IIngredient[] inputs) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.Type.HEAT_EXCHANGER, IngredientSorption.INPUT, Lists.newArrayList(inputs)));
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient[] outputs) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.Type.HEAT_EXCHANGER, IngredientSorption.OUTPUT, Lists.newArrayList(outputs)));
		}
		
		@ZenMethod
		public static void removeAllRecipes() {
			CraftTweakerAPI.apply(new RemoveAllProcessorRecipes(NCRecipes.Type.HEAT_EXCHANGER));
		}
	}
	
	@ZenClass("mods.nuclearcraft.high_turbine")
	@ZenRegister
	public static class HighTurbineHandler extends RecipeHandler {
		
		@ZenMethod
		public static void addRecipe(Object[] objects) {
			CraftTweakerAPI.apply(new AddProcessorRecipe(NCRecipes.Type.HIGH_TURBINE, Lists.newArrayList(objects)));
		}

		@ZenMethod
		public static void removeRecipeWithInput(IIngredient[] inputs) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.Type.HIGH_TURBINE, IngredientSorption.INPUT, Lists.newArrayList(inputs)));
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient[] outputs) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.Type.HIGH_TURBINE, IngredientSorption.OUTPUT, Lists.newArrayList(outputs)));
		}
		
		@ZenMethod
		public static void removeAllRecipes() {
			CraftTweakerAPI.apply(new RemoveAllProcessorRecipes(NCRecipes.Type.HIGH_TURBINE));
		}
	}
	
	@ZenClass("mods.nuclearcraft.low_turbine")
	@ZenRegister
	public static class LowTurbineHandler extends RecipeHandler {
		
		@ZenMethod
		public static void addRecipe(Object[] objects) {
			CraftTweakerAPI.apply(new AddProcessorRecipe(NCRecipes.Type.LOW_TURBINE, Lists.newArrayList(objects)));
		}

		@ZenMethod
		public static void removeRecipeWithInput(IIngredient[] inputs) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.Type.LOW_TURBINE, IngredientSorption.INPUT, Lists.newArrayList(inputs)));
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient[] outputs) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.Type.LOW_TURBINE, IngredientSorption.OUTPUT, Lists.newArrayList(outputs)));
		}
		
		@ZenMethod
		public static void removeAllRecipes() {
			CraftTweakerAPI.apply(new RemoveAllProcessorRecipes(NCRecipes.Type.LOW_TURBINE));
		}
	}
	
	@ZenClass("mods.nuclearcraft.condenser")
	@ZenRegister
	public static class CondenserHandler extends RecipeHandler {
		
		@ZenMethod
		public static void addRecipe(Object[] objects) {
			CraftTweakerAPI.apply(new AddProcessorRecipe(NCRecipes.Type.CONDENSER, Lists.newArrayList(objects)));
		}

		@ZenMethod
		public static void removeRecipeWithInput(IIngredient[] inputs) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.Type.CONDENSER, IngredientSorption.INPUT, Lists.newArrayList(inputs)));
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient[] outputs) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.Type.CONDENSER, IngredientSorption.OUTPUT, Lists.newArrayList(outputs)));
		}
		
		@ZenMethod
		public static void removeAllRecipes() {
			CraftTweakerAPI.apply(new RemoveAllProcessorRecipes(NCRecipes.Type.CONDENSER));
		}
	}
}
