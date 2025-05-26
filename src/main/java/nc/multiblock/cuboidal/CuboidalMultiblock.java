package nc.multiblock.cuboidal;

import nc.multiblock.Multiblock;
import nc.multiblock.internal.MultiblockValidationError;
import nc.util.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.*;
import net.minecraft.util.math.BlockPos.MutableBlockPos;
import net.minecraft.world.World;

import javax.vecmath.Vector3f;

public abstract class CuboidalMultiblock<MULTIBLOCK extends CuboidalMultiblock<MULTIBLOCK, T>, T extends ITileCuboidalMultiblockPart<MULTIBLOCK, T>> extends Multiblock<MULTIBLOCK, T> {
	
	protected CuboidalMultiblock(World world, Class<MULTIBLOCK> multiblockClass, Class<T> tClass) {
		super(world, multiblockClass, tClass);
	}
	
	/**
	 * @return True if the machine is "whole" and should be assembled. False otherwise.
	 */
	@Override
	protected boolean isMachineWhole() {
		
		if (connectedParts.size() < getMinimumNumberOfBlocksForAssembledMachine()) {
			setLastError(MultiblockValidationError.VALIDATION_ERROR_TOO_FEW_PARTS);
			return false;
		}
		
		BlockPos maximumCoord = getMaximumCoord();
		BlockPos minimumCoord = getMinimumCoord();
		
		int minX = minimumCoord.getX();
		int minY = minimumCoord.getY();
		int minZ = minimumCoord.getZ();
		int maxX = maximumCoord.getX();
		int maxY = maximumCoord.getY();
		int maxZ = maximumCoord.getZ();
		
		// Quickly check for exceeded dimensions
		int deltaX = maxX - minX + 1;
		int deltaY = maxY - minY + 1;
		int deltaZ = maxZ - minZ + 1;
		
		int maxXSize = getMaximumXSize();
		int maxYSize = getMaximumYSize();
		int maxZSize = getMaximumZSize();
		int minXSize = getMinimumXSize();
		int minYSize = getMinimumYSize();
		int minZSize = getMinimumZSize();
		
		if (maxXSize > 0 && deltaX > maxXSize) {
			setLastError("zerocore.api.nc.multiblock.validation.machine_too_large", null, maxXSize, "x");
			return false;
		}
		if (maxYSize > 0 && deltaY > maxYSize) {
			setLastError("zerocore.api.nc.multiblock.validation.machine_too_large", null, maxYSize, "y");
			return false;
		}
		if (maxZSize > 0 && deltaZ > maxZSize) {
			setLastError("zerocore.api.nc.multiblock.validation.machine_too_large", null, maxZSize, "z");
			return false;
		}
		if (deltaX < minXSize) {
			setLastError("zerocore.api.nc.multiblock.validation.machine_too_small", null, minXSize, "x");
			return false;
		}
		if (deltaY < minYSize) {
			setLastError("zerocore.api.nc.multiblock.validation.machine_too_small", null, minYSize, "y");
			return false;
		}
		if (deltaZ < minZSize) {
			setLastError("zerocore.api.nc.multiblock.validation.machine_too_small", null, minZSize, "z");
			return false;
		}
		
		// Now we run a simple check on each block within that volume.
		// Any block deviating = NO DEAL SIR
		TileEntity te;
		T part;
		int extremes;
		boolean isPartValid;
		
		for (int x = minX; x <= maxX; ++x) {
			for (int y = minY; y <= maxY; ++y) {
				for (int z = minZ; z <= maxZ; ++z) {
					// Okay, figure out what sort of block this should be.
					BlockPos pos = new BlockPos(x, y, z);
					
					te = WORLD.getTileEntity(pos);
					if (tClass.isInstance(te)) {
						part = tClass.cast(te);
						
						// Ensure this part should actually be allowed within a cuboid of this multiblock's type
						if (!multiblockClass.equals(part.getMultiblockClass())) {
							setLastError("zerocore.api.nc.multiblock.validation.invalid_part", pos, x, y, z);
							return false;
						}
						
						// Ensure this part is actually connected to this multiblock
						if (part.getMultiblock() != this) {
							setLastError("zerocore.api.nc.multiblock.validation.invalid_part_disconnected", pos, x, y, z);
							return false;
						}
					}
					else {
						// This is permitted so that we can incorporate certain non-multiblock parts inside interiors
						part = null;
					}
					
					// Validate block type against both part-level and material-level validators.
					extremes = 0;
					
					if (x == minX) {
						++extremes;
					}
					if (y == minY) {
						++extremes;
					}
					if (z == minZ) {
						++extremes;
					}
					
					if (x == maxX) {
						++extremes;
					}
					if (y == maxY) {
						++extremes;
					}
					if (z == maxZ) {
						++extremes;
					}
					
					if (extremes >= 2) {
						
						isPartValid = part != null ? part.isGoodForFrame(multiblockClass.cast(this)) : isBlockGoodForFrame(WORLD, pos);
						
						if (!isPartValid) {
							if (getLastError() == null) {
								setLastError("zerocore.api.nc.multiblock.validation.invalid_part_for_frame", pos, x, y, z);
							}
							return false;
						}
					}
					else if (extremes == 1) {
						if (y == maxY) {
							
							isPartValid = part != null ? part.isGoodForTop(multiblockClass.cast(this)) : isBlockGoodForTop(WORLD, pos);
							
							if (!isPartValid) {
								if (getLastError() == null) {
									setLastError("zerocore.api.nc.multiblock.validation.invalid_part_for_top", pos, x, y, z);
								}
								return false;
							}
						}
						else if (y == minY) {
							
							isPartValid = part != null ? part.isGoodForBottom(multiblockClass.cast(this)) : isBlockGoodForBottom(WORLD, pos);
							
							if (!isPartValid) {
								if (getLastError() == null) {
									setLastError("zerocore.api.nc.multiblock.validation.invalid_part_for_bottom", pos, x, y, z);
								}
								return false;
							}
						}
						else {
							// Side
							isPartValid = part != null ? part.isGoodForSides(multiblockClass.cast(this)) : isBlockGoodForSides(WORLD, pos);
							
							if (!isPartValid) {
								if (getLastError() == null) {
									setLastError("zerocore.api.nc.multiblock.validation.invalid_part_for_sides", pos, x, y, z);
								}
								return false;
							}
						}
					}
					else {
						
						isPartValid = part != null ? part.isGoodForInterior(multiblockClass.cast(this)) : isBlockGoodForInterior(WORLD, pos);
						
						if (!isPartValid) {
							if (getLastError() == null) {
								setLastError("zerocore.api.nc.multiblock.validation.invalid_part_for_interior", pos, x, y, z);
							}
							return false;
						}
					}
				}
			}
		}
		return true;
	}
	
