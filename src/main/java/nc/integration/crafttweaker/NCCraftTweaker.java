package nc.integration.crafttweaker;

import com.google.common.collect.Lists;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import nc.config.NCConfig;
import nc.recipe.NCRecipes;
import nc.recipe.SorptionType;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

public class NCCraftTweaker {
	
	public static void init() {
		CraftTweakerAPI.registerClass(ManufactoryHandler.class);
		CraftTweakerAPI.registerClass(IsotopeSeparatorHandler.class);
		CraftTweakerAPI.registerClass(DecayHastenerHandler.class);
		CraftTweakerAPI.registerClass(FuelReprocessorHandler.class);
		CraftTweakerAPI.registerClass(AlloyFurnaceHandler.class);
		CraftTweakerAPI.registerClass(InfuserHandler.class);
		CraftTweakerAPI.registerClass(MelterHandler.class);
		CraftTweakerAPI.registerClass(SupercoolerHandler.class);
		CraftTweakerAPI.registerClass(ElectrolyserHandler.class);
		CraftTweakerAPI.registerClass(IrradiatorHandler.class);
		CraftTweakerAPI.registerClass(IngotFormerHandler.class);
		CraftTweakerAPI.registerClass(PressurizerHandler.class);
		CraftTweakerAPI.registerClass(ChemicalReactorHandler.class);
		CraftTweakerAPI.registerClass(SaltMixerHandler.class);
		CraftTweakerAPI.registerClass(CrystallizerHandler.class);
		CraftTweakerAPI.registerClass(DissolverHandler.class);
		CraftTweakerAPI.registerClass(ExtractorHandler.class);
		CraftTweakerAPI.registerClass(FissionHandler.class);
		CraftTweakerAPI.registerClass(FusionHandler.class);
		CraftTweakerAPI.registerClass(SaltFissionHandler.class);
		CraftTweakerAPI.registerClass(CoolantHeaterHandler.class);
	}
	
	public static class RecipeHandler {
		static {
			NCRecipes.init();
		}
	}
	
