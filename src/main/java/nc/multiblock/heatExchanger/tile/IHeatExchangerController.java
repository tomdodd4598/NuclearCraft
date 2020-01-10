package nc.multiblock.heatExchanger.tile;

import nc.multiblock.ILogicMultiblockController;
import nc.multiblock.heatExchanger.HeatExchanger;

public interface IHeatExchangerController extends IHeatExchangerPart, ILogicMultiblockController<HeatExchanger> {
	
	public void updateBlockState(boolean isActive);
}
