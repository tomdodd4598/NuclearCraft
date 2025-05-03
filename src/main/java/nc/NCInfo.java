package nc;

import com.google.common.collect.Lists;
import nc.enumm.*;
import nc.multiblock.turbine.TurbineDynamoCoilType;
import nc.radiation.RadiationHelper;
import nc.recipe.BasicRecipe;
import nc.util.*;
import net.minecraft.util.IStringSerializable;

import java.util.*;

import static nc.config.NCConfig.*;

public class NCInfo {
	
	// Diaphragms
	
	public static String[] machineDiaphragmFixedInfo(BasicRecipe recipe) {
		return new String[] {Lang.localize("info." + Global.MOD_ID + ".diaphragm.fixd"), Lang.localize("info." + Global.MOD_ID + ".diaphragm.efficiency.fixd", NCMath.pcDecimalPlaces(recipe.getMachineDiaphragmEfficiency(), 1)), Lang.localize("info." + Global.MOD_ID + ".diaphragm.contact.fixd", NCMath.pcDecimalPlaces(recipe.getMachineDiaphragmContactFactor(), 1))};
	}
	
	public static String[] machineDiaphragmInfo() {
		return InfoHelper.formattedInfo(Lang.localize("info." + Global.MOD_ID + ".diaphragm.desc"));
	}
	
	// Sieve Assembly
	
	public static String[] machineSieveAssemblyFixedInfo(BasicRecipe recipe) {
		return new String[] {Lang.localize("info." + Global.MOD_ID + ".sieve_assembly.fixd"), Lang.localize("info." + Global.MOD_ID + ".sieve_assembly.efficiency.fixd", NCMath.pcDecimalPlaces(recipe.getMachineSieveAssemblyEfficiency(), 1))};
	}
	
	public static String[] machineSieveAssemblyInfo() {
		return InfoHelper.formattedInfo(Lang.localize("info." + Global.MOD_ID + ".sieve_assembly.desc"));
	}
	
	// Electrolyzer Electrodes
	
	public static String[] electrodeFixedInfo(BasicRecipe cathodeRecipe, BasicRecipe anodeRecipe) {
		boolean anyElectrode = cathodeRecipe != null && anodeRecipe != null;
		List<String> list = Lists.newArrayList(Lang.localize("info." + Global.MOD_ID + ".electrode." + (anyElectrode ? "fixd" : cathodeRecipe != null ? "cathode.fixd" : "anode.fixd")));
		if (cathodeRecipe != null) {
			list.add(Lang.localize("info." + Global.MOD_ID + ".electrode.efficiency." + (anyElectrode ? "cathode.fixd" : "fixd"), NCMath.pcDecimalPlaces(cathodeRecipe.getElectrolyzerElectrodeEfficiency(), 1)));
		}
		if (anodeRecipe != null) {
			list.add(Lang.localize("info." + Global.MOD_ID + ".electrode.efficiency." + (anyElectrode ? "anode.fixd" : "fixd"), NCMath.pcDecimalPlaces(anodeRecipe.getElectrolyzerElectrodeEfficiency(), 1)));
		}
		return list.toArray(new String[0]);
	}
	
	public static String[] electrodeInfo() {
		return InfoHelper.formattedInfo(Lang.localize("info." + Global.MOD_ID + ".electrode.desc"));
	}
	
	// Infiltrator Pressure Fluids
	
	public static String[] infiltratorPressureFluidFixedInfo(BasicRecipe recipe) {
		return new String[] {Lang.localize("info." + Global.MOD_ID + ".infiltrator_pressure_fluid.fixd"), Lang.localize("info." + Global.MOD_ID + ".infiltrator_pressure_fluid.efficiency.fixd", NCMath.pcDecimalPlaces(recipe.getInfiltratorPressureFluidEfficiency(), 1))};
	}
	
	public static String[] infiltratorPressureFluidInfo() {
		return InfoHelper.formattedInfo(Lang.localize("info." + Global.MOD_ID + ".infiltrator_pressure_fluid.desc"));
	}
	
	// Fission Fuel
	
