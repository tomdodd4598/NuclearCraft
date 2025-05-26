package nc.integration.crafttweaker;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import nc.recipe.*;
import stanhebben.zenscript.annotations.*;

public class CTRecipes {
	
	@ZenClass("mods.nuclearcraft.Manufactory")
	@ZenRegister
	public static class ManufactoryMethods {
		
		@ZenMethod
		public static BasicRecipeHandler getRecipeHandler() {
			return NCRecipes.manufactory;
		}
		
		@ZenMethod
		public static void addRecipe(IIngredient itemInput, IIngredient itemOutput, @Optional(valueDouble = 1D) double timeMultiplier, @Optional(valueDouble = 1D) double powerMultiplier, @Optional double processRadiation) {
			getRecipeHandler().ctAddRecipe(itemInput, itemOutput, timeMultiplier, powerMultiplier, processRadiation);
		}
		
		@ZenMethod
		public static void removeRecipeWithInput(IIngredient itemInput) {
			getRecipeHandler().ctRemoveRecipeWithInput(itemInput);
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient itemOutput) {
			getRecipeHandler().ctRemoveRecipeWithOutput(itemOutput);
		}
		
		@ZenMethod
		public static void removeAllRecipes() {
			getRecipeHandler().ctRemoveAllRecipes();
		}
	}
	
	@ZenClass("mods.nuclearcraft.Separator")
	@ZenRegister
	public static class SeparatorMethods {
		
		@ZenMethod
		public static BasicRecipeHandler getRecipeHandler() {
			return NCRecipes.separator;
		}
		
		@ZenMethod
		public static void addRecipe(IIngredient itemInput, IIngredient itemOutput1, IIngredient itemOutput2, @Optional(valueDouble = 1D) double timeMultiplier, @Optional(valueDouble = 1D) double powerMultiplier, @Optional double processRadiation) {
			getRecipeHandler().ctAddRecipe(itemInput, itemOutput1, itemOutput2, timeMultiplier, powerMultiplier, processRadiation);
		}
		
		@ZenMethod
		public static void removeRecipeWithInput(IIngredient itemInput) {
			getRecipeHandler().ctRemoveRecipeWithInput(itemInput);
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient itemOutput1, IIngredient itemOutput2) {
			getRecipeHandler().ctRemoveRecipeWithOutput(itemOutput1, itemOutput2);
		}
		
		@ZenMethod
		public static void removeAllRecipes() {
			getRecipeHandler().ctRemoveAllRecipes();
		}
	}
	
	@ZenClass("mods.nuclearcraft.DecayHastener")
	@ZenRegister
	public static class DecayHastenerMethods {
		
		@ZenMethod
		public static BasicRecipeHandler getRecipeHandler() {
			return NCRecipes.decay_hastener;
		}
		
		@ZenMethod
		public static void addRecipe(IIngredient itemInput, IIngredient itemOutput, @Optional(valueDouble = 1D) double timeMultiplier, @Optional(valueDouble = 1D) double powerMultiplier, @Optional double processRadiation) {
			getRecipeHandler().ctAddRecipe(itemInput, itemOutput, timeMultiplier, powerMultiplier, processRadiation);
		}
		
		@ZenMethod
		public static void removeRecipeWithInput(IIngredient itemInput) {
			getRecipeHandler().ctRemoveRecipeWithInput(itemInput);
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient itemOutput) {
			getRecipeHandler().ctRemoveRecipeWithOutput(itemOutput);
		}
		
		@ZenMethod
		public static void removeAllRecipes() {
			getRecipeHandler().ctRemoveAllRecipes();
		}
	}
	
	@ZenClass("mods.nuclearcraft.FuelReprocessor")
	@ZenRegister
	public static class FuelReprocessorMethods {
		
		@ZenMethod
		public static BasicRecipeHandler getRecipeHandler() {
			return NCRecipes.fuel_reprocessor;
		}
		
		@ZenMethod
		public static void addRecipe(IIngredient itemInput, IIngredient itemOutput1, IIngredient itemOutput2, IIngredient itemOutput3, IIngredient itemOutput4, IIngredient itemOutput5, IIngredient itemOutput6, IIngredient itemOutput7, IIngredient itemOutput8, @Optional(valueDouble = 1D) double timeMultiplier, @Optional(valueDouble = 1D) double powerMultiplier, @Optional double processRadiation) {
			getRecipeHandler().ctAddRecipe(itemInput, itemOutput1, itemOutput2, itemOutput3, itemOutput4, itemOutput5, itemOutput6, itemOutput7, itemOutput8, timeMultiplier, powerMultiplier, processRadiation);
		}
		
		@ZenMethod
		public static void removeRecipeWithInput(IIngredient itemInput) {
			getRecipeHandler().ctRemoveRecipeWithInput(itemInput);
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient itemOutput1, IIngredient itemOutput2, IIngredient itemOutput3, IIngredient itemOutput4, IIngredient itemOutput5, IIngredient itemOutput6, IIngredient itemOutput7, IIngredient itemOutput8) {
			getRecipeHandler().ctRemoveRecipeWithOutput(itemOutput1, itemOutput2, itemOutput3, itemOutput4, itemOutput5, itemOutput6, itemOutput7, itemOutput8);
		}
		
		@ZenMethod
		public static void removeAllRecipes() {
			getRecipeHandler().ctRemoveAllRecipes();
		}
	}
	
	@ZenClass("mods.nuclearcraft.AlloyFurnace")
	@ZenRegister
	public static class AlloyFurnaceMethods {
		
		@ZenMethod
		public static BasicRecipeHandler getRecipeHandler() {
			return NCRecipes.alloy_furnace;
		}
		
		@ZenMethod
		public static void addRecipe(IIngredient itemInput1, IIngredient itemInput2, IIngredient itemOutput, @Optional(valueDouble = 1D) double timeMultiplier, @Optional(valueDouble = 1D) double powerMultiplier, @Optional double processRadiation) {
			getRecipeHandler().ctAddRecipe(itemInput1, itemInput2, itemOutput, timeMultiplier, powerMultiplier, processRadiation);
		}
		
