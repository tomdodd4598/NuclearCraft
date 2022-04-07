package nc.util;

import java.util.Locale;

import net.minecraftforge.fluids.FluidRegistry;

public class FluidRegHelper {
	
	public static boolean fluidExists(String name) {
		return FluidRegistry.getRegisteredFluids().containsKey(name.toLowerCase(Locale.ROOT));
	}
	
	public static boolean fluidsExist(String... names) {
		for (String name : names) {
			if (!fluidExists(name)) {
				return false;
			}
		}
		return true;
	}
}
