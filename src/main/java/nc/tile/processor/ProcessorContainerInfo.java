package nc.tile.processor;

import nc.container.ContainerFunction;
import nc.gui.GuiFunction;
import nc.handler.GuiHandler;
import nc.tile.TileContainerInfo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

public class ProcessorContainerInfo<TILE extends TileEntity> extends TileContainerInfo<TILE> {
	
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
	
	public final int playerInventoryX;
	public final int playerInventoryY;
	
	public ProcessorContainerInfo(String name, ContainerFunction<TILE> containerFunction, GuiFunction<TILE> guiFunction, ContainerFunction<TILE> configContainerFunction, GuiFunction<TILE> configGuiFunction, int itemInputSize, int fluidInputSize, int itemOutputSize, int fluidOutputSize, int inputTankCapacity, int outputTankCapacity, double defaultProcessTime, double defaultProcessPower, boolean consumesInputs, boolean losesProgress, int playerInventoryX, int playerInventoryY) {
		super(name, containerFunction, guiFunction);
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
		this.playerInventoryX = playerInventoryX;
		this.playerInventoryY = playerInventoryY;
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
}