		@ZenMethod
		public static void removeRecipeWithInput(IIngredient itemInput1, IIngredient itemInput2) {
			getRecipeHandler().ctRemoveRecipeWithInput(itemInput1, itemInput2);
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient itemOutput) {
			getRecipeHandler().ctRemoveRecipeWithOutput(itemOutput);
		}
		
		@ZenMethod
		public static void removeAllRecipes() {
			getRecipeHandler().ctRemoveAllRecipes();
		}
	}
	
	@ZenClass("mods.nuclearcraft.Infuser")
	@ZenRegister
	public static class InfuserMethods {
		
		@ZenMethod
		public static BasicRecipeHandler getRecipeHandler() {
			return NCRecipes.infuser;
		}
		
		@ZenMethod
		public static void addRecipe(IIngredient itemInput, IIngredient fluidInput, IIngredient itemOutput, @Optional(valueDouble = 1D) double timeMultiplier, @Optional(valueDouble = 1D) double powerMultiplier, @Optional double processRadiation) {
			getRecipeHandler().ctAddRecipe(itemInput, fluidInput, itemOutput, timeMultiplier, powerMultiplier, processRadiation);
		}
		
		@ZenMethod
		public static void removeRecipeWithInput(IIngredient itemInput, IIngredient fluidInput) {
			getRecipeHandler().ctRemoveRecipeWithInput(itemInput, fluidInput);
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient itemOutput) {
			getRecipeHandler().ctRemoveRecipeWithOutput(itemOutput);
		}
		
		@ZenMethod
		public static void removeAllRecipes() {
			getRecipeHandler().ctRemoveAllRecipes();
		}
	}
	
	@ZenClass("mods.nuclearcraft.Melter")
	@ZenRegister
	public static class MelterMethods {
		
		@ZenMethod
		public static BasicRecipeHandler getRecipeHandler() {
			return NCRecipes.melter;
		}
		
		@ZenMethod
		public static void addRecipe(IIngredient itemInput, IIngredient fluidOutput, @Optional(valueDouble = 1D) double timeMultiplier, @Optional(valueDouble = 1D) double powerMultiplier, @Optional double processRadiation) {
			getRecipeHandler().ctAddRecipe(itemInput, fluidOutput, timeMultiplier, powerMultiplier, processRadiation);
		}
		
		@ZenMethod
		public static void removeRecipeWithInput(IIngredient itemInput) {
			getRecipeHandler().ctRemoveRecipeWithInput(itemInput);
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient fluidOutput) {
			getRecipeHandler().ctRemoveRecipeWithOutput(fluidOutput);
		}
		
		@ZenMethod
		public static void removeAllRecipes() {
			getRecipeHandler().ctRemoveAllRecipes();
		}
	}
	
	@ZenClass("mods.nuclearcraft.Supercooler")
	@ZenRegister
	public static class SupercoolerMethods {
		
		@ZenMethod
		public static BasicRecipeHandler getRecipeHandler() {
			return NCRecipes.supercooler;
		}
		
		@ZenMethod
		public static void addRecipe(IIngredient fluidInput, IIngredient fluidOutput, @Optional(valueDouble = 1D) double timeMultiplier, @Optional(valueDouble = 1D) double powerMultiplier, @Optional double processRadiation) {
			getRecipeHandler().ctAddRecipe(fluidInput, fluidOutput, timeMultiplier, powerMultiplier, processRadiation);
		}
		
		@ZenMethod
		public static void removeRecipeWithInput(IIngredient fluidInput) {
			getRecipeHandler().ctRemoveRecipeWithInput(fluidInput);
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient fluidOutput) {
			getRecipeHandler().ctRemoveRecipeWithOutput(fluidOutput);
		}
		
		@ZenMethod
		public static void removeAllRecipes() {
			getRecipeHandler().ctRemoveAllRecipes();
		}
	}
	
	@ZenClass("mods.nuclearcraft.Electrolyzer")
	@ZenRegister
	public static class ElectrolyzerMethods {
		
		@ZenMethod
		public static BasicRecipeHandler getRecipeHandler() {
			return NCRecipes.electrolyzer;
		}
		
		@ZenMethod
		public static void addRecipe(IIngredient fluidInput, IIngredient fluidOutput1, IIngredient fluidOutput2, IIngredient fluidOutput3, IIngredient fluidOutput4, @Optional(valueDouble = 1D) double timeMultiplier, @Optional(valueDouble = 1D) double powerMultiplier, @Optional double processRadiation) {
			getRecipeHandler().ctAddRecipe(fluidInput, fluidOutput1, fluidOutput2, fluidOutput3, fluidOutput4, timeMultiplier, powerMultiplier, processRadiation);
		}
		
		@ZenMethod
		public static void removeRecipeWithInput(IIngredient fluidInput) {
			getRecipeHandler().ctRemoveRecipeWithInput(fluidInput);
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient fluidOutput1, IIngredient fluidOutput2, IIngredient fluidOutput3, IIngredient fluidOutput4) {
			getRecipeHandler().ctRemoveRecipeWithOutput(fluidOutput1, fluidOutput2, fluidOutput3, fluidOutput4);
		}
		
		@ZenMethod
		public static void removeAllRecipes() {
			getRecipeHandler().ctRemoveAllRecipes();
		}
	}
	
	@ZenClass("mods.nuclearcraft.Assembler")
	@ZenRegister
	public static class AssemblerMethods {
		
		@ZenMethod
		public static BasicRecipeHandler getRecipeHandler() {
			return NCRecipes.assembler;
		}
		
		@ZenMethod
		public static void addRecipe(IIngredient itemInput1, IIngredient itemInput2, IIngredient itemInput3, IIngredient itemInput4, IIngredient itemOutput, @Optional(valueDouble = 1D) double timeMultiplier, @Optional(valueDouble = 1D) double powerMultiplier, @Optional double processRadiation) {
			getRecipeHandler().ctAddRecipe(itemInput1, itemInput2, itemInput3, itemInput4, itemOutput, timeMultiplier, powerMultiplier, processRadiation);
		}
		
