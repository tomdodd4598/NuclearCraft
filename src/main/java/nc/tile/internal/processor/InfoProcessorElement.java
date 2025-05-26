package nc.tile.internal.processor;

import nc.network.tile.processor.ProcessorUpdatePacket;
import nc.recipe.*;
import nc.tile.processor.IProcessor;
import nc.tile.processor.info.ProcessorContainerInfo;
import net.minecraft.tileentity.TileEntity;

public abstract class InfoProcessorElement<TILE extends TileEntity & IProcessor<TILE, PACKET, INFO>, PACKET extends ProcessorUpdatePacket, INFO extends ProcessorContainerInfo<TILE, PACKET, INFO>> extends AbstractProcessorElement {
	
	public INFO info = null;
	
	public InfoProcessorElement(INFO info) {
		this(true, info);
	}
	
	public InfoProcessorElement(boolean baseInit, INFO info) {
		super(false);
		
		if (baseInit) {
			this.info = info;
			baseConstructorInit();
		}
	}
	
	@Override
	public BasicRecipeHandler getRecipeHandler() {
		return info.getRecipeHandler();
	}
	
	@Override
	public boolean getConsumesInputs() {
		return info.consumesInputs;
	}
	
	@Override
	public int getInventoryStackLimit() {
		return 64;
	}
	
	@Override
	public int getInputTankCapacity() {
		return info.inputTankCapacity;
	}
	
	@Override
	public boolean getLosesProgress() {
		return info.losesProgress;
	}
	
	@Override
	public int getItemInputSize() {
		return info.itemInputSize;
	}
	
	@Override
	public int getFluidInputSize() {
		return info.fluidInputSize;
	}
	
	@Override
	public int getItemOutputSize() {
		return info.itemOutputSize;
	}
	
	@Override
	public int getFluidOutputSize() {
		return info.fluidOutputSize;
	}
	
	@Override
	public int getItemInputSlot(int index) {
		return info.itemInputSlots[index];
	}
	
	@Override
	public int getFluidInputTank(int index) {
		return info.fluidInputTanks[index];
	}
	
	@Override
	public int getItemOutputSlot(int index) {
		return info.itemOutputSlots[index];
	}
	
	@Override
	public int getFluidOutputTank(int index) {
		return info.fluidOutputTanks[index];
	}
}
