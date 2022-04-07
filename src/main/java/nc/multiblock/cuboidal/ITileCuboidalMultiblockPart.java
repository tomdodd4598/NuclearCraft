package nc.multiblock.cuboidal;

import javax.annotation.*;

import nc.multiblock.BlockFacing;
import nc.multiblock.tile.ITileMultiblockPart;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public interface ITileCuboidalMultiblockPart<MULTIBLOCK extends CuboidalMultiblock<MULTIBLOCK, T>, T extends ITileCuboidalMultiblockPart<MULTIBLOCK, T>> extends ITileMultiblockPart<MULTIBLOCK, T> {
	
	// Positional Data
	
	public CuboidalPartPositionType getPartPositionType();
	
	/** Get the position of the part in the formed multiblock
	 *
	 * @return the position of the part */
	public @Nonnull PartPosition getPartPosition();
	
	/** Get the outward facing of the part in the formed multiblock
	 *
	 * @return the outward facing of the part. A face is "set" in the BlockFacings object if that face is facing outward */
	public @Nonnull BlockFacing getOutwardsDir();
	
	/** Return the single direction this part is facing if the part is in one side of the multiblock
	 *
	 * @return the direction toward with the part is facing or null if the part is not in one side of the multiblock */
	public @Nullable EnumFacing getOutwardFacing();
	
	/** Return the single direction this part is facing based on it's position in the multiblock
	 *
	 * @return the direction toward with the part is facing or null if the part is not in one side of the multiblock */
	public @Nullable EnumFacing getOutwardFacingFromWorldPosition();
	
	// Positional helpers
	
	public void recalculateOutwardsDirection(BlockPos minCoord, BlockPos maxCoord);
	
	// Validation Helpers
	
	public boolean isGoodForFrame(MULTIBLOCK multiblock);
	
	public boolean isGoodForSides(MULTIBLOCK multiblock);
	
	public boolean isGoodForTop(MULTIBLOCK multiblock);
	
	public boolean isGoodForBottom(MULTIBLOCK multiblock);
	
	public boolean isGoodForInterior(MULTIBLOCK multiblock);
}
