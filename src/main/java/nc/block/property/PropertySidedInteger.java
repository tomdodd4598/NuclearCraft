package nc.block.property;

import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.util.EnumFacing;

public class PropertySidedInteger extends PropertyInteger {
	
	public EnumFacing facing;
	
	public PropertySidedInteger(String name, int min, int max, EnumFacing facing) {
		super(name, min, max);
		this.facing = facing;
	}
	
	public static PropertySidedInteger create(String name, int min, int max, EnumFacing facing) {
		return new PropertySidedInteger(name, min, max, facing);
	}
}