	public boolean hasAxialSymmetry(EnumFacing.Axis axis) {
		if (axis == null) {
			return true;
		}
		
		EnumFacing normal = PosHelper.getAxisDirectionDir(axis, EnumFacing.AxisDirection.NEGATIVE);
		int interiorLength = getInteriorLength(normal);
		
		if (interiorLength <= 1) {
			return true;
		}
		
		Iterable<MutableBlockPos> plane = getInteriorPlane(normal, 0, 0, 0, 0, 0);
		
		for (MutableBlockPos planePos : plane) {
			MutableBlockPos columnPos = new MutableBlockPos(planePos.x, planePos.y, planePos.z);
			ItemStack stack = StackHelper.blockStateToStack(WORLD.getBlockState(columnPos));
			
			for (int i = 1; i < interiorLength; ++i) {
				switch (axis) {
					case X -> ++columnPos.x;
					case Y -> ++columnPos.y;
					case Z -> ++columnPos.z;
				}
				
				IBlockState state = WORLD.getBlockState(columnPos);
				if ((!stack.isEmpty() || !state.getMaterial().equals(Material.AIR)) && !stack.isItemEqual(StackHelper.blockStateToStack(state))) {
					if (getLastError() == null) {
						setLastError("zerocore.api.nc.multiblock.validation.invalid_axial_symmetry", columnPos, columnPos.x, columnPos.y, columnPos.z, axis.getName());
					}
					return false;
				}
			}
		}
		
		return true;
	}
	
