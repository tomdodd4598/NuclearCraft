package nc.integration.crafttweaker;

import com.google.common.collect.Lists;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.entity.IEntityLivingBase;
import crafttweaker.api.item.*;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.liquid.ILiquidStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.api.oredict.IOreDictEntry;
import nc.capability.radiation.entity.IEntityRads;
import nc.radiation.*;
import nc.recipe.ingredient.*;
import net.minecraft.client.util.RecipeItemHelper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import stanhebben.zenscript.annotations.*;

public class CTRadiation {
	
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
				return stack.isEmpty() ? 0D : RadSources.STACK_MAP.get(RecipeItemHelper.pack(stack)) * stack.getCount();
			}
			else if (ingredient instanceof IOreDictEntry) {
				IOreDictEntry ore = (IOreDictEntry) ingredient;
				return RadSources.ORE_MAP.getDouble(ore.getName()) * ore.getAmount();
			}
			else if (ingredient instanceof IngredientStack) {
				IItemIngredient i = CTHelper.buildOreIngredientArray(ingredient, true);
				if (i instanceof OreIngredient) {
					OreIngredient ore = (OreIngredient) i;
					return RadSources.ORE_MAP.getDouble(ore.oreName) * ore.stackSize;
				}
				else {
					ItemStack stack = i.getStack();
					return stack == null || stack.isEmpty() ? 0D : RadSources.STACK_MAP.get(RecipeItemHelper.pack(stack)) * stack.getCount();
				}
			}
			else if (ingredient instanceof ILiquidStack) {
				FluidStack stack = CTHelper.getFluidStack((ILiquidStack) ingredient);
				return stack == null ? 0D : RadiationHelper.getRadiationFromFluid(stack, 1D);
			}
			else {
				return 0D;
			}
		}
		
		@ZenMethod
		public static void addToRadiationBlacklist(IIngredient ingredient) {
			boolean success = false;
			if (ingredient instanceof IItemStack) {
				RadSources.RUNNABLES.add(() -> RadSources.addToStackBlacklist(CTHelper.getItemStack((IItemStack) ingredient)));
				success = true;
			}
			else if (ingredient instanceof IOreDictEntry) {
				RadSources.RUNNABLES.add(() -> RadSources.addToOreBlacklist(((IOreDictEntry) ingredient).getName()));
				success = true;
			}
			else if (ingredient instanceof IngredientStack) {
				IItemIngredient i = CTHelper.buildOreIngredientArray(ingredient, true);
				if (i instanceof OreIngredient) {
					RadSources.RUNNABLES.add(() -> RadSources.addToOreBlacklist(((OreIngredient) i).oreName));
					success = true;
				}
				else if (i.getStack() != null) {
					RadSources.RUNNABLES.add(() -> RadSources.addToStackBlacklist(i.getStack()));
					success = true;
				}
			}
			else if (ingredient instanceof ILiquidStack) {
				FluidStack stack = CTHelper.getFluidStack((ILiquidStack) ingredient);
				if (stack != null && stack.getFluid() != null) {
					RadSources.RUNNABLES.add(() -> RadSources.addToFluidBlacklist(stack.getFluid().getName()));
					success = true;
				}
			}
			
			if (success) {
				CraftTweakerAPI.logInfo("Added " + ingredient.toCommandString() + " to radiation blacklist");
			}
			else {
				if (ingredient == null) {
					CraftTweakerAPI.logError("Attempted to add null ingredient to radiation blacklist");
				}
				else {
					CraftTweakerAPI.logError("Failed to add " + ingredient.toCommandString() + " to radiation blacklist");
				}
			}
		}
		
		@ZenMethod
		public static void setRadiationLevel(IIngredient ingredient, double radiation) {
			boolean success = false;
			if (ingredient instanceof IItemStack) {
				RadSources.RUNNABLES.add(() -> RadSources.addToStackMap(CTHelper.getItemStack((IItemStack) ingredient), radiation));
				success = true;
			}
			else if (ingredient instanceof IOreDictEntry) {
				RadSources.RUNNABLES.add(() -> RadSources.addToOreMap(((IOreDictEntry) ingredient).getName(), radiation));
				success = true;
			}
			else if (ingredient instanceof IngredientStack) {
				IItemIngredient i = CTHelper.buildOreIngredientArray(ingredient, true);
				if (i instanceof OreIngredient) {
					RadSources.RUNNABLES.add(() -> RadSources.addToOreMap(((OreIngredient) i).oreName, radiation));
					success = true;
				}
				else if (i.getStack() != null) {
					RadSources.RUNNABLES.add(() -> RadSources.addToStackMap(i.getStack(), radiation));
					success = true;
				}
			}
			else if (ingredient instanceof ILiquidStack) {
				FluidStack stack = CTHelper.getFluidStack((ILiquidStack) ingredient);
				if (stack != null && stack.getFluid() != null) {
					RadSources.RUNNABLES.add(() -> RadSources.addToFluidMap(stack.getFluid().getName(), radiation));
					success = true;
				}
			}
			
			String rads = RadiationHelper.radsPrefix(radiation, true);
			if (success) {
				CraftTweakerAPI.logInfo("Set the radiation level of " + ingredient.toCommandString() + " to " + rads);
			}
			else {
				if (ingredient == null) {
					CraftTweakerAPI.logError("Attempted to set the radiation level of a null ingredient to " + rads);
				}
				else {
					CraftTweakerAPI.logError("Failed to set the radiation level of " + ingredient.toCommandString() + " to " + rads);
				}
			}
		}
		
		@ZenMethod
		public static void setMaterialPrefixRadiationMultiplier(String orePrefix, double radiationMultiplier) {
			if (orePrefix == null) {
				CraftTweakerAPI.logError("Attempted to set the material prefix radiation multiplier for a null ore dictionary prefix to " + radiationMultiplier);
			}
			else {
				RadSources.addMaterialPrefixMultiplier(orePrefix, radiationMultiplier);
				CraftTweakerAPI.logInfo("Set the material prefix radiation multiplier for the ore prefix \"" + orePrefix + "\" to " + radiationMultiplier);
			}
		}
		
		@ZenMethod
		public static void setMaterialRadiationLevel(String oreSuffix, double radiation) {
			String rads = RadiationHelper.radsPrefix(radiation, true);
			if (oreSuffix == null) {
				CraftTweakerAPI.logError("Attempted to set the material radiation level for a null ore dictionary suffix to " + rads);
			}
			else {
				RadSources.RUNNABLES.add(() -> RadSources.putMaterial(radiation, oreSuffix));
				CraftTweakerAPI.logInfo("Set the material radiation level for the ore suffix \"" + oreSuffix + "\" to " + rads);
			}
		}
		
		@ZenMethod
		public static void setIsotopeRadiationLevel(String oreSuffix, String fluidName, double radiation) {
			String rads = RadiationHelper.radsPrefix(radiation, true);
			if (oreSuffix == null) {
				CraftTweakerAPI.logError("Attempted to set the isotope radiation level for a null ore dictionary suffix to " + rads);
			}
			else if (fluidName == null) {
				CraftTweakerAPI.logError("Attempted to set the isotope radiation level for a null fluid name to " + rads);
			}
			else {
				RadSources.RUNNABLES.add(() -> RadSources.putIsotope(radiation, oreSuffix, fluidName));
				CraftTweakerAPI.logInfo("Set the isotope radiation level for the ore suffix \"" + oreSuffix + "\" and fluid name \"" + fluidName + "\" to " + rads);
			}
		}
		
		@ZenMethod
		public static void setIsotopeRadiationLevel(String oreSuffix, double radiation) {
			String rads = RadiationHelper.radsPrefix(radiation, true);
			if (oreSuffix == null) {
				CraftTweakerAPI.logError("Attempted to set the isotope radiation level for a null ore dictionary suffix to " + rads);
			}
			else {
				RadSources.RUNNABLES.add(() -> RadSources.putIsotope(radiation, oreSuffix, null));
				CraftTweakerAPI.logInfo("Set the isotope radiation level for the ore suffix \"" + oreSuffix + "\" to " + rads);
			}
		}
		
		@ZenMethod
		public static void setFuelRadiationLevel(String oreSuffix, String fluidName, double fuelRadiation, double depletedRadiation) {
			String rads = RadiationHelper.radsPrefix(fuelRadiation, true);
			if (oreSuffix == null) {
				CraftTweakerAPI.logError("Attempted to set the fuel radiation level for a null ore dictionary suffix to " + rads);
			}
			else if (fluidName == null) {
				CraftTweakerAPI.logError("Attempted to set the fuel radiation level for a null fluid name to " + rads);
			}
			else {
				RadSources.RUNNABLES.add(() -> RadSources.putFuel(fuelRadiation, depletedRadiation, oreSuffix, fluidName));
				CraftTweakerAPI.logInfo("Set the fuel radiation level for the ore suffix \"" + oreSuffix + "\" and fluid name \"" + fluidName + "\" to " + rads);
			}
		}
		
		@ZenMethod
		public static void setFuelRadiationLevel(String oreSuffix, double fuelRadiation, double depletedRadiation) {
			String rads = RadiationHelper.radsPrefix(fuelRadiation, true);
			if (oreSuffix == null) {
				CraftTweakerAPI.logError("Attempted to set the fuel radiation level for a null ore dictionary suffix to " + rads);
			}
			else {
				RadSources.RUNNABLES.add(() -> RadSources.putFuel(fuelRadiation, depletedRadiation, oreSuffix, null));
				CraftTweakerAPI.logInfo("Set the fuel radiation level for the ore suffix \"" + oreSuffix + "\" to " + rads);
			}
		}
		
		@ZenMethod
		public static void setFoodRadiationStats(IItemStack food, double radiation, double resistance) {
			String rads = RadiationHelper.radsPrefix(radiation, true);
			String res = RadiationHelper.resistanceSigFigs(resistance);
			if (food == null) {
				CraftTweakerAPI.logError("Attempted to set the food radiation and resistance levels of a null ingredient to " + rads + " and " + res + ", respectively");
			}
			else {
				RadSources.RUNNABLES.add(() -> RadSources.addToFoodMaps(CTHelper.getItemStack(food), radiation, resistance));
				CraftTweakerAPI.logInfo("Set the food radiation and resistance levels of " + food.toCommandString() + " to " + rads + " and " + res + ", respectively");
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
