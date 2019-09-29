package nc.multiblock.fission.solid;

import net.minecraft.util.IStringSerializable;

public enum SolidFissionCellSetting implements IStringSerializable {
	DEFAULT, DISABLED, DEPLETED_OUT, FUEL_SPREAD;
	
	public SolidFissionCellSetting next() {
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