	public static String[] fissionFuelInfo(BasicRecipe recipe) {
		List<String> list = Lists.newArrayList(Lang.localize("info." + Global.MOD_ID + ".fission_fuel.desc"), Lang.localize("info." + Global.MOD_ID + ".fission_fuel.base_time.desc", UnitHelper.applyTimeUnit(recipe.getFissionFuelTime(), 3)), Lang.localize("info." + Global.MOD_ID + ".fission_fuel.base_heat.desc", UnitHelper.prefix(recipe.getFissionFuelHeat(), 5, "H/t")), Lang.localize("info." + Global.MOD_ID + ".fission_fuel.base_efficiency.desc", NCMath.pcDecimalPlaces(recipe.getFissionFuelEfficiency(), 1)), Lang.localize("info." + Global.MOD_ID + ".fission_fuel.criticality.desc", recipe.getFissionFuelCriticality() + " N/t"));
		if (fission_decay_mechanics) {
			list.add(Lang.localize("info." + Global.MOD_ID + ".fission_fuel.decay_factor.desc", NCMath.pcDecimalPlaces(recipe.getFissionFuelDecayFactor(), 1)));
		}
		if (recipe.getFissionFuelSelfPriming()) {
			list.add(Lang.localize("info." + Global.MOD_ID + ".fission_fuel.self_priming.desc"));
		}
		return list.toArray(new String[0]);
	}
	
	// Fission Cooling
	
	public static <T extends Enum<T> & IStringSerializable & ICoolingComponentEnum<?>> String[][] coolingFixedInfo(T[] values, String name) {
		String[][] info = new String[values.length][];
		for (int i = 0; i < values.length; ++i) {
			info[i] = coolingRateInfo(values[i], name);
		}
		return info;
	}
	
	public static <T extends Enum<T> & ICoolingComponentEnum<?>> String[] coolingRateInfo(T type, String name) {
		return coolingRateInfo(type.getCooling(), name);
	}
	
	public static String[] coolingRateInfo(int cooling, String name) {
		return new String[] {Lang.localize("tile." + Global.MOD_ID + "." + name + ".cooling_rate") + " " + cooling + " H/t"};
	}
	
	public static String[][] heatSinkFixedInfo() {
		return coolingFixedInfo(MetaEnums.HeatSinkType.values(), "solid_fission_sink");
	}
	
	public static String[][] heatSinkFixedInfo2() {
		return coolingFixedInfo(MetaEnums.HeatSinkType2.values(), "solid_fission_sink");
	}
	
	public static String[][] coolantHeaterFixedInfo() {
		return coolingFixedInfo(MetaEnums.CoolantHeaterType.values(), "salt_fission_heater");
	}
	
	public static String[][] coolantHeaterFixedInfo2() {
		return coolingFixedInfo(MetaEnums.CoolantHeaterType2.values(), "salt_fission_heater");
	}
	
	// Fission Neutron Sources
	
	public static String[][] neutronSourceFixedInfo() {
		MetaEnums.NeutronSourceType[] values = MetaEnums.NeutronSourceType.values();
		String[][] info = new String[values.length][];
		for (int i = 0; i < values.length; ++i) {
			info[i] = neutronSourceEfficiencyInfo(values[i].getEfficiency());
		}
		return info;
	}
	
	public static String[] neutronSourceEfficiencyInfo(double efficiency) {
		return new String[] {Lang.localize("info." + Global.MOD_ID + ".fission_source.efficiency.fixd", NCMath.pcDecimalPlaces(efficiency, 1))};
	}
	
	public static String[][] neutronSourceInfo() {
		MetaEnums.NeutronSourceType[] values = MetaEnums.NeutronSourceType.values();
		String[][] info = new String[values.length][];
		for (int i = 0; i < values.length; ++i) {
			info[i] = neutronSourceDescriptionInfo();
		}
		return info;
	}
	
	public static String[] neutronSourceDescriptionInfo() {
		return InfoHelper.formattedInfo(Lang.localize("tile." + Global.MOD_ID + ".fission_source.desc"));
	}
	
	// Fission Neutron Shields
	
	public static String[][] neutronShieldFixedInfo() {
		MetaEnums.NeutronShieldType[] values = MetaEnums.NeutronShieldType.values();
		String[][] info = new String[values.length][];
		for (int i = 0; i < values.length; ++i) {
			info[i] = neutronShieldStatInfo(values[i].getHeatPerFlux(), values[i].getEfficiency());
		}
		return info;
	}
	
	public static String[] neutronShieldStatInfo(double heatPerFlux, double efficiency) {
		return new String[] {Lang.localize("info." + Global.MOD_ID + ".fission_shield.heat_per_flux.fixd", UnitHelper.prefix(heatPerFlux, 5, "H/N")), Lang.localize("info." + Global.MOD_ID + ".fission_shield.efficiency.fixd", NCMath.pcDecimalPlaces(efficiency, 1))};
	}
	
	public static String[][] neutronShieldInfo() {
		MetaEnums.NeutronShieldType[] values = MetaEnums.NeutronShieldType.values();
		String[][] info = new String[values.length][];
		for (int i = 0; i < values.length; ++i) {
			info[i] = neutronShieldDescriptionInfo();
		}
		return info;
	}
	
