package nc;

import nc.config.NCConfig;
import nc.enumm.IFissionStats;
import nc.enumm.IItemMeta;
import nc.enumm.MetaEnums;
import nc.enumm.MetaEnums.CoolerType;
import nc.enumm.MetaEnums.IngotType;
import nc.enumm.MetaEnums.UpgradeType;
import nc.util.ArrayHelper;
import nc.util.InfoHelper;
import nc.util.Lang;
import nc.util.NCMath;
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
			info[i] = new String[] {Lang.localise("item." + Global.MOD_ID + ".fuel_rod.base_time.desc", NCMath.round(values[i].getBaseTime()/(1200D*NCConfig.fission_fuel_use))), Lang.localise("item." + Global.MOD_ID + ".fuel_rod.base_power.desc", values[i].getBasePower()*NCConfig.fission_power), Lang.localise("item." + Global.MOD_ID + ".fuel_rod.base_heat.desc", values[i].getBaseHeat()*NCConfig.fission_heat_generation)};
		}
		
		return info;
	}
	
	// Ingot Blocks
	
	public static String moderatorPowerInfo = Lang.localise("info.moderator.power", NCMath.round(6D/NCConfig.fission_moderator_extra_power, 2));
	public static String moderatorHeatInfo = Lang.localise("info.moderator.heat", NCMath.round(6D/NCConfig.fission_moderator_extra_heat, 2));
	
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
	
	// Speed Upgrade
	
	public static final String[] POLY_POWER = new String[] {"linearly", "quadratically", "cubicly", "quarticly", "quinticly", "sexticly", "septicly", "octicly", "nonicly", "decicly", "undecicly", "duodecicly", "tredecicly", "quattuordecicly", "quindecicly"};
	
	public static String[][] upgradeInfo() {
		String[][] info = new String[UpgradeType.values().length][];
		for (int i = 0; i < UpgradeType.values().length; i++) {
			info[i] = InfoHelper.EMPTY_ARRAY;
		}
		info[0] = InfoHelper.formattedInfo(Lang.localise("item.nuclearcraft.upgrade.speed_desc", POLY_POWER[NCConfig.speed_upgrade_power_laws[0] - 1], POLY_POWER[NCConfig.speed_upgrade_power_laws[1] - 1]));
		return info;
	}
	
	// Extra Ore Drops
	
	public static <T extends Enum<T> & IStringSerializable & IItemMeta> String[][] oreDropInfo(String type, T[] values, int[] configIds, int[] metas) {
		String[][] info = new String[values.length][];
		for (int i = 0; i < values.length; i++) {
			info[i] = null;
		}
		for (int i = 0; i < configIds.length; i++) {
			String unloc = "item." + Global.MOD_ID + "." + type + "." + values[metas[i]].getName() + ".desc";
			if (Lang.canLocalise(unloc) && NCConfig.ore_drops[configIds[i]]) info[metas[i]] = InfoHelper.formattedInfo(Lang.localise(unloc));
		}
		
		return info;
	}
	
	public static <T extends Enum<T> & IStringSerializable & IItemMeta> String[][] dustOreDropInfo() {
		return oreDropInfo("dust", MetaEnums.DustType.values(), new int[] {1, 2}, new int[] {9, 10});
	}
	
	public static <T extends Enum<T> & IStringSerializable & IItemMeta> String[][] gemOreDropInfo() {
		return oreDropInfo("gem", MetaEnums.GemType.values(), new int[] {0, 3, 5, 6}, new int[] {0, 2, 3, 4});
	}
	
	public static <T extends Enum<T> & IStringSerializable & IItemMeta> String[][] gemDustOreDropInfo() {
		return oreDropInfo("gem_dust", MetaEnums.GemDustType.values(), new int[] {4}, new int[] {6});
	}
}
