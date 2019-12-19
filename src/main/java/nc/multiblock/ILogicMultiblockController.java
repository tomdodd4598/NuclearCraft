package nc.multiblock;

public interface ILogicMultiblockController<MULTIBLOCK extends Multiblock, LOGIC extends MultiblockLogic> extends IMultiblockController<MULTIBLOCK> {
	
	public Class<? extends LOGIC> getLogicClass();
	
	public LOGIC createNewLogic(LOGIC oldLogic);
}
