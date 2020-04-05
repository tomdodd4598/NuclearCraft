package nc.multiblock.heatExchanger.tile;

import nc.multiblock.heatExchanger.HeatExchanger;
import nc.multiblock.tile.ILogicMultiblockController;

public interface IHeatExchangerController extends IHeatExchangerPart, ILogicMultiblockController<HeatExchanger> {
	
	public void updateBlockState(boolean isActive);
}