	@ZenClass("mods.nuclearcraft.manufactory")
	@ZenRegister
	public static class ManufactoryHandler extends RecipeHandler {
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient output1) {
			CraftTweakerAPI.apply(new AddRecipe(NCRecipes.Type.MANUFACTORY, Lists.newArrayList(input1), Lists.newArrayList(output1), Lists.newArrayList(NCConfig.processor_time[0])));
		}
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient output1, int processTime) {
			CraftTweakerAPI.apply(new AddRecipe(NCRecipes.Type.MANUFACTORY, Lists.newArrayList(input1), Lists.newArrayList(output1), Lists.newArrayList(processTime)));
		}

		@ZenMethod
		public static void removeRecipeWithInput(IIngredient input1) {
			CraftTweakerAPI.apply(new RemoveRecipe(NCRecipes.Type.MANUFACTORY, SorptionType.INPUT, Lists.newArrayList(input1)));
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient output1) {
			CraftTweakerAPI.apply(new RemoveRecipe(NCRecipes.Type.MANUFACTORY, SorptionType.OUTPUT, Lists.newArrayList(output1)));
		}
	}
	
	@ZenClass("mods.nuclearcraft.isotope_separator")
	@ZenRegister
	public static class IsotopeSeparatorHandler extends RecipeHandler {
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient output1, IIngredient output2) {
			CraftTweakerAPI.apply(new AddRecipe(NCRecipes.Type.ISOTOPE_SEPARATOR, Lists.newArrayList(input1), Lists.newArrayList(output1, output2), Lists.newArrayList(NCConfig.processor_time[1])));
		}
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient output1, IIngredient output2, int processTime) {
			CraftTweakerAPI.apply(new AddRecipe(NCRecipes.Type.ISOTOPE_SEPARATOR, Lists.newArrayList(input1), Lists.newArrayList(output1, output2), Lists.newArrayList(processTime)));
		}
		
		@ZenMethod
		public static void removeRecipeWithInput(IIngredient input1) {
			CraftTweakerAPI.apply(new RemoveRecipe(NCRecipes.Type.ISOTOPE_SEPARATOR, SorptionType.INPUT, Lists.newArrayList(input1)));
		}

		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient output1, IIngredient output2) {
			CraftTweakerAPI.apply(new RemoveRecipe(NCRecipes.Type.ISOTOPE_SEPARATOR, SorptionType.OUTPUT, Lists.newArrayList(output1, output2)));
		}
	}
	
	@ZenClass("mods.nuclearcraft.decay_hastener")
	@ZenRegister
	public static class DecayHastenerHandler extends RecipeHandler {
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient output1) {
			CraftTweakerAPI.apply(new AddRecipe(NCRecipes.Type.DECAY_HASTENER, Lists.newArrayList(input1), Lists.newArrayList(output1), Lists.newArrayList(NCConfig.processor_time[2])));
		}
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient output1, int processTime) {
			CraftTweakerAPI.apply(new AddRecipe(NCRecipes.Type.DECAY_HASTENER, Lists.newArrayList(input1), Lists.newArrayList(output1), Lists.newArrayList(processTime)));
		}

		@ZenMethod
		public static void removeRecipeWithInput(IIngredient input1) {
			CraftTweakerAPI.apply(new RemoveRecipe(NCRecipes.Type.DECAY_HASTENER, SorptionType.INPUT, Lists.newArrayList(input1)));
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient output1) {
			CraftTweakerAPI.apply(new RemoveRecipe(NCRecipes.Type.DECAY_HASTENER, SorptionType.OUTPUT, Lists.newArrayList(output1)));
		}
	}
	
	@ZenClass("mods.nuclearcraft.fuel_reprocessor")
	@ZenRegister
	public static class FuelReprocessorHandler extends RecipeHandler {
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient output1, IIngredient output2, IIngredient output3, IIngredient output4) {
			CraftTweakerAPI.apply(new AddRecipe(NCRecipes.Type.FUEL_REPROCESSOR, Lists.newArrayList(input1), Lists.newArrayList(output1, output2, output3, output4), Lists.newArrayList(NCConfig.processor_time[3])));
		}
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient output1, IIngredient output2, IIngredient output3, IIngredient output4, int processTime) {
			CraftTweakerAPI.apply(new AddRecipe(NCRecipes.Type.FUEL_REPROCESSOR, Lists.newArrayList(input1), Lists.newArrayList(output1, output2, output3, output4), Lists.newArrayList(processTime)));
		}

		@ZenMethod
		public static void removeRecipeWithInput(IIngredient input1) {
			CraftTweakerAPI.apply(new RemoveRecipe(NCRecipes.Type.FUEL_REPROCESSOR, SorptionType.INPUT, Lists.newArrayList(input1)));
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient output1, IIngredient output2, IIngredient output3, IIngredient output4) {
			CraftTweakerAPI.apply(new RemoveRecipe(NCRecipes.Type.FUEL_REPROCESSOR, SorptionType.OUTPUT, Lists.newArrayList(output1, output2, output3, output4)));
		}
	}
	
	@ZenClass("mods.nuclearcraft.alloy_furnace")
	@ZenRegister
	public static class AlloyFurnaceHandler extends RecipeHandler {
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient input2, IIngredient output1) {
			CraftTweakerAPI.apply(new AddRecipe(NCRecipes.Type.ALLOY_FURNACE, Lists.newArrayList(input1, input2), Lists.newArrayList(output1), Lists.newArrayList(NCConfig.processor_time[4])));
		}
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient input2, IIngredient output1, int processTime) {
			CraftTweakerAPI.apply(new AddRecipe(NCRecipes.Type.ALLOY_FURNACE, Lists.newArrayList(input1, input2), Lists.newArrayList(output1), Lists.newArrayList(processTime)));
		}

		@ZenMethod
		public static void removeRecipeWithInput(IIngredient input1, IIngredient input2) {
			CraftTweakerAPI.apply(new RemoveRecipe(NCRecipes.Type.ALLOY_FURNACE, SorptionType.INPUT, Lists.newArrayList(input1, input2)));
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient output1) {
			CraftTweakerAPI.apply(new RemoveRecipe(NCRecipes.Type.ALLOY_FURNACE, SorptionType.OUTPUT, Lists.newArrayList(output1)));
		}
	}
	
	@ZenClass("mods.nuclearcraft.infuser")
	@ZenRegister
	public static class InfuserHandler extends RecipeHandler {
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient input2, IIngredient output1) {
			CraftTweakerAPI.apply(new AddRecipe(NCRecipes.Type.INFUSER, Lists.newArrayList(input1, input2), Lists.newArrayList(output1), Lists.newArrayList(NCConfig.processor_time[5])));
		}
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient input2, IIngredient output1, int processTime) {
			CraftTweakerAPI.apply(new AddRecipe(NCRecipes.Type.INFUSER, Lists.newArrayList(input1, input2), Lists.newArrayList(output1), Lists.newArrayList(processTime)));
		}

		@ZenMethod
		public static void removeRecipeWithInput(IIngredient input1, IIngredient input2) {
			CraftTweakerAPI.apply(new RemoveRecipe(NCRecipes.Type.INFUSER, SorptionType.INPUT, Lists.newArrayList(input1, input2)));
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient output1) {
			CraftTweakerAPI.apply(new RemoveRecipe(NCRecipes.Type.INFUSER, SorptionType.OUTPUT, Lists.newArrayList(output1)));
		}
	}
	
	@ZenClass("mods.nuclearcraft.melter")
	@ZenRegister
	public static class MelterHandler extends RecipeHandler {
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient output1) {
			CraftTweakerAPI.apply(new AddRecipe(NCRecipes.Type.MELTER, Lists.newArrayList(input1), Lists.newArrayList(output1), Lists.newArrayList(NCConfig.processor_time[6])));
		}
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient output1, int processTime) {
			CraftTweakerAPI.apply(new AddRecipe(NCRecipes.Type.MELTER, Lists.newArrayList(input1), Lists.newArrayList(output1), Lists.newArrayList(processTime)));
		}

		@ZenMethod
		public static void removeRecipeWithInput(IIngredient input1) {
			CraftTweakerAPI.apply(new RemoveRecipe(NCRecipes.Type.MELTER, SorptionType.INPUT, Lists.newArrayList(input1)));
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient output1) {
			CraftTweakerAPI.apply(new RemoveRecipe(NCRecipes.Type.MELTER, SorptionType.OUTPUT, Lists.newArrayList(output1)));
		}
	}
	
	@ZenClass("mods.nuclearcraft.supercooler")
	@ZenRegister
	public static class SupercoolerHandler extends RecipeHandler {
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient output1) {
			CraftTweakerAPI.apply(new AddRecipe(NCRecipes.Type.SUPERCOOLER, Lists.newArrayList(input1), Lists.newArrayList(output1), Lists.newArrayList(NCConfig.processor_time[7])));
		}
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient output1, int processTime) {
			CraftTweakerAPI.apply(new AddRecipe(NCRecipes.Type.SUPERCOOLER, Lists.newArrayList(input1), Lists.newArrayList(output1), Lists.newArrayList(processTime)));
		}

		@ZenMethod
		public static void removeRecipeWithInput(IIngredient input1) {
			CraftTweakerAPI.apply(new RemoveRecipe(NCRecipes.Type.SUPERCOOLER, SorptionType.INPUT, Lists.newArrayList(input1)));
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient output1) {
			CraftTweakerAPI.apply(new RemoveRecipe(NCRecipes.Type.SUPERCOOLER, SorptionType.OUTPUT, Lists.newArrayList(output1)));
		}
	}
	
	@ZenClass("mods.nuclearcraft.electrolyser")
	@ZenRegister
	public static class ElectrolyserHandler extends RecipeHandler {
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient output1, IIngredient output2, IIngredient output3, IIngredient output4) {
			CraftTweakerAPI.apply(new AddRecipe(NCRecipes.Type.ELECTROLYSER, Lists.newArrayList(input1), Lists.newArrayList(output1, output2, output3, output4), Lists.newArrayList(NCConfig.processor_time[8])));
		}
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient output1, IIngredient output2, IIngredient output3, IIngredient output4, int processTime) {
			CraftTweakerAPI.apply(new AddRecipe(NCRecipes.Type.ELECTROLYSER, Lists.newArrayList(input1), Lists.newArrayList(output1, output2, output3, output4), Lists.newArrayList(processTime)));
		}
		
		@ZenMethod
		public static void removeRecipeWithInput(IIngredient input1) {
			CraftTweakerAPI.apply(new RemoveRecipe(NCRecipes.Type.ELECTROLYSER, SorptionType.INPUT, Lists.newArrayList(input1)));
		}

		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient output1, IIngredient output2, IIngredient output3, IIngredient output4) {
			CraftTweakerAPI.apply(new RemoveRecipe(NCRecipes.Type.ELECTROLYSER, SorptionType.OUTPUT, Lists.newArrayList(output1, output2, output3, output4)));
		}
	}
	
	@ZenClass("mods.nuclearcraft.irradiator")
	@ZenRegister
	public static class IrradiatorHandler extends RecipeHandler {
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient input2, IIngredient output1, IIngredient output2) {
			CraftTweakerAPI.apply(new AddRecipe(NCRecipes.Type.IRRADIATOR, Lists.newArrayList(input1, input2), Lists.newArrayList(output1, output2), Lists.newArrayList(NCConfig.processor_time[9])));
		}
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient input2, IIngredient output1, IIngredient output2, int processTime) {
			CraftTweakerAPI.apply(new AddRecipe(NCRecipes.Type.IRRADIATOR, Lists.newArrayList(input1, input2), Lists.newArrayList(output1, output2), Lists.newArrayList(processTime)));
		}

		@ZenMethod
		public static void removeRecipeWithInput(IIngredient input1, IIngredient input2) {
			CraftTweakerAPI.apply(new RemoveRecipe(NCRecipes.Type.IRRADIATOR, SorptionType.INPUT, Lists.newArrayList(input1, input2)));
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient output1, IIngredient output2) {
			CraftTweakerAPI.apply(new RemoveRecipe(NCRecipes.Type.IRRADIATOR, SorptionType.OUTPUT, Lists.newArrayList(output1, output2)));
		}
	}
	
	@ZenClass("mods.nuclearcraft.ingot_former")
	@ZenRegister
	public static class IngotFormerHandler extends RecipeHandler {
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient output1) {
			CraftTweakerAPI.apply(new AddRecipe(NCRecipes.Type.INGOT_FORMER, Lists.newArrayList(input1), Lists.newArrayList(output1), Lists.newArrayList(NCConfig.processor_time[10])));
		}
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient output1, int processTime) {
			CraftTweakerAPI.apply(new AddRecipe(NCRecipes.Type.INGOT_FORMER, Lists.newArrayList(input1), Lists.newArrayList(output1), Lists.newArrayList(processTime)));
		}

		@ZenMethod
		public static void removeRecipeWithInput(IIngredient input1) {
			CraftTweakerAPI.apply(new RemoveRecipe(NCRecipes.Type.INGOT_FORMER, SorptionType.INPUT, Lists.newArrayList(input1)));
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient output1) {
			CraftTweakerAPI.apply(new RemoveRecipe(NCRecipes.Type.INGOT_FORMER, SorptionType.OUTPUT, Lists.newArrayList(output1)));
		}
	}
	
	@ZenClass("mods.nuclearcraft.pressurizer")
	@ZenRegister
	public static class PressurizerHandler extends RecipeHandler {
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient output1) {
			CraftTweakerAPI.apply(new AddRecipe(NCRecipes.Type.PRESSURIZER, Lists.newArrayList(input1), Lists.newArrayList(output1), Lists.newArrayList(NCConfig.processor_time[11])));
		}
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient output1, int processTime) {
			CraftTweakerAPI.apply(new AddRecipe(NCRecipes.Type.PRESSURIZER, Lists.newArrayList(input1), Lists.newArrayList(output1), Lists.newArrayList(processTime)));
		}

		@ZenMethod
		public static void removeRecipeWithInput(IIngredient input1) {
			CraftTweakerAPI.apply(new RemoveRecipe(NCRecipes.Type.PRESSURIZER, SorptionType.INPUT, Lists.newArrayList(input1)));
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient output1) {
			CraftTweakerAPI.apply(new RemoveRecipe(NCRecipes.Type.PRESSURIZER, SorptionType.OUTPUT, Lists.newArrayList(output1)));
		}
	}
	
	@ZenClass("mods.nuclearcraft.chemical_reactor")
	@ZenRegister
	public static class ChemicalReactorHandler extends RecipeHandler {
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient input2, IIngredient output1, IIngredient output2) {
			CraftTweakerAPI.apply(new AddRecipe(NCRecipes.Type.CHEMICAL_REACTOR, Lists.newArrayList(input1, input2), Lists.newArrayList(output1, output2), Lists.newArrayList(NCConfig.processor_time[12])));
		}
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient input2, IIngredient output1, IIngredient output2, int processTime) {
			CraftTweakerAPI.apply(new AddRecipe(NCRecipes.Type.CHEMICAL_REACTOR, Lists.newArrayList(input1, input2), Lists.newArrayList(output1, output2), Lists.newArrayList(processTime)));
		}

		@ZenMethod
		public static void removeRecipeWithInput(IIngredient input1, IIngredient input2) {
			CraftTweakerAPI.apply(new RemoveRecipe(NCRecipes.Type.CHEMICAL_REACTOR, SorptionType.INPUT, Lists.newArrayList(input1, input2)));
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient output1, IIngredient output2) {
			CraftTweakerAPI.apply(new RemoveRecipe(NCRecipes.Type.CHEMICAL_REACTOR, SorptionType.OUTPUT, Lists.newArrayList(output1, output2)));
		}
	}
	
	@ZenClass("mods.nuclearcraft.salt_mixer")
	@ZenRegister
	public static class SaltMixerHandler extends RecipeHandler {
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient input2, IIngredient output1) {
			CraftTweakerAPI.apply(new AddRecipe(NCRecipes.Type.SALT_MIXER, Lists.newArrayList(input1, input2), Lists.newArrayList(output1), Lists.newArrayList(NCConfig.processor_time[13])));
		}
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient input2, IIngredient output1, int processTime) {
			CraftTweakerAPI.apply(new AddRecipe(NCRecipes.Type.SALT_MIXER, Lists.newArrayList(input1, input2), Lists.newArrayList(output1), Lists.newArrayList(processTime)));
		}

		@ZenMethod
		public static void removeRecipeWithInput(IIngredient input1, IIngredient input2) {
			CraftTweakerAPI.apply(new RemoveRecipe(NCRecipes.Type.SALT_MIXER, SorptionType.INPUT, Lists.newArrayList(input1, input2)));
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient output1) {
			CraftTweakerAPI.apply(new RemoveRecipe(NCRecipes.Type.SALT_MIXER, SorptionType.OUTPUT, Lists.newArrayList(output1)));
		}
	}
	
	@ZenClass("mods.nuclearcraft.crystallizer")
	@ZenRegister
	public static class CrystallizerHandler extends RecipeHandler {
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient output1) {
			CraftTweakerAPI.apply(new AddRecipe(NCRecipes.Type.CRYSTALLIZER, Lists.newArrayList(input1), Lists.newArrayList(output1), Lists.newArrayList(NCConfig.processor_time[14])));
		}
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient output1, int processTime) {
			CraftTweakerAPI.apply(new AddRecipe(NCRecipes.Type.CRYSTALLIZER, Lists.newArrayList(input1), Lists.newArrayList(output1), Lists.newArrayList(processTime)));
		}

		@ZenMethod
		public static void removeRecipeWithInput(IIngredient input1) {
			CraftTweakerAPI.apply(new RemoveRecipe(NCRecipes.Type.CRYSTALLIZER, SorptionType.INPUT, Lists.newArrayList(input1)));
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient output1) {
			CraftTweakerAPI.apply(new RemoveRecipe(NCRecipes.Type.CRYSTALLIZER, SorptionType.OUTPUT, Lists.newArrayList(output1)));
		}
	}
	
	@ZenClass("mods.nuclearcraft.dissolver")
	@ZenRegister
	public static class DissolverHandler extends RecipeHandler {
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient input2, IIngredient output1) {
			CraftTweakerAPI.apply(new AddRecipe(NCRecipes.Type.DISSOLVER, Lists.newArrayList(input1, input2), Lists.newArrayList(output1), Lists.newArrayList(NCConfig.processor_time[15])));
		}
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient input2, IIngredient output1, int processTime) {
			CraftTweakerAPI.apply(new AddRecipe(NCRecipes.Type.DISSOLVER, Lists.newArrayList(input1, input2), Lists.newArrayList(output1), Lists.newArrayList(processTime)));
		}

		@ZenMethod
		public static void removeRecipeWithInput(IIngredient input1, IIngredient input2) {
			CraftTweakerAPI.apply(new RemoveRecipe(NCRecipes.Type.DISSOLVER, SorptionType.INPUT, Lists.newArrayList(input1, input2)));
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient output1) {
			CraftTweakerAPI.apply(new RemoveRecipe(NCRecipes.Type.DISSOLVER, SorptionType.OUTPUT, Lists.newArrayList(output1)));
		}
	}
	
	@ZenClass("mods.nuclearcraft.extractor")
	@ZenRegister
	public static class ExtractorHandler extends RecipeHandler {
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient input2, IIngredient output1) {
			CraftTweakerAPI.apply(new AddRecipe(NCRecipes.Type.EXTRACTOR, Lists.newArrayList(input1, input2), Lists.newArrayList(output1), Lists.newArrayList(NCConfig.processor_time[16])));
		}
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient input2, IIngredient output1, int processTime) {
			CraftTweakerAPI.apply(new AddRecipe(NCRecipes.Type.EXTRACTOR, Lists.newArrayList(input1, input2), Lists.newArrayList(output1), Lists.newArrayList(processTime)));
		}

		@ZenMethod
		public static void removeRecipeWithInput(IIngredient input1, IIngredient input2) {
			CraftTweakerAPI.apply(new RemoveRecipe(NCRecipes.Type.EXTRACTOR, SorptionType.INPUT, Lists.newArrayList(input1, input2)));
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient output1) {
			CraftTweakerAPI.apply(new RemoveRecipe(NCRecipes.Type.EXTRACTOR, SorptionType.OUTPUT, Lists.newArrayList(output1)));
		}
	}
	
	@ZenClass("mods.nuclearcraft.fission")
	@ZenRegister
	public static class FissionHandler extends RecipeHandler {
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient output1, double fuelTime, double power, double heat, String name) {
			CraftTweakerAPI.apply(new AddRecipe(NCRecipes.Type.FISSION, Lists.newArrayList(input1), Lists.newArrayList(output1), Lists.newArrayList(fuelTime, power, heat, name)));
		}

		@ZenMethod
		public static void removeRecipeWithInput(IIngredient input1) {
			CraftTweakerAPI.apply(new RemoveRecipe(NCRecipes.Type.FISSION, SorptionType.INPUT, Lists.newArrayList(input1)));
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient output1) {
			CraftTweakerAPI.apply(new RemoveRecipe(NCRecipes.Type.FISSION, SorptionType.OUTPUT, Lists.newArrayList(output1)));
		}
	}
	
	@ZenClass("mods.nuclearcraft.fusion")
	@ZenRegister
	public static class FusionHandler extends RecipeHandler {
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient input2, IIngredient output1, IIngredient output2, IIngredient output3, IIngredient output4, double fuelTime, double power, double heatVar) {
			CraftTweakerAPI.apply(new AddRecipe(NCRecipes.Type.FUSION, Lists.newArrayList(input1, input2), Lists.newArrayList(output1, output2, output3, output4), Lists.newArrayList(fuelTime, power, heatVar)));
		}

		@ZenMethod
		public static void removeRecipeWithInput(IIngredient input1, IIngredient input2) {
			CraftTweakerAPI.apply(new RemoveRecipe(NCRecipes.Type.FUSION, SorptionType.INPUT, Lists.newArrayList(input1, input2)));
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient output1, IIngredient output2, IIngredient output3, IIngredient output4) {
			CraftTweakerAPI.apply(new RemoveRecipe(NCRecipes.Type.FUSION, SorptionType.OUTPUT, Lists.newArrayList(output1, output2, output3, output4)));
		}
	}
	
	@ZenClass("mods.nuclearcraft.salt_fission")
	@ZenRegister
	public static class SaltFissionHandler extends RecipeHandler {
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient output1, double fuelTime, double power) {
			CraftTweakerAPI.apply(new AddRecipe(NCRecipes.Type.SALT_FISSION, Lists.newArrayList(input1), Lists.newArrayList(output1), Lists.newArrayList(fuelTime, power)));
		}

		@ZenMethod
		public static void removeRecipeWithInput(IIngredient input1) {
			CraftTweakerAPI.apply(new RemoveRecipe(NCRecipes.Type.SALT_FISSION, SorptionType.INPUT, Lists.newArrayList(input1)));
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient output1) {
			CraftTweakerAPI.apply(new RemoveRecipe(NCRecipes.Type.SALT_FISSION, SorptionType.OUTPUT, Lists.newArrayList(output1)));
		}
	}
	
	@ZenClass("mods.nuclearcraft.coolant_heater")
	@ZenRegister
	public static class CoolantHeaterHandler extends RecipeHandler {
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient output1) {
			CraftTweakerAPI.apply(new AddRecipe(NCRecipes.Type.COOLANT_HEATER, Lists.newArrayList(input1), Lists.newArrayList(output1), Lists.newArrayList()));
		}

		@ZenMethod
		public static void removeRecipeWithInput(IIngredient input1) {
			CraftTweakerAPI.apply(new RemoveRecipe(NCRecipes.Type.COOLANT_HEATER, SorptionType.INPUT, Lists.newArrayList(input1)));
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient output1) {
			CraftTweakerAPI.apply(new RemoveRecipe(NCRecipes.Type.COOLANT_HEATER, SorptionType.OUTPUT, Lists.newArrayList(output1)));
		}
	}
}
