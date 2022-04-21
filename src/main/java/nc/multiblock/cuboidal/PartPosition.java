package nc.multiblock.cuboidal;

/* A multiblock library for making irregularly-shaped multiblock machines
 *
 * Original author: Erogenous Beef https://github.com/erogenousbeef/BeefCore
 *
 * Ported to Minecraft 1.9 by ZeroNoRyouki https://github.com/ZeroNoRyouki/ZeroCore */

import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.util.*;

public enum PartPosition implements IStringSerializable {
	
	Unknown(null, Type._Unknown),
	Interior(null, Type._Interior),
	FrameCorner(null, Type._Frame),
	FrameEastWest(null, Type._Frame),
	FrameSouthNorth(null, Type._Frame),
	FrameUpDown(null, Type._Frame),
	TopFace(EnumFacing.UP, Type._Face),
	BottomFace(EnumFacing.DOWN, Type._Face),
	NorthFace(EnumFacing.NORTH, Type._Face),
	SouthFace(EnumFacing.SOUTH, Type._Face),
	EastFace(EnumFacing.EAST, Type._Face),
	WestFace(EnumFacing.WEST, Type._Face);
	
	protected enum Type {
		_Unknown,
		_Interior,
		_Frame,
		_Face
	}
	
	public boolean isFace() {
		return _type == Type._Face;
	}
	
	public boolean isFrame() {
		return _type == Type._Frame;
	}
	
	public EnumFacing getFacing() {
		return _facing;
	}
	
	public Type getType() {
		return _type;
	}
	
	public static PropertyEnum<PartPosition> createProperty(String name) {
		return PropertyEnum.create(name, PartPosition.class);
	}
	
	@Override
	public String getName() {
		return toString();
	}
	
	PartPosition(EnumFacing facing, Type type) {
		_facing = facing;
		_type = type;
	}
	
	private EnumFacing _facing;
	private Type _type;
}
