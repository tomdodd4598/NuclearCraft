import scripts.nc_script_addons.NCOLegacyLib.fission.FissionClasses.FissionIsotopeInfo;
import scripts.nc_script_addons.NCOLegacyLib.fission.FissionClasses.FissionIsotopeInfoListContainer;
import scripts.nc_script_addons.NCOLegacyLib.fission.FissionClasses.FissionFuelInfo;
import scripts.nc_script_addons.NCOLegacyLib.fission.FissionClasses.FissionFuelInfoListContainer;
import scripts.nc_script_addons.NCOLegacyLib.fission.FissionClasses.FuelReprocessorInfo;
import scripts.nc_script_addons.NCOLegacyLib.fission.FissionClasses.FuelReprocessorInfoListContainer;
import scripts.nc_script_addons.NCOLegacyLib.fission.FissionClasses.FuelCentrifugeInfo;
import scripts.nc_script_addons.NCOLegacyLib.fission.FissionClasses.FuelCentrifugeInfoListContainer;
import scripts.nc_script_addons.NCOLegacyLib.fission.FissionHelper;
import scripts.nc_script_addons.NCOLegacyLib.fission.FissionSetup;

import scripts.nc_script_addons.NCOLegacyLib.util.UtilClasses.OreInfo;
import scripts.nc_script_addons.NCOLegacyLib.util.UtilClasses.LiquidInfo;
import scripts.nc_script_addons.NCOLegacyLib.util.RecipeHelper;

import crafttweaker.item.IIngredient;
import crafttweaker.liquid.ILiquidStack;
import crafttweaker.oredict.IOreDict;
import crafttweaker.oredict.IOreDictEntry;

import mods.nuclearcraft.Separator;
import mods.nuclearcraft.FuelReprocessor;
import mods.nuclearcraft.AlloyFurnace;
import mods.nuclearcraft.Infuser;
import mods.nuclearcraft.Melter;
import mods.nuclearcraft.Electrolyzer;
import mods.nuclearcraft.Assembler;
import mods.nuclearcraft.IngotFormer;
import mods.nuclearcraft.ChemicalReactor;
import mods.nuclearcraft.SaltMixer;
import mods.nuclearcraft.Centrifuge;
import mods.nuclearcraft.PebbleFission;
import mods.nuclearcraft.SolidFission;
import mods.nuclearcraft.SaltFission;

import mods.nuclearcraft.Radiation;

//-------------------------------------------------------------------------------------------------------------------------

function ingr(info as OreInfo) as IIngredient {
	return RecipeHelper.ingr_s(info, "");
}

function c(info as OreInfo) as IIngredient {
	return RecipeHelper.ingr_s(info, "Carbide");
}

function tr(info as OreInfo) as IIngredient {
	return RecipeHelper.ingr_s(info, "TRISO");
}

function ox(info as OreInfo) as IIngredient {
	return RecipeHelper.ingr_s(info, "Oxide");
}

function ni(info as OreInfo) as IIngredient {
	return RecipeHelper.ingr_s(info, "Nitride");
}

function za(info as OreInfo) as IIngredient {
	return RecipeHelper.ingr_s(info, "ZA");
}

function liq(info as LiquidInfo) as IIngredient {
	return RecipeHelper.liq_s(info, "");
}

//-------------------------------------------------------------------------------------------------------------------------

function addIsotopeRadiation(info as FissionIsotopeInfo) {
	if (info.hasLiquid) {
		Radiation.setIsotopeRadiationLevel(info.ore, info.name, info.rad);
	}
	else {
		Radiation.setIsotopeRadiationLevel(info.ore, info.rad);
	}
}

function registerIsotopeOreDictEntries(info as FissionIsotopeInfo) {
	for i in 0 to 5 {
		var isotope = oreDict.get("ingot" ~ info.ore ~ FissionHelper.ISOTOPE_ORE_SUFFIX[i]);
		isotope.add(itemUtils.getItem("contenttweaker:" ~ info.name ~ info.suffix[i]));
	}
}

