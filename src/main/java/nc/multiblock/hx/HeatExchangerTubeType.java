package nc.multiblock.hx;

import static nc.config.NCConfig.heat_exchanger_conductivity;

public enum HeatExchangerTubeType {
	
	COPPER("copper", heat_exchanger_conductivity[0]),
	HARD_CARBON("hard_carbon", heat_exchanger_conductivity[1]),
	THERMOCONDUCTING("thermoconducting", heat_exchanger_conductivity[2]);
	
	private final String name;
	private final double conductivity;
	
	HeatExchangerTubeType(String name, double conductivity) {
		this.name = name;
		this.conductivity = conductivity;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	public double getConductivity() {
		return conductivity;
	}
}