		@ZenMethod
		public static void removeRecipeWithInput(IIngredient itemInput1, IIngredient itemInput2, IIngredient itemInput3, IIngredient itemInput4) {
			getRecipeHandler().ctRemoveRecipeWithInput(itemInput1, itemInput2, itemInput3, itemInput4);
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient itemOutput) {
			getRecipeHandler().ctRemoveRecipeWithOutput(itemOutput);
		}
		
		@ZenMethod
		public static void removeAllRecipes() {
			getRecipeHandler().ctRemoveAllRecipes();
		}
	}
	
	@ZenClass("mods.nuclearcraft.IngotFormer")
	@ZenRegister
	public static class IngotFormerMethods {
		
		@ZenMethod
		public static BasicRecipeHandler getRecipeHandler() {
			return NCRecipes.ingot_former;
		}
		
		@ZenMethod
		public static void addRecipe(IIngredient fluidInput, IIngredient itemOutput, @Optional(valueDouble = 1D) double timeMultiplier, @Optional(valueDouble = 1D) double powerMultiplier, @Optional double processRadiation) {
			getRecipeHandler().ctAddRecipe(fluidInput, itemOutput, timeMultiplier, powerMultiplier, processRadiation);
		}
		
		@ZenMethod
		public static void removeRecipeWithInput(IIngredient fluidInput) {
			getRecipeHandler().ctRemoveRecipeWithInput(fluidInput);
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient itemOutput) {
			getRecipeHandler().ctRemoveRecipeWithOutput(itemOutput);
		}
		
		@ZenMethod
		public static void removeAllRecipes() {
			getRecipeHandler().ctRemoveAllRecipes();
		}
	}
	
	@ZenClass("mods.nuclearcraft.Pressurizer")
	@ZenRegister
	public static class PressurizerMethods {
		
		@ZenMethod
		public static BasicRecipeHandler getRecipeHandler() {
			return NCRecipes.pressurizer;
		}
		
		@ZenMethod
		public static void addRecipe(IIngredient itemInput, IIngredient itemOutput, @Optional(valueDouble = 1D) double timeMultiplier, @Optional(valueDouble = 1D) double powerMultiplier, @Optional double processRadiation) {
			getRecipeHandler().ctAddRecipe(itemInput, itemOutput, timeMultiplier, powerMultiplier, processRadiation);
		}
		
		@ZenMethod
		public static void removeRecipeWithInput(IIngredient itemInput) {
			getRecipeHandler().ctRemoveRecipeWithInput(itemInput);
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient itemOutput) {
			getRecipeHandler().ctRemoveRecipeWithOutput(itemOutput);
		}
		
		@ZenMethod
		public static void removeAllRecipes() {
			getRecipeHandler().ctRemoveAllRecipes();
		}
	}
	
	@ZenClass("mods.nuclearcraft.ChemicalReactor")
	@ZenRegister
	public static class ChemicalReactorMethods {
		
		@ZenMethod
		public static BasicRecipeHandler getRecipeHandler() {
			return NCRecipes.chemical_reactor;
		}
		
		@ZenMethod
		public static void addRecipe(IIngredient fluidInput1, IIngredient fluidInput2, IIngredient fluidOutput1, IIngredient fluidOutput2, @Optional(valueDouble = 1D) double timeMultiplier, @Optional(valueDouble = 1D) double powerMultiplier, @Optional double processRadiation) {
			getRecipeHandler().ctAddRecipe(fluidInput1, fluidInput2, fluidOutput1, fluidOutput2, timeMultiplier, powerMultiplier, processRadiation);
		}
		
		@ZenMethod
		public static void removeRecipeWithInput(IIngredient fluidInput1, IIngredient fluidInput2) {
			getRecipeHandler().ctRemoveRecipeWithInput(fluidInput1, fluidInput2);
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient fluidOutput1, IIngredient fluidOutput2) {
			getRecipeHandler().ctRemoveRecipeWithOutput(fluidOutput1, fluidOutput2);
		}
		
		@ZenMethod
		public static void removeAllRecipes() {
			getRecipeHandler().ctRemoveAllRecipes();
		}
	}
	
	@ZenClass("mods.nuclearcraft.SaltMixer")
	@ZenRegister
	public static class SaltMixerMethods {
		
		@ZenMethod
		public static BasicRecipeHandler getRecipeHandler() {
			return NCRecipes.salt_mixer;
		}
		
		@ZenMethod
		public static void addRecipe(IIngredient fluidInput1, IIngredient fluidInput2, IIngredient fluidOutput, @Optional(valueDouble = 1D) double timeMultiplier, @Optional(valueDouble = 1D) double powerMultiplier, @Optional double processRadiation) {
			getRecipeHandler().ctAddRecipe(fluidInput1, fluidInput2, fluidOutput, timeMultiplier, powerMultiplier, processRadiation);
		}
		
		@ZenMethod
		public static void removeRecipeWithInput(IIngredient fluidInput1, IIngredient fluidInput2) {
			getRecipeHandler().ctRemoveRecipeWithInput(fluidInput1, fluidInput2);
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient fluidOutput) {
			getRecipeHandler().ctRemoveRecipeWithOutput(fluidOutput);
		}
		
		@ZenMethod
		public static void removeAllRecipes() {
			getRecipeHandler().ctRemoveAllRecipes();
		}
	}
	
	@ZenClass("mods.nuclearcraft.Crystallizer")
	@ZenRegister
	public static class CrystallizerMethods {
		
		@ZenMethod
		public static BasicRecipeHandler getRecipeHandler() {
			return NCRecipes.crystallizer;
		}
		
		@ZenMethod
		public static void addRecipe(IIngredient fluidInput, IIngredient itemOutput, @Optional(valueDouble = 1D) double timeMultiplier, @Optional(valueDouble = 1D) double powerMultiplier, @Optional double processRadiation) {
			getRecipeHandler().ctAddRecipe(fluidInput, itemOutput, timeMultiplier, powerMultiplier, processRadiation);
		}
		
		@ZenMethod
		public static void removeRecipeWithInput(IIngredient fluidInput) {
			getRecipeHandler().ctRemoveRecipeWithInput(fluidInput);
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient itemOutput) {
			getRecipeHandler().ctRemoveRecipeWithOutput(itemOutput);
		}
		
		@ZenMethod
		public static void removeAllRecipes() {
			getRecipeHandler().ctRemoveAllRecipes();
		}
	}
	
