#priority 2147483645

#loader preinit nc_preinit contenttweaker crafttweaker

import scripts.nc_script_addons.NCOLegacyLib.util.UtilClasses.OreInfo;
import scripts.nc_script_addons.NCOLegacyLib.util.UtilClasses.LiquidInfo;
import scripts.nc_script_addons.NCOLegacyLib.util.SetupHelper;

zenClass FissionIsotopeInfo {
	var name as string;
	var suffix as string[];
	var ore as string;
	var liquidColor as int;
	var hasLiquid as bool;
	var rad as double;
	
	zenConstructor(name as string, suffix as string[], ore as string, rad as double) {
		this.name = name;
		this.suffix = suffix;
		this.ore = ore;
		this.liquidColor = 0;
		this.hasLiquid = false;
		this.rad = rad;
	}
	
	zenConstructor(name as string, suffix as string[], ore as string, liquidColor as int, rad as double) {
		this.name = name;
		this.suffix = suffix;
		this.ore = ore;
		this.liquidColor = liquidColor;
		this.hasLiquid = true;
		this.rad = rad;
	}
}

zenClass FissionIsotopeInfoListContainer {
	var internal as [FissionIsotopeInfo];
	
	zenConstructor() {
		internal = [];
	}
	
	function get() as [FissionIsotopeInfo] {
		return internal;
	}
	
	function add(entry as FissionIsotopeInfo) {
		internal += entry;
	}
	
	function clear() {
		internal = [];
	}
}

zenClass FissionFuelInfo {
	var name as string;
	var solidPrefix as string[];
	var solidSuffix as string[];
	var liquidPrefix as string[];
	var liquidSuffix as string[];
	var ore as string;
	var liquidColor as int;
	var depletedLiquidColor as int;
	var hasLiquid as bool;
	
	var time as int;
	var heat as int;
	var efficiency as double;
	var crit as int;
	var decay as double;
	var prime as bool;
	var fuelRad as double;
	var depletedRad as double;
	
	zenConstructor(name as string, solidPrefix as string[], solidSuffix as string[], ore as string, time as int, heat as int, efficiency as double, crit as int, decay as double, prime as bool, fuelRad as double, depletedRad as double) {
		this.name = name;
		this.solidPrefix = solidPrefix;
		this.solidSuffix = solidSuffix;
		this.liquidPrefix = [] as string[];
		this.liquidSuffix = [] as string[];
		this.ore = ore;
		this.liquidColor = 0;
		this.depletedLiquidColor = 0;
		this.hasLiquid = false;
		
		this.time = time;
		this.heat = heat;
		this.efficiency = efficiency;
		this.crit = crit;
		this.decay = decay;
		this.prime = prime;
		this.fuelRad = fuelRad;
		this.depletedRad = depletedRad;
	}
	
	zenConstructor(name as string, solidPrefix as string[], solidSuffix as string[], liquidPrefix as string[], liquidSuffix as string[], ore as string, liquidColor as int, depletedLiquidColor as int, time as int, heat as int, efficiency as double, crit as int, decay as double, prime as bool, fuelRad as double, depletedRad as double) {
		this.name = name;
		this.solidPrefix = solidPrefix;
		this.solidSuffix = solidSuffix;
		this.liquidPrefix = liquidPrefix;
		this.liquidSuffix = liquidSuffix;
		this.ore = ore;
		this.liquidColor = liquidColor;
		this.depletedLiquidColor = depletedLiquidColor;
		this.hasLiquid = true;
		
		this.time = time;
		this.heat = heat;
		this.efficiency = efficiency;
		this.crit = crit;
		this.decay = decay;
		this.prime = prime;
		this.fuelRad = fuelRad;
		this.depletedRad = depletedRad;
	}
}

zenClass FissionFuelInfoListContainer {
	var internal as [FissionFuelInfo];
	
	zenConstructor() {
		internal = [];
	}
	
	function get() as [FissionFuelInfo] {
		return internal;
	}
	
	function add(entry as FissionFuelInfo) {
		internal += entry;
	}
	
	function clear() {
		internal = [];
	}
}

zenClass FuelReprocessorInfo {
	var nXtra as int;
	var ore as string;
	var out0 as OreInfo;
	var out1 as OreInfo;
	var out2 as OreInfo;
	var out3 as OreInfo;
	var out4 as OreInfo;
	var out5 as OreInfo;
	var out6 as OreInfo;
	var out7 as OreInfo;
	
	zenConstructor(ore as string, nXtraBase as int, out0 as string, n0 as int, out1 as string, n1 as int, out2 as string, n2 as int, out3 as string, n3 as int, waste1 as OreInfo, waste2 as OreInfo) {
		this.nXtra = nXtraBase - n0 - n1 - n2 - n3;
		this.ore = ore;
		this.out0 = OreInfo(out0, n0);
		this.out1 = OreInfo(out1, n1);
		this.out2 = waste1;
		this.out3 = SetupHelper.NULL_ORE_INFO;
		this.out4 = OreInfo(out2, n2);
		this.out5 = OreInfo(out3, n3);
		this.out6 = waste2;
		this.out7 = SetupHelper.NULL_ORE_INFO;
	}
}

zenClass FuelReprocessorInfoListContainer {
	var internal as [FuelReprocessorInfo];
	
	zenConstructor() {
		internal = [];
	}
	
	function get() as [FuelReprocessorInfo] {
		return internal;
	}
	
	function add(entry as FuelReprocessorInfo) {
		internal += entry;
	}
	
	function clear() {
		internal = [];
	}
}

zenClass FuelCentrifugeInfo {
	var name as string;
	var out0 as LiquidInfo;
	var out1 as LiquidInfo;
	var out2 as LiquidInfo;
	var out3 as LiquidInfo;
	var out4 as LiquidInfo;
	var out5 as LiquidInfo;
	
	zenConstructor(name as string, out0 as string, n0 as int, out1 as string, n1 as int, out2 as string, n2 as int, out3 as string, n3 as int, waste1 as LiquidInfo, waste2 as LiquidInfo) {
		this.name = name;
		this.out0 = LiquidInfo(out0, n0 * 16);
		this.out1 = LiquidInfo(out1, n1 * 16);
		this.out2 = waste1;
		this.out3 = LiquidInfo(out2, n2 * 16);
		this.out4 = LiquidInfo(out3, n3 * 16);
		this.out5 = waste2;
	}
}

zenClass FuelCentrifugeInfoListContainer {
	var internal as [FuelCentrifugeInfo];
	
	zenConstructor() {
		internal = [];
	}
	
	function get() as [FuelCentrifugeInfo] {
		return internal;
	}
	
	function add(entry as FuelCentrifugeInfo) {
		internal += entry;
	}
	
	function clear() {
		internal = [];
	}
}
