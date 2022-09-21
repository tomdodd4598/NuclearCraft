package nc.tile.processor;

import java.util.List;

import nc.container.ContainerFunction;
import nc.gui.GuiFunction;
import nc.handler.GuiHandler;
import nc.recipe.*;
import nc.tile.TileContainerInfo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

public class ProcessorContainerInfo<TILE extends TileEntity & IProcessor<TILE, INFO>, INFO extends ProcessorContainerInfo<TILE, INFO>> extends TileContainerInfo<TILE> {
	
	protected final ContainerFunction<TILE> configContainerFunction;
	protected final GuiFunction<TILE> configGuiFunction;
	
	public final int itemInputSize;
	public final int fluidInputSize;
	public final int itemOutputSize;
	public final int fluidOutputSize;
	
	public final int inputTankCapacity;
	public final int outputTankCapacity;
	
	public final double defaultProcessTime;
	public final double defaultProcessPower;
	
	public final boolean consumesInputs;
	public final boolean losesProgress;
	
	public final int guiSizeX;
	public final int guiSizeY;
	
	public final List<int[]> itemInputGuiXYWH;
	public final List<int[]> fluidInputGuiXYWH;
	public final List<int[]> itemOutputGuiXYWH;
	public final List<int[]> fluidOutputGuiXYWH;
	
	public final int playerGuiX;
	public final int playerGuiY;
	
	public final int jeiBackgroundX;
	public final int jeiBackgroundY;
	
	public final int jeiTooltipX;
	public final int jeiTooltipY;
	public final int jeiTooltipWidth;
	public final int jeiTooltipHeight;
	
	public ProcessorContainerInfo(String modId, String name, ContainerFunction<TILE> containerFunction, GuiFunction<TILE> guiFunction, ContainerFunction<TILE> configContainerFunction, GuiFunction<TILE> configGuiFunction, int itemInputSize, int fluidInputSize, int itemOutputSize, int fluidOutputSize, int inputTankCapacity, int outputTankCapacity, double defaultProcessTime, double defaultProcessPower, boolean consumesInputs, boolean losesProgress, int guiSizeX, int guiSizeY, List<int[]> itemInputGuiXYWH, List<int[]> fluidInputGuiXYWH, List<int[]> itemOutputGuiXYWH, List<int[]> fluidOutputGuiXYWH, int playerGuiX, int playerGuiY, int jeiBackgroundX, int jeiBackgroundY, int jeiTooltipX, int jeiTooltipY, int jeiTooltipWidth, int jeiTooltipHeight) {
		super(modId, name, containerFunction, guiFunction);
		this.configContainerFunction = configContainerFunction;
		this.configGuiFunction = configGuiFunction;
		this.itemInputSize = itemInputSize;
		this.fluidInputSize = fluidInputSize;
		this.itemOutputSize = itemOutputSize;
		this.fluidOutputSize = fluidOutputSize;
		this.inputTankCapacity = inputTankCapacity;
		this.outputTankCapacity = outputTankCapacity;
		this.defaultProcessTime = defaultProcessTime;
		this.defaultProcessPower = defaultProcessPower;
		this.consumesInputs = consumesInputs;
		this.losesProgress = losesProgress;
		this.guiSizeX = guiSizeX;
		this.guiSizeY = guiSizeY;
		this.itemInputGuiXYWH = itemInputGuiXYWH;
		this.fluidInputGuiXYWH = fluidInputGuiXYWH;
		this.itemOutputGuiXYWH = itemOutputGuiXYWH;
		this.fluidOutputGuiXYWH = fluidOutputGuiXYWH;
		this.playerGuiX = playerGuiX;
		this.playerGuiY = playerGuiY;
		this.jeiBackgroundX = jeiBackgroundX;
		this.jeiBackgroundY = jeiBackgroundY;
		this.jeiTooltipX = jeiTooltipX;
		this.jeiTooltipY = jeiTooltipY;
		this.jeiTooltipWidth = jeiTooltipWidth;
		this.jeiTooltipHeight = jeiTooltipHeight;
	}
	
	public BasicRecipeHandler getRecipeHandler() {
		return NCRecipes.getHandler(name);
	}
	
	@Override
	public Object getNewContainer(int ID, EntityPlayer player, TILE tile) {
		return (ID == GuiHandler.getGuiId(name) ? containerFunction : configContainerFunction).apply(player, tile);
	}
	
	@Override
	public Object getNewGui(int ID, EntityPlayer player, TILE tile) {
		return (ID == GuiHandler.getGuiId(name) ? guiFunction : configGuiFunction).apply(player, tile);
	}
	
	public int getInventorySize() {
		return itemInputSize + itemOutputSize;
	}
	
	public int getCombinedInventorySize() {
		return 36 + getInventorySize();
	}
	
	public int[] getItemInputStackXY(int i) {
		int[] slotXYWH = itemInputGuiXYWH.get(i);
		return new int[] {slotXYWH[0] + (16 - slotXYWH[2]) / 2, slotXYWH[1] + (16 - slotXYWH[3]) / 2};
	}
	
	public int[] getItemOutputStackXY(int i) {
		int[] slotXYWH = itemOutputGuiXYWH.get(i);
		return new int[] {slotXYWH[0] + (16 - slotXYWH[2]) / 2, slotXYWH[1] + (16 - slotXYWH[3]) / 2};
	}
}
