package nc.multiblock.fission.salt;

import net.minecraft.util.IStringSerializable;

public enum SaltFissionHeaterSetting implements IStringSerializable {
	DEFAULT,
	DISABLED,
	HOT_COOLANT_OUT,
	COOLANT_SPREAD;
	
	public SaltFissionHeaterSetting next() {
		switch (this) {
			case DISABLED:
				return DEFAULT;
			case DEFAULT:
				return HOT_COOLANT_OUT;
			case HOT_COOLANT_OUT:
				return COOLANT_SPREAD;
			case COOLANT_SPREAD:
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
			case HOT_COOLANT_OUT:
				return "hot_coolant_out";
			case COOLANT_SPREAD:
				return "coolant_spread";
			default:
				return "disabled";
		}
	}
}
