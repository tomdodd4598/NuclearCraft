package nc.tile;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import nc.block.property.BlockProperties;
import nc.capability.radiation.source.IRadiationSource;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
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
		return getTileBlockType().getStateFromMeta(getTileBlockMeta()).getValue(BlockProperties.FACING_HORIZONTAL);
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
	
	public default boolean isUsableByPlayer(EntityPlayer player) {
		return getTileWorld().getTileEntity(getTilePos()) != this ? false : player.getDistanceSq(getTilePos().getX() + 0.5D, getTilePos().getY() + 0.5D, getTilePos().getZ() + 0.5D) <= 64D;
	}
	
	// Redstone
	
	public default boolean checkIsRedstonePowered(World world, BlockPos pos) {
		return world.isBlockPowered(pos) || isWeaklyPowered(world, pos);
	}
	
	public default int[] weakSidesToCheck(World world, BlockPos pos) {
		return new int[] {};
	}
	
	public default boolean isWeaklyPowered(World world, BlockPos pos) {
		for (int i : weakSidesToCheck(world, pos)) {
			EnumFacing side = EnumFacing.byIndex(i);
			BlockPos offPos = pos.offset(side);
			if (world.getRedstonePower(offPos, side) > 0) {
				return true;
			}
			else {
				IBlockState state = world.getBlockState(offPos);
				if (state.getBlock() == Blocks.REDSTONE_WIRE && state.getValue(BlockRedstoneWire.POWER).intValue() > 0) {
					return true;
				}
			}
		}
		return false;
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
