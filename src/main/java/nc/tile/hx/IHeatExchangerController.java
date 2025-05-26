package nc.tile.hx;

import nc.multiblock.hx.HeatExchanger;
import nc.network.multiblock.HeatExchangerUpdatePacket;
import nc.tile.TileContainerInfo;
import nc.tile.multiblock.ILogicMultiblockController;
import net.minecraft.tileentity.TileEntity;

public interface IHeatExchangerController<CONTROLLER extends TileEntity & IHeatExchangerController<CONTROLLER>> extends IHeatExchangerPart, ILogicMultiblockController<HeatExchanger, IHeatExchangerPart, HeatExchangerUpdatePacket, CONTROLLER, TileContainerInfo<CONTROLLER>> {
	
	boolean isRenderer();
	
	void setIsRenderer(boolean isRenderer);
}
