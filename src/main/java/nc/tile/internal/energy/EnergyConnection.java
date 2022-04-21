package nc.tile.internal.energy;

import net.minecraft.util.IStringSerializable;

public enum EnergyConnection implements IStringSerializable {
	
	IN,
	OUT,
	BOTH,
	NON;
	
	public boolean canReceive() {
		return this == IN || this == BOTH;
	}
	
	public boolean canExtract() {
		return this == OUT || this == BOTH;
	}
	
	public boolean canConnect() {
		return this != NON;
	}
	
	public EnergyConnection next(Type type) {
		switch (type) {
			case DEFAULT:
				switch (this) {
					case IN:
						return OUT;
					case OUT:
						return NON;
					case NON:
						return IN;
					default:
						return NON;
				}
			default:
				switch (this) {
					case IN:
						return OUT;
					case OUT:
						return NON;
					case NON:
						return IN;
					default:
						return NON;
				}
		}
	}
	
	@Override
	public String getName() {
		switch (this) {
			case IN:
				return "in";
			case OUT:
				return "out";
			case BOTH:
				return "both";
			case NON:
				return "non";
			default:
				return "non";
		}
	}
	
	public static enum Type {
		DEFAULT;
	}
}
