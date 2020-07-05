package nc.integration.crafttweaker;

import static nc.recipe.NCRecipes.*;

import com.google.common.collect.Lists;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.entity.IEntityLivingBase;
import crafttweaker.api.item.*;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.api.oredict.IOreDictEntry;
import nc.capability.radiation.entity.IEntityRads;
import nc.radiation.*;
import nc.recipe.*;
import nc.recipe.ingredient.*;
import net.minecraft.client.util.RecipeItemHelper;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.*;

public class NCCraftTweaker {
	
	@ZenClass("mods.nuclearcraft.Manufactory")
	@ZenRegister
	public static class ManufactoryMethods {
		
		@ZenMethod
		public static ProcessorRecipeHandler getRecipeHandler() {
			return manufactory;
		}
		
		@ZenMethod
		public static void addRecipe(IIngredient input, IIngredient output, @Optional(valueDouble = 1D) double timeMultiplier, @Optional(valueDouble = 1D) double powerMultiplier, @Optional double processRadiation) {
			CraftTweakerAPI.apply(new AddProcessorRecipe(manufactory, Lists.newArrayList(input, output, timeMultiplier, powerMultiplier, processRadiation)));
		}
		
		@ZenMethod
		public static void removeRecipeWithInput(IIngredient input) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(manufactory, IngredientSorption.INPUT, Lists.newArrayList(input)));
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient output) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(manufactory, IngredientSorption.OUTPUT, Lists.newArrayList(output)));
		}
		
		@ZenMethod
		public static void removeAllRecipes() {
			CraftTweakerAPI.apply(new RemoveAllProcessorRecipes(manufactory));
		}
	}
	
	@ZenClass("mods.nuclearcraft.Separator")
	@ZenRegister
	public static class SeparatorMethods {
		
		@ZenMethod
		public static ProcessorRecipeHandler getRecipeHandler() {
			return separator;
		}
		
		@ZenMethod
		public static void addRecipe(IIngredient input, IIngredient output1, IIngredient output2, @Optional(valueDouble = 1D) double timeMultiplier, @Optional(valueDouble = 1D) double powerMultiplier, @Optional double processRadiation) {
			CraftTweakerAPI.apply(new AddProcessorRecipe(separator, Lists.newArrayList(input, output1, output2, timeMultiplier, powerMultiplier, processRadiation)));
		}
		
		@ZenMethod
		public static void removeRecipeWithInput(IIngredient input) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(separator, IngredientSorption.INPUT, Lists.newArrayList(input)));
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient output1, IIngredient output2) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(separator, IngredientSorption.OUTPUT, Lists.newArrayList(output1, output2)));
		}
		
		@ZenMethod
		public static void removeAllRecipes() {
			CraftTweakerAPI.apply(new RemoveAllProcessorRecipes(separator));
		}
	}
	
	@ZenClass("mods.nuclearcraft.DecayHastener")
	@ZenRegister
	public static class DecayHastenerMethods {
		
		@ZenMethod
		public static ProcessorRecipeHandler getRecipeHandler() {
			return decay_hastener;
		}
		
		@ZenMethod
		public static void addRecipe(IIngredient input, IIngredient output, @Optional(valueDouble = 1D) double timeMultiplier, @Optional(valueDouble = 1D) double powerMultiplier, @Optional double processRadiation) {
			CraftTweakerAPI.apply(new AddProcessorRecipe(decay_hastener, Lists.newArrayList(input, output, timeMultiplier, powerMultiplier, processRadiation)));
		}
		
		@ZenMethod
		public static void removeRecipeWithInput(IIngredient input) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(decay_hastener, IngredientSorption.INPUT, Lists.newArrayList(input)));
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient output) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(decay_hastener, IngredientSorption.OUTPUT, Lists.newArrayList(output)));
		}
		
		@ZenMethod
		public static void removeAllRecipes() {
			CraftTweakerAPI.apply(new RemoveAllProcessorRecipes(decay_hastener));
		}
	}
	
	@ZenClass("mods.nuclearcraft.FuelReprocessor")
	@ZenRegister
	public static class FuelReprocessorMethods {
		
		@ZenMethod
		public static ProcessorRecipeHandler getRecipeHandler() {
			return fuel_reprocessor;
		}
		
		@ZenMethod
		public static void addRecipe(IIngredient input, IIngredient output1, IIngredient output2, IIngredient output3, IIngredient output4, IIngredient output5, IIngredient output6, @Optional(valueDouble = 1D) double timeMultiplier, @Optional(valueDouble = 1D) double powerMultiplier, @Optional double processRadiation) {
			CraftTweakerAPI.apply(new AddProcessorRecipe(fuel_reprocessor, Lists.newArrayList(input, output1, output2, output3, output4, output5, output6, timeMultiplier, powerMultiplier, processRadiation)));
		}
		
		@ZenMethod
		public static void removeRecipeWithInput(IIngredient input) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(fuel_reprocessor, IngredientSorption.INPUT, Lists.newArrayList(input)));
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient output1, IIngredient output2, IIngredient output3, IIngredient output4, IIngredient output5, IIngredient output6) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(fuel_reprocessor, IngredientSorption.OUTPUT, Lists.newArrayList(output1, output2, output3, output4, output5, output6)));
		}
		
		@ZenMethod
		public static void removeAllRecipes() {
			CraftTweakerAPI.apply(new RemoveAllProcessorRecipes(fuel_reprocessor));
		}
	}
	
	@ZenClass("mods.nuclearcraft.AlloyFurnace")
	@ZenRegister
	public static class AlloyFurnaceMethods {
		
		@ZenMethod
		public static ProcessorRecipeHandler getRecipeHandler() {
			return alloy_furnace;
		}
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient input2, IIngredient output, @Optional(valueDouble = 1D) double timeMultiplier, @Optional(valueDouble = 1D) double powerMultiplier, @Optional double processRadiation) {
			CraftTweakerAPI.apply(new AddProcessorRecipe(alloy_furnace, Lists.newArrayList(input1, input2, output, timeMultiplier, powerMultiplier, processRadiation)));
		}
		
		@ZenMethod
		public static void removeRecipeWithInput(IIngredient input1, IIngredient input2) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(alloy_furnace, IngredientSorption.INPUT, Lists.newArrayList(input1, input2)));
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient output) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(alloy_furnace, IngredientSorption.OUTPUT, Lists.newArrayList(output)));
		}
		
		@ZenMethod
		public static void removeAllRecipes() {
			CraftTweakerAPI.apply(new RemoveAllProcessorRecipes(alloy_furnace));
		}
	}
	
	@ZenClass("mods.nuclearcraft.Infuser")
	@ZenRegister
	public static class InfuserMethods {
		
		@ZenMethod
		public static ProcessorRecipeHandler getRecipeHandler() {
			return infuser;
		}
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient input2, IIngredient output, @Optional(valueDouble = 1D) double timeMultiplier, @Optional(valueDouble = 1D) double powerMultiplier, @Optional double processRadiation) {
			CraftTweakerAPI.apply(new AddProcessorRecipe(infuser, Lists.newArrayList(input1, input2, output, timeMultiplier, powerMultiplier, processRadiation)));
		}
		
		@ZenMethod
		public static void removeRecipeWithInput(IIngredient input1, IIngredient input2) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(infuser, IngredientSorption.INPUT, Lists.newArrayList(input1, input2)));
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient output) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(infuser, IngredientSorption.OUTPUT, Lists.newArrayList(output)));
		}
		
		@ZenMethod
		public static void removeAllRecipes() {
			CraftTweakerAPI.apply(new RemoveAllProcessorRecipes(infuser));
		}
	}
	
	@ZenClass("mods.nuclearcraft.Melter")
	@ZenRegister
	public static class MelterMethods {
		
		@ZenMethod
		public static ProcessorRecipeHandler getRecipeHandler() {
			return melter;
		}
		
		@ZenMethod
		public static void addRecipe(IIngredient input, IIngredient output, @Optional(valueDouble = 1D) double timeMultiplier, @Optional(valueDouble = 1D) double powerMultiplier, @Optional double processRadiation) {
			CraftTweakerAPI.apply(new AddProcessorRecipe(melter, Lists.newArrayList(input, output, timeMultiplier, powerMultiplier, processRadiation)));
		}
		
		@ZenMethod
		public static void removeRecipeWithInput(IIngredient input) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(melter, IngredientSorption.INPUT, Lists.newArrayList(input)));
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient output) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(melter, IngredientSorption.OUTPUT, Lists.newArrayList(output)));
		}
		
		@ZenMethod
		public static void removeAllRecipes() {
			CraftTweakerAPI.apply(new RemoveAllProcessorRecipes(melter));
		}
	}
	
	@ZenClass("mods.nuclearcraft.Supercooler")
	@ZenRegister
	public static class SupercoolerMethods {
		
		@ZenMethod
		public static ProcessorRecipeHandler getRecipeHandler() {
			return supercooler;
		}
		
		@ZenMethod
		public static void addRecipe(IIngredient input, IIngredient output, @Optional(valueDouble = 1D) double timeMultiplier, @Optional(valueDouble = 1D) double powerMultiplier, @Optional double processRadiation) {
			CraftTweakerAPI.apply(new AddProcessorRecipe(supercooler, Lists.newArrayList(input, output, timeMultiplier, powerMultiplier, processRadiation)));
		}
		
		@ZenMethod
		public static void removeRecipeWithInput(IIngredient input) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(supercooler, IngredientSorption.INPUT, Lists.newArrayList(input)));
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient output) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(supercooler, IngredientSorption.OUTPUT, Lists.newArrayList(output)));
		}
		
		@ZenMethod
		public static void removeAllRecipes() {
			CraftTweakerAPI.apply(new RemoveAllProcessorRecipes(supercooler));
		}
	}
	
	@ZenClass("mods.nuclearcraft.Electrolyzer")
	@ZenRegister
	public static class ElectrolyzerMethods {
		
		@ZenMethod
		public static ProcessorRecipeHandler getRecipeHandler() {
			return electrolyzer;
		}
		
		@ZenMethod
		public static void addRecipe(IIngredient input, IIngredient output1, IIngredient output2, IIngredient output3, IIngredient output4, @Optional(valueDouble = 1D) double timeMultiplier, @Optional(valueDouble = 1D) double powerMultiplier, @Optional double processRadiation) {
			CraftTweakerAPI.apply(new AddProcessorRecipe(electrolyzer, Lists.newArrayList(input, output1, output2, output3, output4, timeMultiplier, powerMultiplier, processRadiation)));
		}
		
		@ZenMethod
		public static void removeRecipeWithInput(IIngredient input) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(electrolyzer, IngredientSorption.INPUT, Lists.newArrayList(input)));
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient output1, IIngredient output2, IIngredient output3, IIngredient output4) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(electrolyzer, IngredientSorption.OUTPUT, Lists.newArrayList(output1, output2, output3, output4)));
		}
		
		@ZenMethod
		public static void removeAllRecipes() {
			CraftTweakerAPI.apply(new RemoveAllProcessorRecipes(electrolyzer));
		}
	}
	
	@ZenClass("mods.nuclearcraft.Assembler")
	@ZenRegister
	public static class AssemblerMethods {
		
		@ZenMethod
		public static ProcessorRecipeHandler getRecipeHandler() {
			return assembler;
		}
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient input2, IIngredient input3, IIngredient input4, IIngredient output, @Optional(valueDouble = 1D) double timeMultiplier, @Optional(valueDouble = 1D) double powerMultiplier, @Optional double processRadiation) {
			CraftTweakerAPI.apply(new AddProcessorRecipe(assembler, Lists.newArrayList(input1, input2, input3, input4, output, timeMultiplier, powerMultiplier, processRadiation)));
		}
		
		@ZenMethod
		public static void removeRecipeWithInput(IIngredient input1, IIngredient input2, IIngredient input3, IIngredient input4) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(assembler, IngredientSorption.INPUT, Lists.newArrayList(input1, input2, input3, input4)));
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient output) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(assembler, IngredientSorption.OUTPUT, Lists.newArrayList(output)));
		}
		
		@ZenMethod
		public static void removeAllRecipes() {
			CraftTweakerAPI.apply(new RemoveAllProcessorRecipes(assembler));
		}
	}
	
	@ZenClass("mods.nuclearcraft.IngotFormer")
	@ZenRegister
	public static class IngotFormerMethods {
		
		@ZenMethod
		public static ProcessorRecipeHandler getRecipeHandler() {
			return ingot_former;
		}
		
		@ZenMethod
		public static void addRecipe(IIngredient input, IIngredient output, @Optional(valueDouble = 1D) double timeMultiplier, @Optional(valueDouble = 1D) double powerMultiplier, @Optional double processRadiation) {
			CraftTweakerAPI.apply(new AddProcessorRecipe(ingot_former, Lists.newArrayList(input, output, timeMultiplier, powerMultiplier, processRadiation)));
		}
		
		@ZenMethod
		public static void removeRecipeWithInput(IIngredient input) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(ingot_former, IngredientSorption.INPUT, Lists.newArrayList(input)));
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient output) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(ingot_former, IngredientSorption.OUTPUT, Lists.newArrayList(output)));
		}
		
		@ZenMethod
		public static void removeAllRecipes() {
			CraftTweakerAPI.apply(new RemoveAllProcessorRecipes(ingot_former));
		}
	}
	
	@ZenClass("mods.nuclearcraft.Pressurizer")
	@ZenRegister
	public static class PressurizerMethods {
		
		@ZenMethod
		public static ProcessorRecipeHandler getRecipeHandler() {
			return pressurizer;
		}
		
		@ZenMethod
		public static void addRecipe(IIngredient input, IIngredient output, @Optional(valueDouble = 1D) double timeMultiplier, @Optional(valueDouble = 1D) double powerMultiplier, @Optional double processRadiation) {
			CraftTweakerAPI.apply(new AddProcessorRecipe(pressurizer, Lists.newArrayList(input, output, timeMultiplier, powerMultiplier, processRadiation)));
		}
		
		@ZenMethod
		public static void removeRecipeWithInput(IIngredient input) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(pressurizer, IngredientSorption.INPUT, Lists.newArrayList(input)));
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient output) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(pressurizer, IngredientSorption.OUTPUT, Lists.newArrayList(output)));
		}
		
		@ZenMethod
		public static void removeAllRecipes() {
			CraftTweakerAPI.apply(new RemoveAllProcessorRecipes(pressurizer));
		}
	}
	
	@ZenClass("mods.nuclearcraft.ChemicalReactor")
	@ZenRegister
	public static class ChemicalReactorMethods {
		
		@ZenMethod
		public static ProcessorRecipeHandler getRecipeHandler() {
			return chemical_reactor;
		}
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient input2, IIngredient output1, IIngredient output2, @Optional(valueDouble = 1D) double timeMultiplier, @Optional(valueDouble = 1D) double powerMultiplier, @Optional double processRadiation) {
			CraftTweakerAPI.apply(new AddProcessorRecipe(chemical_reactor, Lists.newArrayList(input1, input2, output1, output2, timeMultiplier, powerMultiplier, processRadiation)));
		}
		
		@ZenMethod
		public static void removeRecipeWithInput(IIngredient input1, IIngredient input2) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(chemical_reactor, IngredientSorption.INPUT, Lists.newArrayList(input1, input2)));
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient output1, IIngredient output2) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(chemical_reactor, IngredientSorption.OUTPUT, Lists.newArrayList(output1, output2)));
		}
		
		@ZenMethod
		public static void removeAllRecipes() {
			CraftTweakerAPI.apply(new RemoveAllProcessorRecipes(chemical_reactor));
		}
	}
	
	@ZenClass("mods.nuclearcraft.SaltMixer")
	@ZenRegister
	public static class SaltMixerMethods {
		
		@ZenMethod
		public static ProcessorRecipeHandler getRecipeHandler() {
			return salt_mixer;
		}
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient input2, IIngredient output, @Optional(valueDouble = 1D) double timeMultiplier, @Optional(valueDouble = 1D) double powerMultiplier, @Optional double processRadiation) {
			CraftTweakerAPI.apply(new AddProcessorRecipe(salt_mixer, Lists.newArrayList(input1, input2, output, timeMultiplier, powerMultiplier, processRadiation)));
		}
		
		@ZenMethod
		public static void removeRecipeWithInput(IIngredient input1, IIngredient input2) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(salt_mixer, IngredientSorption.INPUT, Lists.newArrayList(input1, input2)));
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient output) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(salt_mixer, IngredientSorption.OUTPUT, Lists.newArrayList(output)));
		}
		
		@ZenMethod
		public static void removeAllRecipes() {
			CraftTweakerAPI.apply(new RemoveAllProcessorRecipes(salt_mixer));
		}
	}
	
	@ZenClass("mods.nuclearcraft.Crystallizer")
	@ZenRegister
	public static class CrystallizerMethods {
		
		@ZenMethod
		public static ProcessorRecipeHandler getRecipeHandler() {
			return crystallizer;
		}
		
		@ZenMethod
		public static void addRecipe(IIngredient input, IIngredient output, @Optional(valueDouble = 1D) double timeMultiplier, @Optional(valueDouble = 1D) double powerMultiplier, @Optional double processRadiation) {
			CraftTweakerAPI.apply(new AddProcessorRecipe(crystallizer, Lists.newArrayList(input, output, timeMultiplier, powerMultiplier, processRadiation)));
		}
		
		@ZenMethod
		public static void removeRecipeWithInput(IIngredient input) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(crystallizer, IngredientSorption.INPUT, Lists.newArrayList(input)));
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient output) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(crystallizer, IngredientSorption.OUTPUT, Lists.newArrayList(output)));
		}
		
		@ZenMethod
		public static void removeAllRecipes() {
			CraftTweakerAPI.apply(new RemoveAllProcessorRecipes(crystallizer));
		}
	}
	
	@ZenClass("mods.nuclearcraft.Enricher")
	@ZenRegister
	public static class EnricherMethods {
		
		@ZenMethod
		public static ProcessorRecipeHandler getRecipeHandler() {
			return enricher;
		}
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient input2, IIngredient output, @Optional(valueDouble = 1D) double timeMultiplier, @Optional(valueDouble = 1D) double powerMultiplier, @Optional double processRadiation) {
			CraftTweakerAPI.apply(new AddProcessorRecipe(enricher, Lists.newArrayList(input1, input2, output, timeMultiplier, powerMultiplier, processRadiation)));
		}
		
		@ZenMethod
		public static void removeRecipeWithInput(IIngredient input1, IIngredient input2) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(enricher, IngredientSorption.INPUT, Lists.newArrayList(input1, input2)));
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient output) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(enricher, IngredientSorption.OUTPUT, Lists.newArrayList(output)));
		}
		
		@ZenMethod
		public static void removeAllRecipes() {
			CraftTweakerAPI.apply(new RemoveAllProcessorRecipes(enricher));
		}
	}
	
	@ZenClass("mods.nuclearcraft.Extractor")
	@ZenRegister
	public static class ExtractorMethods {
		
		@ZenMethod
		public static ProcessorRecipeHandler getRecipeHandler() {
			return extractor;
		}
		
		@ZenMethod
		public static void addRecipe(IIngredient input, IIngredient output1, IIngredient output2, @Optional(valueDouble = 1D) double timeMultiplier, @Optional(valueDouble = 1D) double powerMultiplier, @Optional double processRadiation) {
			CraftTweakerAPI.apply(new AddProcessorRecipe(extractor, Lists.newArrayList(input, output1, output2, timeMultiplier, powerMultiplier, processRadiation)));
		}
		
		@ZenMethod
		public static void removeRecipeWithInput(IIngredient input) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(extractor, IngredientSorption.INPUT, Lists.newArrayList(input)));
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient output1, IIngredient output2) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(extractor, IngredientSorption.OUTPUT, Lists.newArrayList(output1, output2)));
		}
		
		@ZenMethod
		public static void removeAllRecipes() {
			CraftTweakerAPI.apply(new RemoveAllProcessorRecipes(extractor));
		}
	}
	
	@ZenClass("mods.nuclearcraft.Centrifuge")
	@ZenRegister
	public static class CentrifugeMethods {
		
		@ZenMethod
		public static ProcessorRecipeHandler getRecipeHandler() {
			return centrifuge;
		}
		
		@ZenMethod
		public static void addRecipe(IIngredient input, IIngredient output1, IIngredient output2, IIngredient output3, IIngredient output4, @Optional(valueDouble = 1D) double timeMultiplier, @Optional(valueDouble = 1D) double powerMultiplier, @Optional double processRadiation) {
			CraftTweakerAPI.apply(new AddProcessorRecipe(centrifuge, Lists.newArrayList(input, output1, output2, output3, output4, timeMultiplier, powerMultiplier, processRadiation)));
		}
		
		@ZenMethod
		public static void removeRecipeWithInput(IIngredient input) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(centrifuge, IngredientSorption.INPUT, Lists.newArrayList(input)));
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient output1, IIngredient output2, IIngredient output3, IIngredient output4) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(centrifuge, IngredientSorption.OUTPUT, Lists.newArrayList(output1, output2, output3, output4)));
		}
		
		@ZenMethod
		public static void removeAllRecipes() {
			CraftTweakerAPI.apply(new RemoveAllProcessorRecipes(centrifuge));
		}
	}
	
	@ZenClass("mods.nuclearcraft.RockCrusher")
	@ZenRegister
	public static class RockCrusherMethods {
		
		@ZenMethod
		public static ProcessorRecipeHandler getRecipeHandler() {
			return rock_crusher;
		}
		
		@ZenMethod
		public static void addRecipe(IIngredient input, IIngredient output1, IIngredient output2, IIngredient output3, @Optional(valueDouble = 1D) double timeMultiplier, @Optional(valueDouble = 1D) double powerMultiplier, @Optional double processRadiation) {
			CraftTweakerAPI.apply(new AddProcessorRecipe(rock_crusher, Lists.newArrayList(input, output1, output2, output3, timeMultiplier, powerMultiplier, processRadiation)));
		}
		
		@ZenMethod
		public static void removeRecipeWithInput(IIngredient input) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(rock_crusher, IngredientSorption.INPUT, Lists.newArrayList(input)));
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient output1, IIngredient output2, IIngredient output3) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(rock_crusher, IngredientSorption.OUTPUT, Lists.newArrayList(output1, output2, output3)));
		}
		
		@ZenMethod
		public static void removeAllRecipes() {
			CraftTweakerAPI.apply(new RemoveAllProcessorRecipes(rock_crusher));
		}
	}
	
	@ZenClass("mods.nuclearcraft.DecayGenerator")
	@ZenRegister
	public static class DecayGeneratorMethods {
		
		@ZenMethod
		public static ProcessorRecipeHandler getRecipeHandler() {
			return decay_generator;
		}
		
		@ZenMethod
		public static void addRecipe(IIngredient input, IIngredient output, double meanLifetime, double power, double radiation) {
			CraftTweakerAPI.apply(new AddProcessorRecipe(decay_generator, Lists.newArrayList(input, output, meanLifetime, power, radiation)));
		}
		
		@ZenMethod
		public static void removeRecipeWithInput(IIngredient input) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(decay_generator, IngredientSorption.INPUT, Lists.newArrayList(input)));
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient output) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(decay_generator, IngredientSorption.OUTPUT, Lists.newArrayList(output)));
		}
		
		@ZenMethod
		public static void removeAllRecipes() {
			CraftTweakerAPI.apply(new RemoveAllProcessorRecipes(decay_generator));
		}
	}
	
	@ZenClass("mods.nuclearcraft.FissionModerator")
	@ZenRegister
	public static class FissionModeratorMethods {
		
		@ZenMethod
		public static ProcessorRecipeHandler getRecipeHandler() {
			return fission_moderator;
		}
		
		@ZenMethod
		public static void add(IIngredient input, int fluxFactor, double efficiency) {
			CraftTweakerAPI.apply(new AddProcessorRecipe(fission_moderator, Lists.newArrayList(input, fluxFactor, efficiency)));
		}
		
		@ZenMethod
		public static void remove(IIngredient input) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(fission_moderator, IngredientSorption.INPUT, Lists.newArrayList(input)));
		}
		
		@ZenMethod
		public static void removeAll() {
			CraftTweakerAPI.apply(new RemoveAllProcessorRecipes(fission_moderator));
		}
	}
	
	@ZenClass("mods.nuclearcraft.FissionReflector")
	@ZenRegister
	public static class FissionReflectorMethods {
		
		@ZenMethod
		public static ProcessorRecipeHandler getRecipeHandler() {
			return fission_reflector;
		}
		
		@ZenMethod
		public static void add(IIngredient input, double efficiency, double reflectivity) {
			CraftTweakerAPI.apply(new AddProcessorRecipe(fission_reflector, Lists.newArrayList(input, efficiency, reflectivity)));
		}
		
		@ZenMethod
		public static void remove(IIngredient input) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(fission_reflector, IngredientSorption.INPUT, Lists.newArrayList(input)));
		}
		
		@ZenMethod
		public static void removeAll() {
			CraftTweakerAPI.apply(new RemoveAllProcessorRecipes(fission_reflector));
		}
	}
	
	@ZenClass("mods.nuclearcraft.FissionIrradiator")
	@ZenRegister
	public static class FissionIrradiatorMethods {
		
		@ZenMethod
		public static ProcessorRecipeHandler getRecipeHandler() {
			return fission_irradiator;
		}
		
		@ZenMethod
		public static void addRecipe(IIngredient input, IIngredient output, int fluxRequired, double heatPerFlux, double efficiency, double radiation) {
			CraftTweakerAPI.apply(new AddProcessorRecipe(fission_irradiator, Lists.newArrayList(input, output, fluxRequired, heatPerFlux, efficiency, radiation)));
		}
		
		@ZenMethod
		public static void removeRecipeWithInput(IIngredient input) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(fission_irradiator, IngredientSorption.INPUT, Lists.newArrayList(input)));
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient output) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(fission_irradiator, IngredientSorption.OUTPUT, Lists.newArrayList(output)));
		}
		
		@ZenMethod
		public static void removeAllRecipes() {
			CraftTweakerAPI.apply(new RemoveAllProcessorRecipes(fission_irradiator));
		}
	}
	
	@ZenClass("mods.nuclearcraft.PebbleFission")
	@ZenRegister
	public static class PebbleFissionMethods {
		
		@ZenMethod
		public static ProcessorRecipeHandler getRecipeHandler() {
			return pebble_fission;
		}
		
		@ZenMethod
		public static void addRecipe(IIngredient input, IIngredient output, int time, int heat, double efficiency, int criticality, boolean selfPriming, double radiation) {
			CraftTweakerAPI.apply(new AddProcessorRecipe(pebble_fission, Lists.newArrayList(input, output, time, heat, efficiency, criticality, selfPriming, radiation)));
		}
		
		@ZenMethod
		public static void removeRecipeWithInput(IIngredient input) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(pebble_fission, IngredientSorption.INPUT, Lists.newArrayList(input)));
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient output) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(pebble_fission, IngredientSorption.OUTPUT, Lists.newArrayList(output)));
		}
		
		@ZenMethod
		public static void removeAllRecipes() {
			CraftTweakerAPI.apply(new RemoveAllProcessorRecipes(pebble_fission));
		}
	}
	
	@ZenClass("mods.nuclearcraft.SolidFission")
	@ZenRegister
	public static class SolidFissionMethods {
		
		@ZenMethod
		public static ProcessorRecipeHandler getRecipeHandler() {
			return solid_fission;
		}
		
		@ZenMethod
		public static void addRecipe(IIngredient input, IIngredient output, int time, int heat, double efficiency, int criticality, boolean selfPriming, double radiation) {
			CraftTweakerAPI.apply(new AddProcessorRecipe(solid_fission, Lists.newArrayList(input, output, time, heat, efficiency, criticality, selfPriming, radiation)));
		}
		
		@ZenMethod
		public static void removeRecipeWithInput(IIngredient input) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(solid_fission, IngredientSorption.INPUT, Lists.newArrayList(input)));
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient output) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(solid_fission, IngredientSorption.OUTPUT, Lists.newArrayList(output)));
		}
		
		@ZenMethod
		public static void removeAllRecipes() {
			CraftTweakerAPI.apply(new RemoveAllProcessorRecipes(solid_fission));
		}
	}
	
	@ZenClass("mods.nuclearcraft.FissionHeating")
	@ZenRegister
	public static class FissionHeatingMethods {
		
		@ZenMethod
		public static ProcessorRecipeHandler getRecipeHandler() {
			return fission_heating;
		}
		
		@ZenMethod
		public static void addRecipe(IIngredient input, IIngredient output, int heatPerInputMB) {
			CraftTweakerAPI.apply(new AddProcessorRecipe(fission_heating, Lists.newArrayList(input, output, heatPerInputMB)));
		}
		
		@ZenMethod
		public static void removeRecipeWithInput(IIngredient input) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(fission_heating, IngredientSorption.INPUT, Lists.newArrayList(input)));
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient output) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(fission_heating, IngredientSorption.OUTPUT, Lists.newArrayList(output)));
		}
		
		@ZenMethod
		public static void removeAllRecipes() {
			CraftTweakerAPI.apply(new RemoveAllProcessorRecipes(fission_heating));
		}
	}
	
	// TODO
	@ZenClass("mods.nuclearcraft.SaltFission")
	@ZenRegister
	public static class SaltFissionMethods {
		
		@ZenMethod
		public static ProcessorRecipeHandler getRecipeHandler() {
			return salt_fission;
		}
		
		@ZenMethod
		public static void addRecipe(IIngredient input, IIngredient output, double time, int heat, double efficiency, int criticality, boolean selfPriming, double radiation) {
			CraftTweakerAPI.apply(new AddProcessorRecipe(salt_fission, Lists.newArrayList(input, output, time, heat, efficiency, criticality, selfPriming, radiation)));
		}
		
		@ZenMethod
		public static void removeRecipeWithInput(IIngredient input) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(salt_fission, IngredientSorption.INPUT, Lists.newArrayList(input)));
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient output) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(salt_fission, IngredientSorption.OUTPUT, Lists.newArrayList(output)));
		}
		
		@ZenMethod
		public static void removeAllRecipes() {
			CraftTweakerAPI.apply(new RemoveAllProcessorRecipes(salt_fission));
		}
	}
	
	// TODO
	@ZenClass("mods.nuclearcraft.Fusion")
	@ZenRegister
	public static class FusionMethods {
		
		@ZenMethod
		public static ProcessorRecipeHandler getRecipeHandler() {
			return fusion;
		}
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient input2, IIngredient output1, IIngredient output2, IIngredient output3, IIngredient output4, int time, int power, int optimalTemp) {
			CraftTweakerAPI.apply(new AddProcessorRecipe(fusion, Lists.newArrayList(input1, input2, output1, output2, output3, output4, time, power, optimalTemp)));
		}
		
		@ZenMethod
		public static void removeRecipeWithInput(IIngredient input1, IIngredient input2) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(fusion, IngredientSorption.INPUT, Lists.newArrayList(input1, input2)));
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient output1, IIngredient output2, IIngredient output3, IIngredient output4) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(fusion, IngredientSorption.OUTPUT, Lists.newArrayList(output1, output2, output3, output4)));
		}
		
		@ZenMethod
		public static void removeAllRecipes() {
			CraftTweakerAPI.apply(new RemoveAllProcessorRecipes(fusion));
		}
	}
	
	// TODO
	@ZenClass("mods.nuclearcraft.HeatExchanger")
	@ZenRegister
	public static class HeatExchangerMethods {
		
		@ZenMethod
		public static ProcessorRecipeHandler getRecipeHandler() {
			return heat_exchanger;
		}
		
		@ZenMethod
		public static void addRecipe(Object[] objects) {
			CraftTweakerAPI.apply(new AddProcessorRecipe(heat_exchanger, Lists.newArrayList(objects)));
		}
		
		@ZenMethod
		public static void removeRecipeWithInput(IIngredient[] inputs) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(heat_exchanger, IngredientSorption.INPUT, Lists.newArrayList(inputs)));
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient[] outputs) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(heat_exchanger, IngredientSorption.OUTPUT, Lists.newArrayList(outputs)));
		}
		
		@ZenMethod
		public static void removeAllRecipes() {
			CraftTweakerAPI.apply(new RemoveAllProcessorRecipes(heat_exchanger));
		}
	}
	
	// TODO
	@ZenClass("mods.nuclearcraft.Condenser")
	@ZenRegister
	public static class CondenserMethods {
		
		@ZenMethod
		public static ProcessorRecipeHandler getRecipeHandler() {
			return condenser;
		}
		
		@ZenMethod
		public static void addRecipe(Object[] objects) {
			CraftTweakerAPI.apply(new AddProcessorRecipe(condenser, Lists.newArrayList(objects)));
		}
		
		@ZenMethod
		public static void removeRecipeWithInput(IIngredient[] inputs) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(condenser, IngredientSorption.INPUT, Lists.newArrayList(inputs)));
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient[] outputs) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(condenser, IngredientSorption.OUTPUT, Lists.newArrayList(outputs)));
		}
		
		@ZenMethod
		public static void removeAllRecipes() {
			CraftTweakerAPI.apply(new RemoveAllProcessorRecipes(condenser));
		}
	}
	
	@ZenClass("mods.nuclearcraft.Turbine")
	@ZenRegister
	public static class TurbineMethods {
		
		@ZenMethod
		public static ProcessorRecipeHandler getRecipeHandler() {
			return turbine;
		}
		
		@ZenMethod
		public static void addRecipe(IIngredient input, IIngredient output, double powerPerMB, double expansionLevel) {
			CraftTweakerAPI.apply(new AddProcessorRecipe(turbine, Lists.newArrayList(input, output, powerPerMB, expansionLevel)));
		}
		
		@ZenMethod
		public static void removeRecipeWithInput(IIngredient input) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(turbine, IngredientSorption.INPUT, Lists.newArrayList(input)));
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient output) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(turbine, IngredientSorption.OUTPUT, Lists.newArrayList(output)));
		}
		
		@ZenMethod
		public static void removeAllRecipes() {
			CraftTweakerAPI.apply(new RemoveAllProcessorRecipes(turbine));
		}
	}
	
	@ZenClass("mods.nuclearcraft.RadiationScrubber")
	@ZenRegister
	public static class RadiationScrubberMethods {
		
		@ZenMethod
		public static ProcessorRecipeHandler getRecipeHandler() {
			return radiation_scrubber;
		}
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient input2, IIngredient output1, IIngredient output2, int processTime, int processPower, double processEfficiency) {
			CraftTweakerAPI.apply(new AddProcessorRecipe(radiation_scrubber, Lists.newArrayList(input1, input2, output1, output2, processTime, processPower, processEfficiency)));
		}
		
		@ZenMethod
		public static void removeRecipeWithInput(IIngredient input1, IIngredient input2) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(radiation_scrubber, IngredientSorption.INPUT, Lists.newArrayList(input1, input2)));
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient output1, IIngredient output2) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(radiation_scrubber, IngredientSorption.OUTPUT, Lists.newArrayList(output1, output2)));
		}
		
		@ZenMethod
		public static void removeAllRecipes() {
			CraftTweakerAPI.apply(new RemoveAllProcessorRecipes(radiation_scrubber));
		}
	}
	
	@ZenClass("mods.nuclearcraft.RadiationBlockMutation")
	@ZenRegister
	public static class RadiationBlockMutationMethods {
		
		@ZenMethod
		public static ProcessorRecipeHandler getRecipeHandler() {
			return radiation_block_mutation;
		}
		
		@ZenMethod
		public static void addRecipe(IIngredient input, IIngredient output, double radiationThreshold) {
			CraftTweakerAPI.apply(new AddProcessorRecipe(radiation_block_mutation, Lists.newArrayList(input, output, radiationThreshold)));
		}
		
		@ZenMethod
		public static void removeRecipeWithInput(IIngredient input) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(radiation_block_mutation, IngredientSorption.INPUT, Lists.newArrayList(input)));
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient output) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(radiation_block_mutation, IngredientSorption.OUTPUT, Lists.newArrayList(output)));
		}
		
		@ZenMethod
		public static void removeAllRecipes() {
			CraftTweakerAPI.apply(new RemoveAllProcessorRecipes(radiation_block_mutation));
		}
	}
	
	@ZenClass("mods.nuclearcraft.RadiationBlockPurification")
	@ZenRegister
	public static class RadiationBlockPurificationMethods {
		
		@ZenMethod
		public static ProcessorRecipeHandler getRecipeHandler() {
			return radiation_block_purification;
		}
		
		@ZenMethod
		public static void addRecipe(IIngredient input, IIngredient output, double radiationThreshold) {
			CraftTweakerAPI.apply(new AddProcessorRecipe(radiation_block_purification, Lists.newArrayList(input, output, radiationThreshold)));
		}
		
		@ZenMethod
		public static void removeRecipeWithInput(IIngredient input) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(radiation_block_purification, IngredientSorption.INPUT, Lists.newArrayList(input)));
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient output) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(radiation_block_purification, IngredientSorption.OUTPUT, Lists.newArrayList(output)));
		}
		
		@ZenMethod
		public static void removeAllRecipes() {
			CraftTweakerAPI.apply(new RemoveAllProcessorRecipes(radiation_block_purification));
		}
	}
	
	@ZenClass("mods.nuclearcraft.Radiation")
	@ZenRegister
	public static class RadiationMethods {
		
		@ZenMethod
		public static double getRadiationLevel(IIngredient ingredient) {
			if (ingredient == null) {
				return 0D;
			}
			else if (ingredient instanceof IItemStack) {
				ItemStack stack = CTHelper.getItemStack((IItemStack) ingredient);
				if (stack.isEmpty()) {
					return 0D;
				}
				return RadSources.STACK_MAP.get(RecipeItemHelper.pack(stack)) * stack.getCount();
			}
			else if (ingredient instanceof IOreDictEntry) {
				String oreName = ((IOreDictEntry) ingredient).getName();
				int amount = ((IOreDictEntry) ingredient).getAmount();
				return RadSources.ORE_MAP.getDouble(oreName) * amount;
			}
			else if (ingredient instanceof IngredientStack) {
				IItemIngredient ing = CTHelper.buildOreIngredientArray(ingredient, true);
				if (ing instanceof OreIngredient) {
					String oreName = ((OreIngredient) ing).oreName;
					int amount = ((OreIngredient) ing).stackSize;
					return RadSources.ORE_MAP.getDouble(oreName) * amount;
				}
				else {
					ItemStack stack = ing.getStack();
					if (stack == null || stack.isEmpty()) {
						return 0D;
					}
					return RadSources.STACK_MAP.get(RecipeItemHelper.pack(stack)) * stack.getCount();
				}
			}
			else {
				return 0D;
			}
		}
		
		@ZenMethod
		public static void setRadiationImmunityGameStages(boolean defaultImmunity, String... stageNames) {
			nc.radiation.RadiationHandler.default_rad_immunity = defaultImmunity;
			nc.radiation.RadiationHandler.rad_immunity_stages = stageNames;
			CraftTweakerAPI.logInfo("Added radiation immunity game stages " + Lists.newArrayList(stageNames).toString() + ", with immunity " + (defaultImmunity ? "enabled" : "disabled") + " by default");
		}
	}
	
	@ZenRegister
	@ZenExpansion("crafttweaker.entity.IEntityLivingBase")
	public static class EntityExpansion {
		
		@ZenMethod
		public static void addRadiation(IEntityLivingBase entity, double amount, @Optional boolean useImmunity) {
			IEntityRads rads = entityRads(entity);
			if (rads != null) {
				rads.setTotalRads(rads.getTotalRads() + amount, useImmunity);
			}
		}
		
		@ZenMethod
		public static void setRadiation(IEntityLivingBase entity, double amount, @Optional boolean useImmunity) {
			IEntityRads rads = entityRads(entity);
			if (rads != null) {
				rads.setTotalRads(amount, useImmunity);
			}
		}
		
		@ZenMethod
		public static double getRadiation(IEntityLivingBase entity) {
			IEntityRads rads = entityRads(entity);
			return rads == null ? 0D : rads.getTotalRads();
		}
		
		@ZenMethod
		public static void addRadawayBuffer(IEntityLivingBase entity, double amount, @Optional boolean slow) {
			IEntityRads rads = entityRads(entity);
			if (rads != null) {
				rads.setRadawayBuffer(slow, rads.getRadawayBuffer(slow) + amount);
				if (!slow) {
					rads.setRecentRadawayAddition(Math.abs(amount));
				}
			}
		}
		
		@ZenMethod
		public static void setRadawayBuffer(IEntityLivingBase entity, double amount, @Optional boolean slow) {
			IEntityRads rads = entityRads(entity);
			if (rads != null) {
				rads.setRadawayBuffer(slow, amount);
				if (!slow) {
					rads.setRecentRadawayAddition(Math.abs(amount));
				}
			}
		}
		
		@ZenMethod
		public static double getRadawayBuffer(IEntityLivingBase entity, @Optional boolean slow) {
			IEntityRads rads = entityRads(entity);
			return rads == null ? 0D : rads.getRadawayBuffer(slow);
		}
		
		@ZenMethod
		public static void addPoisonBuffer(IEntityLivingBase entity, double amount) {
			IEntityRads rads = entityRads(entity);
			if (rads != null) {
				rads.setPoisonBuffer(rads.getPoisonBuffer() + amount);
				rads.setRecentPoisonAddition(Math.abs(amount));
			}
		}
		
		@ZenMethod
		public static void setPoisonBuffer(IEntityLivingBase entity, double amount) {
			IEntityRads rads = entityRads(entity);
			if (rads != null) {
				rads.setPoisonBuffer(amount);
				rads.setRecentPoisonAddition(Math.abs(amount));
			}
		}
		
		@ZenMethod
		public static double getPoisonBuffer(IEntityLivingBase entity) {
			IEntityRads rads = entityRads(entity);
			return rads == null ? 0D : rads.getPoisonBuffer();
		}
		
		@ZenMethod
		public static void addRadiationResistance(IEntityLivingBase entity, double amount) {
			IEntityRads rads = entityRads(entity);
			if (rads != null) {
				rads.setInternalRadiationResistance(rads.getInternalRadiationResistance() + amount);
				rads.setRecentRadXAddition(Math.abs(amount));
			}
		}
		
		@ZenMethod
		public static void setRadiationResistance(IEntityLivingBase entity, double amount) {
			IEntityRads rads = entityRads(entity);
			if (rads != null) {
				rads.setInternalRadiationResistance(amount);
				rads.setRecentRadXAddition(Math.abs(amount));
			}
		}
		
		@ZenMethod
		public static double getRadiationResistance(IEntityLivingBase entity) {
			IEntityRads rads = entityRads(entity);
			return rads == null ? 0D : rads.getInternalRadiationResistance();
		}
		
		@ZenMethod
		public static double getRawRadiationLevel(IEntityLivingBase entity) {
			IEntityRads rads = entityRads(entity);
			return rads == null ? 0D : rads.getRawRadiationLevel();
		}
		
		@ZenMethod
		public static double getRadiationLevel(IEntityLivingBase entity) {
			IEntityRads rads = entityRads(entity);
			return rads == null ? 0D : rads.getRadiationLevel();
		}
		
		private static IEntityRads entityRads(IEntityLivingBase entity) {
			return RadiationHelper.getEntityRadiation(CraftTweakerMC.getEntityLivingBase(entity));
		}
	}
}
