package nc.multiblock.heatExchanger;

import net.minecraft.util.IStringSerializable;

public enum HeatExchangerTubeSetting implements IStringSerializable {
	
	DISABLED,
	DEFAULT,
	PRODUCT_OUT,
	INPUT_SPREAD;
	
	public HeatExchangerTubeSetting next() {
		switch (this) {
			case DISABLED:
				return DEFAULT;
			case DEFAULT:
				return PRODUCT_OUT;
			case PRODUCT_OUT:
				return INPUT_SPREAD;
			case INPUT_SPREAD:
				return DISABLED;
			default:
				return DISABLED;
		}
	}
	
	@Override
	public String getName() {
		switch (this) {
			case DISABLED:
				return "disabled";
			case DEFAULT:
				return "default";
			case PRODUCT_OUT:
				return "product_out";
			case INPUT_SPREAD:
				return "input_spread";
			default:
				return "disabled";
		}
	}
}
