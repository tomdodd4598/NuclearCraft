package nc.util;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import com.google.common.collect.Lists;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class BlockPosHelper {
	
	public static final BlockPos DEFAULT_NON = new BlockPos(0, -1, 0);
	
	private final BlockPos pos;
	
	public BlockPosHelper(BlockPos pos) {
		this.pos = pos;
	}
	
	public BlockPosHelper(int x, int y, int z) {
		pos = new BlockPos(x, y, z);
	}
	
	public BlockPos[] offsets(EnumFacing[] sides, int dist) {
		BlockPos[] posList = new BlockPos[sides.length];
		for (int i = 0; i < sides.length; i++) posList[i] = pos.offset(sides[i], dist);
		return posList;
	}
	
	private BlockPos position(int x, int y, int z) {
		return new BlockPos(pos.getX() + x, pos.getY() + y, pos.getZ() + z);
	}
	
	// Adjacents
	
	public BlockPos[] adjacents(int dist) {
		return offsets(EnumFacing.VALUES, dist);
	}
	
	public BlockPos[] adjacents() {
		return adjacents(1);
	}
	
	// Horizontals
	
	private static final EnumFacing[] HORIZONTALS_X = new EnumFacing[] {EnumFacing.DOWN, EnumFacing.UP, EnumFacing.NORTH, EnumFacing.SOUTH};
	private static final EnumFacing[] HORIZONTALS_Y = EnumFacing.HORIZONTALS;
	private static final EnumFacing[] HORIZONTALS_Z = new EnumFacing[] {EnumFacing.DOWN, EnumFacing.UP, EnumFacing.WEST, EnumFacing.EAST};
	
	public BlockPos[] horizontalsX(int dist) {
		return offsets(HORIZONTALS_X, dist);
	}
	
	public BlockPos[] horizontalsX() {
		return horizontalsX(1);
	}
	
	public BlockPos[] horizontalsY(int dist) {
		return offsets(HORIZONTALS_Y, dist);
	}
	
	public BlockPos[] horizontalsY() {
		return horizontalsY(1);
	}
	
	public BlockPos[] horizontalsZ(int dist) {
		return offsets(HORIZONTALS_Z, dist);
	}
	
	public BlockPos[] horizontalsZ() {
		return horizontalsZ(1);
	}
	
	public List<BlockPos[]> horizontalsList(int dist) {
		return Lists.newArrayList(horizontalsX(dist), horizontalsY(dist), horizontalsZ(dist));
	}
	
	public List<BlockPos[]> horizontalsList() {
		return horizontalsList(1);
	}
	
	public static EnumFacing[] getHorizontals(EnumFacing dir) {
		switch (dir) {
		case DOWN:
			return HORIZONTALS_Y;
		case UP:
			return HORIZONTALS_Y;
		case NORTH:
			return HORIZONTALS_Z;
		case SOUTH:
			return HORIZONTALS_Z;
		case WEST:
			return HORIZONTALS_X;
		case EAST:
			return HORIZONTALS_X;
		default:
			return HORIZONTALS_Y;
		}
	}
	
	// Axials
	
	private static final EnumFacing[] AXIALS_X = new EnumFacing[] {EnumFacing.WEST, EnumFacing.EAST};
	private static final EnumFacing[] AXIALS_Y = new EnumFacing[] {EnumFacing.DOWN, EnumFacing.UP};
	private static final EnumFacing[] AXIALS_Z = new EnumFacing[] {EnumFacing.NORTH, EnumFacing.SOUTH};
	
	public BlockPos[] axialsX(int dist) {
		return offsets(AXIALS_X, dist);
	}
	
	public BlockPos[] axialsX() {
		return axialsX(1);
	}
	
	public BlockPos[] axialsY(int dist) {
		return offsets(AXIALS_Y, dist);
	}
	
	public BlockPos[] axialsY() {
		return axialsY(1);
	}
	
	public BlockPos[] axialsZ(int dist) {
		return offsets(AXIALS_Z, dist);
	}
	
	public BlockPos[] axialsZ() {
		return axialsZ(1);
	}
	
	public List<BlockPos[]> axialsList(int dist) {
		return Lists.newArrayList(axialsX(dist), axialsY(dist), axialsZ(dist));
	}
	
	public List<BlockPos[]> axialsList() {
		return axialsList(1);
	}
	
	public static List<EnumFacing[]> axialDirsList() {
		return Lists.newArrayList(AXIALS_X, AXIALS_Y, AXIALS_Z);
	}
	
	public static EnumFacing[] getAxialDirs(EnumFacing dir) {
		switch (dir) {
		case DOWN:
			return AXIALS_Y;
		case UP:
			return AXIALS_Y;
		case NORTH:
			return AXIALS_Z;
		case SOUTH:
			return AXIALS_Z;
		case WEST:
			return AXIALS_X;
		case EAST:
			return AXIALS_X;
		default:
			return AXIALS_Y;
		}
	}
	
	// Vertices
	
	private static final EnumFacing[] VERTEX_DNW = new EnumFacing[] {EnumFacing.DOWN, EnumFacing.NORTH, EnumFacing.WEST};
	private static final EnumFacing[] VERTEX_DNE = new EnumFacing[] {EnumFacing.DOWN, EnumFacing.NORTH, EnumFacing.EAST};
	private static final EnumFacing[] VERTEX_DSW = new EnumFacing[] {EnumFacing.DOWN, EnumFacing.SOUTH, EnumFacing.WEST};
	private static final EnumFacing[] VERTEX_DSE = new EnumFacing[] {EnumFacing.DOWN, EnumFacing.SOUTH, EnumFacing.EAST};
	private static final EnumFacing[] VERTEX_UNW = new EnumFacing[] {EnumFacing.UP, EnumFacing.NORTH, EnumFacing.WEST};
	private static final EnumFacing[] VERTEX_UNE = new EnumFacing[] {EnumFacing.UP, EnumFacing.NORTH, EnumFacing.EAST};
	private static final EnumFacing[] VERTEX_USW = new EnumFacing[] {EnumFacing.UP, EnumFacing.SOUTH, EnumFacing.WEST};
	private static final EnumFacing[] VERTEX_USE = new EnumFacing[] {EnumFacing.UP, EnumFacing.SOUTH, EnumFacing.EAST};
	
	public BlockPos[] vertexDNW() {
		return offsets(VERTEX_DNW, 1);
	}
	
	public BlockPos[] vertexDNE() {
		return offsets(VERTEX_DNE, 1);
	}
	
	public BlockPos[] vertexDSW() {
		return offsets(VERTEX_DSW, 1);
	}
	
	public BlockPos[] vertexDSE() {
		return offsets(VERTEX_DSE, 1);
	}
	
	public BlockPos[] vertexUNW() {
		return offsets(VERTEX_UNW, 1);
	}
	
	public BlockPos[] vertexUNE() {
		return offsets(VERTEX_UNE, 1);
	}
	
	public BlockPos[] vertexUSW() {
		return offsets(VERTEX_USW, 1);
	}
	
	public BlockPos[] vertexUSE() {
		return offsets(VERTEX_USE, 1);
	}
	
	public List<BlockPos[]> vertexList() {
		return Lists.newArrayList(vertexDNW(), vertexDNE(), vertexDSW(), vertexDSE(), vertexUNW(), vertexUNE(), vertexUSW(), vertexUSE());
	}
	
	public static List<EnumFacing[]> vertexDirList() {
		return Lists.newArrayList(VERTEX_DNW, VERTEX_DNE, VERTEX_DSW, VERTEX_DSE, VERTEX_UNW, VERTEX_UNE, VERTEX_USW, VERTEX_USE);
	}
	
	// Ring
	
	public List<BlockPos> squareRing(int radius, int height) {
		List<BlockPos> posList = new ArrayList<>();
		for (int i = -radius; i < radius; i++) {
			posList.add(position(i, height, radius));
			posList.add(position(-i, height, -radius));
			posList.add(position(radius, height, -i));
			posList.add(position(-radius, height, i));
		}
		return posList;
	}
	
	public List<BlockPos> cutoffRing(int radius, int height) {
		List<BlockPos> posList = new ArrayList<>();
		for (int i = -radius + 1; i < radius; i++) {
			posList.add(position(i, height, radius));
			posList.add(position(-i, height, -radius));
			posList.add(position(radius, height, -i));
			posList.add(position(-radius, height, i));
		}
		return posList;
	}
	
	public List<BlockPos> squareTube(int radius, int height) {
		return CollectionHelper.concatenate(squareRing(radius - 1, height), squareRing(radius, height - 1), squareRing(radius, height + 1), squareRing(radius + 1, height));
	}
	
	public List<BlockPos> squareTubeDiagonals(int radius, int height) {
		return CollectionHelper.concatenate(squareRing(radius - 1, height - 1), squareRing(radius - 1, height + 1), squareRing(radius + 1, height - 1), squareRing(radius + 1, height + 1));
	}
	
	public List<BlockPos> squareTubeNotHidden(int radius, int height) {
		return CollectionHelper.concatenate(cutoffRing(radius - 1, height), squareRing(radius, height - 1), squareRing(radius, height + 1), squareRing(radius + 1, height));
	}
	
	// Other
	
	public static final EnumFacing.Axis[] AXES = new EnumFacing.Axis[] {EnumFacing.Axis.X, EnumFacing.Axis.Y, EnumFacing.Axis.Z};
	
	public static int getAxisIndex(@Nonnull EnumFacing.Axis axis) {
		return axis == EnumFacing.Axis.X ? 0 : (axis == EnumFacing.Axis.Y ? 1 : 2);
	}
	
	//public static final EnumFacing.AxisDirection[] AXISDIRS = new EnumFacing.AxisDirection[] {EnumFacing.AxisDirection.POSITIVE, EnumFacing.AxisDirection.NEGATIVE};
	
	public static int getAxisDirIndex(@Nonnull EnumFacing.AxisDirection dir) {
		return dir == EnumFacing.AxisDirection.POSITIVE ? 0 : 1;
	}
	
	public List<BlockPos> cuboid(int x1, int y1, int z1, int x2, int y2, int z2) {
		List<BlockPos> posList = new ArrayList<>();
		for(BlockPos pos : BlockPos.getAllInBox(position(x1, y1, z1), position(x2, y2, z2))) posList.add(pos);
		return posList;
	}
	
	public static String stringPos(BlockPos pos) {
		return "[" + pos.getX() + ", " + pos.getY() + ", " + pos.getZ() + "]";
	}
	
	public static BlockPos[] getOtherPlaneCorners(BlockPos pos1, BlockPos pos2) {
		if (pos1.getX() == pos2.getX()) {
			return new BlockPos[] {new BlockPos(pos1.getX(), pos2.getY(), pos1.getZ()), new BlockPos(pos1.getX(), pos1.getY(), pos2.getZ())};
		}
		else if (pos1.getY() == pos2.getY()) {
			return new BlockPos[] {new BlockPos(pos1.getX(), pos1.getY(), pos2.getZ()), new BlockPos(pos2.getX(), pos1.getY(), pos1.getZ())};
		}
		else if (pos1.getZ() == pos2.getZ()) {
			return new BlockPos[] {new BlockPos(pos2.getX(), pos1.getY(), pos1.getZ()), new BlockPos(pos1.getX(), pos2.getY(), pos1.getZ())};
		}
		else return new BlockPos[] {pos1, pos2};
	}
}
