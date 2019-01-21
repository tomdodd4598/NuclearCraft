package nc.multiblock;


import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.util.IStringSerializable;

import java.util.EnumSet;

public enum PropertyBlockFacings implements IStringSerializable {

    None(BlockFacings.computeHash(false, false, false, false, false, false)),
    All(BlockFacings.computeHash(true, true, true, true, true, true)),

    Face_D(BlockFacings.computeHash(true, false, false, false, false, false)),
    Face_E(BlockFacings.computeHash(false, false, false, false, false, true)),
    Face_N(BlockFacings.computeHash(false, false, true, false, false, false)),
    Face_S(BlockFacings.computeHash(false, false, false, true, false, false)),
    Face_U(BlockFacings.computeHash(false, true, false, false, false, false)),
    Face_W(BlockFacings.computeHash(false, false, false, false, true, false)),

    Angle_DE(BlockFacings.computeHash(true, false, false, false, false, true)),
    Angle_DN(BlockFacings.computeHash(true, false, true, false, false, false)),
    Angle_DS(BlockFacings.computeHash(true, false, false, true, false, false)),
    Angle_DW(BlockFacings.computeHash(true, false, false, false, true, false)),
    Angle_EN(BlockFacings.computeHash(false, false, true, false, false, true)),
    Angle_ES(BlockFacings.computeHash(false, false, false, true, false, true)),
    Angle_EU(BlockFacings.computeHash(false, true, false, false, false, true)),
    Angle_NU(BlockFacings.computeHash(false, true, true, false, false, false)),
    Angle_NW(BlockFacings.computeHash(false, false, true, false, true, false)),
    Angle_SU(BlockFacings.computeHash(false, true, false, true, false, false)),
    Angle_SW(BlockFacings.computeHash(false, false, false, true, true, false)),
    Angle_UW(BlockFacings.computeHash(false, true, false, false, true, false)),

    Opposite_DU(BlockFacings.computeHash(true, true, false, false, false, false)),
    Opposite_EW(BlockFacings.computeHash(false, false, false, false, true, true)),
    Opposite_NS(BlockFacings.computeHash(false, false, true, true, false, false)),

    CShape_DEU(BlockFacings.computeHash(true, true, false, false, false, true)),
    CShape_DEW(BlockFacings.computeHash(true, false, false, false, true, true)),
    CShape_DNS(BlockFacings.computeHash(true, false, true, true, false, false)),
    CShape_DNU(BlockFacings.computeHash(true, true, true, false, false, false)),
    CShape_DSU(BlockFacings.computeHash(true, true, false, true, false, false)),
    CShape_DUW(BlockFacings.computeHash(true, true, false, false, true, false)),
    CShape_ENS(BlockFacings.computeHash(false, false, true, true, false, true)),
    CShape_ENW(BlockFacings.computeHash(false, false, true, false, true, true)),
    CShape_ESW(BlockFacings.computeHash(false, false, false, true, true, true)),
    CShape_EUW(BlockFacings.computeHash(false, true, false, false, true, true)),
    CShape_NSU(BlockFacings.computeHash(false, true, true, true, false, false)),
    CShape_NSW(BlockFacings.computeHash(false, false, true, true, true, false)),

    Corner_DEN(BlockFacings.computeHash(true, false, true, false, false, true)),
    Corner_DES(BlockFacings.computeHash(true, false, false, true, false, true)),
    Corner_DNW(BlockFacings.computeHash(true, false, true, false, true, false)),
    Corner_DSW(BlockFacings.computeHash(true, false, false, true, true, false)),
    Corner_ENU(BlockFacings.computeHash(false, true, true, false, false, true)),
    Corner_ESU(BlockFacings.computeHash(false, true, false, true, false, true)),
    Corner_NUW(BlockFacings.computeHash(false, true, true, false, true, false)),
    Corner_SUW(BlockFacings.computeHash(false, true, false, true, true, false)),

    Misc_DENS(BlockFacings.computeHash(true, false, true, true, false, true)),
    Misc_DENU(BlockFacings.computeHash(true, true, true, false, false, true)),
    Misc_DENW(BlockFacings.computeHash(true, false, true, false, true, true)),
    Misc_DESU(BlockFacings.computeHash(true, true, false, true, false, true)),
    Misc_DESW(BlockFacings.computeHash(true, false, false, true, true, true)),
    Misc_DNSW(BlockFacings.computeHash(true, false, true, true, true, false)),
    Misc_DNUW(BlockFacings.computeHash(true, true, true, false, true, false)),
    Misc_DSUW(BlockFacings.computeHash(true, true, false, true, true, false)),
    Misc_ENSU(BlockFacings.computeHash(false, true, true, true, false, true)),
    Misc_ENUW(BlockFacings.computeHash(false, true, true, false, true, true)),
    Misc_ESUW(BlockFacings.computeHash(false, true, false, true, true, true)),
    Misc_NSUW(BlockFacings.computeHash(false, true, true, true, true, false)),

    Pipe_DEUW(BlockFacings.computeHash(true, true, false, false, true, true)),
    Pipe_DNSU(BlockFacings.computeHash(true, true, true, true, false, false)),
    Pipe_ENSW(BlockFacings.computeHash(false, false, true, true, true, true)),

    PipeEnd_DENSU(BlockFacings.computeHash(true, true, true, true, false, true)),
    PipeEnd_DENSW(BlockFacings.computeHash(true, false, true, true, true, true)),
    PipeEnd_DENUW(BlockFacings.computeHash(true, true, true, false, true, true)),
    PipeEnd_DESUW(BlockFacings.computeHash(true, true, false, true, true, true)),
    PipeEnd_DNSUW(BlockFacings.computeHash(true, true, true, true, true, false)),
    PipeEnd_ENSUW(BlockFacings.computeHash(false, true, true, true, false, true));


    public static final PropertyEnum FACINGS = PropertyEnum.create("facings", PropertyBlockFacings.class);

    public static final EnumSet<PropertyBlockFacings> ALL_AND_NONE;
    public static final EnumSet<PropertyBlockFacings> FACES;
    public static final EnumSet<PropertyBlockFacings> ANGLES;
    public static final EnumSet<PropertyBlockFacings> OPPOSITES;
    public static final EnumSet<PropertyBlockFacings> CSHAPES;
    public static final EnumSet<PropertyBlockFacings> CORNERS;
    public static final EnumSet<PropertyBlockFacings> MISCELLANEA;
    public static final EnumSet<PropertyBlockFacings> PIPES;
    public static final EnumSet<PropertyBlockFacings> PIPEENDS;


    @Override
    public String getName() {

        return this._name;
    }

    PropertyBlockFacings(byte hash) {

        this._hash = hash;
        this._name = this.toString().toLowerCase();
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