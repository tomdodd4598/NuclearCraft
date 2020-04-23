package nc.tile.dummy;

import nc.tile.ITile;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IDummyMaster extends ITile {
	
	public default void onDummyNeighborChanged(IBlockState state, World world, BlockPos pos, BlockPos fromPos) {
		refreshIsRedstonePowered(world, pos);
	}
	
	@Override
	public default boolean checkIsRedstonePowered(World world, BlockPos pos) {
		return ITile.super.checkIsRedstonePowered(world, pos) || world.isBlockPowered(getTilePos());
	}
}
