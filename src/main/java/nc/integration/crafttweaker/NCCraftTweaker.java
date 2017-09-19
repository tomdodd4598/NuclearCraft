package nc.integration.crafttweaker;

import com.google.common.collect.Lists;

import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import minetweaker.api.liquid.ILiquidStack;
import minetweaker.api.minecraft.MineTweakerMC;
import nc.recipe.SorptionType;
import nc.recipe.generator.FissionRecipes;
import nc.recipe.generator.FusionRecipes;
import nc.recipe.processor.AlloyFurnaceRecipes;
import nc.recipe.processor.ChemicalReactorRecipes;
import nc.recipe.processor.CrystallizerRecipes;
import nc.recipe.processor.DecayHastenerRecipes;
import nc.recipe.processor.ElectrolyserRecipes;
import nc.recipe.processor.FuelReprocessorRecipes;
import nc.recipe.processor.InfuserRecipes;
import nc.recipe.processor.IngotFormerRecipes;
import nc.recipe.processor.IrradiatorRecipes;
import nc.recipe.processor.IsotopeSeparatorRecipes;
import nc.recipe.processor.ManufactoryRecipes;
import nc.recipe.processor.MelterRecipes;
import nc.recipe.processor.PressurizerRecipes;
import nc.recipe.processor.SaltMixerRecipes;
import nc.recipe.processor.SupercoolerRecipes;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

public class NCCraftTweaker {
	
	public static void init() {
		MineTweakerAPI.registerClass(ManufactoryHandler.class);
		MineTweakerAPI.registerClass(IsotopeSeparatorHandler.class);
		MineTweakerAPI.registerClass(DecayHastenerHandler.class);
		MineTweakerAPI.registerClass(FuelReprocessorHandler.class);
		MineTweakerAPI.registerClass(AlloyFurnaceHandler.class);
		MineTweakerAPI.registerClass(InfuserHandler.class);
		MineTweakerAPI.registerClass(MelterHandler.class);
		MineTweakerAPI.registerClass(SupercoolerHandler.class);
		MineTweakerAPI.registerClass(ElectrolyserHandler.class);
		MineTweakerAPI.registerClass(IrradiatorHandler.class);
		MineTweakerAPI.registerClass(IngotFormerHandler.class);
		MineTweakerAPI.registerClass(PressurizerHandler.class);
		MineTweakerAPI.registerClass(ChemicalReactorHandler.class);
		MineTweakerAPI.registerClass(SaltMixerHandler.class);
		MineTweakerAPI.registerClass(CrystallizerHandler.class);
		MineTweakerAPI.registerClass(FissionHandler.class);
		MineTweakerAPI.registerClass(FusionHandler.class);
	}
	
	@ZenClass("mods.nuclearcraft.manufactory")
	public static class ManufactoryHandler {
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IItemStack output1) {
			MineTweakerAPI.apply(new AddRecipe(ManufactoryRecipes.instance(), Lists.newArrayList(input1), Lists.newArrayList(MineTweakerMC.getItemStack(output1))));
		}

