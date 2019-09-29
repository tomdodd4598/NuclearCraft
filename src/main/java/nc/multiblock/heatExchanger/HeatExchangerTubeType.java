package nc.multiblock.heatExchanger;

import nc.config.NCConfig;

public enum HeatExchangerTubeType {
	COPPER("copper", NCConfig.heat_exchanger_conductivity[0]),
	HARD_CARBON("hard_carbon", NCConfig.heat_exchanger_conductivity[1]),
	THERMOCONDUCTING("thermoconducting", NCConfig.heat_exchanger_conductivity[2]);
	
	private String name;
	private double conductivity;
	
	private HeatExchangerTubeType(String name, double conductivity) {
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