	public boolean hasPlanarSymmetry(EnumFacing.Axis axis) {
		if (axis == null) {
			return true;
		}
		
		EnumFacing normal = PosHelper.getAxisDirectionDir(axis, EnumFacing.AxisDirection.NEGATIVE);
		int interiorLength = getInteriorLength(normal);
		
		for (int i = 0; i < interiorLength; ++i) {
			ItemStack stack = null;
			for (BlockPos pos : getInteriorPlane(normal, i, 0, 0, 0, 0)) {
				IBlockState state = WORLD.getBlockState(pos);
				if (stack == null) {
					stack = StackHelper.blockStateToStack(state);
				}
				else if ((!stack.isEmpty() || !state.getMaterial().equals(Material.AIR)) && !stack.isItemEqual(StackHelper.blockStateToStack(state))) {
					if (getLastError() == null) {
						String planeName = switch (axis) {
							case X -> "YZ";
							case Y -> "XZ";
							case Z -> "XY";
						};
						setLastError("zerocore.api.nc.multiblock.validation.invalid_planar_symmetry", pos, pos.getX(), pos.getY(), pos.getZ(), planeName);
					}
					return false;
				}
			}
		}
		
		return true;
	}
	
	public BlockPos getMinimumInteriorCoord() {
		return getMinimumCoord().add(1, 1, 1);
	}
	
	public BlockPos getMaximumInteriorCoord() {
		return getMaximumCoord().add(-1, -1, -1);
	}
	
	public int getMinInteriorX() {
		return getMinimumCoord().getX() + 1;
	}
	
	public int getMinInteriorY() {
		return getMinimumCoord().getY() + 1;
	}
	
	public int getMinInteriorZ() {
		return getMinimumCoord().getZ() + 1;
	}
	
	public int getMaxInteriorX() {
		return getMaximumCoord().getX() - 1;
	}
	
	public int getMaxInteriorY() {
		return getMaximumCoord().getY() - 1;
	}
	
	public int getMaxInteriorZ() {
		return getMaximumCoord().getZ() - 1;
	}
	
	public BlockPos getExtremeInteriorCoord(boolean maxX, boolean maxY, boolean maxZ) {
		return new BlockPos(maxX ? getMaxInteriorX() : getMinInteriorX(), maxY ? getMaxInteriorY() : getMinInteriorY(), maxZ ? getMaxInteriorZ() : getMinInteriorZ());
	}
	
	public int getClampedInteriorX(int x) {
		return MathHelper.clamp(x, getMinInteriorX(), getMaxInteriorX());
	}
	
	public int getClampedInteriorY(int y) {
		return MathHelper.clamp(y, getMinInteriorY(), getMaxInteriorY());
	}
	
	public int getClampedInteriorZ(int z) {
		return MathHelper.clamp(z, getMinInteriorZ(), getMaxInteriorZ());
	}
	
	public BlockPos getClampedInteriorCoord(BlockPos pos) {
		return new BlockPos(getClampedInteriorX(pos.getX()), getClampedInteriorY(pos.getY()), getClampedInteriorZ(pos.getZ()));
	}
	
	public int getExteriorLengthX() {
		return getMaximumCoord().getX() - getMinimumCoord().getX() + 1;
	}
	
	public int getExteriorLengthY() {
		return getMaximumCoord().getY() - getMinimumCoord().getY() + 1;
	}
	
	public int getExteriorLengthZ() {
		return getMaximumCoord().getZ() - getMinimumCoord().getZ() + 1;
	}
	
	public int getInteriorLengthX() {
		return getMaximumCoord().getX() - getMinimumCoord().getX() - 1;
	}
	
	public int getInteriorLengthY() {
		return getMaximumCoord().getY() - getMinimumCoord().getY() - 1;
	}
	
	public int getInteriorLengthZ() {
		return getMaximumCoord().getZ() - getMinimumCoord().getZ() - 1;
	}
	
	public int getExteriorVolume() {
		return getExteriorLengthX() * getExteriorLengthY() * getExteriorLengthZ();
	}
	
	public int getInteriorVolume() {
		return getInteriorLengthX() * getInteriorLengthY() * getInteriorLengthZ();
	}
	
	public int getExteriorSurfaceArea() {
		return 2 * (getExteriorLengthX() * getExteriorLengthY() + getExteriorLengthY() * getExteriorLengthZ() + getExteriorLengthZ() * getExteriorLengthX());
	}
	
