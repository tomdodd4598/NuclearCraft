package nc.multiblock.heatExchanger.tile;

import nc.multiblock.heatExchanger.HeatExchanger;
import nc.multiblock.tile.ILogicMultiblockController;
import nc.network.multiblock.HeatExchangerUpdatePacket;

public interface IHeatExchangerController<CONTROLLER extends IHeatExchangerController<CONTROLLER>> extends IHeatExchangerPart, ILogicMultiblockController<HeatExchanger, IHeatExchangerPart, HeatExchangerUpdatePacket, CONTROLLER> {
	
}
