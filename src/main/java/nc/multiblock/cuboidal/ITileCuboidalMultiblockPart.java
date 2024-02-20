package nc.multiblock.cuboidal;

import javax.annotation.*;

import nc.multiblock.internal.BlockFacing;
import nc.tile.multiblock.ITileMultiblockPart;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public interface ITileCuboidalMultiblockPart<MULTIBLOCK extends CuboidalMultiblock<MULTIBLOCK, T>, T extends ITileCuboidalMultiblockPart<MULTIBLOCK, T>> extends ITileMultiblockPart<MULTIBLOCK, T> {
	
	// Positional Data
	
	CuboidalPartPositionType getPartPositionType();
	
	/** Get the position of the part in the formed multiblock
	 *
	 * @return the position of the part */
	@Nonnull PartPosition getPartPosition();
	
	/** Get the outward facing of the part in the formed multiblock
	 *
	 * @return the outward facing of the part. A face is "set" in the BlockFacings object if that face is facing outward */
	@Nonnull BlockFacing getOutwardsDir();
	
	/** Return the single direction this part is facing if the part is in one side of the multiblock
	 *
	 * @return the direction toward with the part is facing or null if the part is not in one side of the multiblock */
	@Nullable EnumFacing getOutwardFacing();
	
	/** Return the single direction this part is facing based on it's position in the multiblock
	 *
	 * @return the direction toward with the part is facing or null if the part is not in one side of the multiblock */
	@Nullable EnumFacing getOutwardFacingFromWorldPosition();
	
	// Positional helpers
	
	void recalculateOutwardsDirection(BlockPos minCoord, BlockPos maxCoord);
	
	// Validation Helpers
	
	boolean isGoodForFrame(MULTIBLOCK multiblock);
	
	boolean isGoodForSides(MULTIBLOCK multiblock);
	
	boolean isGoodForTop(MULTIBLOCK multiblock);
	
	boolean isGoodForBottom(MULTIBLOCK multiblock);
	
	boolean isGoodForInterior(MULTIBLOCK multiblock);
}