	public int getInteriorSurfaceArea() {
		return 2 * (getInteriorLengthX() * getInteriorLengthY() + getInteriorLengthY() * getInteriorLengthZ() + getInteriorLengthZ() * getInteriorLengthX());
	}
	
	public int getInteriorLength(EnumFacing dir) {
		if (dir == null) {
			return getInteriorLengthY();
		}
		
		return switch (dir) {
			case DOWN -> getInteriorLengthY();
			case UP -> getInteriorLengthY();
			case NORTH -> getInteriorLengthZ();
			case SOUTH -> getInteriorLengthZ();
			case WEST -> getInteriorLengthX();
			case EAST -> getInteriorLengthX();
		};
	}
	
	public boolean isInInterior(BlockPos pos) {
		int x = pos.getX(), y = pos.getY(), z = pos.getZ();
		BlockPos min = getMinimumCoord(), max = getMaximumCoord();
		return x >= min.getX() + 1 && x <= max.getX() - 1 && y >= min.getY() + 1 && y <= max.getY() - 1 && z >= min.getZ() + 1 && z <= max.getZ() - 1;
	}
	
	protected abstract int getMinimumInteriorLength();
	
	protected abstract int getMaximumInteriorLength();
	
	@Override
	protected int getMinimumNumberOfBlocksForAssembledMachine() {
		return NCMath.hollowCube(getMinimumInteriorLength() + 2);
	}
	
	@Override
	protected int getMinimumXSize() {
		return getMinimumInteriorLength() + 2;
	}
	
	@Override
	protected int getMinimumYSize() {
		return getMinimumInteriorLength() + 2;
	}
	
	@Override
	protected int getMinimumZSize() {
		return getMinimumInteriorLength() + 2;
	}
	
	@Override
	protected int getMaximumXSize() {
		return getMaximumInteriorLength() + 2;
	}
	
	@Override
	protected int getMaximumYSize() {
		return getMaximumInteriorLength() + 2;
	}
	
	@Override
	protected int getMaximumZSize() {
		return getMaximumInteriorLength() + 2;
	}
	
	public Iterable<MutableBlockPos> getWallMinX() {
		return BlockPos.getAllInBoxMutable(getExtremeCoord(false, false, false), getExtremeCoord(false, true, true));
	}
	
	public Iterable<MutableBlockPos> getWallMaxX() {
		return BlockPos.getAllInBoxMutable(getExtremeCoord(true, false, false), getExtremeCoord(true, true, true));
	}
	
	public Iterable<MutableBlockPos> getWallMinY() {
		return BlockPos.getAllInBoxMutable(getExtremeCoord(false, false, false), getExtremeCoord(true, false, true));
	}
	
	public Iterable<MutableBlockPos> getWallMaxY() {
		return BlockPos.getAllInBoxMutable(getExtremeCoord(false, true, false), getExtremeCoord(true, true, true));
	}
	
	public Iterable<MutableBlockPos> getWallMinZ() {
		return BlockPos.getAllInBoxMutable(getExtremeCoord(false, false, false), getExtremeCoord(true, true, false));
	}
	
	public Iterable<MutableBlockPos> getWallMaxZ() {
		return BlockPos.getAllInBoxMutable(getExtremeCoord(false, false, true), getExtremeCoord(true, true, true));
	}
	
	public Iterable<MutableBlockPos> getWallMin(EnumFacing.Axis axis) {
		if (axis == null) {
			return BlockPos.getAllInBoxMutable(getExtremeCoord(false, false, false), getExtremeCoord(false, false, false));
		}
		
		return switch (axis) {
			case X -> getWallMinX();
			case Y -> getWallMinY();
			case Z -> getWallMinZ();
		};
	}
	
	public Iterable<MutableBlockPos> getWallMax(EnumFacing.Axis axis) {
		if (axis == null) {
			return BlockPos.getAllInBoxMutable(getExtremeCoord(true, true, true), getExtremeCoord(true, true, true));
		}
		
		return switch (axis) {
			case X -> getWallMaxX();
			case Y -> getWallMaxY();
			case Z -> getWallMaxZ();
		};
	}
	
	public boolean isInMinWall(EnumFacing.Axis axis, BlockPos pos) {
		if (axis == null) {
			return false;
		}
		
		return switch (axis) {
			case X -> pos.getX() == getMinX();
			case Y -> pos.getY() == getMinY();
			case Z -> pos.getZ() == getMinZ();
		};
	}
	
