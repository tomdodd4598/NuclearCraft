package nc.multiblock;

import it.unimi.dsi.fastutil.bytes.*;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

/** A general purpose class to track the state of all 6 faces of a block
 *
 * Example usages: - track which faces are exposed on the outside walls of a complex structure - track which faces is connected to a face of a similar block */
public final class BlockFacing {
	
	public static final BlockFacing NONE, ALL, DOWN, UP, NORTH, SOUTH, WEST, EAST;
	
	public static final PropertyBool FACING_DOWN = PropertyBool.create("downFacing");
	public static final PropertyBool FACING_UP = PropertyBool.create("upFacing");
	public static final PropertyBool FACING_WEST = PropertyBool.create("westFacing");
	public static final PropertyBool FACING_EAST = PropertyBool.create("eastFacing");
	public static final PropertyBool FACING_NORTH = PropertyBool.create("northFacing");
	public static final PropertyBool FACING_SOUTH = PropertyBool.create("southFacing");
	
	/** Check if a specific face is "set"
	 * 
	 * @param facing
	 *            the face to check
	 * @return true if the face is "set", false otherwise */
	public boolean isSet(EnumFacing facing) {
		
		return 0 != (_value & 1 << facing.getIndex());
	}
	
	public boolean none() {
		return 0 == _value;
	}
	
	public boolean all() {
		return 0x3f == _value;
	}
	
	public boolean down() {
		return isSet(EnumFacing.DOWN);
	}
	
	public boolean up() {
		return isSet(EnumFacing.UP);
	}
	
	public boolean north() {
		return isSet(EnumFacing.NORTH);
	}
	
	public boolean south() {
		return isSet(EnumFacing.SOUTH);
	}
	
	public boolean west() {
		return isSet(EnumFacing.WEST);
	}
	
	public boolean east() {
		return isSet(EnumFacing.EAST);
	}
	
	public IBlockState toBlockState(IBlockState state) {
		return state.withProperty(FACING_DOWN, isSet(EnumFacing.DOWN)).withProperty(FACING_UP, isSet(EnumFacing.UP)).withProperty(FACING_WEST, isSet(EnumFacing.WEST)).withProperty(FACING_EAST, isSet(EnumFacing.EAST)).withProperty(FACING_NORTH, isSet(EnumFacing.NORTH)).withProperty(FACING_SOUTH, isSet(EnumFacing.SOUTH));
	}
	
	/** Return a BlockFacing object that describe the current facing with the given face set or unset
	 *
	 * @param facing
	 *            the face to modify
	 * @param value
	 *            the new value for the state of the face
	 * @return a BlockFacing object */
	public BlockFacing set(EnumFacing facing, boolean value) {
		byte newHash = _value;
		
		if (value) {
			newHash |= 1 << facing.getIndex();
		}
		else {
			newHash &= ~(1 << facing.getIndex());
		}
		
		return BlockFacing.from(newHash);
	}
	
	/** Count the number of faces that are in the required state
	 *
	 * @param areSet
	 *            specify if you are looking for "set" faces (true) or not (false)
	 * @return the number of faces found in the required state */
	public int countFacesIf(boolean areSet) {
		int checkFor = areSet ? 1 : 0;
		int mask = _value;
		int faces = 0;
		
		for (int i = 0; i < 6; ++i, mask = mask >>> 1) {
			
			if ((mask & 1) == checkFor) {
				++faces;
			}
		}
		
		return faces;
	}
	
	/** Return a PropertyBlockFacings for the current facing
	 *
	 * @return a PropertyBlockFacings value */
	public PropertyBlockFacing toProperty() {
		PropertyBlockFacing[] values = PropertyBlockFacing.values();
		
		for (PropertyBlockFacing value : values) {
			if (value._hash == _value) {
				return value;
			}
		}
		
		return PropertyBlockFacing.None;
	}
	
	/** Offset the given BlockPos in all direction set in this object
	 *
	 * @param originalPosition
	 *            the original position
	 * @return the new position */
	public BlockPos offsetBlockPos(BlockPos originalPosition) {
		int x = 0, y = 0, z = 0;
		
		for (EnumFacing facing : EnumFacing.VALUES) {
			if (isSet(facing)) {
				
				x += facing.getXOffset();
				y += facing.getYOffset();
				z += facing.getZOffset();
			}
		}
		
		return originalPosition.add(x, y, z);
	}
	
