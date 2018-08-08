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
		return Lang.localise("tile." + Global.MOD_ID + ".cooler.cooling_rate") + " " + CoolerType.values()[meta].getCooling() + " H/t";
	}
	
	private static String coolerInfoString(int meta) {
		return Lang.localise("tile." + Global.MOD_ID + ".cooler." + CoolerType.values()[meta].name().toLowerCase() + ".desc");
	}
	
	// Fuel Rods
	
	public static <T extends Enum<T> & IStringSerializable & IItemMeta & IFissionStats> String[][] fuelRodInfo(T[] values) {
		String[][] info = new String[values.length][];
		for (int i = 0; i < values.length; i++) {
			info[i] = new String[] {Lang.localise("item." + Global.MOD_ID + ".fuel_rod.base_time.desc", NCMathHelper.round(values[i].getBaseTime()/(1200D*NCConfig.fission_fuel_use))), Lang.localise("item." + Global.MOD_ID + ".fuel_rod.base_power.desc", values[i].getBasePower()*NCConfig.fission_power), Lang.localise("item." + Global.MOD_ID + ".fuel_rod.base_heat.desc", values[i].getBaseHeat()*NCConfig.fission_heat_generation)};
		}
		
		return info;
	}
	
	// Ingot Blocks
	
	public static String moderatorPowerInfo = Lang.localise("info.moderator.power", NCMathHelper.round(6D/NCConfig.fission_moderator_extra_power, 2));
	public static String moderatorHeatInfo = Lang.localise("info.moderator.heat", NCMathHelper.round(6D/NCConfig.fission_moderator_extra_heat, 2));
	
	public static String[][] ingotBlockInfo() {
		String[][] info = new String[IngotType.values().length][];
		for (int i = 0; i < IngotType.values().length; i++) {
			info[i] = InfoHelper.EMPTY_ARRAY;
		}
		info[8] = InfoHelper.formattedInfo(Lang.localise(ingotBlockInfoString(8), moderatorPowerInfo, moderatorHeatInfo));
		info[9] = InfoHelper.formattedInfo(Lang.localise(ingotBlockInfoString(9), moderatorPowerInfo, moderatorHeatInfo));
		return info;
	}
	
	private static String ingotBlockInfoString(int meta) {
		return "tile." + Global.MOD_ID + ".ingot_block." + IngotType.values()[meta].name().toLowerCase() + ".desc";
	}
	
	public static String[][] ingotBlockFixedInfo() {
		String[][] info = new String[IngotType.values().length][];
		for (int i = 0; i < IngotType.values().length; i++) {
			info[i] = InfoHelper.EMPTY_ARRAY;
		}
		info[8] = new String[] {Lang.localise("info.moderator.desc")};
		info[9] = new String[] {Lang.localise("info.moderator.desc")};
		return info;
	}
}
