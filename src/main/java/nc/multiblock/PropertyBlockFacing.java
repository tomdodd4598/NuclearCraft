package nc.multiblock;

import java.util.*;

import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.util.IStringSerializable;

public enum PropertyBlockFacing implements IStringSerializable {
	
	None(BlockFacing.computeHash(false, false, false, false, false, false)),
	All(BlockFacing.computeHash(true, true, true, true, true, true)),
	
	Face_D(BlockFacing.computeHash(true, false, false, false, false, false)),
	Face_E(BlockFacing.computeHash(false, false, false, false, false, true)),
	Face_N(BlockFacing.computeHash(false, false, true, false, false, false)),
	Face_S(BlockFacing.computeHash(false, false, false, true, false, false)),
	Face_U(BlockFacing.computeHash(false, true, false, false, false, false)),
	Face_W(BlockFacing.computeHash(false, false, false, false, true, false)),
	
	Angle_DE(BlockFacing.computeHash(true, false, false, false, false, true)),
	Angle_DN(BlockFacing.computeHash(true, false, true, false, false, false)),
	Angle_DS(BlockFacing.computeHash(true, false, false, true, false, false)),
	Angle_DW(BlockFacing.computeHash(true, false, false, false, true, false)),
	Angle_EN(BlockFacing.computeHash(false, false, true, false, false, true)),
	Angle_ES(BlockFacing.computeHash(false, false, false, true, false, true)),
	Angle_EU(BlockFacing.computeHash(false, true, false, false, false, true)),
	Angle_NU(BlockFacing.computeHash(false, true, true, false, false, false)),
	Angle_NW(BlockFacing.computeHash(false, false, true, false, true, false)),
	Angle_SU(BlockFacing.computeHash(false, true, false, true, false, false)),
	Angle_SW(BlockFacing.computeHash(false, false, false, true, true, false)),
	Angle_UW(BlockFacing.computeHash(false, true, false, false, true, false)),
	
	Opposite_DU(BlockFacing.computeHash(true, true, false, false, false, false)),
	Opposite_EW(BlockFacing.computeHash(false, false, false, false, true, true)),
	Opposite_NS(BlockFacing.computeHash(false, false, true, true, false, false)),
	
	CShape_DEU(BlockFacing.computeHash(true, true, false, false, false, true)),
	CShape_DEW(BlockFacing.computeHash(true, false, false, false, true, true)),
	CShape_DNS(BlockFacing.computeHash(true, false, true, true, false, false)),
	CShape_DNU(BlockFacing.computeHash(true, true, true, false, false, false)),
	CShape_DSU(BlockFacing.computeHash(true, true, false, true, false, false)),
	CShape_DUW(BlockFacing.computeHash(true, true, false, false, true, false)),
	CShape_ENS(BlockFacing.computeHash(false, false, true, true, false, true)),
	CShape_ENW(BlockFacing.computeHash(false, false, true, false, true, true)),
	CShape_ESW(BlockFacing.computeHash(false, false, false, true, true, true)),
	CShape_EUW(BlockFacing.computeHash(false, true, false, false, true, true)),
	CShape_NSU(BlockFacing.computeHash(false, true, true, true, false, false)),
	CShape_NSW(BlockFacing.computeHash(false, false, true, true, true, false)),
	
	Corner_DEN(BlockFacing.computeHash(true, false, true, false, false, true)),
	Corner_DES(BlockFacing.computeHash(true, false, false, true, false, true)),
	Corner_DNW(BlockFacing.computeHash(true, false, true, false, true, false)),
	Corner_DSW(BlockFacing.computeHash(true, false, false, true, true, false)),
	Corner_ENU(BlockFacing.computeHash(false, true, true, false, false, true)),
	Corner_ESU(BlockFacing.computeHash(false, true, false, true, false, true)),
	Corner_NUW(BlockFacing.computeHash(false, true, true, false, true, false)),
	Corner_SUW(BlockFacing.computeHash(false, true, false, true, true, false)),
	
