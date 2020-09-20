package nc.multiblock.turbine.tile;

import nc.multiblock.tile.ILogicMultiblockController;
import nc.multiblock.turbine.Turbine;

public interface ITurbineController extends ITurbinePart, ILogicMultiblockController<Turbine> {
	
	public boolean isRenderer();
	
	public void setIsRenderer(boolean isRenderer);
}
