package nc.tile.internal.fluid;

import net.minecraft.util.IStringSerializable;

public enum FluidConnection implements IStringSerializable {
	IN, OUT, BOTH, NON;
	
	public boolean canFill() {
		return this == IN || this == BOTH;
	}
	
	public boolean canDrain() {
		return this == OUT || this == BOTH;
	}
	
	public boolean canConnect() {
		return this != NON;
	}
	
	public FluidConnection next() {
		switch (this) {
		case IN:
			return OUT;
		case OUT:
			return BOTH;
		case BOTH:
			return NON;
		case NON:
			return IN;
		default:
			return NON;
		}
	}
	
	public FluidConnection nextAlt() {
		switch (this) {
		case IN:
			return OUT;
		case OUT:
			return NON;
		case BOTH:
			return IN;
		case NON:
			return BOTH;
		default:
			return NON;
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
}
