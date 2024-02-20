package nc.tile;

import java.util.List;

import javax.annotation.*;

import nc.block.property.BlockProperties;
import nc.block.tile.IActivatable;
import nc.capability.radiation.source.IRadiationSource;
import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface ITile {
	
	TileEntity getTile();
	
	World getTileWorld();
	
	BlockPos getTilePos();
	
	Block getTileBlockType();
	
	int getTileBlockMeta();
	
	default EnumFacing getFacingHorizontal() {
		return getTileBlockType().getStateFromMeta(getTileBlockMeta()).getValue(BlockProperties.FACING_HORIZONTAL);
	}
	
	default IBlockState getBlockState(BlockPos pos) {
		return getTileWorld().getBlockState(pos);
	}
	
	default Block getBlock(BlockPos pos) {
		return getBlockState(pos).getBlock();
	}
	
	IRadiationSource getRadiationSource();
	
	default boolean shouldSaveRadiation() {
		return true;
	}
	
	default void setActivity(boolean isActive) {
		setState(isActive, getTile());
	}
	
	@Deprecated
    default void setState(boolean isActive, TileEntity tile) {
		if (getTileBlockType() instanceof IActivatable) {
			((IActivatable) getTileBlockType()).setActivity(isActive, tile);
		}
	}
	
	default void onBlockNeighborChanged(IBlockState state, World world, BlockPos pos, BlockPos fromPos) {
		refreshIsRedstonePowered(world, pos);
	}
	
	default boolean isUsableByPlayer(EntityPlayer player) {
		return player.getDistanceSq(getTilePos().getX() + 0.5D, getTilePos().getY() + 0.5D, getTilePos().getZ() + 0.5D) <= 64D;
	}
	
	// Redstone
	
	default boolean checkIsRedstonePowered(World world, BlockPos pos) {
		return world.isBlockPowered(pos) || isWeaklyPowered(world, pos);
	}
	
	default int[] weakSidesToCheck(World world, BlockPos pos) {
		return new int[] {};
	}
	
	default boolean isWeaklyPowered(World world, BlockPos pos) {
		for (int i : weakSidesToCheck(world, pos)) {
			EnumFacing side = EnumFacing.byIndex(i);
			BlockPos offPos = pos.offset(side);
			if (world.getRedstonePower(offPos, side) > 0) {
				return true;
			}
			else {
				IBlockState state = world.getBlockState(offPos);
				if (state.getBlock() == Blocks.REDSTONE_WIRE && state.getValue(BlockRedstoneWire.POWER) > 0) {
					return true;
				}
			}
		}
		return false;
	}
	
	default void refreshIsRedstonePowered(World world, BlockPos pos) {
		setIsRedstonePowered(checkIsRedstonePowered(world, pos));
	}
	
	boolean getIsRedstonePowered();
	
	void setIsRedstonePowered(boolean isRedstonePowered);
	
	boolean getAlternateComparator();
	
	void setAlternateComparator(boolean alternate);
	
	boolean getRedstoneControl();
	
	void setRedstoneControl(boolean redstoneControl);
	
	// State Updating
	
	void markTileDirty();
	
	default void notifyBlockUpdate() {
		IBlockState state = getTileWorld().getBlockState(getTilePos());
		getTileWorld().notifyBlockUpdate(getTilePos(), state, state, 3);
	}
	
	default void notifyNeighborsOfStateChange() {
		getTileWorld().notifyNeighborsOfStateChange(getTilePos(), getTileBlockType(), true);
	}
	
	/** Call after markDirty if comparators might need to know about the changes made to the TE */
	default void updateComparatorOutputLevel() {
		getTileWorld().updateComparatorOutputLevel(getTilePos(), getTileBlockType());
	}
	
	default void markDirtyAndNotify(boolean notifyNeighbors) {
		markTileDirty();
		notifyBlockUpdate();
		if (notifyNeighbors) {
			notifyNeighborsOfStateChange();
		}
	}
	
	@Deprecated
    default void markDirtyAndNotify() {
		markDirtyAndNotify(true);
	}
	
	// Capabilities
	
	/** Use when the capability provider side argument must be non-null */
	default @Nonnull EnumFacing nonNullSide(@Nullable EnumFacing side) {
		return side == null ? EnumFacing.DOWN : side;
	}
	
	// HWYLA
	
	default @Nonnull List<String> addToHWYLATooltip(@Nonnull List<String> tooltip) {
		return tooltip;
	}
}