		@ZenMethod
		public static void removeRecipe(IIngredient output1) {
			MineTweakerAPI.apply(new RemoveRecipe(ManufactoryRecipes.instance(), SorptionType.OUTPUT, Lists.newArrayList(output1)));
		}
	}
	
	@ZenClass("mods.nuclearcraft.isotope_separator")
	public static class IsotopeSeparatorHandler {
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IItemStack output1, IItemStack output2) {
			MineTweakerAPI.apply(new AddRecipe(IsotopeSeparatorRecipes.instance(), Lists.newArrayList(input1), Lists.newArrayList(MineTweakerMC.getItemStack(output1), MineTweakerMC.getItemStack(output2))));
		}

		@ZenMethod
		public static void removeRecipe(IIngredient output1, IIngredient output2) {
			MineTweakerAPI.apply(new RemoveRecipe(IsotopeSeparatorRecipes.instance(), SorptionType.OUTPUT, Lists.newArrayList(output1, output2)));
		}
	}
	
	@ZenClass("mods.nuclearcraft.decay_hastener")
	public static class DecayHastenerHandler {
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IItemStack output1) {
			MineTweakerAPI.apply(new AddRecipe(DecayHastenerRecipes.instance(), Lists.newArrayList(input1), Lists.newArrayList(MineTweakerMC.getItemStack(output1))));
		}

		@ZenMethod
		public static void removeRecipe(IIngredient output1) {
			MineTweakerAPI.apply(new RemoveRecipe(DecayHastenerRecipes.instance(), SorptionType.OUTPUT, Lists.newArrayList(output1)));
		}
	}
	
	@ZenClass("mods.nuclearcraft.fuel_reprocessor")
	public static class FuelReprocessorHandler {
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IItemStack output1, IItemStack output2, IItemStack output3, IItemStack output4) {
			MineTweakerAPI.apply(new AddRecipe(FuelReprocessorRecipes.instance(), Lists.newArrayList(input1), Lists.newArrayList(MineTweakerMC.getItemStack(output1), MineTweakerMC.getItemStack(output2), MineTweakerMC.getItemStack(output3), MineTweakerMC.getItemStack(output4))));
		}

		@ZenMethod
		public static void removeRecipe(IIngredient output1, IIngredient output2, IIngredient output3, IIngredient output4) {
			MineTweakerAPI.apply(new RemoveRecipe(FuelReprocessorRecipes.instance(), SorptionType.OUTPUT, Lists.newArrayList(output1, output2, output3, output4)));
		}
	}
	
	@ZenClass("mods.nuclearcraft.alloy_furnace")
	public static class AlloyFurnaceHandler {
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient input2, IItemStack output1) {
			MineTweakerAPI.apply(new AddRecipe(AlloyFurnaceRecipes.instance(), Lists.newArrayList(input1, input2), Lists.newArrayList(MineTweakerMC.getItemStack(output1))));
		}

		@ZenMethod
		public static void removeRecipe(IIngredient output1) {
			MineTweakerAPI.apply(new RemoveRecipe(AlloyFurnaceRecipes.instance(), SorptionType.OUTPUT, Lists.newArrayList(output1)));
		}
	}
	
	@ZenClass("mods.nuclearcraft.infuser")
	public static class InfuserHandler {
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient input2, IItemStack output1) {
			MineTweakerAPI.apply(new AddRecipe(InfuserRecipes.instance(), Lists.newArrayList(input1, input2), Lists.newArrayList(MineTweakerMC.getItemStack(output1))));
		}

		@ZenMethod
		public static void removeRecipe(IIngredient output1) {
			MineTweakerAPI.apply(new RemoveRecipe(InfuserRecipes.instance(), SorptionType.OUTPUT, Lists.newArrayList(output1)));
		}
	}
	
	@ZenClass("mods.nuclearcraft.melter")
	public static class MelterHandler {
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, ILiquidStack output1) {
			MineTweakerAPI.apply(new AddRecipe(MelterRecipes.instance(), Lists.newArrayList(input1), Lists.newArrayList(MineTweakerMC.getLiquidStack(output1))));
		}

		@ZenMethod
		public static void removeRecipe(IIngredient output1) {
			MineTweakerAPI.apply(new RemoveRecipe(MelterRecipes.instance(), SorptionType.OUTPUT, Lists.newArrayList(output1)));
		}
	}
	
	@ZenClass("mods.nuclearcraft.supercooler")
	public static class SupercoolerHandler {
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, ILiquidStack output1) {
			MineTweakerAPI.apply(new AddRecipe(SupercoolerRecipes.instance(), Lists.newArrayList(input1), Lists.newArrayList(MineTweakerMC.getLiquidStack(output1))));
		}

		@ZenMethod
		public static void removeRecipe(IIngredient output1) {
			MineTweakerAPI.apply(new RemoveRecipe(SupercoolerRecipes.instance(), SorptionType.OUTPUT, Lists.newArrayList(output1)));
		}
	}
	
	@ZenClass("mods.nuclearcraft.electrolyser")
	public static class ElectrolyserHandler {
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, ILiquidStack output1, ILiquidStack output2, ILiquidStack output3, ILiquidStack output4) {
			MineTweakerAPI.apply(new AddRecipe(ElectrolyserRecipes.instance(), Lists.newArrayList(input1), Lists.newArrayList(MineTweakerMC.getLiquidStack(output1), MineTweakerMC.getLiquidStack(output2), MineTweakerMC.getLiquidStack(output3), MineTweakerMC.getLiquidStack(output4))));
		}

		@ZenMethod
		public static void removeRecipe(IIngredient output1, IIngredient output2, IIngredient output3, IIngredient output4) {
			MineTweakerAPI.apply(new RemoveRecipe(ElectrolyserRecipes.instance(), SorptionType.OUTPUT, Lists.newArrayList(output1, output2, output3, output4)));
		}
	}
	
	@ZenClass("mods.nuclearcraft.irradiator")
	public static class IrradiatorHandler {
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient input2, ILiquidStack output1, ILiquidStack output2) {
			MineTweakerAPI.apply(new AddRecipe(IrradiatorRecipes.instance(), Lists.newArrayList(input1, input2), Lists.newArrayList(MineTweakerMC.getLiquidStack(output1), MineTweakerMC.getLiquidStack(output2))));
		}

		@ZenMethod
		public static void removeRecipe(IIngredient output1, IIngredient output2) {
			MineTweakerAPI.apply(new RemoveRecipe(IrradiatorRecipes.instance(), SorptionType.OUTPUT, Lists.newArrayList(output1, output2)));
		}
	}
	
	@ZenClass("mods.nuclearcraft.ingot_former")
	public static class IngotFormerHandler {
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IItemStack output1) {
			MineTweakerAPI.apply(new AddRecipe(IngotFormerRecipes.instance(), Lists.newArrayList(input1), Lists.newArrayList(MineTweakerMC.getItemStack(output1))));
		}

		@ZenMethod
		public static void removeRecipe(IIngredient output1) {
			MineTweakerAPI.apply(new RemoveRecipe(IngotFormerRecipes.instance(), SorptionType.OUTPUT, Lists.newArrayList(output1)));
		}
	}
	
	@ZenClass("mods.nuclearcraft.pressurizer")
	public static class PressurizerHandler {
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IItemStack output1) {
			MineTweakerAPI.apply(new AddRecipe(PressurizerRecipes.instance(), Lists.newArrayList(input1), Lists.newArrayList(MineTweakerMC.getItemStack(output1))));
		}

		@ZenMethod
		public static void removeRecipe(IIngredient output1) {
			MineTweakerAPI.apply(new RemoveRecipe(PressurizerRecipes.instance(), SorptionType.OUTPUT, Lists.newArrayList(output1)));
		}
	}
	
	@ZenClass("mods.nuclearcraft.chemical_reactor")
	public static class ChemicalReactorHandler {
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient input2, ILiquidStack output1, ILiquidStack output2) {
			MineTweakerAPI.apply(new AddRecipe(ChemicalReactorRecipes.instance(), Lists.newArrayList(input1, input2), Lists.newArrayList(MineTweakerMC.getLiquidStack(output1), MineTweakerMC.getLiquidStack(output2))));
		}

		@ZenMethod
		public static void removeRecipe(IIngredient output1, IIngredient output2) {
			MineTweakerAPI.apply(new RemoveRecipe(ChemicalReactorRecipes.instance(), SorptionType.OUTPUT, Lists.newArrayList(output1, output2)));
		}
	}
	
	@ZenClass("mods.nuclearcraft.salt_mixer")
	public static class SaltMixerHandler {
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient input2, ILiquidStack output1) {
			MineTweakerAPI.apply(new AddRecipe(SaltMixerRecipes.instance(), Lists.newArrayList(input1, input2), Lists.newArrayList(MineTweakerMC.getLiquidStack(output1))));
		}

		@ZenMethod
		public static void removeRecipe(IIngredient output1) {
			MineTweakerAPI.apply(new RemoveRecipe(SaltMixerRecipes.instance(), SorptionType.OUTPUT, Lists.newArrayList(output1)));
		}
	}
	
	@ZenClass("mods.nuclearcraft.crystallizer")
	public static class CrystallizerHandler {
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IItemStack output1) {
			MineTweakerAPI.apply(new AddRecipe(CrystallizerRecipes.instance(), Lists.newArrayList(input1), Lists.newArrayList(MineTweakerMC.getItemStack(output1))));
		}

		@ZenMethod
		public static void removeRecipe(IIngredient output1) {
			MineTweakerAPI.apply(new RemoveRecipe(CrystallizerRecipes.instance(), SorptionType.OUTPUT, Lists.newArrayList(output1)));
		}
	}
	
	@ZenClass("mods.nuclearcraft.fission")
	public static class FissionHandler {
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IItemStack output1) {
			MineTweakerAPI.apply(new AddRecipe(FissionRecipes.instance(), Lists.newArrayList(input1), Lists.newArrayList(MineTweakerMC.getItemStack(output1))));
		}

		@ZenMethod
		public static void removeRecipe(IIngredient output1) {
			MineTweakerAPI.apply(new RemoveRecipe(FissionRecipes.instance(), SorptionType.OUTPUT, Lists.newArrayList(output1)));
		}
	}
	
	@ZenClass("mods.nuclearcraft.fusion")
	public static class FusionHandler {
		
		@ZenMethod
		public static void addRecipe(IIngredient input1, IIngredient input2, ILiquidStack output1, ILiquidStack output2, ILiquidStack output3, ILiquidStack output4) {
			MineTweakerAPI.apply(new AddRecipe(FusionRecipes.instance(), Lists.newArrayList(input1, input2), Lists.newArrayList(MineTweakerMC.getLiquidStack(output1), MineTweakerMC.getLiquidStack(output2), MineTweakerMC.getLiquidStack(output3), MineTweakerMC.getLiquidStack(output4))));
		}

		@ZenMethod
		public static void removeRecipe(IIngredient output1, IIngredient output2, IIngredient output3, IIngredient output4) {
			MineTweakerAPI.apply(new RemoveRecipe(FusionRecipes.instance(), SorptionType.OUTPUT, Lists.newArrayList(output1, output2, output3, output4)));
		}
	}
}
