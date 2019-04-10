package nc.block.property;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public interface ISidedProperty<T extends Enum<T> & IStringSerializable> {
	
	public T getProperty(IBlockAccess world, BlockPos pos, EnumFacing facing);
}
