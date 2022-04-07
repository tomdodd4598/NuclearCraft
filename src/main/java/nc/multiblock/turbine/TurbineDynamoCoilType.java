package nc.multiblock.turbine;

import static nc.config.NCConfig.turbine_coil_conductivity;

import net.minecraft.util.IStringSerializable;

public enum TurbineDynamoCoilType implements IStringSerializable {
	
	MAGNESIUM("magnesium", 0, turbine_coil_conductivity[0]),
	BERYLLIUM("beryllium", 1, turbine_coil_conductivity[1]),
	ALUMINUM("aluminum", 2, turbine_coil_conductivity[2]),
	GOLD("gold", 3, turbine_coil_conductivity[3]),
	COPPER("copper", 4, turbine_coil_conductivity[4]),
	SILVER("silver", 5, turbine_coil_conductivity[5]);
	
	private String name;
	private int id;
	private double conductivity;
	
	private TurbineDynamoCoilType(String name, int id, double conductivity) {
		this.name = name;
		this.id = id;
		this.conductivity = conductivity;
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return getName();
	}
	
	public int getID() {
		return id;
	}
	
	public double getConductivity() {
		return conductivity;
	}
}
