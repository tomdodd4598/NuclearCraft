package nc.multiblock.heatExchanger.tile;

import nc.multiblock.ILogicMultiblockController;
import nc.multiblock.heatExchanger.HeatExchanger;
import nc.multiblock.heatExchanger.HeatExchangerLogic;

public interface IHeatExchangerController extends IHeatExchangerPart, ILogicMultiblockController<HeatExchanger, HeatExchangerLogic> {
	
	public void updateBlockState(boolean isActive);
}