	@ZenClass("mods.nuclearcraft.Enricher")
	@ZenRegister
	public static class EnricherMethods {
		
		@ZenMethod
		public static BasicRecipeHandler getRecipeHandler() {
			return NCRecipes.enricher;
		}
		
		@ZenMethod
		public static void addRecipe(IIngredient itemInput, IIngredient fluidInput, IIngredient fluidOutput, @Optional(valueDouble = 1D) double timeMultiplier, @Optional(valueDouble = 1D) double powerMultiplier, @Optional double processRadiation) {
			getRecipeHandler().ctAddRecipe(itemInput, fluidInput, fluidOutput, timeMultiplier, powerMultiplier, processRadiation);
		}
		
		@ZenMethod
		public static void removeRecipeWithInput(IIngredient itemInput, IIngredient fluidInput) {
			getRecipeHandler().ctRemoveRecipeWithInput(itemInput, fluidInput);
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient fluidOutput) {
			getRecipeHandler().ctRemoveRecipeWithOutput(fluidOutput);
		}
		
		@ZenMethod
		public static void removeAllRecipes() {
			getRecipeHandler().ctRemoveAllRecipes();
		}
	}
	
	@ZenClass("mods.nuclearcraft.Extractor")
	@ZenRegister
	public static class ExtractorMethods {
		
		@ZenMethod
		public static BasicRecipeHandler getRecipeHandler() {
			return NCRecipes.extractor;
		}
		
		@ZenMethod
		public static void addRecipe(IIngredient itemInput, IIngredient itemOutput, IIngredient fluidOutput, @Optional(valueDouble = 1D) double timeMultiplier, @Optional(valueDouble = 1D) double powerMultiplier, @Optional double processRadiation) {
			getRecipeHandler().ctAddRecipe(itemInput, itemOutput, fluidOutput, timeMultiplier, powerMultiplier, processRadiation);
		}
		
		@ZenMethod
		public static void removeRecipeWithInput(IIngredient itemInput) {
			getRecipeHandler().ctRemoveRecipeWithInput(itemInput);
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient itemOutput, IIngredient fluidOutput) {
			getRecipeHandler().ctRemoveRecipeWithOutput(itemOutput, fluidOutput);
		}
		
		@ZenMethod
		public static void removeAllRecipes() {
			getRecipeHandler().ctRemoveAllRecipes();
		}
	}
	
	@ZenClass("mods.nuclearcraft.Centrifuge")
	@ZenRegister
	public static class CentrifugeMethods {
		
		@ZenMethod
		public static BasicRecipeHandler getRecipeHandler() {
			return NCRecipes.centrifuge;
		}
		
		@ZenMethod
		public static void addRecipe(IIngredient fluidInput, IIngredient fluidOutput1, IIngredient fluidOutput2, IIngredient fluidOutput3, IIngredient fluidOutput4, IIngredient fluidOutput5, IIngredient fluidOutput6, @Optional(valueDouble = 1D) double timeMultiplier, @Optional(valueDouble = 1D) double powerMultiplier, @Optional double processRadiation) {
			getRecipeHandler().ctAddRecipe(fluidInput, fluidOutput1, fluidOutput2, fluidOutput3, fluidOutput4, fluidOutput5, fluidOutput6, timeMultiplier, powerMultiplier, processRadiation);
		}
		
		@ZenMethod
		public static void removeRecipeWithInput(IIngredient fluidInput) {
			getRecipeHandler().ctRemoveRecipeWithInput(fluidInput);
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient fluidOutput1, IIngredient fluidOutput2, IIngredient fluidOutput3, IIngredient fluidOutput4, IIngredient fluidOutput5, IIngredient fluidOutput6) {
			getRecipeHandler().ctRemoveRecipeWithOutput(fluidOutput1, fluidOutput2, fluidOutput3, fluidOutput4, fluidOutput5, fluidOutput6);
		}
		
		@ZenMethod
		public static void removeAllRecipes() {
			getRecipeHandler().ctRemoveAllRecipes();
		}
	}
	
	@ZenClass("mods.nuclearcraft.RockCrusher")
	@ZenRegister
	public static class RockCrusherMethods {
		
		@ZenMethod
		public static BasicRecipeHandler getRecipeHandler() {
			return NCRecipes.rock_crusher;
		}
		
		@ZenMethod
		public static void addRecipe(IIngredient itemInput, IIngredient itemOutput1, IIngredient itemOutput2, IIngredient itemOutput3, @Optional(valueDouble = 1D) double timeMultiplier, @Optional(valueDouble = 1D) double powerMultiplier, @Optional double processRadiation) {
			getRecipeHandler().ctAddRecipe(itemInput, itemOutput1, itemOutput2, itemOutput3, timeMultiplier, powerMultiplier, processRadiation);
		}
		
		@ZenMethod
		public static void removeRecipeWithInput(IIngredient itemInput) {
			getRecipeHandler().ctRemoveRecipeWithInput(itemInput);
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient itemOutput1, IIngredient itemOutput2, IIngredient itemOutput3) {
			getRecipeHandler().ctRemoveRecipeWithOutput(itemOutput1, itemOutput2, itemOutput3);
		}
		
		@ZenMethod
		public static void removeAllRecipes() {
			getRecipeHandler().ctRemoveAllRecipes();
		}
	}
	
	@ZenClass("mods.nuclearcraft.ElectricFurnace")
	@ZenRegister
	public static class ElectricFurnaceMethods {
		
		@ZenMethod
		public static BasicRecipeHandler getRecipeHandler() {
			return NCRecipes.electric_furnace;
		}
		
		@ZenMethod
		public static void addRecipe(IIngredient itemInput, IIngredient itemOutput, @Optional(valueDouble = 1D) double timeMultiplier, @Optional(valueDouble = 1D) double powerMultiplier, @Optional double processRadiation) {
			getRecipeHandler().ctAddRecipe(itemInput, itemOutput, timeMultiplier, powerMultiplier, processRadiation);
		}
		
		@ZenMethod
		public static void removeRecipeWithInput(IIngredient itemInput) {
			getRecipeHandler().ctRemoveRecipeWithInput(itemInput);
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient itemOutput) {
			getRecipeHandler().ctRemoveRecipeWithOutput(itemOutput);
		}
		
