package nc;

import nc.config.NCConfig;
import nc.enumm.IFissionStats;
import nc.enumm.IItemMeta;
import nc.enumm.MetaEnums.CoolerType;
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
		return Lang.localise("tile.cooler.cooling_rate") + " " + NCConfig.fission_cooling_rate[meta - 1] + " H/t";
	}
	
	private static String coolerInfoString(int meta) {
		return "tile.cooler." + CoolerType.values()[meta].name().toLowerCase() + ".desc";
	}
	
	// Fuel Rods
	
	public static <T extends Enum<T> & IStringSerializable & IItemMeta & IFissionStats> String[][] fuelRodInfo(T[] values) {
		String[][] info = new String[values.length][];
		for (int i = 0; i < values.length; i++) {
			info[i] = new String[] {Lang.localise("item.fuel_rod.base_time.desc", NCMathHelper.Round(values[i].getBaseTime()/1200D)), Lang.localise("item.fuel_rod.base_power.desc", values[i].getBasePower()), Lang.localise("item.fuel_rod.base_heat.desc", values[i].getBaseHeat())};
		}
		
		return info;
	}
}
