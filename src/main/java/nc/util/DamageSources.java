package nc.util;

import net.minecraft.util.DamageSource;

public class DamageSources {
	
	public static final DamageSource SUPERFLUID_FREEZE = new DamageSource("superfluid_freeze").setDamageBypassesArmor();
	public static final DamageSource PLASMA_BURN = new DamageSource("plasma_burn").setDamageBypassesArmor();
	public static final DamageSource GAS_BURN = new DamageSource("gas_burn").setDamageBypassesArmor();
	public static final DamageSource STEAM_BURN = new DamageSource("steam_burn").setDamageBypassesArmor();
	public static final DamageSource MOLTEN_BURN = new DamageSource("molten_burn");
	public static final DamageSource CORIUM_BURN = new DamageSource("corium_burn");
	public static final DamageSource HOT_COOLANT_BURN = new DamageSource("hot_coolant_burn");
	public static final DamageSource ACID_BURN = new DamageSource("acid_burn");
	public static final DamageSource FLUID_BURN = new DamageSource("fluid_burn");
	public static final DamageSource HYPOTHERMIA = new DamageSource("hypothermia").setDamageBypassesArmor();
	public static final DamageSource FISSION_BURN = new DamageSource("fission_burn");
	public static final DamageSource FATAL_RADS = new DamageSource("fatal_rads").setDamageBypassesArmor().setDamageIsAbsolute();
	
}
