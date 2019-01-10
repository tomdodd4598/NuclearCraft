package nc.multiblock.turbine;

import nc.config.NCConfig;
import net.minecraft.util.IStringSerializable;

public enum TurbineRotorBladeType implements IStringSerializable {
	STEEL("steel", 0, NCConfig.turbine_blade_efficiency[0], NCConfig.turbine_blade_expansion[0]),
	EXTREME("extreme", 1, NCConfig.turbine_blade_efficiency[1], NCConfig.turbine_blade_expansion[1]),
	SIC_SIC_CMC("sic_sic_cmc", 2, NCConfig.turbine_blade_efficiency[2], NCConfig.turbine_blade_expansion[2]);
	
	private String name;
	private int id;
	private double efficiency;
	private double expansion;
	
	private TurbineRotorBladeType(String name, int id, double efficiency, double expansion) {
		this.name = name;
		this.id = id;
		this.efficiency = efficiency;
		this.expansion = expansion;
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return getName();
	}
	
	public double getEfficiency() {
		return efficiency;
	}
	
	public double getExpansionCoefficient() {
		return expansion;
	}
}
