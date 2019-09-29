package nc.util;

import net.minecraftforge.fluids.FluidRegistry;

public class FluidRegHelper {
	
	public static boolean fluidExists(String name) {
		return FluidRegistry.getRegisteredFluids().keySet().contains(name.toLowerCase());
	}
	
	public static boolean fluidsExist(String... names) {
		for (String name : names) {
			if (!fluidExists(name)) return false;
		}
		return true;
	}
}
