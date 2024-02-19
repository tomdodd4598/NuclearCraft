package nc.tile;

import java.util.List;

import nc.util.StreamHelper;

public class TileContainerInfoHelper {
	
	public static int[] standardSlot(int x, int y) {
		return new int[] {x, y, 16, 16};
	}
	
	public static int[] bigSlot(int x, int y) {
		return new int[] {x, y, 24, 24};
	}
	
	public static List<int[]> stackXYList(List<int[]> slotXYWHList) {
		return StreamHelper.map(slotXYWHList, TileContainerInfoHelper::stackXY);
	}
	
	public static int[] stackXY(int[] slotXYWH) {
		return new int[] {slotXYWH[0] + (16 - slotXYWH[2]) / 2, slotXYWH[1] + (16 - slotXYWH[3]) / 2};
	}
}
