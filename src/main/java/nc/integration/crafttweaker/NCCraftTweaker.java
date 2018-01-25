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
	}
	
	@ZenClass("mods.nuclearcraft.manufactory")
	@ZenRegister
	public static class ManufactoryHandler {
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient output1) {
			CraftTweakerAPI.apply(new AddRecipe(NCRecipes.MANUFACTORY_RECIPES, Lists.newArrayList(input1), Lists.newArrayList(output1), Lists.newArrayList(NCConfig.processor_time[0])));
		}
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient output1, int processTime) {
			CraftTweakerAPI.apply(new AddRecipe(NCRecipes.MANUFACTORY_RECIPES, Lists.newArrayList(input1), Lists.newArrayList(output1), Lists.newArrayList(processTime)));
		}

		@ZenMethod
		public static void removeRecipe(IIngredient output1) {
			CraftTweakerAPI.apply(new RemoveRecipe(NCRecipes.MANUFACTORY_RECIPES, SorptionType.OUTPUT, Lists.newArrayList(output1)));
		}
	}
	
	@ZenClass("mods.nuclearcraft.isotope_separator")
	@ZenRegister
	public static class IsotopeSeparatorHandler {
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient output1, IIngredient output2) {
			CraftTweakerAPI.apply(new AddRecipe(NCRecipes.ISOTOPE_SEPARATOR_RECIPES, Lists.newArrayList(input1), Lists.newArrayList(output1, output2), Lists.newArrayList(NCConfig.processor_time[1])));
		}
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient output1, IIngredient output2, int processTime) {
			CraftTweakerAPI.apply(new AddRecipe(NCRecipes.ISOTOPE_SEPARATOR_RECIPES, Lists.newArrayList(input1), Lists.newArrayList(output1, output2), Lists.newArrayList(processTime)));
		}

		@ZenMethod
		public static void removeRecipe(IIngredient output1, IIngredient output2) {
			CraftTweakerAPI.apply(new RemoveRecipe(NCRecipes.ISOTOPE_SEPARATOR_RECIPES, SorptionType.OUTPUT, Lists.newArrayList(output1, output2)));
		}
	}
	
	@ZenClass("mods.nuclearcraft.decay_hastener")
	@ZenRegister
	public static class DecayHastenerHandler {
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient output1) {
			CraftTweakerAPI.apply(new AddRecipe(NCRecipes.DECAY_HASTENER_RECIPES, Lists.newArrayList(input1), Lists.newArrayList(output1), Lists.newArrayList(NCConfig.processor_time[2])));
		}
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient output1, int processTime) {
			CraftTweakerAPI.apply(new AddRecipe(NCRecipes.DECAY_HASTENER_RECIPES, Lists.newArrayList(input1), Lists.newArrayList(output1), Lists.newArrayList(processTime)));
		}

		@ZenMethod
		public static void removeRecipe(IIngredient output1) {
			CraftTweakerAPI.apply(new RemoveRecipe(NCRecipes.DECAY_HASTENER_RECIPES, SorptionType.OUTPUT, Lists.newArrayList(output1)));
		}
	}
	
	@ZenClass("mods.nuclearcraft.fuel_reprocessor")
	@ZenRegister
	public static class FuelReprocessorHandler {
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient output1, IIngredient output2, IIngredient output3, IIngredient output4) {
			CraftTweakerAPI.apply(new AddRecipe(NCRecipes.FUEL_REPROCESSOR_RECIPES, Lists.newArrayList(input1), Lists.newArrayList(output1, output2, output3, output4), Lists.newArrayList(NCConfig.processor_time[3])));
		}
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient output1, IIngredient output2, IIngredient output3, IIngredient output4, int processTime) {
			CraftTweakerAPI.apply(new AddRecipe(NCRecipes.FUEL_REPROCESSOR_RECIPES, Lists.newArrayList(input1), Lists.newArrayList(output1, output2, output3, output4), Lists.newArrayList(processTime)));
		}

		@ZenMethod
		public static void removeRecipe(IIngredient output1, IIngredient output2, IIngredient output3, IIngredient output4) {
			CraftTweakerAPI.apply(new RemoveRecipe(NCRecipes.FUEL_REPROCESSOR_RECIPES, SorptionType.OUTPUT, Lists.newArrayList(output1, output2, output3, output4)));
		}
	}
	
	@ZenClass("mods.nuclearcraft.alloy_furnace")
	@ZenRegister
	public static class AlloyFurnaceHandler {
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient input2, IIngredient output1) {
			CraftTweakerAPI.apply(new AddRecipe(NCRecipes.ALLOY_FURNACE_RECIPES, Lists.newArrayList(input1, input2), Lists.newArrayList(output1), Lists.newArrayList(NCConfig.processor_time[4])));
		}
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient input2, IIngredient output1, int processTime) {
			CraftTweakerAPI.apply(new AddRecipe(NCRecipes.ALLOY_FURNACE_RECIPES, Lists.newArrayList(input1, input2), Lists.newArrayList(output1), Lists.newArrayList(processTime)));
		}

		@ZenMethod
		public static void removeRecipe(IIngredient output1) {
			CraftTweakerAPI.apply(new RemoveRecipe(NCRecipes.ALLOY_FURNACE_RECIPES, SorptionType.OUTPUT, Lists.newArrayList(output1)));
		}
	}
	
	@ZenClass("mods.nuclearcraft.infuser")
	@ZenRegister
	public static class InfuserHandler {
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient input2, IIngredient output1) {
			CraftTweakerAPI.apply(new AddRecipe(NCRecipes.INFUSER_RECIPES, Lists.newArrayList(input1, input2), Lists.newArrayList(output1), Lists.newArrayList(NCConfig.processor_time[5])));
		}
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient input2, IIngredient output1, int processTime) {
			CraftTweakerAPI.apply(new AddRecipe(NCRecipes.INFUSER_RECIPES, Lists.newArrayList(input1, input2), Lists.newArrayList(output1), Lists.newArrayList(processTime)));
		}

		@ZenMethod
		public static void removeRecipe(IIngredient output1) {
			CraftTweakerAPI.apply(new RemoveRecipe(NCRecipes.INFUSER_RECIPES, SorptionType.OUTPUT, Lists.newArrayList(output1)));
		}
	}
	
	@ZenClass("mods.nuclearcraft.melter")
	@ZenRegister
	public static class MelterHandler {
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient output1) {
			CraftTweakerAPI.apply(new AddRecipe(NCRecipes.MELTER_RECIPES, Lists.newArrayList(input1), Lists.newArrayList(output1), Lists.newArrayList(NCConfig.processor_time[6])));
		}
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient output1, int processTime) {
			CraftTweakerAPI.apply(new AddRecipe(NCRecipes.MELTER_RECIPES, Lists.newArrayList(input1), Lists.newArrayList(output1), Lists.newArrayList(processTime)));
		}

		@ZenMethod
		public static void removeRecipe(IIngredient output1) {
			CraftTweakerAPI.apply(new RemoveRecipe(NCRecipes.MELTER_RECIPES, SorptionType.OUTPUT, Lists.newArrayList(output1)));
		}
	}
	
	@ZenClass("mods.nuclearcraft.supercooler")
	@ZenRegister
	public static class SupercoolerHandler {
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient output1) {
			CraftTweakerAPI.apply(new AddRecipe(NCRecipes.SUPERCOOLER_RECIPES, Lists.newArrayList(input1), Lists.newArrayList(output1), Lists.newArrayList(NCConfig.processor_time[7])));
		}
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient output1, int processTime) {
			CraftTweakerAPI.apply(new AddRecipe(NCRecipes.SUPERCOOLER_RECIPES, Lists.newArrayList(input1), Lists.newArrayList(output1), Lists.newArrayList(processTime)));
		}

		@ZenMethod
		public static void removeRecipe(IIngredient output1) {
			CraftTweakerAPI.apply(new RemoveRecipe(NCRecipes.SUPERCOOLER_RECIPES, SorptionType.OUTPUT, Lists.newArrayList(output1)));
		}
	}
	
	@ZenClass("mods.nuclearcraft.electrolyser")
	@ZenRegister
	public static class ElectrolyserHandler {
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient output1, IIngredient output2, IIngredient output3, IIngredient output4) {
			CraftTweakerAPI.apply(new AddRecipe(NCRecipes.ELECTROLYSER_RECIPES, Lists.newArrayList(input1), Lists.newArrayList(output1, output2, output3, output4), Lists.newArrayList(NCConfig.processor_time[8])));
		}
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient output1, IIngredient output2, IIngredient output3, IIngredient output4, int processTime) {
			CraftTweakerAPI.apply(new AddRecipe(NCRecipes.ELECTROLYSER_RECIPES, Lists.newArrayList(input1), Lists.newArrayList(output1, output2, output3, output4), Lists.newArrayList(processTime)));
		}

		@ZenMethod
		public static void removeRecipe(IIngredient output1, IIngredient output2, IIngredient output3, IIngredient output4) {
			CraftTweakerAPI.apply(new RemoveRecipe(NCRecipes.ELECTROLYSER_RECIPES, SorptionType.OUTPUT, Lists.newArrayList(output1, output2, output3, output4)));
		}
	}
	
	@ZenClass("mods.nuclearcraft.irradiator")
	@ZenRegister
	public static class IrradiatorHandler {
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient input2, IIngredient output1, IIngredient output2) {
			CraftTweakerAPI.apply(new AddRecipe(NCRecipes.IRRADIATOR_RECIPES, Lists.newArrayList(input1, input2), Lists.newArrayList(output1, output2), Lists.newArrayList(NCConfig.processor_time[9])));
		}
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient input2, IIngredient output1, IIngredient output2, int processTime) {
			CraftTweakerAPI.apply(new AddRecipe(NCRecipes.IRRADIATOR_RECIPES, Lists.newArrayList(input1, input2), Lists.newArrayList(output1, output2), Lists.newArrayList(processTime)));
		}

		@ZenMethod
		public static void removeRecipe(IIngredient output1, IIngredient output2) {
			CraftTweakerAPI.apply(new RemoveRecipe(NCRecipes.IRRADIATOR_RECIPES, SorptionType.OUTPUT, Lists.newArrayList(output1, output2)));
		}
	}
	
	@ZenClass("mods.nuclearcraft.ingot_former")
	@ZenRegister
	public static class IngotFormerHandler {
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient output1) {
			CraftTweakerAPI.apply(new AddRecipe(NCRecipes.INGOT_FORMER_RECIPES, Lists.newArrayList(input1), Lists.newArrayList(output1), Lists.newArrayList(NCConfig.processor_time[10])));
		}
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient output1, int processTime) {
			CraftTweakerAPI.apply(new AddRecipe(NCRecipes.INGOT_FORMER_RECIPES, Lists.newArrayList(input1), Lists.newArrayList(output1), Lists.newArrayList(processTime)));
		}

		@ZenMethod
		public static void removeRecipe(IIngredient output1) {
			CraftTweakerAPI.apply(new RemoveRecipe(NCRecipes.INGOT_FORMER_RECIPES, SorptionType.OUTPUT, Lists.newArrayList(output1)));
		}
	}
	
	@ZenClass("mods.nuclearcraft.pressurizer")
	@ZenRegister
	public static class PressurizerHandler {
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient output1) {
			CraftTweakerAPI.apply(new AddRecipe(NCRecipes.PRESSURIZER_RECIPES, Lists.newArrayList(input1), Lists.newArrayList(output1), Lists.newArrayList(NCConfig.processor_time[11])));
		}
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient output1, int processTime) {
			CraftTweakerAPI.apply(new AddRecipe(NCRecipes.PRESSURIZER_RECIPES, Lists.newArrayList(input1), Lists.newArrayList(output1), Lists.newArrayList(processTime)));
		}

		@ZenMethod
		public static void removeRecipe(IIngredient output1) {
			CraftTweakerAPI.apply(new RemoveRecipe(NCRecipes.PRESSURIZER_RECIPES, SorptionType.OUTPUT, Lists.newArrayList(output1)));
		}
	}
	
	@ZenClass("mods.nuclearcraft.chemical_reactor")
	@ZenRegister
	public static class ChemicalReactorHandler {
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient input2, IIngredient output1, IIngredient output2) {
			CraftTweakerAPI.apply(new AddRecipe(NCRecipes.CHEMICAL_REACTOR_RECIPES, Lists.newArrayList(input1, input2), Lists.newArrayList(output1, output2), Lists.newArrayList(NCConfig.processor_time[12])));
		}
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient input2, IIngredient output1, IIngredient output2, int processTime) {
			CraftTweakerAPI.apply(new AddRecipe(NCRecipes.CHEMICAL_REACTOR_RECIPES, Lists.newArrayList(input1, input2), Lists.newArrayList(output1, output2), Lists.newArrayList(processTime)));
		}

		@ZenMethod
		public static void removeRecipe(IIngredient output1, IIngredient output2) {
			CraftTweakerAPI.apply(new RemoveRecipe(NCRecipes.CHEMICAL_REACTOR_RECIPES, SorptionType.OUTPUT, Lists.newArrayList(output1, output2)));
		}
	}
	
	@ZenClass("mods.nuclearcraft.salt_mixer")
	@ZenRegister
	public static class SaltMixerHandler {
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient input2, IIngredient output1) {
			CraftTweakerAPI.apply(new AddRecipe(NCRecipes.SALT_MIXER_RECIPES, Lists.newArrayList(input1, input2), Lists.newArrayList(output1), Lists.newArrayList(NCConfig.processor_time[13])));
		}
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient input2, IIngredient output1, int processTime) {
			CraftTweakerAPI.apply(new AddRecipe(NCRecipes.SALT_MIXER_RECIPES, Lists.newArrayList(input1, input2), Lists.newArrayList(output1), Lists.newArrayList(processTime)));
		}

		@ZenMethod
		public static void removeRecipe(IIngredient output1) {
			CraftTweakerAPI.apply(new RemoveRecipe(NCRecipes.SALT_MIXER_RECIPES, SorptionType.OUTPUT, Lists.newArrayList(output1)));
		}
	}
	
	@ZenClass("mods.nuclearcraft.crystallizer")
	@ZenRegister
	public static class CrystallizerHandler {
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient output1) {
			CraftTweakerAPI.apply(new AddRecipe(NCRecipes.CRYSTALLIZER_RECIPES, Lists.newArrayList(input1), Lists.newArrayList(output1), Lists.newArrayList(NCConfig.processor_time[14])));
		}
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient output1, int processTime) {
			CraftTweakerAPI.apply(new AddRecipe(NCRecipes.CRYSTALLIZER_RECIPES, Lists.newArrayList(input1), Lists.newArrayList(output1), Lists.newArrayList(processTime)));
		}

		@ZenMethod
		public static void removeRecipe(IIngredient output1) {
			CraftTweakerAPI.apply(new RemoveRecipe(NCRecipes.CRYSTALLIZER_RECIPES, SorptionType.OUTPUT, Lists.newArrayList(output1)));
		}
	}
	
	@ZenClass("mods.nuclearcraft.dissolver")
	@ZenRegister
	public static class DissolverHandler {
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient input2, IIngredient output1) {
			CraftTweakerAPI.apply(new AddRecipe(NCRecipes.DISSOLVER_RECIPES, Lists.newArrayList(input1, input2), Lists.newArrayList(output1), Lists.newArrayList(NCConfig.processor_time[15])));
		}
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient input2, IIngredient output1, int processTime) {
			CraftTweakerAPI.apply(new AddRecipe(NCRecipes.DISSOLVER_RECIPES, Lists.newArrayList(input1, input2), Lists.newArrayList(output1), Lists.newArrayList(processTime)));
		}

		@ZenMethod
		public static void removeRecipe(IIngredient output1) {
			CraftTweakerAPI.apply(new RemoveRecipe(NCRecipes.DISSOLVER_RECIPES, SorptionType.OUTPUT, Lists.newArrayList(output1)));
		}
	}
	
	@ZenClass("mods.nuclearcraft.extractor")
	@ZenRegister
	public static class ExtractorHandler {
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient input2, IIngredient output1) {
			CraftTweakerAPI.apply(new AddRecipe(NCRecipes.EXTRACTOR_RECIPES, Lists.newArrayList(input1, input2), Lists.newArrayList(output1), Lists.newArrayList(NCConfig.processor_time[16])));
		}
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient input2, IIngredient output1, int processTime) {
			CraftTweakerAPI.apply(new AddRecipe(NCRecipes.EXTRACTOR_RECIPES, Lists.newArrayList(input1, input2), Lists.newArrayList(output1), Lists.newArrayList(processTime)));
		}

		@ZenMethod
		public static void removeRecipe(IIngredient output1) {
			CraftTweakerAPI.apply(new RemoveRecipe(NCRecipes.EXTRACTOR_RECIPES, SorptionType.OUTPUT, Lists.newArrayList(output1)));
		}
	}
	
	@ZenClass("mods.nuclearcraft.fission")
	@ZenRegister
	public static class FissionHandler {
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient output1, double fuelTime, double power, double heat, String name) {
			CraftTweakerAPI.apply(new AddRecipe(NCRecipes.FISSION_RECIPES, Lists.newArrayList(input1), Lists.newArrayList(output1), Lists.newArrayList(fuelTime, power, heat, name)));
		}

		@ZenMethod
		public static void removeRecipe(IIngredient output1) {
			CraftTweakerAPI.apply(new RemoveRecipe(NCRecipes.FISSION_RECIPES, SorptionType.OUTPUT, Lists.newArrayList(output1)));
		}
	}
	
	@ZenClass("mods.nuclearcraft.fusion")
	@ZenRegister
	public static class FusionHandler {
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient input2, IIngredient output1, IIngredient output2, IIngredient output3, IIngredient output4, double fuelTime, double power, double heatVar) {
			CraftTweakerAPI.apply(new AddRecipe(NCRecipes.FUSION_RECIPES, Lists.newArrayList(input1, input2), Lists.newArrayList(output1, output2, output3, output4), Lists.newArrayList(fuelTime, power, heatVar)));
		}

		@ZenMethod
		public static void removeRecipe(IIngredient output1, IIngredient output2, IIngredient output3, IIngredient output4) {
			CraftTweakerAPI.apply(new RemoveRecipe(NCRecipes.FUSION_RECIPES, SorptionType.OUTPUT, Lists.newArrayList(output1, output2, output3, output4)));
		}
	}
}