function addIsotopeAuxiliaryRecipes(info as FissionIsotopeInfo) {
	AlloyFurnace.addRecipe(oreDict.get("ingot" ~ info.ore), <ore:ingotZirconium> | <ore:dustZirconium>, oreDict.get("ingot" ~ info.ore ~ "ZA"));
	AlloyFurnace.addRecipe(oreDict.get("ingot" ~ info.ore), <ore:ingotGraphite> | <ore:dustGraphite>, oreDict.get("ingot" ~ info.ore ~ "Carbide"));
	Infuser.addRecipe(oreDict.get("ingot" ~ info.ore), <liquid:oxygen> * 1000, oreDict.get("ingot" ~ info.ore ~ "Oxide"));
	Infuser.addRecipe(oreDict.get("ingot" ~ info.ore), <liquid:nitrogen> * 1000, oreDict.get("ingot" ~ info.ore ~ "Nitride"));
	Separator.addRecipe(oreDict.get("ingot" ~ info.ore ~ "ZA"), oreDict.get("ingot" ~ info.ore), <ore:dustZirconium>);
	Separator.addRecipe(oreDict.get("ingot" ~ info.ore ~ "Carbide"), oreDict.get("ingot" ~ info.ore), <ore:dustGraphite>);
	
	furnace.addRecipe(itemUtils.getItem("contenttweaker:" ~ info.name), itemUtils.getItem("contenttweaker:" ~ info.name ~ info.suffix[2]));
	furnace.addRecipe(itemUtils.getItem("contenttweaker:" ~ info.name), itemUtils.getItem("contenttweaker:" ~ info.name ~ info.suffix[3]));
	
	if (info.hasLiquid) {
		IngotFormer.addRecipe(game.getLiquid(info.name) * 144, oreDict.get("ingot" ~ info.ore));
		Melter.addRecipe(oreDict.get("ingot" ~ info.ore), game.getLiquid(info.name) * 144);
	}
}

function addFuelRadiation(info as FissionFuelInfo) {
	if (info.hasLiquid) {
		Radiation.setFuelRadiationLevel(info.ore, info.name, info.fuelRad, info.depletedRad);
	}
	else {
		Radiation.setFuelRadiationLevel(info.ore, info.fuelRad, info.depletedRad);
	}
}

function registerFuelOreDictEntries(info as FissionFuelInfo) {
	for i in 0 to 2 {
		var pellet = oreDict.get("ingot" ~ info.ore ~ FissionHelper.PELLET_ORE_SUFFIX[i]);
		pellet.add(itemUtils.getItem("contenttweaker:" ~ info.solidPrefix[0] ~ info.name ~ info.solidSuffix[i]));
	}
	
	for i in 0 to 4 {
		var fuel = oreDict.get("ingot" ~ info.ore ~ FissionHelper.ALL_SOLID_FUEL_ORE_SUFFIX[i]);
		fuel.add(itemUtils.getItem("contenttweaker:" ~ info.solidPrefix[1] ~ info.name ~ info.solidSuffix[i + 2]));
		
		var depleted = oreDict.get("ingotDepleted" ~ info.ore ~ FissionHelper.ALL_SOLID_FUEL_ORE_SUFFIX[i]);
		depleted.add(itemUtils.getItem("contenttweaker:" ~ info.solidPrefix[2] ~ info.name ~ info.solidSuffix[i + 2]));
	}
}