	Misc_DENS(BlockFacing.computeHash(true, false, true, true, false, true)),
	Misc_DENU(BlockFacing.computeHash(true, true, true, false, false, true)),
	Misc_DENW(BlockFacing.computeHash(true, false, true, false, true, true)),
	Misc_DESU(BlockFacing.computeHash(true, true, false, true, false, true)),
	Misc_DESW(BlockFacing.computeHash(true, false, false, true, true, true)),
	Misc_DNSW(BlockFacing.computeHash(true, false, true, true, true, false)),
	Misc_DNUW(BlockFacing.computeHash(true, true, true, false, true, false)),
	Misc_DSUW(BlockFacing.computeHash(true, true, false, true, true, false)),
	Misc_ENSU(BlockFacing.computeHash(false, true, true, true, false, true)),
	Misc_ENUW(BlockFacing.computeHash(false, true, true, false, true, true)),
	Misc_ESUW(BlockFacing.computeHash(false, true, false, true, true, true)),
	Misc_NSUW(BlockFacing.computeHash(false, true, true, true, true, false)),
	
	Pipe_DEUW(BlockFacing.computeHash(true, true, false, false, true, true)),
	Pipe_DNSU(BlockFacing.computeHash(true, true, true, true, false, false)),
	Pipe_ENSW(BlockFacing.computeHash(false, false, true, true, true, true)),
	
	PipeEnd_DENSU(BlockFacing.computeHash(true, true, true, true, false, true)),
	PipeEnd_DENSW(BlockFacing.computeHash(true, false, true, true, true, true)),
	PipeEnd_DENUW(BlockFacing.computeHash(true, true, true, false, true, true)),
	PipeEnd_DESUW(BlockFacing.computeHash(true, true, false, true, true, true)),
	PipeEnd_DNSUW(BlockFacing.computeHash(true, true, true, true, true, false)),
	PipeEnd_ENSUW(BlockFacing.computeHash(false, true, true, true, false, true));
	
	public static final PropertyEnum<PropertyBlockFacing> FACINGS = PropertyEnum.create("facings", PropertyBlockFacing.class);
	
	public static final EnumSet<PropertyBlockFacing> ALL_AND_NONE;
	public static final EnumSet<PropertyBlockFacing> FACES;
	public static final EnumSet<PropertyBlockFacing> ANGLES;
	public static final EnumSet<PropertyBlockFacing> OPPOSITES;
	public static final EnumSet<PropertyBlockFacing> CSHAPES;
	public static final EnumSet<PropertyBlockFacing> CORNERS;
	public static final EnumSet<PropertyBlockFacing> MISCELLANEA;
	public static final EnumSet<PropertyBlockFacing> PIPES;
	public static final EnumSet<PropertyBlockFacing> PIPEENDS;
	
	@Override
	public String getName() {
		
		return _name;
	}
	
	PropertyBlockFacing(byte hash) {
		
		_hash = hash;
		_name = toString().toLowerCase(Locale.ROOT);
	}
	
	final byte _hash;
	private final String _name;
	
	static {
		ALL_AND_NONE = EnumSet.of(All, None);
		FACES = EnumSet.of(Face_D, Face_E, Face_N, Face_S, Face_U, Face_W);
		ANGLES = EnumSet.of(Angle_DE, Angle_DN, Angle_DS, Angle_DW, Angle_EN, Angle_ES, Angle_EU, Angle_NU, Angle_NW, Angle_SU, Angle_SW, Angle_UW);
		OPPOSITES = EnumSet.of(Opposite_DU, Opposite_EW, Opposite_NS);
		CSHAPES = EnumSet.of(CShape_DEU, CShape_DEW, CShape_DNS, CShape_DNU, CShape_DSU, CShape_DUW, CShape_ENS, CShape_ENW, CShape_ESW, CShape_EUW, CShape_NSU, CShape_NSW);
		CORNERS = EnumSet.of(Corner_DEN, Corner_DES, Corner_DNW, Corner_DSW, Corner_ENU, Corner_ESU, Corner_NUW, Corner_SUW);
		MISCELLANEA = EnumSet.of(Misc_DENS, Misc_DENU, Misc_DENW, Misc_DESU, Misc_DESW, Misc_DNSW, Misc_DNUW, Misc_DSUW, Misc_ENSU, Misc_ENUW, Misc_ESUW, Misc_NSUW);
		PIPES = EnumSet.of(Pipe_DEUW, Pipe_DNSU, Pipe_ENSW);
		PIPEENDS = EnumSet.of(PipeEnd_DENSU, PipeEnd_DENSW, PipeEnd_DENUW, PipeEnd_DESUW, PipeEnd_DNSUW, PipeEnd_ENSUW);
	}
	
}
