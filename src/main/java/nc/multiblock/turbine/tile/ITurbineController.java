package nc.multiblock.turbine.tile;

import nc.multiblock.ILogicMultiblockController;
import nc.multiblock.turbine.Turbine;
import nc.multiblock.turbine.TurbineLogic;

public interface ITurbineController extends ITurbinePart, ILogicMultiblockController<Turbine, TurbineLogic> {
	
	public void updateBlockState(boolean isActive);
	
	public boolean isRenderer();
	
	public void setIsRenderer(boolean isRenderer);
}
