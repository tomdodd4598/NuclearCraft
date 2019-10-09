package nc.multiblock.heatExchanger;

import net.minecraft.util.IStringSerializable;

public enum HeatExchangerType implements IStringSerializable {
	HEAT_EXCHANGER, CONDENSER;
	
	@Override
	public String getName() {
		switch (this) {
		case HEAT_EXCHANGER:
			return "heat_exchanger";
		case CONDENSER:
			return "condenser";
		default:
			return "heat_exchanger";
		}
	}
}
