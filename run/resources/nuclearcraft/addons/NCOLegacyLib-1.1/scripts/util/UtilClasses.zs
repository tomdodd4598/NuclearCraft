#priority 2147483647

#loader preinit nc_preinit contenttweaker crafttweaker

zenClass OreInfo {
	var ore as string;
	var maxStackSize as int;
	var chancePercent as int;
	var minStackSize as int;
	
	zenConstructor(ore as string, maxStackSize as int) {
		this.ore = ore;
		this.maxStackSize = maxStackSize;
		this.chancePercent = -1;
		this.minStackSize = 0;
	}
	
	zenConstructor(ore as string, maxStackSize as int, chancePercent as int) {
		this.ore = ore;
		this.maxStackSize = maxStackSize;
		this.chancePercent = chancePercent;
		this.minStackSize = 0;
	}
	
	zenConstructor(ore as string, maxStackSize as int, chancePercent as int, minStackSize as int) {
		this.ore = ore;
		this.maxStackSize = maxStackSize;
		this.chancePercent = chancePercent;
		this.minStackSize = minStackSize;
	}
}

zenClass LiquidInfo {
	var name as string;
	var maxStackSize as int;
	var chancePercent as int;
	var stackDiff as int;
	var minStackSize as int;
	
	zenConstructor(name as string, maxStackSize as int) {
		this.name = name;
		this.maxStackSize = maxStackSize;
		this.chancePercent = -1;
		this.stackDiff = -1;
		this.minStackSize = 0;
	}
	
	zenConstructor(name as string, maxStackSize as int, chancePercent as int, stackDiff as int) {
		this.name = name;
		this.maxStackSize = maxStackSize;
		this.chancePercent = chancePercent;
		this.stackDiff = stackDiff;
		this.minStackSize = 0;
	}
	
	zenConstructor(name as string, maxStackSize as int, chancePercent as int, stackDiff as int, minStackSize as int) {
		this.name = name;
		this.maxStackSize = maxStackSize;
		this.chancePercent = chancePercent;
		this.stackDiff = stackDiff;
		this.minStackSize = minStackSize;
	}
}
