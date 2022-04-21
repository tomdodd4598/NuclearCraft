package nc.multiblock.cuboidal;

import javax.annotation.*;

import nc.multiblock.BlockFacing;
import nc.multiblock.tile.TileMultiblockPart;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public abstract class TileCuboidalMultiblockPart<MULTIBLOCK extends CuboidalMultiblock<MULTIBLOCK, T>, T extends ITileCuboidalMultiblockPart<MULTIBLOCK, T>> extends TileMultiblockPart<MULTIBLOCK, T> implements ITileCuboidalMultiblockPart<MULTIBLOCK, T> {
	
	protected final CuboidalPartPositionType positionType;
	protected PartPosition partPosition;
	protected BlockFacing outwardFacings;
	
	public TileCuboidalMultiblockPart(Class<MULTIBLOCK> multiblockClass, Class<T> tClass, CuboidalPartPositionType positionType) {
		super(multiblockClass, tClass);
		
		this.positionType = positionType;
		partPosition = PartPosition.Unknown;
		outwardFacings = BlockFacing.NONE;
	}
	
	// Positional Data
	
	@Override
	public CuboidalPartPositionType getPartPositionType() {
		return positionType;
	}
	
	/** Get the position of the part in the formed multiblock
	 *
	 * @return the position of the part */
	@Override
	@Nonnull
	public PartPosition getPartPosition() {
		return partPosition;
	}
	
	/** Get the outward facing of the part in the formed multiblock
	 *
	 * @return the outward facing of the part. A face is "set" in the BlockFacings object if that face is facing outward */
	@Override
	@Nonnull
	public BlockFacing getOutwardsDir() {
		return outwardFacings;
	}
	
	/** Return the single direction this part is facing if the part is in one side of the multiblock
	 *
	 * @return the direction toward with the part is facing or null if the part is not in one side of the multiblock */
	@Override
	@Nullable
	public EnumFacing getOutwardFacing() {
		EnumFacing facing = null != partPosition ? partPosition.getFacing() : null;
		
		if (null == facing) {
			BlockFacing out = this.getOutwardsDir();
			if (!out.none() && 1 == out.countFacesIf(true)) {
				facing = out.firstIf(true);
			}
		}
		return facing;
	}
	
	/** Return the single direction this part is facing based on it's position in the multiblock
	 *
	 * @return the direction toward with the part is facing or null if the part is not in one side of the multiblock */
	@Override
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
		partPosition = PartPosition.Unknown;
		outwardFacings = BlockFacing.NONE;
	}
	
	// Positional helpers
	
	@Override
	public void recalculateOutwardsDirection(BlockPos minCoord, BlockPos maxCoord) {
		BlockPos myPosition = getPos();
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
		
		if (eastFacing || westFacing) {
			++facesMatching;
		}
		if (upFacing || downFacing) {
			++facesMatching;
		}
		if (southFacing || northFacing) {
			++facesMatching;
		}
		
		// what is our position in the multiblock structure?
		
		if (facesMatching <= 0) {
			partPosition = PartPosition.Interior;
		}
		else if (facesMatching >= 3) {
			partPosition = PartPosition.FrameCorner;
		}
		else if (facesMatching == 2) {
			if (!eastFacing && !westFacing) {
				partPosition = PartPosition.FrameEastWest;
			}
			else if (!southFacing && !northFacing) {
				partPosition = PartPosition.FrameSouthNorth;
			}
			else {
				partPosition = PartPosition.FrameUpDown;
			}
		}
		else {
			// only 1 face matches
			if (eastFacing) {
				partPosition = PartPosition.EastFace;
			}
			else if (westFacing) {
				partPosition = PartPosition.WestFace;
			}
			else if (southFacing) {
				partPosition = PartPosition.SouthFace;
			}
			else if (northFacing) {
				partPosition = PartPosition.NorthFace;
			}
			else if (upFacing) {
				partPosition = PartPosition.TopFace;
			}
			else {
				partPosition = PartPosition.BottomFace;
			}
		}
	}
	
	// Validation Helpers
	
	@Override
	public boolean isGoodForFrame(MULTIBLOCK multiblock) {
		if (positionType.isGoodForFrame()) {
			return true;
		}
		setStandardLastError(multiblock);
		return false;
	}
	
	@Override
	public boolean isGoodForSides(MULTIBLOCK multiblock) {
		if (positionType.isGoodForWall()) {
			return true;
		}
		setStandardLastError(multiblock);
		return false;
	}
	
	@Override
	public boolean isGoodForTop(MULTIBLOCK multiblock) {
		if (positionType.isGoodForWall()) {
			return true;
		}
		setStandardLastError(multiblock);
		return false;
	}
	
	@Override
	public boolean isGoodForBottom(MULTIBLOCK multiblock) {
		if (positionType.isGoodForWall()) {
			return true;
		}
		setStandardLastError(multiblock);
		return false;
	}
	
	@Override
	public boolean isGoodForInterior(MULTIBLOCK multiblock) {
		if (positionType.isGoodForInterior()) {
			return true;
		}
		setStandardLastError(multiblock);
		return false;
	}
}
