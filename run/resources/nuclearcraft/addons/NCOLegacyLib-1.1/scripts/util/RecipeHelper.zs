#priority 2147483646

import scripts.nc_script_addons.NCOLegacyLib.util.UtilClasses.OreInfo;
import scripts.nc_script_addons.NCOLegacyLib.util.UtilClasses.LiquidInfo;

import crafttweaker.item.IIngredient;
import crafttweaker.liquid.ILiquidStack;
import crafttweaker.oredict.IOreDict;
import crafttweaker.oredict.IOreDictEntry;

import mods.nuclearcraft.ChanceItemIngredient;
import mods.nuclearcraft.ChanceFluidIngredient;

function ingr_s(info as OreInfo, suffix as string) as IIngredient {
	if (info.ore.matches("null")) {
		return null;
	}
	
	var baseIngr = (oreDict.get(info.ore ~ suffix) * info.maxStackSize) as IIngredient;
	return info.chancePercent < 0 ? baseIngr : ChanceItemIngredient.create(baseIngr, info.chancePercent, info.minStackSize);
}

function liq_s(info as LiquidInfo, suffix as string) as IIngredient {
	if (info.name.matches("null")) {
		return null;
	}
	
	var baseIngr = (game.getLiquid(info.name ~ suffix) * info.maxStackSize) as IIngredient;
	return info.chancePercent < 0 ? baseIngr : ChanceFluidIngredient.create(baseIngr, info.chancePercent, info.stackDiff, info.minStackSize);
}
