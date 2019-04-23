package nc.multiblock.saltFission;

import net.minecraft.util.IStringSerializable;

public enum SaltFissionVesselSetting implements IStringSerializable {
	DEFAULT, DISABLED, DEPLETED_OUT, FUEL_SPREAD;
	
	public SaltFissionVesselSetting next() {
		switch (this) {
		case DISABLED:
			return DEFAULT;
		case DEFAULT:
			return DEPLETED_OUT;
		case DEPLETED_OUT:
			return FUEL_SPREAD;
		case FUEL_SPREAD:
			return DISABLED;
		default:
			return DISABLED;
		}
	}
	
	@Override
	public String getName() {
		switch (this) {
		case DEFAULT:
			return "default";
		case DISABLED:
			return "disabled";
		case DEPLETED_OUT:
			return "depleted_out";
		case FUEL_SPREAD:
			return "fuel_spread";
		default:
			return "disabled";
		}
	}
}
