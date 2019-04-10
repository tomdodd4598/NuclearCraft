package nc.tile;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import nc.capability.radiation.source.IRadiationSource;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface ITile {
	
	public World getTileWorld();
	
	public BlockPos getTilePos();
	
	public void markTileDirty();
	
	public Block getTileBlockType();
	
	public IRadiationSource getRadiationSource();
	
	public default boolean shouldSaveRadiation() {
		return true;
	}
	
	public void setState(boolean isActive);
	
	public default void onBlockNeighborChanged(IBlockState state, World world, BlockPos pos, BlockPos fromPos) {
		refreshIsRedstonePowered(world, pos);
	}
	
	// Redstone
	
	public default boolean checkIsRedstonePowered(World world, BlockPos pos) {
		return world.isBlockPowered(pos);
	}
	
	public default void refreshIsRedstonePowered(World world, BlockPos pos) {
		setIsRedstonePowered(checkIsRedstonePowered(world, pos));
	}
	
	public boolean getIsRedstonePowered();
	
	public void setIsRedstonePowered(boolean isRedstonePowered);
	
	public boolean getAlternateComparator();
	
	public void setAlternateComparator(boolean alternate);
	
	public boolean getRedstoneControl();
	
	public void setRedstoneControl(boolean redstoneControl);
	
	// State Updating
	
	public default void markAndRefresh() {
		markAndRefresh(getTilePos(), getTileWorld().getBlockState(getTilePos()));
	}
	
	public default void markAndRefresh(IBlockState newState) {
		markAndRefresh(getTilePos(), newState);
	}
	
	public default void markAndRefresh(BlockPos pos, IBlockState newState) {
		markTileDirty();
		getTileWorld().notifyBlockUpdate(pos, getTileWorld().getBlockState(pos), newState, 3);
		getTileWorld().notifyNeighborsOfStateChange(pos, getTileBlockType(), true);
	}
	
	// Capabilities
	
	/** Use when the capability provider side argument must be non-null */
	public default @Nonnull EnumFacing nonNullSide(@Nullable EnumFacing side) {
		return side == null ? EnumFacing.UP : side;
	}
}