	public boolean isInMaxWall(EnumFacing.Axis axis, BlockPos pos) {
		if (axis == null) {
			return false;
		}
		
		return switch (axis) {
			case X -> pos.getX() == getMaxX();
			case Y -> pos.getY() == getMaxY();
			case Z -> pos.getZ() == getMaxZ();
		};
	}
	
	public boolean isInWall(EnumFacing side, BlockPos pos) {
		if (side == null) {
			return false;
		}
		
		return switch (side) {
			case DOWN -> pos.getY() == getMinY();
			case UP -> pos.getY() == getMaxY();
			case NORTH -> pos.getZ() == getMinZ();
			case SOUTH -> pos.getZ() == getMaxZ();
			case WEST -> pos.getX() == getMinX();
			case EAST -> pos.getX() == getMaxX();
		};
	}
	
	public BlockPos getMinimumInteriorPlaneCoord(EnumFacing normal, int depth, int uCushion, int vCushion) {
		if (normal == null) {
			return getExtremeInteriorCoord(false, false, false);
		}
		
		return switch (normal) {
			case DOWN -> getExtremeInteriorCoord(false, false, false).offset(EnumFacing.UP, depth).offset(EnumFacing.SOUTH, uCushion).offset(EnumFacing.EAST, vCushion);
			case UP -> getExtremeInteriorCoord(false, true, false).offset(EnumFacing.DOWN, depth).offset(EnumFacing.SOUTH, uCushion).offset(EnumFacing.EAST, vCushion);
			case NORTH -> getExtremeInteriorCoord(false, false, false).offset(EnumFacing.SOUTH, depth).offset(EnumFacing.EAST, uCushion).offset(EnumFacing.UP, vCushion);
			case SOUTH -> getExtremeInteriorCoord(false, false, true).offset(EnumFacing.NORTH, depth).offset(EnumFacing.EAST, uCushion).offset(EnumFacing.UP, vCushion);
			case WEST -> getExtremeInteriorCoord(false, false, false).offset(EnumFacing.EAST, depth).offset(EnumFacing.UP, uCushion).offset(EnumFacing.SOUTH, vCushion);
			case EAST -> getExtremeInteriorCoord(true, false, false).offset(EnumFacing.WEST, depth).offset(EnumFacing.UP, uCushion).offset(EnumFacing.SOUTH, vCushion);
		};
	}
	
	public BlockPos getMaximumInteriorPlaneCoord(EnumFacing normal, int depth, int uCushion, int vCushion) {
		if (normal == null) {
			return getExtremeInteriorCoord(false, false, false);
		}
		
		return switch (normal) {
			case DOWN -> getExtremeInteriorCoord(true, false, true).offset(EnumFacing.UP, depth).offset(EnumFacing.NORTH, uCushion).offset(EnumFacing.WEST, vCushion);
			case UP -> getExtremeInteriorCoord(true, true, true).offset(EnumFacing.DOWN, depth).offset(EnumFacing.NORTH, uCushion).offset(EnumFacing.WEST, vCushion);
			case NORTH -> getExtremeInteriorCoord(true, true, false).offset(EnumFacing.SOUTH, depth).offset(EnumFacing.WEST, uCushion).offset(EnumFacing.DOWN, vCushion);
			case SOUTH -> getExtremeInteriorCoord(true, true, true).offset(EnumFacing.NORTH, depth).offset(EnumFacing.WEST, uCushion).offset(EnumFacing.DOWN, vCushion);
			case WEST -> getExtremeInteriorCoord(false, true, true).offset(EnumFacing.EAST, depth).offset(EnumFacing.DOWN, uCushion).offset(EnumFacing.NORTH, vCushion);
			case EAST -> getExtremeInteriorCoord(true, true, true).offset(EnumFacing.WEST, depth).offset(EnumFacing.DOWN, uCushion).offset(EnumFacing.NORTH, vCushion);
		};
	}
	
