package nc.util;

import nc.recipe.StackType;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.oredict.OreDictionary;

public class OreStackHelper {
	
	public static boolean exists(String ore, StackType type) {
		if (!type.isFluid()) if (OreDictionary.getOres(ore).size() > 0) return true;
		if (!type.isItem()) if (FluidRegistry.getRegisteredFluids().keySet().contains(ore.toLowerCase())) return true;
		return false;
	}
	
	public static final String[] INGOT_VOLUME_TYPES = new String[] {"ingot", "dust"};
	public static final String[] NUGGET_VOLUME_TYPES = new String[] {"nugget", "tinyDust"};
	
	public static final String[] DUST_VOLUME_TYPES = new String[] {"dust"};
	public static final String[] TINYDUST_VOLUME_TYPES = new String[] {"tinyDust"};
	
	public static final String[] FUEL_VOLUME_TYPES = new String[] {"fuel", "dust"};
	
	public static final String[] BLOCK_VOLUME_TYPES = new String[] {"block"};
}