	/** Return the first face that is in the required state
	 *
	 * @param isSet
	 *            specify if you are looking for "set" faces (true) or not (false)
	 * @return the first face that match the required state or null if no face is found */
	public EnumFacing firstIf(boolean isSet) {
		for (EnumFacing facing : EnumFacing.VALUES) {
			if (isSet == isSet(facing)) {
				return facing;
			}
		}
		
		return null;
	}
	
	/** Return a BlockFacing object that describe the passed in state
	 * 
	 * @param down
	 *            the state of the "down" face
	 * @param up
	 *            the state of the "up" face
	 * @param north
	 *            the state of the "north" face
	 * @param south
	 *            the state of the "south" face
	 * @param west
	 *            the state of the "west" face
	 * @param east
	 *            the state of the "east" face
	 * @return a BlockFacing object */
	public static BlockFacing from(boolean down, boolean up, boolean north, boolean south, boolean west, boolean east) {
		return BlockFacing.from(BlockFacing.computeHash(down, up, north, south, west, east));
	}
	
	/** Return a BlockFacing object that describe the passed in state
	 * 
	 * @param facings
	 *            an array describing the state. the elements of the array must be filled in following the order in EnumFacing.VALUES
	 * @return a BlockFacing object */
	public static BlockFacing from(boolean[] facings) {
		return BlockFacing.from(BlockFacing.computeHash(facings));
	}
	
	@Override
	public String toString() {
		
		return String.format("Facings: %s%s%s%s%s%s", isSet(EnumFacing.DOWN) ? "DOWN " : "", isSet(EnumFacing.UP) ? "UP " : "", isSet(EnumFacing.NORTH) ? "NORTH " : "", isSet(EnumFacing.SOUTH) ? "SOUTH " : "", isSet(EnumFacing.WEST) ? "WEST " : "", isSet(EnumFacing.EAST) ? "EAST " : "");
	}
	
	static BlockFacing from(byte hash) {
		BlockFacing facings = CACHE.get(hash);
		
		if (null == facings) {
			
			facings = new BlockFacing(hash);
			CACHE.put(hash, facings);
		}
		
		return facings;
	}
	
	private final byte _value;
	
	private BlockFacing(byte value) {
		_value = value;
	}
	
	static byte computeHash(boolean down, boolean up, boolean north, boolean south, boolean west, boolean east) {
		byte hash = 0;
		
		if (down) {
			hash |= 1 << EnumFacing.DOWN.getIndex();
		}
		
		if (up) {
			hash |= 1 << EnumFacing.UP.getIndex();
		}
		
		if (north) {
			hash |= 1 << EnumFacing.NORTH.getIndex();
		}
		
		if (south) {
			hash |= 1 << EnumFacing.SOUTH.getIndex();
		}
		
		if (west) {
			hash |= 1 << EnumFacing.WEST.getIndex();
		}
		
		if (east) {
			hash |= 1 << EnumFacing.EAST.getIndex();
		}
		
		return hash;
	}
	
	static byte computeHash(boolean[] facings) {
		byte hash = 0;
		int len = null == facings ? -1 : facings.length;
		
		if (len < 0 || len > EnumFacing.VALUES.length) {
			throw new IllegalArgumentException("Invalid length of facings array");
		}
		
		for (int i = 0; i < len; ++i) {
			if (facings[i]) {
				hash |= 1 << EnumFacing.VALUES[i].getIndex();
			}
		}
		
		return hash;
	}
	
	private static final Byte2ObjectMap<BlockFacing> CACHE = new Byte2ObjectOpenHashMap<>();
	
	static {
		byte hash = BlockFacing.computeHash(false, false, false, false, false, false);
		CACHE.put(hash, NONE = new BlockFacing(hash));
		
		hash = BlockFacing.computeHash(true, true, true, true, true, true);
		CACHE.put(hash, ALL = new BlockFacing(hash));
		
		hash = BlockFacing.computeHash(true, false, false, false, false, false);
		CACHE.put(hash, DOWN = new BlockFacing(hash));
		
		hash = BlockFacing.computeHash(false, true, false, false, false, false);
		CACHE.put(hash, UP = new BlockFacing(hash));
		
		hash = BlockFacing.computeHash(false, false, true, false, false, false);
		CACHE.put(hash, NORTH = new BlockFacing(hash));
		
		hash = BlockFacing.computeHash(false, false, false, true, false, false);
		CACHE.put(hash, SOUTH = new BlockFacing(hash));
		
		hash = BlockFacing.computeHash(false, false, false, false, true, false);
		CACHE.put(hash, WEST = new BlockFacing(hash));
		
		hash = BlockFacing.computeHash(false, false, false, false, false, true);
		CACHE.put(hash, EAST = new BlockFacing(hash));
	}
}
