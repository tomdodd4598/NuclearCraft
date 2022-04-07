package nc.multiblock.turbine.tile;

import nc.multiblock.tile.ILogicMultiblockController;
import nc.multiblock.turbine.Turbine;
import nc.network.multiblock.TurbineUpdatePacket;

public interface ITurbineController<CONTROLLER extends ITurbineController<CONTROLLER>> extends ITurbinePart, ILogicMultiblockController<Turbine, ITurbinePart, TurbineUpdatePacket, CONTROLLER> {
	
	public boolean isRenderer();
	
	public void setIsRenderer(boolean isRenderer);
}