function addFuelAuxiliaryRecipes(info as FissionFuelInfo) {
	AlloyFurnace.addRecipe(oreDict.get("ingot" ~ info.ore), <ore:ingotZirconium> | <ore:dustZirconium>, oreDict.get("ingot" ~ info.ore ~ "ZA"));
	AlloyFurnace.addRecipe(oreDict.get("ingot" ~ info.ore), <ore:ingotGraphite> | <ore:dustGraphite>, oreDict.get("ingot" ~ info.ore ~ "Carbide"));
	Infuser.addRecipe(oreDict.get("ingot" ~ info.ore), <liquid:oxygen> * 1000, oreDict.get("ingot" ~ info.ore ~ "Oxide"));
	Infuser.addRecipe(oreDict.get("ingot" ~ info.ore), <liquid:nitrogen> * 1000, oreDict.get("ingot" ~ info.ore ~ "Nitride"));
	Separator.addRecipe(oreDict.get("ingot" ~ info.ore ~ "ZA"), oreDict.get("ingot" ~ info.ore), <ore:dustZirconium>);
	Separator.addRecipe(oreDict.get("ingot" ~ info.ore ~ "Carbide"), oreDict.get("ingot" ~ info.ore), <ore:dustGraphite>);
	Assembler.addRecipe(oreDict.get("ingot" ~ info.ore ~ "Carbide") * 9, <ore:dustGraphite>, <ore:ingotPyrolyticCarbon>, <ore:ingotSiliconCarbide>, oreDict.get("ingot" ~ info.ore ~ "TRISO") * 9);
	
	furnace.addRecipe(itemUtils.getItem("contenttweaker:" ~ info.solidPrefix[0] ~ info.name), itemUtils.getItem("contenttweaker:" ~ info.solidPrefix[1] ~ info.name ~ info.solidSuffix[3]));
	furnace.addRecipe(itemUtils.getItem("contenttweaker:" ~ info.solidPrefix[0] ~ info.name), itemUtils.getItem("contenttweaker:" ~ info.solidPrefix[1] ~ info.name ~ info.solidSuffix[4]));
	
	if (info.hasLiquid) {
		ChemicalReactor.addRecipe(game.getLiquid(info.name) * 72, <liquid:fluorine> * 500, game.getLiquid(info.name ~ info.liquidSuffix[1]) * 72, null, 0.5, 0.5);
		Electrolyzer.addRecipe(game.getLiquid(info.name ~ info.liquidSuffix[1]) * 72, game.getLiquid(info.name) * 72, <liquid:fluorine> * 500, null, null, 0.5, 1.0);
		SaltMixer.addRecipe(game.getLiquid(info.name ~ info.liquidSuffix[1]) * 72, <liquid:flibe> * 72, game.getLiquid(info.name ~ info.liquidSuffix[2]) * 72, 0.5, 1.0);
		Centrifuge.addRecipe(game.getLiquid(info.name ~ info.liquidSuffix[2]) * 72, game.getLiquid(info.name ~ info.liquidSuffix[1]) * 72, <liquid:flibe> * 72, null, null, null, null, 0.5, 1.0);
		Centrifuge.addRecipe(game.getLiquid(info.liquidPrefix[0] ~ info.name ~ info.liquidSuffix[2]) * 72, game.getLiquid(info.liquidPrefix[0] ~ info.name ~ info.liquidSuffix[1]) * 72, <liquid:flibe> * 72, null, null, null, null, 0.5, 1.0);
		Electrolyzer.addRecipe(game.getLiquid(info.liquidPrefix[0] ~ info.name ~ info.liquidSuffix[1]) * 72, game.getLiquid(info.liquidPrefix[0] ~ info.name) * 72, <liquid:fluorine> * 500, null, null, 0.5, 1.0);
		IngotFormer.addRecipe(game.getLiquid(info.name) * 144, oreDict.get("ingot" ~ info.ore));
		Melter.addRecipe(oreDict.get("ingot" ~ info.ore), game.getLiquid(info.name) * 144);
	}
}

function addPebbleFissionRecipes(info as FissionFuelInfo) {
	var fuel = oreDict.get("ingot" ~ info.ore ~ FissionHelper.TRISO_ORE_SUFFIX);
	var depleted = oreDict.get("ingotDepleted" ~ info.ore ~ FissionHelper.TRISO_ORE_SUFFIX);
	PebbleFission.addRecipe(fuel, depleted, (FissionHelper.TRISO_TIME_MULT * info.time) as int, (FissionHelper.TRISO_HEAT_MULT * info.heat) as int, info.efficiency, (FissionHelper.TRISO_CRIT_MULT * info.crit) as int, info.decay, info.prime, (info.fuelRad + info.depletedRad) / 64.0);
}

