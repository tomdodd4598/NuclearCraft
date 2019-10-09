package nc.multiblock.fission;

import net.minecraft.util.IStringSerializable;

public enum FissionReactorType implements IStringSerializable {
	PEBBLE_BED, SOLID_FUEL, MOLTEN_SALT;
	
	@Override
	public String getName() {
		switch (this) {
		case PEBBLE_BED:
			return "pebble_bed";
		case SOLID_FUEL:
			return "solid_fuel";
		case MOLTEN_SALT:
			return "molten_salt";
		default:
			return "solid_fuel";
		}
	}
}
