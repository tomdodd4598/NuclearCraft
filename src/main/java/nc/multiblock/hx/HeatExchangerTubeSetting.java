package nc.multiblock.hx;

import net.minecraft.util.IStringSerializable;

public enum HeatExchangerTubeSetting implements IStringSerializable {
	
	DISABLED,
	DEFAULT,
	PRODUCT_OUT,
	INPUT_SPREAD;
	
	public HeatExchangerTubeSetting next() {
        return switch (this) {
            case DISABLED -> DEFAULT;
            case DEFAULT -> PRODUCT_OUT;
            case PRODUCT_OUT -> INPUT_SPREAD;
            case INPUT_SPREAD -> DISABLED;
        };
	}
	
	@Override
	public String getName() {
        return switch (this) {
            case DISABLED -> "disabled";
            case DEFAULT -> "default";
            case PRODUCT_OUT -> "product_out";
            case INPUT_SPREAD -> "input_spread";
        };
	}
}