		@ZenMethod
		public static void removeAllRecipes() {
			getRecipeHandler().ctRemoveAllRecipes();
		}
	}
	
	@ZenClass("mods.nuclearcraft.DecayGenerator")
	@ZenRegister
	public static class DecayGeneratorMethods {
		
		@ZenMethod
		public static BasicRecipeHandler getRecipeHandler() {
			return NCRecipes.decay_generator;
		}
		
		@ZenMethod
		public static void addRecipe(IIngredient blockInput, IIngredient blockOutput, double meanLifetime, double power, double radiation) {
			getRecipeHandler().ctAddRecipe(blockInput, blockOutput, meanLifetime, power, radiation);
		}
		
		@ZenMethod
		public static void removeRecipeWithInput(IIngredient blockInput) {
			getRecipeHandler().ctRemoveRecipeWithInput(blockInput);
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient blockOutput) {
			getRecipeHandler().ctRemoveRecipeWithOutput(blockOutput);
		}
		
		@ZenMethod
		public static void removeAllRecipes() {
			getRecipeHandler().ctRemoveAllRecipes();
		}
	}
	
	@ZenClass("mods.nuclearcraft.Diaphragm")
	@ZenRegister
	public static class DiaphragmMethods {
		
		@ZenMethod
		public static BasicRecipeHandler getRecipeHandler() {
			return NCRecipes.machine_diaphragm;
		}
		
		@ZenMethod
		public static void add(IIngredient block, double efficiency, double contactFactor) {
			getRecipeHandler().ctAddRecipe(block, efficiency, contactFactor);
		}
		
		@ZenMethod
		public static void remove(IIngredient block) {
			getRecipeHandler().ctRemoveRecipeWithInput(block);
		}
		
		@ZenMethod
		public static void removeAll() {
			getRecipeHandler().ctRemoveAllRecipes();
		}
	}
	
	@ZenClass("mods.nuclearcraft.SieveAssembly")
	@ZenRegister
	public static class SieveAssemblyMethods {
		
		@ZenMethod
		public static BasicRecipeHandler getRecipeHandler() {
			return NCRecipes.machine_sieve_assembly;
		}
		
		@ZenMethod
		public static void add(IIngredient block, double efficiency) {
			getRecipeHandler().ctAddRecipe(block, efficiency);
		}
		
		@ZenMethod
		public static void remove(IIngredient block) {
			getRecipeHandler().ctRemoveRecipeWithInput(block);
		}
		
		@ZenMethod
		public static void removeAll() {
			getRecipeHandler().ctRemoveAllRecipes();
		}
	}
	
	@ZenClass("mods.nuclearcraft.MultiblockElectrolyzer")
	@ZenRegister
	public static class MultiblockElectrolyzerMethods {
		
		@ZenMethod
		public static BasicRecipeHandler getRecipeHandler() {
			return NCRecipes.multiblock_electrolyzer;
		}
		
		@ZenMethod
		public static void addRecipe(IIngredient itemInput1, IIngredient itemInput2, IIngredient fluidInput1, IIngredient fluidInput2, IIngredient itemOutput1, IIngredient itemOutput2, IIngredient itemOutput3, IIngredient itemOutput4, IIngredient fluidOutput1, IIngredient fluidOutput2, IIngredient fluidOutput3, IIngredient fluidOutput4, double timeMultiplier, double powerMultiplier, double processRadiation, String electrolyteGroup) {
			getRecipeHandler().ctAddRecipe(itemInput1, itemInput2, fluidInput1, fluidInput2, itemOutput1, itemOutput2, itemOutput3, itemOutput4, fluidOutput1, fluidOutput2, fluidOutput3, fluidOutput4, timeMultiplier, powerMultiplier, processRadiation, electrolyteGroup);
		}
		
		@ZenMethod
		public static void removeRecipeWithInput(IIngredient itemInput1, IIngredient itemInput2, IIngredient fluidInput1, IIngredient fluidInput2) {
			getRecipeHandler().ctRemoveRecipeWithInput(itemInput1, itemInput2, fluidInput1, fluidInput2);
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient itemOutput1, IIngredient itemOutput2, IIngredient itemOutput3, IIngredient itemOutput4, IIngredient fluidOutput1, IIngredient fluidOutput2, IIngredient fluidOutput3, IIngredient fluidOutput4) {
			getRecipeHandler().ctRemoveRecipeWithOutput(itemOutput1, itemOutput2, itemOutput3, itemOutput4, fluidOutput1, fluidOutput2, fluidOutput3, fluidOutput4);
		}
		
		@ZenMethod
		public static void removeAllRecipes() {
			getRecipeHandler().ctRemoveAllRecipes();
		}
		
		@ZenMethod
		public static void addElectrolyte(String electrolyteGroup, IIngredient fluid, double efficiency) {
			NCRecipes.multiblock_electrolyzer.addElectrolyte(electrolyteGroup, CTHelper.buildAdditionFluidIngredient(fluid), efficiency);
		}
		
		@ZenMethod
		public static void removeElectrolyte(String electrolyteGroup, IIngredient fluid) {
			NCRecipes.multiblock_electrolyzer.removeElectrolyte(electrolyteGroup, CTHelper.buildAdditionFluidIngredient(fluid));
		}
		
		@ZenMethod
		public static void removeElectrolyteGroup(String electrolyteGroup) {
			NCRecipes.multiblock_electrolyzer.removeElectrolyteGroup(electrolyteGroup);
		}
		
		@ZenMethod
		public static void removeAllElectrolyteGroups() {
			NCRecipes.multiblock_electrolyzer.removeAllElectrolyteGroups();
		}
	}
	
	@ZenClass("mods.nuclearcraft.ElectrolyzerCathode")
	@ZenRegister
	public static class ElectrolyzerCathodeMethods {
		
		@ZenMethod
		public static BasicRecipeHandler getRecipeHandler() {
			return NCRecipes.electrolyzer_cathode;
		}
		
		@ZenMethod
		public static void add(IIngredient block, double efficiency) {
			getRecipeHandler().ctAddRecipe(block, efficiency);
		}
		
