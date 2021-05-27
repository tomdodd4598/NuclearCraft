#priority 2147483645

#loader preinit nc_preinit contenttweaker crafttweaker

import crafttweaker.item.IIngredient;
import crafttweaker.liquid.ILiquidStack;
import crafttweaker.oredict.IOreDict;
import crafttweaker.oredict.IOreDictEntry;

static ISOTOPE_ORE_SUFFIX as string[] = ["", "Carbide", "Oxide", "Nitride", "ZA"];

static PELLET_ORE_SUFFIX as string[] = ["", "Carbide"];

static ALL_SOLID_FUEL_ORE_SUFFIX as string[] = ["TRISO", "Oxide", "Nitride", "ZA"];

static TRISO_ORE_SUFFIX as string = "TRISO";
static TRISO_TIME_MULT as double = 0.9;
static TRISO_HEAT_MULT as double = 1.0 / 0.9;
static TRISO_CRIT_MULT as double = 0.9;

static SFR_ORE_SUFFIX as string[] = ["Oxide", "Nitride", "ZA"];
static SFR_TIME_MULT as double[] = [1.0, 1.25, 0.8];
static SFR_HEAT_MULT as double[] = [1.0, 0.8, 1.25];
static SFR_CRIT_MULT as double[] = [1.0, 1.25, 0.85];

static MSR_TIME_MULT as double = 1.25;
static MSR_HEAT_MULT as double = 0.8;
static MSR_CRIT_MULT as double = 1.0;
