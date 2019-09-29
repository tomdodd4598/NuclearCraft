package nc;

import nc.config.NCConfig;
import nc.enumm.IFissionStats;
import nc.enumm.IItemMeta;
import nc.enumm.MetaEnums;
import nc.enumm.MetaEnums.RadShieldingType;
import nc.enumm.MetaEnums.UpgradeType;
import nc.multiblock.turbine.TurbineDynamoCoilType;
import nc.radiation.RadiationHelper;
import nc.recipe.ProcessorRecipe;
import nc.util.CollectionHelper;
import nc.util.InfoHelper;
import nc.util.Lang;
import nc.util.NCMath;
import nc.util.UnitHelper;
import net.minecraft.util.IStringSerializable;

public class NCInfo {
	
	// Coolers
	
	/*public static String[][] coolerInfo() {
		String[][] info = new String[CoolerType.values().length][];
		info[0] = new String[] {};
		for (int i = 1; i < CoolerType.values().length; i++) {
			info[i] = CollectionHelper.concatenate(new String[] {coolingRateString(i)}, InfoHelper.formattedInfo(coolerInfoString(i)));
		}
		return info;
	}
	
	private static String coolingRateString(int meta) {
		return Lang.localise("tile." + Global.MOD_ID + ".cooler.cooling_rate") + " " + NCMath.decimalPlaces(CoolerType.values()[meta].getCooling(), 2) + " H/t";
	}
	
	private static String coolerInfoString(int meta) {
		return Lang.localise("tile." + Global.MOD_ID + ".cooler." + CoolerType.values()[meta].name().toLowerCase() + ".desc");
	}*/
	
	// Fuel Rods
	
	public static <T extends Enum<T> & IStringSerializable & IItemMeta & IFissionStats> String[][] fuelRodInfo(T[] values) {
		String[][] info = new String[values.length][];
		for (int i = 0; i < values.length; i++) {
			info[i] = new String[] {
					Lang.localise("item." + Global.MOD_ID + ".fission_fuel.base_time.desc", UnitHelper.applyTimeUnit(values[i].getBaseTime()*NCConfig.fission_fuel_time_multiplier, 3)),
					Lang.localise("item." + Global.MOD_ID + ".fission_fuel.base_heat.desc", UnitHelper.prefix(values[i].getBaseHeat(), 5, "H/t")),
					Lang.localise("item." + Global.MOD_ID + ".fission_fuel.base_efficiency.desc", Math.round(100D*values[i].getBaseEfficiency()) + "%"),
					Lang.localise("item." + Global.MOD_ID + ".fission_fuel.criticality.desc", values[i].getCriticality() + " N/t")
					};
		}
		return info;
	}
	
	// Fission Moderators
	
	public static String[] fissionModeratorInfo() {
		return InfoHelper.formattedInfo(Lang.localise("info." + Global.MOD_ID + ".moderator.desc", NCConfig.fission_neutron_reach, NCConfig.fission_neutron_reach/2));
	}
	
	public static String[] fissionModeratorFixedInfo(ProcessorRecipe moderatorInfo) {
		return new String[] {
				Lang.localise("info." + Global.MOD_ID + ".moderator.fixd"),
				Lang.localise("info." + Global.MOD_ID + ".moderator.flux_factor.fixd", moderatorInfo.getFissionModeratorFluxFactor() + " N/t"),
				Lang.localise("info." + Global.MOD_ID + ".moderator.efficiency.fixd", Math.round(100D*moderatorInfo.getFissionModeratorEfficiency()) + "%")
				};
	}
	
	// Fission Reflectors
	
	public static String[] fissionReflectorInfo() {
		return InfoHelper.formattedInfo(Lang.localise("info." + Global.MOD_ID + ".reflector.desc"));
	}
	
	public static String[] fissionReflectorFixedInfo(ProcessorRecipe reflectorInfo) {
		return new String[] {
				Lang.localise("info." + Global.MOD_ID + ".reflector.fixd"),
				Lang.localise("info." + Global.MOD_ID + ".reflector.efficiency.fixd", Math.round(100D*reflectorInfo.getFissionReflectorEfficiency()) + "%"),
				Lang.localise("info." + Global.MOD_ID + ".reflector.reflectivity.fixd", Math.round(100D*reflectorInfo.getFissionReflectorReflectivity()) + "%")
				};
	}
	
	// Dynamo Coils
	
	public static String[][] dynamoCoilInfo() {
		String[][] info = new String[TurbineDynamoCoilType.values().length][];
		info[0] = new String[] {};
		for (int i = 0; i < TurbineDynamoCoilType.values().length; i++) {
			info[i] = CollectionHelper.concatenate(new String[] {coilConductivityString(i)}, InfoHelper.formattedInfo(coiInfoString(i)));
		}
		return info;
	}
	
	private static String coilConductivityString(int meta) {
		return Lang.localise("tile." + Global.MOD_ID + ".turbine_dynamo_coil.conductivity") + " " + NCMath.decimalPlaces(100D*TurbineDynamoCoilType.values()[meta].getConductivity(), 1) + "%";
	}
	
	private static String coiInfoString(int meta) {
		return Lang.localise("tile." + Global.MOD_ID + ".turbine_dynamo_coil." + TurbineDynamoCoilType.values()[meta].name().toLowerCase() + ".desc");
	}
	
	// Speed Upgrade
	
	public static final String[] POLY_POWER = new String[] {"linearly", "quadratically", "cubicly", "quarticly", "quinticly", "sexticly", "septicly", "octicly", "nonicly", "decicly", "undecicly", "duodecicly", "tredecicly", "quattuordecicly", "quindecicly"};
	
	public static String[][] upgradeInfo() {
		String[][] info = new String[UpgradeType.values().length][];
		for (int i = 0; i < UpgradeType.values().length; i++) {
			info[i] = InfoHelper.EMPTY_ARRAY;
		}
		info[0] = InfoHelper.formattedInfo(Lang.localise("item.nuclearcraft.upgrade.speed_desc", powerString(NCConfig.speed_upgrade_power_laws[0]), powerString(NCConfig.speed_upgrade_power_laws[1])));
		info[1] = InfoHelper.formattedInfo(Lang.localise("item.nuclearcraft.upgrade.energy_desc", powerString(NCConfig.energy_upgrade_power_laws[0])));
		return info;
	}
	
	private static String powerString(double power) {
		return (power == (int)power ? "" : Lang.localise("info.nuclearcraft.approximately" + " ")) + POLY_POWER[(int)Math.round(power) - 1];
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
	
	// Rad Shielding
	
	public static String[][] radShieldingInfo() {
		String[][] info = new String[RadShieldingType.values().length][];
		for (int i = 0; i < RadShieldingType.values().length; i++) {
			info[i] = InfoHelper.formattedInfo(Lang.localise("item.nuclearcraft.rad_shielding.desc" + (NCConfig.radiation_hardcore_containers > 0D ? "_hardcore" : ""), RadiationHelper.resistanceSigFigs(NCConfig.radiation_shielding_level[i])));
		}
		return info;
	}
}