		@ZenMethod
		public static void remove(IIngredient block) {
			getRecipeHandler().ctRemoveRecipeWithInput(block);
		}
		
		@ZenMethod
		public static void removeAll() {
			getRecipeHandler().ctRemoveAllRecipes();
		}
	}
	
	@ZenClass("mods.nuclearcraft.ElectrolyzerAnode")
	@ZenRegister
	public static class ElectrolyzerAnodeMethods {
		
		@ZenMethod
		public static BasicRecipeHandler getRecipeHandler() {
			return NCRecipes.electrolyzer_anode;
		}
		
		@ZenMethod
		public static void add(IIngredient block, double efficiency) {
			getRecipeHandler().ctAddRecipe(block, efficiency);
		}
		
		@ZenMethod
		public static void remove(IIngredient block) {
			getRecipeHandler().ctRemoveRecipeWithInput(block);
		}
		
		@ZenMethod
		public static void removeAll() {
			getRecipeHandler().ctRemoveAllRecipes();
		}
	}
	
	@ZenClass("mods.nuclearcraft.MultiblockDistiller")
	@ZenRegister
	public static class MultiblockDistillerMethods {
		
		@ZenMethod
		public static BasicRecipeHandler getRecipeHandler() {
			return NCRecipes.multiblock_distiller;
		}
		
		@ZenMethod
		public static void addRecipe(IIngredient fluidInput1, IIngredient fluidInput2, IIngredient fluidOutput1, IIngredient fluidOutput2, IIngredient fluidOutput3, IIngredient fluidOutput4, IIngredient fluidOutput5, IIngredient fluidOutput6, IIngredient fluidOutput7, IIngredient fluidOutput8, @Optional(valueDouble = 1D) double timeMultiplier, @Optional(valueDouble = 1D) double powerMultiplier, @Optional double processRadiation) {
			getRecipeHandler().ctAddRecipe(fluidInput1, fluidInput2, fluidOutput1, fluidOutput2, fluidOutput3, fluidOutput4, fluidOutput5, fluidOutput6, fluidOutput7, fluidOutput8, timeMultiplier, powerMultiplier, processRadiation);
		}
		
		@ZenMethod
		public static void removeRecipeWithInput(IIngredient fluidInput1, IIngredient fluidInput2) {
			getRecipeHandler().ctRemoveRecipeWithInput(fluidInput1, fluidInput2);
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient fluidOutput1, IIngredient fluidOutput2, IIngredient fluidOutput3, IIngredient fluidOutput4, IIngredient fluidOutput5, IIngredient fluidOutput6, IIngredient fluidOutput7, IIngredient fluidOutput8) {
			getRecipeHandler().ctRemoveRecipeWithOutput(fluidOutput1, fluidOutput2, fluidOutput3, fluidOutput4, fluidOutput5, fluidOutput6, fluidOutput7, fluidOutput8);
		}
		
		@ZenMethod
		public static void removeAllRecipes() {
			getRecipeHandler().ctRemoveAllRecipes();
		}
	}
	
	@ZenClass("mods.nuclearcraft.MultiblockInfiltrator")
	@ZenRegister
	public static class MultiblockInfiltratorMethods {
		
		@ZenMethod
		public static BasicRecipeHandler getRecipeHandler() {
			return NCRecipes.multiblock_infiltrator;
		}
		
		@ZenMethod
		public static void addRecipe(IIngredient itemInput1, IIngredient itemInput2, IIngredient fluidInput1, IIngredient fluidInput2, IIngredient itemOutput, double timeMultiplier, double powerMultiplier, double processRadiation, double heatingFactor) {
			getRecipeHandler().ctAddRecipe(itemInput1, itemInput2, fluidInput1, fluidInput2, itemOutput, timeMultiplier, powerMultiplier, processRadiation, heatingFactor);
		}
		
		@ZenMethod
		public static void removeRecipeWithInput(IIngredient itemInput1, IIngredient itemInput2, IIngredient fluidInput1, IIngredient fluidInput2) {
			getRecipeHandler().ctRemoveRecipeWithInput(itemInput1, itemInput2, fluidInput1, fluidInput2);
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient itemOutput) {
			getRecipeHandler().ctRemoveRecipeWithOutput(itemOutput);
		}
		
		@ZenMethod
		public static void removeAllRecipes() {
			getRecipeHandler().ctRemoveAllRecipes();
		}
	}
	
	@ZenClass("mods.nuclearcraft.InfiltratorPressureFluid")
	@ZenRegister
	public static class InfiltratorPressureFluidMethods {
		
		@ZenMethod
		public static BasicRecipeHandler getRecipeHandler() {
			return NCRecipes.infiltrator_pressure_fluid;
		}
		
		@ZenMethod
		public static void add(IIngredient fluid, double efficiency) {
			getRecipeHandler().ctAddRecipe(fluid, efficiency);
		}
		
		@ZenMethod
		public static void remove(IIngredient fluid) {
			getRecipeHandler().ctRemoveRecipeWithInput(fluid);
		}
		
		@ZenMethod
		public static void removeAll() {
			getRecipeHandler().ctRemoveAllRecipes();
		}
	}
	
	@ZenClass("mods.nuclearcraft.FissionModerator")
	@ZenRegister
	public static class FissionModeratorMethods {
		
		@ZenMethod
		public static BasicRecipeHandler getRecipeHandler() {
			return NCRecipes.fission_moderator;
		}
		
		@ZenMethod
		public static void add(IIngredient block, int fluxFactor, double efficiency) {
			getRecipeHandler().ctAddRecipe(block, fluxFactor, efficiency);
		}
		
		@ZenMethod
		public static void remove(IIngredient block) {
			getRecipeHandler().ctRemoveRecipeWithInput(block);
		}
		
		@ZenMethod
		public static void removeAll() {
			getRecipeHandler().ctRemoveAllRecipes();
		}
	}
	
	@ZenClass("mods.nuclearcraft.FissionReflector")
	@ZenRegister
	public static class FissionReflectorMethods {
		
		@ZenMethod
		public static BasicRecipeHandler getRecipeHandler() {
			return NCRecipes.fission_reflector;
		}
		
