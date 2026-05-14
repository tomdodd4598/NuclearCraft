#priority 2147483646

#loader preinit nc_preinit contenttweaker crafttweaker

import scripts.nc_script_addons.NCOLegacyLib.util.UtilClasses.OreInfo;
import scripts.nc_script_addons.NCOLegacyLib.util.UtilClasses.LiquidInfo;

// INGREDIENT

static NULL_ORE_INFO as OreInfo = OreInfo("null", 0);

static NULL_LIQUID_INFO as LiquidInfo = LiquidInfo("null", 0);

// MATH

function min_d(a as double, b as double) as double {
	return a < b ? a : b;
}

function min_f(a as float, b as float) as float {
	return a < b ? a : b;
}

function min_i(a as int, b as int) as int {
	return a < b ? a : b;
}

function min_l(a as long, b as long) as long {
	return a < b ? a : b;
}

function max_d(a as double, b as double) as double {
	return a > b ? a : b;
}

function max_f(a as float, b as float) as float {
	return a > b ? a : b;
}

function max_i(a as int, b as int) as int {
	return a > b ? a : b;
}

function max_l(a as long, b as long) as long {
	return a > b ? a : b;
}

function clamp_d(x as double, min as double, max as double) as double {
	return x < min ? min : (x > max ? max : x);
}

function clamp_f(x as float, min as float, max as float) as float {
	return x < min ? min : (x > max ? max : x);
}

function clamp_i(x as int, min as int, max as int) as int {
	return x < min ? min : (x > max ? max : x);
}

function clamp_l(x as long, min as long, max as long) as long {
	return x < min ? min : (x > max ? max : x);
}
