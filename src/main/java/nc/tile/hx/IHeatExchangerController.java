package nc.tile.hx;

import nc.multiblock.hx.HeatExchanger;
import nc.network.multiblock.HeatExchangerUpdatePacket;
import nc.tile.multiblock.ILogicMultiblockController;

public interface IHeatExchangerController<CONTROLLER extends IHeatExchangerController<CONTROLLER>> extends IHeatExchangerPart, ILogicMultiblockController<HeatExchanger, IHeatExchangerPart, HeatExchangerUpdatePacket, CONTROLLER> {
	
}
