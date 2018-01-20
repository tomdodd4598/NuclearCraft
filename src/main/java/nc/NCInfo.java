package nc;

import nc.config.NCConfig;
import nc.enumm.IFissionStats;
import nc.enumm.IItemMeta;
import nc.enumm.MetaEnums.CoolerType;
import nc.enumm.MetaEnums.IngotType;
import nc.util.ArrayHelper;
import nc.util.InfoHelper;
import nc.util.Lang;
import nc.util.NCMathHelper;
import net.minecraft.util.IStringSerializable;

public class NCInfo {
	
	// Coolers
	
	public static String[][] coolerInfo() {
		String[][] info = new String[CoolerType.values().length][];
		info[0] = new String[] {};
		for (int i = 1; i < CoolerType.values().length; i++) {
			info[i] = ArrayHelper.concatenate(new String[] {coolingRateString(i)}, InfoHelper.formattedInfo(coolerInfoString(i)));
		}
		return info;
	}
	
	private static String coolingRateString(int meta) {
		return Lang.localise("tile.cooler.cooling_rate") + " " + CoolerType.values()[meta].getCooling() + " H/t";
	}
	
	private static String coolerInfoString(int meta) {
		String defaultInfo = "tile.cooler." + CoolerType.values()[meta].name().toLowerCase() + ".desc";
		if (Lang.canLocalise(defaultInfo)) return Lang.localise(defaultInfo);
		return "tile.cooler." + CoolerType.values()[meta].name().toLowerCase() + (NCConfig.fission_new_mechanics ? ".des1" : ".des0");
	}
	
	// Fuel Rods
	
	public static <T extends Enum<T> & IStringSerializable & IItemMeta & IFissionStats> String[][] fuelRodInfo(T[] values) {
		String[][] info = new String[values.length][];
		for (int i = 0; i < values.length; i++) {
			info[i] = new String[] {Lang.localise("item.fuel_rod.base_time.desc", NCMathHelper.round(values[i].getBaseTime()/1200D)), Lang.localise("item.fuel_rod.base_power.desc", values[i].getBasePower()), Lang.localise("item.fuel_rod.base_heat.desc", values[i].getBaseHeat())};
		}
		
		return info;
	}
	
	// Ingot Blocks
	
	public static String[][] ingotBlockInfo() {
		String[][] info = new String[IngotType.values().length][];
		for (int i = 0; i < IngotType.values().length; i++) {
			info[i] = new String[] {};
		}
		info[8] = InfoHelper.formattedInfo(Lang.localise(ingotBlockInfoString(8), powerInfo, heatInfo));
		return info;
	}
	
	private static String ingotBlockInfoString(int meta) {
		return "tile.ingot_block." + IngotType.values()[meta].name().toLowerCase() + ".desc";
	}
	
	public static String powerInfo = NCConfig.fission_new_mechanics ? Lang.localise("tile.ingot_block.graphite.des0", NCMathHelper.round(6D/NCConfig.fission_graphite_extra_power, 2)) : Lang.localise("tile.ingot_block.graphite.des1");
	public static String heatInfo = NCConfig.fission_new_mechanics ? Lang.localise("tile.ingot_block.graphite.des2", NCMathHelper.round(6D/NCConfig.fission_graphite_extra_heat, 2)) : Lang.localise("tile.ingot_block.graphite.des3");
}
