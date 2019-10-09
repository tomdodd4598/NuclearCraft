package nc.multiblock.heatExchanger.tile;

import nc.multiblock.IMultiblockPart;
import nc.multiblock.heatExchanger.HeatExchanger;

public interface IHeatExchangerController extends IMultiblockPart<HeatExchanger> {
	
	public void updateBlockState(boolean isActive);
}
