package nc.tile;

import static nc.block.property.BlockProperties.FACING_HORIZONTAL;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import nc.capability.radiation.source.IRadiationSource;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface ITile {
	
	public World getTileWorld();
	
	public BlockPos getTilePos();
	
	public Block getTileBlockType();
	
	public int getTileBlockMeta();
	
	public default EnumFacing getFacingHorizontal() {
		return getTileBlockType().getStateFromMeta(getTileBlockMeta()).getValue(FACING_HORIZONTAL);
	}
	
	public default IBlockState getBlockState(BlockPos pos) {
		return getTileWorld().getBlockState(pos);
	}
	
	public default Block getBlock(BlockPos pos) {
		return getBlockState(pos).getBlock();
	}
	
	public IRadiationSource getRadiationSource();
	
	public default boolean shouldSaveRadiation() {
		return true;
	}
	
	public void setState(boolean isActive, TileEntity tile);
	
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
	
	public void markTileDirty();
	
	public void markDirtyAndNotify();
	
	// Capabilities
	
	/** Use when the capability provider side argument must be non-null */
	public default @Nonnull EnumFacing nonNullSide(@Nullable EnumFacing side) {
		return side == null ? EnumFacing.DOWN : side;
	}
}
