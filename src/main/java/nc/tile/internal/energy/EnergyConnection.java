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
        return switch (type) {
            case DEFAULT -> switch (this) {
                case IN -> OUT;
                case OUT -> NON;
                case NON -> IN;
                default -> NON;
            };
        };
	}
	
	@Override
	public String getName() {
        return switch (this) {
            case IN -> "in";
            case OUT -> "out";
            case BOTH -> "both";
            case NON -> "non";
        };
	}
	
	public enum Type {
		DEFAULT
    }
}
