package nc.integration.crafttweaker;

import com.google.common.collect.Lists;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.entity.IEntityLivingBase;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.item.IngredientStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.api.oredict.IOreDictEntry;
import nc.capability.radiation.entity.IEntityRads;
import nc.radiation.RadSources;
import nc.radiation.RadiationHelper;
import nc.recipe.IngredientSorption;
import nc.recipe.NCRecipes;
import nc.recipe.ingredient.IItemIngredient;
import nc.recipe.ingredient.OreIngredient;
import net.minecraft.client.util.RecipeItemHelper;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenExpansion;
import stanhebben.zenscript.annotations.ZenMethod;

public class NCCraftTweaker {
	
	@ZenClass("mods.nuclearcraft.manufactory")
	@ZenRegister
	public static class ManufactoryHandler {
		
		@ZenMethod
		public static void addRecipe(IIngredient input, IIngredient output, @Optional(valueDouble = 1D) double timeMultiplier, @Optional(valueDouble = 1D) double powerMultiplier, @Optional double processRadiation) {
			CraftTweakerAPI.apply(new AddProcessorRecipe(NCRecipes.manufactory, Lists.newArrayList(input, output, timeMultiplier, powerMultiplier, processRadiation)));
		}
		
		@ZenMethod
		public static void removeRecipeWithInput(IIngredient input) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.manufactory, IngredientSorption.INPUT, Lists.newArrayList(input)));
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient output) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.manufactory, IngredientSorption.OUTPUT, Lists.newArrayList(output)));
		}
		
		@ZenMethod
		public static void removeAllRecipes() {
			CraftTweakerAPI.apply(new RemoveAllProcessorRecipes(NCRecipes.manufactory));
		}
	}
	
	@ZenClass("mods.nuclearcraft.separator")
	@ZenRegister
	public static class SeparatorHandler {
		
		@ZenMethod
		public static void addRecipe(IIngredient input, IIngredient output1, IIngredient output2, @Optional(valueDouble = 1D) double timeMultiplier, @Optional(valueDouble = 1D) double powerMultiplier, @Optional double processRadiation) {
			CraftTweakerAPI.apply(new AddProcessorRecipe(NCRecipes.separator, Lists.newArrayList(input, output1, output2, timeMultiplier, powerMultiplier, processRadiation)));
		}
		
		@ZenMethod
		public static void removeRecipeWithInput(IIngredient input) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.separator, IngredientSorption.INPUT, Lists.newArrayList(input)));
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient output1, IIngredient output2) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.separator, IngredientSorption.OUTPUT, Lists.newArrayList(output1, output2)));
		}
		
		@ZenMethod
		public static void removeAllRecipes() {
			CraftTweakerAPI.apply(new RemoveAllProcessorRecipes(NCRecipes.separator));
		}
	}
	
	@ZenClass("mods.nuclearcraft.decay_hastener")
	@ZenRegister
	public static class DecayHastenerHandler {
		
		@ZenMethod
		public static void addRecipe(IIngredient input, IIngredient output, @Optional(valueDouble = 1D) double timeMultiplier, @Optional(valueDouble = 1D) double powerMultiplier, @Optional double processRadiation) {
			CraftTweakerAPI.apply(new AddProcessorRecipe(NCRecipes.decay_hastener, Lists.newArrayList(input, output, timeMultiplier, powerMultiplier, processRadiation)));
		}
		
		@ZenMethod
		public static void removeRecipeWithInput(IIngredient input) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.decay_hastener, IngredientSorption.INPUT, Lists.newArrayList(input)));
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient output) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.decay_hastener, IngredientSorption.OUTPUT, Lists.newArrayList(output)));
		}
		
		@ZenMethod
		public static void removeAllRecipes() {
			CraftTweakerAPI.apply(new RemoveAllProcessorRecipes(NCRecipes.decay_hastener));
		}
	}
	
	@ZenClass("mods.nuclearcraft.fuel_reprocessor")
	@ZenRegister
	public static class FuelReprocessorHandler {
		
		@ZenMethod
		public static void addRecipe(IIngredient input, IIngredient output1, IIngredient output2, IIngredient output3, IIngredient output4, IIngredient output5, IIngredient output6, @Optional(valueDouble = 1D) double timeMultiplier, @Optional(valueDouble = 1D) double powerMultiplier, @Optional double processRadiation) {
			CraftTweakerAPI.apply(new AddProcessorRecipe(NCRecipes.fuel_reprocessor, Lists.newArrayList(input, output1, output2, output3, output4, output5, output6, timeMultiplier, powerMultiplier, processRadiation)));
		}
		
		@ZenMethod
		public static void removeRecipeWithInput(IIngredient input) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.fuel_reprocessor, IngredientSorption.INPUT, Lists.newArrayList(input)));
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient output1, IIngredient output2, IIngredient output3, IIngredient output4, IIngredient output5, IIngredient output6) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.fuel_reprocessor, IngredientSorption.OUTPUT, Lists.newArrayList(output1, output2, output3, output4, output5, output6)));
		}
		
		@ZenMethod
		public static void removeAllRecipes() {
			CraftTweakerAPI.apply(new RemoveAllProcessorRecipes(NCRecipes.fuel_reprocessor));
		}
	}
	
	@ZenClass("mods.nuclearcraft.alloy_furnace")
	@ZenRegister
	public static class AlloyFurnaceHandler {
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient input2, IIngredient output, @Optional(valueDouble = 1D) double timeMultiplier, @Optional(valueDouble = 1D) double powerMultiplier, @Optional double processRadiation) {
			CraftTweakerAPI.apply(new AddProcessorRecipe(NCRecipes.alloy_furnace, Lists.newArrayList(input1, input2, output, timeMultiplier, powerMultiplier, processRadiation)));
		}
		
		@ZenMethod
		public static void removeRecipeWithInput(IIngredient input1, IIngredient input2) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.alloy_furnace, IngredientSorption.INPUT, Lists.newArrayList(input1, input2)));
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient output) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.alloy_furnace, IngredientSorption.OUTPUT, Lists.newArrayList(output)));
		}
		
		@ZenMethod
		public static void removeAllRecipes() {
			CraftTweakerAPI.apply(new RemoveAllProcessorRecipes(NCRecipes.alloy_furnace));
		}
	}
	
	@ZenClass("mods.nuclearcraft.infuser")
	@ZenRegister
	public static class InfuserHandler {
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient input2, IIngredient output, @Optional(valueDouble = 1D) double timeMultiplier, @Optional(valueDouble = 1D) double powerMultiplier, @Optional double processRadiation) {
			CraftTweakerAPI.apply(new AddProcessorRecipe(NCRecipes.infuser, Lists.newArrayList(input1, input2, output, timeMultiplier, powerMultiplier, processRadiation)));
		}
		
		@ZenMethod
		public static void removeRecipeWithInput(IIngredient input1, IIngredient input2) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.infuser, IngredientSorption.INPUT, Lists.newArrayList(input1, input2)));
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient output) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.infuser, IngredientSorption.OUTPUT, Lists.newArrayList(output)));
		}
		
		@ZenMethod
		public static void removeAllRecipes() {
			CraftTweakerAPI.apply(new RemoveAllProcessorRecipes(NCRecipes.infuser));
		}
	}
	
	@ZenClass("mods.nuclearcraft.melter")
	@ZenRegister
	public static class MelterHandler {
		
		@ZenMethod
		public static void addRecipe(IIngredient input, IIngredient output, @Optional(valueDouble = 1D) double timeMultiplier, @Optional(valueDouble = 1D) double powerMultiplier, @Optional double processRadiation) {
			CraftTweakerAPI.apply(new AddProcessorRecipe(NCRecipes.melter, Lists.newArrayList(input, output, timeMultiplier, powerMultiplier, processRadiation)));
		}
		
		@ZenMethod
		public static void removeRecipeWithInput(IIngredient input) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.melter, IngredientSorption.INPUT, Lists.newArrayList(input)));
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient output) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.melter, IngredientSorption.OUTPUT, Lists.newArrayList(output)));
		}
		
		@ZenMethod
		public static void removeAllRecipes() {
			CraftTweakerAPI.apply(new RemoveAllProcessorRecipes(NCRecipes.melter));
		}
	}
	
	@ZenClass("mods.nuclearcraft.supercooler")
	@ZenRegister
	public static class SupercoolerHandler {
		
		@ZenMethod
		public static void addRecipe(IIngredient input, IIngredient output, @Optional(valueDouble = 1D) double timeMultiplier, @Optional(valueDouble = 1D) double powerMultiplier, @Optional double processRadiation) {
			CraftTweakerAPI.apply(new AddProcessorRecipe(NCRecipes.supercooler, Lists.newArrayList(input, output, timeMultiplier, powerMultiplier, processRadiation)));
		}
		
		@ZenMethod
		public static void removeRecipeWithInput(IIngredient input) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.supercooler, IngredientSorption.INPUT, Lists.newArrayList(input)));
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient output) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.supercooler, IngredientSorption.OUTPUT, Lists.newArrayList(output)));
		}
		
		@ZenMethod
		public static void removeAllRecipes() {
			CraftTweakerAPI.apply(new RemoveAllProcessorRecipes(NCRecipes.supercooler));
		}
	}
	
	@ZenClass("mods.nuclearcraft.electrolyzer")
	@ZenRegister
	public static class ElectrolyzerHandler {
		
		@ZenMethod
		public static void addRecipe(IIngredient input, IIngredient output1, IIngredient output2, IIngredient output3, IIngredient output4, @Optional(valueDouble = 1D) double timeMultiplier, @Optional(valueDouble = 1D) double powerMultiplier, @Optional double processRadiation) {
			CraftTweakerAPI.apply(new AddProcessorRecipe(NCRecipes.electrolyzer, Lists.newArrayList(input, output1, output2, output3, output4, timeMultiplier, powerMultiplier, processRadiation)));
		}
		
		@ZenMethod
		public static void removeRecipeWithInput(IIngredient input) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.electrolyzer, IngredientSorption.INPUT, Lists.newArrayList(input)));
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient output1, IIngredient output2, IIngredient output3, IIngredient output4) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.electrolyzer, IngredientSorption.OUTPUT, Lists.newArrayList(output1, output2, output3, output4)));
		}
		
		@ZenMethod
		public static void removeAllRecipes() {
			CraftTweakerAPI.apply(new RemoveAllProcessorRecipes(NCRecipes.electrolyzer));
		}
	}
	
	@ZenClass("mods.nuclearcraft.assembler")
	@ZenRegister
	public static class AssemblerHandler {
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient input2, IIngredient input3, IIngredient input4, IIngredient output, @Optional(valueDouble = 1D) double timeMultiplier, @Optional(valueDouble = 1D) double powerMultiplier, @Optional double processRadiation) {
			CraftTweakerAPI.apply(new AddProcessorRecipe(NCRecipes.assembler, Lists.newArrayList(input1, input2, input3, input4, output, timeMultiplier, powerMultiplier, processRadiation)));
		}
		
		@ZenMethod
		public static void removeRecipeWithInput(IIngredient input1, IIngredient input2, IIngredient input3, IIngredient input4) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.assembler, IngredientSorption.INPUT, Lists.newArrayList(input1, input2, input3, input4)));
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient output) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.assembler, IngredientSorption.OUTPUT, Lists.newArrayList(output)));
		}
		
		@ZenMethod
		public static void removeAllRecipes() {
			CraftTweakerAPI.apply(new RemoveAllProcessorRecipes(NCRecipes.assembler));
		}
	}
	
	@ZenClass("mods.nuclearcraft.ingot_former")
	@ZenRegister
	public static class IngotFormerHandler {
		
		@ZenMethod
		public static void addRecipe(IIngredient input, IIngredient output, @Optional(valueDouble = 1D) double timeMultiplier, @Optional(valueDouble = 1D) double powerMultiplier, @Optional double processRadiation) {
			CraftTweakerAPI.apply(new AddProcessorRecipe(NCRecipes.ingot_former, Lists.newArrayList(input, output, timeMultiplier, powerMultiplier, processRadiation)));
		}
		
		@ZenMethod
		public static void removeRecipeWithInput(IIngredient input) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.ingot_former, IngredientSorption.INPUT, Lists.newArrayList(input)));
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient output) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.ingot_former, IngredientSorption.OUTPUT, Lists.newArrayList(output)));
		}
		
		@ZenMethod
		public static void removeAllRecipes() {
			CraftTweakerAPI.apply(new RemoveAllProcessorRecipes(NCRecipes.ingot_former));
		}
	}
	
	@ZenClass("mods.nuclearcraft.pressurizer")
	@ZenRegister
	public static class PressurizerHandler {
		
		@ZenMethod
		public static void addRecipe(IIngredient input, IIngredient output, @Optional(valueDouble = 1D) double timeMultiplier, @Optional(valueDouble = 1D) double powerMultiplier, @Optional double processRadiation) {
			CraftTweakerAPI.apply(new AddProcessorRecipe(NCRecipes.pressurizer, Lists.newArrayList(input, output, timeMultiplier, powerMultiplier, processRadiation)));
		}
		
		@ZenMethod
		public static void removeRecipeWithInput(IIngredient input) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.pressurizer, IngredientSorption.INPUT, Lists.newArrayList(input)));
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient output) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.pressurizer, IngredientSorption.OUTPUT, Lists.newArrayList(output)));
		}
		
		@ZenMethod
		public static void removeAllRecipes() {
			CraftTweakerAPI.apply(new RemoveAllProcessorRecipes(NCRecipes.pressurizer));
		}
	}
	
	@ZenClass("mods.nuclearcraft.chemical_reactor")
	@ZenRegister
	public static class ChemicalReactorHandler {
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient input2, IIngredient output1, IIngredient output2, @Optional(valueDouble = 1D) double timeMultiplier, @Optional(valueDouble = 1D) double powerMultiplier, @Optional double processRadiation) {
			CraftTweakerAPI.apply(new AddProcessorRecipe(NCRecipes.chemical_reactor, Lists.newArrayList(input1, input2, output1, output2, timeMultiplier, powerMultiplier, processRadiation)));
		}
		
		@ZenMethod
		public static void removeRecipeWithInput(IIngredient input1, IIngredient input2) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.chemical_reactor, IngredientSorption.INPUT, Lists.newArrayList(input1, input2)));
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient output1, IIngredient output2) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.chemical_reactor, IngredientSorption.OUTPUT, Lists.newArrayList(output1, output2)));
		}
		
		@ZenMethod
		public static void removeAllRecipes() {
			CraftTweakerAPI.apply(new RemoveAllProcessorRecipes(NCRecipes.chemical_reactor));
		}
	}
	
	@ZenClass("mods.nuclearcraft.salt_mixer")
	@ZenRegister
	public static class SaltMixerHandler {
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient input2, IIngredient output, @Optional(valueDouble = 1D) double timeMultiplier, @Optional(valueDouble = 1D) double powerMultiplier, @Optional double processRadiation) {
			CraftTweakerAPI.apply(new AddProcessorRecipe(NCRecipes.salt_mixer, Lists.newArrayList(input1, input2, output, timeMultiplier, powerMultiplier, processRadiation)));
		}
		
		@ZenMethod
		public static void removeRecipeWithInput(IIngredient input1, IIngredient input2) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.salt_mixer, IngredientSorption.INPUT, Lists.newArrayList(input1, input2)));
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient output) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.salt_mixer, IngredientSorption.OUTPUT, Lists.newArrayList(output)));
		}
		
		@ZenMethod
		public static void removeAllRecipes() {
			CraftTweakerAPI.apply(new RemoveAllProcessorRecipes(NCRecipes.salt_mixer));
		}
	}
	
	@ZenClass("mods.nuclearcraft.crystallizer")
	@ZenRegister
	public static class CrystallizerHandler {
		
		@ZenMethod
		public static void addRecipe(IIngredient input, IIngredient output, @Optional(valueDouble = 1D) double timeMultiplier, @Optional(valueDouble = 1D) double powerMultiplier, @Optional double processRadiation) {
			CraftTweakerAPI.apply(new AddProcessorRecipe(NCRecipes.crystallizer, Lists.newArrayList(input, output, timeMultiplier, powerMultiplier, processRadiation)));
		}
		
		@ZenMethod
		public static void removeRecipeWithInput(IIngredient input) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.crystallizer, IngredientSorption.INPUT, Lists.newArrayList(input)));
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient output) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.crystallizer, IngredientSorption.OUTPUT, Lists.newArrayList(output)));
		}
		
		@ZenMethod
		public static void removeAllRecipes() {
			CraftTweakerAPI.apply(new RemoveAllProcessorRecipes(NCRecipes.crystallizer));
		}
	}
	
	@ZenClass("mods.nuclearcraft.enricher")
	@ZenRegister
	public static class EnricherHandler {
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient input2, IIngredient output, @Optional(valueDouble = 1D) double timeMultiplier, @Optional(valueDouble = 1D) double powerMultiplier, @Optional double processRadiation) {
			CraftTweakerAPI.apply(new AddProcessorRecipe(NCRecipes.enricher, Lists.newArrayList(input1, input2, output, timeMultiplier, powerMultiplier, processRadiation)));
		}
		
		@ZenMethod
		public static void removeRecipeWithInput(IIngredient input1, IIngredient input2) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.enricher, IngredientSorption.INPUT, Lists.newArrayList(input1, input2)));
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient output) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.enricher, IngredientSorption.OUTPUT, Lists.newArrayList(output)));
		}
		
		@ZenMethod
		public static void removeAllRecipes() {
			CraftTweakerAPI.apply(new RemoveAllProcessorRecipes(NCRecipes.enricher));
		}
	}
	
	@ZenClass("mods.nuclearcraft.extractor")
	@ZenRegister
	public static class ExtractorHandler {
		
		@ZenMethod
		public static void addRecipe(IIngredient input, IIngredient output1, IIngredient output2, @Optional(valueDouble = 1D) double timeMultiplier, @Optional(valueDouble = 1D) double powerMultiplier, @Optional double processRadiation) {
			CraftTweakerAPI.apply(new AddProcessorRecipe(NCRecipes.extractor, Lists.newArrayList(input, output1, output2, timeMultiplier, powerMultiplier, processRadiation)));
		}
		
		@ZenMethod
		public static void removeRecipeWithInput(IIngredient input) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.extractor, IngredientSorption.INPUT, Lists.newArrayList(input)));
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient output1, IIngredient output2) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.extractor, IngredientSorption.OUTPUT, Lists.newArrayList(output1, output2)));
		}
		
		@ZenMethod
		public static void removeAllRecipes() {
			CraftTweakerAPI.apply(new RemoveAllProcessorRecipes(NCRecipes.extractor));
		}
	}
	
	@ZenClass("mods.nuclearcraft.centrifuge")
	@ZenRegister
	public static class CentrifugeHandler {
		
		@ZenMethod
		public static void addRecipe(IIngredient input, IIngredient output1, IIngredient output2, IIngredient output3, IIngredient output4, @Optional(valueDouble = 1D) double timeMultiplier, @Optional(valueDouble = 1D) double powerMultiplier, @Optional double processRadiation) {
			CraftTweakerAPI.apply(new AddProcessorRecipe(NCRecipes.centrifuge, Lists.newArrayList(input, output1, output2, output3, output4, timeMultiplier, powerMultiplier, processRadiation)));
		}
		
		@ZenMethod
		public static void removeRecipeWithInput(IIngredient input) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.centrifuge, IngredientSorption.INPUT, Lists.newArrayList(input)));
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient output1, IIngredient output2, IIngredient output3, IIngredient output4) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.centrifuge, IngredientSorption.OUTPUT, Lists.newArrayList(output1, output2, output3, output4)));
		}
		
		@ZenMethod
		public static void removeAllRecipes() {
			CraftTweakerAPI.apply(new RemoveAllProcessorRecipes(NCRecipes.centrifuge));
		}
	}
	
	@ZenClass("mods.nuclearcraft.rock_crusher")
	@ZenRegister
	public static class RockCrusherHandler {
		
		@ZenMethod
		public static void addRecipe(IIngredient input, IIngredient output1, IIngredient output2, IIngredient output3, @Optional(valueDouble = 1D) double timeMultiplier, @Optional(valueDouble = 1D) double powerMultiplier, @Optional double processRadiation) {
			CraftTweakerAPI.apply(new AddProcessorRecipe(NCRecipes.rock_crusher, Lists.newArrayList(input, output1, output2, output3, timeMultiplier, powerMultiplier, processRadiation)));
		}
		
		@ZenMethod
		public static void removeRecipeWithInput(IIngredient input) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.rock_crusher, IngredientSorption.INPUT, Lists.newArrayList(input)));
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient output1, IIngredient output2, IIngredient output3) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.rock_crusher, IngredientSorption.OUTPUT, Lists.newArrayList(output1, output2, output3)));
		}
		
		@ZenMethod
		public static void removeAllRecipes() {
			CraftTweakerAPI.apply(new RemoveAllProcessorRecipes(NCRecipes.rock_crusher));
		}
	}
	
	@ZenClass("mods.nuclearcraft.decay_generator")
	@ZenRegister
	public static class DecayGeneratorHandler {
		
		@ZenMethod
		public static void addRecipe(IIngredient input, IIngredient output, double meanLifetime, double power, double radiation) {
			CraftTweakerAPI.apply(new AddProcessorRecipe(NCRecipes.decay_generator, Lists.newArrayList(input, output, meanLifetime, power, radiation)));
		}
		
		@ZenMethod
		public static void removeRecipeWithInput(IIngredient input) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.decay_generator, IngredientSorption.INPUT, Lists.newArrayList(input)));
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient output) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.decay_generator, IngredientSorption.OUTPUT, Lists.newArrayList(output)));
		}
		
		@ZenMethod
		public static void removeAllRecipes() {
			CraftTweakerAPI.apply(new RemoveAllProcessorRecipes(NCRecipes.decay_generator));
		}
	}
	
	@ZenClass("mods.nuclearcraft.fission_moderator")
	@ZenRegister
	public static class FissionModeratorHandler {
		
		@ZenMethod
		public static void add(IIngredient input, int fluxFactor, double efficiency) {
			CraftTweakerAPI.apply(new AddProcessorRecipe(NCRecipes.fission_moderator, Lists.newArrayList(input, fluxFactor, efficiency)));
		}
		
		@ZenMethod
		public static void remove(IIngredient input) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.fission_moderator, IngredientSorption.INPUT, Lists.newArrayList(input)));
		}
		
		@ZenMethod
		public static void removeAll() {
			CraftTweakerAPI.apply(new RemoveAllProcessorRecipes(NCRecipes.fission_moderator));
		}
	}
	
	@ZenClass("mods.nuclearcraft.fission_reflector")
	@ZenRegister
	public static class FissionReflectorHandler {
		
		@ZenMethod
		public static void add(IIngredient input, double efficiency, double reflectivity) {
			CraftTweakerAPI.apply(new AddProcessorRecipe(NCRecipes.fission_reflector, Lists.newArrayList(input, efficiency, reflectivity)));
		}
		
		@ZenMethod
		public static void remove(IIngredient input) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.fission_reflector, IngredientSorption.INPUT, Lists.newArrayList(input)));
		}
		
		@ZenMethod
		public static void removeAll() {
			CraftTweakerAPI.apply(new RemoveAllProcessorRecipes(NCRecipes.fission_reflector));
		}
	}
	
	@ZenClass("mods.nuclearcraft.fission_irradiator")
	@ZenRegister
	public static class FissionIrradiatorHandler {
		
		@ZenMethod
		public static void addRecipe(IIngredient input, IIngredient output, int fluxRequired, double heatPerFlux, double radiation) {
			CraftTweakerAPI.apply(new AddProcessorRecipe(NCRecipes.fission_irradiator, Lists.newArrayList(input, output, fluxRequired, heatPerFlux, radiation)));
		}
		
		@ZenMethod
		public static void removeRecipeWithInput(IIngredient input) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.fission_irradiator, IngredientSorption.INPUT, Lists.newArrayList(input)));
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient output) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.fission_irradiator, IngredientSorption.OUTPUT, Lists.newArrayList(output)));
		}
		
		@ZenMethod
		public static void removeAllRecipes() {
			CraftTweakerAPI.apply(new RemoveAllProcessorRecipes(NCRecipes.fission_irradiator));
		}
	}
	
	@ZenClass("mods.nuclearcraft.solid_fission")
	@ZenRegister
	public static class SolidFissionHandler {
		
		@ZenMethod
		public static void addRecipe(IIngredient input, IIngredient output, int time, int heat, double efficiency, int criticality, boolean selfPriming, double radiation) {
			CraftTweakerAPI.apply(new AddProcessorRecipe(NCRecipes.solid_fission, Lists.newArrayList(input, output, time, heat, efficiency, criticality, selfPriming, radiation)));
		}
		
		@ZenMethod
		public static void removeRecipeWithInput(IIngredient input) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.solid_fission, IngredientSorption.INPUT, Lists.newArrayList(input)));
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient output) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.solid_fission, IngredientSorption.OUTPUT, Lists.newArrayList(output)));
		}
		
		@ZenMethod
		public static void removeAllRecipes() {
			CraftTweakerAPI.apply(new RemoveAllProcessorRecipes(NCRecipes.solid_fission));
		}
	}
	
	@ZenClass("mods.nuclearcraft.fission_heating")
	@ZenRegister
	public static class FissionHeatingHandler {
		
		@ZenMethod
		public static void addRecipe(IIngredient input, IIngredient output, int heatPerInputMB) {
			CraftTweakerAPI.apply(new AddProcessorRecipe(NCRecipes.fission_heating, Lists.newArrayList(input, output, heatPerInputMB)));
		}
		
		@ZenMethod
		public static void removeRecipeWithInput(IIngredient input) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.fission_heating, IngredientSorption.INPUT, Lists.newArrayList(input)));
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient output) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.fission_heating, IngredientSorption.OUTPUT, Lists.newArrayList(output)));
		}
		
		@ZenMethod
		public static void removeAllRecipes() {
			CraftTweakerAPI.apply(new RemoveAllProcessorRecipes(NCRecipes.fission_heating));
		}
	}
	
	//TODO
	@ZenClass("mods.nuclearcraft.salt_fission")
	@ZenRegister
	public static class SaltFissionHandler {
		
		@ZenMethod
		public static void addRecipe(IIngredient input, IIngredient output, int time, int heat, double efficiency, int criticality, boolean selfPriming, double radiation) {
			CraftTweakerAPI.apply(new AddProcessorRecipe(NCRecipes.salt_fission, Lists.newArrayList(input, output, time, heat, efficiency, criticality, selfPriming, radiation)));
		}
		
		@ZenMethod
		public static void removeRecipeWithInput(IIngredient input) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.salt_fission, IngredientSorption.INPUT, Lists.newArrayList(input)));
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient output) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.salt_fission, IngredientSorption.OUTPUT, Lists.newArrayList(output)));
		}
		
		@ZenMethod
		public static void removeAllRecipes() {
			CraftTweakerAPI.apply(new RemoveAllProcessorRecipes(NCRecipes.salt_fission));
		}
	}
	
	//TODO
	@ZenClass("mods.nuclearcraft.fusion")
	@ZenRegister
	public static class FusionHandler {
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient input2, IIngredient output1, IIngredient output2, IIngredient output3, IIngredient output4, int time, int power, int optimalTemp) {
			CraftTweakerAPI.apply(new AddProcessorRecipe(NCRecipes.fusion, Lists.newArrayList(input1, input2, output1, output2, output3, output4, time, power, optimalTemp)));
		}
		
		@ZenMethod
		public static void removeRecipeWithInput(IIngredient input1, IIngredient input2) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.fusion, IngredientSorption.INPUT, Lists.newArrayList(input1, input2)));
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient output1, IIngredient output2, IIngredient output3, IIngredient output4) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.fusion, IngredientSorption.OUTPUT, Lists.newArrayList(output1, output2, output3, output4)));
		}
		
		@ZenMethod
		public static void removeAllRecipes() {
			CraftTweakerAPI.apply(new RemoveAllProcessorRecipes(NCRecipes.fusion));
		}
	}
	
	//TODO
	@ZenClass("mods.nuclearcraft.heat_exchanger")
	@ZenRegister
	public static class HeatExchangerHandler {
		
		@ZenMethod
		public static void addRecipe(Object[] objects) {
			CraftTweakerAPI.apply(new AddProcessorRecipe(NCRecipes.heat_exchanger, Lists.newArrayList(objects)));
		}
		
		@ZenMethod
		public static void removeRecipeWithInput(IIngredient[] inputs) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.heat_exchanger, IngredientSorption.INPUT, Lists.newArrayList(inputs)));
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient[] outputs) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.heat_exchanger, IngredientSorption.OUTPUT, Lists.newArrayList(outputs)));
		}
		
		@ZenMethod
		public static void removeAllRecipes() {
			CraftTweakerAPI.apply(new RemoveAllProcessorRecipes(NCRecipes.heat_exchanger));
		}
	}
	
	//TODO
	@ZenClass("mods.nuclearcraft.condenser")
	@ZenRegister
	public static class CondenserHandler {
		
		@ZenMethod
		public static void addRecipe(Object[] objects) {
			CraftTweakerAPI.apply(new AddProcessorRecipe(NCRecipes.condenser, Lists.newArrayList(objects)));
		}
		
		@ZenMethod
		public static void removeRecipeWithInput(IIngredient[] inputs) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.condenser, IngredientSorption.INPUT, Lists.newArrayList(inputs)));
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient[] outputs) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.condenser, IngredientSorption.OUTPUT, Lists.newArrayList(outputs)));
		}
		
		@ZenMethod
		public static void removeAllRecipes() {
			CraftTweakerAPI.apply(new RemoveAllProcessorRecipes(NCRecipes.condenser));
		}
	}
	
	@ZenClass("mods.nuclearcraft.turbine")
	@ZenRegister
	public static class TurbineHandler {
		
		@ZenMethod
		public static void addRecipe(IIngredient input, IIngredient output, double powerPerMB, double expansionLevel) {
			CraftTweakerAPI.apply(new AddProcessorRecipe(NCRecipes.turbine, Lists.newArrayList(input, output, powerPerMB, expansionLevel)));
		}
		
		@ZenMethod
		public static void removeRecipeWithInput(IIngredient input) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.turbine, IngredientSorption.INPUT, Lists.newArrayList(input)));
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient output) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.turbine, IngredientSorption.OUTPUT, Lists.newArrayList(output)));
		}
		
		@ZenMethod
		public static void removeAllRecipes() {
			CraftTweakerAPI.apply(new RemoveAllProcessorRecipes(NCRecipes.turbine));
		}
	}
	
	@ZenClass("mods.nuclearcraft.radiation_scrubber")
	@ZenRegister
	public static class RadiationScrubberHandler {
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient input2, IIngredient output1, IIngredient output2, int processTime, int processPower, double processEfficiency) {
			CraftTweakerAPI.apply(new AddProcessorRecipe(NCRecipes.radiation_scrubber, Lists.newArrayList(input1, input2, output1, output2, processTime, processPower, processEfficiency)));
		}
		
		@ZenMethod
		public static void removeRecipeWithInput(IIngredient input1, IIngredient input2) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.radiation_scrubber, IngredientSorption.INPUT, Lists.newArrayList(input1, input2)));
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient output1, IIngredient output2) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.radiation_scrubber, IngredientSorption.OUTPUT, Lists.newArrayList(output1, output2)));
		}
		
		@ZenMethod
		public static void removeAllRecipes() {
			CraftTweakerAPI.apply(new RemoveAllProcessorRecipes(NCRecipes.radiation_scrubber));
		}
	}
	
	@ZenClass("mods.nuclearcraft.radiation_block_mutation")
	@ZenRegister
	public static class RadiationBlockMutation {
		
		@ZenMethod
		public static void addRecipe(IIngredient input, IIngredient output, double radiationThreshold) {
			CraftTweakerAPI.apply(new AddProcessorRecipe(NCRecipes.radiation_block_mutation, Lists.newArrayList(input, output, radiationThreshold)));
		}
		
		@ZenMethod
		public static void removeRecipeWithInput(IIngredient input) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.radiation_block_mutation, IngredientSorption.INPUT, Lists.newArrayList(input)));
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient output) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.radiation_block_mutation, IngredientSorption.OUTPUT, Lists.newArrayList(output)));
		}
		
		@ZenMethod
		public static void removeAllRecipes() {
			CraftTweakerAPI.apply(new RemoveAllProcessorRecipes(NCRecipes.radiation_block_mutation));
		}
	}
	
	@ZenClass("mods.nuclearcraft.radiation_block_purification")
	@ZenRegister
	public static class RadiationBlockPurification {
		
		@ZenMethod
		public static void addRecipe(IIngredient input, IIngredient output, double radiationThreshold) {
			CraftTweakerAPI.apply(new AddProcessorRecipe(NCRecipes.radiation_block_purification, Lists.newArrayList(input, output, radiationThreshold)));
		}
		
		@ZenMethod
		public static void removeRecipeWithInput(IIngredient input) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.radiation_block_purification, IngredientSorption.INPUT, Lists.newArrayList(input)));
		}
		
		@ZenMethod
		public static void removeRecipeWithOutput(IIngredient output) {
			CraftTweakerAPI.apply(new RemoveProcessorRecipe(NCRecipes.radiation_block_purification, IngredientSorption.OUTPUT, Lists.newArrayList(output)));
		}
		
		@ZenMethod
		public static void removeAllRecipes() {
			CraftTweakerAPI.apply(new RemoveAllProcessorRecipes(NCRecipes.radiation_block_purification));
		}
	}
	
	@ZenClass("mods.nuclearcraft.radiation")
	@ZenRegister
	public static class RadiationHandler {
		
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
				return RadSources.STACK_MAP.get(RecipeItemHelper.pack(stack))*stack.getCount();
			}
			else if (ingredient instanceof IOreDictEntry) {
				String oreName = ((IOreDictEntry) ingredient).getName();
				int amount = ((IOreDictEntry) ingredient).getAmount();
				return RadSources.ORE_MAP.getDouble(oreName)*amount;
			}
			else if (ingredient instanceof IngredientStack) {
				IItemIngredient ing = CTHelper.buildOreIngredientArray(ingredient, true);
				if (ing instanceof OreIngredient) {
					String oreName = ((OreIngredient) ing).oreName;
					int amount = ((OreIngredient) ing).stackSize;
					return RadSources.ORE_MAP.getDouble(oreName)*amount;
				}
				else {
					ItemStack stack = ing.getStack();
					if (stack == null || stack.isEmpty()) {
						return 0D;
					}
					return RadSources.STACK_MAP.get(RecipeItemHelper.pack(stack))*stack.getCount();
				}
			}
			else {
				return 0D;
			}
		}
		
		@ZenMethod
		public static void addBlockMutation(IIngredient input, IIngredient output, double threshold) {
			CraftTweakerAPI.apply(new AddProcessorRecipe(NCRecipes.radiation_block_mutation, Lists.newArrayList(input, output, threshold)));
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
				if (!slow) rads.setRecentRadawayAddition(Math.abs(amount));
			}
		}
		
		@ZenMethod
		public static void setRadawayBuffer(IEntityLivingBase entity, double amount, @Optional boolean slow) {
			IEntityRads rads = entityRads(entity);
			if (rads != null) {
				rads.setRadawayBuffer(slow, amount);
				if (!slow) rads.setRecentRadawayAddition(Math.abs(amount));
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