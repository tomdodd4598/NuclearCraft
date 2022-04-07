package nc.util;

import java.util.List;

import javax.annotation.Nonnull;

import com.google.common.collect.Lists;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class PosHelper {
	
	public static final BlockPos DEFAULT_NON = new BlockPos(0, -1, 0);
	
	// Horizontals
	
	private static final EnumFacing[] HORIZONTALS_X = new EnumFacing[] {EnumFacing.DOWN, EnumFacing.UP, EnumFacing.NORTH, EnumFacing.SOUTH};
	private static final EnumFacing[] HORIZONTALS_Y = EnumFacing.HORIZONTALS;
	private static final EnumFacing[] HORIZONTALS_Z = new EnumFacing[] {EnumFacing.DOWN, EnumFacing.UP, EnumFacing.WEST, EnumFacing.EAST};
	
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
	
	public static final EnumFacing[][] VERTEX_DIRS = new EnumFacing[][] {new EnumFacing[] {EnumFacing.DOWN, EnumFacing.NORTH, EnumFacing.WEST}, new EnumFacing[] {EnumFacing.DOWN, EnumFacing.NORTH, EnumFacing.EAST}, new EnumFacing[] {EnumFacing.DOWN, EnumFacing.SOUTH, EnumFacing.WEST}, new EnumFacing[] {EnumFacing.DOWN, EnumFacing.SOUTH, EnumFacing.EAST}, new EnumFacing[] {EnumFacing.UP, EnumFacing.NORTH, EnumFacing.WEST}, new EnumFacing[] {EnumFacing.UP, EnumFacing.NORTH, EnumFacing.EAST}, new EnumFacing[] {EnumFacing.UP, EnumFacing.SOUTH, EnumFacing.WEST}, new EnumFacing[] {EnumFacing.UP, EnumFacing.SOUTH, EnumFacing.EAST}
	};
	
	// Edges
	
	public static final EnumFacing[][] EDGE_DIRS = new EnumFacing[][] {new EnumFacing[] {EnumFacing.DOWN, EnumFacing.NORTH}, new EnumFacing[] {EnumFacing.DOWN, EnumFacing.SOUTH}, new EnumFacing[] {EnumFacing.DOWN, EnumFacing.WEST}, new EnumFacing[] {EnumFacing.DOWN, EnumFacing.EAST}, new EnumFacing[] {EnumFacing.UP, EnumFacing.NORTH}, new EnumFacing[] {EnumFacing.UP, EnumFacing.SOUTH}, new EnumFacing[] {EnumFacing.UP, EnumFacing.WEST}, new EnumFacing[] {EnumFacing.UP, EnumFacing.EAST}, new EnumFacing[] {EnumFacing.NORTH, EnumFacing.WEST}, new EnumFacing[] {EnumFacing.NORTH, EnumFacing.WEST}, new EnumFacing[] {EnumFacing.SOUTH, EnumFacing.EAST}, new EnumFacing[] {EnumFacing.SOUTH, EnumFacing.EAST}
	};
	
	// Planes
	
	public static final EnumFacing[][] PLANE_DIRS = new EnumFacing[][] {new EnumFacing[] {EnumFacing.DOWN, EnumFacing.UP, EnumFacing.NORTH, EnumFacing.SOUTH}, new EnumFacing[] {EnumFacing.NORTH, EnumFacing.SOUTH, EnumFacing.WEST, EnumFacing.EAST}, new EnumFacing[] {EnumFacing.WEST, EnumFacing.EAST, EnumFacing.DOWN, EnumFacing.UP}
	};
	
	// Axes
	
	public static final EnumFacing.Axis[] AXES = new EnumFacing.Axis[] {EnumFacing.Axis.X, EnumFacing.Axis.Y, EnumFacing.Axis.Z};
	
	public static int getAxisIndex(@Nonnull EnumFacing.Axis axis) {
		return axis == EnumFacing.Axis.X ? 0 : axis == EnumFacing.Axis.Y ? 1 : 2;
	}
	
	public static final EnumFacing.AxisDirection[] AXIS_DIRS = new EnumFacing.AxisDirection[] {EnumFacing.AxisDirection.POSITIVE, EnumFacing.AxisDirection.NEGATIVE};
	
	public static int getAxisDirIndex(@Nonnull EnumFacing.AxisDirection dir) {
		return dir == EnumFacing.AxisDirection.POSITIVE ? 0 : 1;
	}
}
