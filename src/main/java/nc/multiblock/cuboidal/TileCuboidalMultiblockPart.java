package nc.multiblock.cuboidal;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import nc.multiblock.BlockFacing;
import nc.multiblock.Multiblock;
import nc.multiblock.tile.TileMultiblockPart;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public abstract class TileCuboidalMultiblockPart<MULTIBLOCK extends CuboidalMultiblock> extends TileMultiblockPart<MULTIBLOCK> {
	
	private final CuboidalPartPositionType positionType;
	private PartPosition position;
	private BlockFacing outwardFacings;
	
	public TileCuboidalMultiblockPart(Class<MULTIBLOCK> tClass, CuboidalPartPositionType positionType) {
		super(tClass);
		
		this.positionType = positionType;
		position = PartPosition.Unknown;
		outwardFacings = BlockFacing.NONE;
	}
	
	// Positional Data
	
	public CuboidalPartPositionType getPartPositionType() {
		return positionType;
	}
	
	/**
	 * Get the position of the part in the formed multiblock
	 *
	 * @return the position of the part
	 */
	@Nonnull
	public PartPosition getPartPosition() {
		return position;
	}
	
	/**
	 * Get the outward facing of the part in the formed multiblock
	 *
	 * @return the outward facing of the part. A face is "set" in the BlockFacings object if that face is facing outward
	 */
	@Nonnull
	public BlockFacing getOutwardsDir() {
		return outwardFacings;
	}
	
	/**
	 * Return the single direction this part is facing if the part is in one side of the multiblock
	 *
	 * @return the direction toward with the part is facing or null if the part is not in one side of the multiblock
	 */
	@Nullable
	public EnumFacing getOutwardFacing() {
		EnumFacing facing = null != this.position ? this.position.getFacing() : null;
		
		if (null == facing) {
			BlockFacing out = this.getOutwardsDir();
			if (!out.none() && 1 == out.countFacesIf(true)) {
				facing = out.firstIf(true);
			}
		}
		return facing;
	}
	
	/**
	 * Return the single direction this part is facing based on it's position in the multiblock
	 *
	 * @return the direction toward with the part is facing or null if the part is not in one side of the multiblock
	 */
	@Nullable
	public EnumFacing getOutwardFacingFromWorldPosition() {
		BlockFacing facings = null;
		MULTIBLOCK multiblock = getMultiblock();
		
		if (null != multiblock) {
			BlockPos position = pos;
			BlockPos min = multiblock.getMinimumCoord();
			BlockPos max = multiblock.getMaximumCoord();
			int x = position.getX(), y = position.getY(), z = position.getZ();
			
			facings = BlockFacing.from(min.getY() == y, max.getY() == y, min.getZ() == z, max.getZ() == z, min.getX() == x, max.getX() == x);
		}
		
		return null != facings && !facings.none() && 1 == facings.countFacesIf(true) ? facings.firstIf(true) : null;
	}
	
	// Handlers from MultiblockTileEntityBase
	
	@Override
	public void onAttached(MULTIBLOCK newMultiblock) {
		super.onAttached(newMultiblock);
		recalculateOutwardsDirection(newMultiblock.getMinimumCoord(), newMultiblock.getMaximumCoord());
	}
	
	@Override
	public void onMachineAssembled(MULTIBLOCK multiblock) {
		// Discover where I am on the multiblock
		recalculateOutwardsDirection(multiblock.getMinimumCoord(), multiblock.getMaximumCoord());
	}
	
	@Override
	public void onMachineBroken() {
		position = PartPosition.Unknown;
		outwardFacings = BlockFacing.NONE;
	}
	
	// Positional helpers
	
	public void recalculateOutwardsDirection(BlockPos minCoord, BlockPos maxCoord) {
		BlockPos myPosition = this.getPos();
		int myX = myPosition.getX();
		int myY = myPosition.getY();
		int myZ = myPosition.getZ();
		int facesMatching = 0;
		
		// which direction are we facing?
		
		boolean downFacing = myY == minCoord.getY();
		boolean upFacing = myY == maxCoord.getY();
		boolean northFacing = myZ == minCoord.getZ();
		boolean southFacing = myZ == maxCoord.getZ();
		boolean westFacing = myX == minCoord.getX();
		boolean eastFacing = myX == maxCoord.getX();
		
		this.outwardFacings = BlockFacing.from(downFacing, upFacing, northFacing, southFacing, westFacing, eastFacing);
		
		// how many faces are facing outward?
		
		if (eastFacing || westFacing) ++facesMatching;
		if (upFacing || downFacing) ++facesMatching;
		if (southFacing || northFacing) ++facesMatching;
		
		// what is our position in the multiblock structure?
		
		if (facesMatching <= 0) {
			this.position = PartPosition.Interior;
		}
		else if (facesMatching >= 3) {
			this.position = PartPosition.FrameCorner;
		}
		else if (facesMatching == 2) {
			if (!eastFacing && !westFacing) {
				this.position = PartPosition.FrameEastWest;
			}
			else if (!southFacing && !northFacing) {
				this.position = PartPosition.FrameSouthNorth;
			}
			else {
				this.position = PartPosition.FrameUpDown;
			}
		}
		else {
			// only 1 face matches
			if (eastFacing) {
				this.position = PartPosition.EastFace;
			}
			else if (westFacing) {
				this.position = PartPosition.WestFace;
			}
			else if (southFacing) {
				this.position = PartPosition.SouthFace;
			}
			else if (northFacing) {
				this.position = PartPosition.NorthFace;
			}
			else if (upFacing) {
				this.position = PartPosition.TopFace;
			}
			else {
				this.position = PartPosition.BottomFace;
			}
		}
	}
	
	///// Validation Helpers (IMultiblockPart)
	
	public boolean isGoodForFrame(Multiblock multiblock) {
		if (positionType.isGoodForFrame()) return true;
		setStandardLastError(multiblock);
		return false;
	}

	public boolean isGoodForSides(Multiblock multiblock) {
		if (positionType.isGoodForWall()) return true;
		setStandardLastError(multiblock);
		return false;
	}

	public boolean isGoodForTop(Multiblock multiblock) {
		if (positionType.isGoodForWall()) return true;
		setStandardLastError(multiblock);
		return false;
	}

	public boolean isGoodForBottom(Multiblock multiblock) {
		if (positionType.isGoodForWall()) return true;
		setStandardLastError(multiblock);
		return false;
	}

	public boolean isGoodForInterior(Multiblock multiblock) {
		if (positionType.isGoodForInterior()) return true;
		setStandardLastError(multiblock);
		return false;
	}
}
