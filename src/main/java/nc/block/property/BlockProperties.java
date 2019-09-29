package nc.block.property;

import net.minecraft.block.BlockDirectional;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.util.EnumFacing;

public class BlockProperties {
	
	public static final PropertyDirection FACING_HORIZONTAL = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	public static final PropertyDirection FACING_ALL = BlockDirectional.FACING;
	public static final PropertyBool ACTIVE = PropertyBool.create("active");
	public static final PropertyBool FRAME = PropertyBool.create("frame");
}
