package nc.tile.turbine;

import nc.multiblock.turbine.Turbine;
import nc.network.multiblock.TurbineUpdatePacket;
import nc.tile.multiblock.ILogicMultiblockController;

public interface ITurbineController<CONTROLLER extends ITurbineController<CONTROLLER>> extends ITurbinePart, ILogicMultiblockController<Turbine, ITurbinePart, TurbineUpdatePacket, CONTROLLER> {
	
	boolean isRenderer();
	
	void setIsRenderer(boolean isRenderer);
}
