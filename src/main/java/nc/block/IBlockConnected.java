package nc.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public interface IBlockConnected {
	
	public default boolean equalAdjacent(IBlockAccess world, BlockPos pos, EnumFacing side) {
		if (!connectedTexturesEnabled()) return false;
		IBlockState state = world.getBlockState(pos);
		IBlockState other = world.getBlockState(pos.offset(side));
		if (state != null && other != null) {
			if (state.getBlock() == other.getBlock()) return true;
		}
		return false;
	}
	
	public boolean connectedTexturesEnabled();
}