function addSolidFissionRecipes(info as FissionFuelInfo) {
	for i in 0 to 3 {
		var fuel = oreDict.get("ingot" ~ info.ore ~ FissionHelper.SFR_ORE_SUFFIX[i]);
		var depleted = oreDict.get("ingotDepleted" ~ info.ore ~ FissionHelper.SFR_ORE_SUFFIX[i]);
		SolidFission.addRecipe(fuel, depleted, (FissionHelper.SFR_TIME_MULT[i] * info.time) as int, (FissionHelper.SFR_HEAT_MULT[i] * info.heat) as int, info.efficiency, (FissionHelper.SFR_CRIT_MULT[i] * info.crit) as int, info.decay, info.prime, (info.fuelRad + info.depletedRad) / 64.0);
	}
}

function addSaltFissionRecipes(info as FissionFuelInfo) {
	var fuel = game.getLiquid(info.name ~ info.liquidSuffix[2]);
	var depleted = game.getLiquid(info.liquidPrefix[0] ~ info.name ~ info.liquidSuffix[2]);
	SaltFission.addRecipe(fuel, depleted, FissionHelper.MSR_TIME_MULT * info.time / 144.0, (FissionHelper.MSR_HEAT_MULT * info.heat) as int, info.efficiency, (FissionHelper.MSR_CRIT_MULT * info.crit) as int, info.decay, info.prime, (info.fuelRad + info.depletedRad) / 64.0);
}

function addFuelReprocessorRecipes(info as FuelReprocessorInfo) {
	FuelReprocessor.addRecipe(oreDict.get(info.ore ~ "TRISO") * 9, c(info.out0), c(info.out1), ingr(info.out2), <ore:dustGraphite> * (info.nXtra + 2), c(info.out4), c(info.out5), ingr(info.out6), oreDict has "dustSiliconCarbide" ? <ore:dustSiliconCarbide> : <ore:ingotSiliconCarbide>);
	FuelReprocessor.addRecipe(oreDict.get(info.ore ~ "Oxide") * 9, ox(info.out0), ox(info.out1), ingr(info.out2), ingr(info.out3), ox(info.out4), ox(info.out5), ingr(info.out6), ingr(info.out7));
	FuelReprocessor.addRecipe(oreDict.get(info.ore ~ "Nitride") * 9, ni(info.out0), ni(info.out1), ingr(info.out2), ingr(info.out3), ni(info.out4), ni(info.out5), ingr(info.out6), ingr(info.out7));
	FuelReprocessor.addRecipe(oreDict.get(info.ore ~ "ZA") * 9, za(info.out0), za(info.out1), ingr(info.out2), <ore:dustZirconium> * info.nXtra, za(info.out4), za(info.out5), ingr(info.out6), ingr(info.out7));
}

function addCentrifugeRecipes(info as FuelCentrifugeInfo) {
	Centrifuge.addRecipe(game.getLiquid(info.name) * 144, liq(info.out0), liq(info.out1), liq(info.out2), liq(info.out3), liq(info.out4), liq(info.out5));
}

//-------------------------------------------------------------------------------------------------------------------------

for info in FissionSetup.FISSION_ISOTOPE_INFOS.get() {
	addIsotopeRadiation(info);
	registerIsotopeOreDictEntries(info);
}

for info in FissionSetup.FISSION_ISOTOPE_INFOS.get() {
	addIsotopeAuxiliaryRecipes(info);
}

for info in FissionSetup.FISSION_FUEL_INFOS.get() {
	addFuelRadiation(info);
	registerFuelOreDictEntries(info);
}

for info in FissionSetup.FISSION_FUEL_INFOS.get() {
	addFuelAuxiliaryRecipes(info);
	
	addPebbleFissionRecipes(info);
	addSolidFissionRecipes(info);
	
	if (info.hasLiquid) {
		addSaltFissionRecipes(info);
	}
}

for info in FissionSetup.FUEL_REPROCESSOR_INFOS.get() {
	addFuelReprocessorRecipes(info);
}

for info in FissionSetup.FUEL_CENTRIFUGE_INFOS.get() {
	addCentrifugeRecipes(info);
}
