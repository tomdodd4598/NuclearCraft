package nc.tile.processor.info.builder;

import java.util.*;

import com.google.common.collect.Lists;

import nc.util.MinMax.MinMaxInt;

public abstract class ContainerInfoBuilder<BUILDER extends ContainerInfoBuilder<BUILDER>> {
	
	public final String modId;
	public final String name;
	
	protected int[] guiWH = new int[] {176, 166};
	
	protected List<int[]> itemInputGuiXYWH = new ArrayList<>();
	protected List<int[]> fluidInputGuiXYWH = new ArrayList<>();
	protected List<int[]> itemOutputGuiXYWH = new ArrayList<>();
	protected List<int[]> fluidOutputGuiXYWH = new ArrayList<>();
	
	protected int[] playerGuiXY = new int[] {8, 84};
	
	protected int[] progressBarGuiXYWHUV = new int[] {74, 35, 37, 16, 176, 3};
	protected int[] energyBarGuiXYWHUV = new int[] {8, 6, 16, 74, 176, 90};
	
	protected int[] machineConfigGuiXY = new int[] {27, 63};
	protected int[] redstoneControlGuiXY = new int[] {47, 63};
	
	protected boolean jeiCategoryEnabled = true;
	
	protected String jeiCategoryUid;
	protected String jeiTitle;
	protected String jeiTexture;
	
	protected int[] jeiBackgroundXYWH = new int[] {51, 30, 86, 26};
	protected int[] jeiTooltipXYWH = new int[] {73, 34, 38, 18};
	protected int[] jeiClickAreaXYWH = new int[] {73, 34, 38, 18};
	
	protected ContainerInfoBuilder(String modId, String name) {
		this.modId = modId;
		this.name = name;
		
		jeiCategoryUid = modId + "_" + name;
		jeiTitle = "tile." + modId + "." + name + ".name";
		jeiTexture = modId + ":textures/gui/container/" + name + ".png";
	}
	
	@SuppressWarnings("unchecked")
	public BUILDER getThis() {
		return (BUILDER) this;
	}
	
	public BUILDER setGuiWH(int w, int h) {
		guiWH = new int[] {w, h};
		return getThis();
	}
	
	public BUILDER setItemInputSlots(int[]... slots) {
		itemInputGuiXYWH = Lists.newArrayList(slots);
		autoJeiBackgroundXY();
		return getThis();
	}
	
	public BUILDER setFluidInputSlots(int[]... slots) {
		fluidInputGuiXYWH = Lists.newArrayList(slots);
		autoJeiBackgroundXY();
		return getThis();
	}
	
	public BUILDER setItemOutputSlots(int[]... slots) {
		itemOutputGuiXYWH = Lists.newArrayList(slots);
		autoJeiBackgroundXY();
		return getThis();
	}
	
	public BUILDER setFluidOutputSlots(int[]... slots) {
		fluidOutputGuiXYWH = Lists.newArrayList(slots);
		autoJeiBackgroundXY();
		return getThis();
	}
	
	public BUILDER setPlayerGuiXY(int x, int y) {
		playerGuiXY = new int[] {x, y};
		return getThis();
	}
	
	public BUILDER setProgressBarGuiXYWHUV(int x, int y, int w, int h, int u, int v) {
		progressBarGuiXYWHUV = new int[] {x, y, w, h, u, v};
		jeiTooltipXYWH = new int[] {x - 1, y - 1, w + 1, h + 2};
		jeiClickAreaXYWH = new int[] {x - 1, y - 1, w + 1, h + 2};
		return getThis();
	}
	
	public BUILDER setEnergyBarGuiXYWHUV(int x, int y, int w, int h, int u, int v) {
		energyBarGuiXYWHUV = new int[] {x, y, w, h, u, v};
		return getThis();
	}
	
	public BUILDER setMachineConfigGuiXY(int x, int y) {
		machineConfigGuiXY = new int[] {x, y};
		return getThis();
	}
	
	public BUILDER setRedstoneControlGuiXY(int x, int y) {
		redstoneControlGuiXY = new int[] {x, y};
		return getThis();
	}
	
	public BUILDER setJeiCategoryEnabled(boolean jeiCategoryEnabled) {
		this.jeiCategoryEnabled = jeiCategoryEnabled;
		return getThis();
	}
	
	public BUILDER setJeiCategoryUid(String jeiCategoryUid) {
		this.jeiCategoryUid = jeiCategoryUid;
		return getThis();
	}
	
	public BUILDER setJeiTitle(String jeiTitle) {
		this.jeiTitle = jeiTitle;
		return getThis();
	}
	
	public BUILDER setJeiTexture(String jeiTexture) {
		this.jeiTexture = jeiTexture;
		return getThis();
	}
	
	public BUILDER setJeiBackgroundXYWH(int x, int y, int w, int h) {
		jeiBackgroundXYWH = new int[] {x, y, w, h};
		return getThis();
	}
	
	public BUILDER setJeiTooltipXYWH(int x, int y, int w, int h) {
		jeiTooltipXYWH = new int[] {x, y, w, h};
		return getThis();
	}
	
	public BUILDER setJeiClickAreaXYWH(int x, int y, int w, int h) {
		jeiClickAreaXYWH = new int[] {x, y, w, h};
		return getThis();
	}
	
	public BUILDER standardExtend(int x, int y) {
		guiWH[0] += x;
		guiWH[1] += y;
		
		playerGuiXY[0] += x;
		playerGuiXY[1] += y;
		
		energyBarGuiXYWHUV[3] += y;
		
		machineConfigGuiXY[0] += x;
		machineConfigGuiXY[1] += y;
		
		redstoneControlGuiXY[0] += x;
		redstoneControlGuiXY[1] += y;
		
		return getThis();
	}

	public BUILDER setStandardJeiAlternateTitle() {
		jeiTitle = modId + "." + name + ".jei_name";
		return getThis();
	}
	
	public BUILDER setStandardJeiAlternateTexture() {
		jeiTexture = modId + ":textures/gui/container/" + name + "_jei.png";
		return getThis();
	}
	
	public void autoJeiBackgroundXY() {
		MinMaxInt minMaxCenterX = new MinMaxInt(), minMaxCenterY = new MinMaxInt();
		for (List<int[]> xywhList : Arrays.asList(itemInputGuiXYWH, fluidInputGuiXYWH, itemOutputGuiXYWH, fluidOutputGuiXYWH)) {
			for (int[] xywh : xywhList) {
				minMaxCenterX.update(xywh[0] - 2);
				minMaxCenterY.update(xywh[1] - 2);
				minMaxCenterX.update(xywh[0] + xywh[2] + 2);
				minMaxCenterY.update(xywh[1] + xywh[3] + 2);
			}
		}
		
		jeiBackgroundXYWH = new int[] {minMaxCenterX.getMin(), minMaxCenterY.getMin(), minMaxCenterX.getDiff(), minMaxCenterY.getDiff()};
	}
	
	public BUILDER disableProgressBar() {
		progressBarGuiXYWHUV = new int[] {-1, -1, -1, -1, -1, -1};
		jeiTooltipXYWH = new int[] {-1, -1, -1, -1};
		jeiClickAreaXYWH = new int[] {-1, -1, -1, -1};
		return getThis();
	}
}
