package nc.block.property;

import net.minecraft.block.properties.PropertyBool;
import net.minecraft.util.EnumFacing;

public class PropertySidedBool extends PropertyBool {
	
	public EnumFacing facing;
	
	public PropertySidedBool(String name, EnumFacing facing) {
		super(name);
		this.facing = facing;
	}
	
	public static PropertySidedBool create(String name, EnumFacing facing) {
		return new PropertySidedBool(name, facing);
	}
}
