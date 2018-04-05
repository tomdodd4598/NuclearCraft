package nc.multiblock.cuboidal;

/*
 * A multiblock library for making irregularly-shaped multiblock machines
 *
 * Original author: Erogenous Beef
 * https://github.com/erogenousbeef/BeefCore
 *
 * Ported to Minecraft 1.9 by ZeroNoRyouki
 * https://github.com/ZeroNoRyouki/ZeroCore
 */

import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;

public enum PartPosition implements IStringSerializable {
	Unknown(null, Type.Unknown),
	Interior(null, Type.Unknown),
	FrameCorner(null, Type.Frame),
	FrameEastWest(null, Type.Frame),
	FrameSouthNorth(null, Type.Frame),
	FrameUpDown(null, Type.Frame),
	TopFace(EnumFacing.UP, Type.Face),
	BottomFace(EnumFacing.DOWN, Type.Face),
	NorthFace(EnumFacing.NORTH, Type.Face),
	SouthFace(EnumFacing.SOUTH, Type.Face),
	EastFace(EnumFacing.EAST, Type.Face),
	WestFace(EnumFacing.WEST, Type.Face);

	public enum Type {
		Unknown,
		Interior,
		Frame,
		Face
	}
	
	public boolean isFace() {
		return this._type == Type.Face;
	}

	public boolean isFrame() {
		return this._type == Type.Frame;
	}

	public EnumFacing getFacing() {
		return this._facing;
	}

	public Type getType() {
		return this._type;
	}

	public static PropertyEnum createProperty(String name) {

		return PropertyEnum.create(name, PartPosition.class);
	}

	@Override
	public String getName() {

		return this.toString();
	}

	PartPosition(EnumFacing facing, Type type) {

		this._facing = facing;
		this._type = type;
	}

	private EnumFacing _facing;
	private Type _type;
}