		@ZenMethod
		public static void add(IIngredient block, double efficiency, double reflectivity) {
			getRecipeHandler().ctAddRecipe(block, efficiency, reflectivity);
		}
		
		@ZenMethod
		public static void remove(IIngredient block) {
			getRecipeHandler().ctRemoveRecipeWithInput(block);
		}
		
		@ZenMethod
		public static void removeAll() {
			getRecipeHandler().ctRemoveAllRecipes();
		}
	}
	
	@ZenClass("mods.nuclearcraft.FissionIrradiator")
	@ZenRegister
	public static class FissionIrradiatorMethods {
		
		@ZenMethod
		public static BasicRecipeHandler getRecipeHandler() {
			return NCRecipes.fission_irradiator;
		}
		
		@ZenMethod
		public static void addRecipe(IIngredient itemInput, IIngredient itemOutput, long fluxRequired, double heatPerFlux, double efficiency, @Optional(valueLong = 0) long minFluxPerTick, @Optional(valueLong = -1) long maxFluxPerTick, @Optional double radiation) {
			getRecipeHandler().ctAddRecipe(itemInput, itemOutput, fluxRequired, heatPerFlux, efficiency, minFluxPerTick, maxFluxPerTick, radiation);
		}
		
		@ZenMethod
		public static void removeRecipeWithInput(IIngredient itemInput) {
			getRecipeHandler().ctRemoveRecipeWithInput(itemInput);
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient itemOutput) {
			getRecipeHandler().ctRemoveRecipeWithOutput(itemOutput);
		}
		
		@ZenMethod
		public static void removeAllRecipes() {
			getRecipeHandler().ctRemoveAllRecipes();
		}
	}
	
	@ZenClass("mods.nuclearcraft.PebbleFission")
	@ZenRegister
	public static class PebbleFissionMethods {
		
		@ZenMethod
		public static BasicRecipeHandler getRecipeHandler() {
			return NCRecipes.pebble_fission;
		}
		
		@ZenMethod
		public static void addRecipe(IIngredient itemInput, IIngredient itemOutput, int time, int heat, double efficiency, int criticality, double decayFactor, boolean selfPriming, double radiation) {
			getRecipeHandler().ctAddRecipe(itemInput, itemOutput, time, heat, efficiency, criticality, decayFactor, selfPriming, radiation);
		}
		
		@ZenMethod
		public static void removeRecipeWithInput(IIngredient itemInput) {
			getRecipeHandler().ctRemoveRecipeWithInput(itemInput);
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient itemOutput) {
			getRecipeHandler().ctRemoveRecipeWithOutput(itemOutput);
		}
		
		@ZenMethod
		public static void removeAllRecipes() {
			getRecipeHandler().ctRemoveAllRecipes();
		}
	}
	
	@ZenClass("mods.nuclearcraft.SolidFission")
	@ZenRegister
	public static class SolidFissionMethods {
		
		@ZenMethod
		public static BasicRecipeHandler getRecipeHandler() {
			return NCRecipes.solid_fission;
		}
		
		@ZenMethod
		public static void addRecipe(IIngredient itemInput, IIngredient itemOutput, int time, int heat, double efficiency, int criticality, double decayFactor, boolean selfPriming, double radiation) {
			getRecipeHandler().ctAddRecipe(itemInput, itemOutput, time, heat, efficiency, criticality, decayFactor, selfPriming, radiation);
		}
		
		@ZenMethod
		public static void removeRecipeWithInput(IIngredient itemInput) {
			getRecipeHandler().ctRemoveRecipeWithInput(itemInput);
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient itemOutput) {
			getRecipeHandler().ctRemoveRecipeWithOutput(itemOutput);
		}
		
		@ZenMethod
		public static void removeAllRecipes() {
			getRecipeHandler().ctRemoveAllRecipes();
		}
	}
	
	@ZenClass("mods.nuclearcraft.FissionHeating")
	@ZenRegister
	public static class FissionHeatingMethods {
		
		@ZenMethod
		public static BasicRecipeHandler getRecipeHandler() {
			return NCRecipes.fission_heating;
		}
		
		@ZenMethod
		public static void addRecipe(IIngredient fluidInput, IIngredient fluidOutput, int heatPerInputMB) {
			getRecipeHandler().ctAddRecipe(fluidInput, fluidOutput, heatPerInputMB);
		}
		
		@ZenMethod
		public static void removeRecipeWithInput(IIngredient fluidInput) {
			getRecipeHandler().ctRemoveRecipeWithInput(fluidInput);
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient fluidOutput) {
			getRecipeHandler().ctRemoveRecipeWithOutput(fluidOutput);
		}
		
		@ZenMethod
		public static void removeAllRecipes() {
			getRecipeHandler().ctRemoveAllRecipes();
		}
	}
	
	@ZenClass("mods.nuclearcraft.SaltFission")
	@ZenRegister
	public static class SaltFissionMethods {
		
		@ZenMethod
		public static BasicRecipeHandler getRecipeHandler() {
			return NCRecipes.salt_fission;
		}
		
		@ZenMethod
		public static void addRecipe(IIngredient fluidInput, IIngredient fluidOutput, double time, int heat, double efficiency, int criticality, double decayFactor, boolean selfPriming, double radiation) {
			getRecipeHandler().ctAddRecipe(fluidInput, fluidOutput, time, heat, efficiency, criticality, decayFactor, selfPriming, radiation);
		}
		
		@ZenMethod
		public static void removeRecipeWithInput(IIngredient fluidInput) {
			getRecipeHandler().ctRemoveRecipeWithInput(fluidInput);
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient fluidOutput) {
			getRecipeHandler().ctRemoveRecipeWithOutput(fluidOutput);
		}
		
		@ZenMethod
		public static void removeAllRecipes() {
			getRecipeHandler().ctRemoveAllRecipes();
		}
	}
	
	@ZenClass("mods.nuclearcraft.FissionEmergencyCooling")
	@ZenRegister
	public static class FissionEmergencyCoolingMethods {
		
		@ZenMethod
		public static BasicRecipeHandler getRecipeHandler() {
			return NCRecipes.fission_emergency_cooling;
		}
		