	public Vector3f getMiddleInteriorPlaneCoord(EnumFacing normal, int depth, int minUCushion, int minVCushion, int maxUCushion, int maxVCushion) {
		BlockPos min = getMinimumInteriorPlaneCoord(normal, depth, minUCushion, minVCushion);
		BlockPos max = getMaximumInteriorPlaneCoord(normal, depth, maxUCushion, maxVCushion);
		return new Vector3f((min.getX() + max.getX()) / 2F, (min.getY() + max.getY()) / 2F, (min.getZ() + max.getZ()) / 2F);
	}
	
	public Iterable<MutableBlockPos> getInteriorPlaneMinX(int depth, int minUCushion, int minVCushion, int maxUCushion, int maxVCushion) {
		return BlockPos.getAllInBoxMutable(getMinimumInteriorPlaneCoord(EnumFacing.WEST, depth, minUCushion, minVCushion), getMaximumInteriorPlaneCoord(EnumFacing.WEST, depth, maxUCushion, maxVCushion));
	}
	
	public Iterable<MutableBlockPos> getInteriorPlaneMaxX(int depth, int minUCushion, int minVCushion, int maxUCushion, int maxVCushion) {
		return BlockPos.getAllInBoxMutable(getMinimumInteriorPlaneCoord(EnumFacing.EAST, depth, minUCushion, minVCushion), getMaximumInteriorPlaneCoord(EnumFacing.EAST, depth, maxUCushion, maxVCushion));
	}
	
	public Iterable<MutableBlockPos> getInteriorPlaneMinY(int depth, int minUCushion, int minVCushion, int maxUCushion, int maxVCushion) {
		return BlockPos.getAllInBoxMutable(getMinimumInteriorPlaneCoord(EnumFacing.DOWN, depth, minUCushion, minVCushion), getMaximumInteriorPlaneCoord(EnumFacing.DOWN, depth, maxUCushion, maxVCushion));
	}
	
	public Iterable<MutableBlockPos> getInteriorPlaneMaxY(int depth, int minUCushion, int minVCushion, int maxUCushion, int maxVCushion) {
		return BlockPos.getAllInBoxMutable(getMinimumInteriorPlaneCoord(EnumFacing.UP, depth, minUCushion, minVCushion), getMaximumInteriorPlaneCoord(EnumFacing.UP, depth, maxUCushion, maxVCushion));
	}
	
	public Iterable<MutableBlockPos> getInteriorPlaneMinZ(int depth, int minUCushion, int minVCushion, int maxUCushion, int maxVCushion) {
		return BlockPos.getAllInBoxMutable(getMinimumInteriorPlaneCoord(EnumFacing.NORTH, depth, minUCushion, minVCushion), getMaximumInteriorPlaneCoord(EnumFacing.NORTH, depth, maxUCushion, maxVCushion));
	}
	
	public Iterable<MutableBlockPos> getInteriorPlaneMaxZ(int depth, int minUCushion, int minVCushion, int maxUCushion, int maxVCushion) {
		return BlockPos.getAllInBoxMutable(getMinimumInteriorPlaneCoord(EnumFacing.SOUTH, depth, minUCushion, minVCushion), getMaximumInteriorPlaneCoord(EnumFacing.SOUTH, depth, maxUCushion, maxVCushion));
	}
	
	public Iterable<MutableBlockPos> getInteriorPlane(EnumFacing normal, int depth, int minUCushion, int minVCushion, int maxUCushion, int maxVCushion) {
		if (normal == null) {
			return BlockPos.getAllInBoxMutable(getExtremeInteriorCoord(false, false, false), getExtremeInteriorCoord(false, false, false));
		}
		
		return switch (normal) {
			case DOWN -> getInteriorPlaneMinY(depth, minUCushion, minVCushion, maxUCushion, maxVCushion);
			case UP -> getInteriorPlaneMaxY(depth, minUCushion, minVCushion, maxUCushion, maxVCushion);
			case NORTH -> getInteriorPlaneMinZ(depth, minUCushion, minVCushion, maxUCushion, maxVCushion);
			case SOUTH -> getInteriorPlaneMaxZ(depth, minUCushion, minVCushion, maxUCushion, maxVCushion);
			case WEST -> getInteriorPlaneMinX(depth, minUCushion, minVCushion, maxUCushion, maxVCushion);
			case EAST -> getInteriorPlaneMaxX(depth, minUCushion, minVCushion, maxUCushion, maxVCushion);
		};
	}
}