	public static String[] neutronShieldDescriptionInfo() {
		return InfoHelper.formattedInfo(Lang.localize("tile." + Global.MOD_ID + ".fission_shield.desc"));
	}
	
	// Fission Moderators
	
	public static String[] fissionModeratorFixedInfo(BasicRecipe moderatorInfo) {
		return new String[] {Lang.localize("info." + Global.MOD_ID + ".moderator.fixd"), Lang.localize("info." + Global.MOD_ID + ".moderator.flux_factor.fixd", moderatorInfo.getFissionModeratorFluxFactor() + " N/t"), Lang.localize("info." + Global.MOD_ID + ".moderator.efficiency.fixd", NCMath.pcDecimalPlaces(moderatorInfo.getFissionModeratorEfficiency(), 1))};
	}
	
	public static String[] fissionModeratorInfo() {
		return InfoHelper.formattedInfo(Lang.localize("info." + Global.MOD_ID + ".moderator.desc", fission_neutron_reach, fission_neutron_reach / 2));
	}
	
	// Fission Reflectors
	
	public static String[] fissionReflectorFixedInfo(BasicRecipe reflectorInfo) {
		return new String[] {Lang.localize("info." + Global.MOD_ID + ".reflector.fixd"), Lang.localize("info." + Global.MOD_ID + ".reflector.reflectivity.fixd", NCMath.pcDecimalPlaces(reflectorInfo.getFissionReflectorReflectivity(), 1)), Lang.localize("info." + Global.MOD_ID + ".reflector.efficiency.fixd", NCMath.pcDecimalPlaces(reflectorInfo.getFissionReflectorEfficiency(), 1))};
	}
	
	public static String[] fissionReflectorInfo() {
		return InfoHelper.formattedInfo(Lang.localize("info." + Global.MOD_ID + ".reflector.desc"));
	}
	
	// Dynamo Coils
	
	public static String[][] dynamoCoilFixedInfo() {
		String[][] info = new String[TurbineDynamoCoilType.values().length][];
		for (int i = 0; i < TurbineDynamoCoilType.values().length; ++i) {
			info[i] = coilConductivityInfo(i);
		}
		return info;
	}
	
	public static String[] coilConductivityInfo(int meta) {
		return coilConductivityInfo(TurbineDynamoCoilType.values()[meta].getConductivity());
	}
	
	public static String[] coilConductivityInfo(double conductivity) {
		return new String[] {Lang.localize("tile." + Global.MOD_ID + ".turbine_dynamo_coil.conductivity") + " " + NCMath.pcDecimalPlaces(conductivity, 1)};
	}
	
	// Speed Upgrade
	
	public static String[][] upgradeInfo() {
		String[][] info = new String[MetaEnums.UpgradeType.values().length][];
		for (int i = 0; i < MetaEnums.UpgradeType.values().length; ++i) {
			info[i] = InfoHelper.EMPTY_ARRAY;
		}
		info[0] = InfoHelper.formattedInfo(Lang.localize("item.nuclearcraft.upgrade.speed_desc", powerAdverb(speed_upgrade_power_laws_fp[0], "increase", "with"), powerAdverb(speed_upgrade_power_laws_fp[1], "increase", "")));
		info[1] = InfoHelper.formattedInfo(Lang.localize("item.nuclearcraft.upgrade.energy_desc", powerAdverb(energy_upgrade_power_laws_fp[0], "decrease", "with")));
		return info;
	}
	
	public static String powerAdverb(double power, String verb, String preposition) {
		if (power != (long) power) {
			verb += "_approximately";
		}
		verb = Lang.localize("nc.sf." + verb);
		
		long p = Math.round(power);
		
		preposition = "nc.sf." + preposition;
		return Lang.canLocalize(preposition) ? Lang.localize("nc.sf.power_adverb_preposition", Lang.localize("nc.sf.power_adverb" + p, verb), Lang.localize(preposition)) : Lang.localize("nc.sf.power_adverb" + p, verb);
	}
	
	// Rad Shielding
	
	public static String[][] radShieldingInfo() {
		String[][] info = new String[MetaEnums.RadShieldingType.values().length][];
		for (int i = 0; i < MetaEnums.RadShieldingType.values().length; ++i) {
			info[i] = InfoHelper.formattedInfo(Lang.localize("item.nuclearcraft.rad_shielding.desc" + (radiation_hardcore_containers > 0D ? "_hardcore" : ""), RadiationHelper.resistanceSigFigs(radiation_shielding_level[i])));
		}
		return info;
	}
}