		@ZenMethod
		public static void addRecipe(IIngredient fluidInput, IIngredient fluidOutput, int coolingPerInputMB) {
			getRecipeHandler().ctAddRecipe(fluidInput, fluidOutput, coolingPerInputMB);
		}
		
		@ZenMethod
		public static void removeRecipeWithInput(IIngredient fluidInput) {
			getRecipeHandler().ctRemoveRecipeWithInput(fluidInput);
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient fluidOutput) {
			getRecipeHandler().ctRemoveRecipeWithOutput(fluidOutput);
		}
		
		@ZenMethod
		public static void removeAllRecipes() {
			getRecipeHandler().ctRemoveAllRecipes();
		}
	}
	
	@ZenClass("mods.nuclearcraft.HeatExchanger")
	@ZenRegister
	public static class HeatExchangerMethods {
		
		@ZenMethod
		public static BasicRecipeHandler getRecipeHandler() {
			return NCRecipes.heat_exchanger;
		}
		
		@ZenMethod
		public static void addRecipe(IIngredient fluidInput, IIngredient fluidOutput, double heatDifference, int temperatureIn, int temperatureOut, @Optional boolean latentHeating, @Optional int preferredFlowDirection, @Optional double flowDirectionBonus) {
			getRecipeHandler().ctAddRecipe(fluidInput, fluidOutput, heatDifference, temperatureIn, temperatureOut, latentHeating, preferredFlowDirection, flowDirectionBonus);
		}
		
		@ZenMethod
		public static void removeRecipeWithInput(IIngredient fluidInput) {
			getRecipeHandler().ctRemoveRecipeWithInput(fluidInput);
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient fluidOutput) {
			getRecipeHandler().ctRemoveRecipeWithOutput(fluidOutput);
		}
		
		@ZenMethod
		public static void removeAllRecipes() {
			getRecipeHandler().ctRemoveAllRecipes();
		}
	}
	
	@ZenClass("mods.nuclearcraft.Turbine")
	@ZenRegister
	public static class TurbineMethods {
		
		@ZenMethod
		public static BasicRecipeHandler getRecipeHandler() {
			return NCRecipes.turbine;
		}
		
		@ZenMethod
		public static void addRecipe(IIngredient fluidInput, IIngredient fluidOutput, double powerPerMB, double expansionLevel, double spinUpMultiplier, @Optional(value = "cloud") String particleEffect, @Optional(valueDouble = 5D / 116D) double particleSpeedMultiplier) {
			getRecipeHandler().ctAddRecipe(fluidInput, fluidOutput, powerPerMB, expansionLevel, spinUpMultiplier, particleEffect, particleSpeedMultiplier);
		}
		
		@ZenMethod
		public static void removeRecipeWithInput(IIngredient fluidInput) {
			getRecipeHandler().ctRemoveRecipeWithInput(fluidInput);
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient fluidOutput) {
			getRecipeHandler().ctRemoveRecipeWithOutput(fluidOutput);
		}
		
		@ZenMethod
		public static void removeAllRecipes() {
			getRecipeHandler().ctRemoveAllRecipes();
		}
	}
	
	@ZenClass("mods.nuclearcraft.RadiationScrubber")
	@ZenRegister
	public static class RadiationScrubberMethods {
		
		@ZenMethod
		public static BasicRecipeHandler getRecipeHandler() {
			return NCRecipes.radiation_scrubber;
		}
		
		@ZenMethod
		public static void addRecipe(IIngredient itemInput, IIngredient fluidInput, IIngredient itemOutput, IIngredient fluidOutput, long processTime, long processPower, double processEfficiency) {
			getRecipeHandler().ctAddRecipe(itemInput, fluidInput, itemOutput, fluidOutput, processTime, processPower, processEfficiency);
		}
		
		@ZenMethod
		public static void removeRecipeWithInput(IIngredient itemInput, IIngredient fluidInput) {
			getRecipeHandler().ctRemoveRecipeWithInput(itemInput, fluidInput);
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient itemOutput, IIngredient fluidOutput) {
			getRecipeHandler().ctRemoveRecipeWithOutput(itemOutput, fluidOutput);
		}
		
		@ZenMethod
		public static void removeAllRecipes() {
			getRecipeHandler().ctRemoveAllRecipes();
		}
	}
	
	@ZenClass("mods.nuclearcraft.RadiationBlockMutation")
	@ZenRegister
	public static class RadiationBlockMutationMethods {
		
		@ZenMethod
		public static BasicRecipeHandler getRecipeHandler() {
			return NCRecipes.radiation_block_mutation;
		}
		
		@ZenMethod
		public static void addRecipe(IIngredient blockInput, IIngredient blockOutput, double radiationThreshold) {
			getRecipeHandler().ctAddRecipe(blockInput, blockOutput, radiationThreshold);
		}
		
		@ZenMethod
		public static void removeRecipeWithInput(IIngredient blockInput) {
			getRecipeHandler().ctRemoveRecipeWithInput(blockInput);
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient blockOutput) {
			getRecipeHandler().ctRemoveRecipeWithOutput(blockOutput);
		}
		
		@ZenMethod
		public static void removeAllRecipes() {
			getRecipeHandler().ctRemoveAllRecipes();
		}
	}
	
	@ZenClass("mods.nuclearcraft.RadiationBlockPurification")
	@ZenRegister
	public static class RadiationBlockPurificationMethods {
		
		@ZenMethod
		public static BasicRecipeHandler getRecipeHandler() {
			return NCRecipes.radiation_block_purification;
		}
		
		@ZenMethod
		public static void addRecipe(IIngredient blockInput, IIngredient blockOutput, double radiationThreshold) {
			getRecipeHandler().ctAddRecipe(blockInput, blockOutput, radiationThreshold);
		}
		
		@ZenMethod
		public static void removeRecipeWithInput(IIngredient blockInput) {
			getRecipeHandler().ctRemoveRecipeWithInput(blockInput);
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient blockOutput) {
			getRecipeHandler().ctRemoveRecipeWithOutput(blockOutput);
		}
		
		@ZenMethod
		public static void removeAllRecipes() {
			getRecipeHandler().ctRemoveAllRecipes();
		}
	}
}
